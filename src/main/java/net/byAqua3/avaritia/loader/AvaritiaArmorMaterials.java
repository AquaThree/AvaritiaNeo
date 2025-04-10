package net.byAqua3.avaritia.loader;

import java.util.EnumMap;

import net.byAqua3.avaritia.Avaritia;
import net.minecraft.Util;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAssets;

public class AvaritiaArmorMaterials {
	
	public static final ArmorMaterial INFINITY = new ArmorMaterial(9999, Util.make(new EnumMap<>(ArmorType.class), enumMap -> {
		enumMap.put(ArmorType.BOOTS, 6);
        enumMap.put(ArmorType.LEGGINGS, 12);
        enumMap.put(ArmorType.CHESTPLATE, 16);
        enumMap.put(ArmorType.HELMET, 6);
    }), 9999, SoundEvents.ARMOR_EQUIP_DIAMOND, 1.0F, 9999.0F, null, ResourceKey.create(EquipmentAssets.ROOT_ID ,ResourceLocation.tryBuild(Avaritia.MODID, "infinity")));


}
