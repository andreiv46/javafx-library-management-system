package org.ardeu.librarymanagementsystem.services;

import org.ardeu.librarymanagementsystem.entities.book.Book;
import org.ardeu.librarymanagementsystem.fileio.base.FileHandler;
import org.ardeu.librarymanagementsystem.services.base.Service;

public class BookService extends Service<Book> {

    public BookService(FileHandler<Book> fileHandler) {
        super(fileHandler);
    }
}
