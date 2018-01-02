package io.toolisticon.example.spiapexample.service;

import io.toolisticon.example.spiapexample.api.DecimalCalculationOperation;
import io.toolisticon.example.spiapexample.api.HelloWorldSpiInterface;
import io.toolisticon.spiap.api.Service;
import io.toolisticon.spiap.api.Services;

/**
 * Implements the multiplication decimal operation
 */
@Services({
        @Service(
                value = DecimalCalculationOperation.class,
                id = "MULTIPLICATION",
                description = "Does the multiplication operation on two int values",
                priority = 10),
        @Service(
                value = HelloWorldSpiInterface.class
        )
})
public class MultiplicationDecimalOperationImpl implements DecimalCalculationOperation, HelloWorldSpiInterface {

    @Override
    public int invokeOperation(int operand1, int operand2) {
        return operand1 * operand2;
    }


    @Override
    public String doSomething() {
        return "SUCCESSFULLY";
    }
}
