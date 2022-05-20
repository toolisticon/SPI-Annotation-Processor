package io.toolisticon.spiap.processor;

import io.toolisticon.aptk.tools.AbstractAnnotationProcessor;
import io.toolisticon.aptk.tools.FilerUtils;
import io.toolisticon.aptk.tools.MessagerUtils;
import io.toolisticon.aptk.tools.generators.SimpleJavaWriter;
import io.toolisticon.aptk.tools.wrapper.ElementWrapper;
import io.toolisticon.aptk.tools.wrapper.PackageElementWrapper;
import io.toolisticon.aptk.tools.wrapper.TypeElementWrapper;
import io.toolisticon.spiap.api.Spi;
import io.toolisticon.spiap.api.SpiServiceLocator;
import io.toolisticon.spiap.api.SpiServiceLocators;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Annotation Processor for {@link Spi} and {@link io.toolisticon.spiap.api.SpiServiceLocator}.
 */
public class SpiProcessor extends AbstractAnnotationProcessor {

    private final static Set<String> SUPPORTED_ANNOTATIONS =
            createSupportedAnnotationSet(Spi.class, SpiServiceLocator.class, SpiServiceLocators.class);


    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return SUPPORTED_ANNOTATIONS;
    }

    @Override
    public boolean processAnnotations(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        handleSpiAnnotation(roundEnv);
        handleSpiServiceLocatorAnnotation(roundEnv);

        return false;
    }

    private void handleSpiAnnotation(RoundEnvironment roundEnv) {
        // handle Spi annotation
        for (Element element : roundEnv.getElementsAnnotatedWith(Spi.class)) {

            TypeElementWrapper typeElement = TypeElementWrapper.wrap((TypeElement) element);

            MessagerUtils.info(element, "Process : " + typeElement.getSimpleName() + " annotated with Spi annotation");


            // check if it is place on interface
            if (!typeElement.isInterface()) {
                MessagerUtils.error(typeElement.unwrap(), SpiProcessorMessages.ERROR_SPI_ANNOTATION_MUST_BE_PLACED_ON_INTERFACE);
                continue;
            }


            // Generate service locator depending on settings configured in Spi annotation
            SpiWrapper spiAnnotation = SpiWrapper.wrap(element);
            if (spiAnnotation != null && spiAnnotation.generateServiceLocator()) {

                // Now create the service locator
                PackageElementWrapper packageElement = typeElement.getPackage();

                // generate service locator
                generateServiceLocator(element, packageElement, typeElement);
            }

        }

    }

    private void handleSpiServiceLocatorAnnotation(RoundEnvironment roundEnv) {

        // handle SpiServiceLocator annotation
        for (Element element : roundEnv.getElementsAnnotatedWith(SpiServiceLocator.class)) {

            PackageElementWrapper packageElement = PackageElementWrapper.wrap((PackageElement) element);

            MessagerUtils.info(element, "Process : package '" + packageElement.getQualifiedName() + "# annotated with SpiServiceLocator annotation");

            // get type from annotation
            SpiServiceLocatorWrapper spiWrapper = SpiServiceLocatorWrapper.wrap(element);
            handleSpiServiceLocationWrapper(spiWrapper, packageElement);

        }

        // handle SpiServiceLocator annotation
        for (Element element : roundEnv.getElementsAnnotatedWith(SpiServiceLocators.class)) {

            PackageElementWrapper packageElement = PackageElementWrapper.wrap((PackageElement) element);

            MessagerUtils.info(element, "Process : package '" + packageElement.getQualifiedName() + "' annotated with SpiServiceLocator annotation");

            // get type from annotation
            SpiServiceLocatorsWrapper annotationWrapper = SpiServiceLocatorsWrapper.wrap(element);
            if (annotationWrapper == null) {
                continue;
            }

            for (SpiServiceLocatorWrapper spiWrapper : annotationWrapper.value()) {
                handleSpiServiceLocationWrapper(spiWrapper, packageElement);
            }

        }

    }

    private void handleSpiServiceLocationWrapper(
            final SpiServiceLocatorWrapper annotationWrapper, final PackageElementWrapper packageElement) {

        if (annotationWrapper == null || annotationWrapper.valueAsTypeMirror() == null) {
            MessagerUtils.error(packageElement.unwrap(), "Couldn't get type from annotations attributes");
            return;
        }

        // Safe to get Optional since it refers class value
        TypeElementWrapper serviceLocatorInterfaceElement = annotationWrapper.valueAsTypeMirrorWrapper().getTypeElement().get();

        // check if it is place on interface
        if (!serviceLocatorInterfaceElement.isInterface()) {
            MessagerUtils.error(packageElement.unwrap(), SpiProcessorMessages.ERROR_SERVICE_LOCATOR_PASSED_SPI_CLASS_MUST_BE_AN_INTERFACE);
            return;
        }

        // generate service locator
        generateServiceLocator(packageElement.unwrap(), packageElement, serviceLocatorInterfaceElement);
    }

    private void generateServiceLocator(Element annotatedElement, PackageElementWrapper packageElement, TypeElementWrapper typeElement) {

        // create Model
        Map<String, Object> model = new HashMap<>();
        model.put("package", packageElement.getQualifiedName());
        model.put("canonicalName", typeElement.getQualifiedName());
        model.put("simpleName", typeElement.getSimpleName());

        Map<String, Object> constants = new HashMap<>();
        constants.put("id", Constants.PROPERTY_KEY_ID);
        constants.put("description", Constants.PROPERTY_KEY_DESCRIPTION);
        constants.put("priority", Constants.PROPERTY_KEY_PRIORITY);
        constants.put("outOfService", Constants.PROPERTY_KEY_OUT_OF_SERVICE);
        model.put("constants", constants);

        String filePath = packageElement.getQualifiedName() + "." + typeElement.getSimpleName() + "ServiceLocator";

        try {
            SimpleJavaWriter javaWriter = FilerUtils.createSourceFile(filePath, annotatedElement);
            javaWriter.writeTemplate("/ServiceLocator.tpl", model);
            javaWriter.close();
        } catch (IOException e) {
            MessagerUtils.error(annotatedElement, SpiProcessorMessages.ERROR_COULD_NOT_CREATE_SERVICE_LOCATOR, filePath);
        }

    }

}
