package com.vlad.recipeplanner.service;

import com.vlad.recipeplanner.TestData;
import com.vlad.recipeplanner.exception.InvalidRecipeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RecipeBookTest {

    private RecipeBook book;

    @BeforeEach
    void setUp() {
        book = new RecipeBook();
        book.add(TestData.pancakes());
    }

    @Test
    void addStoresRecipe() {
        assertEquals(1, book.size());
        assertTrue(book.findByName("pancakes").isPresent());
    }

    @Test
    void duplicateNameIsRejected() {
        assertThrows(InvalidRecipeException.class, () -> book.add(TestData.pancakes()));
    }

    @Test
    void removeDeletesRecipe() {
        assertTrue(book.remove("PANCAKES"));
        assertTrue(book.isEmpty());
        assertFalse(book.remove("Pancakes"));
    }
}
