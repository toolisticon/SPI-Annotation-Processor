package io.toolisticon.spiap.processor;

import io.toolisticon.annotationprocessortoolkit.AbstractAnnotationProcessor;
import io.toolisticon.annotationprocessortoolkit.generators.SimpleResourceWriter;
import io.toolisticon.annotationprocessortoolkit.tools.AnnotationUtils;
import io.toolisticon.annotationprocessortoolkit.tools.AnnotationValueUtils;
import io.toolisticon.annotationprocessortoolkit.tools.ElementUtils;
import io.toolisticon.spiap.api.OutOfService;
import io.toolisticon.spiap.api.Service;
import io.toolisticon.spiap.api.Services;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Annotation Processor for {@link Service} and {@link Services}.
 */
public class ServiceProcessor extends AbstractAnnotationProcessor {

    private final static Set<String> SUPPORTED_ANNOTATIONS = createSupportedAnnotationSet(Services.class, Service.class);

    private final static Map<String, SimpleResourceWriter> spiResourceFilePool = new HashMap<String, SimpleResourceWriter>();

    private final static ServiceImplMap serviceImplHashMap = new ServiceImplMap();


    /**
     * Map to store service provider implementations.
     */
    protected static class ServiceImplMap extends HashMap<String, Set<String>> {

        /**
         * Convenience method to add a value.
         * Creates a Set if no entry for passedkey can be found.
         *
         * @param key   the key to look for
         * @param value the value to store in keys set
         * @return the keys set
         */
        public Set<String> put(String key, String value) {
            Set<String> set = this.get(key);
            if (set == null) {
                set = new HashSet<String>();
                this.put(key, set);
            }
            set.add(value);
            return set;
        }

    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return SUPPORTED_ANNOTATIONS;
    }

    @Override
    public boolean processAnnotations(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        if (!roundEnv.processingOver()) {
            processAnnotationsInternally(annotations, roundEnv);
        } else {
            writeConfigurationFiles();
        }

        return false;

    }

    private void processAnnotationsInternally(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        // process Services annotation
        for (Element element : roundEnv.getElementsAnnotatedWith(Services.class)) {

            // Check for OutOfService annotation
            if (checkSkipProcessingBecauseOfOutOfServiceAnnotation(element)) {
                continue;
            }

            Service[] services = element.getAnnotation(Services.class).value();
            AnnotationMirror servicesAnnotationMirror = AnnotationUtils.getAnnotationMirror(element, Services.class);

            for (Service service : services) {

                AnnotationValue annotationValue = AnnotationUtils.getAnnotationValueOfAttribute(servicesAnnotationMirror);
                AnnotationMirror[] serviceAnnotationMirrors = AnnotationValueUtils.getAnnotationValueArray(annotationValue);

                //getMessager().error(element, "GOT ENCAPSULATED SERVICE MIRROR : " + annotationValue.toString());
                for (AnnotationMirror serviceAnnotationMirror : serviceAnnotationMirrors) {
                    processAnnotation(serviceAnnotationMirror, element);
                }
            }

        }


        // process Service annotation
        for (Element element : roundEnv.getElementsAnnotatedWith(Service.class)) {

            // Check for OutOfService annotation
            if (checkSkipProcessingBecauseOfOutOfServiceAnnotation(element)) {
                continue;
            }

            AnnotationMirror serviceAnnotationMirror = AnnotationUtils.getAnnotationMirror(element, Service.class);

            processAnnotation(serviceAnnotationMirror, element);

        }


    }

    private boolean checkSkipProcessingBecauseOfOutOfServiceAnnotation(Element element) {
        // Check for OutOfService annotation
        if (element.getAnnotation(OutOfService.class) != null) {
            // skip processing of element
            getMessager().info(element, ServiceProcessorMessages.INFO_SKIP_ELEMENT_ANNOTATED_AS_OUT_OF_SERVICE.getMessage(), ElementUtils.CastElement.castClass(element).getQualifiedName());
            return true;
        }
        return false;
    }

