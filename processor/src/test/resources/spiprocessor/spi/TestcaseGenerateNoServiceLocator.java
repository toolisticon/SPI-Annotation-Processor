package io.toolisticon.spiap.processor.tests;

import io.toolisticon.spiap.api.Spi;

@Spi(generateServiceLocator = false)
public interface TestcaseGenerateNoServiceLocator {
    String doSomething();
}