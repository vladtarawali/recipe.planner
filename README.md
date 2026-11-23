# Recipe Planner

A Java console application for storing recipes, planning meals for the week and generating a shopping list.
Course project for **Programming 2 – Java OOP Projectwork**.

## Problem statement

**Users:** people who cook at home – students, families, people who watch their calories.

**Goal:** keep all recipes in one place, quickly find a suitable recipe, plan what to cook during the week
and know exactly what to buy.

**Problem:** recipes are usually scattered across notes, websites and memory. Planning a week of meals and
adding up ingredients for shopping by hand is slow and error-prone. Recipe Planner stores recipes with
validated data, lets the user search and sort them, assign them to days of the week, and builds a combined
shopping list automatically. All data is saved between runs.

## Features

| Feature | Issue |
|---------|-------|
| Add, validate and remove recipes | #1 |
| Search recipes with interchangeable strategies (name, ingredient, max calories, max preparation time) | #2 |
| Sort recipes by name, calories or preparation time (ascending / descending) | #3 |
| Weekly meal plan with daily calorie totals, and an automatically generated shopping list | #4 |
| Save and load data between runs (text file) | #5 |

## Prerequisites

- Java **17** or newer
- Maven **3.8+** (or use IntelliJ IDEA, which includes Maven)

## Build, run and test

From the project root:

```bash
mvn clean compile        # build
mvn compile exec:java    # run the application
mvn test                 # run all JUnit tests
mvn clean package        # build the JAR into target/
```

In IntelliJ IDEA: open the project folder, wait for Maven to load, then run
`src/main/java/com/vlad/recipeplanner/ui/Main.java`.
To run the tests: right-click `src/test/java` → **Run 'All Tests'**.

Data is stored in `data/recipes.txt` (created automatically, not committed to Git).

## Sample usage

```
===== Recipe Planner =====
1. Add recipe
2. Show all recipes
3. Search recipes
4. Sort recipes
5. Remove recipe
6. Add recipe to weekly plan
7. Show weekly plan
8. Show shopping list
9. Save
0. Save and exit
Choose: 1
Recipe name: Pancakes
Calories (kcal): 350
Preparation time (minutes): 20
Add ingredients (leave name empty to finish):
  Ingredient name: flour
  Quantity: 200
  Unit (g, ml, pcs...): g
  Ingredient name: egg
  Quantity: 2
  Unit (g, ml, pcs...): pcs
  Ingredient name:
Recipe added: Pancakes

Choose: 3
Search by: 1) name  2) ingredient  3) max calories  4) max preparation time
Choose: 2
Ingredient: egg
 - Pancakes (350 kcal, 20 min) - [flour 200 g, egg 2 pcs]

Choose: 6
Day (1=Monday ... 7=Sunday): 1
Recipe name: Pancakes
Added Pancakes to MONDAY

Choose: 8
Shopping list:
 - egg 2 pcs
 - flour 200 g

Choose: 1
Recipe name: Soup
Calories (kcal): -5
...
Error: Calories must be between 0 and 10000
```

## Project structure

```
src/main/java/com/vlad/recipeplanner
├── model         domain:      Recipe, Ingredient, MealPlan
├── exception     domain rules: InvalidRecipeException, StorageException
├── service       logic:       RecipeBook, ShoppingListGenerator
├── search        Strategy:    SearchStrategy + NameSearch, IngredientSearch, MaxCaloriesSearch, MaxPrepTimeSearch
├── sort          sorting:     RecipeSortOrder
├── persistence   storage:     RecipeStorage, FileRecipeStorage
└── ui            interface:   Main, ConsoleUI
src/test/java/...              JUnit 5 tests
docs/                          UML class diagram, user stories
```

## Implemented requirements

