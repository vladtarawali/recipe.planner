package com.vlad.recipeplanner.model;

import com.vlad.recipeplanner.exception.InvalidRecipeException;

import java.util.Objects;

/**
 * One ingredient of a recipe, e.g. "flour, 200 g". Immutable.
 */
public class Ingredient {

    /** Characters reserved by the file format (see FileRecipeStorage). */
    static final String FORBIDDEN_CHARS = "|;:";

    private final String name;
    private final double quantity;
    private final String unit;

    public Ingredient(String name, double quantity, String unit) {
        this.name = Validation.requireText(name, "Ingredient name");
        this.unit = Validation.requireText(unit, "Unit");
        if (quantity <= 0 || Double.isNaN(quantity) || Double.isInfinite(quantity)) {
            throw new InvalidRecipeException("Quantity of '" + name + "' must be a positive number");
        }
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public double getQuantity() {
        return quantity;
    }

    public String getUnit() {
        return unit;
    }

    /** Returns a new ingredient with the same name and unit and the given quantity. */
    public Ingredient withQuantity(double newQuantity) {
        return new Ingredient(name, newQuantity, unit);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ingredient other)) return false;
        return Double.compare(quantity, other.quantity) == 0
                && name.equalsIgnoreCase(other.name)
                && unit.equalsIgnoreCase(other.unit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name.toLowerCase(), quantity, unit.toLowerCase());
    }

    @Override
    public String toString() {
        return name + " " + formatQuantity(quantity) + " " + unit;
    }

    static String formatQuantity(double q) {
        return q == Math.floor(q) ? String.valueOf((long) q) : String.valueOf(q);
    }
}
