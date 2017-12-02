package io.toolisticon.example.spiapexample.api;

import io.toolisticon.spiap.api.Spi;

/**
 * Example SPI to demonstrate usage of this project.
 */
@Spi
public interface DecimalCalculationOperation {

    int invokeOperation(int operand1, int operand2);

}
