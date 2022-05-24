package io.toolisticon.spiap.processor.tests;

import io.toolisticon.spiap.api.OutOfService;
import io.toolisticon.spiap.api.SpiService;
import io.toolisticon.spiap.api.SpiServices;
import io.toolisticon.spiap.processor.serviceprocessortest.AnotherTestSpi;
import io.toolisticon.spiap.processor.serviceprocessortest.TestSpi;

@OutOfService
@SpiServices({
        @SpiService(TestSpi.class),
        @SpiService(AnotherTestSpi.class)
})
public class TestcaseDontProcessOutOfServiceService implements TestSpi, AnotherTestSpi {
    public String doSomething() {
        return "";
    }

    public void anotherThingToDo() {

    }

}