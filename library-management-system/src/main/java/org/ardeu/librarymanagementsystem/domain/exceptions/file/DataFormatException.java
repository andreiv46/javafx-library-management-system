package org.ardeu.librarymanagementsystem.domain.exceptions.file;

import java.io.IOException;

public class DataFormatException extends IOException {
    public DataFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataFormatException(String message) {
        super(message);
    }
}
