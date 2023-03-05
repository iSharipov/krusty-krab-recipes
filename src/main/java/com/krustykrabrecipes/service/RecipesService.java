package com.krustykrabrecipes.service;

import com.krustykrabrecipes.api.model.CreateRecipeRequest;
import com.krustykrabrecipes.api.model.IngredientUpdateRequest;
import com.krustykrabrecipes.api.model.RecipeResponse;
import com.krustykrabrecipes.api.model.UpdateRecipeRequest;
import com.krustykrabrecipes.exception.RecipeNotFoundException;
import com.krustykrabrecipes.model.Ingredient;
import com.krustykrabrecipes.model.Recipe;
import com.krustykrabrecipes.model.mapper.IngredientMapper;
import com.krustykrabrecipes.model.mapper.RecipeMapper;
import com.krustykrabrecipes.repository.IngredientRepository;
import com.krustykrabrecipes.repository.RecipeRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class RecipesService {

    private final RecipeRepository recipeRepository;
    private final RecipeMapper recipeMapper;
    private final IngredientRepository ingredientRepository;
    private final IngredientMapper ingredientMapper;

    public List<RecipeResponse> getAllRecipes() {
        return recipeMapper.toRecipes(recipeRepository.findAllByRemovedFalse());
    }

    public RecipeResponse getRecipe(String recipeId) {
        return recipeRepository.findByIdAndRemovedFalse(Long.parseLong(recipeId))
                .map(recipeMapper::toRecipe)
                .orElseThrow(
                        RecipeNotFoundException::new);
    }

    public RecipeResponse createRecipe(CreateRecipeRequest createRecipeRequest) {
        Recipe recipe = recipeRepository.save(recipeMapper.toRecipe(createRecipeRequest));
        List<Ingredient> ingredients = ingredientMapper.toIngredients(createRecipeRequest.getIngredients());
        ingredients.forEach(ingredient -> ingredient.setRecipeId(recipe.getId()));
        List<Ingredient> persistedIngredients = ingredientRepository.saveAll(ingredients);
        recipe.setIngredients(persistedIngredients);
        return recipeMapper.toRecipe(recipe);
    }

    public RecipeResponse updateRecipe(String recipeId, UpdateRecipeRequest updateRecipeRequest) {
        Recipe recipe = recipeRepository.findById(Long.parseLong(recipeId))
                .orElseThrow(
                        RecipeNotFoundException::new);
        recipeMapper.updateRecipe(recipe, updateRecipeRequest);
        recipeRepository.save(recipe);
        recipe.getIngredients().forEach(ingredientModel ->
                {
                    Optional<IngredientUpdateRequest> first = updateRecipeRequest.getIngredients()
                            .stream()
                            .filter(ingredient -> ingredientModel.getId().equals(ingredient.getId()))
                            .findFirst();
                    first.ifPresent(ingredientUpdateRequest -> ingredientMapper.update(ingredientModel,
                            ingredientUpdateRequest));
                    ingredientRepository.save(ingredientModel);
                }
        );
        return recipeMapper.toRecipe(recipeRepository.findById(Long.parseLong(recipeId))
                .orElseThrow(RecipeNotFoundException::new));
    }

    public void deleteRecipe(String recipeId) {
        Recipe recipe = recipeRepository.findById(Long.parseLong(recipeId))
                .orElseThrow(
                        RecipeNotFoundException::new);
        recipe.setRemoved(true);
        recipeRepository.save(recipe);
    }

    public List<RecipeResponse> findBySearchCriteria(Specification<Recipe> spec) {
        return recipeMapper.toRecipes(recipeRepository.findAll(spec));
    }
}
