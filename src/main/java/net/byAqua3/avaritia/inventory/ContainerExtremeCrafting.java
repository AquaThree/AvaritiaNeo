package net.byAqua3.avaritia.inventory;

import java.util.List;

import net.minecraft.core.NonNullList;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;

public class ContainerExtremeCrafting implements CraftingContainer {

	private final SimpleContainer matrix;
	
	private final NonNullList<ItemStack> items;
    private final int width;
    private final int height;
    private final AbstractContainerMenu container;

	public ContainerExtremeCrafting(AbstractContainerMenu container, int width, int height, SimpleContainer matrix) {
		this.matrix = matrix;
		this.container = container;
        this.width = width;
        this.height = height;
        this.items = matrix.getItems();
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
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public void clearContent() {
        this.items.clear();
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public List<ItemStack> getItems() {
        return List.copyOf(this.items);
    }

	@Override
	public void fillStackedContents(StackedItemContents stackedContents) {
		for (int i = 0; i < this.matrix.getContainerSize(); i++) {
			stackedContents.accountSimpleStack(this.matrix.getItem(i));
		}
	}

}
