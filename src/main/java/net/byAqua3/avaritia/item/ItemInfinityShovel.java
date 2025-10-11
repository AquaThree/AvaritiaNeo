package net.byAqua3.avaritia.item;

import java.util.ArrayList;
import java.util.List;

import net.byAqua3.avaritia.loader.AvaritiaBlockTags;
import net.byAqua3.avaritia.loader.AvaritiaDataComponents;
import net.byAqua3.avaritia.loader.AvaritiaTiers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class ItemInfinityShovel extends ShovelItem {

	public ItemInfinityShovel(Properties properties) {
		super(AvaritiaTiers.INFINITY, properties.attributes(ShovelItem.createAttributes(AvaritiaTiers.INFINITY, 16, -2.0F)));
	}

	@Override
	public int getDamage(ItemStack stack) {
		return 0;
	}

	@Override
	public boolean isFoil(ItemStack stack) {
		return false;
	}

	@Override
	public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity livingEntity) {
		int blockRange = (int) Math.round(8.0D);

		if (livingEntity instanceof Player) {
			Player player = (Player) livingEntity;

			if (stack.has(AvaritiaDataComponents.DESTROYER.get()) && stack.getOrDefault(AvaritiaDataComponents.DESTROYER.get(), false)) {
				List<ItemStack> drops = new ArrayList<>();

				for (int x = -blockRange; x <= blockRange; x++) {
					for (int y = -blockRange; y <= blockRange; y++) {
						for (int z = -blockRange; z <= blockRange; z++) {
							BlockPos rangePos = new BlockPos(Mth.floor(pos.getX() + x), Mth.floor(pos.getY() + y), Mth.floor(pos.getZ() + z));
							BlockState rangeState = level.getBlockState(rangePos);
							Block rangeBlock = rangeState.getBlock();
							List<TagKey<Block>> tags = rangeState.getTags().toList();
							if (!rangeState.isAir() && tags.contains(AvaritiaBlockTags.INFINITY_SHOVEL)) {
								if (!level.isClientSide() && !player.isCreative()) {
									List<ItemStack> blockDrops = Block.getDrops(rangeState, (ServerLevel) level, pos, null);
									if (!blockDrops.isEmpty()) {
										drops.addAll(blockDrops);
									} else {
										ResourceLocation blockKey = BuiltInRegistries.BLOCK.getKey(rangeBlock);
										Item blockItem = BuiltInRegistries.ITEM.get(blockKey);
										drops.add(new ItemStack(blockItem));
									}
								}
								level.destroyBlock(rangePos, false);
							}
						}
					}
				}
				if (!level.isClientSide() && !drops.isEmpty()) {
					ItemEntity itemEntity = new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), ItemMatterCluster.makeCluster(drops));
					itemEntity.setDefaultPickUpDelay();
					level.addFreshEntity(itemEntity);
				}
			}
		}
		return super.mineBlock(stack, level, state, pos, livingEntity);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (player.isShiftKeyDown()) {
			if (!level.isClientSide()) {
				stack.update(AvaritiaDataComponents.DESTROYER.get(), false, destroyer -> !destroyer);
			}
			return InteractionResultHolder.success(stack);
		}
		return InteractionResultHolder.pass(stack);
	}}