    private void processAnnotation(AnnotationMirror serviceAnnotation, Element annotatedElement) {

        // check if it is placed on Class
        if (!ElementUtils.CheckKindOfElement.isClass(annotatedElement)) {
            getMessager().error(annotatedElement, ServiceProcessorMessages.ERROR_SPI_ANNOTATION_MUST_BE_PLACED_ON_CLASS.getMessage());
            return;

        }

        // Now create the service locator

        TypeElement typeElement = ElementUtils.CastElement.castClass(annotatedElement);


        if (serviceAnnotation != null) {

            Set<String> spiInterfaces = new HashSet<String>();
            String spiAttributeTypes = AnnotationValueUtils.getTypeMirrorValue(AnnotationUtils.getAnnotationValueOfAttribute(serviceAnnotation)).toString();
            if (spiAttributeTypes != null) {
                spiInterfaces.add(spiAttributeTypes);
            }


            for (String fqTypeName : spiInterfaces) {


                TypeMirror typeMirror = getTypeUtils().doTypeRetrieval().getTypeMirror(fqTypeName);
                TypeElement typeMirrorTypeElement = (TypeElement) getTypeUtils().getTypes().asElement(typeMirror);

                //check if type is interface
                if (!ElementUtils.CheckKindOfElement.isInterface(typeMirrorTypeElement)) {
                    getMessager().error(annotatedElement, ServiceProcessorMessages.ERROR_VALUE_ATTRIBUTE_MUST_ONLY_CONTAIN_INTERFACES.getMessage(), typeMirrorTypeElement.getQualifiedName().toString());
                    break;
                }


                // check if annotatedElement is assignable to spi interface == implements the spi interface
                if (!getTypeUtils().doTypeComparison().isAssignableTo(typeElement, typeMirror)) {

                    getMessager().error(annotatedElement, ServiceProcessorMessages.ERROR_ANNOTATED_CLASS_MUST_IMPLEMENT_CONFIGURED_INTERFACES.getMessage(), typeMirrorTypeElement.getQualifiedName().toString());
                    break;

                }

                // put service provider implementations in map by using the SPI fqn as key
                serviceImplHashMap.put(fqTypeName, typeElement.getQualifiedName().toString());


            }

        }
    }


    private void writeConfigurationFiles() {

        // Iterate through services that were detected by the annotation processor
        for (Map.Entry<String, Set<String>> entry : serviceImplHashMap.entrySet()) {

            // look for existing resource file and load already configured values
            String serviceProviderFile = "META-INF/services/" + entry.getKey();

            Set<String> existingServiceImplementations = readServiceFile(serviceProviderFile);

            // check if we have found new services
            if (existingServiceImplementations.containsAll(entry.getValue())) {
                getMessager().info(null, "All services implementations were already registered for ${0}", entry.getKey());
                return;
            }

            Set<String> allServiceImplementations = new HashSet<String>(entry.getValue());
            allServiceImplementations.addAll(existingServiceImplementations);

            try {

                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(getFiler().createResource(StandardLocation.SOURCE_OUTPUT, "", serviceProviderFile).openOutputStream()));

                for (String value : allServiceImplementations) {
                    bw.write(value);
                    bw.newLine();
                }
                bw.flush();

                bw.close();
                getMessager().info(null, "Written service provider registration file for ${0} containing ${1}", serviceProviderFile, allServiceImplementations);

            } catch (IOException e) {
                getMessager().error(null, "Wasn't able to write service provider registration file for ${0}", entry.getKey());
                return;
            }


        }

    }

    protected Set<String> readServiceFile(String serviceProviderFile) {

        getMessager().info(null, "Reading existing service file : ${0}", serviceProviderFile);

        Set<String> resultSet = new HashSet<String>();

        try {

            FileObject existingFile = getFiler().getResource(StandardLocation.SOURCE_OUTPUT, "",
                    serviceProviderFile);

            BufferedReader br = new BufferedReader(existingFile.openReader(true));

            String currentLine = br.readLine();
            while (currentLine != null) {

                resultSet.add(currentLine.trim());

                // read next line
                currentLine = br.readLine();
            }

        } catch (IOException e) {
            getMessager().info(null, "Wasn't able to open existing service file for ${0}", serviceProviderFile);
        }

        return resultSet;
    }

}
