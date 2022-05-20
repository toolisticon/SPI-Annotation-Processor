package io.toolisticon.spiap.processor;

import io.toolisticon.aptk.tools.AbstractAnnotationProcessor;
import io.toolisticon.aptk.tools.ElementUtils;
import io.toolisticon.aptk.tools.FilerUtils;
import io.toolisticon.aptk.tools.MessagerUtils;
import io.toolisticon.aptk.tools.TypeMirrorWrapper;
import io.toolisticon.aptk.tools.TypeUtils;
import io.toolisticon.aptk.tools.generators.SimpleResourceWriter;
import io.toolisticon.aptk.tools.wrapper.ElementWrapper;
import io.toolisticon.aptk.tools.wrapper.TypeElementWrapper;
import io.toolisticon.spiap.api.OutOfService;
import io.toolisticon.spiap.api.Service;
import io.toolisticon.spiap.api.Services;
import io.toolisticon.spiap.api.SpiService;
import io.toolisticon.spiap.api.SpiServices;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Annotation Processor for {@link Service} and {@link Services}.
 * Creates the service descriptor file in "/META-INF/services/fqnOfSpi".
 * Additionally, it creates a property file containing all additional metadata used by the ServiceLocator (like order, id and description)
 * to get rid of the api library as run time dependency.
 */
public class ServiceProcessor extends AbstractAnnotationProcessor {

    private final static Set<String> SUPPORTED_ANNOTATIONS = createSupportedAnnotationSet(Services.class, Service.class, SpiServices.class, SpiService.class);

    private final ServiceImplMap serviceImplHashMap = new ServiceImplMap();

    /**
     * Map to store service provider implementations.
     */
    protected static class ServiceImplMap extends HashMap<String, Set<String>> {

        /**
         * Convenience method to add a value.
         * Creates a Set if no entry for passed key can be found.
         *
         * @param key   the key to look for
         * @param value the value to store in keys set
         * @return the keys set
         */
        public Set<String> put(String key, String value) {
            Set<String> set = this.computeIfAbsent(key, k -> new HashSet<>());
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
            processAnnotationsInternally(roundEnv);
        } else {
            writeConfigurationFiles();
        }

        return false;

    }

    private void processAnnotationsInternally(RoundEnvironment roundEnv) {

        // process Services annotation
        for (Element element : roundEnv.getElementsAnnotatedWith(Services.class)) {

            // Check for OutOfService annotation
            if (checkSkipProcessingBecauseOfOutOfServiceAnnotation(element)) {
                continue;
            }

            // read annotation
            ServicesWrapper servicesAnnotationWrapper = ServicesWrapper.wrap(element);

            for (ServiceWrapper serviceWrapper : servicesAnnotationWrapper.value()) {
                processAnnotation(serviceWrapper, element);
            }


        }


        // process Service annotation
        for (Element element : roundEnv.getElementsAnnotatedWith(Service.class)) {

            // Check for OutOfService annotation
            if (checkSkipProcessingBecauseOfOutOfServiceAnnotation(element)) {
                continue;
            }

            // read annotation
            ServiceWrapper serviceAnnotationWrapper = ServiceWrapper.wrap(element);

            processAnnotation(serviceAnnotationWrapper, element);

        }


        // process Services annotation
        for (Element element : roundEnv.getElementsAnnotatedWith(SpiServices.class)) {

            // Check for OutOfService annotation
            if (checkSkipProcessingBecauseOfOutOfServiceAnnotation(element)) {
                continue;
            }

            // read annotation
            SpiServicesWrapper servicesAnnotationWrapper = SpiServicesWrapper.wrap(element);

            for (SpiServiceWrapper serviceWrapper : servicesAnnotationWrapper.value()) {
                processAnnotation(serviceWrapper, element);
            }


        }


        // process Service annotation
        for (Element element : roundEnv.getElementsAnnotatedWith(SpiService.class)) {

            // Check for OutOfService annotation
            if (checkSkipProcessingBecauseOfOutOfServiceAnnotation(element)) {
                continue;
            }

            // read annotation
            SpiServiceWrapper serviceAnnotationWrapper = SpiServiceWrapper.wrap(element);

            processAnnotation(serviceAnnotationWrapper, element);

        }


    }

    private boolean checkSkipProcessingBecauseOfOutOfServiceAnnotation(Element element) {
        // Check for OutOfService annotation
        if (element.getAnnotation(OutOfService.class) != null) {
            // skip processing of element
            MessagerUtils.info(element, ServiceProcessorMessages.INFO_SKIP_ELEMENT_ANNOTATED_AS_OUT_OF_SERVICE, ElementUtils.CastElement.castClass(element).getQualifiedName());
            return true;
        }
        return false;
    }

