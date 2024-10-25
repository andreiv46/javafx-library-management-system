package org.ardeu.librarymanagementsystem.services.registry;

import org.ardeu.librarymanagementsystem.services.base.Service;

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
//            throw new Exception("Service not registered");
            System.out.println("service not registered");
            return null;
        }
        return serviceClass.cast(service);
    }

    public void loadData(){
        this.services.forEach((_, service) -> {
            try {
                service.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void saveData(){
        this.services.forEach((_, service) -> {
            try {
                service.save();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
