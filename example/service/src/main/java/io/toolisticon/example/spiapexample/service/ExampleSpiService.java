package io.toolisticon.example.spiapexample.service;

import io.toolisticon.example.spiapexample.api.ExampleSpiInterface;
import io.toolisticon.spiap.api.SpiImpl;

/**
 * Example about how to use the {@link SpiImpl} annotation.
 */

@SpiImpl(spis = {"io.toolisticon.example.spiapexample.api.ExampleSpiInterface"})
public class ExampleSpiService implements ExampleSpiInterface {
    @Override
    public String doSomething() {
        return "IT WORKS !";
    }
}
