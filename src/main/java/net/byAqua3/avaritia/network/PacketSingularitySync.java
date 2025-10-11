package net.byAqua3.avaritia.network;

import java.util.ArrayList;
import java.util.List;

import net.byAqua3.avaritia.Avaritia;
import net.byAqua3.avaritia.loader.AvaritiaSingularities;
import net.byAqua3.avaritia.singularity.Singularity;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.handling.IPayloadHandler;

public record PacketSingularitySync(List<Singularity> singularities) implements CustomPacketPayload {

	public static final Type<PacketSingularitySync> TYPE = new Type<>(ResourceLocation.tryBuild(Avaritia.MODID, "singularity_sync"));
	public static final StreamCodec<RegistryFriendlyByteBuf, PacketSingularitySync> STREAM_CODEC = StreamCodec.of(PacketSingularitySync::toNetwork, PacketSingularitySync::fromNetwork);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return PacketSingularitySync.TYPE;
	}

	private static PacketSingularitySync fromNetwork(RegistryFriendlyByteBuf friendlyByteBuf) {
		int size = friendlyByteBuf.readVarInt();
		List<Singularity> singularities = new ArrayList<>();

		for (int j = 0; j < size; ++j) {
			singularities.add(Singularity.streamCodec().decode(friendlyByteBuf));
		}
		return new PacketSingularitySync(singularities);
	}

	private static void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, PacketSingularitySync packet) {
		friendlyByteBuf.writeVarInt(packet.singularities.size());

		for (Singularity singularity : packet.singularities) {
			Singularity.streamCodec().encode(friendlyByteBuf, singularity);
		}
	}

	public static class Handler implements IPayloadHandler<PacketSingularitySync> {
		@Override
		public void handle(PacketSingularitySync packet, IPayloadContext context) {
			context.enqueueWork(() -> {
				AvaritiaSingularities.getInstance().getSingularities().clear();
				AvaritiaSingularities.getInstance().getSingularities().addAll(packet.singularities);
			});
		}
	}}
