package com.vlad.recipeplanner.service;

import com.vlad.recipeplanner.exception.InvalidRecipeException;
import com.vlad.recipeplanner.model.Recipe;
import com.vlad.recipeplanner.search.SearchStrategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Stores all recipes and provides adding (#1), searching (#2) and sorting (#3).
 */
public class RecipeBook {

    private final List<Recipe> recipes = new ArrayList<>();

    /** Adds a recipe. Names must be unique (case-insensitive). */
    public void add(Recipe recipe) {
        if (recipe == null) {
            throw new InvalidRecipeException("Recipe must not be null");
        }
        if (recipes.contains(recipe)) {
            throw new InvalidRecipeException("A recipe named '" + recipe.getName() + "' already exists");
        }
        recipes.add(recipe);
    }

    public boolean remove(String name) {
        return recipes.removeIf(r -> r.getName().equalsIgnoreCase(name.trim()));
    }

    public Optional<Recipe> findByName(String name) {
        if (name == null) return Optional.empty();
        return recipes.stream()
                .filter(r -> r.getName().equalsIgnoreCase(name.trim()))
                .findFirst();
    }

    public List<Recipe> getAll() {
        return Collections.unmodifiableList(recipes);
    }

    public List<Recipe> search(SearchStrategy strategy) {
        return recipes.stream().filter(strategy::matches).toList();
    }

    public List<Recipe> sorted(Comparator<Recipe> comparator) {
        List<Recipe> copy = new ArrayList<>(recipes);
        copy.sort(comparator);
        return copy;
    }

    public int size() {
        return recipes.size();
    }

    public boolean isEmpty() {
        return recipes.isEmpty();
    }

    public void clear() {
        recipes.clear();
    }
}
