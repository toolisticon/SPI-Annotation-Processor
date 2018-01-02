package io.toolisticon.example.spiapexample.service;

import io.toolisticon.example.spiapexample.api.HelloWorldSpiInterface;
import io.toolisticon.spiap.api.Service;

/**
 * Example about how to use the {@link Service} annotation.
 */

@Service(HelloWorldSpiInterface.class)
public class HelloWorldSpiService implements HelloWorldSpiInterface {
    @Override
    public String doSomething() {
        return "Hello World";
    }
}
