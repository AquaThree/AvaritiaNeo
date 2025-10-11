package net.byAqua3.avaritia.item;

import net.byAqua3.avaritia.loader.AvaritiaTiers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.GrassBlock;
import net.minecraft.world.level.block.TallGrassBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.neoforge.common.ItemAbilities;

public class ItemInfinityHoe extends HoeItem {

	public ItemInfinityHoe(Properties properties) {
		super(AvaritiaTiers.INFINITY, properties.attributes(HoeItem.createAttributes(AvaritiaTiers.INFINITY, 29, 0.0F)));
	}

	@Override
	public int getDamage(ItemStack stack) {
		return 0;
	}

	@Override
	public boolean isFoil(ItemStack stack) {
		return false;
	}

	private boolean applyBonemeal(Level level, BlockPos pos, Player player) {
		BlockState blockState = level.getBlockState(pos);
		Block block = blockState.getBlock();
		if (block instanceof BonemealableBlock && !(block instanceof GrassBlock) && !(block instanceof TallGrassBlock) && !(block instanceof DoublePlantBlock)) {
			BonemealableBlock bonemealableBlock = (BonemealableBlock) block;
			if (bonemealableBlock.isValidBonemealTarget(level, pos, blockState)) {
				if (level instanceof ServerLevel) {
					if (bonemealableBlock.isBonemealSuccess(level, level.random, pos, blockState)) {
						bonemealableBlock.performBonemeal((ServerLevel) level, level.random, pos, blockState);
					}
				}
			} else {
				level.destroyBlock(pos, true);
				for (ItemStack stack : player.getInventory().items) {
					if (stack.getItem() instanceof BlockItem && ((BlockItem) stack.getItem()).getBlock() == block) {
						level.setBlockAndUpdate(pos, block.defaultBlockState());
						if (!player.isCreative()) {
							stack.shrink(1);
						}
						break;
					}
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		Level level = context.getLevel();
		BlockPos blockPos = context.getClickedPos();
		Player player = context.getPlayer();
		if (player.isShiftKeyDown()) {
			if (!this.applyBonemeal(level, blockPos, player)) {
				BlockState blockState = level.getBlockState(blockPos).getToolModifiedState(context, ItemAbilities.HOE_TILL, false);

				if (blockState != null) {
					level.playSound(player, blockPos, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0F, 1.0F);
					if (!level.isClientSide) {
						level.setBlock(blockPos, blockState, 11);
						level.gameEvent(GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Context.of(player, blockState));
					}
				}
			}
		} else {
			int blockRange = (int) Math.round(4.0D);

			for (int x = -blockRange; x <= blockRange; x++) {
				for (int z = -blockRange; z <= blockRange; z++) {
					BlockPos rangePos = new BlockPos(Mth.floor(blockPos.getX() + x), Mth.floor(blockPos.getY()), Mth.floor(blockPos.getZ() + z));
					BlockState rangeState = level.getBlockState(rangePos);
					Block rangeBlock = rangeState.getBlock();

					if (rangeBlock instanceof BonemealableBlock) {
						this.applyBonemeal(level, rangePos, player);
					} else {
						this.applyBonemeal(level, rangePos.offset(0, 1, 0), player);
					}

					if (rangeState.getToolModifiedState(context, ItemAbilities.HOE_TILL, false) != null) {
						rangeState = rangeState.getToolModifiedState(context, ItemAbilities.HOE_TILL, false);
						level.playSound(player, rangePos, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0F, 1.0F);
						if (!level.isClientSide) {
							level.setBlock(rangePos, rangeState, 11);
							level.gameEvent(GameEvent.BLOCK_CHANGE, rangePos, GameEvent.Context.of(player, rangeState));
						}
					}
				}
			}
		}
		return InteractionResult.sidedSuccess(level.isClientSide);
	}}
