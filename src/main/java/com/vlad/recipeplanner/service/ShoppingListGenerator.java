package com.vlad.recipeplanner.service;

import com.vlad.recipeplanner.model.Ingredient;
import com.vlad.recipeplanner.model.MealPlan;
import com.vlad.recipeplanner.model.Recipe;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Builds a shopping list from a meal plan (issue #4).
 * Same ingredient with the same unit is summed: 200 g flour + 300 g flour = 500 g flour.
 * Different units are kept separate (we don't convert g to kg).
 */
public class ShoppingListGenerator {

    public List<Ingredient> generate(MealPlan plan) {
        Map<String, Ingredient> totals = new LinkedHashMap<>();

        for (Recipe recipe : plan.getAllMeals()) {
            for (Ingredient ing : recipe.getIngredients()) {
                String key = ing.getName().toLowerCase() + "|" + ing.getUnit().toLowerCase();
                totals.merge(key, ing,
                        (existing, added) -> existing.withQuantity(existing.getQuantity() + added.getQuantity()));
            }
        }

        List<Ingredient> result = new ArrayList<>(totals.values());
        result.sort(Comparator.comparing(Ingredient::getName, String.CASE_INSENSITIVE_ORDER));
        return result;
    }
}
