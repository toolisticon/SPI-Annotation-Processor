package de.holisticon.example.spiapexample.usage;

import de.holisticon.example.spiapexample.api.ExampleSpiInterfaceServiceLocator;

/**
 * Demonstrates usage of the SPI.
 */
public class ServiceUser {

    public static void main(String[] args) {

        System.out.println(ExampleSpiInterfaceServiceLocator.locate().doSomething());

    }

}
