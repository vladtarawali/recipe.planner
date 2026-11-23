package com.vlad.recipeplanner.sort;

import com.vlad.recipeplanner.TestData;
import com.vlad.recipeplanner.model.Recipe;
import com.vlad.recipeplanner.service.RecipeBook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RecipeSortOrderTest {

    private RecipeBook book;

    @BeforeEach
    void setUp() {
        book = new RecipeBook();
        book.add(TestData.pancakes()); // 350 kcal, 20 min
        book.add(TestData.omelette()); // 250 kcal, 10 min
        book.add(TestData.lasagna());  // 800 kcal, 90 min
    }

    private List<String> names(List<Recipe> recipes) {
        return recipes.stream().map(Recipe::getName).toList();
    }

    @Test
    void sortByName() {
        assertEquals(List.of("Lasagna", "Omelette", "Pancakes"),
                names(book.sorted(RecipeSortOrder.BY_NAME.ascending())));
    }

    @Test
    void sortByCalories() {
        assertEquals(List.of("Omelette", "Pancakes", "Lasagna"),
                names(book.sorted(RecipeSortOrder.BY_CALORIES.ascending())));
    }

    @Test
    void sortByPrepTimeDescending() {
        assertEquals(List.of("Lasagna", "Pancakes", "Omelette"),
                names(book.sorted(RecipeSortOrder.BY_PREP_TIME.descending())));
    }

    @Test
    void sortingDoesNotChangeOriginalOrder() {
        book.sorted(RecipeSortOrder.BY_NAME.ascending());
        assertEquals(List.of("Pancakes", "Omelette", "Lasagna"), names(book.getAll()));
    }
}
