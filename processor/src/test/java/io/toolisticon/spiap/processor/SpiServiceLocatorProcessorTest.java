package io.toolisticon.spiap.processor;

import io.toolisticon.annotationprocessortoolkit.tools.MessagerUtils;
import io.toolisticon.cute.CompileTestBuilder;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests of {@link SpiProcessor}.
 */

public class SpiServiceLocatorProcessorTest {


    @Before
    public void init() {
        MessagerUtils.setPrintMessageCodes(true);
    }

    private CompileTestBuilder.CompilationTestBuilder compileTestBuilder = CompileTestBuilder
            .compilationTest()
            .addProcessors(SpiProcessor.class);

    @Test
    public void test_validUsage() {

        compileTestBuilder
                .addSources("spiprocessor/spiservicelocator/validusage/package-info.java")
                .compilationShouldSucceed()
                .executeTest();

    }

    @Test
    public void test_testAnnotationWasPutOnClass() {
        compileTestBuilder
                .addSources("spiprocessor/spiservicelocator/classAsValueAttribute/package-info.java")
                .compilationShouldFail()
                .expectErrorMessageThatContains(SpiProcessorMessages.ERROR_SERVICE_LOCATOR_PASSED_SPI_CLASS_MUST_BE_AN_INTERFACE.getCode())
                .executeTest();
    }

}