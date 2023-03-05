package com.krustykrabrecipes.api.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.krustykrabrecipes.Application;
import com.krustykrabrecipes.api.model.RecipeResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = Application.class)
@AutoConfigureMockMvc
class RecipesControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void recipesGet() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .get("/recipes")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(4)));
    }

    @Test
    void recipesRecipeIdGet() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .get("/recipes/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("cake"));
    }

    @Test
    void recipesPost() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .post("/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(recipe()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void recipesRecipeIdDelete() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .delete("/recipes/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(recipe()))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    void recipesSearchPost() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .post("/recipes/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(search()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(1)));
    }

    @Test
    void recipesRecipeIdPut() throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                        .post("/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(recipe()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        var postResult = mvcResult.getResponse().getContentAsString();
        var recipeResponse = objectMapper.readValue(postResult, RecipeResponse.class);

        mvc.perform(MockMvcRequestBuilders
                        .put("/recipes/" + recipeResponse.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(updateRecipe()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.ingredients[0].name").value("tomato"));
    }

    private String recipe() {
        return """
                {
                  "name": "soup",
                  "instructions": "cook",
                  "vegetarian": false,
                  "servings": 2,
                  "ingredients": [
                    {
                      "name": "potatoes",
                      "description": "fresh",
                      "unit": "kg",
                      "amount": 1
                    }
                  ]
                }""";
    }

    private String updateRecipe() {
        return """
                {
                  "name": "soup",
                  "instructions": "cook",
                  "vegetarian": false,
                  "servings": 2,
                  "ingredients": [
                    {
                      "id": 1,
                      "name": "tomato",
                      "description": "dried",
                      "unit": "kg",
                      "amount": 2
                    }
                  ]
                }""";
    }

    private String search() {
        return """
                [
                  {
                    "filterKey": "instructions",
                    "value": "fry",
                    "operation": "cn",
                    "dataOption": "string"
                  }
                ]""";
    }
}