package org.ardeu.librarymanagementsystem.services.base;

import java.io.IOException;

public interface DataService {
    void save() throws IOException;
    void load() throws IOException;
}