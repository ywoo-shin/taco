package com.jarry.taco.data;

import com.jarry.taco.Ingredient;
import org.springframework.data.repository.CrudRepository;

public interface IngredientRepository extends CrudRepository<Ingredient, String> {
	
}
