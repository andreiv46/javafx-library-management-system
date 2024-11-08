package org.ardeu.librarymanagementsystem.domain.exceptions.file;

import java.io.IOException;

/**
 * Exception thrown when there is an issue with the format of data being processed.
 * This custom exception is used to signal problems such as invalid or corrupted data format,
 * typically occurring during data reading or writing operations.
 */
public class DataFormatException extends IOException {

    /**
     * Constructs a new {@link DataFormatException} with the specified detail message and cause.
     *
     * @param message the detail message (which is saved for later retrieval by the {@link Throwable#getMessage()} method)
     * @param cause   the cause of the exception (which is saved for later retrieval by the {@link Throwable#getCause()} method)
     */
    public DataFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}
