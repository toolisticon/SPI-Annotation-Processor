package io.toolisticon.spiap.processor;

import io.toolisticon.example.spiapexample.api.DecimalCalculationOperation;
import io.toolisticon.example.spiapexample.api.DecimalCalculationOperationServiceLocator;
import io.toolisticon.example.spiapexample.service.DivisionDecimalOperationImpl;
import io.toolisticon.example.spiapexample.service.MultiplicationDecimalOperationImpl;
import io.toolisticon.spiap.api.SpiImpl;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Test to check if service locator file contains all implementations.
 */
public class GeneratedServiceLocatorFileTest {


    @Test
    public void getServiceKeys_forDecimalCalculationOperation() {

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


    @Test
    public void locateAll_locateAllServices() {

        List<DecimalCalculationOperation> services = DecimalCalculationOperationServiceLocator.locateAll();

        Set<String> serviceIds = new HashSet<String>();

        for (DecimalCalculationOperation service : services) {
            String id = service.getClass().getAnnotation(SpiImpl.class).id();
            serviceIds.add(id != null && !id.isEmpty() ? id : service.getClass().getCanonicalName());
        }

        // check if id referenced and fqn referenced service implementations have been found
        MatcherAssert.assertThat(serviceIds, Matchers.containsInAnyOrder("MULTIPLICATION", "ADDITION", "SUBTRACTION", DivisionDecimalOperationImpl.class.getCanonicalName()));

    }

    @Test
    public void locate_getOneService() {

        MatcherAssert.assertThat(DecimalCalculationOperationServiceLocator.locate(), Matchers.notNullValue());

    }

    @Test
    public void locateById_locateServiceById() {

        DecimalCalculationOperation result = DecimalCalculationOperationServiceLocator.locateById("MULTIPLICATION");

        MatcherAssert.assertThat(result, Matchers.notNullValue());
        MatcherAssert.assertThat(result, Matchers.instanceOf(MultiplicationDecimalOperationImpl.class));

    }

    @Test(expected = DecimalCalculationOperationServiceLocator.ImplementationNotFoundException.class)
    public void locateById_locateServiceById_withNullValue() {

        DecimalCalculationOperation result = DecimalCalculationOperationServiceLocator.locateById(null);


    }

    @Test(expected = DecimalCalculationOperationServiceLocator.ImplementationNotFoundException.class)
    public void locateById_locateServiceById_withNonexistingId() {

        DecimalCalculationOperation result = DecimalCalculationOperationServiceLocator.locateById("XXX");


    }

    @Test
    public void locateByServiceKey_locateServiceByServiceKey() {

        DecimalCalculationOperation result = DecimalCalculationOperationServiceLocator.locateByServiceKey(
                new DecimalCalculationOperationServiceLocator.ServiceKey("MULTIPLICATION"));

        MatcherAssert.assertThat(result, Matchers.notNullValue());
        MatcherAssert.assertThat(result, Matchers.instanceOf(MultiplicationDecimalOperationImpl.class));

    }

    @Test(expected = DecimalCalculationOperationServiceLocator.ImplementationNotFoundException.class)
    public void locateByServiceKey_locateServiceById_withNullValue() {

        DecimalCalculationOperation result = DecimalCalculationOperationServiceLocator.locateByServiceKey(null);


    }

    @Test(expected = DecimalCalculationOperationServiceLocator.ImplementationNotFoundException.class)
    public void locateByServiceKey_locateServiceById_withNonexistingId() {

        DecimalCalculationOperation result = DecimalCalculationOperationServiceLocator.locateByServiceKey(
                new DecimalCalculationOperationServiceLocator.ServiceKey("XXX"));


    }


}
