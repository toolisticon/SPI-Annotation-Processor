package io.toolisticon.example.spiapexample.service;

import io.toolisticon.example.spiapexample.api.DecimalCalculationOperation;
import io.toolisticon.spiap.api.SpiImpl;


/**
 * Implements the subtraction decimal operation
 */
@SpiImpl(value = DecimalCalculationOperation.class, id = "SUBTRACTION", description = "Does the subtraction operation on two int values")
public class SubtractionDecimalOperationImpl implements DecimalCalculationOperation {

    @Override
    public int invokeOperation(int operand1, int operand2) {
        return operand1 - operand2;
    }

}