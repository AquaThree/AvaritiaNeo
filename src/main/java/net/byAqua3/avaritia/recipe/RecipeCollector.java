package net.byAqua3.avaritia.recipe;

import net.byAqua3.avaritia.loader.AvaritiaItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class RecipeCollector implements RecipeExtremeCrafting {

	@Override
	public boolean matches(CraftingInput container, Level level) {
		return false;
	}

	@Override
	public ItemStack assemble(CraftingInput container, HolderLookup.Provider registryAccess) {
		return null;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return false;
	}

	@Override
	public ItemStack getResultItem(HolderLookup.Provider registryAccess) {
		return new ItemStack(AvaritiaItems.NEUTRON_PILE.get());
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return null;
	}}
