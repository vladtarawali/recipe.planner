package com.vlad.recipeplanner.search;

import com.vlad.recipeplanner.model.Recipe;

/** Matches recipes that contain an ingredient with the given name. */
public class IngredientSearch implements SearchStrategy {

    private final String ingredientName;

    public IngredientSearch(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    @Override
    public boolean matches(Recipe recipe) {
        return recipe.containsIngredient(ingredientName);
    }
}
