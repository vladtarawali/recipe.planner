package com.vlad.recipeplanner.model;

import com.vlad.recipeplanner.exception.InvalidRecipeException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RecipeValidationTest {

    private final List<Ingredient> ingredients = List.of(new Ingredient("egg", 2, "pcs"));

    @Test
    void validRecipeIsCreated() {
        Recipe r = new Recipe("  Omelette ", ingredients, 250, 10);
        assertEquals("Omelette", r.getName());
        assertEquals(250, r.getCalories());
        assertEquals(10, r.getPrepTimeMinutes());
    }

    @Test
    void emptyNameIsRejected() {
        assertThrows(InvalidRecipeException.class, () -> new Recipe("  ", ingredients, 250, 10));
        assertThrows(InvalidRecipeException.class, () -> new Recipe(null, ingredients, 250, 10));
    }

    @Test
    void reservedCharactersInNameAreRejected() {
        assertThrows(InvalidRecipeException.class, () -> new Recipe("Soup|Salad", ingredients, 250, 10));
    }

    @Test
    void recipeWithoutIngredientsIsRejected() {
        assertThrows(InvalidRecipeException.class, () -> new Recipe("Air", List.of(), 0, 1));
        assertThrows(InvalidRecipeException.class, () -> new Recipe("Air", null, 0, 1));
    }

    @Test
    void negativeCaloriesAreRejected() {
        assertThrows(InvalidRecipeException.class, () -> new Recipe("X", ingredients, -1, 10));
    }

    @Test
    void nonPositivePrepTimeIsRejected() {
        assertThrows(InvalidRecipeException.class, () -> new Recipe("X", ingredients, 100, 0));
    }

    @Test
    void invalidIngredientIsRejected() {
        assertThrows(InvalidRecipeException.class, () -> new Ingredient("", 1, "g"));
        assertThrows(InvalidRecipeException.class, () -> new Ingredient("salt", 0, "g"));
        assertThrows(InvalidRecipeException.class, () -> new Ingredient("salt", -5, "g"));
        assertThrows(InvalidRecipeException.class, () -> new Ingredient("salt", 5, " "));
    }

    @Test
    void ingredientListCannotBeModifiedFromOutside() {
        List<Ingredient> list = new ArrayList<>(ingredients);
        Recipe r = new Recipe("Omelette", list, 250, 10);
        list.add(new Ingredient("cheese", 50, "g"));
        assertEquals(1, r.getIngredients().size());
        assertThrows(UnsupportedOperationException.class,
                () -> r.getIngredients().add(new Ingredient("ham", 1, "pcs")));
    }
}
