package net.byAqua3.avaritia.inventory;

import java.util.Optional;

import net.byAqua3.avaritia.inventory.slot.SlotInfinity;
import net.byAqua3.avaritia.loader.AvaritiaBlocks;
import net.byAqua3.avaritia.loader.AvaritiaDataComponents;
import net.byAqua3.avaritia.loader.AvaritiaMenus;
import net.byAqua3.avaritia.tile.TileInfinityChest;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class MenuInfinityChest extends AbstractContainerMenu {

	private final TileInfinityChest tile;

	public MenuInfinityChest(int id, Inventory inventory, FriendlyByteBuf friendlyByteBuf) {
		this(id, inventory, (TileInfinityChest) inventory.player.level().getBlockEntity(friendlyByteBuf.readBlockPos()));
	}

	public MenuInfinityChest(int id, Inventory inventory, TileInfinityChest tile) {
		super(AvaritiaMenus.INFINITY_CHEST.get(), id);
		this.tile = tile;

		int y;
		for (y = 0; y < 12; y++) {
			for (int i = 0; i < 25; i++) {
				this.addSlot(new SlotInfinity(tile.chest, i + y * 25, 8 + i * 18, 18 + y * 18));
			}
		}

		for (y = 0; y < 3; y++) {
			for (int i = 0; i < 9; i++) {
				this.addSlot(new Slot(inventory, i + y * 9 + 9, 152 + i * 18, 248 + y * 18));
			}
		}
		for (int x = 0; x < 9; x++) {
			this.addSlot(new Slot(inventory, x, 152 + x * 18, 306));
		}
	}

	public TileInfinityChest getTile() {
		return this.tile;
	}
	
	private boolean tryItemClickBehaviourOverride(Player player, ClickAction clickAction, Slot slot, ItemStack clickedItem, ItemStack carriedItem) {
        // Neo: Fire the ItemStackedOnOtherEvent, and return true if it was cancelled (meaning the event was handled). Returning true will trigger the container to stop processing further logic.
        if (net.neoforged.neoforge.common.CommonHooks.onItemStackedOn(clickedItem, carriedItem, slot, clickAction, player, createCarriedSlotAccess())) {
            return true;
        }

        FeatureFlagSet featureflagset = player.level().enabledFeatures();
        return carriedItem.isItemEnabled(featureflagset) && carriedItem.overrideStackedOnOther(slot, clickAction, player)
            ? true
            : clickedItem.isItemEnabled(featureflagset)
                && clickedItem.overrideOtherStackedOnMe(carriedItem, slot, clickAction, player, this.createCarriedSlotAccess());
    }
	
	 private SlotAccess createCarriedSlotAccess() {
	        return new SlotAccess() {
	            @Override
	            public ItemStack get() {
	                return MenuInfinityChest.this.getCarried();
	            }

	            @Override
	            public boolean set(ItemStack stack) {
	            	MenuInfinityChest.this.setCarried(stack);
	                return true;
	            }
	        };
	    }
	
	@Override
	public void clicked(int slotId, int button, ClickType clickType, Player player) {
		Inventory inventory = player.getInventory();
		if ((clickType == ClickType.PICKUP || clickType == ClickType.QUICK_MOVE) && (button == 0 || button == 1)) {
            ClickAction clickaction = button == 0 ? ClickAction.PRIMARY : ClickAction.SECONDARY;
            if (slotId == -999) {
                if (!this.getCarried().isEmpty()) {
                	if(this.getCarried().has(AvaritiaDataComponents.MAX_STACK_SIZE.get())) {
                	   this.getCarried().remove(AvaritiaDataComponents.MAX_STACK_SIZE.get());
   			        }
                    if (clickaction == ClickAction.PRIMARY) {
                        player.drop(this.getCarried(), true);
                        this.setCarried(ItemStack.EMPTY);
                    } else {
                        player.drop(this.getCarried().split(1), true);
                    }
                }
            } else if (clickType == ClickType.QUICK_MOVE) {
                if (slotId < 0) {
                    return;
                }

                Slot slot6 = this.slots.get(slotId);
                if (!slot6.mayPickup(player)) {
                    return;
                }

                ItemStack itemstack8 = this.quickMoveStack(player, slotId);

                while (!itemstack8.isEmpty() && ItemStack.isSameItem(slot6.getItem(), itemstack8)) {
                    itemstack8 = this.quickMoveStack(player, slotId);
                }
            } else {
                if (slotId < 0) {
                    return;
                }

                Slot slot7 = this.slots.get(slotId);
                ItemStack itemstack9 = slot7.getItem();
                ItemStack itemstack10 = this.getCarried();
                player.updateTutorialInventoryAction(itemstack10, slot7.getItem(), clickaction);
                if (!this.tryItemClickBehaviourOverride(player, clickaction, slot7, itemstack9, itemstack10)) {
                    if (itemstack9.isEmpty()) {
                        if (!itemstack10.isEmpty()) {
                        	if (slot7.mayPlace(itemstack10)) {
                            	if (slotId >= 300 && slotId < 336) {
                            	    if(itemstack10.has(AvaritiaDataComponents.MAX_STACK_SIZE.get())) {
                            	       itemstack10.remove(AvaritiaDataComponents.MAX_STACK_SIZE.get());
                  			        }
                            	} else {
                            		itemstack10.update(AvaritiaDataComponents.MAX_STACK_SIZE.get(), itemstack10.getMaxStackSize(), size -> Integer.MAX_VALUE);
                            	}
                        	}
                            int i3 = clickaction == ClickAction.PRIMARY ? itemstack10.getCount() : 1;
                            this.setCarried(slot7.safeInsert(itemstack10, i3));
                        }
                    } else if (slot7.mayPickup(player)) {
                        if (itemstack10.isEmpty()) {
                            int j3 = clickaction == ClickAction.PRIMARY ? Math.min(64, itemstack9.getCount()) : (Math.min(64, itemstack9.getCount()) + 1) / 2;
                            Optional<ItemStack> optional1 = slot7.tryRemove(j3, Integer.MAX_VALUE, player);
                            optional1.ifPresent(p_150421_ -> {
                                this.setCarried(p_150421_);
                                slot7.onTake(player, p_150421_);
                            });
                        } else if (slot7.mayPlace(itemstack10)) {
                        	if (slotId >= 300 && slotId < 336) {
                        	    if(itemstack10.has(AvaritiaDataComponents.MAX_STACK_SIZE.get())) {
                        	       itemstack10.remove(AvaritiaDataComponents.MAX_STACK_SIZE.get());
              			        }
                        	} else {
                        		itemstack10.update(AvaritiaDataComponents.MAX_STACK_SIZE.get(), itemstack10.getMaxStackSize(), size -> Integer.MAX_VALUE);
                        	}
                            if (ItemStack.isSameItemSameComponents(itemstack9, itemstack10)) {
                                int k3 = clickaction == ClickAction.PRIMARY ? itemstack10.getCount() : 1;
                                this.setCarried(slot7.safeInsert(itemstack10, k3));
                            } else if (itemstack10.getCount() <= slot7.getMaxStackSize(itemstack10)) {
                                this.setCarried(itemstack9);
                                slot7.setByPlayer(itemstack10);
                            }
                        } else if (ItemStack.isSameItemSameComponents(itemstack9, itemstack10)) {
                            Optional<ItemStack> optional = slot7.tryRemove(
                                itemstack9.getCount(), itemstack10.getMaxStackSize() - itemstack10.getCount(), player
                            );
                            optional.ifPresent(p_150428_ -> {
                                itemstack10.grow(p_150428_.getCount());
                                slot7.onTake(player, p_150428_);
                            });
                        }
                    }
                }

                slot7.setChanged();
            }
        } else if (clickType == ClickType.SWAP && (button >= 0 && button < 9 || button == 40)) {
            ItemStack itemstack2 = inventory.getItem(button);
            Slot slot5 = this.slots.get(slotId);
            ItemStack itemstack7 = slot5.getItem();
            if (!itemstack2.isEmpty() || !itemstack7.isEmpty()) {
                if (itemstack2.isEmpty()) {
                    if (slot5.mayPickup(player)) {
                    	ItemStack itemstack3 = itemstack7.split(Math.min(itemstack7.getCount(), Math.min(64, itemstack7.getMaxStackSize())));
                    	if(itemstack3.has(AvaritiaDataComponents.MAX_STACK_SIZE.get())) {
                    	   itemstack3.remove(AvaritiaDataComponents.MAX_STACK_SIZE.get());
     			        }
                        inventory.setItem(button, itemstack3);
                        //slot5.onSwapCraft(itemstack7.getCount());
                        slot5.onTake(player, itemstack7);
                    }
                } else if (itemstack7.isEmpty()) {
                    if (slot5.mayPlace(itemstack2)) {
                        int j2 = slot5.getMaxStackSize(itemstack2);
                        if (itemstack2.getCount() > j2) {
                            slot5.setByPlayer(itemstack2.split(j2));
                        } else {
                            inventory.setItem(button, ItemStack.EMPTY);
                            if(slotId >= 300 && slotId < 336) {
                            	if(itemstack2.has(AvaritiaDataComponents.MAX_STACK_SIZE.get())) {
                            	   itemstack2.remove(AvaritiaDataComponents.MAX_STACK_SIZE.get());
              			        }
                            } else {
                            	itemstack2.update(AvaritiaDataComponents.MAX_STACK_SIZE.get(), itemstack2.getMaxStackSize(), size -> Integer.MAX_VALUE);
                            }
                            slot5.setByPlayer(itemstack2);
                        }
                    }
                } else if (slot5.mayPickup(player) && slot5.mayPlace(itemstack2) && itemstack7.getCount() <= Math.min(itemstack7.getMaxStackSize(), Math.min(64, slot5.getMaxStackSize()))) {
                	if(itemstack7.has(AvaritiaDataComponents.MAX_STACK_SIZE.get())) {
              	       itemstack7.remove(AvaritiaDataComponents.MAX_STACK_SIZE.get());
			            }
             	    inventory.setItem(button, itemstack7);
                 	if(inventory.getItem(button).has(AvaritiaDataComponents.MAX_STACK_SIZE.get())) {
                        inventory.getItem(button).remove(AvaritiaDataComponents.MAX_STACK_SIZE.get());
    			        }
                 	itemstack2.update(AvaritiaDataComponents.MAX_STACK_SIZE.get(), itemstack2.getMaxStackSize(), size -> Integer.MAX_VALUE);
                 	if(slotId >= 300 && slotId < 336) {
                     	if(itemstack2.has(AvaritiaDataComponents.MAX_STACK_SIZE.get())) {
                     	   itemstack2.remove(AvaritiaDataComponents.MAX_STACK_SIZE.get());
       			        }
                     } else {
                     	itemstack2.update(AvaritiaDataComponents.MAX_STACK_SIZE.get(), itemstack2.getMaxStackSize(), size -> Integer.MAX_VALUE);
                     }
                     slot5.setByPlayer(itemstack2);
                     slot5.onTake(player, itemstack7);
                }
            }
        } else if (clickType == ClickType.CLONE && player.hasInfiniteMaterials() && this.getCarried().isEmpty() && slotId >= 0) {
            Slot slot4 = this.slots.get(slotId);
            if (slot4.hasItem()) {
                ItemStack itemstack5 = slot4.getItem();
                ItemStack itemstack6 = itemstack5.copyWithCount(Math.min(64, itemstack5.getMaxStackSize()));
                if(itemstack6.has(AvaritiaDataComponents.MAX_STACK_SIZE.get())) {
             	   itemstack6.remove(AvaritiaDataComponents.MAX_STACK_SIZE.get());
			    }
                this.setCarried(itemstack6);
            }
        } else if (clickType == ClickType.THROW && this.getCarried().isEmpty() && slotId >= 0) {
            Slot slot3 = this.slots.get(slotId);
            int j1 = button == 0 ? 1 : Math.min(64, slot3.getItem().getCount());
            ItemStack itemstack7 = slot3.safeTake(j1, Integer.MAX_VALUE, player);
            if(itemstack7.has(AvaritiaDataComponents.MAX_STACK_SIZE.get())) {
               itemstack7.remove(AvaritiaDataComponents.MAX_STACK_SIZE.get());
			}
            player.drop(itemstack7, true);
        } else {
        	super.clicked(slotId, button, clickType, player);
        }
	}

	protected boolean moveItemStackTo(ItemStack stack, int startIndex, int endIndex, boolean reverseDirection, boolean inventory) {
		boolean flag = false;
		int i = startIndex;
		if (reverseDirection) {
			i = endIndex - 1;
		}

		if (!stack.isEmpty()) {
			while (reverseDirection ? i >= startIndex : i < endIndex) {
				Slot slot = this.slots.get(i);
				ItemStack itemStack = slot.getItem();
				if (!itemStack.isEmpty() && ItemStack.isSameItemSameComponents(stack, itemStack)) {
					int j = itemStack.getCount() + stack.getCount();
					int maxSize = inventory ? Math.min(Math.min(64, slot.getMaxStackSize()), stack.getMaxStackSize()) : Math.max(slot.getMaxStackSize(), stack.getMaxStackSize());
					if (j <= maxSize && j > 0) {
						if(!inventory) {
					       itemStack.update(AvaritiaDataComponents.MAX_STACK_SIZE.get(), itemStack.getMaxStackSize(), size -> Integer.MAX_VALUE);
						}
						stack.setCount(0);
						itemStack.setCount(j);
						slot.setChanged();
						flag = true;
					} else if (itemStack.getCount() < maxSize) {
						if(inventory) {
						   if(itemStack.has(AvaritiaDataComponents.MAX_STACK_SIZE.get())) {
						      itemStack.remove(AvaritiaDataComponents.MAX_STACK_SIZE.get());
						   }
						}
						stack.shrink(maxSize - itemStack.getCount());
						itemStack.setCount(maxSize);
						slot.setChanged();
						flag = true;
					}
				}

				if (reverseDirection) {
					--i;
				} else {
					++i;
				}
			}
		}

		if (!stack.isEmpty()) {
			if (reverseDirection) {
				i = endIndex - 1;
			} else {
				i = startIndex;
			}

			while (reverseDirection ? i >= startIndex : i < endIndex) {
				Slot slot1 = this.slots.get(i);
				ItemStack itemstack1 = slot1.getItem();
				if (itemstack1.isEmpty() && slot1.mayPlace(stack)) {
					if(!inventory) {
						stack.update(AvaritiaDataComponents.MAX_STACK_SIZE.get(), stack.getMaxStackSize(), size -> Integer.MAX_VALUE);
					}
					slot1.setByPlayer(stack.split(Math.min(stack.getCount(), Math.min(64, slot1.getMaxStackSize()))));
					if(inventory) {
				       if(slot1.getItem().has(AvaritiaDataComponents.MAX_STACK_SIZE.get())) {
					      slot1.getItem().remove(AvaritiaDataComponents.MAX_STACK_SIZE.get());
					   }
					}
					slot1.setChanged();
					flag = true;
					break;
				}

				if (reverseDirection) {
					--i;
				} else {
					++i;
				}
			}
		}

		return flag;
	}

	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		ItemStack resultStack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		if (slot != null && slot.hasItem()) {
			ItemStack slotStack = slot.getItem();

			if (index < 300) {
				if (!this.moveItemStackTo(slotStack, 300, 336, true, true)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.moveItemStackTo(slotStack, 0, 300, false, false)) {
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
		return this.tile.getLevel().getBlockState(pos).is(AvaritiaBlocks.INFINITY_CHEST.get())
				&& player.distanceToSqr(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D) <= 64.0D;
	}

}
