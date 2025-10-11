package net.byAqua3.avaritia.loader;

import net.byAqua3.avaritia.network.PacketSingularitySync;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.HandlerThread;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class AvaritiaNetworks {

	public static void registerNetworks(IEventBus modEventBus) {
		modEventBus.addListener(AvaritiaNetworks::onRegisterPayloadHandlers);
	}

	@SubscribeEvent
	public static void onRegisterPayloadHandlers(RegisterPayloadHandlersEvent event) {
		PayloadRegistrar registrar = event.registrar("1");

		registrar = registrar.executesOn(HandlerThread.MAIN);
		registrar.playBidirectional(PacketSingularitySync.TYPE, PacketSingularitySync.STREAM_CODEC, new PacketSingularitySync.Handler());
	}}
