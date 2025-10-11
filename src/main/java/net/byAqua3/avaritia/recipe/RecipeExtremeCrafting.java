package net.byAqua3.avaritia.recipe;

import net.byAqua3.avaritia.loader.AvaritiaRecipes;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

public interface RecipeExtremeCrafting extends Recipe<CraftingInput> {

	@Override
	default RecipeType<?> getType() {
		return AvaritiaRecipes.EXTREME_CRAFTING.get();
	}}
