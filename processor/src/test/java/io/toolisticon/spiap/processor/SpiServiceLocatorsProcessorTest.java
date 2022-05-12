package io.toolisticon.spiap.processor;

import io.toolisticon.aptk.tools.MessagerUtils;
import io.toolisticon.cute.CompileTestBuilder;
import io.toolisticon.cute.JavaFileObjectUtils;
import javax.tools.StandardLocation;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests of {@link SpiProcessor}.
 */

public class SpiServiceLocatorsProcessorTest {


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
                .addSources("spiprocessor/spiservicelocator/multipleSpiLocators/package-info.java")
                .compilationShouldSucceed()
            .expectThatGeneratedSourceFileExists("io.toolisticon.spiap.processor.TestSpiServiceLocator")
            .expectThatGeneratedSourceFileExists("io.toolisticon.spiap.processor.AnotherTestSpiServiceLocator")
            .executeTest();

    }

}
