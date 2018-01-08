package io.toolisticon.spiap.processor.tests;

import io.toolisticon.spiap.api.OutOfService;
import io.toolisticon.spiap.api.Service;
import io.toolisticon.spiap.api.Services;
import io.toolisticon.spiap.processor.serviceprocessortest.AnotherTestSpi;
import io.toolisticon.spiap.processor.serviceprocessortest.TestSpi;

@OutOfService
@Services({
        @Service(TestSpi.class),
        @Service(AnotherTestSpi.class)
})
public class TestcaseDontProcessOutOfServiceService implements TestSpi, AnotherTestSpi {
    public String doSomething() {
        return "";
    }

    public void anotherThingToDo() {

    }

}