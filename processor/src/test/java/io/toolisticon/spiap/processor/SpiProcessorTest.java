package io.toolisticon.spiap.processor;

import io.toolisticon.annotationprocessortoolkit.tools.MessagerUtils;
import io.toolisticon.compiletesting.CompileTestBuilder;
import io.toolisticon.compiletesting.JavaFileObjectUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests of {@link SpiProcessor}.
 */

public class SpiProcessorTest {

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
                .addSources(JavaFileObjectUtils.readFromResource("/spiprocessor/spi/TestcaseValidUsage.java"))
                .compilationShouldSucceed()
                .testCompilation();

    }

    @Test
    public void test_testAnnotationWasPutOnClass() {

        compileTestBuilder
                .addSources(JavaFileObjectUtils.readFromResource("/spiprocessor/spi/TestcaseAnnotationOnClass.java"))
                .compilationShouldFail()
                .expectedErrorMessages(SpiProcessorMessages.ERROR_SPI_ANNOTATION_MUST_BE_PLACED_ON_INTERFACE.getCode())
                .testCompilation();

    }


}