    private void processAnnotation(ServiceAnnotation serviceAnnotationWrapper, Element annotatedElement) {

        // check if it is placed on Class
        if (!ElementUtils.CheckKindOfElement.isClass(annotatedElement)) {
            MessagerUtils.error(annotatedElement, ServiceProcessorMessages.ERROR_SPI_ANNOTATION_MUST_BE_PLACED_ON_CLASS);
            return;
        }

        // Now create the service locator

        TypeElementWrapper typeElement = TypeElementWrapper.wrap((TypeElement) annotatedElement);

        if (serviceAnnotationWrapper != null) {

            Set<String> spiInterfaces = new HashSet<>();
            String spiAttributeTypes = serviceAnnotationWrapper.valueAsFqn();
            if (spiAttributeTypes != null) {
                spiInterfaces.add(spiAttributeTypes);
            }

            for (String fqTypeName : spiInterfaces) {

                TypeMirrorWrapper typeMirror = TypeMirrorWrapper.wrap(fqTypeName);
                TypeElementWrapper typeMirrorTypeElement = typeMirror.getTypeElement().get();

                //check if type is interface
                if (!typeMirrorTypeElement.isInterface()) {
                    MessagerUtils.error(annotatedElement, ServiceProcessorMessages.ERROR_VALUE_ATTRIBUTE_MUST_ONLY_CONTAIN_INTERFACES, typeMirrorTypeElement.getQualifiedName());
                    break;
                }

                // check if annotatedElement is assignable to spi interface == implements the spi interface
                if (!TypeUtils.TypeComparison.isAssignableTo(typeElement.unwrap(), typeMirror.unwrap())) {

                    MessagerUtils.error(annotatedElement, ServiceProcessorMessages.ERROR_ANNOTATED_CLASS_MUST_IMPLEMENT_CONFIGURED_INTERFACES, typeMirrorTypeElement.getQualifiedName());
                    break;

                }

                // Add configuration property file
                writePropertiesFile(typeElement, serviceAnnotationWrapper);

                // put service provider implementations in map by using the SPI fqn as key
                serviceImplHashMap.put(fqTypeName, typeElement.getQualifiedName());


            }

        }
    }

    /**
     * This method generates the property files of configured service annotation values.
     * This allows us not to get rid of spiap-api dependency at runtime.
     *
     * @param annotatedTypeElement     the annotated element
     * @param serviceAnnotationWrapper The Service annotations AnnotationMirror
     */
    private void writePropertiesFile(TypeElementWrapper annotatedTypeElement, ServiceAnnotation serviceAnnotationWrapper) {


        String spiClassName = serviceAnnotationWrapper.valueAsFqn();

        String serviceClassName = annotatedTypeElement.getQualifiedName();
        String fileName = "META-INF/spiap/" + spiClassName + "/" + serviceClassName + ".properties";

        try {

            // write service provider file by using a template
            SimpleResourceWriter propertiesFileObjectWriter = FilerUtils.createResource(StandardLocation.CLASS_OUTPUT, "", fileName);

            // Get properties
            Properties properties = new Properties();

            // default to fqn of service if id is empty
            properties.setProperty(Constants.PROPERTY_KEY_ID, !serviceAnnotationWrapper.id().isEmpty() ? serviceAnnotationWrapper.id() : serviceClassName);
            properties.setProperty(Constants.PROPERTY_KEY_DESCRIPTION, serviceAnnotationWrapper.description());
            properties.setProperty(Constants.PROPERTY_KEY_PRIORITY, "" + serviceAnnotationWrapper.priority());
            properties.setProperty(Constants.PROPERTY_KEY_OUT_OF_SERVICE, "" + annotatedTypeElement.getAnnotation(OutOfService.class).isPresent());

            StringWriter writer = new StringWriter();
            properties.store(writer, "Property files used by the spi service locator");
            writer.flush();
            writer.close();

            propertiesFileObjectWriter.write(writer.toString());
            propertiesFileObjectWriter.close();


        } catch (IOException e) {
            MessagerUtils.error(annotatedTypeElement.unwrap(), "Wasn't able to write service provider properties file for service ${0} for spi ${1}", serviceClassName, spiClassName);
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
                MessagerUtils.info(null, "All services implementations were already registered for ${0}", entry.getKey());
                return;
            }

            Set<String> allServiceImplementations = new HashSet<>(entry.getValue());
            allServiceImplementations.addAll(existingServiceImplementations);

            try {

                // write service provider file by using a template
                SimpleResourceWriter writer = FilerUtils.createResource(StandardLocation.CLASS_OUTPUT, "", serviceProviderFile);

                Map<String, Object> model = new HashMap<>();
                model.put("fqns", allServiceImplementations);

                writer.writeTemplateString("!{for fqn: fqns}${fqn}\n!{/for}", model);
                writer.close();

                /*-
                // Alternative approach by using the Filer utility class
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(getFiler().createResource(StandardLocation.CLASS_OUTPUT, "", serviceProviderFile).openOutputStream()));

                for (String value : allServiceImplementations) {
                    bw.write(value);
                    bw.newLine();
                }
                bw.flush();

                bw.close();
                */

                MessagerUtils.info(null, "Written service provider registration file for ${0} containing ${1}", serviceProviderFile, allServiceImplementations);

            } catch (IOException e) {
                MessagerUtils.error(null, "Wasn't able to write service provider registration file for ${0}", entry.getKey());
                return;
            }


        }

    }

    /**
     * Read service file.
     * Get all already defined service providers.
     *
     * @param serviceProviderFile the service provider file
     * @return a set containing all service providers
     */
    protected Set<String> readServiceFile(String serviceProviderFile) {

        MessagerUtils.info(null, "Reading existing service file : ${0}", serviceProviderFile);

        Set<String> resultSet = new HashSet<>();

        try {

            resultSet.addAll(FilerUtils.getResource(StandardLocation.CLASS_OUTPUT, "",
                    serviceProviderFile).readAsLines(true));


        } catch (IOException e) {
            MessagerUtils.info(null, "Wasn't able to open existing service file for ${0}", serviceProviderFile);
        }

        return resultSet;
    }

}
