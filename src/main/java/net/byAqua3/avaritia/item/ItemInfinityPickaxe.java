package net.byAqua3.avaritia.item;

import java.util.ArrayList;
import java.util.List;

import net.byAqua3.avaritia.loader.AvaritiaDataComponents;
import net.byAqua3.avaritia.loader.AvaritiaTiers;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

public class ItemInfinityPickaxe extends PickaxeItem {

	public ItemInfinityPickaxe(Properties properties) {
		super(AvaritiaTiers.INFINITY, properties.attributes(PickaxeItem.createAttributes(AvaritiaTiers.INFINITY, 15, -2.8F)));
	}

	@Override
	public int getDamage(ItemStack stack) {
		return 0;
	}

	@SuppressWarnings("resource")
	@Override
	public boolean isFoil(ItemStack stack) {
		stack.enchant(Minecraft.getInstance().level.registryAccess().holderOrThrow(Enchantments.FORTUNE), 10);
		return false;
	}

	@Override
	public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity livingEntity) {
		int blockRange = (int) Math.round(8.0D);

		if (livingEntity instanceof Player) {
			Player player = (Player) livingEntity;

			if (stack.has(AvaritiaDataComponents.HAMMER.get()) && stack.getOrDefault(AvaritiaDataComponents.HAMMER.get(), false)) {
				List<ItemStack> drops = new ArrayList<>();

				for (int x = -blockRange; x <= blockRange; x++) {
					for (int y = -blockRange; y <= blockRange; y++) {
						for (int z = -blockRange; z <= blockRange; z++) {
							BlockPos rangePos = new BlockPos(Mth.floor(pos.getX() + x), Mth.floor(pos.getY() + y), Mth.floor(pos.getZ() + z));
							BlockState rangeState = level.getBlockState(rangePos);
							Block rangeBlock = rangeState.getBlock();
							if (!rangeState.isAir()) {
								if (!level.isClientSide() && !player.isCreative()) {
									LootParams.Builder lootParams$builder = new LootParams.Builder((ServerLevel) level).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(rangePos)).withParameter(LootContextParams.TOOL, stack).withOptionalParameter(LootContextParams.BLOCK_ENTITY, null);
									List<ItemStack> blockDrops = rangeState.getDrops(lootParams$builder);

									if (!blockDrops.isEmpty()) {
										drops.addAll(blockDrops);
									} else {
										ResourceLocation blockKey = BuiltInRegistries.BLOCK.getKey(rangeBlock);
										Item blockItem = BuiltInRegistries.ITEM.get(blockKey);
										drops.add(new ItemStack(blockItem));
									}
								}

								if (!(rangeBlock instanceof BaseFireBlock)) {
									level.levelEvent(2001, rangePos, Block.getId(rangeState));
								}
								level.setBlockAndUpdate(rangePos, Blocks.AIR.defaultBlockState());
								level.gameEvent(GameEvent.BLOCK_DESTROY, rangePos, GameEvent.Context.of(player, rangeState));
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
	public boolean hurtEnemy(ItemStack stack, LivingEntity entity, LivingEntity attacker) {
		if (entity != null) {
			if (stack.has(AvaritiaDataComponents.HAMMER.get()) && stack.getOrDefault(AvaritiaDataComponents.HAMMER.get(), false)) {
				int i = 10;

				entity.setDeltaMovement(entity.getDeltaMovement().add(-Math.sin(attacker.getYRot() * (float) Math.PI / 180.0F) * i * 0.5F, 2.0D, Math.cos(attacker.getYRot() * (float) Math.PI / 180.0F) * i * 0.5F));
			}
		}
		return super.hurtEnemy(stack, entity, attacker);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);

		if (player.isShiftKeyDown()) {
			if (!level.isClientSide()) {
				stack.update(AvaritiaDataComponents.HAMMER.get(), false, hammer -> !hammer);
			}
			return InteractionResultHolder.success(stack);
		}
		return InteractionResultHolder.pass(stack);
	}}
