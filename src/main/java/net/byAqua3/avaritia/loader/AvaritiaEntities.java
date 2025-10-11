package net.byAqua3.avaritia.loader;

import net.byAqua3.avaritia.Avaritia;
import net.byAqua3.avaritia.entity.EntityEndestPearl;
import net.byAqua3.avaritia.entity.EntityGapingVoid;
import net.byAqua3.avaritia.entity.EntityInfinityArrow;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AvaritiaEntities {

	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, Avaritia.MODID);

	public static final DeferredHolder<EntityType<?>, EntityType<EntityInfinityArrow>> INFINITY_ARROW = ENTITY_TYPES.register("infinity_arrow", () -> EntityType.Builder.<EntityInfinityArrow>of(EntityInfinityArrow::new, MobCategory.MISC).clientTrackingRange(4).updateInterval(20).sized(0.5F, 0.5F).build(ResourceLocation.tryBuild(Avaritia.MODID, "infinity_arrow").toString()));
	public static final DeferredHolder<EntityType<?>, EntityType<EntityEndestPearl>> ENDEST_PEARL = ENTITY_TYPES.register("endest_pearl", () -> EntityType.Builder.<EntityEndestPearl>of(EntityEndestPearl::new, MobCategory.MISC).clientTrackingRange(4).updateInterval(10).sized(0.25F, 0.25F).build(ResourceLocation.tryBuild(Avaritia.MODID, "endest_pearl").toString()));
	public static final DeferredHolder<EntityType<?>, EntityType<EntityGapingVoid>> GAPING_VOID = ENTITY_TYPES.register("gaping_void", () -> EntityType.Builder.<EntityGapingVoid>of(EntityGapingVoid::new, MobCategory.MISC).clientTrackingRange(16).updateInterval(10).sized(0.1F, 0.1F).build(ResourceLocation.tryBuild(Avaritia.MODID, "gaping_void").toString()));

	public static void registerEntities(IEventBus modEventBus) {
		ENTITY_TYPES.register(modEventBus);
	}
}
