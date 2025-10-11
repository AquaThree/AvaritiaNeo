package net.byAqua3.avaritia.util;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandlerModifiable;

public class WrapperInfinityChest implements IItemHandlerModifiable {
	
    private final Container inventory;

    public WrapperInfinityChest(Container inventory) {
        this.inventory = inventory;
    }
    
    public Container getInventory() {
        return inventory;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WrapperInfinityChest that = (WrapperInfinityChest) o;

        return this.getInventory().equals(that.getInventory());
    }

    @Override
    public int hashCode() {
        return this.getInventory().hashCode();
    }

    @Override
    public int getSlots() {
        return this.getInventory().getContainerSize();
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return this.getInventory().getItem(slot);
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        }
        ItemStack stackInSlot = this.getInventory().getItem(slot);

        int m;
        if (!stackInSlot.isEmpty()) {
            if (stackInSlot.getCount() >= Math.min(this.getInventory().getMaxStackSize(stackInSlot), this.getSlotLimit(slot))) {
                return stack;
            }

            if (!ItemStack.isSameItemSameComponents(stack, stackInSlot)) {
                return stack;
            }

            if (!this.getInventory().canPlaceItem(slot, stack)) {
                return stack;
            }

            m = Math.min(this.getInventory().getMaxStackSize(stack), this.getSlotLimit(slot)) - stackInSlot.getCount();

            if (stack.getCount() <= m) {
                if (!simulate) {
                    ItemStack copy = stack.copy();
                    copy.grow(stackInSlot.getCount());
                    this.getInventory().setItem(slot, copy);
                    this.getInventory().setChanged();
                }

                return ItemStack.EMPTY;
            } else {
                // copy the stack to not modify the original one
                stack = stack.copy();
                if (!simulate) {
                    ItemStack copy = stack.split(m);
                    copy.grow(stackInSlot.getCount());
                    this.getInventory().setItem(slot, copy);
                    this.getInventory().setChanged();
                    return stack;
                } else {
                    stack.shrink(m);
                    return stack;
                }
            }
        } else {
            if (!this.getInventory().canPlaceItem(slot, stack)) {
                return stack;
            }

            m = Math.min(this.getInventory().getMaxStackSize(stack), this.getSlotLimit(slot));
            if (m < stack.getCount()) {
                // copy the stack to not modify the original one
                stack = stack.copy();
                if (!simulate) {
                	this.getInventory().setItem(slot, stack.split(m));
                	this.getInventory().setChanged();
                    return stack;
                } else {
                    stack.shrink(m);
                    return stack;
                }
            } else {
                if (!simulate) {
                	this.getInventory().setItem(slot, stack);
                	this.getInventory().setChanged();
                }
                return ItemStack.EMPTY;
            }
        }
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
    	 if (amount == 0) {
             return ItemStack.EMPTY;
    	 }
    	 
         ItemStack stackInSlot = this.getInventory().getItem(slot);

         if (stackInSlot.isEmpty()) {
             return ItemStack.EMPTY;
         }
         if (simulate) {
             if (stackInSlot.getCount() < amount) {
                 return stackInSlot.copy();
             } else {
                 ItemStack copy = stackInSlot.copy();
                 copy.setCount(amount);
                 return copy;
             }
         } else {
             int m = Math.min(stackInSlot.getCount(), amount);

             ItemStack decrStackSize = this.getInventory().removeItem(slot, m);
             this.getInventory().setChanged();
             return decrStackSize;
         }
    }

    @Override
    public void setStackInSlot(int slot, ItemStack stack) {
    	this.getInventory().setItem(slot, stack);
    }

    @Override
    public int getSlotLimit(int slot) {
        return this.getInventory().getMaxStackSize();
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return this.getInventory().canPlaceItem(slot, stack);
    }
}
