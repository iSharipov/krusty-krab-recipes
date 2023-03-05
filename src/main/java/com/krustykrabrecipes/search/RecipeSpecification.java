package com.krustykrabrecipes.search;

import com.krustykrabrecipes.api.model.SearchCriteria;
import com.krustykrabrecipes.model.Ingredient;
import com.krustykrabrecipes.model.Ingredient_;
import com.krustykrabrecipes.model.Recipe;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Objects;

public class RecipeSpecification implements Specification<Recipe> {

    private final SearchCriteria searchCriteria;

    private static final String INGREDIENT_KEY = "ingredient";

    public RecipeSpecification(final SearchCriteria searchCriteria) {
        super();
        this.searchCriteria = searchCriteria;
    }

    @Override
    public Predicate toPredicate(Root<Recipe> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

        String strToSearch = searchCriteria.getValue().toString().toLowerCase();

        switch (Objects.requireNonNull(SearchOperation.getSimpleOperation(searchCriteria.getOperation()))) {
            case CONTAINS:
                if (searchCriteria.getFilterKey().equals(INGREDIENT_KEY)) {
                    return cb.like(cb.lower(recipeJoin(root).get(Ingredient_.NAME)), "%" + strToSearch +
                            "%");
                }
                return cb.like(root.get(searchCriteria.getFilterKey()), "%" + strToSearch + "%");

            case DOES_NOT_CONTAIN:
                if (searchCriteria.getFilterKey().equals(INGREDIENT_KEY)) {
                    return cb.like(cb.lower(recipeJoin(root).get(Ingredient_.NAME)), "%" + strToSearch +
                            "%");
                }
                return cb.notLike(cb.lower(root.get(searchCriteria.getFilterKey())), "%" + strToSearch + "%");

            case BEGINS_WITH:
                if (searchCriteria.getFilterKey().equals(INGREDIENT_KEY)) {
                    return cb.like(cb.lower(recipeJoin(root).get(Ingredient_.NAME)), "%" + strToSearch +
                            "%");
                }
                return cb.like(cb.lower(root.get(searchCriteria.getFilterKey())), strToSearch + "%");

            case DOES_NOT_BEGIN_WITH:
                if (searchCriteria.getFilterKey().equals(INGREDIENT_KEY)) {
                    return cb.like(cb.lower(recipeJoin(root).get(Ingredient_.NAME)), "%" + strToSearch +
                            "%");
                }
                return cb.notLike(cb.lower(root.get(searchCriteria.getFilterKey())), strToSearch + "%");

            case ENDS_WITH:
                if (searchCriteria.getFilterKey().equals(INGREDIENT_KEY)) {
                    return cb.like(cb.lower(recipeJoin(root).get(Ingredient_.NAME)), "%" + strToSearch +
                            "%");
                }
                return cb.like(cb.lower(root.get(searchCriteria.getFilterKey())), "%" + strToSearch);

            case DOES_NOT_END_WITH:
                if (searchCriteria.getFilterKey().equals(INGREDIENT_KEY)) {
                    return cb.like(cb.lower(recipeJoin(root).get(Ingredient_.NAME)), "%" + strToSearch +
                            "%");
                }
                return cb.notLike(cb.lower(root.get(searchCriteria.getFilterKey())), "%" + strToSearch);

            case EQUAL:
                return cb.equal(root.get(searchCriteria.getFilterKey()), searchCriteria.getValue());

            case NOT_EQUAL:
                if (searchCriteria.getFilterKey().equals(INGREDIENT_KEY)) {
                    return cb.like(cb.lower(recipeJoin(root).get(Ingredient_.NAME)), "%" + strToSearch +
                            "%");
                }
                return cb.notEqual(root.get(searchCriteria.getFilterKey()), searchCriteria.getValue());

            case NUL:
                return cb.isNull(root.get(searchCriteria.getFilterKey()));

            case NOT_NULL:
                return cb.isNotNull(root.get(searchCriteria.getFilterKey()));

            case GREATER_THAN:
                return cb.greaterThan(root.get(searchCriteria.getFilterKey()),
                        searchCriteria.getValue().toString());

            case GREATER_THAN_EQUAL:
                return cb.greaterThanOrEqualTo(root.get(searchCriteria.getFilterKey()),
                        searchCriteria.getValue().toString());

            case LESS_THAN:
                return cb.lessThan(root.get(searchCriteria.getFilterKey()),
                        searchCriteria.getValue().toString());

            case LESS_THAN_EQUAL:
                return cb.lessThanOrEqualTo(root.get(searchCriteria.getFilterKey()),
                        searchCriteria.getValue().toString());
        }
        return null;
    }

    private Join<Recipe, List<Ingredient>> recipeJoin(Root<Recipe> root) {
        return root.join("ingredients");

    }
}
