package io.toolisticon.spiap.processor.tests;

import io.toolisticon.spiap.api.SpiService;
import io.toolisticon.spiap.processor.serviceprocessortest.TestSpi;

@SpiService(TestSpi.class)
public class TestcaseValidUsage implements TestSpi {
    public String doSomething() {
        return "";
    }
}