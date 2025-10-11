package net.byAqua3.avaritia.inventory.slot;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class SlotFake extends Slot {

	public SlotFake(Container container, int slot, int x, int y) {
		super(container, slot, x, y);
	}
	
	@Override
	public boolean isFake() {
		return true;
	}
	
	@Override
	public boolean mayPlace(ItemStack stack) {
		return false;
	}
	
	@Override
	public boolean mayPickup(Player player) {
        return false;
    }}
