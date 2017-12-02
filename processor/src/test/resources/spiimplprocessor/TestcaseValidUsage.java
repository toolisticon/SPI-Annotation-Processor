package io.toolisticon.spiap.processor.tests;

import io.toolisticon.spiap.api.SpiImpl;
import io.toolisticon.spiap.processor.spiimplprocessortest.TestSpi;

@SpiImpl(TestSpi.class)
public class TestcaseValidUsage implements TestSpi {
    public String doSomething() {
        return "";
    }
}