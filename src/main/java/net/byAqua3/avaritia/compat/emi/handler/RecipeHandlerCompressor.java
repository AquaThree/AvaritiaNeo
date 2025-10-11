package net.byAqua3.avaritia.compat.emi.handler;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Lists;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.handler.StandardRecipeHandler;
import net.byAqua3.avaritia.compat.emi.AvaritiaEMIPlugin;
import net.byAqua3.avaritia.inventory.MenuNeutroniumCompressor;
import net.minecraft.world.inventory.Slot;

public class RecipeHandlerCompressor implements StandardRecipeHandler<MenuNeutroniumCompressor> {
	
	@Override
	public boolean supportsRecipe(EmiRecipe recipe) {
		return recipe.getCategory() == AvaritiaEMIPlugin.COMPRESSOR && recipe.supportsRecipeTree();
	}

	@Override
	public List<Slot> getInputSources(MenuNeutroniumCompressor handler) {
		List<Slot> list = Lists.newArrayList();
		list.add(handler.getSlot(0));
		int invStart = 2;
		for (int i = 1; i < invStart + 36; i++) { 
			list.add(handler.getSlot(i));
		}
		return list;
	}

	@Override
	public List<Slot> getCraftingSlots(MenuNeutroniumCompressor handler) {
		List<Slot> list = Lists.newArrayList();
		list.add(handler.getSlot(0));
		return list;
	}
	
	@Override
	public @Nullable Slot getOutputSlot(MenuNeutroniumCompressor handler) {
		return handler.getSlot(1);
	}
}
