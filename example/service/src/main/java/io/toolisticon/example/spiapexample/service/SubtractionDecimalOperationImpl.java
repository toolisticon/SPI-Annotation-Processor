package io.toolisticon.example.spiapexample.service;

import io.toolisticon.example.spiapexample.api.DecimalCalculationOperation;
import io.toolisticon.spiap.api.Service;


/**
 * Implements the subtraction decimal operation
 */
@Service(value = DecimalCalculationOperation.class, id = "SUBTRACTION", description = "Does the subtraction operation on two int values", priority = -5)
public class SubtractionDecimalOperationImpl implements DecimalCalculationOperation {

    @Override
    public int invokeOperation(int operand1, int operand2) {
        return operand1 - operand2;
    }

}