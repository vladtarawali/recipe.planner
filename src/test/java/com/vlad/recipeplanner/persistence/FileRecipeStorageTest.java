package com.vlad.recipeplanner.persistence;

import com.vlad.recipeplanner.TestData;
import com.vlad.recipeplanner.exception.StorageException;
import com.vlad.recipeplanner.model.MealPlan;
import com.vlad.recipeplanner.model.Recipe;
import com.vlad.recipeplanner.service.RecipeBook;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.DayOfWeek;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileRecipeStorageTest {

    @TempDir
    Path tempDir;

    @Test
    void savedDataIsLoadedBack() {
        Path file = tempDir.resolve("data.txt");
        RecipeBook book = new RecipeBook();
        book.add(TestData.pancakes());
        book.add(TestData.omelette());
        MealPlan plan = new MealPlan();
        plan.addMeal(DayOfWeek.MONDAY, book.findByName("Pancakes").orElseThrow());
        plan.addMeal(DayOfWeek.SUNDAY, book.findByName("Omelette").orElseThrow());

        new FileRecipeStorage(file).save(book, plan);

        RecipeBook loadedBook = new RecipeBook();
        MealPlan loadedPlan = new MealPlan();
        new FileRecipeStorage(file).load(loadedBook, loadedPlan);

        assertEquals(2, loadedBook.size());
        Recipe pancakes = loadedBook.findByName("Pancakes").orElseThrow();
        assertEquals(350, pancakes.getCalories());
        assertEquals(20, pancakes.getPrepTimeMinutes());
        assertEquals(TestData.pancakes().getIngredients(), pancakes.getIngredients());
        assertEquals(List.of(pancakes), loadedPlan.getMeals(DayOfWeek.MONDAY));
        assertEquals("Omelette", loadedPlan.getMeals(DayOfWeek.SUNDAY).get(0).getName());
    }

    @Test
    void missingFileMeansEmptyData() {
        RecipeBook book = new RecipeBook();
        MealPlan plan = new MealPlan();
        new FileRecipeStorage(tempDir.resolve("nothing.txt")).load(book, plan);
        assertTrue(book.isEmpty());
        assertTrue(plan.isEmpty());
    }

    @Test
    void corruptedFileThrowsStorageException() throws Exception {
        Path file = tempDir.resolve("bad.txt");
        Files.writeString(file, "RECIPE|Broken|abc|10|egg:1:pcs\n");
        assertThrows(StorageException.class,
                () -> new FileRecipeStorage(file).load(new RecipeBook(), new MealPlan()));
    }
}
