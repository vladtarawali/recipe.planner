package com.vlad.recipeplanner.ui;

import com.vlad.recipeplanner.exception.InvalidRecipeException;
import com.vlad.recipeplanner.exception.StorageException;
import com.vlad.recipeplanner.model.Ingredient;
import com.vlad.recipeplanner.model.MealPlan;
import com.vlad.recipeplanner.model.Recipe;
import com.vlad.recipeplanner.persistence.RecipeStorage;
import com.vlad.recipeplanner.search.IngredientSearch;
import com.vlad.recipeplanner.search.MaxCaloriesSearch;
import com.vlad.recipeplanner.search.MaxPrepTimeSearch;
import com.vlad.recipeplanner.search.NameSearch;
import com.vlad.recipeplanner.search.SearchStrategy;
import com.vlad.recipeplanner.service.RecipeBook;
import com.vlad.recipeplanner.service.ShoppingListGenerator;
import com.vlad.recipeplanner.sort.RecipeSortOrder;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * Console menu. Only handles input/output; all logic lives in the service classes.
 */
public class ConsoleUI {

    private final Scanner in;
    private final RecipeBook book;
    private final MealPlan plan;
    private final RecipeStorage storage;
    private final ShoppingListGenerator shoppingListGenerator = new ShoppingListGenerator();

    public ConsoleUI(Scanner in, RecipeBook book, MealPlan plan, RecipeStorage storage) {
        this.in = in;
        this.book = book;
        this.plan = plan;
        this.storage = storage;
    }

