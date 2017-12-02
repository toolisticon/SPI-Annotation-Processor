package io.toolisticon.spiap.processor;

import de.holisticon.annotationprocessortoolkit.AbstractAnnotationProcessor;
import de.holisticon.annotationprocessortoolkit.generators.SimpleJavaWriter;
import de.holisticon.annotationprocessortoolkit.tools.ElementUtils;
import io.toolisticon.spiap.api.Spi;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Annotation Processor for {@link Spi}.
 */
public class SpiProcessor extends AbstractAnnotationProcessor {

    private final static Set<String> SUPPORTED_ANNOTATIONS = createSupportedAnnotationSet(Spi.class);


    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return SUPPORTED_ANNOTATIONS;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        for (Element element : roundEnv.getElementsAnnotatedWith(Spi.class)) {

            // check if it is place on interface
            if (!ElementUtils.CheckKindOfElement.isInterface(element)) {
                getMessager().error(element, SpiProcessorMessages.ERROR_SPI_ANNOTATION_MUST_BE_PLACED_ON_INTERFACE.getMessage());
                continue;
            }

            System.out.println("Process : " + element.getSimpleName());

            // Now create the service locator
            TypeElement typeElement = ElementUtils.CastElement.castInterface(element);
            PackageElement packageElement = (PackageElement) ElementUtils.AccessEnclosingElements.getFirstEnclosingElementOfKind(typeElement, ElementKind.PACKAGE);

            // create Model
            Map<String, Object> model = new HashMap<String, Object>();
            model.put("package", packageElement.getQualifiedName().toString());
            model.put("canonicalName", typeElement.getQualifiedName().toString());
            model.put("simpleName", typeElement.getSimpleName().toString());

            String filePath = packageElement.getQualifiedName().toString() + "." + typeElement.getSimpleName().toString() + "ServiceLocator";

            try {
                SimpleJavaWriter javaWriter = getFileObjectUtils().createSourceFile(filePath, element);
                javaWriter.writeTemplate("/ServiceLocator.tpl", model);
                javaWriter.close();
            } catch (IOException e) {
                getMessager().error(element, SpiProcessorMessages.ERROR_COULD_NOT_CREATE_SERVICE_LOCATOR.getMessage(), filePath);
            }
        }


        return false;
    }
}
