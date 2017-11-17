package io.toolisticon.example.spiapexample.api;

import io.toolisticon.spiap.api.Spi;

/**
 * Demonstrates the usage of the {@link Spi} annotation.
 */

@Spi
public interface ExampleSpiInterface {

    String doSomething();

}
