package com.vlad.recipeplanner.service;

import com.vlad.recipeplanner.TestData;
import com.vlad.recipeplanner.model.Ingredient;
import com.vlad.recipeplanner.model.MealPlan;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ShoppingListGeneratorTest {

    @Test
    void sameIngredientsAreSummed() {
        MealPlan plan = new MealPlan();
        plan.addMeal(DayOfWeek.MONDAY, TestData.pancakes()); // flour 200 g, milk 300 ml, egg 2
        plan.addMeal(DayOfWeek.TUESDAY, TestData.omelette()); // egg 3, milk 50 ml
        plan.addMeal(DayOfWeek.FRIDAY, TestData.pancakes());

        List<Ingredient> list = new ShoppingListGenerator().generate(plan);

        assertEquals(List.of(
                new Ingredient("egg", 7, "pcs"),
                new Ingredient("flour", 400, "g"),
                new Ingredient("milk", 650, "ml")
        ), list);
    }

    @Test
    void emptyPlanGivesEmptyList() {
        assertTrue(new ShoppingListGenerator().generate(new MealPlan()).isEmpty());
    }

    @Test
    void dailyCaloriesAreCalculated() {
        MealPlan plan = new MealPlan();
        plan.addMeal(DayOfWeek.MONDAY, TestData.pancakes());
        plan.addMeal(DayOfWeek.MONDAY, TestData.omelette());
        assertEquals(600, plan.getTotalCalories(DayOfWeek.MONDAY));
        assertEquals(0, plan.getTotalCalories(DayOfWeek.SUNDAY));
    }
}
