package io.toolisticon.spiap.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Binder for supporting several {@link SpiServiceLocator} in a single class.
 *
 * @since 0.8.2
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.PACKAGE})
public @interface SpiServiceLocators {

    /**
     * The SpiServiceLocators.
     *
     * @return an array of SpiServiceLocator
     */
    SpiServiceLocator[] value();

}
