package com.krustykrabrecipes.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "instructions", nullable = false)
    private String instructions;

    @Column(name = "servings")
    private Integer servings;

    @OneToMany(
            mappedBy = "recipe",
            cascade = CascadeType.ALL
    )
    private List<Ingredient> ingredients = new ArrayList<>();

    @Column(name = "vegetarian")
    private Boolean vegetarian;

    @Column(name = "removed", nullable = false)
    private boolean removed;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recipe recipe = (Recipe) o;
        return removed == recipe.removed && Objects.equals(id, recipe.id) &&
                Objects.equals(name, recipe.name) && Objects.equals(description, recipe.description) &&
                Objects.equals(instructions, recipe.instructions) &&
                Objects.equals(servings, recipe.servings) &&
                Objects.equals(ingredients, recipe.ingredients) &&
                Objects.equals(vegetarian, recipe.vegetarian);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, instructions, servings, ingredients, vegetarian, removed);
    }
}