    public void run() {
        loadData();
        boolean running = true;
        while (running) {
            printMenu();
            String choice = in.nextLine().trim();
            try {
                switch (choice) {
                    case "1" -> addRecipe();
                    case "2" -> printRecipes(book.getAll());
                    case "3" -> searchRecipes();
                    case "4" -> sortRecipes();
                    case "5" -> removeRecipe();
                    case "6" -> addToMealPlan();
                    case "7" -> printMealPlan();
                    case "8" -> printShoppingList();
                    case "9" -> saveData();
                    case "0" -> {
                        saveData();
                        running = false;
                    }
                    default -> System.out.println("Unknown option, try again.");
                }
            } catch (InvalidRecipeException | StorageException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        System.out.println("Bye!");
    }

    private void printMenu() {
        System.out.println();
        System.out.println("===== Recipe Planner =====");
        System.out.println("1. Add recipe");
        System.out.println("2. Show all recipes");
        System.out.println("3. Search recipes");
        System.out.println("4. Sort recipes");
        System.out.println("5. Remove recipe");
        System.out.println("6. Add recipe to weekly plan");
        System.out.println("7. Show weekly plan");
        System.out.println("8. Show shopping list");
        System.out.println("9. Save");
        System.out.println("0. Save and exit");
        System.out.print("Choose: ");
    }

    // ---------- #1 add ----------

    private void addRecipe() {
        String name = readLine("Recipe name: ");
        int calories = readInt("Calories (kcal): ");
        int prep = readInt("Preparation time (minutes): ");

        List<Ingredient> ingredients = new ArrayList<>();
        System.out.println("Add ingredients (leave name empty to finish):");
        while (true) {
            String ingName = readLine("  Ingredient name: ");
            if (ingName.isEmpty()) break;
            double qty = readDouble("  Quantity: ");
            String unit = readLine("  Unit (g, ml, pcs...): ");
            try {
                ingredients.add(new Ingredient(ingName, qty, unit));
            } catch (InvalidRecipeException e) {
                System.out.println("  Error: " + e.getMessage() + " - ingredient skipped.");
            }
        }

        Recipe recipe = new Recipe(name, ingredients, calories, prep); // validates everything
        book.add(recipe);
        System.out.println("Recipe added: " + recipe.getName());
    }

    private void removeRecipe() {
        String name = readLine("Name of recipe to remove: ");
        Optional<Recipe> recipe = book.findByName(name);
        if (recipe.isEmpty()) {
            System.out.println("No recipe with that name.");
            return;
        }
        plan.removeRecipeEverywhere(recipe.get());
        book.remove(name);
        System.out.println("Removed.");
    }

    // ---------- #2 search ----------

    private void searchRecipes() {
        System.out.println("Search by: 1) name  2) ingredient  3) max calories  4) max preparation time");
        String choice = readLine("Choose: ");
        SearchStrategy strategy = switch (choice) {
            case "1" -> new NameSearch(readLine("Name contains: "));
            case "2" -> new IngredientSearch(readLine("Ingredient: "));
            case "3" -> new MaxCaloriesSearch(readInt("Max calories: "));
            case "4" -> new MaxPrepTimeSearch(readInt("Max minutes: "));
            default -> null;
        };
        if (strategy == null) {
            System.out.println("Unknown option.");
            return;
        }
        printRecipes(book.search(strategy));
    }

    // ---------- #3 sort ----------

    private void sortRecipes() {
        System.out.println("Sort by: 1) name  2) calories  3) preparation time");
        RecipeSortOrder order = switch (readLine("Choose: ")) {
            case "1" -> RecipeSortOrder.BY_NAME;
            case "2" -> RecipeSortOrder.BY_CALORIES;
            case "3" -> RecipeSortOrder.BY_PREP_TIME;
            default -> null;
        };
        if (order == null) {
            System.out.println("Unknown option.");
            return;
        }
        boolean desc = readLine("Descending? (y/n): ").equalsIgnoreCase("y");
        printRecipes(book.sorted(desc ? order.descending() : order.ascending()));
    }

    // ---------- #4 meal plan & shopping list ----------

    private void addToMealPlan() {
        if (book.isEmpty()) {
            System.out.println("Add some recipes first.");
            return;
        }
        DayOfWeek day = readDay();
        if (day == null) return;
        String name = readLine("Recipe name: ");
        Optional<Recipe> recipe = book.findByName(name);
        if (recipe.isEmpty()) {
            System.out.println("No recipe with that name.");
            return;
        }
        plan.addMeal(day, recipe.get());
        System.out.println("Added " + recipe.get().getName() + " to " + day);
    }

    private void printMealPlan() {
        if (plan.isEmpty()) {
            System.out.println("The weekly plan is empty.");
            return;
        }
        for (DayOfWeek day : DayOfWeek.values()) {
            List<Recipe> meals = plan.getMeals(day);
            System.out.printf("%-9s (%d kcal): ", day, plan.getTotalCalories(day));
            if (meals.isEmpty()) {
                System.out.println("-");
            } else {
                System.out.println(String.join(", ", meals.stream().map(Recipe::getName).toList()));
            }
        }
    }

    private void printShoppingList() {
        List<Ingredient> list = shoppingListGenerator.generate(plan);
        if (list.isEmpty()) {
            System.out.println("Shopping list is empty - add recipes to the weekly plan first.");
            return;
        }
        System.out.println("Shopping list:");
        list.forEach(i -> System.out.println(" - " + i));
    }

    // ---------- #5 save/load ----------

    private void loadData() {
        try {
            storage.load(book, plan);
            if (!book.isEmpty()) {
                System.out.println("Loaded " + book.size() + " recipe(s).");
            }
        } catch (StorageException e) {
            System.out.println("Could not load saved data: " + e.getMessage());
            book.clear();
            plan.clear();
        }
    }

    private void saveData() {
        storage.save(book, plan);
        System.out.println("Data saved.");
    }

    // ---------- input helpers ----------

    private void printRecipes(List<Recipe> recipes) {
        if (recipes.isEmpty()) {
            System.out.println("No recipes found.");
            return;
        }
        recipes.forEach(r -> System.out.println(" - " + r));
    }

    private String readLine(String prompt) {
        System.out.print(prompt);
        return in.nextLine().trim();
    }

    private int readInt(String prompt) {
        while (true) {
            try {
                return Integer.parseInt(readLine(prompt));
            } catch (NumberFormatException e) {
                System.out.println("Please enter a whole number.");
            }
        }
    }

    private double readDouble(String prompt) {
        while (true) {
            try {
                return Double.parseDouble(readLine(prompt).replace(',', '.'));
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
            }
        }
    }

    private DayOfWeek readDay() {
        String input = readLine("Day (1=Monday ... 7=Sunday): ");
        try {
            return DayOfWeek.of(Integer.parseInt(input));
        } catch (RuntimeException e) {
            System.out.println("Invalid day.");
            return null;
        }
    }
}
