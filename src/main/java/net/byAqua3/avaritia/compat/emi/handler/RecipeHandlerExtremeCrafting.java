package net.byAqua3.avaritia.compat.emi.handler;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Lists;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.handler.StandardRecipeHandler;
import net.byAqua3.avaritia.compat.emi.AvaritiaEMIPlugin;
import net.byAqua3.avaritia.inventory.MenuExtremeCrafting;
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
	public @Nullable Slot getOutputSlot(MenuExtremeCrafting handler) {
		return handler.getSlot(0);
	}
}
