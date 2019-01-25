package io.toolisticon.spiap.interationtest;

import io.toolisticon.example.spiapexample.api.DecimalCalculationOperation;

public class SquareDecimalOperationServiceImpl implements DecimalCalculationOperation {

    @Override
    public int invokeOperation(int operand1, int operand2) {

        if (operand2 <= 0) {
            return 0;
        }

        int result = operand1;

        for (int i = 1; i < operand2; i++) {
            result = result * operand1;
            i++;
        }

        return result;
    }
}