| Requirement | Where |
|-------------|-------|
| **Encapsulation** | All fields are `private`; `Recipe` and `Ingredient` validate data in their constructors, so an invalid object cannot exist; `getIngredients()` returns an unmodifiable list. |
| **Abstraction** | Interfaces `SearchStrategy` and `RecipeStorage`. |
| **Inheritance & runtime polymorphism** | Four classes implement `SearchStrategy` and override `matches(Recipe)`. `RecipeBook.search(SearchStrategy)` calls whichever implementation the user picks at runtime. `FileRecipeStorage` implements `RecipeStorage`. The custom exceptions extend `RuntimeException`. |
| **Relationships** | Composition `Recipe 1 *-- 1..* Ingredient`; aggregation `RecipeBook 1 o-- 0..* Recipe` and `MealPlan 1 o-- 0..* Recipe` (see UML). |
| **Collections & generics** | `List<Recipe>`, `List<Ingredient>`, `EnumMap<DayOfWeek, List<Recipe>>`, `Optional<Recipe>`, `Map.merge` in the shopping list. |
| **Sorting** | `Comparator<Recipe>` built with lambdas / method references in `RecipeSortOrder`, plus `reversed()` for descending order. |
| **Exceptions** | Custom `InvalidRecipeException` (domain rule: invalid recipe data, duplicate names) and `StorageException` (file errors / corrupted data). Invalid console input is caught and the user can try again. |
| **Persistence** | `FileRecipeStorage` reads and writes a text file using `BufferedReader` / `BufferedWriter` in **try-with-resources**. |
| **Testing** | 27 JUnit 5 tests: validation, recipe book, search, sorting, shopping list, save/load. |
| **Build** | Maven project with standard directory layout. |

## Design pattern: Strategy

**Problem:** the user can search recipes in several different ways, and more ways may be added later.
Putting every search type into one method with `if/else` or `switch` would make `RecipeBook` grow
with every new search and would mix the search rules with the storage of recipes.

**Solution:** each search rule is a separate class implementing the `SearchStrategy` interface.

| Role in the pattern | Class |
|---------------------|-------|
| Strategy (interface) | `SearchStrategy` – `boolean matches(Recipe recipe)` |
| Concrete strategies | `NameSearch`, `IngredientSearch`, `MaxCaloriesSearch`, `MaxPrepTimeSearch` |
| Context (client) | `RecipeBook.search(SearchStrategy strategy)` |
| Chooses the strategy at runtime | `ConsoleUI.searchRecipes()` |

**Benefit:** `RecipeBook` does not know or care how a recipe is matched – it just calls `matches()`.
A new search type (for example "vegetarian only") is added by writing one new class, without changing
`RecipeBook` (open/closed principle). Strategies can also be combined, e.g.
`new IngredientSearch("egg").and(new MaxCaloriesSearch(400))`. Each strategy is small and is tested on its own.

The same idea is used for sorting: `RecipeSortOrder` supplies interchangeable `Comparator<Recipe>` objects
to `RecipeBook.sorted(...)`.

The UML class diagram is in [docs/](docs/README.md).

## Main design decisions

- **Validation in constructors** – `Recipe` and `Ingredient` are immutable and checked when created, so the rest of the program can trust them.
- **Unchecked custom exceptions** – domain errors are reported with clear messages and caught once in the UI loop.
- **Separate layers** – the UI only reads input and prints output; logic is in `service`, file handling in `persistence`.
- **Storage behind an interface** – `RecipeStorage` makes it possible to switch to JSON or a database without changing the UI.
- **Plain text file format** – human-readable and needs no extra libraries. The characters `| ; :` are reserved and rejected in names.
- **Recipe names are unique** (case-insensitive), so the meal plan can refer to recipes by name in the saved file.

## Known limitations

- Units are not converted: `500 g` and `1 kg` of the same ingredient are listed separately in the shopping list.
- The characters `|`, `;` and `:` cannot be used in recipe or ingredient names.
- Recipes cannot be edited – remove and add them again.
- Only one user and one weekly plan; no history of previous weeks.
- Console interface only (no GUI).

## Contributors

- **vladtarawali** – design, implementation, tests, documentation

## License

This project is licensed under the MIT License – see [LICENSE](LICENSE).
