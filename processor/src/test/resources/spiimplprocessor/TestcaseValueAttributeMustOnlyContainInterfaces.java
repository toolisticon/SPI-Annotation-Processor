package io.toolisticon.spiap.processor.tests;

import io.toolisticon.spiap.api.SpiImpl;
import io.toolisticon.spiap.processor.spiimplprocessortest.TestSpi;

@SpiImpl(TestcaseValueAttributeMustOnlyContainInterfaces.class)
public class TestcaseValueAttributeMustOnlyContainInterfaces implements TestSpi {
    public String doSomething() {
        return "";
    }
}