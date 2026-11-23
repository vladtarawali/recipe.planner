# User Stories

Primary user: a person who cooks at home and wants to organise recipes and plan the week's meals
(students, families, people tracking calories).

| ID | User story | Acceptance criteria | Issue |
|----|-----------|---------------------|-------|
| US1 | As a home cook, I want to **add a recipe** with ingredients, calories and preparation time, so that I keep all my recipes in one place. | A recipe needs a non-empty unique name, at least one ingredient, calories 0–10000 and preparation time 1–1440 min. Invalid data shows an error and nothing is saved. | #1 |
| US2 | As a home cook, I want to **search recipes** by name, ingredient, maximum calories or maximum preparation time, so that I quickly find something suitable. | Each search type returns only matching recipes. An empty result shows "No recipes found". | #2 |
| US3 | As a home cook, I want to **sort recipes** by name, calories or preparation time (ascending or descending), so that I can compare them. | The list is shown in the selected order. The stored order is not changed. | #3 |
| US4 | As a person planning my week, I want to **assign recipes to days of the week**, so that I know what to cook each day. | A recipe can be added to any day, several recipes per day are allowed, total calories per day are shown. | #4 |
| US5 | As a person going shopping, I want a **shopping list** generated from my weekly plan, so that I buy everything at once. | Identical ingredients with the same unit are summed (200 g + 300 g flour = 500 g flour). The list is sorted by name. | #4 |
| US6 | As a returning user, I want my **recipes and plan to be saved** between runs, so that I don't have to enter them again. | Data is saved on "Save" and on exit, and loaded automatically on start. A corrupted file shows an error instead of crashing. | #5 |
| US7 | As a user, I want to **remove a recipe** I no longer need, so that my list stays clean. | The recipe is removed from the book and from every day of the plan. | #1 |
