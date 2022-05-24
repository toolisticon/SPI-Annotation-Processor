package io.toolisticon.spiap.processor;

import io.toolisticon.aptk.tools.TypeMirrorWrapper;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.type.TypeMirror;

/**
 * Interface to provide support for {@link io.toolisticon.spiap.api.SpiService} via a generic wrapper api.
 */
public interface ServiceAnnotation {

    /**
     * Gets the element on which the wrapped annotation is used.
     */
    Element _annotatedElement();

    /**
     * Gets the wrapped AnnotationMirror.
     */
    AnnotationMirror _annotationMirror();

    /**
     * Gets the Service.value from wrapped annotation.
     * @return the attribute value as a TypeMirror
     */
    TypeMirror valueAsTypeMirror();

    /**
     * Gets the Service.value from wrapped annotation.
     * @return the attribute value as a TypeMirror
     */
    TypeMirrorWrapper valueAsTypeMirrorWrapper();

    /**
     * Gets the Service.value from wrapped annotation.
     * @return the attribute value as a fqn
     */
    String valueAsFqn();



    /**
     * Gets the Service.id from wrapped annotation.
     * @return the attribute value
     */
    String id() ;


    /**
     * Allows to check if attribute was explicitly set or if default value is used.
     * @return true, if default value is used, otherwise false
     */
    boolean idIsDefaultValue();

    /**
     * Gets the Service.description from wrapped annotation.
     * @return the attribute value
     */
    String description();


    /**
     * Allows to check if attribute was explicitly set or if default value is used.
     * @return true, if default value is used, otherwise false
     */
    boolean descriptionIsDefaultValue();

    /**
     * Gets the Service.priority from wrapped annotation.
     * @return the attribute value
     */
    int priority() ;


    /**
     * Allows to check if attribute was explicitly set or if default value is used.
     * @return true, if default value is used, otherwise false
     */
    boolean priorityIsDefaultValue();

}
