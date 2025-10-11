package net.byAqua3.avaritia.loader;

import java.util.HashMap;
import java.util.Map;

import net.byAqua3.avaritia.Avaritia;
import net.byAqua3.avaritia.geometry.GeometryLoaderCosmic;
import net.byAqua3.avaritia.geometry.GeometryLoaderHalo;
import net.byAqua3.avaritia.geometry.IBakedModelRenderer;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ModelEvent;

public class AvaritiaModels {
	
	public static final Map<ResourceLocation, BlockModel> LOAD_MODELS = new HashMap<>();
	public static final Map<ResourceLocation, IBakedModelRenderer> LOAD_ITEM_MODELS = new HashMap<>();

	public static void registerModels(IEventBus modEventBus) {
		modEventBus.addListener(AvaritiaModels::onRegisterGeometryLoaders);
	}

	@SubscribeEvent
	public static void onRegisterGeometryLoaders(ModelEvent.RegisterGeometryLoaders event) {
		event.register(ResourceLocation.tryBuild(Avaritia.MODID, "cosmic"), new GeometryLoaderCosmic());
		event.register(ResourceLocation.tryBuild(Avaritia.MODID, "halo"), new GeometryLoaderHalo());
	}
}
