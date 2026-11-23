package com.vlad.recipeplanner.model;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * A weekly plan: for each day of the week, a list of recipes (issue #4).
 */
public class MealPlan {

    private final Map<DayOfWeek, List<Recipe>> days = new EnumMap<>(DayOfWeek.class);

    public MealPlan() {
        for (DayOfWeek day : DayOfWeek.values()) {
            days.put(day, new ArrayList<>());
        }
    }

    public void addMeal(DayOfWeek day, Recipe recipe) {
        if (day == null || recipe == null) {
            throw new IllegalArgumentException("Day and recipe must not be null");
        }
        days.get(day).add(recipe);
    }

    public boolean removeMeal(DayOfWeek day, Recipe recipe) {
        return days.get(day).remove(recipe);
    }

    /** Removes a recipe from every day (used when a recipe is deleted). */
    public void removeRecipeEverywhere(Recipe recipe) {
        days.values().forEach(list -> list.removeIf(r -> r.equals(recipe)));
    }

    public List<Recipe> getMeals(DayOfWeek day) {
        return Collections.unmodifiableList(days.get(day));
    }

    public List<Recipe> getAllMeals() {
        List<Recipe> all = new ArrayList<>();
        days.values().forEach(all::addAll);
        return all;
    }

    public int getTotalCalories(DayOfWeek day) {
        return days.get(day).stream().mapToInt(Recipe::getCalories).sum();
    }

    public boolean isEmpty() {
        return days.values().stream().allMatch(List::isEmpty);
    }

    public void clear() {
        days.values().forEach(List::clear);
    }
}
