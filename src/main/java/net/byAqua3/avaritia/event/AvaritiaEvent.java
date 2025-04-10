package net.byAqua3.avaritia.event;

import java.util.ArrayList;
import java.util.List;

import net.byAqua3.avaritia.damage.InfinityDamageSource;
import net.byAqua3.avaritia.item.ItemInfinityArmor;
import net.byAqua3.avaritia.item.ItemInfinityAxe;
import net.byAqua3.avaritia.item.ItemInfinityBow;
import net.byAqua3.avaritia.item.ItemInfinityHoe;
import net.byAqua3.avaritia.item.ItemInfinityPickaxe;
import net.byAqua3.avaritia.item.ItemInfinityShovel;
import net.byAqua3.avaritia.item.ItemInfinitySword;
import net.byAqua3.avaritia.item.ItemMatterCluster;
import net.byAqua3.avaritia.item.ItemSkullFireSword;
import net.byAqua3.avaritia.loader.AvaritiaConfigs;
import net.byAqua3.avaritia.loader.AvaritiaDataComponents;
import net.byAqua3.avaritia.loader.AvaritiaItems;
import net.byAqua3.avaritia.loader.AvaritiaSingularities;
import net.byAqua3.avaritia.loader.AvaritiaTriggers;
import net.byAqua3.avaritia.network.PacketSingularitySync;
import net.byAqua3.avaritia.singularity.Singularity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.ItemEntityPickupEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

public class AvaritiaEvent {

	public static boolean ClientFly;
	public static boolean ClientMove;

	public static List<String> FlyPlayer = new ArrayList<>();
	public static List<String> MovePlayer = new ArrayList<>();

	public static boolean isInfinityArmor(Player player) {
		if (player.getItemBySlot(EquipmentSlot.HEAD).getItem() == AvaritiaItems.INFINITY_HELMET.get()
				&& player.getItemBySlot(EquipmentSlot.CHEST).getItem() == AvaritiaItems.INFINITY_CHESTPLATE.get()
				&& player.getItemBySlot(EquipmentSlot.LEGS).getItem() == AvaritiaItems.INFINITY_LEGGINGS.get()
				&& player.getItemBySlot(EquipmentSlot.FEET).getItem() == AvaritiaItems.INFINITY_BOOTS.get()) {
			return true;
		}
		return false;
	}

	@SubscribeEvent
	public void onEntityJoinWorld(EntityJoinLevelEvent event) {
		Entity entity = event.getEntity();
		if (entity instanceof ItemEntity) {
			ItemEntity itemEntity = (ItemEntity) entity;
			ItemStack itemStack = itemEntity.getItem();
			Item item = itemStack.getItem();
			if (item instanceof ItemInfinitySword || item instanceof ItemInfinityBow || item instanceof ItemInfinityAxe
					|| item instanceof ItemInfinityPickaxe || item instanceof ItemInfinityShovel
					|| item instanceof ItemInfinityHoe || item instanceof ItemInfinityArmor) {
				itemEntity.setPickUpDelay(8);
			}
		}
	}

