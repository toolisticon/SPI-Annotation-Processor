package io.toolisticon.spiap.processor.tests;

import io.toolisticon.spiap.api.SpiService;
import io.toolisticon.spiap.api.SpiServices;
import io.toolisticon.spiap.processor.serviceprocessortest.AnotherTestSpi;
import io.toolisticon.spiap.processor.serviceprocessortest.TestSpi;

@SpiServices({
        @SpiService(TestSpi.class),
        @SpiService(AnotherTestSpi.class)
})

public class TestcaseMultipleServicesWithSpiServicesAnnotation implements TestSpi, AnotherTestSpi {
    public String doSomething() {
        return "";
    }

    public void anotherThingToDo() {

    }

}