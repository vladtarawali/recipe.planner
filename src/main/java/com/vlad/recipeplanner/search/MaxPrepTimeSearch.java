package com.vlad.recipeplanner.search;

import com.vlad.recipeplanner.model.Recipe;

/** Matches recipes that can be prepared in at most the given number of minutes. */
public class MaxPrepTimeSearch implements SearchStrategy {

    private final int maxMinutes;

    public MaxPrepTimeSearch(int maxMinutes) {
        this.maxMinutes = maxMinutes;
    }

    @Override
    public boolean matches(Recipe recipe) {
        return recipe.getPrepTimeMinutes() <= maxMinutes;
    }
}
