package net.byAqua3.avaritia.loader;

import net.byAqua3.avaritia.event.AvaritiaClientEvent;
import net.byAqua3.avaritia.event.AvaritiaEvent;
import net.neoforged.neoforge.common.NeoForge;

public class AvaritiaEvents {
	
	public static void registerEvents() {
		NeoForge.EVENT_BUS.register(new AvaritiaEvent());
		NeoForge.EVENT_BUS.register(new AvaritiaClientEvent());
	}}
