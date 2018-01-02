package io.toolisticon.spiap.processor.tests;

import io.toolisticon.spiap.api.Service;
import io.toolisticon.spiap.processor.serviceprocessortest.TestSpi;

@Service(TestcaseValueAttributeMustOnlyContainInterfaces.class)
public class TestcaseValueAttributeMustOnlyContainInterfaces implements TestSpi {
    public String doSomething() {
        return "";
    }
}