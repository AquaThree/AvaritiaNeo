package net.byAqua3.avaritia.inventory;

import net.byAqua3.avaritia.loader.AvaritiaMenus;
import net.byAqua3.avaritia.tile.TileNeutronCollector;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class MenuNeutronCollector extends MenuMachine<TileNeutronCollector> {

	public final ContainerData dataAccess;

	public MenuNeutronCollector(int id, Inventory inventory, FriendlyByteBuf friendlyByteBuf) {
		this(id, inventory, (TileNeutronCollector) inventory.player.level().getBlockEntity(friendlyByteBuf.readBlockPos()), new SimpleContainerData(1));
	}

	public MenuNeutronCollector(int id, Inventory inventory, TileNeutronCollector machineTile, ContainerData dataAccess) {
		super(AvaritiaMenus.NEUTRON_COLLECTOR.get(), id, inventory, machineTile);
		this.addSlot(new Slot(machineTile.result, 0, 80, 35) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return false;
			}
		});
		this.addPlayerInventory(8, 84);
		this.dataAccess = dataAccess;
		this.addDataSlots(this.dataAccess);
	}

	public int getProgress() {
		return this.dataAccess.get(0);
	}

	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		ItemStack resultStack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		if (slot != null && slot.hasItem()) {
			ItemStack slotStack = slot.getItem();
			resultStack = slotStack.copy();
			if (index == 0) {
				if (!this.moveItemStackTo(slotStack, 1, 37, true)) {
					return ItemStack.EMPTY;
				}
				slot.onQuickCraft(slotStack, resultStack);
			} else if (index >= 1 && index < 37) {
				if (index < 28) {
					if (!this.moveItemStackTo(slotStack, 28, 37, false)) {
						return ItemStack.EMPTY;
					}
				} else if (!this.moveItemStackTo(slotStack, 1, 28, false)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.moveItemStackTo(slotStack, 1, 37, false)) {
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
}
