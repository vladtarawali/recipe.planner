package com.vlad.recipeplanner.search;

import com.vlad.recipeplanner.TestData;
import com.vlad.recipeplanner.model.Recipe;
import com.vlad.recipeplanner.service.RecipeBook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SearchStrategyTest {

    private RecipeBook book;

    @BeforeEach
    void setUp() {
        book = new RecipeBook();
        book.add(TestData.pancakes());
        book.add(TestData.omelette());
        book.add(TestData.lasagna());
    }

    private List<String> names(List<Recipe> recipes) {
        return recipes.stream().map(Recipe::getName).toList();
    }

    @Test
    void searchByName() {
        assertEquals(List.of("Pancakes"), names(book.search(new NameSearch("cake"))));
    }

    @Test
    void searchByIngredient() {
        assertEquals(List.of("Pancakes", "Omelette"), names(book.search(new IngredientSearch("EGG"))));
    }

    @Test
    void searchByMaxCalories() {
        assertEquals(List.of("Pancakes", "Omelette"), names(book.search(new MaxCaloriesSearch(400))));
    }

    @Test
    void searchByMaxPrepTime() {
        assertEquals(List.of("Omelette"), names(book.search(new MaxPrepTimeSearch(15))));
    }

    @Test
    void strategiesCanBeCombined() {
        SearchStrategy s = new IngredientSearch("flour").and(new MaxCaloriesSearch(500));
        assertEquals(List.of("Pancakes"), names(book.search(s)));
    }

    @Test
    void noMatchesReturnsEmptyList() {
        assertTrue(book.search(new NameSearch("pizza")).isEmpty());
    }
}
