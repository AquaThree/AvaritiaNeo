package net.byAqua3.avaritia.item;

import java.util.List;

import com.google.common.collect.Lists;

import net.byAqua3.avaritia.event.AvaritiaClientEvent;
import net.byAqua3.avaritia.loader.AvaritiaConfigs;
import net.byAqua3.avaritia.loader.AvaritiaDataComponents;
import net.byAqua3.avaritia.loader.AvaritiaItems;
import net.byAqua3.avaritia.util.TextComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.level.Level;

public class ItemInfinityArmor extends ArmorItem {

	public ItemInfinityArmor(ArmorMaterial material, ArmorType type, Properties properties) {
		super(material, type, properties.stacksTo(1));
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
	public int getDamage(ItemStack stack) {
		return 0;
	}

	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip,
			TooltipFlag flag) {
		if (this.getEquipmentSlot(stack) == EquipmentSlot.FEET) {
			tooltip.add(TextComponent.getText(""));
			tooltip.add(TextComponent.getText(ChatFormatting.BLUE.toString() + "+" + AvaritiaClientEvent.makeSANIC("SANIC").getString() + ChatFormatting.BLUE.toString() + "% Speed"));
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
		super.inventoryTick(stack, level, entity, slotId, isSelected);
		if (entity instanceof Player) {
			Player player = (Player) entity;
			if (this.getEquipmentSlot(stack) == EquipmentSlot.HEAD && slotId == 39) {
				player.setAirSupply(300);
				player.getFoodData().setFoodLevel(20);
				player.getFoodData().setSaturation(20.0F);
				if (AvaritiaConfigs.nightVision.get()) {
					player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 300, 0, false, false));
				}
			} else if (this.getEquipmentSlot(stack) == EquipmentSlot.CHEST && slotId == 38) {
				player.setArrowCount(0);
				player.getAbilities().mayfly = true;
				player.getAbilities().setFlyingSpeed(0.05F * 2);
				if (AvaritiaConfigs.clearBadEffect.get()) {
					List<MobEffectInstance> mobEffects = Lists.newArrayList(player.getActiveEffects());
					for (MobEffectInstance mobEffect : mobEffects) {
						if (!mobEffect.getEffect().value().isBeneficial()) {
							player.removeEffect(mobEffect.getEffect());
						}
					}
				}
				stack.update(AvaritiaDataComponents.FLY.get(), false, fly -> player.getAbilities().flying);
			} else if (this.getEquipmentSlot(stack) == EquipmentSlot.LEGS && slotId == 37) {
				player.clearFire();
			} else if (this.getEquipmentSlot(stack) == EquipmentSlot.FEET && slotId == 36) {
				player.getAttribute(Attributes.STEP_HEIGHT).setBaseValue(1.0625F);
			}
		}
	}
	@Override
	public EquipmentSlot getEquipmentSlot(ItemStack stack) {
		if(stack.getItem() == AvaritiaItems.INFINITY_HELMET.get()) {
			return EquipmentSlot.HEAD;
		} else if(stack.getItem() == AvaritiaItems.INFINITY_CHESTPLATE.get()) {
			return EquipmentSlot.CHEST;
		} else if(stack.getItem() == AvaritiaItems.INFINITY_LEGGINGS.get()) {
			return EquipmentSlot.LEGS;
		} else if(stack.getItem() == AvaritiaItems.INFINITY_BOOTS.get()) {
			return EquipmentSlot.FEET;
		}
        return null;
    }

}
