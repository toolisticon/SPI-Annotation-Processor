package de.holisticon.example.spiapexample.api;

import de.holisticon.tools.spiap.api.Spi;

/**
 * Demonstrates the usage of the {@link Spi} annotation.
 */

@Spi
public interface ExampleSpiInterface {

    String doSomething();

}
