package net.byAqua3.avaritia.util;

import net.byAqua3.avaritia.loader.AvaritiaDataComponents;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandlerModifiable;

public class ItemHandlerInfinity implements IItemHandlerModifiable {
    private final Container inv;

    public ItemHandlerInfinity(Container inv) {
        this.inv = inv;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        ItemHandlerInfinity that = (ItemHandlerInfinity) o;

        return getInv().equals(that.getInv());
    }

    @Override
    public int hashCode() {
        return getInv().hashCode();
    }

    @Override
    public int getSlots() {
        return getInv().getContainerSize();
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return getInv().getItem(slot);
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if (stack.isEmpty())
            return ItemStack.EMPTY;

        ItemStack stackInSlot = getInv().getItem(slot);
        
        stack.update(AvaritiaDataComponents.MAX_STACK_SIZE.get(), stack.getMaxStackSize(), size -> Integer.MAX_VALUE);

        int m;
        if (!stackInSlot.isEmpty()) {
            if (stackInSlot.getCount() >= Math.max(stackInSlot.getMaxStackSize(), getSlotLimit(slot))) {
                return stack;
            }

            if (!ItemStack.isSameItemSameComponents(stack, stackInSlot)) {
                return stack;
            }

            if (!getInv().canPlaceItem(slot, stack)) {
                return stack;
            }

            m = Math.max(stack.getMaxStackSize(), getSlotLimit(slot)) - stackInSlot.getCount();

            if (stack.getCount() <= m) {
                if (!simulate) {
                    ItemStack copy = stack.copy();
                    copy.grow(stackInSlot.getCount());
                    getInv().setItem(slot, copy);
                    getInv().setChanged();
                }

                return ItemStack.EMPTY;
            } else {
                // copy the stack to not modify the original one
                stack = stack.copy();
                if (!simulate) {
                    ItemStack copy = stack.split(m);
                    copy.grow(stackInSlot.getCount());
                    getInv().setItem(slot, copy);
                    getInv().setChanged();
                    return stack;
                } else {
                    stack.shrink(m);
                    return stack;
                }
            }
        } else {
            if (!getInv().canPlaceItem(slot, stack)) {
                return stack;
            }

            m = Math.max(stack.getMaxStackSize(), getSlotLimit(slot));
            if (m < stack.getCount()) {
                // copy the stack to not modify the original one
                stack = stack.copy();
                if (!simulate) {
                    getInv().setItem(slot, stack.split(m));
                    getInv().setChanged();
                    return stack;
                } else {
                    stack.shrink(m);
                    return stack;
                }
            } else {
                if (!simulate) {
                    getInv().setItem(slot, stack);
                    getInv().setChanged();
                }
                return ItemStack.EMPTY;
            }
        }
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (amount == 0)
            return ItemStack.EMPTY;

        ItemStack stackInSlot = getInv().getItem(slot);

        if (stackInSlot.isEmpty())
            return ItemStack.EMPTY;

        if (simulate) {
            if (stackInSlot.getCount() < amount) {
            	ItemStack stack = stackInSlot.copy();
            	
            	if(stack.has(AvaritiaDataComponents.MAX_STACK_SIZE.get())) {
            	   stack.remove(AvaritiaDataComponents.MAX_STACK_SIZE.get());
         		}
                return stack;
            } else {
                ItemStack stack = stackInSlot.copy();
                stack.setCount(amount);
                
                if(stack.has(AvaritiaDataComponents.MAX_STACK_SIZE.get())) {
             	   stack.remove(AvaritiaDataComponents.MAX_STACK_SIZE.get());
          		}
                return stack;
            }
        } else {
            int m = Math.min(stackInSlot.getCount(), amount);

            ItemStack decrStackSize = getInv().removeItem(slot, m);
            getInv().setChanged();
            
            if(decrStackSize.has(AvaritiaDataComponents.MAX_STACK_SIZE.get())) {
               decrStackSize.remove(AvaritiaDataComponents.MAX_STACK_SIZE.get());
       		}
            return decrStackSize;
        }
    }

    @Override
    public void setStackInSlot(int slot, ItemStack stack) {
        getInv().setItem(slot, stack);
    }

    @Override
    public int getSlotLimit(int slot) {
        return getInv().getMaxStackSize();
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return getInv().canPlaceItem(slot, stack);
    }

    public Container getInv() {
        return inv;
    }
}
