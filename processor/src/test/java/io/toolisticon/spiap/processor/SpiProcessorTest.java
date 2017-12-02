package io.toolisticon.spiap.processor;

import de.holisticon.annotationprocessortoolkit.testhelper.AbstractAnnotationProcessorIntegrationTest;
import de.holisticon.annotationprocessortoolkit.testhelper.integrationtest.AnnotationProcessorIntegrationTestConfiguration;
import de.holisticon.annotationprocessortoolkit.testhelper.integrationtest.AnnotationProcessorIntegrationTestConfigurationBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;

/**
 * Tests of {@link SpiProcessor}.
 */
@RunWith(Parameterized.class)
public class SpiProcessorTest extends AbstractAnnotationProcessorIntegrationTest<SpiProcessor> {


    public SpiProcessorTest(String description, AnnotationProcessorIntegrationTestConfiguration configuration) {
        super(configuration);
    }

    @Before
    public void init() {
        SpiProcessorMessages.setPrintMessageCodes(true);
    }

    @Override
    protected SpiProcessor getAnnotationProcessor() {
        return new SpiProcessor();
    }

    @Parameterized.Parameters(name = "{index}: \"{0}\"")
    public static List<Object[]> data() {

        return Arrays.asList(new Object[][]{
                {
                        "Test valid usage",
                        AnnotationProcessorIntegrationTestConfigurationBuilder
                                .createTestConfig()
                                .setSourceFileToCompile("spiprocessor/TestcaseValidUsage.java")
                                .compilationShouldSucceed()
                                .build()
                },
                {
                        "Test annotation was put on class",
                        AnnotationProcessorIntegrationTestConfigurationBuilder
                                .createTestConfig()
                                .setSourceFileToCompile("spiprocessor/TestcaseAnnotationOnClass.java")
                                .compilationShouldFail()
                                .addMessageValidator()
                                .setErrorChecks(SpiProcessorMessages.ERROR_SPI_ANNOTATION_MUST_BE_PLACED_ON_INTERFACE.getCode())
                                .finishMessageValidator()
                                .build()
                },


        });

    }


    @Test
    public void testCorrectnessOfAdviceArgumentAnnotation() {
        super.test();
    }


}