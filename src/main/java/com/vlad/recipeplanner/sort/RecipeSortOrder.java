package com.vlad.recipeplanner.sort;

import com.vlad.recipeplanner.model.Recipe;

import java.util.Comparator;

/**
 * Available sort orders for recipes (issue #3).
 * Ties are broken by name so the order is always stable and predictable.
 */
public enum RecipeSortOrder {

    BY_NAME(Comparator.comparing(Recipe::getName, String.CASE_INSENSITIVE_ORDER)),
    BY_CALORIES(Comparator.comparingInt(Recipe::getCalories)
            .thenComparing(Recipe::getName, String.CASE_INSENSITIVE_ORDER)),
    BY_PREP_TIME(Comparator.comparingInt(Recipe::getPrepTimeMinutes)
            .thenComparing(Recipe::getName, String.CASE_INSENSITIVE_ORDER));

    private final Comparator<Recipe> comparator;

    RecipeSortOrder(Comparator<Recipe> comparator) {
        this.comparator = comparator;
    }

    public Comparator<Recipe> ascending() {
        return comparator;
    }

    public Comparator<Recipe> descending() {
        return comparator.reversed();
    }
}
