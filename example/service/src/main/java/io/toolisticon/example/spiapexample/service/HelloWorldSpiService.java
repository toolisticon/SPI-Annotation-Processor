package io.toolisticon.example.spiapexample.service;

import io.toolisticon.example.spiapexample.api.HelloWorldSpiInterface;
import io.toolisticon.spiap.api.Service;

/**
 * Example about how to use the {@link Service} annotation.
 * This class is kept to use {@link Service} to test backward compatibility.
 */

@Service(value = HelloWorldSpiInterface.class, description = "Hi again")
public class HelloWorldSpiService implements HelloWorldSpiInterface {
    @Override
    public String doSomething() {
        return "Hello World";
    }
}
