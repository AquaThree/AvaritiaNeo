package net.byAqua3.avaritia.compat.rei;

import net.byAqua3.avaritia.compat.ICompatInit;
import net.byAqua3.avaritia.compat.rei.event.AvaritiaREIEvent;
import net.byAqua3.avaritia.compat.rei.network.PacketDisplaySync;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.HandlerThread;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class AvaritiaREI implements ICompatInit {

	@SubscribeEvent
	public static void onRegisterPayloadHandlers(RegisterPayloadHandlersEvent event) {
		PayloadRegistrar registrar = event.registrar("1");

		registrar = registrar.executesOn(HandlerThread.MAIN);
		registrar.playBidirectional(PacketDisplaySync.TYPE, PacketDisplaySync.STREAM_CODEC,
				new PacketDisplaySync.Handler());
	}

	@Override
	public void init(IEventBus modEventBus) {
		modEventBus.addListener(AvaritiaREI::onRegisterPayloadHandlers);
		NeoForge.EVENT_BUS.register(new AvaritiaREIEvent());
	}

}
