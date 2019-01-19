package io.toolisticon.example.spiapexample.usage;


import io.toolisticon.example.spiapexample.api.DecimalCalculationOperationServiceLocator;
import io.toolisticon.example.spiapexample.api.HelloWorldSpiInterfaceServiceLocator;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

/**
 * Demonstrates usage of the SPI.
 */
public class IntegrationTest {

    @Ignore
    @Test
    public void main() {

        System.out.println("SAY '" + HelloWorldSpiInterfaceServiceLocator.locate().doSomething() + "' !");

        // Now do some mathematical operations
        int operand1 = 5;
        int operand2 = 3;

        List<DecimalCalculationOperationServiceLocator.ServiceImplementation> serviceImpls = DecimalCalculationOperationServiceLocator.getServiceImplementations();
        for (DecimalCalculationOperationServiceLocator.ServiceImplementation serviceImpl : serviceImpls) {

            int result = serviceImpl.getServiceInstance().invokeOperation(operand1, operand2);
            System.out.println(String.format("Impl. ID: '%s' , description: '%s' , operand1 : %d , operand2 : %d , result : %d", serviceImpl.getId(), serviceImpl.getDescription(), operand1, operand2, result));

        }


    }

}
