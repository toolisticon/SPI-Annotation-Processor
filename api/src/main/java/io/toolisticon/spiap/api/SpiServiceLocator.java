package io.toolisticon.spiap.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Generates a service locator for a service interface.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.PACKAGE})
public @interface SpiServiceLocator {
    /**
     * The service interface to create the service locator class for.
     */
    Class<?> value();


}
