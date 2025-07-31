package net.byAqua3.avaritia.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import net.byAqua3.avaritia.inventory.ContainerExtremeCrafting;
import net.byAqua3.avaritia.recipe.RecipeCollector;
import net.byAqua3.avaritia.recipe.RecipeCompressor;
import net.byAqua3.avaritia.recipe.RecipeExtremeCrafting;
import net.byAqua3.avaritia.recipe.RecipeExtremeShaped;
import net.byAqua3.avaritia.recipe.RecipeExtremeShapeless;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;

public class RecipeUtils {

	public static List<RecipeExtremeCrafting> getExtremeCraftingRecipes(Level level) {
		Collection<RecipeHolder<?>> recipes = level.getRecipeManager().getRecipes();
		List<RecipeExtremeCrafting> recipeList = new ArrayList<>();
		for (RecipeHolder<?> recipeHolder : recipes) {
			Recipe<?> recipe = recipeHolder.value();
			if (recipe instanceof RecipeExtremeShaped || recipe instanceof RecipeExtremeShapeless) {
				RecipeExtremeCrafting recipeExtremeCrafting = (RecipeExtremeCrafting) recipe;
				recipeList.add(recipeExtremeCrafting);
			}
		}
		recipeList.sort(new Comparator<RecipeExtremeCrafting>() {
			@Override
			public int compare(RecipeExtremeCrafting recipe1, RecipeExtremeCrafting recipe2) {
				int itemId1 = BuiltInRegistries.ITEM.getId(recipe1.getResultItem(level.registryAccess()).getItem());
				int itemId2 = BuiltInRegistries.ITEM.getId(recipe2.getResultItem(level.registryAccess()).getItem());
				return Integer.valueOf(itemId1).compareTo(Integer.valueOf(itemId2));
			}
		});
		return recipeList;
	}

	public static List<RecipeCompressor> getCompressorRecipes(Level level) {
		Collection<RecipeHolder<?>> recipes = level.getRecipeManager().getRecipes();
		List<RecipeCompressor> recipeList = new ArrayList<>();
		for (RecipeHolder<?> recipeHolder : recipes) {
			Recipe<?> recipe = recipeHolder.value();
			if (recipe instanceof RecipeCompressor) {
				RecipeCompressor recipeCompressor = (RecipeCompressor) recipe;
				if (!recipeCompressor.getIngredients().isEmpty()) {
					recipeList.add(recipeCompressor);
				}
			}
		}
		recipeList.sort(new Comparator<RecipeCompressor>() {
			@Override
			public int compare(RecipeCompressor recipe1, RecipeCompressor recipe2) {
				int itemId1 = BuiltInRegistries.ITEM.getId(recipe1.getResultItem(level.registryAccess()).getItem());
				int itemId2 = BuiltInRegistries.ITEM.getId(recipe2.getResultItem(level.registryAccess()).getItem());
				return Integer.valueOf(itemId1).compareTo(Integer.valueOf(itemId2));
			}
		});
		return recipeList;
	}

	public static List<RecipeCollector> getCollectorRecipes(Level level) {
		List<RecipeCollector> recipeList = new ArrayList<>();
		recipeList.add(new RecipeCollector());
		return recipeList;
	}

	public static RecipeCompressor getCompressorRecipe(Level level, ItemStack input) {
		Collection<RecipeHolder<?>> recipes = level.getRecipeManager().getRecipes();
		SimpleContainer matrix = new SimpleContainer(1);
		matrix.addItem(input);
		ContainerExtremeCrafting crafting = new ContainerExtremeCrafting(null, 1, 1, matrix);
		for (RecipeHolder<?> recipeHolder : recipes) {
			Recipe<?> recipe = recipeHolder.value();
			if (recipe instanceof RecipeCompressor) {
				RecipeCompressor recipeCompressor = (RecipeCompressor) recipe;
				if (recipeCompressor.matches(crafting.asCraftInput(), level)) {
					return recipeCompressor;
				}
			}
		}
		return null;
	}

	public static RecipeCompressor getCompressorRecipeFromResult(Level level, ItemStack result) {
		Collection<RecipeHolder<?>> recipes = level.getRecipeManager().getRecipes();
		for (RecipeHolder<?> recipeHolder : recipes) {
			Recipe<?> recipe = recipeHolder.value();
			if (recipe instanceof RecipeCompressor) {
				RecipeCompressor recipeCompressor = (RecipeCompressor) recipe;
				if (ItemStack.isSameItemSameComponents(recipeCompressor.getResultItem(level.registryAccess()), result)) {
					return recipeCompressor;
				}
			}
		}
		return null;
	}

	public static RecipeCompressor getCompressorRecipeFromResult(Level level, ItemStack result, DataComponentType<?> dataComponentType) {
		Collection<RecipeHolder<?>> recipes = level.getRecipeManager().getRecipes();
		for (RecipeHolder<?> recipeHolder : recipes) {
			Recipe<?> recipe = recipeHolder.value();
			if (recipe instanceof RecipeCompressor) {
				RecipeCompressor recipeCompressor = (RecipeCompressor) recipe;
				ItemStack itemStack = recipeCompressor.getResultItem(level.registryAccess());
				if (ItemStack.isSameItem(itemStack, result) && itemStack.has(dataComponentType) && result.has(dataComponentType) && itemStack.get(dataComponentType) == result.get(dataComponentType)) {
					return recipeCompressor;
				}
			}
		}
		return null;
	}

}
