package com.vlad.recipeplanner.search;

import com.vlad.recipeplanner.model.Recipe;

/** Matches recipes whose name contains the given text (case-insensitive). */
public class NameSearch implements SearchStrategy {

    private final String text;

    public NameSearch(String text) {
        this.text = text == null ? "" : text.trim().toLowerCase();
    }

    @Override
    public boolean matches(Recipe recipe) {
        return recipe.getName().toLowerCase().contains(text);
    }
}
