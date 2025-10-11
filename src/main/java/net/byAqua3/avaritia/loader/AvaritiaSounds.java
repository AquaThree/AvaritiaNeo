package net.byAqua3.avaritia.loader;

import net.byAqua3.avaritia.Avaritia;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AvaritiaSounds {
	
	public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, Avaritia.MODID);
	
	public static final DeferredHolder<SoundEvent, SoundEvent> GAPING_VOID = SOUNDS.register("gaping_void", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.tryBuild(Avaritia.MODID, "gaping_void")));
	
	public static void registerSounds(IEventBus modEventBus) {
		SOUNDS.register(modEventBus);
	}}
