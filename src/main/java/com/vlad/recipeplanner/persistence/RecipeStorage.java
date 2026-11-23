package com.vlad.recipeplanner.persistence;

import com.vlad.recipeplanner.model.MealPlan;
import com.vlad.recipeplanner.service.RecipeBook;

/**
 * Saves and loads application data between runs (issue #5).
 * An interface, so the file format can be swapped (e.g. JSON) without touching the UI.
 */
public interface RecipeStorage {

    void save(RecipeBook book, MealPlan plan);

    /** Loads data into the given (empty) book and plan. Does nothing if no data exists yet. */
    void load(RecipeBook book, MealPlan plan);
}
