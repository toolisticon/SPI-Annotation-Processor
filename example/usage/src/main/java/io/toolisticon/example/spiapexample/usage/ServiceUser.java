package io.toolisticon.example.spiapexample.usage;


import io.toolisticon.example.spiapexample.api.DecimalCalculationOperation;
import io.toolisticon.example.spiapexample.api.DecimalCalculationOperationServiceLocator;
import io.toolisticon.example.spiapexample.api.HelloWorldSpiInterfaceServiceLocator;

import java.util.List;

/**
 * Demonstrates usage of the SPI.
 */
public class ServiceUser {

    public static void main(String[] args) {

        System.out.println("SAY '" + HelloWorldSpiInterfaceServiceLocator.locate().doSomething() + "' !");

        // Now do some mathematical operations
        int operand1 = 5;
        int operand2 = 3;

        // with meta data (service wrapped in ServiceImpl)
        System.out.println("By getting wrapped service with metadata by using getServiceImpls");
        List<DecimalCalculationOperationServiceLocator.ServiceImplementation> operationServiceKeys = DecimalCalculationOperationServiceLocator.getServiceImplementations();
        for (DecimalCalculationOperationServiceLocator.ServiceImplementation sk : operationServiceKeys) {

            int result = DecimalCalculationOperationServiceLocator.locateById(sk.getId()).invokeOperation(operand1, operand2);
            System.out.println(String.format("Impl. ID: '%s' , description: '%s' , operand1 : %d , operand2 : %d , result : %d", sk.getId(), sk.getDescription(), operand1, operand2, result));

        }

        // direct usage
        System.out.println("Direct usage by using locateAll");
        List<DecimalCalculationOperation> services = DecimalCalculationOperationServiceLocator.locateAll();
        for (DecimalCalculationOperation service : services) {

            int result = service.invokeOperation(operand1, operand2);
            System.out.println(String.format("Impl.: '%s' , operand1 : %d , operand2 : %d , result : %d", service.getClass().getCanonicalName(), operand1, operand2, result));

        }


    }

}
