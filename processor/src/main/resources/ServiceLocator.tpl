package ${ package };

import ${ canonicalName };
import io.toolisticon.spiap.api.Service;
import io.toolisticon.spiap.api.Services;
import io.toolisticon.spiap.api.OutOfService;

import java.io.InputStream;
import java.lang.NoClassDefFoundError;
import java.lang.NumberFormatException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Properties;
import java.util.ServiceLoader;


/**
 * A generic service locator.
 */
public class ${ simpleName }ServiceLocator {

    /**
     * Caches all loaded services.
     */
    private static List<ServiceImplementation> serviceImplementations;


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
    public static class ServiceImplementation {

        private final String id;
        private final String description;
        private final int priority;
        private final boolean outOfService;
        private final ${ simpleName } serviceInstance;

        /**
         * Initial default config
         */
        private ServiceImplementation(${ simpleName } serviceInstance) {
            this.serviceInstance = serviceInstance;
            this.id = serviceInstance.getClass().getCanonicalName();
            this.description = "";
            this.priority = 0;
            this.outOfService = false;
        }

        private ServiceImplementation(ServiceImplementation previousConfig, String id, String description, Integer priority, Boolean outOfService) {
            this.serviceInstance = previousConfig.getServiceInstance();
            this.id = id != null ? id : previousConfig.getId();
            this.description = description != null ? description : previousConfig.getDescription();
            this.priority = priority !=null ? priority : previousConfig.getPriority();
            this.outOfService = outOfService != null ? outOfService : previousConfig.isOutOfService();
        }


        public String getId() {
            return id;
        }

        public String getDescription() {
            return description;
        }

        public int getPriority() {
            return priority;
        }

        public boolean isOutOfService () {
            return outOfService;
        }

        public ${ simpleName } getServiceInstance () {
            return serviceInstance;
        }


    }


    private static ServiceImplementation getServiceImplementationByAnnotations(ServiceImplementation previousConfig, ${ simpleName } serviceImpl) {

        try {
            Service serviceAnnotation = serviceImpl.getClass().getAnnotation(Service.class);
            if (serviceAnnotation == null) {
                Services servicesAnnotation = serviceImpl.getClass().getAnnotation(Services.class);
                if (servicesAnnotation != null) {
                    for (Service service : servicesAnnotation.value()) {
                        if (${ simpleName }.class.equals(service.value())) {
                            serviceAnnotation = service;
                            break;
                        }
                    }
                }
            }
            String id = serviceAnnotation != null ? serviceAnnotation.id() : null;
            String description = serviceAnnotation != null  ? serviceAnnotation.description() : null;
            Integer priority = serviceAnnotation != null ? serviceAnnotation.priority() : null;
            boolean outOfService = serviceImpl.getClass().getAnnotation(OutOfService.class) != null;

            return new ServiceImplementation(previousConfig, id, description, priority, outOfService);

        } catch (NoClassDefFoundError e) {
            // ignore
        }

        return previousConfig;

    }

    private static ServiceImplementation getServiceImplementationByProperty(ServiceImplementation previousConfig, ${ simpleName } serviceImpl) {

        // first try to get config from property file
        final String propertyFileName = "/META-INF/spiap/" + ${ simpleName }.class.getCanonicalName() + "/" + serviceImpl.getClass().getCanonicalName() + ".properties";
        InputStream inputStream = ${ simpleName }ServiceLocator.class.getResourceAsStream(propertyFileName);
        if (inputStream != null) {

            try {
                Properties properties = new Properties();
                properties.load(inputStream);

                String id = properties.getProperty("${constants.id}");
                String description = properties.getProperty("${constants.description}");
                Integer priority = null;
                try {
                    String priorityString = properties.getProperty("${constants.priority}");
                    if (priorityString != null) {
                        priority = Integer.valueOf(priorityString);
                    }
                } catch (NumberFormatException e) {
                    // ignore
                }

                Boolean outOfService = null;
                String outOfServiceString = properties.getProperty("${constants.outOfService}");
                if (outOfServiceString != null) {
                    outOfService = Boolean.valueOf(outOfServiceString);
                }

                return new ServiceImplementation(previousConfig, id, description, priority, outOfService);

            } catch (Exception e) {
                // do nothing
            }
        }

        return previousConfig;

    }


    public static ServiceImplementation getServiceImplementation(${ simpleName } serviceImpl) {

        // create default config
        ServiceImplementation serviceKey = new ServiceImplementation(serviceImpl);

        // try to get ServiceKey from annotation
        serviceKey = getServiceImplementationByAnnotations(serviceKey, serviceImpl);

        serviceKey = getServiceImplementationByProperty(serviceKey, serviceImpl);

        return serviceKey;

    }


   /**
    * Comparator which allows sorting of service implementations by priority.
    */
    public static class ServicePriorityComparator implements Comparator<ServiceImplementation> {

        public int compare (ServiceImplementation o1,ServiceImplementation o2){
            if (o1 == null && o2 == null) {
                return 0;
            } else if (o1 != null && o2 == null) {
                return -1;
            } else if (o1 == null && o2 != null) {
                 return -1;
            } else {

                if (o1.priority == o2.priority) {
                    return 0;
                } else {
                    return o1.getPriority() < o2.getPriority() ? -1 : 1;
                }

            }
        }

    }

    /*
     * Get {@link ServiceKey} for all available implementations.
     * @return a list that contains ServiceKeys for all available service implementations, or an empty List if none could be found.
     */
    public static List<${ simpleName }ServiceLocator.ServiceImplementation> getServiceImplementations() {

        locateAll();
        return new ArrayList(serviceImplementations);

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

            locateAll();

            for (ServiceImplementation serviceImpl : serviceImplementations) {

                if (id.equals(serviceImpl.getId())) {
                    return serviceImpl.getServiceInstance();
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
     */
    public static ${ simpleName } locate() {
        final List services = locateAll();
        return services.isEmpty() ? (${ simpleName })null : (${ simpleName })services.get(0);
    }

    /**
     * Locates all available service implementations.
     * @return returns a list containing all available service implementations or an empty list, if no implementation can be found.
     */
    public static List< ${ simpleName } > locateAll() {

        if (serviceImplementations == null) {

            final Iterator<${ simpleName }> iterator = ServiceLoader.load(${ simpleName }.class).iterator();
            final List<ServiceImplementation> tmpServiceImplementations = new ArrayList<ServiceImplementation>();

            while (iterator.hasNext()) {
                try {

                    ${ simpleName } service = iterator.next();

                    ServiceImplementation serviceImpl = getServiceImplementation(service);
                    if (!serviceImpl.isOutOfService()) {
                        tmpServiceImplementations.add(serviceImpl);
                    }

                }
                catch (Error e) {
                    e.printStackTrace(System.err);
                }
            }

            Collections.sort(tmpServiceImplementations, new ServicePriorityComparator());

            serviceImplementations = tmpServiceImplementations;

        }

        // Now return services
        final List<${ simpleName }> services = new ArrayList<${ simpleName }>();

        for (ServiceImplementation service : serviceImplementations) {
            services.add(service.getServiceInstance());
        }

        return services;

    }

    /**
     * Clear cache and reload services.
     */
    public static void reloadServices() {
        serviceImplementations = null;
        locateAll();
    }

}
