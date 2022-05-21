package io.toolisticon.spiap.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks an implementation of one or more SPI.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.TYPE})
public @interface SpiService {

    /**
     * The SPI interface implemented by the annotated class.
     *
     * @return  the spi interface
     */
    Class<?> value();

    /**
     * This optional attribute is used to declare an identifier for this implementation.
     * If not set, the full qualified class name of the service implementation will be used as id.
     *
     * @return the id
     */
    String id() default "";

    /**
     * This optional attribute is used to add a short description about the implementing class.
     *
     * @return the description
     */
    String description() default "";

    /**
     * This optional attribute defines the order of the service implementations returned by the generated service allocator.
     * Lower value are defining a higher priority. Defaults to 0.
     *
     * @return the priority
     */
    int priority() default 0;

}
