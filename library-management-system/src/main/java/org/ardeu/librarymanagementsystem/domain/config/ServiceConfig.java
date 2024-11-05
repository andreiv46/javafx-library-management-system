package org.ardeu.librarymanagementsystem.domain.config;

import org.ardeu.librarymanagementsystem.domain.filerepository.config.FilePathConfig;
import org.ardeu.librarymanagementsystem.domain.filerepository.handlers.BinaryMapFileHandler;
import org.ardeu.librarymanagementsystem.domain.filerepository.handlers.BookMapFileHandler;
import org.ardeu.librarymanagementsystem.domain.filerepository.handlers.InventoryMapFileHandler;
import org.ardeu.librarymanagementsystem.domain.filerepository.handlers.LoanMapFileHandler;
import org.ardeu.librarymanagementsystem.domain.services.*;
import org.ardeu.librarymanagementsystem.domain.services.registry.ServiceRegistry;

public record ServiceConfig(ServiceRegistry serviceRegistry) {
    public ServiceConfig(ServiceRegistry serviceRegistry) {
        this.serviceRegistry = serviceRegistry;
        configureServices();
    }

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
