package net.byAqua3.avaritia.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import net.byAqua3.avaritia.inventory.ContainerExtremeCrafting;
import net.byAqua3.avaritia.recipe.RecipeCollector;
import net.byAqua3.avaritia.recipe.RecipeCompressor;
import net.byAqua3.avaritia.recipe.RecipeExtremeCrafting;
import net.byAqua3.avaritia.recipe.RecipeExtremeShaped;
import net.byAqua3.avaritia.recipe.RecipeExtremeShapeless;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;

public class RecipeUtils {

	public static List<RecipeExtremeCrafting> getExtremeCraftingRecipes(List<RecipeHolder<?>> recipes) {
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
				int itemId1 = BuiltInRegistries.ITEM.getId(recipe1.getResultItem(RegistryAccess.EMPTY).getItem());
				int itemId2 = BuiltInRegistries.ITEM.getId(recipe2.getResultItem(RegistryAccess.EMPTY).getItem());
				return Integer.valueOf(itemId1).compareTo(Integer.valueOf(itemId2));
			}
		});
		return recipeList;
	}

	public static List<RecipeExtremeCrafting> getExtremeCraftingRecipes(RecipeManager recipeManager) {
		return getExtremeCraftingRecipes(new ArrayList<>(recipeManager.getRecipes()));
	}

	public static List<RecipeExtremeCrafting> getExtremeCraftingRecipes(Level level) {
		return getExtremeCraftingRecipes(level.getRecipeManager());
	}

	public static List<RecipeCompressor> getCompressorRecipes(List<RecipeHolder<?>> recipes) {
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
				int itemId1 = BuiltInRegistries.ITEM.getId(recipe1.getResultItem(RegistryAccess.EMPTY).getItem());
				int itemId2 = BuiltInRegistries.ITEM.getId(recipe2.getResultItem(RegistryAccess.EMPTY).getItem());
				return Integer.valueOf(itemId1).compareTo(Integer.valueOf(itemId2));
			}
		});
		return recipeList;
	}

	public static List<RecipeCompressor> getCompressorRecipes(RecipeManager recipeManager) {
		return getCompressorRecipes(new ArrayList<>(recipeManager.getRecipes()));
	}

	public static List<RecipeCompressor> getCompressorRecipes(Level level) {
		return getCompressorRecipes(level.getRecipeManager());
	}

	public static RecipeCompressor getCompressorRecipe(List<RecipeHolder<?>> recipes, ItemStack input) {
		SimpleContainer matrix = new SimpleContainer(1);
		matrix.addItem(input);
		ContainerExtremeCrafting crafting = new ContainerExtremeCrafting(null, 1, 1, matrix);
		for (RecipeHolder<?> recipeHolder : recipes) {
			Recipe<?> recipe = recipeHolder.value();
			if (recipe instanceof RecipeCompressor) {
				RecipeCompressor recipeCompressor = (RecipeCompressor) recipe;
				if (recipeCompressor.matches(crafting.asCraftInput(), null)) {
					return recipeCompressor;
				}
			}
		}
		return null;
	}

	public static RecipeCompressor getCompressorRecipe(RecipeManager recipeManager, ItemStack input) {
		return getCompressorRecipe(new ArrayList<>(recipeManager.getRecipes()), input);
	}

	public static RecipeCompressor getCompressorRecipe(Level level, ItemStack input) {
		return getCompressorRecipe(level.getRecipeManager(), input);
	}

	public static RecipeCompressor getCompressorRecipeFromResult(List<RecipeHolder<?>> recipes, ItemStack result) {
		for (RecipeHolder<?> recipeHolder : recipes) {
			Recipe<?> recipe = recipeHolder.value();
			if (recipe instanceof RecipeCompressor) {
				RecipeCompressor recipeCompressor = (RecipeCompressor) recipe;
				if (ItemStack.isSameItemSameComponents(recipeCompressor.getResultItem(RegistryAccess.EMPTY), result)) {
					return recipeCompressor;
				}
			}
		}
		return null;
	}

	public static RecipeCompressor getCompressorRecipeFromResult(RecipeManager recipeManager, ItemStack result) {
		return getCompressorRecipeFromResult(new ArrayList<>(recipeManager.getRecipes()), result);
	}

	public static RecipeCompressor getCompressorRecipeFromResult(Level level, ItemStack result) {
		return getCompressorRecipeFromResult(level.getRecipeManager(), result);
	}

	public static RecipeCompressor getCompressorRecipeFromResult(List<RecipeHolder<?>> recipes, ItemStack result, DataComponentType<?> dataComponentType) {
		for (RecipeHolder<?> recipeHolder : recipes) {
			Recipe<?> recipe = recipeHolder.value();
			if (recipe instanceof RecipeCompressor) {
				RecipeCompressor recipeCompressor = (RecipeCompressor) recipe;
				ItemStack itemStack = recipeCompressor.getResultItem(RegistryAccess.EMPTY);
				if (ItemStack.isSameItem(itemStack, result) && itemStack.has(dataComponentType) && result.has(dataComponentType) && itemStack.get(dataComponentType).equals(result.get(dataComponentType))) {
					return recipeCompressor;
				}
			}
		}
		return null;
	}

	public static RecipeCompressor getCompressorRecipeFromResult(RecipeManager recipeManager, ItemStack result, DataComponentType<?> dataComponentType) {
		return getCompressorRecipeFromResult(new ArrayList<>(recipeManager.getRecipes()), result, dataComponentType);
	}

	public static RecipeCompressor getCompressorRecipeFromResult(Level level, ItemStack result, DataComponentType<?> dataComponentType) {
		return getCompressorRecipeFromResult(level.getRecipeManager(), result, dataComponentType);
	}

	public static List<RecipeCollector> getCollectorRecipes() {
		List<RecipeCollector> recipeList = new ArrayList<>();
		recipeList.add(new RecipeCollector());
		return recipeList;
	}

	public static ResourceLocation getRecipeId(List<RecipeHolder<?>> recipes, Recipe<?> recipe) {
		for (RecipeHolder<?> recipeHolder : recipes) {
			if (recipe.equals(recipeHolder.value())) {
				return recipeHolder.id();
			}
		}
		return null;
	}

	public static ResourceLocation getRecipeId(RecipeManager recipeManager, Recipe<?> recipe) {
		return getRecipeId(new ArrayList<>(recipeManager.getRecipes()), recipe);
	}

	public static ResourceLocation getRecipeId(Level level, Recipe<?> recipe) {
		return getRecipeId(level.getRecipeManager(), recipe);
	}
}
