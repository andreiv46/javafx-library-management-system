package org.ardeu.librarymanagementsystem.domain.services.base;

import java.io.IOException;

public interface DataService {
    void save() throws IOException;
    void load() throws IOException;
}
