package net.byAqua3.avaritia.loader;

import com.mojang.serialization.Codec;

import net.byAqua3.avaritia.Avaritia;
import net.byAqua3.avaritia.component.ClusterContainerContents;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AvaritiaDataComponents {

	public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENTS = DeferredRegister.create(BuiltInRegistries.DATA_COMPONENT_TYPE, Avaritia.MODID);

	public static final DeferredHolder<DataComponentType<?>, DataComponentType<ClusterContainerContents>> CLUSTER_CONTAINER = DATA_COMPONENTS.register("cluster_container", () -> DataComponentType.<ClusterContainerContents>builder().persistent(ClusterContainerContents.CODEC).networkSynchronized(ClusterContainerContents.STREAM_CODEC).cacheEncoding().build());

	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> HAMMER = DATA_COMPONENTS.register("hammer", () -> DataComponentType.<Boolean>builder().persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL).cacheEncoding().build());
	
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> DESTROYER = DATA_COMPONENTS.register("destroyer", () -> DataComponentType.<Boolean>builder().persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL).cacheEncoding().build());
	
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> FLY = DATA_COMPONENTS.register("fly", () -> DataComponentType.<Boolean>builder().persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL).cacheEncoding().build());

	public static final DeferredHolder<DataComponentType<?>, DataComponentType<String>> SINGULARITY_ID = DATA_COMPONENTS.register("singularity_id", () -> DataComponentType.<String>builder().persistent(Codec.STRING).networkSynchronized(ByteBufCodecs.STRING_UTF8).cacheEncoding().build());
	
	public static void registerDataComponents(IEventBus modEventBus) {
		DATA_COMPONENTS.register(modEventBus);
	}}
