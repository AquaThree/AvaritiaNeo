package net.byAqua3.avaritia.inventory.slot;

import net.byAqua3.avaritia.loader.AvaritiaDataComponents;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class SlotInfinity extends Slot {

	public SlotInfinity(Container container, int slotId, int pX, int pY) {
		super(container, slotId, pX, pY);
	}
	
	@Override
	public int getMaxStackSize() {
        return this.container.getMaxStackSize();
    }

	@Override
    public int getMaxStackSize(ItemStack stack) {
        return Math.max(this.getMaxStackSize(), stack.getMaxStackSize());
    }
	
	@Override
	public ItemStack getItem() {
		ItemStack itemStack = this.container.getItem(this.getContainerSlot());
		itemStack.update(AvaritiaDataComponents.MAX_STACK_SIZE.get(), itemStack.getMaxStackSize(), size -> Integer.MAX_VALUE);
        return itemStack;
    }

}
