package net.byAqua3.avaritia.compat.rei.display;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.byAqua3.avaritia.compat.rei.AvaritiaREIPlugin;
import net.byAqua3.avaritia.recipe.RecipeCompressor;
import net.minecraft.core.RegistryAccess;

import java.util.Arrays;
import java.util.List;

public class DisplayCompressorRecipe implements Display {

	private RecipeCompressor recipe;

	public DisplayCompressorRecipe(RecipeCompressor recipe) {
		this.recipe = recipe;
	}

	public RecipeCompressor getRecipe() {
		return this.recipe;
	}

	public int getCost() {
		return this.recipe.getCost();
	}

	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return AvaritiaREIPlugin.COMPRESSOR;
	}

	@Override
	public List<EntryIngredient> getInputEntries() {
		return EntryIngredients.ofIngredients(this.recipe.getIngredients());
	}

	@Override
	public List<EntryIngredient> getOutputEntries() {
		return Arrays.asList(EntryIngredients.of(this.recipe.getResultItem(RegistryAccess.EMPTY)));
	}
}