package io.toolisticon.example.spiapexample.usage;

import io.toolisticon.example.spiapexample.api.ExampleSpiInterfaceServiceLocator;

/**
 * Demonstrates usage of the SPI.
 */
public class ServiceUser {

    public static void main(String[] args) {

        System.out.println(ExampleSpiInterfaceServiceLocator.locate().doSomething());

    }

}
