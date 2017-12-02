package io.toolisticon.example.spiapexample.service;

import io.toolisticon.example.spiapexample.api.HelloWorldSpiInterface;
import io.toolisticon.spiap.api.SpiImpl;

/**
 * Example about how to use the {@link SpiImpl} annotation.
 */

@SpiImpl(HelloWorldSpiInterface.class)
public class HelloWorldSpiService implements HelloWorldSpiInterface {
    @Override
    public String doSomething() {
        return "Hello World";
    }
}
