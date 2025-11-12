package net.byAqua3.avaritia.compat.kubejs.schema;

import java.util.List;

import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.BooleanComponent;
import dev.latvian.mods.kubejs.recipe.component.IngredientComponent;
import dev.latvian.mods.kubejs.recipe.component.ItemStackComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class SchemaExtremeShapelessRecipe extends RecipeSchema {

	public final static RecipeKey<List<Ingredient>> INGREDIENTS = IngredientComponent.OPTIONAL_INGREDIENT.instance().asList().inputKey("ingredients");
	public final static RecipeKey<ItemStack> RESULT = ItemStackComponent.ITEM_STACK.outputKey("result");
	public final static RecipeKey<Boolean> SINGULARITIES = BooleanComponent.BOOLEAN.inputKey("singularities").optional(false);

	public SchemaExtremeShapelessRecipe() {
		super(new RecipeKey[] { INGREDIENTS, RESULT, SINGULARITIES });
		this.constructor(new RecipeKey[] { INGREDIENTS, RESULT });
		this.constructor(new RecipeKey[] { INGREDIENTS, RESULT, SINGULARITIES });
	}}
