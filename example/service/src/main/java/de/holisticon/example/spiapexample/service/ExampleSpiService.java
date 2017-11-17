package de.holisticon.example.spiapexample.service;

import de.holisticon.example.spiapexample.api.ExampleSpiInterface;
import de.holisticon.tools.spiap.api.SpiImpl;

/**
 * Example about how to use the {@link SpiImpl} annotation.
 */

@SpiImpl(spis = {"de.holisticon.example.spiapexample.api.ExampleSpiInterface"})
public class ExampleSpiService implements ExampleSpiInterface {
    @Override
    public String doSomething() {
        return "IT WORKS !";
    }
}
