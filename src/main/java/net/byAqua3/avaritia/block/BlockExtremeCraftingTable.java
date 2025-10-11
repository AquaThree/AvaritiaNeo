package net.byAqua3.avaritia.block;

import net.byAqua3.avaritia.inventory.MenuExtremeCrafting;
import net.byAqua3.avaritia.tile.TileExtremeCraftingTable;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class BlockExtremeCraftingTable extends Block implements EntityBlock {
	
	public static final Component TITLE = Component.translatable("avaritia:container.extreme_crafting_table.title");

	public BlockExtremeCraftingTable(Properties properties) {
		super(properties);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new TileExtremeCraftingTable(pos, state);
	}

	@Override
	public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult result) {
		if (level.isClientSide()) {
			return InteractionResult.SUCCESS;
		} else {
			BlockEntity blockEntity = level.getBlockEntity(pos);
			if (blockEntity instanceof TileExtremeCraftingTable) {
				TileExtremeCraftingTable tile = (TileExtremeCraftingTable) blockEntity;
				SimpleMenuProvider simpleMenuProvider = new SimpleMenuProvider(
						(id, inventory, access) -> new MenuExtremeCrafting(id, inventory, tile), Component.empty());

				player.openMenu(simpleMenuProvider, pos);
			}
			return InteractionResult.CONSUME;
		}
	}
	
}
