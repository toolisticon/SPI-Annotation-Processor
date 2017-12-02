package ${ package };

import ${ canonicalName };
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
public class ${ simpleName }ServiceLocator {

    /**
     * Exception that is thrown if a specific service implementation can't be found.
     */
     public static class ImplementationNotFoundException extends RuntimeException{

            public ImplementationNotFoundException(String id) {
                super(String.format("Couldn't find implementation for spi ${simpleName} with id : '%s'",id));
            }

        }

    /**
     * Key class for looking checking available spi service implementations.
     */
    public static class ServiceKey {

        private final String id;
        private final String description;

        private ServiceKey(${ simpleName } serviceImpl) {

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
    public static List<${ simpleName }ServiceLocator.ServiceKey> getServiceKeys() {
        List<${ simpleName }ServiceLocator.ServiceKey> result = new ArrayList<${ simpleName }ServiceLocator.ServiceKey>();

        for (${ simpleName } serviceImpl : locateAll()) {
            result.add(new ${ simpleName }ServiceLocator.ServiceKey(serviceImpl));
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
    public static ${ simpleName } locateByServiceKey(${ simpleName }ServiceLocator.ServiceKey serviceKey) {

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
    public static ${ simpleName } locateById(String id) {

        if (id != null) {
            for (${ simpleName } serviceImpl : locateAll()) {

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
    private ${ simpleName }ServiceLocator() {
    }

    /**
     * Locates a service implementation.
     * The first implementation returned by the locateAll method will be returned if more than one implementation is available.
     * Successive calls may return different service implementations.
     * @return returns the first Implementation found via locateAll method call.
     **/
    public static ${ simpleName } locate() {
        final List services = locateAll();
        return services.isEmpty() ? (${ simpleName })null : (${ simpleName })services.get(0);
    }

    /**
     * Locates all available service implementations.
     * @return returns a list containing all available service implementations or an empty list, if no implementation can be found.
     */
    public static List< ${ simpleName } > locateAll() {

        final Iterator<${ simpleName }> iterator = ServiceLoader.load(${ simpleName }.class).iterator();
        final List<${ simpleName }> services = new ArrayList<${ simpleName }>();

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
