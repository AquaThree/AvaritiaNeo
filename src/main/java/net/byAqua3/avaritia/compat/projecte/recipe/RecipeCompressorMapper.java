package net.byAqua3.avaritia.compat.projecte.recipe;

import java.util.ArrayList;
import java.util.List;

import moze_intel.projecte.api.mapper.collector.IMappingCollector;
import moze_intel.projecte.api.mapper.recipe.INSSFakeGroupManager;
import moze_intel.projecte.api.mapper.recipe.RecipeTypeMapper;
import moze_intel.projecte.api.nss.NSSItem;
import moze_intel.projecte.api.nss.NormalizedSimpleStack;
import moze_intel.projecte.emc.mappers.recipe.BaseRecipeTypeMapper;
import moze_intel.projecte.utils.EMCHelper;
import net.byAqua3.avaritia.loader.AvaritiaRecipes;
import net.byAqua3.avaritia.recipe.RecipeCompressor;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;

@RecipeTypeMapper
public class RecipeCompressorMapper extends BaseRecipeTypeMapper {

	@Override
	public String getName() {
		return "Compressor Mapper";
	}

	@Override
	public String getDescription() {
		return "Neutronium Compressor Mapper.";
	}

	@Override
	public String getTranslationKey() {
		return "mapping.mapper.neutronium_compressor";
	}

	@Override
	public boolean canHandle(RecipeType<?> recipeType) {
		return recipeType == AvaritiaRecipes.COMPRESSOR.get();
	}

	@Override
	public boolean handleRecipe(IMappingCollector<NormalizedSimpleStack, Long> mapper, RecipeHolder<?> recipeHolder,
			RegistryAccess registryAccess, INSSFakeGroupManager fakeGroupManager) {
		Recipe<?> recipe = recipeHolder.value();
		ItemStack resultItem = recipe.getResultItem(registryAccess);
		if (resultItem.isEmpty()) {
			return false;
		}
		if(this.canHandle(recipe.getType())) {
			List<ItemStack> validRecipeItems = new ArrayList<>();
			
			for (Ingredient ingredient : recipe.getIngredients()) {
				ItemStack[] itemStacks = ingredient.getItems();
				for (ItemStack itemStack : itemStacks) {
					validRecipeItems.add(itemStack);
				}
			}
			if (!validRecipeItems.isEmpty()) {
				mapper.addConversion(resultItem.getCount(), NSSItem.createItem(resultItem), EMCHelper.intMapOf(NSSItem.createItem(validRecipeItems.get(0)), ((RecipeCompressor) recipe).getCost()));
				return true;
			}
		}
		return false;
	}

}
