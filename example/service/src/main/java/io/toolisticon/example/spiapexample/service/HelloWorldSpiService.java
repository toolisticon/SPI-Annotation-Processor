package io.toolisticon.example.spiapexample.service;

import io.toolisticon.example.spiapexample.api.HelloWorldSpiInterface;
import io.toolisticon.spiap.api.SpiService;

/**
 * Example about how to use the {@link SpiService} annotation.
 * This class is kept to use {@link SpiService} to test backward compatibility.
 */

@SpiService(value = HelloWorldSpiInterface.class, description = "Hi again")
public class HelloWorldSpiService implements HelloWorldSpiInterface {
    @Override
    public String doSomething() {
        return "Hello World";
    }
}
