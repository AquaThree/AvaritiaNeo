package net.byAqua3.avaritia.event;

import java.util.ArrayList;
import java.util.List;

import net.byAqua3.avaritia.damage.DamageSourceInfinity;
import net.byAqua3.avaritia.item.ItemInfinityAxe;
import net.byAqua3.avaritia.item.ItemInfinityHoe;
import net.byAqua3.avaritia.item.ItemInfinityPickaxe;
import net.byAqua3.avaritia.item.ItemInfinityShovel;
import net.byAqua3.avaritia.item.ItemMatterCluster;
import net.byAqua3.avaritia.item.ItemSkullFireSword;
import net.byAqua3.avaritia.loader.AvaritiaConfigs;
import net.byAqua3.avaritia.loader.AvaritiaDataComponents;
import net.byAqua3.avaritia.loader.AvaritiaItemTags;
import net.byAqua3.avaritia.loader.AvaritiaItems;
import net.byAqua3.avaritia.loader.AvaritiaSingularities;
import net.byAqua3.avaritia.loader.AvaritiaTriggers;
import net.byAqua3.avaritia.network.PacketSingularitySync;
import net.byAqua3.avaritia.singularity.Singularity;
import net.byAqua3.avaritia.util.ItemUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.protocol.game.ClientboundPlayerAbilitiesPacket;
import net.minecraft.network.protocol.game.ClientboundUpdateAttributesPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.server.players.PlayerList;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
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
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.ItemEntityPickupEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

public class AvaritiaEvent {

	public static List<String> FlyPlayer = new ArrayList<>();
	public static List<String> MovePlayer = new ArrayList<>();

