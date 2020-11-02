package io.toolisticon.spiap.processor;

import io.toolisticon.annotationprocessortoolkit.tools.MessagerUtils;
import io.toolisticon.cute.CompileTestBuilder;
import org.junit.Before;
import org.junit.Test;

import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;

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
                .addSources("/spiprocessor/spi/TestcaseValidUsage.java")
                .compilationShouldSucceed()
                .expectThatJavaFileObjectExists(
                        StandardLocation.SOURCE_OUTPUT,
                        "io.toolisticon.spiap.processor.tests.TestcaseValidUsageServiceLocator",
                        JavaFileObject.Kind.SOURCE
                )
                .executeTest();

    }

    @Test
    public void test_testAnnotationWasPutOnClass() {

        compileTestBuilder
                .addSources("/spiprocessor/spi/TestcaseAnnotationOnClass.java")
                .compilationShouldFail()
                .expectErrorMessageThatContains(SpiProcessorMessages.ERROR_SPI_ANNOTATION_MUST_BE_PLACED_ON_INTERFACE.getCode())
                .executeTest();

    }

    @Test
    public void test_shouldNotGenerateServiceLocator() {
        compileTestBuilder
                .addSources("/spiprocessor/spi/TestcaseGenerateNoServiceLocator.java")
                .compilationShouldSucceed()
                .expectThatGeneratedSourceFileDoesntExist("io.toolisticon.spiap.processor.tests.TestcaseGenerateNoServiceLocatorServiceLocator")
                .executeTest();
    }

}