package net.byAqua3.avaritia.inventory;

import net.byAqua3.avaritia.loader.AvaritiaBlocks;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.CraftingMenu;

public class MenuCustomCrafting extends CraftingMenu {
	
	private final ContainerLevelAccess access;

	public MenuCustomCrafting(int id, Inventory inventory, ContainerLevelAccess access) {
		super(id, inventory, access);
		this.access = access;
	}
	
	@Override
    public boolean stillValid(Player player) {
        return stillValid(this.access, player, AvaritiaBlocks.COMPRESSED_CRAFTING_TABLE.get()) || stillValid(this.access, player, AvaritiaBlocks.DOUBLE_COMPRESSED_CRAFTING_TABLE.get());
    }}
