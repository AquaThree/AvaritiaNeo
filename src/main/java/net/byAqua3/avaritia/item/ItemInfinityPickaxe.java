package net.byAqua3.avaritia.item;

import java.util.ArrayList;
import java.util.List;

import net.byAqua3.avaritia.loader.AvaritiaDataComponents;
import net.byAqua3.avaritia.loader.AvaritiaToolMaterials;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
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
<<<<<<< HEAD
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

public class ItemInfinityPickaxe extends PickaxeItem {
=======

public class ItemInfinityPickaxe extends PickaxeItem {

	private final UUID uuid = UUID.randomUUID();
>>>>>>> d4d1cb6c05ca8fdefe75682952d4a2a39f983c43

	public ItemInfinityPickaxe(Properties properties) {
		super(AvaritiaToolMaterials.INFINITY, 15, -2.8F, properties);
	}

	@Override
	public boolean hasCustomEntity(ItemStack stack) {
		return true;
	}

	@Override
	public boolean onEntityItemUpdate(ItemStack stack, ItemEntity itemEntity) {
		itemEntity.setInvulnerable(true);
		return super.onEntityItemUpdate(stack, itemEntity);
	}
	
	@Override
<<<<<<< HEAD
	public int getDamage(ItemStack stack) {
		return 0;
=======
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
		HashMultimap<Attribute, AttributeModifier> hashMultimap = HashMultimap
				.create(super.getAttributeModifiers(slot, stack));
		if (!stack.getAllEnchantments().containsKey(Enchantments.BLOCK_FORTUNE)) {
			stack.enchant(Enchantments.BLOCK_FORTUNE, 10);
		}
		if (slot == EquipmentSlot.MAINHAND && stack.hasTag() && stack.getOrCreateTag().getBoolean("hammer")) {
			hashMultimap.put(Attributes.ATTACK_KNOCKBACK,
					new AttributeModifier(uuid, "Tool modifier", 20.0D, AttributeModifier.Operation.ADDITION));
		}
		return hashMultimap;
>>>>>>> d4d1cb6c05ca8fdefe75682952d4a2a39f983c43
	}
	
	@SuppressWarnings("resource")
	@Override
	public boolean isFoil(ItemStack stack) {
		stack.enchant(Minecraft.getInstance().level.registryAccess().holderOrThrow(Enchantments.FORTUNE), 10);
        return false;
    }

	@Override
<<<<<<< HEAD
	public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity livingEntity) {
		int blockRange = (int) Math.round(8.0D);
		
		if (livingEntity instanceof Player) {
			Player player = (Player) livingEntity;

		if (stack.has(AvaritiaDataComponents.HAMMER.get()) && stack.getOrDefault(AvaritiaDataComponents.HAMMER.get(), false)) {
=======
	public boolean onBlockStartBreak(ItemStack stack, BlockPos pos, Player player) {
		Level level = player.level();
		BlockPos blockPos = player.blockPosition();
		int blockRange = (int) Math.round(8.0D);

		if (stack.hasTag() && stack.getOrCreateTag().getBoolean("hammer")) {
>>>>>>> d4d1cb6c05ca8fdefe75682952d4a2a39f983c43
			List<ItemStack> drops = new ArrayList<>();

			for (int x = -blockRange; x <= blockRange; x++) {
				for (int y = -blockRange; y <= blockRange; y++) {
					for (int z = -blockRange; z <= blockRange; z++) {
<<<<<<< HEAD
						BlockPos rangePos = new BlockPos(Mth.floor(pos.getX() + x), Mth.floor(pos.getY() + y),
								Mth.floor(pos.getZ() + z));
=======
						BlockPos rangePos = new BlockPos(Mth.floor(blockPos.getX() + x), Mth.floor(blockPos.getY() + y),
								Mth.floor(blockPos.getZ() + z));
>>>>>>> d4d1cb6c05ca8fdefe75682952d4a2a39f983c43
						BlockState rangeState = level.getBlockState(rangePos);
						Block rangeBlock = rangeState.getBlock();
						if (!rangeState.isAir()) {
							if (!level.isClientSide() && !player.isCreative()) {
<<<<<<< HEAD
								LootParams.Builder lootParams$builder = new LootParams.Builder((ServerLevel) level)
							            .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(rangePos))
							            .withParameter(LootContextParams.TOOL, stack)
							            .withOptionalParameter(LootContextParams.BLOCK_ENTITY, null);
								List<ItemStack> blockDrops = rangeState.getDrops(lootParams$builder);
								
=======
								List<ItemStack> blockDrops = Block.getDrops(rangeState, (ServerLevel) level, blockPos,
										null);
>>>>>>> d4d1cb6c05ca8fdefe75682952d4a2a39f983c43
								if (!blockDrops.isEmpty()) {
									drops.addAll(blockDrops);
								} else {
									ResourceLocation blockKey = BuiltInRegistries.BLOCK.getKey(rangeBlock);
<<<<<<< HEAD
									Item blockItem = BuiltInRegistries.ITEM.getValue(blockKey);
=======
									Item blockItem = BuiltInRegistries.ITEM.get(blockKey);
>>>>>>> d4d1cb6c05ca8fdefe75682952d4a2a39f983c43
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
<<<<<<< HEAD
				ItemEntity itemEntity = new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(),
=======
				ItemEntity itemEntity = new ItemEntity(level, blockPos.getX(), blockPos.getY(), blockPos.getZ(),
>>>>>>> d4d1cb6c05ca8fdefe75682952d4a2a39f983c43
						ItemMatterCluster.makeCluster(drops));
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
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		
		if (player.isShiftKeyDown()) {
			if (!level.isClientSide()) {
				stack.update(AvaritiaDataComponents.HAMMER.get(), false, hammer -> !hammer);
			}
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}

}
