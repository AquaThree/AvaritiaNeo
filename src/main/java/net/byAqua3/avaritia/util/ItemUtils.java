package net.byAqua3.avaritia.util;

import net.byAqua3.avaritia.loader.AvaritiaItems;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;

public class ItemUtils {
	
	public static boolean isInfinityArmor(Player player) {
		if (player.getItemBySlot(EquipmentSlot.HEAD).getItem() == AvaritiaItems.INFINITY_HELMET.get() && player.getItemBySlot(EquipmentSlot.CHEST).getItem() == AvaritiaItems.INFINITY_CHESTPLATE.get() && player.getItemBySlot(EquipmentSlot.LEGS).getItem() == AvaritiaItems.INFINITY_LEGGINGS.get() && player.getItemBySlot(EquipmentSlot.FEET).getItem() == AvaritiaItems.INFINITY_BOOTS.get()) {
			return true;
		}
		return false;
	}}
