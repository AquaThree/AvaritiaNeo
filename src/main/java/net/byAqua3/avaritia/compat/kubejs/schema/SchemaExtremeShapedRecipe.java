package net.byAqua3.avaritia.compat.kubejs.schema;

import java.util.List;

import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.IngredientComponent;
import dev.latvian.mods.kubejs.recipe.component.ItemStackComponent;
import dev.latvian.mods.kubejs.recipe.component.MapRecipeComponent;
import dev.latvian.mods.kubejs.recipe.component.StringComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import dev.latvian.mods.kubejs.util.IntBounds;
import dev.latvian.mods.kubejs.util.TinyMap;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class SchemaExtremeShapedRecipe extends RecipeSchema {

	public final static RecipeKey<List<String>> PATTERN = StringComponent.OPTIONAL_STRING.instance().asList().inputKey("pattern");
	public final static RecipeKey<TinyMap<Character, Ingredient>> KEY = MapRecipeComponent.patternOf(IngredientComponent.INGREDIENT.instance(), IntBounds.DEFAULT).inputKey("key");
	public final static RecipeKey<ItemStack> RESULT = ItemStackComponent.ITEM_STACK.outputKey("result");

	public SchemaExtremeShapedRecipe() {
		super(new RecipeKey[] { PATTERN, KEY, RESULT });
		this.constructor(new RecipeKey[] { PATTERN, KEY, RESULT });
	}
}
