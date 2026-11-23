package com.vlad.recipeplanner;

import com.vlad.recipeplanner.model.Ingredient;
import com.vlad.recipeplanner.model.Recipe;

import java.util.List;

public final class TestData {

    private TestData() {
    }

    public static Recipe pancakes() {
        return new Recipe("Pancakes",
                List.of(new Ingredient("flour", 200, "g"), new Ingredient("milk", 300, "ml"),
                        new Ingredient("egg", 2, "pcs")),
                350, 20);
    }

    public static Recipe omelette() {
        return new Recipe("Omelette",
                List.of(new Ingredient("egg", 3, "pcs"), new Ingredient("milk", 50, "ml")),
                250, 10);
    }

    public static Recipe lasagna() {
        return new Recipe("Lasagna",
                List.of(new Ingredient("pasta", 250, "g"), new Ingredient("beef", 400, "g"),
                        new Ingredient("flour", 30, "g")),
                800, 90);
    }
}
