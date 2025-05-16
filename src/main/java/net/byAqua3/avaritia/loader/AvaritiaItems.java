package net.byAqua3.avaritia.loader;

import net.byAqua3.avaritia.Avaritia;
import net.byAqua3.avaritia.item.ItemCrystalMatrixIngot;
import net.byAqua3.avaritia.item.ItemEndestPearl;
import net.byAqua3.avaritia.item.ItemGapingVoid;
import net.byAqua3.avaritia.item.ItemInfinityArmor;
import net.byAqua3.avaritia.item.ItemInfinityAxe;
import net.byAqua3.avaritia.item.ItemInfinityBow;
import net.byAqua3.avaritia.item.ItemInfinityCatalyst;
import net.byAqua3.avaritia.item.ItemInfinityHoe;
import net.byAqua3.avaritia.item.ItemInfinityIngot;
import net.byAqua3.avaritia.item.ItemInfinityPickaxe;
import net.byAqua3.avaritia.item.ItemInfinityShovel;
import net.byAqua3.avaritia.item.ItemInfinitySingularity;
import net.byAqua3.avaritia.item.ItemInfinitySword;
import net.byAqua3.avaritia.item.ItemJsonSingularity;
import net.byAqua3.avaritia.item.ItemMatterCluster;
import net.byAqua3.avaritia.item.ItemNeutronNugget;
import net.byAqua3.avaritia.item.ItemNeutronPile;
import net.byAqua3.avaritia.item.ItemNeutroniumIngot;
import net.byAqua3.avaritia.item.ItemRecordFragment;
import net.byAqua3.avaritia.item.ItemSingularity;
import net.byAqua3.avaritia.item.ItemSkullFireSword;
import net.byAqua3.avaritia.property.ItemPropertyDestroyer;
import net.byAqua3.avaritia.property.ItemPropertyFull;
import net.byAqua3.avaritia.property.ItemPropertyHammer;
import net.byAqua3.avaritia.property.ItemPropertyPull;
import net.byAqua3.avaritia.property.ItemPropertyPulling;
import net.byAqua3.avaritia.render.special.SpecialModelWrapper;
import net.byAqua3.avaritia.render.special.SpecialRenderCosmic;
import net.byAqua3.avaritia.render.special.SpecialRenderHalo;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.equipment.ArmorType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterConditionalItemModelPropertyEvent;
import net.neoforged.neoforge.client.event.RegisterItemModelsEvent;
import net.neoforged.neoforge.client.event.RegisterSpecialModelRendererEvent;
import net.neoforged.neoforge.client.event.RegisterRangeSelectItemModelPropertyEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AvaritiaItems {
	
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, Avaritia.MODID);
	
	public static final Rarity COSMIC_RARITY = AvaritiaEnumParams.COSMIC_RARITY_ENUM_PROXY.getValue();
	
	public static final DeferredHolder<Item, Item> INFINITY_SWORD = ITEMS.register("infinity_sword", id -> new ItemInfinitySword(new Item.Properties().rarity(COSMIC_RARITY).setId(ResourceKey.create(Registries.ITEM, id))));
	public static final DeferredHolder<Item, Item> SKULLFIRE_SWORD = ITEMS.register("skullfire_sword", id -> new ItemSkullFireSword(new Item.Properties().rarity(Rarity.EPIC).setId(ResourceKey.create(Registries.ITEM, id))));
	public static final DeferredHolder<Item, Item> INFINITY_AXE = ITEMS.register("infinity_axe", id -> new ItemInfinityAxe(new Item.Properties().rarity(COSMIC_RARITY).setId(ResourceKey.create(Registries.ITEM, id))));
	public static final DeferredHolder<Item, Item> INFINITY_PICKAXE = ITEMS.register("infinity_pickaxe", id -> new ItemInfinityPickaxe(new Item.Properties().rarity(COSMIC_RARITY).setId(ResourceKey.create(Registries.ITEM, id))));
	public static final DeferredHolder<Item, Item> INFINITY_SHOVEL = ITEMS.register("infinity_shovel", id -> new ItemInfinityShovel(new Item.Properties().rarity(COSMIC_RARITY).setId(ResourceKey.create(Registries.ITEM, id))));
	public static final DeferredHolder<Item, Item> INFINITY_HOE = ITEMS.register("infinity_hoe", id -> new ItemInfinityHoe(new Item.Properties().rarity(COSMIC_RARITY).setId(ResourceKey.create(Registries.ITEM, id))));
	public static final DeferredHolder<Item, Item> INFINITY_BOW = ITEMS.register("infinity_bow", id -> new ItemInfinityBow(new Item.Properties().rarity(COSMIC_RARITY).setId(ResourceKey.create(Registries.ITEM, id))));
	public static final DeferredHolder<Item, Item> INFINITY_HELMET = ITEMS.register("infinity_helmet", id -> new ItemInfinityArmor(AvaritiaArmorMaterials.INFINITY, ArmorType.HELMET, new Item.Properties().rarity(COSMIC_RARITY).setId(ResourceKey.create(Registries.ITEM, id))));
	public static final DeferredHolder<Item, Item> INFINITY_CHESTPLATE = ITEMS.register("infinity_chestplate", id -> new ItemInfinityArmor(AvaritiaArmorMaterials.INFINITY, ArmorType.CHESTPLATE, new Item.Properties().rarity(COSMIC_RARITY).setId(ResourceKey.create(Registries.ITEM, id))));
	public static final DeferredHolder<Item, Item> INFINITY_LEGGINGS = ITEMS.register("infinity_leggings", id -> new ItemInfinityArmor(AvaritiaArmorMaterials.INFINITY, ArmorType.LEGGINGS, new Item.Properties().rarity(COSMIC_RARITY).setId(ResourceKey.create(Registries.ITEM, id))));
	public static final DeferredHolder<Item, Item> INFINITY_BOOTS = ITEMS.register("infinity_boots", id -> new ItemInfinityArmor(AvaritiaArmorMaterials.INFINITY, ArmorType.BOOTS, new Item.Properties().rarity(COSMIC_RARITY).setId(ResourceKey.create(Registries.ITEM, id))));
	
	public static final DeferredHolder<Item, Item> RECORD_FRAGMENT = ITEMS.register("record_fragment", id -> new ItemRecordFragment(new Item.Properties().rarity(COSMIC_RARITY).setId(ResourceKey.create(Registries.ITEM, id))));
	public static final DeferredHolder<Item, Item> DIAMOND_LATTICE = ITEMS.register("diamond_lattice", id -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON).setId(ResourceKey.create(Registries.ITEM, id))));
	public static final DeferredHolder<Item, Item> CRYSTAL_MATRIX_INGOT = ITEMS.register("crystal_matrix_ingot", id -> new ItemCrystalMatrixIngot(new Item.Properties().rarity(Rarity.RARE).setId(ResourceKey.create(Registries.ITEM, id))));
	public static final DeferredHolder<Item, Item> NEUTRON_PILE = ITEMS.register("neutron_pile", id -> new ItemNeutronPile(new Item.Properties().rarity(Rarity.UNCOMMON).setId(ResourceKey.create(Registries.ITEM, id))));
	public static final DeferredHolder<Item, Item> NEUTRON_NUGGET = ITEMS.register("neutron_nugget", id -> new ItemNeutronNugget(new Item.Properties().rarity(Rarity.UNCOMMON).setId(ResourceKey.create(Registries.ITEM, id))));
	public static final DeferredHolder<Item, Item> NETRONIUM_INGOT = ITEMS.register("neutronium_ingot", id -> new ItemNeutroniumIngot(new Item.Properties().rarity(Rarity.RARE).setId(ResourceKey.create(Registries.ITEM, id))));
	public static final DeferredHolder<Item, Item> INFINITY_CATALYST = ITEMS.register("infinity_catalyst", id -> new ItemInfinityCatalyst(new Item.Properties().rarity(Rarity.EPIC).setId(ResourceKey.create(Registries.ITEM, id))));
	public static final DeferredHolder<Item, Item> INFINITY_INGOT = ITEMS.register("infinity_ingot", id -> new ItemInfinityIngot(new Item.Properties().rarity(COSMIC_RARITY).setId(ResourceKey.create(Registries.ITEM, id))));
	
	public static final DeferredHolder<Item, Item> ENDEST_PEARL = ITEMS.register("endest_pearl", id -> new ItemEndestPearl(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id))));
	public static final DeferredHolder<Item, Item> GAPING_VOID = ITEMS.register("gaping_void", id -> new ItemGapingVoid(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id))));
	public static final DeferredHolder<Item, Item> MATTER_CLUSTER = ITEMS.register("matter_cluster", id -> new ItemMatterCluster(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id))));
	
	public static final DeferredHolder<Item, Item> COPPER_SINGULARITY = ITEMS.register("copper_singularity", id -> new ItemSingularity(new Item.Properties().rarity(Rarity.UNCOMMON).setId(ResourceKey.create(Registries.ITEM, id)), 8999194, 14971392));
	public static final DeferredHolder<Item, Item> IROM_SINGULARITY = ITEMS.register("iron_singularity", id -> new ItemSingularity(new Item.Properties().rarity(Rarity.UNCOMMON).setId(ResourceKey.create(Registries.ITEM, id)), 8355711, 15132648));
	public static final DeferredHolder<Item, Item> GOLD_SINGULARITY = ITEMS.register("gold_singularity", id -> new ItemSingularity(new Item.Properties().rarity(Rarity.UNCOMMON).setId(ResourceKey.create(Registries.ITEM, id)), 14393875, 15265571));
	public static final DeferredHolder<Item, Item> LAPIS_SINGULARITY = ITEMS.register("lapis_singularity", id -> new ItemSingularity(new Item.Properties().rarity(Rarity.UNCOMMON).setId(ResourceKey.create(Registries.ITEM, id)), 2247599, 5931746));
	public static final DeferredHolder<Item, Item> REDSTONE_SINGULARITY = ITEMS.register("redstone_singularity", id -> new ItemSingularity(new Item.Properties().rarity(Rarity.UNCOMMON).setId(ResourceKey.create(Registries.ITEM, id)), 9437184, 14614528));
	public static final DeferredHolder<Item, Item> QUARTZ_SINGULARITY = ITEMS.register("quartz_singularity", id -> new ItemSingularity(new Item.Properties().rarity(Rarity.UNCOMMON).setId(ResourceKey.create(Registries.ITEM, id)), 9733757, 16777215));
	public static final DeferredHolder<Item, Item> DIAMOND_SINGULARITY = ITEMS.register("diamond_singularity", id -> new ItemSingularity(new Item.Properties().rarity(Rarity.UNCOMMON).setId(ResourceKey.create(Registries.ITEM, id)), 9424329, 4566181));
	public static final DeferredHolder<Item, Item> EMERALD_SINGULARITY = ITEMS.register("emerald_singularity", id -> new ItemSingularity(new Item.Properties().rarity(Rarity.UNCOMMON).setId(ResourceKey.create(Registries.ITEM, id)), 9228656, 6078004));
	public static final DeferredHolder<Item, Item> AMETHYST_SINGULARITY = ITEMS.register("amethyst_singularity", id -> new ItemSingularity(new Item.Properties().rarity(Rarity.UNCOMMON).setId(ResourceKey.create(Registries.ITEM, id)), 5519754, 11767539));
	public static final DeferredHolder<Item, Item> NETHERITE_SINGULARITY = ITEMS.register("netherite_singularity", id -> new ItemSingularity(new Item.Properties().rarity(Rarity.UNCOMMON).setId(ResourceKey.create(Registries.ITEM, id)), 3221802, 6637376));
	public static final DeferredHolder<Item, Item> JSON_SINGULARITY = ITEMS.register("json_singularity", id -> new ItemJsonSingularity(new Item.Properties().rarity(Rarity.UNCOMMON).setId(ResourceKey.create(Registries.ITEM, id))));
	public static final DeferredHolder<Item, Item> INFINITY_SINGULARITY = ITEMS.register("infinity_singularity", id -> new ItemInfinitySingularity(new Item.Properties().rarity(Rarity.EPIC).setId(ResourceKey.create(Registries.ITEM, id))));
	
	public static final DeferredHolder<Item, Item> ULTIMATE_STEW = ITEMS.register("ultimate_stew", id -> new Item(new Item.Properties().food(new FoodProperties.Builder().saturationModifier(20.0F).nutrition(20).alwaysEdible().build()).setId(ResourceKey.create(Registries.ITEM, id))));
	public static final DeferredHolder<Item, Item> COSMIC_MEATBALLS = ITEMS.register("cosmic_meatballs", id -> new Item(new Item.Properties().food(new FoodProperties.Builder().saturationModifier(20.0F).nutrition(20).alwaysEdible().build()).setId(ResourceKey.create(Registries.ITEM, id))));
	
	public static void registerItems(IEventBus modEventBus) {
		ITEMS.register(modEventBus);
		modEventBus.addListener(AvaritiaItems::onRegisterRangeSelectItemModelProperty);
		modEventBus.addListener(AvaritiaItems::onRegisterConditionalItemModelProperty);
		modEventBus.addListener(AvaritiaItems::onRegisterItemModels);
		modEventBus.addListener(AvaritiaItems::onRegisterSpecialModelRenderer);
	}
	
	@SubscribeEvent
	public static void onRegisterRangeSelectItemModelProperty(RegisterRangeSelectItemModelPropertyEvent event) {
		event.register(ResourceLocation.tryBuild(Avaritia.MODID, "pull"), ItemPropertyPull.MAP_CODEC);
	}
	
	@SubscribeEvent
	public static void onRegisterConditionalItemModelProperty(RegisterConditionalItemModelPropertyEvent event) {
		event.register(ResourceLocation.tryBuild(Avaritia.MODID, "pulling"), ItemPropertyPulling.MAP_CODEC);
		event.register(ResourceLocation.tryBuild(Avaritia.MODID, "hammer"), ItemPropertyHammer.MAP_CODEC);
		event.register(ResourceLocation.tryBuild(Avaritia.MODID, "destroyer"), ItemPropertyDestroyer.MAP_CODEC);
		event.register(ResourceLocation.tryBuild(Avaritia.MODID, "full"), ItemPropertyFull.MAP_CODEC);
	}
	
	@SubscribeEvent
	public static void onRegisterItemModels(RegisterItemModelsEvent event) {
		event.register(ResourceLocation.tryBuild(Avaritia.MODID, "special"), SpecialModelWrapper.Unbaked.MAP_CODEC);
	}
	
	@SubscribeEvent
	public static void onRegisterSpecialModelRenderer(RegisterSpecialModelRendererEvent event) {
		event.register(ResourceLocation.tryBuild(Avaritia.MODID, "cosmic"), SpecialRenderCosmic.Unbaked.MAP_CODEC);
	    event.register(ResourceLocation.tryBuild(Avaritia.MODID, "halo"), SpecialRenderHalo.Unbaked.MAP_CODEC);
	}

}
