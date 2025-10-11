package net.byAqua3.avaritia.inventory;

import net.byAqua3.avaritia.tile.TileExtremeCraftingTable;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.item.ItemStack;

public class ContainerExtremeResult extends ResultContainer {

	private final TileExtremeCraftingTable tile;

	public ContainerExtremeResult(TileExtremeCraftingTable tile) {
		this.tile = tile;
	}

	@Override
	public int getContainerSize() {
		return this.tile.result.getContainerSize();
	}

	@Override
	public boolean isEmpty() {
		return this.tile.result.isEmpty();
	}

	@Override
	public ItemStack getItem(int slot) {
		return this.tile.result.getItem(slot);
	}

	@Override
	public ItemStack removeItemNoUpdate(int slot) {
		return this.tile.result.removeItemNoUpdate(slot);
	}

	@Override
	public ItemStack removeItem(int slot, int count) {
		return this.tile.result.removeItem(slot, count);
	}

	@Override
	public void setItem(int slot, ItemStack stack) {
		this.tile.result.setItem(slot, stack);
	}

	@Override
	public void setChanged() {
		this.tile.result.setChanged();
	}}
