package net.byAqua3.avaritia.inventory;

import net.byAqua3.avaritia.loader.AvaritiaBlocks;
import net.byAqua3.avaritia.loader.AvaritiaMenus;
import net.byAqua3.avaritia.loader.AvaritiaRecipes;
import net.byAqua3.avaritia.tile.TileExtremeCraftingTable;

import java.util.Optional;

import net.byAqua3.avaritia.inventory.slot.SlotExtremeResult;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class MenuExtremeCrafting extends AbstractContainerMenu {

	private final Player player;

	private final TileExtremeCraftingTable tile;

	private final ContainerExtremeCrafting craftSlots;

	private final ResultContainer resultContainer = new ResultContainer();

	private final SlotExtremeResult resultSlot;

	public MenuExtremeCrafting(int id, Inventory inventory, FriendlyByteBuf friendlyByteBuf) {
		this(id, inventory, (TileExtremeCraftingTable) inventory.player.level().getBlockEntity(friendlyByteBuf.readBlockPos()));
	}

	public MenuExtremeCrafting(int id, Inventory inventory, TileExtremeCraftingTable tile) {
		super(AvaritiaMenus.EXTREME_CRAFTING.get(), id);
		this.player = inventory.player;
		this.tile = tile;
		this.craftSlots = new ContainerExtremeCrafting(this, 9, 9, tile.matrix);
		this.addSlot(this.resultSlot = new SlotExtremeResult(this.player, this.craftSlots, this.resultContainer, 0, 210, 80));

		int y;
		for (y = 0; y < 9; y++) {
			for (int i = 0; i < 9; i++) {
				this.addSlot(new Slot(this.craftSlots, i + y * 9, 12 + i * 18, 8 + y * 18));
			}
		}
		for (y = 0; y < 3; y++) {
			for (int i = 0; i < 9; i++) {
				this.addSlot(new Slot(inventory, i + y * 9 + 9, 39 + i * 18, 174 + y * 18));
			}
		}
		for (int x = 0; x < 9; x++) {
			this.addSlot(new Slot(inventory, x, 39 + x * 18, 232));
		}
		this.slotsChanged(this.craftSlots);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void slotsChanged(Container container) {
		Level level = this.tile.getLevel();
		if (level.isClientSide()) {
			return;
		}
		ItemStack itemStack = ItemStack.EMPTY;

		Optional<? extends RecipeHolder<Recipe<CraftingInput>>> recipe = level.getRecipeManager().getRecipeFor((RecipeType) AvaritiaRecipes.EXTREME_CRAFTING.get(), ((ContainerExtremeCrafting) container).asCraftInput(), level);

		if (recipe.isPresent()) {
			resultSlot.setUsedRecipe(recipe.get().value());
			itemStack = recipe.get().value().assemble(this.craftSlots.asCraftInput(), level.registryAccess());
		} else {
			resultSlot.setUsedRecipe(null);
		}
		this.resultContainer.setItem(0, itemStack);
	}

	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		ItemStack resultStack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		if (slot != null && slot.hasItem()) {
			ItemStack slotStack = slot.getItem();
			resultStack = slotStack.copy();

			if (index == 0) {
				if (!this.moveItemStackTo(slotStack, 82, 118, true)) {
					return ItemStack.EMPTY;
				}

				slot.onQuickCraft(slotStack, resultStack);
			} else if (index >= 82 && index < 118) {
				if (!this.moveItemStackTo(slotStack, 1, 82, false))
					if (index < 109) {
						if (!this.moveItemStackTo(slotStack, 109, 118, false))
							return ItemStack.EMPTY;
					} else if (!this.moveItemStackTo(slotStack, 82, 109, false)) {
						return ItemStack.EMPTY;
					}
			} else if (!this.moveItemStackTo(slotStack, 82, 118, false)) {
				return ItemStack.EMPTY;
			}
			if (slotStack.isEmpty()) {
				slot.setByPlayer(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}
			if (slotStack.getCount() == resultStack.getCount()) {
				return ItemStack.EMPTY;
			}
			slot.onTake(player, slotStack);
		}
		return resultStack;
	}

	@Override
	public boolean stillValid(Player player) {
		BlockPos pos = this.tile.getBlockPos();
		return this.tile.getLevel().getBlockState(pos).is(AvaritiaBlocks.EXTREME_CRAFTING_TABLE.get()) && player.distanceToSqr(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D) <= 64.0D;
	}}
