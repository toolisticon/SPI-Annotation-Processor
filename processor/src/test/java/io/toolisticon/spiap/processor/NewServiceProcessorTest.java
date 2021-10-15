package io.toolisticon.spiap.processor;


import io.toolisticon.aptk.tools.MessagerUtils;
import io.toolisticon.cute.CompileTestBuilder;
import io.toolisticon.cute.GeneratedFileObjectMatcher;
import io.toolisticon.cute.JavaFileObjectUtils;
import io.toolisticon.cute.CompileTestBuilder;
import org.junit.Before;
import org.junit.Test;

import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;


/**
 * Tests of {@link SpiProcessor}.
 */

public class NewServiceProcessorTest {


    @Before
    public void init() {

        MessagerUtils.setPrintMessageCodes(true);

        compileTestBuilder = CompileTestBuilder
                .compilationTest()
                .addProcessors(ServiceProcessor.class);

    }


    CompileTestBuilder.CompilationTestBuilder compileTestBuilder;


    @Test
    public void test_valid_usage() {

        compileTestBuilder
                .addSources("serviceprocessor/TestcaseValidUsage.java")
                .compilationShouldSucceed()
                .expectThatFileObjectExists(StandardLocation.CLASS_OUTPUT, "META-INF.services", "io.toolisticon.spiap.processor.serviceprocessortest.TestSpi", JavaFileObjectUtils.readFromString("testcase", "io.toolisticon.spiap.processor.tests.TestcaseValidUsage\n"))
                .expectThatFileObjectExists(StandardLocation.CLASS_OUTPUT, "META-INF.services", "io.toolisticon.spiap.processor.serviceprocessortest.TestSpi", new GeneratedFileObjectMatcher<FileObject>() {
                    @Override
                    public boolean check(FileObject fileObject) throws IOException {
                        return fileObject.getCharContent(false).toString().contains("processor");
                    }
                })
                .executeTest();
    }


    @Test
    public void test_annotation_must_be_placed_on_class() {

        compileTestBuilder
                .addSources("serviceprocessor/TestcaseUsageOnInterface.java")
                .compilationShouldFail()
                .expectErrorMessageThatContains(ServiceProcessorMessages.ERROR_SPI_ANNOTATION_MUST_BE_PLACED_ON_CLASS.getCode())
                .executeTest();
    }


    @Test
    public void test_annotation_value_attribute_must_only_contain_interfaces() {

        compileTestBuilder
                .addSources("serviceprocessor/TestcaseValueAttributeMustOnlyContainInterfaces.java")
                .compilationShouldFail()
                .expectErrorMessageThatContains(ServiceProcessorMessages.ERROR_VALUE_ATTRIBUTE_MUST_ONLY_CONTAIN_INTERFACES.getCode())
                .executeTest();
    }


    @Test
    public void test_annotated_type_must_implement_configured_interfaces() {

        compileTestBuilder
                .addSources("serviceprocessor/TestcaseMustImplementAnnotatedInterface.java")
                .compilationShouldFail()
                .expectErrorMessageThatContains(ServiceProcessorMessages.ERROR_ANNOTATED_CLASS_MUST_IMPLEMENT_CONFIGURED_INTERFACES.getCode())
                .executeTest();
    }


    @Test
    public void test_processing_should_succees_with_plain_interfaces() {

        compileTestBuilder
                .addSources("serviceprocessor/TestcaseValidUseWithPlainInterface.java")
                .compilationShouldSucceed()
                .executeTest();
    }


    @Test
    public void test_multiple_services_implemented() {

        compileTestBuilder
                .addSources("serviceprocessor/TestcaseMultipleServices.java")
                .compilationShouldSucceed()
                .executeTest();
    }


    @Test
    public void test_OutOfService_annotated_services_shouldnt_be_processed() {

        compileTestBuilder
                .addSources("serviceprocessor/TestcaseDontProcessOutOfServiceService.java")
                .expectNoteMessageThatContains(ServiceProcessorMessages.INFO_SKIP_ELEMENT_ANNOTATED_AS_OUT_OF_SERVICE.getCode())
                .compilationShouldSucceed()
                .executeTest();
    }


}