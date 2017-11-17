package io.toolisticon.spiap.processor;

import de.holisticon.annotationprocessortoolkit.AbstractAnnotationProcessor;
import de.holisticon.annotationprocessortoolkit.generators.SimpleResourceWriter;
import de.holisticon.annotationprocessortoolkit.tools.ElementUtils;
import io.toolisticon.spiap.api.SpiImpl;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.io.IOException;
import java.util.HashMap;
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
                getMessager().error(element, "SpiImpl annotation must only be used on Classes");
                continue;
            }

            // Now create the service locator

            TypeElement typeElement = ElementUtils.CastElement.castInterface(element);

            // Get all interfaces
            SpiImpl annotation = typeElement.getAnnotation(SpiImpl.class);

            if (annotation != null && annotation.spis() != null) {

                for (String fqTypeName : annotation.spis()) {

                    Class type = null;

                    try {
                        type = Class.forName(fqTypeName);
                    } catch (ClassNotFoundException e) {
                        getMessager().error(element, "SpiImpl annotation spi class ${0} can not be found", fqTypeName);
                        continue outer;
                    }


                    //check if type is interface
                    if (!type.isInterface()) {
                        getMessager().error(element, "SpiImpl annotation only accepts interfaces - ${0} is no interface", type.getCanonicalName());
                        continue outer;
                    }


                    // check if annotatedElement is assignable to spi interface == implements the spi interface
                    if (!getTypeUtils().doTypeComparison().isAssignableTo(typeElement, type)) {

                        getMessager().error(element, "SpiImpl doesn't implement the ${0} interface", type.getCanonicalName());
                        continue outer;

                    }


                    // Get writer for spi locator resource file
                    SimpleResourceWriter simpleResourceWriter = spiResourceFilePool.get(type.getCanonicalName());
                    String filename = "META-INF/services/" + type.getCanonicalName();
                    if (simpleResourceWriter == null) {


                        try {
                            simpleResourceWriter = getFileObjectUtils().createResource(filename);
                        } catch (IOException e) {
                            getMessager().error(element, "Cannot open spi service location file for writing : ${0}", simpleResourceWriter);
                            continue outer;
                        }

                        spiResourceFilePool.put(type.getCanonicalName(), simpleResourceWriter);


                    }


                    try {
                        simpleResourceWriter.append(typeElement.getQualifiedName().toString());
                    } catch (IOException e) {
                        getMessager().error(element, "Cannot append to spi service location file : ${0}", filename);
                        continue outer;
                    }


                }

            }


        }

        return false;
    }


}
