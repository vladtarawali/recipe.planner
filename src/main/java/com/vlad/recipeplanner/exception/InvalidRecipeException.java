package com.vlad.recipeplanner.exception;

/**
 * Thrown when recipe or ingredient data is invalid (issue #1).
 */
public class InvalidRecipeException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InvalidRecipeException(String message) {
        super(message);
    }
}
