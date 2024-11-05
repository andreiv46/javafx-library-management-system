package org.ardeu.librarymanagementsystem.domain.services.registry;

import org.ardeu.librarymanagementsystem.domain.services.base.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ServiceRegistry {
    private static ServiceRegistry instance;
    private final Map<Class<? extends Service<?>>, Service<?>> services = new HashMap<>();

    private ServiceRegistry() {}

    public static ServiceRegistry getInstance(){
        if(Objects.isNull(instance)){
            synchronized (ServiceRegistry.class){
                if(Objects.isNull(instance)){
                    instance = new ServiceRegistry();
                }
            }
        }
        return instance;
    }

    public <T extends Service<?>> void register(Class<T> serviceClass, T service){
        this.services.putIfAbsent(serviceClass, service);
    }

    public <T extends Service<?>> T getService(Class<T> serviceClass) {
        Object service = this.services.get(serviceClass);
        if(Objects.isNull(service)){
            throw new IllegalArgumentException("Service not found for class: " + serviceClass.getSimpleName());
        }
        return serviceClass.cast(service);
    }

    public void loadData(){
        this.services.forEach((_, service) -> {
            loadServiceData(service);
        });
    }

    public void saveData(){
        this.services.forEach((_, service) -> {
            saveServiceData(service);
        });
    }

    private void loadServiceData(Service<?> service) {
        try {
            service.load();
        } catch (IOException e) {
            handleServiceError(service, e, "loading");
        }
    }

    private void saveServiceData(Service<?> service) {
        try {
            service.save();
        } catch (IOException e) {
            handleServiceError(service, e, "saving");
        }
    }

    private void handleServiceError(Service<?> service, IOException e, String action) {
        System.err.printf("Error %s data for service %s: %s%n", action, service.getClass().getSimpleName(), e.getMessage());
        throw new RuntimeException(e);
    }
}
