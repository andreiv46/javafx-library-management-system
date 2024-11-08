package org.ardeu.librarymanagementsystem.domain.config;

import org.ardeu.librarymanagementsystem.domain.filerepository.config.FilePathConfig;
import org.ardeu.librarymanagementsystem.domain.filerepository.handlers.BinaryMapFileHandler;
import org.ardeu.librarymanagementsystem.domain.filerepository.handlers.BookMapFileHandler;
import org.ardeu.librarymanagementsystem.domain.filerepository.handlers.InventoryMapFileHandler;
import org.ardeu.librarymanagementsystem.domain.filerepository.handlers.LoanMapFileHandler;
import org.ardeu.librarymanagementsystem.domain.services.*;
import org.ardeu.librarymanagementsystem.domain.services.registry.ServiceRegistry;

/**
 * Configuration class for registering and configuring services in the {@link ServiceRegistry}.
 * <p>
 * This class is responsible for creating and registering services that interact with various data
 * sources. It uses specific file handlers for each service to persist data to file paths
 * defined in {@link FilePathConfig}.
 */
public record ServiceConfig(ServiceRegistry serviceRegistry) {

    /**
     * Constructs a {@link ServiceConfig} instance and configures the services by registering them
     * in the {@link ServiceRegistry}.
     *
     * @param serviceRegistry the service registry to register services with
     */
    public ServiceConfig(ServiceRegistry serviceRegistry) {
        this.serviceRegistry = serviceRegistry;
        configureServices();
    }

    /**
     * Registers the various services in the {@link ServiceRegistry}. Each service is configured with
     * an appropriate file handler to manage its respective data.
     */
    public void configureServices() {
        serviceRegistry.register(
                AuthorService.class,
                new AuthorService(new BinaryMapFileHandler<>(FilePathConfig.AUTHORS_PATH)));

        serviceRegistry.register(
                BookService.class,
                new BookService(new BookMapFileHandler(FilePathConfig.BOOKS_PATH)));

        serviceRegistry.register(
                GenreService.class,
                new GenreService(new BinaryMapFileHandler<>(FilePathConfig.GENRES_PATH)));

        serviceRegistry.register(
                InventoryService.class,
                new InventoryService(new InventoryMapFileHandler(FilePathConfig.INVENTORIES_PATH)));

        serviceRegistry.register(
                LoanService.class,
                new LoanService(new LoanMapFileHandler(FilePathConfig.LOANS_PATH)));

        serviceRegistry.register(
                MemberService.class,
                new MemberService(new BinaryMapFileHandler<>(FilePathConfig.MEMBERS_PATH)));
    }
}

