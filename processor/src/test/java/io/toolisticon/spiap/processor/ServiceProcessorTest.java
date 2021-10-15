package io.toolisticon.spiap.processor;


import io.toolisticon.aptk.tools.MessagerUtils;
import io.toolisticon.cute.CompileTestBuilder;
import io.toolisticon.cute.JavaFileObjectUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests of {@link SpiProcessor}.
 */
public class ServiceProcessorTest {


    @Before
    public void init() {
        MessagerUtils.setPrintMessageCodes(true);
    }


    CompileTestBuilder.CompilationTestBuilder compileTestBuilder = CompileTestBuilder
            .compilationTest()
            .addProcessors(ServiceProcessor.class);

    @Test
    public void test_validUsage() {

        compileTestBuilder
                .addSources("serviceprocessor/TestcaseValidUsage.java")
                .compilationShouldSucceed()
                .executeTest();
    }


    @Test
    public void test_annotationMustBePlacedOnClass() {
        compileTestBuilder
                .addSources("serviceprocessor/TestcaseUsageOnInterface.java")
                .compilationShouldFail()
                .expectErrorMessageThatContains(ServiceProcessorMessages.ERROR_SPI_ANNOTATION_MUST_BE_PLACED_ON_CLASS.getCode())
                .executeTest();
    }


    @Test
    public void test_annotationValueAttributeMustOnlyContainInterfaces() {
        compileTestBuilder
                .addSources("serviceprocessor/TestcaseValueAttributeMustOnlyContainInterfaces.java")
                .compilationShouldFail()
                .expectErrorMessageThatContains(ServiceProcessorMessages.ERROR_VALUE_ATTRIBUTE_MUST_ONLY_CONTAIN_INTERFACES.getCode())
                .executeTest();
    }

    @Test
    public void test_annotatedTypeMustImplementConfiguredInterfaces() {
        compileTestBuilder
                .addSources("serviceprocessor/TestcaseMustImplementAnnotatedInterface.java")
                .compilationShouldFail()
                .expectErrorMessageThatContains(ServiceProcessorMessages.ERROR_ANNOTATED_CLASS_MUST_IMPLEMENT_CONFIGURED_INTERFACES.getCode())
                .executeTest();
    }

    @Test
    public void test_processingShouldSucceesWithPlainInterfaces() {
        compileTestBuilder
                .addSources("serviceprocessor/TestcaseValidUseWithPlainInterface.java")
                .compilationShouldSucceed()
                .executeTest();
    }

    @Test
    public void test_multipleServicesImplemented() {
        compileTestBuilder
                .addSources("serviceprocessor/TestcaseMultipleServices.java")
                .compilationShouldSucceed()
                .executeTest();
    }


    @Test
    public void test_OutOfServiceAnnotatedServicesShouldntBeProcessed() {
        compileTestBuilder
                .addSources("serviceprocessor/TestcaseDontProcessOutOfServiceService.java")
                .expectNoteMessageThatContains(ServiceProcessorMessages.INFO_SKIP_ELEMENT_ANNOTATED_AS_OUT_OF_SERVICE.getCode())
                .compilationShouldSucceed()
                .executeTest();

    }


}

