package net.byAqua3.avaritia.compat.projecte.recipe;

import moze_intel.projecte.api.mapper.recipe.RecipeTypeMapper;
import moze_intel.projecte.emc.mappers.recipe.BaseRecipeTypeMapper;
import net.byAqua3.avaritia.loader.AvaritiaRecipes;
import net.minecraft.world.item.crafting.RecipeType;

@RecipeTypeMapper
public class RecipeExtremeMapper extends BaseRecipeTypeMapper {

	@Override
	public String getName() {
		return "Extreme Mapper";
	}

	@Override
	public String getDescription() {
		return "Extreme Crafting Mapper.";
	}

	@Override
	public String getTranslationKey() {
		return "mapping.mapper.extreme_crafting";
	}

	@Override
	public boolean canHandle(RecipeType<?> recipeType) {
		return recipeType == AvaritiaRecipes.EXTREME_CRAFTING.get();
	}
}
