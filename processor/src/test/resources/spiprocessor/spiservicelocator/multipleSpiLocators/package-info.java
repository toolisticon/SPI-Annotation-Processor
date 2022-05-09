/**
 * This package contains the annotation processors.
 */
@SpiServiceLocators({
    @SpiServiceLocator(TestSpi.class),
    @SpiServiceLocator(AnotherTestSpi.class)
})
package io.toolisticon.spiap.processor;

import io.toolisticon.spiap.api.SpiServiceLocator;
import io.toolisticon.spiap.api.SpiServiceLocators;
import io.toolisticon.spiap.processor.serviceprocessortest.AnotherTestSpi;
import io.toolisticon.spiap.processor.serviceprocessortest.TestSpi;
