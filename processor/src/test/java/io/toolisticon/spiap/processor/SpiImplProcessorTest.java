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
public class SpiImplProcessorTest extends AbstractAnnotationProcessorIntegrationTest<SpiImplProcessor> {


    public SpiImplProcessorTest(String description, AnnotationProcessorIntegrationTestConfiguration configuration) {
        super(configuration);
    }

    @Before
    public void init() {
        SpiImplProcessorMessages.setPrintMessageCodes(true);
    }

    @Override
    protected SpiImplProcessor getAnnotationProcessor() {
        return new SpiImplProcessor();
    }

    @Parameterized.Parameters(name = "{index}: \"{0}\"")
    public static List<Object[]> data() {

        return Arrays.asList(new Object[][]{
                {
                        "Test valid usage",
                        AnnotationProcessorIntegrationTestConfigurationBuilder
                                .createTestConfig()
                                .setSourceFileToCompile("spiimplprocessor/TestcaseValidUsage.java")
                                .compilationShouldSucceed()
                                .build()
                },
                {
                        "Test annotation must be placed on class",
                        AnnotationProcessorIntegrationTestConfigurationBuilder
                                .createTestConfig()
                                .setSourceFileToCompile("spiimplprocessor/TestcaseUsageOnInterface.java")
                                .compilationShouldFail()
                                .addMessageValidator()
                                .setErrorChecks(SpiImplProcessorMessages.ERROR_SPI_ANNOTATION_MUST_BE_PLACED_ON_CLASS.getCode())
                                .finishMessageValidator()
                                .build()
                },
                {
                        "Test annotation value attribute must only contain interfaces",
                        AnnotationProcessorIntegrationTestConfigurationBuilder
                                .createTestConfig()
                                .setSourceFileToCompile("spiimplprocessor/TestcaseValueAttributeMustOnlyContainInterfaces.java")
                                .compilationShouldFail()
                                .addMessageValidator()
                                .setErrorChecks(SpiImplProcessorMessages.ERROR_VALUE_ATTRIBUTE_MUST_ONLY_CONTAIN_INTERFACES.getCode())
                                .finishMessageValidator()
                                .build()
                },
                {
                        "Test annotated type must implement configured interfaces",
                        AnnotationProcessorIntegrationTestConfigurationBuilder
                                .createTestConfig()
                                .setSourceFileToCompile("spiimplprocessor/TestcaseMustImplementAnnotatedInterface.java")
                                .compilationShouldFail()
                                .addMessageValidator()
                                .setErrorChecks(SpiImplProcessorMessages.ERROR_ANNOTATED_CLASS_MUST_IMPLEMENT_CONFIGURED_INTERFACES.getCode())
                                .finishMessageValidator()
                                .build()
                },
                {
                        "Test processing should succees with plain interfaces",
                        AnnotationProcessorIntegrationTestConfigurationBuilder
                                .createTestConfig()
                                .setSourceFileToCompile("spiimplprocessor/TestcaseValidUseWithPlainInterface.java")
                                .compilationShouldSucceed()
                                .build()
                },


        });

    }


    @Test
    public void testCorrectnessOfAdviceArgumentAnnotation() {
        super.test();
    }


}