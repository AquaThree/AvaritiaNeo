package net.byAqua3.avaritia.loader;

import net.byAqua3.avaritia.Avaritia;
import net.byAqua3.avaritia.trigger.TriggerAvaritiaRoot;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AvaritiaTriggers {
	
	public static final DeferredRegister<CriterionTrigger<?>> TRIGGERS = DeferredRegister.create(BuiltInRegistries.TRIGGER_TYPES, Avaritia.MODID);
	
	public static final DeferredHolder<CriterionTrigger<?>, TriggerAvaritiaRoot> ROOT = TRIGGERS.register("root", TriggerAvaritiaRoot::new);
	
	public static void registerTriggers(IEventBus modEventBus) {
		TRIGGERS.register(modEventBus);
	}}
