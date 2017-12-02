package io.toolisticon.example.spiapexample.service;

import io.toolisticon.example.spiapexample.api.DecimalCalculationOperation;
import io.toolisticon.spiap.api.SpiImpl;

/**
 * Implements the addition decimal operation
 */
@SpiImpl(value = DecimalCalculationOperation.class, id = "ADDITION", description = "Does the addition operation on two int values")
public class AdditionDecimalOperationImpl implements DecimalCalculationOperation {

    @Override
    public int invokeOperation(int operand1, int operand2) {
        return operand1 + operand2;
    }

}
