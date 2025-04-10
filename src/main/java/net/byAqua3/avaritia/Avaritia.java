package net.byAqua3.avaritia;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.byAqua3.avaritia.loader.AvaritiaBlocks;
import net.byAqua3.avaritia.loader.AvaritiaCompats;
import net.byAqua3.avaritia.loader.AvaritiaConfigs;
import net.byAqua3.avaritia.loader.AvaritiaDataComponents;
import net.byAqua3.avaritia.loader.AvaritiaEntities;
import net.byAqua3.avaritia.loader.AvaritiaEvents;
import net.byAqua3.avaritia.loader.AvaritiaItems;
import net.byAqua3.avaritia.loader.AvaritiaMenus;
import net.byAqua3.avaritia.loader.AvaritiaNetworks;
import net.byAqua3.avaritia.loader.AvaritiaRecipes;
import net.byAqua3.avaritia.loader.AvaritiaSounds;
import net.byAqua3.avaritia.loader.AvaritiaTabs;
import net.byAqua3.avaritia.loader.AvaritiaTriggers;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLDedicatedServerSetupEvent;

@Mod(Avaritia.MODID)
public class Avaritia {

	public static final String MODID = "avaritia";
	public static final String NAME = "Avaritia";
	public static final String VERSION = "1.0.3";
	public static final String[] AUTHORS = new String[] { "Aqua3" };

	public static final Logger LOGGER = LogUtils.getLogger();

	public Avaritia(IEventBus modEventBus) {
		AvaritiaDataComponents.registerDataComponents(modEventBus);
		AvaritiaItems.registerItems(modEventBus);
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
		modEventBus.addListener(this::commonSetup);
		modEventBus.addListener(this::clientSetup);
		modEventBus.addListener(this::serverSetup);
	}

	private void commonSetup(final FMLCommonSetupEvent event) {
	}

	private void clientSetup(final FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
<<<<<<< HEAD
			AvaritiaConfigs.registerConfigScreen();
		});
=======
			AvaritiaItems.initItemProperties();
			AvaritiaMenus.registerScreens();
	        });
>>>>>>> d4d1cb6c05ca8fdefe75682952d4a2a39f983c43
	}

	private void serverSetup(final FMLDedicatedServerSetupEvent event) {
	}

}
