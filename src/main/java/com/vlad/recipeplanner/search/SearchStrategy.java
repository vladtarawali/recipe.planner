package com.vlad.recipeplanner.search;

import com.vlad.recipeplanner.model.Recipe;

/**
 * Strategy pattern (issue #2): each implementation decides whether a recipe matches.
 * RecipeBook.search(...) works with any strategy, so new search types can be added
 * without changing existing code.
 */
public interface SearchStrategy {

    boolean matches(Recipe recipe);

    /** Combines two strategies: a recipe must match both. */
    default SearchStrategy and(SearchStrategy other) {
        return recipe -> this.matches(recipe) && other.matches(recipe);
    }
}
