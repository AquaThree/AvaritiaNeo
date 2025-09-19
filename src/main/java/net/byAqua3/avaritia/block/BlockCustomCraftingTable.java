package net.byAqua3.avaritia.block;

import net.byAqua3.avaritia.inventory.MenuCustomCrafting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CraftingTableBlock;
import net.minecraft.world.level.block.state.BlockState;

public class BlockCustomCraftingTable extends CraftingTableBlock {
	
	private static final Component CONTAINER_TITLE = Component.translatable("container.crafting");

	public BlockCustomCraftingTable(Properties properties) {
		super(properties);
	}
	
	@Override
    public MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        return new SimpleMenuProvider(
            (id, inventory, access) -> new MenuCustomCrafting(id, inventory, ContainerLevelAccess.create(level, pos)), CONTAINER_TITLE
        );
    }

}
