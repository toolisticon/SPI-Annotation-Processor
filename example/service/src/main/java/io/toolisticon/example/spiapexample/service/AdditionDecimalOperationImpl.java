package io.toolisticon.example.spiapexample.service;

import io.toolisticon.example.spiapexample.api.DecimalCalculationOperation;
import io.toolisticon.spiap.api.Service;
import io.toolisticon.spiap.api.SpiService;

/**
 * Implements the addition decimal operation
 */
@SpiService(value = DecimalCalculationOperation.class, id = "ADDITION", description = "Does the addition operation on two int values", priority = -10)
public class AdditionDecimalOperationImpl implements DecimalCalculationOperation {

    @Override
    public int invokeOperation(int operand1, int operand2) {
        return operand1 + operand2;
    }

}
