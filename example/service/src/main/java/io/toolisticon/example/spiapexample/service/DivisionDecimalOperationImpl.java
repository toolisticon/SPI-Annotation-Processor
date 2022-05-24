package io.toolisticon.example.spiapexample.service;

import io.toolisticon.example.spiapexample.api.DecimalCalculationOperation;
import io.toolisticon.spiap.api.SpiService;


/**
 * Implements the division decimal operation.
 * <p>
 * It's ok not to set the id. The full qualified class name will be used as fallback value for id.
 */
@SpiService(DecimalCalculationOperation.class)
public class DivisionDecimalOperationImpl implements DecimalCalculationOperation {

    @Override
    public int invokeOperation(int operand1, int operand2) {
        return operand1 / operand2;
    }

}

