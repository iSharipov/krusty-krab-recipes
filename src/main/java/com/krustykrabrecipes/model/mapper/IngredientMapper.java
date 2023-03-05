package com.krustykrabrecipes.model.mapper;

import com.krustykrabrecipes.api.model.IngredientUpdateRequest;
import com.krustykrabrecipes.model.Ingredient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper
public interface IngredientMapper {
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "recipe", ignore = true),
            @Mapping(target = "recipeId", ignore = true)
    })
    Ingredient toIngredient(com.krustykrabrecipes.api.model.Ingredient ingredient);

    List<Ingredient> toIngredients(List<com.krustykrabrecipes.api.model.Ingredient> ingredients);

    @Mappings({
            @Mapping(target = "recipe", ignore = true),
            @Mapping(target = "recipeId", ignore = true)
    })
    void update(@MappingTarget Ingredient ingredient, IngredientUpdateRequest ingredientUpdateRequest);
}
