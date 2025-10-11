package net.byAqua3.avaritia.compat.rei.display;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.byAqua3.avaritia.compat.rei.AvaritiaREIPlugin;
import net.byAqua3.avaritia.recipe.RecipeExtremeCrafting;
import net.byAqua3.avaritia.recipe.RecipeExtremeShapeless;
import net.minecraft.core.RegistryAccess;
import java.util.Arrays;
import java.util.List;

public class DisplayExtremeRecipe implements Display {

	private RecipeExtremeCrafting recipe;

	public DisplayExtremeRecipe(RecipeExtremeCrafting recipe) {
		this.recipe = recipe;
	}

	public RecipeExtremeCrafting getRecipe() {
		return this.recipe;
	}

	public boolean isShapeless() {
		return this.recipe instanceof RecipeExtremeShapeless;
	}

	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return AvaritiaREIPlugin.EXTREME_CRAFTING;
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