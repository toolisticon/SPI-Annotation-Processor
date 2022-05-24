package ${ package };

import ${ canonicalName };

import io.toolisticon.spiap.api.OutOfService;
import io.toolisticon.spiap.api.SpiService;
import io.toolisticon.spiap.api.SpiServices;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.ServiceLoader;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


/**
 * Service locator for ${ simpleName } to provide SPI implementations.
 */
public enum ${ simpleName }ServiceLocator {

  INSTANCE;


  /**
   * Caches all loaded services until reload is invoked or classloader has been changed.
   */
  private final List<ServiceImplementation> serviceImplementations = new ArrayList<>();

  /** Classloader used for service loading. */
  private ClassLoader classLoaderToUse = null;


  /**
   * Get {@link ServiceImplementation} for {@link ${ simpleName }} implementation.
   *
   * @param serviceImpl
   *          of type ${ simpleName }
   * @return ServiceImplementation
   */
  public static ServiceImplementation getServiceImplementation(
      final ${ simpleName } serviceImpl) {

    // create default config
    ServiceImplementation serviceKey = new ServiceImplementation(serviceImpl);

    // try to get ServiceKey from annotation
    serviceKey = INSTANCE.getServiceImplementationByAnnotations(serviceKey, serviceImpl);
    serviceKey = INSTANCE.getServiceImplementationByProperty(serviceKey, serviceImpl);
    return serviceKey;

  }


  /**
   * Get {@link ServiceImplementation} for all available implementations.
   *
   * @return a list that contains ServiceImplementations for all available service
   *         implementations, or an empty list if none could be found.
   */
  public static List<ServiceImplementation> getServiceImplementations() {

    return new ArrayList<>(INSTANCE.loadImplementations());
  }


  /**
   * Locate a service implementation with a specific id.
   *
   * @param id
   *          the id to search
   * @return the service implementation with the id
   * @throws ImplementationNotFoundException
   *           if either passed id is null or if the service implementation can't be found.
   */
  public static ${ simpleName } locateById(final String id) {

    return locateImplById(id).orElseThrow(() -> new ImplementationNotFoundException(id));
  }


  /**
   * Locate a service implementation with a specific id.
   *
   * @param id
   *          the id to search for.
   * @return {@code Optional<${ simpleName }>} service implementation identified by the
   *         given id.
   */
  public static Optional<${ simpleName }> locateImplById(final String id) {

    if (id == null) {
      return Optional.empty();
    }

    return getServiceImplementations().stream()
        .filter(si -> id.equals(si.getId()))
        .findFirst()
        .map(ServiceImplementation::getServiceInstance);
  }


  /**
   * Locate a service implementation.
   *
   * If more than one implementation is available, the first one is returned.
   *
   * Important: Successive calls may return different service implementations.
   *
   * @return First implementation found via locateAll method call.
   */
  public static ${ simpleName } locate() {

    final List<${ simpleName }> services = locateAll();
    return services.isEmpty()
        ? null
        : services.get(0);
  }


  /**
   * Locate all available service implementations.
   *
   * @return List containing all available service implementations or an empty list, if no
   *         implementation can be found.
   */
  public static List<${ simpleName }> locateAll() {

    return getServiceImplementations().stream()
        .map(ServiceImplementation::getServiceInstance)
        .collect(Collectors.toList());
  }


  /**
   * Clear cache and reload services.
   */
  public static void reloadServices() {

    INSTANCE.serviceImplementations.clear();
    INSTANCE.loadImplementations();
  }


  /**
   * Load service implementations if cache is empty.
   *
   * @return {@code List<ServiceImplementation>} containing the currently available service
   *         implementations for the classloader.
   */
  private List<ServiceImplementation> loadImplementations() {

    if (!serviceImplementations.isEmpty()) {
      return serviceImplementations;
    }

    final Iterator<${ simpleName }> iterator =
        ServiceLoader.load(${ simpleName }.class, getClassLoaderToUse()).iterator();

    serviceImplementations.addAll(
        StreamSupport.stream(Spliterators.spliteratorUnknownSize(
            iterator, Spliterator.ORDERED | Spliterator.NONNULL), false)
            .map(${ simpleName }ServiceLocator::getServiceImplementation)
            .filter(si -> !si.isOutOfService())
            .sorted()
            .collect(Collectors.toList()));
    return serviceImplementations;
  }


  /**
   * Get the service implementation by Service annotation.
   *
   * @param previousConfig
   *          of type ServiceImplementation
   * @param serviceImpl
   *          of type ${ simpleName }
   * @return ServiceImplementation
   */
  private ServiceImplementation getServiceImplementationByAnnotations(
      final ServiceImplementation previousConfig,
      final ${ simpleName } serviceImpl) {

    try {
      final boolean outOfService = serviceImpl.getClass().getAnnotation(OutOfService.class) != null;

      return getSpiServiceAnnotation(serviceImpl)
          .map(s -> new ServiceImplementation(
              previousConfig, s.id(), s.description(), s.priority(), outOfService))
          .orElse(previousConfig);
    } catch (final NoClassDefFoundError ignore) {
      // ignore
    }

    return previousConfig;
  }



  /**
   * Find the service by SpiService annotation.
   *
   * @param serviceImpl
   *          of type ${ simpleName }
   * @return {@code Optional<Service>}
   */
  private Optional<SpiService> getSpiServiceAnnotation(
      final ${ simpleName } serviceImpl) {

    final SpiService serviceAnnotation = serviceImpl.getClass().getAnnotation(SpiService.class);
    if (serviceAnnotation != null) {
      return Optional.of(serviceAnnotation);
    }

    final SpiServices servicesAnnotation = serviceImpl.getClass().getAnnotation(SpiServices.class);
    if (servicesAnnotation != null) {
      return Arrays.stream(servicesAnnotation.value())
          .filter(service -> ${ simpleName }.class.equals(service.value()))
          .findFirst();
    }
    return Optional.empty();
  }

