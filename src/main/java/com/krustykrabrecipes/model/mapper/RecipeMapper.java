package com.krustykrabrecipes.model.mapper;

import com.krustykrabrecipes.api.model.CreateRecipeRequest;
import com.krustykrabrecipes.api.model.RecipeResponse;
import com.krustykrabrecipes.model.Recipe;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper
public interface RecipeMapper {
    RecipeResponse toRecipe(Recipe recipe);

    List<RecipeResponse> toRecipes(List<Recipe> recipes);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "removed", ignore = true),
            @Mapping(target = "ingredients", ignore = true)
    })
    Recipe toRecipe(CreateRecipeRequest createRecipeRequest);
}
