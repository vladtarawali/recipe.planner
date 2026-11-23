package com.vlad.recipeplanner.exception;

/**
 * Thrown when data cannot be saved to or loaded from storage (issue #5).
 */
public class StorageException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }

    public StorageException(String message) {
        super(message);
    }
}
