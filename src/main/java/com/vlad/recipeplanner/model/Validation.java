package com.vlad.recipeplanner.model;

import com.vlad.recipeplanner.exception.InvalidRecipeException;

/**
 * Shared validation helpers for model classes.
 */
final class Validation {

    private Validation() {
    }

    static String requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new InvalidRecipeException(fieldName + " must not be empty");
        }
        String trimmed = value.trim();
        for (char c : Ingredient.FORBIDDEN_CHARS.toCharArray()) {
            if (trimmed.indexOf(c) >= 0) {
                throw new InvalidRecipeException(fieldName + " must not contain '" + c + "'");
            }
        }
        return trimmed;
    }
}
