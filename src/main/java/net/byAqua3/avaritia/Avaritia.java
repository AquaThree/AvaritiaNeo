package net.byAqua3.avaritia;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.byAqua3.avaritia.loader.AvaritiaArmorMaterials;
import net.byAqua3.avaritia.loader.AvaritiaBlocks;
import net.byAqua3.avaritia.loader.AvaritiaCompats;
import net.byAqua3.avaritia.loader.AvaritiaConfigs;
import net.byAqua3.avaritia.loader.AvaritiaDataComponents;
import net.byAqua3.avaritia.loader.AvaritiaEntities;
import net.byAqua3.avaritia.loader.AvaritiaEntityRenderers;
import net.byAqua3.avaritia.loader.AvaritiaEvents;
import net.byAqua3.avaritia.loader.AvaritiaItems;
import net.byAqua3.avaritia.loader.AvaritiaMenus;
import net.byAqua3.avaritia.loader.AvaritiaModels;
import net.byAqua3.avaritia.loader.AvaritiaNetworks;
import net.byAqua3.avaritia.loader.AvaritiaRecipes;
import net.byAqua3.avaritia.loader.AvaritiaShaders;
import net.byAqua3.avaritia.loader.AvaritiaSounds;
import net.byAqua3.avaritia.loader.AvaritiaTabs;
import net.byAqua3.avaritia.loader.AvaritiaTriggers;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;

@Mod(Avaritia.MODID)
public class Avaritia {

	public static final String MODID = "avaritia";
	public static final String NAME = "Avaritia";
	public static final String VERSION = "1.2.5";
	public static final String[] AUTHORS = new String[] { "Aqua3" };

	public static final Logger LOGGER = LogUtils.getLogger();

	public Avaritia(IEventBus modEventBus) {
		AvaritiaArmorMaterials.registerArmorMaterials(modEventBus);
		AvaritiaDataComponents.registerDataComponents(modEventBus);
		AvaritiaItems.registerItems(modEventBus);
		AvaritiaModels.registerModels(modEventBus);
		AvaritiaBlocks.registerBlocks(modEventBus);
		AvaritiaEntities.registerEntities(modEventBus);
		AvaritiaRecipes.registerRecipes(modEventBus);
		AvaritiaSounds.registerSounds(modEventBus);
		AvaritiaTabs.registerTabs(modEventBus);
		AvaritiaMenus.registerMenus(modEventBus);
		AvaritiaTriggers.registerTriggers(modEventBus);
		AvaritiaNetworks.registerNetworks(modEventBus);
		AvaritiaEvents.registerEvents();
		AvaritiaConfigs.registerConfigs();
		AvaritiaCompats.registerCompats(modEventBus);
		if (FMLEnvironment.dist.isClient()) {
			AvaritiaEntityRenderers.registerEntityRenderers(modEventBus);
			AvaritiaShaders.registerShaders(modEventBus);
		}
		modEventBus.addListener(this::commonSetup);
		modEventBus.addListener(this::clientSetup);
		modEventBus.addListener(this::serverSetup);
	}

	private void commonSetup(final FMLCommonSetupEvent event) {
	}

	private void clientSetup(final FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
			AvaritiaItems.initItemProperties();
			AvaritiaConfigs.registerConfigScreen();
		});
	}

	private void serverSetup(final FMLDedicatedServerSetupEvent event) {
	}}
