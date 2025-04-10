package net.byAqua3.avaritia.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import net.byAqua3.avaritia.inventory.ContainerExtremeCrafting;
import net.byAqua3.avaritia.recipe.RecipeCompressor;
import net.byAqua3.avaritia.recipe.RecipeExtremeCrafting;
import net.byAqua3.avaritia.recipe.RecipeExtremeShaped;
import net.byAqua3.avaritia.recipe.RecipeExtremeShapeless;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;

public class AvaritiaRecipeUtils {

	public static List<RecipeExtremeCrafting> getExtremeCraftingRecipes(RecipeManager recipeManager) {
		Collection<RecipeHolder<?>> recipes = recipeManager.getRecipes();
		List<RecipeExtremeCrafting> recipeList = new ArrayList<>();
		for (RecipeHolder<?> recipe : recipes) {
			if (recipe.value() instanceof RecipeExtremeShaped || recipe.value() instanceof RecipeExtremeShapeless) {
				recipeList.add((RecipeExtremeCrafting) recipe.value());
			}
		}
		recipeList.sort(new Comparator<RecipeExtremeCrafting>() {
			@Override
			public int compare(RecipeExtremeCrafting recipe1, RecipeExtremeCrafting recipe2) {
				int itemId1 = BuiltInRegistries.ITEM.getId(recipe1.getResultItem(RegistryAccess.EMPTY).getItem());
				int itemId2 = BuiltInRegistries.ITEM.getId(recipe2.getResultItem(RegistryAccess.EMPTY).getItem());
				return Integer.valueOf(itemId1).compareTo(Integer.valueOf(itemId2));
			}
		});
		return recipeList;
	}

	public static List<RecipeExtremeCrafting> getExtremeCraftingRecipes(Level level) {
		if (level.getServer() == null) {
			return new ArrayList<>();
		}
		return getExtremeCraftingRecipes(level.getServer().getRecipeManager());
	}

	public static List<RecipeCompressor> getCompressorRecipes(RecipeManager recipeManager) {
		Collection<RecipeHolder<?>> recipes = recipeManager.getRecipes();
		List<RecipeCompressor> recipeList = new ArrayList<>();
		for (RecipeHolder<?> recipe : recipes) {
			if (recipe.value() instanceof RecipeCompressor) {
				RecipeCompressor recipeCompressor = (RecipeCompressor) recipe.value();
				if (!recipeCompressor.ingredients.isEmpty()) {
					recipeList.add(recipeCompressor);
				}
			}
		}
		recipeList.sort(new Comparator<RecipeCompressor>() {
			@Override
			public int compare(RecipeCompressor recipe1, RecipeCompressor recipe2) {
				int itemId1 = BuiltInRegistries.ITEM.getId(recipe1.getResultItem(RegistryAccess.EMPTY).getItem());
				int itemId2 = BuiltInRegistries.ITEM.getId(recipe2.getResultItem(RegistryAccess.EMPTY).getItem());
				return Integer.valueOf(itemId1).compareTo(Integer.valueOf(itemId2));
			}
		});
		return recipeList;
	}

	public static List<RecipeCompressor> getCompressorRecipes(Level level) {
		if (level.getServer() == null) {
			return new ArrayList<>();
		}
		return getCompressorRecipes(level.getServer().getRecipeManager());
	}

	public static RecipeCompressor getCompressorRecipe(RecipeManager recipeManager, ItemStack input) {
		Collection<RecipeHolder<?>> recipes = recipeManager.getRecipes();
		SimpleContainer matrix = new SimpleContainer(1);
		matrix.addItem(input);
		ContainerExtremeCrafting crafting = new ContainerExtremeCrafting(null, 1, 1, matrix);
		for (RecipeHolder<?> recipe : recipes) {
			if (recipe.value() instanceof RecipeCompressor) {
				RecipeCompressor recipeCompressor = (RecipeCompressor) recipe.value();
				if (recipeCompressor.matches(crafting.asCraftInput(), null)) {
					return recipeCompressor;
				}
			}
		}
		return null;
	}

	public static RecipeCompressor getCompressorRecipe(Level level, ItemStack input) {
		if (level.getServer() == null) {
			return null;
		}
		return getCompressorRecipe(level.getServer().getRecipeManager(), input);
	}
	
	public static RecipeCompressor getCompressorRecipeFromResult(RecipeManager recipeManager, ItemStack result) {
			Collection<RecipeHolder<?>> recipes = recipeManager.getRecipes();
			for (RecipeHolder<?> recipe : recipes) {
				if (recipe.value() instanceof RecipeCompressor) {
					RecipeCompressor recipeCompressor = (RecipeCompressor) recipe.value();
					if (ItemStack.isSameItemSameComponents(recipeCompressor.getResultItem(RegistryAccess.EMPTY), result)) {
						return recipeCompressor;
					}
				}
			}
		return null;
	}

	public static RecipeCompressor getCompressorRecipeFromResult(Level level, ItemStack result) {
		if (level.getServer() == null) {
			return null;
		}
		return getCompressorRecipeFromResult(level.getServer().getRecipeManager(), result);
	}

}
