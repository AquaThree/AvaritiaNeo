package net.byAqua3.avaritia.compat.kubejs.schema;

import java.util.List;

import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.IngredientComponent;
import dev.latvian.mods.kubejs.recipe.component.ItemStackComponent;
import dev.latvian.mods.kubejs.recipe.component.NumberComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class SchemaCompressorRecipe extends RecipeSchema {

	public final static RecipeKey<List<Ingredient>> INGREDIENTS = IngredientComponent.UNWRAPPED_INGREDIENT_LIST.inputKey("ingredients");
	public final static RecipeKey<ItemStack> RESULT = ItemStackComponent.ITEM_STACK.outputKey("result");
	public final static RecipeKey<Integer> COST = NumberComponent.INT.inputKey("cost");

	public SchemaCompressorRecipe() {
		super(new RecipeKey[] { INGREDIENTS, RESULT, COST });
		this.constructor(new RecipeKey[] { INGREDIENTS, RESULT, COST });
	}}
