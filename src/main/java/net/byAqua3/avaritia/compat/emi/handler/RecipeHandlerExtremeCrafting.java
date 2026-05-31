package net.byAqua3.avaritia.compat.emi.handler;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Lists;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.handler.StandardRecipeHandler;
import dev.emi.emi.api.stack.EmiIngredient;
import net.byAqua3.avaritia.compat.emi.AvaritiaEMIPlugin;
import net.byAqua3.avaritia.compat.emi.recipe.EMIRecipeExtremeCrafting;
import net.byAqua3.avaritia.inventory.MenuExtremeCrafting;
import net.byAqua3.avaritia.recipe.RecipeExtremeShaped;
import net.minecraft.world.inventory.Slot;

public class RecipeHandlerExtremeCrafting implements StandardRecipeHandler<MenuExtremeCrafting> {

	@Override
	public boolean supportsRecipe(EmiRecipe recipe) {
		return recipe.getCategory() == AvaritiaEMIPlugin.EXTREME_CRAFTING && recipe.supportsRecipeTree();
	}

	@Override
	public List<Slot> getInputSources(MenuExtremeCrafting handler) {
		List<Slot> list = Lists.newArrayList();
		for (int i = 1; i < 82; i++) {
			list.add(handler.getSlot(i));
		}
		int invStart = 82;
		for (int i = invStart; i < invStart + 36; i++) {
			list.add(handler.getSlot(i));
		}
		return list;
	}

	@Override
	public List<Slot> getCraftingSlots(MenuExtremeCrafting handler) {
		List<Slot> list = Lists.newArrayList();
		for (int i = 1; i < 82; i++) {
			list.add(handler.getSlot(i));
		}
		return list;
	}

	@Override
	public List<Slot> getCraftingSlots(EmiRecipe recipe, MenuExtremeCrafting handler) {
		if (recipe instanceof EMIRecipeExtremeCrafting) {
			EMIRecipeExtremeCrafting extremeCraftingRecipe = (EMIRecipeExtremeCrafting) recipe;
			List<EmiIngredient> ingredients = extremeCraftingRecipe.getInputs();
			if (extremeCraftingRecipe.getRecipe() instanceof RecipeExtremeShaped) {
				List<Slot> slots = new ArrayList<>();
				RecipeExtremeShaped shapedRecipe = (RecipeExtremeShaped) extremeCraftingRecipe.getRecipe();

				for (int y = 0; y < 9; y++) {
					for (int x = 0; x < 9; x++) {
						int slotIndex = ((9 - shapedRecipe.getWidth()) / 2 + x) + ((9 - shapedRecipe.getHeight()) / 2 + y) * 9;
						int inputIndex = x + y * shapedRecipe.getWidth();
						if (inputIndex >= ingredients.size() || x >= shapedRecipe.getWidth()) {
							continue;
						}
						slots.add(handler.getSlot(slotIndex + 1));
					}
				}
				return slots;
			}
		}
		return this.getCraftingSlots(handler);
	}

	@Override
	public @Nullable Slot getOutputSlot(MenuExtremeCrafting handler) {
		return handler.getSlot(0);
	}
}
