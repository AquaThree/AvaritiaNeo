package net.byAqua3.avaritia.loader;

import java.util.EnumMap;
import java.util.List;

import net.byAqua3.avaritia.Avaritia;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AvaritiaArmorMaterials {
	
	public static final DeferredRegister<ArmorMaterial> ARMOR_MATERIALS = DeferredRegister.create(BuiltInRegistries.ARMOR_MATERIAL, Avaritia.MODID);
	
	public static final DeferredHolder<ArmorMaterial, ArmorMaterial> INFINITY = ARMOR_MATERIALS.register("infinity", () -> new ArmorMaterial(Util.make(new EnumMap<>(ArmorItem.Type.class), enumMap -> {
		enumMap.put(ArmorItem.Type.BOOTS, 6);
        enumMap.put(ArmorItem.Type.LEGGINGS, 12);
        enumMap.put(ArmorItem.Type.CHESTPLATE, 16);
        enumMap.put(ArmorItem.Type.HELMET, 6);
    }), 0, SoundEvents.ARMOR_EQUIP_DIAMOND, () -> Ingredient.EMPTY, List.of(new ArmorMaterial.Layer(ResourceLocation.tryBuild(Avaritia.MODID, "infinity"))), 1.0F, 10.0F));
	
	public static void registerArmorMaterials(IEventBus modEventBus) {
		ARMOR_MATERIALS.register(modEventBus);
	}}
