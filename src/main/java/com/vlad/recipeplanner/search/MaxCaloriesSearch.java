package com.vlad.recipeplanner.search;

import com.vlad.recipeplanner.model.Recipe;

/** Matches recipes with at most the given number of calories. */
public class MaxCaloriesSearch implements SearchStrategy {

    private final int maxCalories;

    public MaxCaloriesSearch(int maxCalories) {
        this.maxCalories = maxCalories;
    }

    @Override
    public boolean matches(Recipe recipe) {
        return recipe.getCalories() <= maxCalories;
    }
}
