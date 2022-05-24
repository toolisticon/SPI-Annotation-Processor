package io.toolisticon.example.spiapexample.service;

import io.toolisticon.example.spiapexample.api.DecimalCalculationOperation;
import io.toolisticon.example.spiapexample.api.HelloWorldSpiInterface;
import io.toolisticon.spiap.api.SpiService;
import io.toolisticon.spiap.api.SpiServices;

/**
 * Implements the multiplication decimal operation
 */
@SpiServices({
        @SpiService(
                value = DecimalCalculationOperation.class,
                id = "MULTIPLICATION",
                description = "Does the multiplication operation on two int values",
                priority = 10),
        @SpiService(
                value = HelloWorldSpiInterface.class,
                description = "Say Hello"
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
