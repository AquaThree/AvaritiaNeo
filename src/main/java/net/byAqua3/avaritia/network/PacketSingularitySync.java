package net.byAqua3.avaritia.network;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.UnaryOperator;

import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import net.byAqua3.avaritia.Avaritia;
import net.byAqua3.avaritia.loader.AvaritiaSingularities;
import net.byAqua3.avaritia.singularity.Singularity;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.handling.IPayloadHandler;

public record PacketSingularitySync(List<Singularity> singularities) implements CustomPacketPayload {
	public static final Type<PacketSingularitySync> TYPE = new Type<>(ResourceLocation.tryBuild(Avaritia.MODID, "singularity_sync"));
	public static final StreamCodec<RegistryFriendlyByteBuf, PacketSingularitySync> STREAM_CODEC = StreamCodec
			.composite(new StreamCodec<RegistryFriendlyByteBuf, Singularity>() {
					@SuppressWarnings("deprecation")
					@Override
					public void encode(RegistryFriendlyByteBuf buf, Singularity singularity) {
						RegistryFriendlyByteBuf tmpBuf = new RegistryFriendlyByteBuf(Unpooled.buffer(),
								buf.registryAccess());
						try {
							Singularity.streamCodec().encode(tmpBuf, singularity);
						} catch (Throwable throwable) {
							tmpBuf.release();
							buf.writeBoolean(false);
							Avaritia.LOGGER.debug("Failed to encode singularity: %s".formatted(singularity), throwable);
							return;
						}

						buf.writeBoolean(true);
						RegistryFriendlyByteBuf.writeByteArray(buf, ByteBufUtil.getBytes(tmpBuf));
						tmpBuf.release();
					}

					@SuppressWarnings("deprecation")
					@Override
					public Singularity decode(RegistryFriendlyByteBuf buf) {
						boolean success = buf.readBoolean();
						if (!success) {
							return null;
						}
						RegistryFriendlyByteBuf tmpBuf = new RegistryFriendlyByteBuf(
								Unpooled.wrappedBuffer(RegistryFriendlyByteBuf.readByteArray(buf)),
								buf.registryAccess());
						try {
							return Singularity.streamCodec().decode(tmpBuf);
						} catch (Throwable throwable) {
							return null;
						} finally {
							tmpBuf.release();
						}
					}
				}
			.apply(ByteBufCodecs.<RegistryFriendlyByteBuf, Singularity, Collection<Singularity>>collection(ArrayList::new))
					.map(list -> {
						return list.stream().filter(Objects::nonNull).toList();
					}, UnaryOperator.identity()), PacketSingularitySync::singularities, PacketSingularitySync::new);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return PacketSingularitySync.TYPE;
	}

	public static class Handler implements IPayloadHandler<PacketSingularitySync> {
		@Override
		public void handle(PacketSingularitySync packet, IPayloadContext context) {
			context.enqueueWork(() -> {
				AvaritiaSingularities.getInstance().getSingularities().clear();
				AvaritiaSingularities.getInstance().getSingularities().addAll(packet.singularities);
			});
		}
	}

}
