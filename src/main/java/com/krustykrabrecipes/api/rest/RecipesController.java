package com.krustykrabrecipes.api.rest;

import com.krustykrabrecipes.api.model.CreateRecipeRequest;
import com.krustykrabrecipes.api.model.RecipeResponse;
import com.krustykrabrecipes.api.model.SearchCriteria;
import com.krustykrabrecipes.search.RecipeSpecificationBuilder;
import com.krustykrabrecipes.service.RecipesService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
public class RecipesController implements RecipesApi {

    private final RecipesService recipesService;


    @Override
    public ResponseEntity<List<RecipeResponse>> recipesGet() {
        return ResponseEntity.ok(recipesService.getAllRecipes());
    }

    @Override
    public ResponseEntity<RecipeResponse> recipesRecipeIdGet(String recipeId) {
        return ResponseEntity.ok(recipesService.getRecipe(recipeId));
    }

    @Override
    public ResponseEntity<RecipeResponse> recipesPost(CreateRecipeRequest createRecipeRequest) {
        return ResponseEntity.ok(recipesService.createRecipe(createRecipeRequest));
    }

    @Override
    public ResponseEntity<Void> recipesRecipeIdDelete(String recipeId) {
        recipesService.deleteRecipe(recipeId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<List<RecipeResponse>> recipesSearchPost(List<SearchCriteria> searchCriteria) {
        RecipeSpecificationBuilder builder = new RecipeSpecificationBuilder();
        if (searchCriteria != null) {
            searchCriteria.forEach(x -> {
                x.setDataOption(x.getDataOption());
                builder.with(x);
            });

        }
        return ResponseEntity.ok(recipesService.findBySearchCriteria(builder.build()));
    }
}
