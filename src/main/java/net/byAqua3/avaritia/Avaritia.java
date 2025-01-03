package net.byAqua3.avaritia;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.byAqua3.avaritia.event.AvaritiaEvents;
import net.byAqua3.avaritia.loader.AvaritiaSounds;
import net.byAqua3.avaritia.loader.AvaritiaTabs;
import net.byAqua3.avaritia.loader.AvaritiaBlocks;
import net.byAqua3.avaritia.loader.AvaritiaEntities;
import net.byAqua3.avaritia.loader.AvaritiaItems;
import net.byAqua3.avaritia.loader.AvaritiaMenus;
import net.byAqua3.avaritia.loader.AvaritiaRecipes;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLDedicatedServerSetupEvent;

@Mod(Avaritia.MODID)
public class Avaritia {

	public static final String MODID = "avaritia";
	public static final String NAME = "Avaritia";
	public static final String VERSION = "1.0.1";

	public static final Logger LOGGER = LogUtils.getLogger();

	public Avaritia(IEventBus modEventBus) {
		AvaritiaItems.registerItems(modEventBus);
		AvaritiaBlocks.registerBlocks(modEventBus);
		AvaritiaEntities.registerEntities(modEventBus);
		AvaritiaRecipes.registerRecipes(modEventBus);
		AvaritiaSounds.registerSounds(modEventBus);
		AvaritiaTabs.registerTabs(modEventBus);
		AvaritiaMenus.registerMenus(modEventBus);
		AvaritiaEvents.registerEvents();
		modEventBus.addListener(this::commonSetup);
		modEventBus.addListener(this::clientSetup);
		modEventBus.addListener(this::serverSetup);
	}

	private void commonSetup(final FMLCommonSetupEvent event) {
	}

	private void clientSetup(final FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
			AvaritiaItems.initItemProperties();
			AvaritiaMenus.registerScreens();
	        });
	}

	private void serverSetup(final FMLDedicatedServerSetupEvent event) {
	}
}
