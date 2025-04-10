package net.byAqua3.avaritia.recipe;

import net.byAqua3.avaritia.loader.AvaritiaRecipes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

public interface RecipeExtremeCrafting extends Recipe<CraftingInput> {

	@Override
	default RecipeType<? extends Recipe<CraftingInput>> getType() {
		return AvaritiaRecipes.EXTREME_CRAFTING.get();
	}
	
	public ItemStack getResultItem(HolderLookup.Provider registryAccess);
	
	default NonNullList<ItemStack> getRemainingItems(CraftingInput craftingInput) {
        NonNullList<ItemStack> nonNullList = NonNullList.withSize(craftingInput.size(), ItemStack.EMPTY);

        for (int i = 0; i < nonNullList.size(); i++) {
            ItemStack item = craftingInput.getItem(i);
            nonNullList.set(i, item.getCraftingRemainder());
        }
        return nonNullList;
    }

}
