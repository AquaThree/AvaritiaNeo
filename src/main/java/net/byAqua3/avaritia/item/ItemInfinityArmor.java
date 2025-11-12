package net.byAqua3.avaritia.item;

import java.util.List;

import com.google.common.collect.Lists;

import net.byAqua3.avaritia.event.AvaritiaClientEvent;
import net.byAqua3.avaritia.loader.AvaritiaConfigs;
import net.byAqua3.avaritia.loader.AvaritiaDataComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ItemInfinityArmor extends ArmorItem {

	public ItemInfinityArmor(Holder<ArmorMaterial> material, Type type, Properties properties) {
		super(material, type, properties.stacksTo(1));
	}

	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		if (this.getEquipmentSlot() == EquipmentSlot.FEET) {
			tooltip.add(Component.literal(""));
			tooltip.add(Component.literal(ChatFormatting.BLUE.toString() + "+" + AvaritiaClientEvent.makeSANIC("SANIC").getString() + ChatFormatting.BLUE.toString() + "% Speed"));
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
		super.inventoryTick(stack, level, entity, slotId, isSelected);
		if (entity instanceof Player) {
			Player player = (Player) entity;
			if (this.getEquipmentSlot() == EquipmentSlot.HEAD && slotId == 39) {
				player.setAirSupply(300);
				player.getFoodData().setFoodLevel(20);
				player.getFoodData().setSaturation(20.0F);
				if (AvaritiaConfigs.nightVision.get()) {
					player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 300, 0, false, false));
				}
			} else if (this.getEquipmentSlot() == EquipmentSlot.CHEST && slotId == 38) {
				player.setArrowCount(0);
				player.getAbilities().mayfly = true;
				player.getAbilities().setFlyingSpeed(0.05F * (float) (2.0F * AvaritiaConfigs.flySpeedValue.get()));
				if (AvaritiaConfigs.clearBadEffect.get()) {
					List<MobEffectInstance> mobEffects = Lists.newArrayList(player.getActiveEffects());
					for (MobEffectInstance mobEffect : mobEffects) {
						if (!mobEffect.getEffect().value().isBeneficial()) {
							player.removeEffect(mobEffect.getEffect());
						}
					}
				}
				stack.update(AvaritiaDataComponents.FLY.get(), false, fly -> player.getAbilities().flying);
			} else if (this.getEquipmentSlot() == EquipmentSlot.LEGS && slotId == 37) {
				if (AvaritiaConfigs.clearFire.get()) {
					player.clearFire();
				}
			} else if (this.getEquipmentSlot() == EquipmentSlot.FEET && slotId == 36) {
				if (AvaritiaConfigs.stepHeight.get()) {
					player.getAttribute(Attributes.STEP_HEIGHT).setBaseValue(1.0625F);
				}

				if (AvaritiaConfigs.speed.get()) {
					boolean flying = player.getAbilities().flying;
					boolean swimming = player.isInWater();
					if (player.onGround() || flying || swimming) {
						boolean sneaking = player.isCrouching();
						float speed = (float) (0.15F * AvaritiaConfigs.speedValue.get()) * (flying ? 1.1F : 1.0F) * (swimming ? 1.2F : 1.0F) * (sneaking ? 0.1F : 1.0F);

						if (player.zza > 0.0F) {
							player.moveRelative(speed, new Vec3(0, 0, 1));
						} else if (player.zza < 0.0F) {
							player.moveRelative(-speed * 0.25F, new Vec3(0, 0, 1));
						}

						if (player.xxa != 0.0F) {
							player.moveRelative(speed * 0.5F * Math.signum(player.xxa), new Vec3(1, 0, 0));
						}
					}
				}
			}
		}
	}
}
