package io.toolisticon.example.spiapexample.service;

import io.toolisticon.example.spiapexample.api.DecimalCalculationOperation;
import io.toolisticon.spiap.api.SpiImpl;

/**
 * Implements the multiplication decimal operation
 */
@SpiImpl(value = DecimalCalculationOperation.class, id = "MULTIPLICATION", description = "Does the multiplication operation on two int values")
public class MultiplicationDecimalOperationImpl implements DecimalCalculationOperation {

    @Override
    public int invokeOperation(int operand1, int operand2) {
        return operand1 * operand2;
    }

}
