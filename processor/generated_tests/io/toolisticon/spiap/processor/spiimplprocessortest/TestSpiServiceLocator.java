package io.toolisticon.spiap.processor.serviceprocessortest;

import io.toolisticon.spiap.processor.serviceprocessortest.TestSpi;
import io.toolisticon.spiap.api.SpiImpl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.ServiceLoader;

/**
 * A generic service locator.
 */
public class TestSpiServiceLocator {

    /**
     * Exception that is thrown if a specific service implementation can't be found.
     */
     public static class ImplementationNotFoundException extends RuntimeException{

            public ImplementationNotFoundException(String id) {
                super(String.format("Couldn't find implementation for spi TestSpi with id : '%s'",id));
            }

        }

    /**
     * Key class for looking checking available spi service implementations.
     */
    public static class ServiceKey {

        private final String id;
        private final String description;

        private ServiceKey(TestSpi serviceImpl) {

            SpiImpl spiImplAnnotation = serviceImpl.getClass().getAnnotation(SpiImpl.class);

            id = spiImplAnnotation != null && !spiImplAnnotation.id().equals("") ? spiImplAnnotation.id() : serviceImpl.getClass().getCanonicalName();
            description = spiImplAnnotation != null && !spiImplAnnotation.description().equals("") ? spiImplAnnotation.description() : "";

        }

        public ServiceKey(String id) {
            this (id, "");
        }

        public ServiceKey(String id, String description) {
            this.id = id;
            this.description = description;
        }

        public String getId() {
            return id;
        }

        public String getDescription() {
            return description;
        }


    }

    /*
     * Get {@link ServiceKey} for all available implementations.
     * @return a list that contains ServiceKeys for all available service implementations, or an empty List if none could be found.
     */
    public static List<TestSpiServiceLocator.ServiceKey> getServiceKeys() {
        List<TestSpiServiceLocator.ServiceKey> result = new ArrayList<TestSpiServiceLocator.ServiceKey>();

        for (TestSpi serviceImpl : locateAll()) {
            result.add(new TestSpiServiceLocator.ServiceKey(serviceImpl));
        }

        return result;
    }

    /**
     * locate a service implementation with a specific {@link ServiceKey}.
     *
     * @param serviceKey the service key to search
     * @return the service implementation with the service key
     * @throws ImplementationNotFoundException if either passed service key, it's id are null or if the service implementation can't be found.
     */
    public static TestSpi locateByServiceKey(TestSpiServiceLocator.ServiceKey serviceKey) {

        if (serviceKey == null) {
            throw new ImplementationNotFoundException(null);
        }

        return locateById(serviceKey.getId());

    }

    /**
     * locate a service implementation with a specific id.
     *
     * @param id the id to search
     * @return the service implementation with the id
     * @throws ImplementationNotFoundException if either passed id is null or if the service implementation can't be found.
     */
    public static TestSpi locateById(String id) {

        if (id != null) {
            for (TestSpi serviceImpl : locateAll()) {

                ServiceKey sk = new ServiceKey(serviceImpl);
                if (id.equals(sk.getId())) {
                    return serviceImpl;
                }
            }
        }

        throw new ImplementationNotFoundException(id);

    }

    /**
     * Hide constructor.
     */
    private TestSpiServiceLocator() {
    }

    /**
     * Locates a service implementation.
     * The first implementation returned by the locateAll method will be returned if more than one implementation is available.
     * Successive calls may return different service implementations.
     * @return returns the first Implementation found via locateAll method call.
     **/
    public static TestSpi locate() {
        final List services = locateAll();
        return services.isEmpty() ? (TestSpi)null : (TestSpi)services.get(0);
    }

    /**
     * Locates all available service implementations.
     * @return returns a list containing all available service implementations or an empty list, if no implementation can be found.
     */
    public static List< TestSpi > locateAll() {

        final Iterator<TestSpi> iterator = ServiceLoader.load(TestSpi.class).iterator();
        final List<TestSpi> services = new ArrayList<TestSpi>();

        while (iterator.hasNext()) {
            try {
                services.add(iterator.next());
            }
            catch (Error e) {
                e.printStackTrace(System.err);
            }
        }

        return services;

    }

}
