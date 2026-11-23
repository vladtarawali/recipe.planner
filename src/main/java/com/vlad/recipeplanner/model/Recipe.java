package com.vlad.recipeplanner.model;

import com.vlad.recipeplanner.exception.InvalidRecipeException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * A recipe with ingredients, calories and preparation time (issue #1).
 * All data is validated in the constructor, so an invalid Recipe can never exist.
 */
public class Recipe {

    public static final int MAX_CALORIES = 10_000;
    public static final int MAX_PREP_MINUTES = 24 * 60;

    private final String name;
    private final List<Ingredient> ingredients;
    private final int calories;
    private final int prepTimeMinutes;

    public Recipe(String name, List<Ingredient> ingredients, int calories, int prepTimeMinutes) {
        this.name = Validation.requireText(name, "Recipe name");

        if (ingredients == null || ingredients.isEmpty()) {
            throw new InvalidRecipeException("Recipe '" + this.name + "' must have at least one ingredient");
        }
        if (ingredients.stream().anyMatch(Objects::isNull)) {
            throw new InvalidRecipeException("Ingredients must not contain null");
        }
        if (calories < 0 || calories > MAX_CALORIES) {
            throw new InvalidRecipeException("Calories must be between 0 and " + MAX_CALORIES);
        }
        if (prepTimeMinutes <= 0 || prepTimeMinutes > MAX_PREP_MINUTES) {
            throw new InvalidRecipeException("Preparation time must be between 1 and " + MAX_PREP_MINUTES + " minutes");
        }

        this.ingredients = Collections.unmodifiableList(new ArrayList<>(ingredients));
        this.calories = calories;
        this.prepTimeMinutes = prepTimeMinutes;
    }

    public String getName() {
        return name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public int getCalories() {
        return calories;
    }

    public int getPrepTimeMinutes() {
        return prepTimeMinutes;
    }

    public boolean containsIngredient(String ingredientName) {
        if (ingredientName == null) return false;
        String needle = ingredientName.trim().toLowerCase();
        return ingredients.stream().anyMatch(i -> i.getName().toLowerCase().contains(needle));
    }

    /** Recipes are identified by name (case-insensitive). */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Recipe other)) return false;
        return name.equalsIgnoreCase(other.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name.toLowerCase());
    }

    @Override
    public String toString() {
        return String.format("%s (%d kcal, %d min) - %s", name, calories, prepTimeMinutes, ingredients);
    }
}
