package net.byAqua3.avaritia.inventory;

import net.byAqua3.avaritia.tile.TileMachine;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;

public abstract class MenuMachine<T extends TileMachine> extends AbstractContainerMenu {

	private final Inventory inventory;

	public final T machineTile;

	protected MenuMachine(MenuType<?> menuType, int id, Inventory inventory, T machineTile) {
		super(menuType, id);
		this.inventory = inventory;
		this.machineTile = machineTile;
	}

	protected void addPlayerInventory(int xOffset, int yOffset) {
		for (int row = 0; row < 3; row++) {
			for (int i = 0; i < 9; i++) {
				this.addSlot(new Slot(this.inventory, i + row * 9 + 9, xOffset + i * 18, yOffset + row * 18));
			}
		}
		for (int slot = 0; slot < 9; slot++) {
			this.addSlot(new Slot(this.inventory, slot, xOffset + slot * 18, yOffset + 58));
		}
	}

	@Override
	public boolean stillValid(Player player) {
		BlockPos pos = this.machineTile.getBlockPos();
		return this.machineTile.getLevel().getBlockState(pos).is(this.machineTile.getBlockState().getBlock()) && player.distanceToSqr(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D) <= 64.0D;
	}
}
