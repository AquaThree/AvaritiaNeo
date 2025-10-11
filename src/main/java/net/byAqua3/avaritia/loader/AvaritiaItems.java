package net.byAqua3.avaritia.loader;

import net.byAqua3.avaritia.Avaritia;
import net.byAqua3.avaritia.item.ItemCrystalMatrixIngot;
import net.byAqua3.avaritia.item.ItemEndestPearl;
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
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AvaritiaItems {

	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, Avaritia.MODID);

	public static final Rarity COSMIC_RARITY = AvaritiaEnumParams.COSMIC_RARITY_ENUM_PROXY.getValue();

	public static final DeferredHolder<Item, Item> INFINITY_SWORD = ITEMS.register("infinity_sword", () -> new ItemInfinitySword(new Item.Properties().rarity(COSMIC_RARITY)));
	public static final DeferredHolder<Item, Item> SKULLFIRE_SWORD = ITEMS.register("skullfire_sword", () -> new ItemSkullFireSword(new Item.Properties().rarity(Rarity.EPIC)));
	public static final DeferredHolder<Item, Item> INFINITY_AXE = ITEMS.register("infinity_axe", () -> new ItemInfinityAxe(new Item.Properties().rarity(COSMIC_RARITY)));
	public static final DeferredHolder<Item, Item> INFINITY_PICKAXE = ITEMS.register("infinity_pickaxe", () -> new ItemInfinityPickaxe(new Item.Properties().rarity(COSMIC_RARITY)));
	public static final DeferredHolder<Item, Item> INFINITY_SHOVEL = ITEMS.register("infinity_shovel", () -> new ItemInfinityShovel(new Item.Properties().rarity(COSMIC_RARITY)));
	public static final DeferredHolder<Item, Item> INFINITY_HOE = ITEMS.register("infinity_hoe", () -> new ItemInfinityHoe(new Item.Properties().rarity(COSMIC_RARITY)));
	public static final DeferredHolder<Item, Item> INFINITY_BOW = ITEMS.register("infinity_bow", () -> new ItemInfinityBow(new Item.Properties().rarity(COSMIC_RARITY)));
	public static final DeferredHolder<Item, Item> INFINITY_HELMET = ITEMS.register("infinity_helmet", () -> new ItemInfinityArmor(AvaritiaArmorMaterials.INFINITY, ArmorItem.Type.HELMET, new Item.Properties().rarity(COSMIC_RARITY)));
	public static final DeferredHolder<Item, Item> INFINITY_CHESTPLATE = ITEMS.register("infinity_chestplate", () -> new ItemInfinityArmor(AvaritiaArmorMaterials.INFINITY, ArmorItem.Type.CHESTPLATE, new Item.Properties().rarity(COSMIC_RARITY)));
	public static final DeferredHolder<Item, Item> INFINITY_LEGGINGS = ITEMS.register("infinity_leggings", () -> new ItemInfinityArmor(AvaritiaArmorMaterials.INFINITY, ArmorItem.Type.LEGGINGS, new Item.Properties().rarity(COSMIC_RARITY)));
	public static final DeferredHolder<Item, Item> INFINITY_BOOTS = ITEMS.register("infinity_boots", () -> new ItemInfinityArmor(AvaritiaArmorMaterials.INFINITY, ArmorItem.Type.BOOTS, new Item.Properties().rarity(COSMIC_RARITY)));

	public static final DeferredHolder<Item, Item> RECORD_FRAGMENT = ITEMS.register("record_fragment", () -> new ItemRecordFragment(new Item.Properties().rarity(COSMIC_RARITY)));
	public static final DeferredHolder<Item, Item> DIAMOND_LATTICE = ITEMS.register("diamond_lattice", () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));
	public static final DeferredHolder<Item, Item> CRYSTAL_MATRIX_INGOT = ITEMS.register("crystal_matrix_ingot", () -> new ItemCrystalMatrixIngot(new Item.Properties().rarity(Rarity.RARE)));
	public static final DeferredHolder<Item, Item> NEUTRON_PILE = ITEMS.register("neutron_pile", () -> new ItemNeutronPile(new Item.Properties().rarity(Rarity.UNCOMMON)));
	public static final DeferredHolder<Item, Item> NEUTRON_NUGGET = ITEMS.register("neutron_nugget", () -> new ItemNeutronNugget(new Item.Properties().rarity(Rarity.UNCOMMON)));
	public static final DeferredHolder<Item, Item> NETRONIUM_INGOT = ITEMS.register("neutronium_ingot", () -> new ItemNeutroniumIngot(new Item.Properties().rarity(Rarity.RARE)));
	public static final DeferredHolder<Item, Item> INFINITY_CATALYST = ITEMS.register("infinity_catalyst", () -> new ItemInfinityCatalyst(new Item.Properties().rarity(Rarity.EPIC)));
	public static final DeferredHolder<Item, Item> INFINITY_INGOT = ITEMS.register("infinity_ingot", () -> new ItemInfinityIngot(new Item.Properties().rarity(COSMIC_RARITY)));

	public static final DeferredHolder<Item, Item> ENDEST_PEARL = ITEMS.register("endest_pearl", () -> new ItemEndestPearl(new Item.Properties().rarity(Rarity.RARE)));
	public static final DeferredHolder<Item, Item> MATTER_CLUSTER = ITEMS.register("matter_cluster", () -> new ItemMatterCluster(new Item.Properties().rarity(COSMIC_RARITY)));

	public static final DeferredHolder<Item, Item> COPPER_SINGULARITY = ITEMS.register("copper_singularity", () -> new ItemSingularity(new Item.Properties().rarity(Rarity.UNCOMMON), 8999194, 14971392));
	public static final DeferredHolder<Item, Item> IROM_SINGULARITY = ITEMS.register("iron_singularity", () -> new ItemSingularity(new Item.Properties().rarity(Rarity.UNCOMMON), 8355711, 15132648));
	public static final DeferredHolder<Item, Item> GOLD_SINGULARITY = ITEMS.register("gold_singularity", () -> new ItemSingularity(new Item.Properties().rarity(Rarity.UNCOMMON), 14393875, 15265571));
	public static final DeferredHolder<Item, Item> LAPIS_SINGULARITY = ITEMS.register("lapis_singularity", () -> new ItemSingularity(new Item.Properties().rarity(Rarity.UNCOMMON), 2247599, 5931746));
	public static final DeferredHolder<Item, Item> REDSTONE_SINGULARITY = ITEMS.register("redstone_singularity", () -> new ItemSingularity(new Item.Properties().rarity(Rarity.UNCOMMON), 9437184, 14614528));
	public static final DeferredHolder<Item, Item> QUARTZ_SINGULARITY = ITEMS.register("quartz_singularity", () -> new ItemSingularity(new Item.Properties().rarity(Rarity.UNCOMMON), 9733757, 16777215));
	public static final DeferredHolder<Item, Item> DIAMOND_SINGULARITY = ITEMS.register("diamond_singularity", () -> new ItemSingularity(new Item.Properties().rarity(Rarity.UNCOMMON), 9424329, 4566181));
	public static final DeferredHolder<Item, Item> EMERALD_SINGULARITY = ITEMS.register("emerald_singularity", () -> new ItemSingularity(new Item.Properties().rarity(Rarity.UNCOMMON), 9228656, 6078004));
	public static final DeferredHolder<Item, Item> AMETHYST_SINGULARITY = ITEMS.register("amethyst_singularity", () -> new ItemSingularity(new Item.Properties().rarity(Rarity.UNCOMMON), 5519754, 11767539));
	public static final DeferredHolder<Item, Item> NETHERITE_SINGULARITY = ITEMS.register("netherite_singularity", () -> new ItemSingularity(new Item.Properties().rarity(Rarity.UNCOMMON), 3221802, 6637376));
	public static final DeferredHolder<Item, Item> JSON_SINGULARITY = ITEMS.register("json_singularity", () -> new ItemJsonSingularity(new Item.Properties().rarity(Rarity.UNCOMMON)));
	public static final DeferredHolder<Item, Item> INFINITY_SINGULARITY = ITEMS.register("infinity_singularity", () -> new ItemInfinitySingularity(new Item.Properties().rarity(Rarity.EPIC)));

	public static final DeferredHolder<Item, Item> ULTIMATE_STEW = ITEMS.register("ultimate_stew", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().saturationModifier(20.0F).nutrition(20).effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 5 * 60 * 20, 1), 1).alwaysEdible().build())));
	public static final DeferredHolder<Item, Item> COSMIC_MEATBALLS = ITEMS.register("cosmic_meatballs", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().saturationModifier(20.0F).nutrition(20).effect(() -> new MobEffectInstance(MobEffects.DAMAGE_BOOST, 5 * 60 * 20, 1), 1).alwaysEdible().build())));

	public static void registerItems(IEventBus modEventBus) {
		ITEMS.register(modEventBus);
	}

	public static void initItemProperties() {
		ItemProperties.register(INFINITY_BOW.get(), ResourceLocation.tryBuild(Avaritia.MODID, "pull"), (stack, level, entity, i) -> (entity == null) ? 0.0F : ((entity.getUseItem() != stack) ? 0.0F : ((stack.getUseDuration(entity) - entity.getUseItemRemainingTicks()) / ItemInfinityBow.DRAW_TIME)));
		ItemProperties.register(INFINITY_BOW.get(), ResourceLocation.tryBuild(Avaritia.MODID, "pulling"), (stack, level, entity, i) -> (entity != null && entity.isUsingItem() && entity.getUseItem() == stack) ? 1.0F : 0.0F);
		ItemProperties.register(INFINITY_PICKAXE.get(), ResourceLocation.tryBuild(Avaritia.MODID, "hammer"), (stack, level, entity, i) -> (stack.has(AvaritiaDataComponents.HAMMER.get()) && stack.getOrDefault(AvaritiaDataComponents.HAMMER.get(), false)) ? 1.0F : 0.0F);
		ItemProperties.register(INFINITY_SHOVEL.get(), ResourceLocation.tryBuild(Avaritia.MODID, "destroyer"), (stack, level, entity, i) -> (stack.has(AvaritiaDataComponents.DESTROYER.get()) && stack.getOrDefault(AvaritiaDataComponents.DESTROYER.get(), false)) ? 1.0F : 0.0F);
		ItemProperties.register(MATTER_CLUSTER.get(), ResourceLocation.tryBuild(Avaritia.MODID, "full"), (stack, level, entity, i) -> (stack.has(AvaritiaDataComponents.CLUSTER_CONTAINER.get()) && ItemMatterCluster.getClusterCount(ItemMatterCluster.getClusterItems(stack)) == ItemMatterCluster.CAPACITY) ? 1.0F : 0.0F);
	}
}
