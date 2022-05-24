package io.toolisticon.example.spiapexample.service;

import io.toolisticon.example.spiapexample.api.DecimalCalculationOperation;
import io.toolisticon.spiap.api.OutOfService;
import io.toolisticon.spiap.api.SpiService;

/**
 * Example for deactivating a service implementation via {@link OutOfService}.
 */
@SpiService(DecimalCalculationOperation.class)
@OutOfService
public class ModuloDecimalOperationImpl implements DecimalCalculationOperation {

    @Override
    public int invokeOperation(int operand1, int operand2) {
        return operand1 % operand2;
    }

}
