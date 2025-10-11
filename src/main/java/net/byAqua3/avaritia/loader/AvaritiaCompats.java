package net.byAqua3.avaritia.loader;

import net.byAqua3.avaritia.compat.AvaritiaCompat;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;

public class AvaritiaCompats {

	public static void registerCompats(IEventBus modEventBus) {
		AvaritiaCompat[] compats = AvaritiaCompat.values();
		for (AvaritiaCompat compat : compats) {
			if (ModList.get().isLoaded(compat.getModId())) {
				if(compat.getModInit() != null) {
				   compat.getModInit().init(modEventBus);	
				}
			}
		}
	}
}