	@SubscribeEvent
	public void onEntityJoinWorld(EntityJoinLevelEvent event) {
		Entity entity = event.getEntity();
		
		if (entity instanceof ItemEntity) {
			ItemEntity itemEntity = (ItemEntity) entity;
			ItemStack itemStack = itemEntity.getItem();
			List<TagKey<Item>> tags = itemStack.getTags().toList();

			if (tags.contains(AvaritiaItemTags.IMMORTAL)) {
				itemEntity.setInvulnerable(true);
			}

			if (tags.contains(AvaritiaItemTags.FAST_PICKUP)) {
				itemEntity.setPickUpDelay(8);
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@SubscribeEvent
	public void onServerTick(ServerTickEvent.Post event) {
		MinecraftServer server = event.getServer();
		PlayerList playerList = server.getPlayerList();
		List<ServerPlayer> players = playerList.getPlayers();

		for (ServerPlayer player : players) {
			String playerName = player.getDisplayName().getString();
			ServerLevel serverLevel = (ServerLevel) player.level();
			ServerChunkCache serverChunkCache = serverLevel.getChunkSource();

			if (FlyPlayer.contains(playerName) && player.getItemBySlot(EquipmentSlot.CHEST).getItem() != AvaritiaItems.INFINITY_CHESTPLATE.get()) {
				if (!player.isCreative()) {
					player.getAbilities().mayfly = false;
				}
				player.getAbilities().setFlyingSpeed(0.05F);
				serverChunkCache.broadcastAndSend(player, new ClientboundPlayerAbilitiesPacket(player.getAbilities()));

				FlyPlayer.remove(playerName);
			}
			if (MovePlayer.contains(playerName) && player.getItemBySlot(EquipmentSlot.FEET).getItem() != AvaritiaItems.INFINITY_BOOTS.get()) {
				player.getAttribute(Attributes.STEP_HEIGHT).setBaseValue(0.5F);
				serverChunkCache.broadcastAndSend(player, new ClientboundUpdateAttributesPacket(player.getId(), player.getAttributes().getSyncableAttributes()));

				MovePlayer.remove(playerName);
			}

			if (!AvaritiaEvent.FlyPlayer.contains(playerName) && player.getItemBySlot(EquipmentSlot.CHEST).getItem() == AvaritiaItems.INFINITY_CHESTPLATE.get()) {
				AvaritiaEvent.FlyPlayer.add(playerName);
			}

			if (!AvaritiaEvent.MovePlayer.contains(playerName) && player.getItemBySlot(EquipmentSlot.FEET).getItem() == AvaritiaItems.INFINITY_BOOTS.get()) {
				AvaritiaEvent.MovePlayer.add(playerName);
			}
		}
	}

	@SubscribeEvent
	public void onLivingTick(EntityTickEvent.Post event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();

			if (player instanceof ServerPlayer) {
				ServerPlayer serverPlayer = (ServerPlayer) player;
				AvaritiaTriggers.ROOT.get().trigger(serverPlayer);
			}
		}
	}

	@SubscribeEvent
	public void onLivingJump(LivingEvent.LivingJumpEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			if (player.getItemBySlot(EquipmentSlot.FEET).getItem() == AvaritiaItems.INFINITY_BOOTS.get()) {
				if (AvaritiaConfigs.highJump.get()) {
					player.push(0.0D, 0.4D, 0.0D);
				}
			}
		}
	}

	@SubscribeEvent
	public void onLivingDrops(LivingDropsEvent event) {
		if (event.getEntity() instanceof AbstractSkeleton) {
			if (event.getSource().getEntity() instanceof Player) {
				Player player = (Player) event.getSource().getEntity();
				ItemStack itemStack = player.getMainHandItem();

				if (!itemStack.isEmpty() && itemStack.getItem() instanceof ItemSkullFireSword) {
					for (ItemEntity itemEntity : event.getDrops()) {
						if (itemEntity.getItem().getItem() == Items.WITHER_SKELETON_SKULL) {
							event.getDrops().remove(itemEntity);
						}
					}
					int randomInt = player.getRandom().nextInt(100);
					if (randomInt < AvaritiaConfigs.dropChange.get()) {
						ItemEntity itemEntity = new ItemEntity(event.getEntity().level(), event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), new ItemStack(Items.WITHER_SKELETON_SKULL));
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
			if (ItemUtils.isInfinityArmor(player) && !(damageSource instanceof DamageSourceInfinity)) {
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
			if (ItemUtils.isInfinityArmor(player) && !(damageSource instanceof DamageSourceInfinity)) {
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
			if (ItemUtils.isInfinityArmor(player) && !(damageSource instanceof DamageSourceInfinity)) {
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
			if (ItemUtils.isInfinityArmor(player) && !(damageSource instanceof DamageSourceInfinity)) {
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
			if (itemStack.has(AvaritiaDataComponents.HAMMER.get()) && itemStack.getOrDefault(AvaritiaDataComponents.HAMMER.get(), false)) {
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
			if (itemStack.has(AvaritiaDataComponents.HAMMER.get()) && itemStack.getOrDefault(AvaritiaDataComponents.HAMMER.get(), false)) {
				if (player.isCreative() || blockState.getDestroySpeed(level, blockPos) <= -1) {
					item.mineBlock(itemStack, level, blockState, blockPos, player);
				}
			} else {
				if (!level.isClientSide() && !player.isCreative()) {
					List<ItemStack> blockDrops = Block.getDrops(blockState, (ServerLevel) level, blockPos, null);
					if (blockDrops.isEmpty()) {
						ResourceLocation blockKey = BuiltInRegistries.BLOCK.getKey(block);
						Item blockItem = BuiltInRegistries.ITEM.get(blockKey);
						ItemEntity itemEntity = new ItemEntity(level, blockPos.getX(), blockPos.getY(), blockPos.getZ(), new ItemStack(blockItem));
						itemEntity.setDefaultPickUpDelay();
						level.addFreshEntity(itemEntity);
						level.destroyBlock(blockPos, false);
					}
				}
			}
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
						}
						break;
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onAddReloadListener(AddReloadListenerEvent event) {
		event.addListener(new ResourceManagerReloadListener() {
			@Override
			public void onResourceManagerReload(ResourceManager resourceManager) {
				List<Singularity> singularities = AvaritiaSingularities.loadSingularities(resourceManager);
				AvaritiaSingularities.getInstance().getSingularities().clear();
				AvaritiaSingularities.getInstance().getSingularities().addAll(singularities);
			}
		});
	}

	@SubscribeEvent
	public void onDatapackSync(OnDatapackSyncEvent event) {
		ServerPlayer player = event.getPlayer();
		List<Singularity> singularities = AvaritiaSingularities.getInstance().getSingularities();

		if (player == null) {
			PacketDistributor.sendToAllPlayers(new PacketSingularitySync(singularities));
		} else {
			PacketDistributor.sendToPlayer(player, new PacketSingularitySync(singularities));
		}
	}
}
