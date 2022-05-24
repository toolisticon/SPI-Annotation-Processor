package io.toolisticon.spiap.processor.tests;

import io.toolisticon.spiap.api.SpiService;
import io.toolisticon.spiap.processor.serviceprocessortest.TestSpi;

@SpiService(TestcaseValueAttributeMustOnlyContainInterfaces.class)
public class TestcaseValueAttributeMustOnlyContainInterfaces implements TestSpi {
    public String doSomething() {
        return "";
    }
}