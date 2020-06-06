package io.toolisticon.spiap.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks an interface that should be used as an SPI.
 * By default this annotation will trigger the creation of the SPI service locator.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.TYPE})
public @interface Spi {

    /**
     * Should the service locator be generated?
     * @return the configured value or true as a default value
     */
    boolean generateServiceLocator() default true;

}
