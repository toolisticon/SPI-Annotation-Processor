package io.toolisticon.spiap.integrationtest;

import io.toolisticon.example.spiapexample.api.DecimalCalculationOperation;
import io.toolisticon.example.spiapexample.api.DecimalCalculationOperationServiceLocator;
import io.toolisticon.example.spiapexample.service.AdditionDecimalOperationImpl;
import io.toolisticon.example.spiapexample.service.DivisionDecimalOperationImpl;
import io.toolisticon.example.spiapexample.service.MultiplicationDecimalOperationImpl;
import io.toolisticon.example.spiapexample.service.SubtractionDecimalOperationImpl;
import io.toolisticon.spiap.interationtest.SquareDecimalOperationServiceImpl;
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
    public void getServiceImplementations_forDecimalCalculationOperation() {

        final int operand1 = 5;
        final int operand2 = 3;

        List<io.toolisticon.example.spiapexample.api.DecimalCalculationOperationServiceLocator.ServiceImplementation> operationServiceKeys = DecimalCalculationOperationServiceLocator.getServiceImplementations();

        MatcherAssert.assertThat(operationServiceKeys, Matchers.hasSize(5));

        Set<String> ids = new HashSet<String>();
        for (DecimalCalculationOperationServiceLocator.ServiceImplementation sk : operationServiceKeys) {
            ids.add(sk.getId());
        }

        // check if id referenced and fqn referenced service implementations have been found
        MatcherAssert.assertThat(ids, Matchers.containsInAnyOrder("SQUARE", "MULTIPLICATION", "ADDITION", "SUBTRACTION", DivisionDecimalOperationImpl.class.getCanonicalName()));


    }


    @Test
    public void locateAll_locateAllServices() {

        List<DecimalCalculationOperation> services = DecimalCalculationOperationServiceLocator.locateAll();

        Set<String> serviceIds = new HashSet<String>();

        for (DecimalCalculationOperation service : services) {
            serviceIds.add(service.getClass().getCanonicalName());
        }

        // check if id referenced and fqn referenced service implementations have been found
        MatcherAssert.assertThat(serviceIds, Matchers.containsInAnyOrder(SquareDecimalOperationServiceImpl.class.getCanonicalName(), MultiplicationDecimalOperationImpl.class.getCanonicalName(), AdditionDecimalOperationImpl.class.getCanonicalName(), SubtractionDecimalOperationImpl.class.getCanonicalName(), DivisionDecimalOperationImpl.class.getCanonicalName()));

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
    public void locateAll_checkOrder() {

        List<DecimalCalculationOperation> result = DecimalCalculationOperationServiceLocator.locateAll();

        MatcherAssert.assertThat(result, Matchers.hasSize(5));

        MatcherAssert.assertThat(result.get(0), Matchers.instanceOf(SquareDecimalOperationServiceImpl.class));
        MatcherAssert.assertThat(result.get(1), Matchers.instanceOf(AdditionDecimalOperationImpl.class));
        MatcherAssert.assertThat(result.get(2), Matchers.instanceOf(SubtractionDecimalOperationImpl.class));
        MatcherAssert.assertThat(result.get(3), Matchers.instanceOf(DivisionDecimalOperationImpl.class));
        MatcherAssert.assertThat(result.get(4), Matchers.instanceOf(MultiplicationDecimalOperationImpl.class));


    }


    @Test
    public void getServiceKeys_checkOrder() {

        List<DecimalCalculationOperationServiceLocator.ServiceImplementation> result = DecimalCalculationOperationServiceLocator.getServiceImplementations();

        MatcherAssert.assertThat(result, Matchers.hasSize(5));

        MatcherAssert.assertThat(result.get(0).getId(), Matchers.is("SQUARE"));
        MatcherAssert.assertThat(result.get(1).getId(), Matchers.is("ADDITION"));
        MatcherAssert.assertThat(result.get(2).getId(), Matchers.is("SUBTRACTION"));
        MatcherAssert.assertThat(result.get(3).getId(), Matchers.is("io.toolisticon.example.spiapexample.service.DivisionDecimalOperationImpl"));
        MatcherAssert.assertThat(result.get(4).getId(), Matchers.is("MULTIPLICATION"));


    }


}
