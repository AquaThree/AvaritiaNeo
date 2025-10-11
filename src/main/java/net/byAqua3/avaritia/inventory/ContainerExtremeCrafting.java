package net.byAqua3.avaritia.inventory;

import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.TransientCraftingContainer;
import net.minecraft.world.item.ItemStack;

public class ContainerExtremeCrafting extends TransientCraftingContainer {

	private final SimpleContainer matrix;

	private final AbstractContainerMenu container;

	public ContainerExtremeCrafting(AbstractContainerMenu container, int width, int height, SimpleContainer matrix) {
		super(container, width, height, matrix.getItems());
		this.matrix = matrix;
		this.container = container;
	}

	@Override
	public int getContainerSize() {
		return this.matrix.getContainerSize();
	}

	@Override
	public boolean isEmpty() {
		return this.matrix.isEmpty();
	}

	@Override
	public ItemStack getItem(int slot) {
		return this.matrix.getItem(slot);
	}

	@Override
	public ItemStack removeItemNoUpdate(int slot) {
		return this.matrix.removeItemNoUpdate(slot);
	}

	@Override
	public ItemStack removeItem(int slot, int count) {
		ItemStack itemStack = this.matrix.removeItem(slot, count);
		if (!itemStack.isEmpty()) {
			this.container.slotsChanged(this);
		}
		return itemStack;
	}

	@Override
	public void setItem(int slot, ItemStack stack) {
		this.matrix.setItem(slot, stack);
		this.container.slotsChanged(this);
	}

	@Override
	public void setChanged() {
		this.matrix.setChanged();
	}

	@Override
	public void fillStackedContents(StackedContents stackedContents) {
		for (int i = 0; i < this.matrix.getContainerSize(); i++) {
			stackedContents.accountStack(this.matrix.getItem(i));
		}
	}}