  /**
   * Read the spiap properties file and get ServiceImplementation based on the properties.
   *
   * If not found, return previousConfig implementation.
   *
   * @param previousConfig
   *          of type ServiceImplementation
   * @param serviceImpl
   *          of type ${ simpleName }
   * @return ServiceImplementation
   */
  private ServiceImplementation getServiceImplementationByProperty(
      final ServiceImplementation previousConfig,
      final ${ simpleName } serviceImpl) {

    // first try to get config from property file
    final String propertyFileName = String.format("/META-INF/spiap/%s/%s.properties",
        ${ simpleName }.class.getCanonicalName(),
        serviceImpl.getClass().getCanonicalName());

    return loadServiceProperties(propertyFileName)
        .flatMap(p -> getServiceImplementationFromProperties(previousConfig, p))
        .orElse(previousConfig);
  }


  /**
   * Build ServiceImplementation based on the given properties. If an error occurs return
   * Optional.empty.
   *
   * @param previousConfig
   *          of type ServiceImplementation
   * @param properties
   *          of type Properties
   * @return {@code Optional<ServiceImplementation>}
   */
  private Optional<ServiceImplementation> getServiceImplementationFromProperties(
      final ServiceImplementation previousConfig,
      final Properties properties) {

    if (properties == null) {
      return Optional.empty();
    }

    try {
      final String id = properties.getProperty("id");
      final String description = properties.getProperty("description");
      Integer priority = null;
      try {
        final String priorityString = properties.getProperty("priority");
        if (priorityString != null) {
          priority = Integer.valueOf(priorityString);
        }
      } catch (final NumberFormatException e) {
        // ignore
      }

      Boolean outOfService = null;
      final String outOfServiceString = properties.getProperty("outOfService");
      if (outOfServiceString != null) {
        outOfService = Boolean.valueOf(outOfServiceString);
      }
      return Optional.of(
          new ServiceImplementation(previousConfig, id, description, priority, outOfService));
    } catch (final Exception ignore) {
      // do nothing
    }
    return Optional.empty();
  }


  /**
   * Load the spiap properties file from resources. In terms, an error occurs return an empty
   * Optional.
   *
   * @param propertyFileName
   *          of type String
   * @return {@code Optional<Properties>}
   */
  private Optional<Properties> loadServiceProperties(final String propertyFileName) {

    try (final InputStream inputStream =
        ${ simpleName }ServiceLocator.class.getResourceAsStream(propertyFileName)) {
      if (inputStream == null) {
        return Optional.empty();
      }
      final Properties properties = new Properties();
      properties.load(inputStream);
      return Optional.of(properties);
    } catch (final IOException ignore) {
      // No properties found. Ignore.
    }
    return Optional.empty();
  }


  /**
   * Method getClassLoaderToUse returns the classLoaderToUse of this
   * ${ simpleName }ServiceLocator object.
   *
   *
   *
   * @return the classLoaderToUse (type ClassLoader) of this ${ simpleName }ServiceLocator
   *         object.
   */
  private ClassLoader getClassLoaderToUse() {

    return classLoaderToUse != null
        ? classLoaderToUse
        : Thread.currentThread().getContextClassLoader();
  }


  /**
   * Set the classloader to use.
   * @param classLoader
   *          The class loader to be used to load provider-configuration files and provider classes,
   *          or null if the currents thread is to be used
   */
  public static void setClassLoaderToUse(final ClassLoader classLoader) {

    INSTANCE.serviceImplementations.clear();
    INSTANCE.classLoaderToUse = classLoader;
  }


  /**
   * Exception that is thrown if a specific service implementation cannot be found.
   */
  public static class ImplementationNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -8610400812558665841L;


    public ImplementationNotFoundException(final String id) {

      super(String.format(
          "Could not find implementation for spi ${ simpleName } with id : '%s'", id));
    }
  }

  /**
   * Key class for looking for available spi service implementations.
   */
  public static class ServiceImplementation implements Comparable<ServiceImplementation> {

    private final String id;
    private final String description;
    private final int priority;
    private final boolean outOfService;
    private final ${ simpleName } serviceInstance;


    /**
     * Initial default config
     */
    private ServiceImplementation(final ${ simpleName } serviceInstance) {

      this.serviceInstance = serviceInstance;
      this.id = serviceInstance.getClass().getCanonicalName();
      this.description = "";
      this.priority = 0;
      this.outOfService = false;
    }


    private ServiceImplementation(
        final ServiceImplementation previousConfig,
        final String id,
        final String description,
        final Integer priority,
        final Boolean outOfService) {

      this.serviceInstance = previousConfig.getServiceInstance();
      this.id = id != null ? id : previousConfig.getId();
      this.description = description != null ? description : previousConfig.getDescription();
      this.priority = priority != null ? priority : previousConfig.getPriority();
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


    public boolean isOutOfService() {

      return outOfService;
    }


    public ${ simpleName } getServiceInstance() {

      return serviceInstance;
    }


    /**
     * Compare by priority.
     *
     * @param o
     *          of type ServiceImplementation
     *
     * @return int
     */
    @Override
    public int compareTo(final ServiceImplementation o) {

      if (o == null) {
        return -1;
      }
      if (this == o) {
        return 0;
      }
      if (this.getPriority() == o.getPriority()) {
        return 0;
      } else {
        return this.getPriority() < o.getPriority() ? -1 : 1;
      }
    }
  }
}
