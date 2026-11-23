# Recipe Planner

Java OOP project: a console application for managing recipes and planning meals for the week.

## Features

- **Add and validate recipes** (#1) – name, ingredients, calories and preparation time are validated; invalid data throws `InvalidRecipeException`.
- **Search with interchangeable strategies** (#2) – Strategy pattern: search by name, ingredient, max calories or max preparation time. Strategies can be combined with `and(...)`.
- **Sort recipes** (#3) – by name, calories or preparation time, ascending or descending.
- **Weekly meal plan and shopping list** (#4) – assign recipes to days of the week; the shopping list sums identical ingredients.
- **Save and load data** (#5) – data is stored in `data/recipes.txt` and loaded automatically on start.

## Project structure

```
com.vlad.recipeplanner
├── model         Recipe, Ingredient, MealPlan
├── exception     InvalidRecipeException, StorageException
├── service       RecipeBook, ShoppingListGenerator
├── search        SearchStrategy + NameSearch, IngredientSearch, MaxCaloriesSearch, MaxPrepTimeSearch
├── sort          RecipeSortOrder
├── persistence   RecipeStorage, FileRecipeStorage
└── ui            Main, ConsoleUI
```

## OOP concepts used

- **Encapsulation** – private fields, validation in constructors, unmodifiable collections.
- **Polymorphism / interfaces** – `SearchStrategy`, `RecipeStorage`.
- **Strategy pattern** – interchangeable search strategies.
- **Custom exceptions** – `InvalidRecipeException`, `StorageException`.
- **Separation of concerns** – UI, business logic and storage are in separate packages.

## How to run

Requirements: Java 17+, Maven.

```bash
mvn compile exec:java   # run the application
mvn test                # run unit tests
```

Or run `com.vlad.recipeplanner.ui.Main` from IntelliJ IDEA.

## License

MIT
