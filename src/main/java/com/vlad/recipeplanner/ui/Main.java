package com.vlad.recipeplanner.ui;

import com.vlad.recipeplanner.model.MealPlan;
import com.vlad.recipeplanner.persistence.FileRecipeStorage;
import com.vlad.recipeplanner.service.RecipeBook;

import java.nio.file.Path;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Path dataFile = Path.of("data", "recipes.txt");
        new ConsoleUI(
                new Scanner(System.in),
                new RecipeBook(),
                new MealPlan(),
                new FileRecipeStorage(dataFile)
        ).run();
    }
}
