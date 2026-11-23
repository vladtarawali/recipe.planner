package com.vlad.recipeplanner.persistence;

import com.vlad.recipeplanner.exception.InvalidRecipeException;
import com.vlad.recipeplanner.exception.StorageException;
import com.vlad.recipeplanner.model.Ingredient;
import com.vlad.recipeplanner.model.MealPlan;
import com.vlad.recipeplanner.model.Recipe;
import com.vlad.recipeplanner.service.RecipeBook;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

/**
 * Stores data in a plain text file, one item per line:
 * <pre>
 * RECIPE|Pancakes|350|20|flour:200:g;milk:300:ml;egg:2:pcs
 * PLAN|MONDAY|Pancakes
 * </pre>
 * The characters | ; : are not allowed in names (validated in the model), so parsing is safe.
 */
public class FileRecipeStorage implements RecipeStorage {

    private final Path file;

    public FileRecipeStorage(Path file) {
        this.file = file;
    }

    @Override
    public void save(RecipeBook book, MealPlan plan) {
        List<String> lines = new ArrayList<>();
        for (Recipe r : book.getAll()) {
            lines.add(String.join("|", "RECIPE", r.getName(),
                    String.valueOf(r.getCalories()),
                    String.valueOf(r.getPrepTimeMinutes()),
                    encodeIngredients(r.getIngredients())));
        }
        for (DayOfWeek day : DayOfWeek.values()) {
            for (Recipe r : plan.getMeals(day)) {
                lines.add(String.join("|", "PLAN", day.name(), r.getName()));
            }
        }
        try {
            Path parent = file.toAbsolutePath().getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            Files.write(file, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new StorageException("Could not save data to " + file, e);
        }
    }

    @Override
    public void load(RecipeBook book, MealPlan plan) {
        if (!Files.exists(file)) {
            return; // first run, nothing saved yet
        }
        List<String> lines;
        try {
            lines = Files.readAllLines(file, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new StorageException("Could not read data from " + file, e);
        }

        List<String[]> planLines = new ArrayList<>();
        int lineNo = 0;
        for (String line : lines) {
            lineNo++;
            if (line.isBlank()) continue;
            String[] parts = line.split("\\|", -1);
            try {
                switch (parts[0]) {
                    case "RECIPE" -> book.add(decodeRecipe(parts));
                    case "PLAN" -> planLines.add(parts); // recipes must be loaded first
                    default -> throw new StorageException("Unknown record type '" + parts[0] + "'");
                }
            } catch (InvalidRecipeException | IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
                throw new StorageException("Corrupted data at line " + lineNo + ": " + e.getMessage(), e);
            }
        }

        for (String[] parts : planLines) {
            if (parts.length != 3) {
                throw new StorageException("Corrupted plan entry: " + String.join("|", parts));
            }
            DayOfWeek day = DayOfWeek.valueOf(parts[1]);
            Recipe recipe = book.findByName(parts[2])
                    .orElseThrow(() -> new StorageException("Plan refers to unknown recipe '" + parts[2] + "'"));
            plan.addMeal(day, recipe);
        }
    }

    private static String encodeIngredients(List<Ingredient> ingredients) {
        List<String> encoded = new ArrayList<>();
        for (Ingredient i : ingredients) {
            encoded.add(i.getName() + ":" + i.getQuantity() + ":" + i.getUnit());
        }
        return String.join(";", encoded);
    }

    private static Recipe decodeRecipe(String[] parts) {
        if (parts.length != 5) {
            throw new IllegalArgumentException("Recipe line must have 5 fields");
        }
        List<Ingredient> ingredients = new ArrayList<>();
        for (String item : parts[4].split(";")) {
            String[] f = item.split(":", -1);
            if (f.length != 3) {
                throw new IllegalArgumentException("Bad ingredient '" + item + "'");
            }
            ingredients.add(new Ingredient(f[0], Double.parseDouble(f[1]), f[2]));
        }
        return new Recipe(parts[1], ingredients, Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
    }
}
