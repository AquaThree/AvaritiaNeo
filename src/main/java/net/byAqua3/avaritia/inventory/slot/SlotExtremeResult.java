package net.byAqua3.avaritia.inventory.slot;

import net.byAqua3.avaritia.loader.AvaritiaRecipes;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;

public class SlotExtremeResult extends ResultSlot {

	private final CraftingContainer craftSlots;

	private Recipe<CraftingInput> usedRecipe;

	public SlotExtremeResult(Player player, CraftingContainer craftSlots, Container container, int idx, int xPos,
			int yPos) {
		super(player, craftSlots, container, idx, xPos, yPos);
		this.craftSlots = craftSlots;
	}

	public void setUsedRecipe(Recipe<CraftingInput> usedRecipe) {
		this.usedRecipe = usedRecipe;
	}
	
	private NonNullList<ItemStack> copyAllInputItems(CraftingInput craftingInput) {
        NonNullList<ItemStack> nonNullList = NonNullList.withSize(craftingInput.size(), ItemStack.EMPTY);

        for (int i = 0; i < nonNullList.size(); i++) {
        	nonNullList.set(i, craftingInput.getItem(i));
        }

        return nonNullList;
    }
	
	private NonNullList<ItemStack> getRemainingItems(CraftingInput craftingInput, Level level) {
        return level instanceof ServerLevel serverlevel
            ? serverlevel.getServer().getRecipeManager().getRecipeFor(AvaritiaRecipes.EXTREME_CRAFTING.get(), craftingInput, serverlevel).map(recipeHolder -> recipeHolder.value().getRemainingItems(craftingInput))
                .orElseGet(() -> copyAllInputItems(craftingInput))
            : CraftingRecipe.defaultCraftingReminder(craftingInput);
    }

	@Override
	public void onTake(Player player, ItemStack stack) {
		if (this.usedRecipe == null) {
			return;
		}

		CraftingInput craftingInput = this.craftSlots.asCraftInput();
		int i = this.craftSlots.asPositionedCraftInput().left();
		int j = this.craftSlots.asPositionedCraftInput().top();
		NonNullList<ItemStack> remainingItems = this.getRemainingItems(craftingInput, player.level());;

		for (int k = 0; k < craftingInput.height(); k++) {
			for (int l = 0; l < craftingInput.width(); l++) {
				int i1 = l + i + (k + j) * this.craftSlots.getWidth();
				ItemStack slotStack = this.craftSlots.getItem(i1);
				ItemStack remainingStack = remainingItems.get(l + k * craftingInput.width());

				if (!slotStack.isEmpty()) {
					this.craftSlots.removeItem(i1, 1);
					slotStack = this.craftSlots.getItem(i1);
				}

				if (!remainingStack.isEmpty()) {
					if (slotStack.isEmpty()) {
						this.craftSlots.setItem(i1, remainingStack);
					} else if (ItemStack.isSameItemSameComponents(slotStack, remainingStack)) {
						remainingStack.grow(slotStack.getCount());
						this.craftSlots.setItem(i1, remainingStack);
					} else if (!player.getInventory().add(remainingStack)) {
						player.drop(remainingStack, false);
					}
				}
			}
		}
	}

}
