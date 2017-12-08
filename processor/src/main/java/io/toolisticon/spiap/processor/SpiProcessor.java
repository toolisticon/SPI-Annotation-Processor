package io.toolisticon.spiap.processor;

import de.holisticon.annotationprocessortoolkit.AbstractAnnotationProcessor;
import de.holisticon.annotationprocessortoolkit.generators.SimpleJavaWriter;
import de.holisticon.annotationprocessortoolkit.tools.AnnotationUtils;
import de.holisticon.annotationprocessortoolkit.tools.ElementUtils;
import io.toolisticon.spiap.api.Spi;
import io.toolisticon.spiap.api.SpiServiceLocator;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Annotation Processor for {@link Spi} and {@link io.toolisticon.spiap.api.SpiServiceLocator}.
 */
public class SpiProcessor extends AbstractAnnotationProcessor {

    private final static Set<String> SUPPORTED_ANNOTATIONS = createSupportedAnnotationSet(Spi.class, SpiServiceLocator.class);


    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return SUPPORTED_ANNOTATIONS;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        handeSpiAnnotation(annotations, roundEnv);
        handeSpiServiceLocatorAnnotation(annotations, roundEnv);

        return false;
    }

    private void handeSpiAnnotation(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        // handle Spi annotation
        for (Element element : roundEnv.getElementsAnnotatedWith(Spi.class)) {

            getMessager().info(element, "Process : " + element.getSimpleName() + " annotated with Spi annotation");


            // check if it is place on interface
            if (!ElementUtils.CheckKindOfElement.isInterface(element)) {
                getMessager().error(element, SpiProcessorMessages.ERROR_SPI_ANNOTATION_MUST_BE_PLACED_ON_INTERFACE.getMessage());
                continue;
            }


            // Now create the service locator
            TypeElement typeElement = ElementUtils.CastElement.castInterface(element);
            PackageElement packageElement = (PackageElement) ElementUtils.AccessEnclosingElements.getFirstEnclosingElementOfKind(typeElement, ElementKind.PACKAGE);


            // generate service locator
            generateServiceLocator(element, packageElement, typeElement);

        }

    }

    private void handeSpiServiceLocatorAnnotation(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        // handle Spi annotation
        for (Element element : roundEnv.getElementsAnnotatedWith(SpiServiceLocator.class)) {

            getMessager().info(element, "Process : " + element.getSimpleName() + " annotated with SpiServiceLocator annotation");


            // get type from annotation
            TypeMirror typeMirror = AnnotationUtils.getClassAttributeFromAnnotationAsTypeMirror(element, SpiServiceLocator.class);
            if (typeMirror == null) {
                getMessager().error(element, "Couldn't get type from annotations attributes");
                continue;
            }

            Element serviceLocatorInterfaceElement = getTypeUtils().getTypes().asElement(typeMirror);

            // check if it is place on interface
            if (!ElementUtils.CheckKindOfElement.isInterface(serviceLocatorInterfaceElement)) {
                getMessager().error(element, SpiProcessorMessages.ERROR_SPI_ANNOTATION_MUST_BE_PLACED_ON_INTERFACE.getMessage());
                continue;
            }


            // Now create the service locator
            TypeElement typeElement = ElementUtils.CastElement.castInterface(serviceLocatorInterfaceElement);
            PackageElement packageElement = (PackageElement) element;


            // generate service locator
            generateServiceLocator(element, packageElement, typeElement);

        }

    }


    private void generateServiceLocator(Element annotatedElement, PackageElement packageElement, TypeElement typeElement) {

        // create Model
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("package", packageElement.getQualifiedName().toString());
        model.put("canonicalName", typeElement.getQualifiedName().toString());
        model.put("simpleName", typeElement.getSimpleName().toString());

        String filePath = packageElement.getQualifiedName().toString() + "." + typeElement.getSimpleName().toString() + "ServiceLocator";

        try {
            SimpleJavaWriter javaWriter = getFileObjectUtils().createSourceFile(filePath, annotatedElement);
            javaWriter.writeTemplate("/ServiceLocator.tpl", model);
            javaWriter.close();
        } catch (IOException e) {
            getMessager().error(annotatedElement, SpiProcessorMessages.ERROR_COULD_NOT_CREATE_SERVICE_LOCATOR.getMessage(), filePath);
        }

    }

}
