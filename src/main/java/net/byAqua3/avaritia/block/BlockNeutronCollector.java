package net.byAqua3.avaritia.block;

import net.byAqua3.avaritia.inventory.MenuNeutronCollector;
import net.byAqua3.avaritia.loader.AvaritiaBlocks;
import net.byAqua3.avaritia.tile.TileNeutronCollector;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class BlockNeutronCollector extends BlockMachine {

	public static final Component TITLE = Component.translatable("avaritia:container.neutron_collector.title");

	public BlockNeutronCollector(Properties properties) {
		super(properties, AvaritiaBlocks.NEUTRON_COLLECTOR_TILE::get);
	}

	@Override
	public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult result) {
		if (level.isClientSide()) {
			return InteractionResult.SUCCESS;
		} else {
			BlockEntity blockEntity = level.getBlockEntity(pos);
			if (blockEntity instanceof TileNeutronCollector) {
				TileNeutronCollector tile = (TileNeutronCollector) blockEntity;
				SimpleMenuProvider simpleMenuProvider = new SimpleMenuProvider(
						(id, inventory, access) -> new MenuNeutronCollector(id, inventory, tile, tile.dataAccess),
						TITLE);

				player.openMenu(simpleMenuProvider, pos);
			}
			return InteractionResult.CONSUME;
		}
	}

	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
		if (state.getBlock() != newState.getBlock()) {
			BlockEntity blockEntity = level.getBlockEntity(pos);

			if (blockEntity instanceof TileNeutronCollector) {
				TileNeutronCollector tile = (TileNeutronCollector) blockEntity;
				NonNullList<ItemStack> itemStacks = tile.result.getItems();
				for (ItemStack itemStack : itemStacks) {
					if (!itemStack.isEmpty()) {
						ItemEntity itemEntity = new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), itemStack);
						itemEntity.setDefaultPickUpDelay();
						level.addFreshEntity(itemEntity);
					}
				}
			}
		}
		super.onRemove(state, level, pos, newState, movedByPiston);
	}}
