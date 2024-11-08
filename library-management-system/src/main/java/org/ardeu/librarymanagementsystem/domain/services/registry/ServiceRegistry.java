package org.ardeu.librarymanagementsystem.domain.services.registry;

import org.ardeu.librarymanagementsystem.domain.services.base.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A singleton class responsible for managing and accessing services in the application.
 * It allows registering services, retrieving them by their class type, and loading or saving their data.
 */
public class ServiceRegistry {

    /**
     * The singleton instance of the ServiceRegistry.
     */
    private static ServiceRegistry instance;

    /**
     * A map that holds the registered services, keyed by their class type.
     */
    private final Map<Class<? extends Service<?>>, Service<?>> services = new HashMap<>();

    /**
     * Private constructor to prevent direct instantiation of the ServiceRegistry.
     */
    private ServiceRegistry() {}

    /**
     * Gets the singleton instance of the ServiceRegistry.
     * Initializes the instance if it doesn't exist, ensuring thread safety.
     *
     * @return the singleton instance of the ServiceRegistry
     */
    public static ServiceRegistry getInstance() {
        if (Objects.isNull(instance)) {
            synchronized (ServiceRegistry.class) {
                if (Objects.isNull(instance)) {
                    instance = new ServiceRegistry();
                }
            }
        }
        return instance;
    }

    /**
     * Registers a service with the given service class type and service instance.
     * If a service of the same class type is already registered, it will not be added again.
     *
     * @param serviceClass the class type of the service to register
     * @param service the service instance to register
     * @param <T> the type of the service
     */
    public <T extends Service<?>> void register(Class<T> serviceClass, T service) {
        this.services.putIfAbsent(serviceClass, service);
    }

    /**
     * Retrieves the registered service for the given class type.
     * Throws an {@link IllegalArgumentException} if no service is registered for the given class type.
     *
     * @param serviceClass the class type of the service to retrieve
     * @param <T> the type of the service
     * @return the registered service instance
     * @throws IllegalArgumentException if no service is found for the given class type
     */
    public <T extends Service<?>> T getService(Class<T> serviceClass) {
        Object service = this.services.get(serviceClass);
        if (Objects.isNull(service)) {
            throw new IllegalArgumentException("Service not found for class: " + serviceClass.getSimpleName());
        }
        return serviceClass.cast(service);
    }

    /**
     * Loads data for all registered services by calling their respective {@link Service#load()} methods.
     */
    public void loadData() {
        this.services.forEach((_, service) -> {
            loadServiceData(service);
        });
    }

    /**
     * Saves data for all registered services by calling their respective {@link Service#save()} methods.
     */
    public void saveData() {
        this.services.forEach((_, service) -> {
            saveServiceData(service);
        });
    }

    /**
     * Loads data for a specific service.
     * Handles any {@link IOException} that occurs during loading by calling {@link #handleServiceError(Service, IOException, String)}.
     *
     * @param service the service whose data needs to be loaded
     */
    private void loadServiceData(Service<?> service) {
        try {
            service.load();
        } catch (IOException e) {
            handleServiceError(service, e, "loading");
        }
    }

    /**
     * Saves data for a specific service.
     * Handles any {@link IOException} that occurs during saving by calling {@link #handleServiceError(Service, IOException, String)}.
     *
     * @param service the service whose data needs to be saved
     */
    private void saveServiceData(Service<?> service) {
        try {
            service.save();
        } catch (IOException e) {
            handleServiceError(service, e, "saving");
        }
    }

    /**
     * Handles errors that occur during loading or saving data for a service.
     * Throws a {@link RuntimeException}.
     *
     * @param service the service that caused the error
     * @param e the {@link IOException} that occurred
     * @param action the action that failed ("loading" or "saving")
     */
    private void handleServiceError(Service<?> service, IOException e, String action) {
        System.err.printf("Error %s data for service %s: %s%n", action, service.getClass().getSimpleName(), e.getMessage());
        throw new RuntimeException(e);
    }
}
