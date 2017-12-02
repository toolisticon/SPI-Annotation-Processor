package io.toolisticon.spiap.processor;

import io.toolisticon.example.spiapexample.api.DecimalCalculationOperationServiceLocator;
import io.toolisticon.example.spiapexample.service.DivisionDecimalOperationImpl;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Test to check if service locator file contains all implementations.
 */
public class GeneratedServiceLocatorFileTest {

    @Test
    public void testIfServiceLocatorFileContainsAllImplementations() throws IOException {

        /*-
        BufferedReader br = new BufferedReader(new InputStreamReader(TestSpi.class.getClassLoader().getResourceAsStream("META-INF/services/io.toolisticon.spiap.processor.testspi.TestSpi")));

        Set<String> lookUpSet = new HashSet<String>();
        while (true) {

            String line = br.readLine();

            if (line == null) {
                break;
            } else if (!line.isEmpty()) {
                lookUpSet.add(line);
            }

        }

        // test for implementations
        MatcherAssert.assertThat(lookUpSet, Matchers.contains(TestSpiImpl1.class.getCanonicalName(), TestSpiImpl2.class.getCanonicalName(), TestSpiImpl3.class.getCanonicalName()));
        */

    }

    @Test
    @Ignore
    public void getServices_forDecimalCalculationOperation() {

        final int operand1 = 5;
        final int operand2 = 3;

        List<io.toolisticon.example.spiapexample.api.DecimalCalculationOperationServiceLocator.ServiceKey> operationServiceKeys = DecimalCalculationOperationServiceLocator.getServiceKeys();

        MatcherAssert.assertThat(operationServiceKeys, Matchers.hasSize(4));

        Set<String> ids = new HashSet<String>();
        for (DecimalCalculationOperationServiceLocator.ServiceKey sk : operationServiceKeys) {
            ids.add(sk.getId());
        }

        // check if id referenced and fqn referenced service implementations have been found
        MatcherAssert.assertThat(ids, Matchers.containsInAnyOrder("MULTIPLICATION", "ADDITION", "SUBTRACTION", DivisionDecimalOperationImpl.class.getCanonicalName()));


    }

}
