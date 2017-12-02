package io.toolisticon.spiap.processor;

import de.holisticon.annotationprocessortoolkit.AbstractAnnotationProcessor;
import de.holisticon.annotationprocessortoolkit.generators.SimpleResourceWriter;
import de.holisticon.annotationprocessortoolkit.tools.AnnotationUtils;
import de.holisticon.annotationprocessortoolkit.tools.ElementUtils;
import io.toolisticon.spiap.api.SpiImpl;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Annotation Processor for {@link SpiImpl}.
 */
public class SpiImplProcessor extends AbstractAnnotationProcessor {

    private final static Set<String> SUPPORTED_ANNOTATIONS = createSupportedAnnotationSet(SpiImpl.class);

    private final static Map<String, SimpleResourceWriter> spiResourceFilePool = new HashMap<String, SimpleResourceWriter>();


    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return SUPPORTED_ANNOTATIONS;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {


        outer:
        for (Element element : roundEnv.getElementsAnnotatedWith(SpiImpl.class)) {

            // check if it is place on Class
            if (!ElementUtils.CheckKindOfElement.isClass(element)) {
                getMessager().error(element, SpiImplProcessorMessages.ERROR_SPI_ANNOTATION_MUST_BE_PLACED_ON_CLASS.getMessage());
                continue;
            }

            // Now create the service locator

            TypeElement typeElement = ElementUtils.CastElement.castInterface(element);

            // Get all interfaces
            SpiImpl annotation = typeElement.getAnnotation(SpiImpl.class);

            if (annotation != null) {

                Set<String> spiInterfaces = new HashSet<String>();
                String[] spiAttributeTypes = AnnotationUtils.getClassArrayAttributeFromAnnotationAsFqn(typeElement, SpiImpl.class);
                if (spiAttributeTypes != null) {
                    spiInterfaces.addAll(Arrays.asList(spiAttributeTypes));
                }


                for (String fqTypeName : spiInterfaces) {


                    TypeMirror typeMirror = getTypeUtils().doTypeRetrieval().getTypeMirror(fqTypeName);
                    TypeElement typeMirrorTypeElement = (TypeElement) getTypeUtils().getTypes().asElement(typeMirror);

                    //check if type is interface
                    if (!ElementUtils.CheckKindOfElement.isInterface(typeMirrorTypeElement)) {
                        getMessager().error(element, SpiImplProcessorMessages.ERROR_VALUE_ATTRIBUTE_MUST_ONLY_CONTAIN_INTERFACES.getMessage(), typeMirrorTypeElement.getQualifiedName().toString());
                        continue outer;
                    }


                    // check if annotatedElement is assignable to spi interface == implements the spi interface
                    if (!getTypeUtils().doTypeComparison().isAssignableTo(typeElement, typeMirror)) {

                        getMessager().error(element, SpiImplProcessorMessages.ERROR_ANNOTATED_CLASS_MUST_IMPLEMENT_CONFIGURED_INTERFACES.getMessage(), typeMirrorTypeElement.getQualifiedName().toString());
                        continue outer;

                    }


                    // Get writer for spi locator resource file
                    SimpleResourceWriter simpleResourceWriter = spiResourceFilePool.get(typeMirrorTypeElement.getQualifiedName().toString());
                    String filename = "META-INF/services/" + typeMirrorTypeElement.getQualifiedName().toString();
                    if (simpleResourceWriter == null) {


                        try {
                            simpleResourceWriter = getFileObjectUtils().createResource(filename);
                        } catch (IOException e) {
                            getMessager().error(element, SpiImplProcessorMessages.ERROR_COULD_NOT_CREATE_SERVICE_LOCATOR_FILE.getMessage(), simpleResourceWriter);
                            continue outer;
                        }

                        spiResourceFilePool.put(typeMirrorTypeElement.getQualifiedName().toString(), simpleResourceWriter);


                    }


                    try {
                        simpleResourceWriter.append(typeElement.getQualifiedName().toString() + "\n");
                    } catch (IOException e) {
                        getMessager().error(element, SpiImplProcessorMessages.ERROR_COULD_NOT_APPEND_TO_SERVICE_LOCATOR_FILE.getMessage(), filename);
                        continue outer;
                    }


                }

            }


        }

        return false;
    }


}