	@SuppressWarnings("deprecation")
	@SubscribeEvent
	public void onLivingTick(EntityTickEvent.Post event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			String playerName = player.getDisplayName().getString();

			if (player instanceof ServerPlayer) {
				ServerPlayer serverPlayer = (ServerPlayer) player;
				AvaritiaTriggers.ROOT.get().trigger(serverPlayer);
			}

			if ((ClientFly || FlyPlayer.contains(playerName))
					&& player.getItemBySlot(EquipmentSlot.CHEST).getItem() != AvaritiaItems.INFINITY_CHESTPLATE.get()) {
				if (!player.isCreative()) {
					player.getAbilities().mayfly = false;
					player.getAbilities().flying = false;
				}
				player.getAbilities().setFlyingSpeed(0.05F);
				if (player.level().isClientSide() && ClientFly) {
					ClientFly = false;
				}
				if (FlyPlayer.contains(playerName)) {
					FlyPlayer.remove(playerName);
				}
			}
			if ((ClientMove || MovePlayer.contains(playerName))
					&& player.getItemBySlot(EquipmentSlot.FEET).getItem() != AvaritiaItems.INFINITY_BOOTS.get()) {
				player.getAttribute(Attributes.STEP_HEIGHT).setBaseValue(0.5F);
				if (player.level().isClientSide() && ClientMove) {
					ClientMove = false;
				}
				if (MovePlayer.contains(playerName)) {
					MovePlayer.remove(playerName);
				}
			}

			if (player.getItemBySlot(EquipmentSlot.CHEST).getItem() == AvaritiaItems.INFINITY_CHESTPLATE.get()) {
				if (player.level().isClientSide()) {
					AvaritiaEvent.ClientFly = true;
				}

				if (!AvaritiaEvent.FlyPlayer.contains(playerName)) {
					AvaritiaEvent.FlyPlayer.add(playerName);
				}
			}

			if (player.getItemBySlot(EquipmentSlot.FEET).getItem() == AvaritiaItems.INFINITY_BOOTS.get()) {
				boolean flying = player.getAbilities().flying;
				boolean swimming = player.isInWater();
				if (player.onGround() || flying || swimming) {
					boolean sneaking = player.isCrouching();
					float speed = 0.15f * (flying ? 1.1F : 1.0F) * (swimming ? 1.2F : 1.0F) * (sneaking ? 0.1F : 1.0F);

					if (player.zza > 0.0F) {
						player.moveRelative(speed, new Vec3(0, 0, 1));
					} else if (player.zza < 0.0F) {
						player.moveRelative(-speed * 0.25F, new Vec3(0, 0, 1));
					}

					if (player.xxa != 0.0F) {
						player.moveRelative(speed * 0.5F * Math.signum(player.xxa), new Vec3(1, 0, 0));
					}
				}

				if (player.level().isClientSide()) {
					AvaritiaEvent.ClientMove = true;
				}

				if (!AvaritiaEvent.MovePlayer.contains(playerName)) {
					AvaritiaEvent.MovePlayer.add(playerName);
				}

			}
		}
	}

	@SubscribeEvent
	public void onLivingJump(LivingEvent.LivingJumpEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			if (player.getItemBySlot(EquipmentSlot.FEET).getItem() == AvaritiaItems.INFINITY_BOOTS.get()) {
				player.setDeltaMovement(player.getDeltaMovement().x, player.getDeltaMovement().y + 0.4D,
						player.getDeltaMovement().z);
			}
		}
	}

	@SubscribeEvent
	public void onLivingDrops(LivingDropsEvent event) {
		if (event.getEntity() instanceof AbstractSkeleton) {
			if (event.getSource().getEntity() instanceof Player) {
				Player player = (Player) event.getSource().getEntity();

				if (player.getMainHandItem().getItem() instanceof ItemSkullFireSword) {
					for (ItemEntity itemEntity : event.getDrops()) {
						if (itemEntity.getItem().getItem() == Items.WITHER_SKELETON_SKULL) {
							event.getDrops().remove(itemEntity);
						}
					}
					int randomInt = player.getRandom().nextInt(100) + 1;
					if (randomInt <= AvaritiaConfigs.dropChange.get()) {
						ItemEntity itemEntity = new ItemEntity(event.getEntity().level(), event.getEntity().getX(),
								event.getEntity().getY(), event.getEntity().getZ(),
								new ItemStack(Items.WITHER_SKELETON_SKULL));
						itemEntity.setDefaultPickUpDelay();
						event.getDrops().add(itemEntity);
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onLivingAttack(LivingIncomingDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			DamageSource damageSource = event.getSource();
			Player player = (Player) event.getEntity();
			if (isInfinityArmor(player) && !(damageSource instanceof InfinityDamageSource)) {
				event.setCanceled(true);
				player.hurtTime = 0;
				player.deathTime = 0;
			}
		}
	}

	@SubscribeEvent
	public void onLivingDamage(LivingDamageEvent.Pre event) {
		if (event.getEntity() instanceof Player) {
			DamageSource damageSource = event.getSource();
			Player player = (Player) event.getEntity();
			if (isInfinityArmor(player) && !(damageSource instanceof InfinityDamageSource)) {
				event.setNewDamage(0.0F);
				player.hurtTime = 0;
				player.deathTime = 0;
			}
		}
	}

	@SubscribeEvent
	public void onLivingHurt(LivingDamageEvent.Post event) {
		if (event.getEntity() instanceof Player) {
			DamageSource damageSource = event.getSource();
			Player player = (Player) event.getEntity();
			if (isInfinityArmor(player) && !(damageSource instanceof InfinityDamageSource)) {
				player.hurtTime = 0;
				player.deathTime = 0;
			}
		}
	}

	@SubscribeEvent
	public void onLivingDeath(LivingDeathEvent event) {
		if (event.getEntity() instanceof Player) {
			DamageSource damageSource = event.getSource();
			Player player = (Player) event.getEntity();
			if (isInfinityArmor(player) && !(damageSource instanceof InfinityDamageSource)) {
				event.setCanceled(true);
				player.hurtTime = 0;
				player.deathTime = 0;
				player.setHealth(Math.max(20.0F, player.getMaxHealth()));
			}
		}
	}

	@SubscribeEvent
	public void onPlayerBreakSpeed(PlayerEvent.BreakSpeed event) {
		Player player = event.getEntity();
		ItemStack itemStack = player.getMainHandItem();
		Item item = itemStack.getItem();

		if (item instanceof ItemInfinityAxe) {
			event.setNewSpeed(event.getNewSpeed() * 5.0F);
		} else if (item instanceof ItemInfinityPickaxe) {
			if (itemStack.has(AvaritiaDataComponents.HAMMER.get())
					&& itemStack.getOrDefault(AvaritiaDataComponents.HAMMER.get(), false)) {
				event.setNewSpeed(10.0F);
			} else {
				event.setNewSpeed(Float.MAX_VALUE);
			}
		} else if (item instanceof ItemInfinityShovel) {
			event.setNewSpeed(event.getNewSpeed() * 5.0F);
		} else if (item instanceof ItemInfinityHoe) {
			event.setNewSpeed(event.getNewSpeed() * 5.0F);
		}
	}

	@SubscribeEvent
	public void onPlayerLeftClick(PlayerInteractEvent.LeftClickBlock event) {
		Level level = event.getLevel();
		Player player = event.getEntity();
		BlockPos blockPos = event.getPos();
		BlockState blockState = level.getBlockState(blockPos);
		Block block = blockState.getBlock();
		ItemStack itemStack = event.getItemStack();
		Item item = itemStack.getItem();

		if (item instanceof ItemInfinityAxe || item instanceof ItemInfinityShovel) {
			if (player.isCreative()) {
				item.mineBlock(itemStack, level, blockState, blockPos, player);
			}
		}

		if (item instanceof ItemInfinityPickaxe) {
			if (itemStack.has(AvaritiaDataComponents.HAMMER.get())
					&& itemStack.getOrDefault(AvaritiaDataComponents.HAMMER.get(), false)) {
				if (player.isCreative() || blockState.getDestroySpeed(level, blockPos) <= -1) {
					item.mineBlock(itemStack, level, blockState, blockPos, player);
				}
			} else {
				if (!level.isClientSide() && !player.isCreative()) {
					List<ItemStack> blockDrops = Block.getDrops(blockState, (ServerLevel) level, blockPos, null);
					if (blockDrops.isEmpty()) {
						ResourceLocation blockKey = BuiltInRegistries.BLOCK.getKey(block);
						Item blockItem = BuiltInRegistries.ITEM.getValue(blockKey);
						ItemEntity itemEntity = new ItemEntity(level, blockPos.getX(), blockPos.getY(), blockPos.getZ(),
								new ItemStack(blockItem));
						itemEntity.setDefaultPickUpDelay();
						level.addFreshEntity(itemEntity);
						level.destroyBlock(blockPos, false);
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onLivingEntityUseFinishItem(LivingEntityUseItemEvent.Finish event) {
		LivingEntity livingEntity = event.getEntity();
		ItemStack itemStack = event.getResultStack();
		Item item = itemStack.getItem();

		if (item == AvaritiaItems.ULTIMATE_STEW.get() || item == AvaritiaItems.COSMIC_MEATBALLS.get()) {
			livingEntity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 5 * 60 * 20, 1));
		}
	}

	@SubscribeEvent
	public void onEntityItemPickup(ItemEntityPickupEvent.Post event) {
		Player player = event.getPlayer();
		ItemStack itemStack = event.getOriginalStack();
		Item item = itemStack.getItem();

		if (item instanceof ItemMatterCluster) {
			List<ItemStack> stackItems = ItemMatterCluster.getClusterItems(itemStack);
			for (int i = 0; i < player.getInventory().items.size(); i++) {
				ItemStack matterStack = player.getInventory().items.get(i);
				if (!matterStack.isEmpty() && matterStack.getItem() instanceof ItemMatterCluster) {
					List<ItemStack> matterItems = ItemMatterCluster.getClusterItems(matterStack);
					if (ItemMatterCluster.getClusterCount(matterItems) < ItemMatterCluster.CAPACITY) {
						int count = ItemMatterCluster.CAPACITY - ItemMatterCluster.getClusterCount(matterItems);
						if (count >= ItemMatterCluster.getClusterCount(stackItems)) {
							List<ItemStack> newMatterItems = new ArrayList<>();
							newMatterItems.addAll(matterItems);
							newMatterItems.addAll(stackItems);
							player.getInventory().items.set(i, ItemMatterCluster.makeCluster(newMatterItems));

							for (ItemStack oldMatterStack : player.getInventory().items) {
								if (oldMatterStack.getItem() instanceof ItemMatterCluster) {
									List<ItemStack> oldMatterItems = ItemMatterCluster.getClusterItems(oldMatterStack);
									int oldCount = ItemMatterCluster.getClusterCount(oldMatterItems);
									if (oldCount == 0) {
										oldMatterStack.setCount(0);
									}
								}
							}
						} else if (count > 0) {
							List<ItemStack> newStackItems = new ArrayList<>();
							List<ItemStack> newMatterItems = new ArrayList<>();
							int index = 0;
							int size = 0;

							for (; index < stackItems.size(); index++) {
								ItemStack newStack = stackItems.get(index);
								size += newStack.getCount();
								if (size >= count) {
									break;
								}
							}
							int newCount = size - count;
							ItemStack newStack = stackItems.get(index);
							ItemStack copyStack = newStack.copy();
							copyStack.setCount(copyStack.getCount() - newCount);
							newStack.setCount(newCount);

							newMatterItems.addAll(matterItems);
							newMatterItems.add(copyStack);
							newMatterItems.addAll(stackItems.subList(0, index));

							newStackItems.add(newStack);
							newStackItems.addAll(stackItems.subList(index + 1, stackItems.size() - 1));

							player.getInventory().items.set(i, ItemMatterCluster.makeCluster(newMatterItems));
							// if (player.getInventory().getFreeSlot() != -1) {
							// player.getInventory().add(ItemMatterCluster.makeCluster(newStackItems));
							// } else {
							// ItemEntity newItemEntity = new ItemEntity(player.level(), player.getX(),
							// player.getY(),
							// player.getZ(), ItemMatterCluster.makeCluster(newStackItems));
							// newItemEntity.setDefaultPickUpDelay();
							// level.addFreshEntity(newItemEntity);
							// }
						}
						break;
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onDatapackSync(OnDatapackSyncEvent event) {
		ServerPlayer player = event.getPlayer();
		ResourceManager resourceManager;
		List<Singularity> singularities;

		if (player == null) {
			resourceManager = event.getPlayerList().getServer().getResourceManager();
			singularities = AvaritiaSingularities.loadSingularities(resourceManager);
			PacketDistributor.sendToAllPlayers(new PacketSingularitySync(singularities));
		} else {
			resourceManager = player.getServer().getResourceManager();;
			singularities = AvaritiaSingularities.loadSingularities(resourceManager);
			PacketDistributor.sendToPlayer(player, new PacketSingularitySync(singularities));
		}
	}

}
