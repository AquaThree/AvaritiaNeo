package net.byAqua3.avaritia.compat.rei.network;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.UnaryOperator;

import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.impl.client.registry.display.DisplayRegistryImpl;
import net.byAqua3.avaritia.Avaritia;
import net.byAqua3.avaritia.compat.rei.display.DisplayCompressorRecipe;
import net.byAqua3.avaritia.compat.rei.display.DisplayExtremeRecipe;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.handling.IPayloadHandler;

public record PacketDisplaySync(List<Display> displays) implements CustomPacketPayload {
	public static final Type<PacketDisplaySync> TYPE = new Type<>(
			ResourceLocation.tryBuild(Avaritia.MODID, "display_sync"));
	public static final StreamCodec<RegistryFriendlyByteBuf, PacketDisplaySync> STREAM_CODEC = StreamCodec
			.composite(new StreamCodec<RegistryFriendlyByteBuf, Display>() {
					@SuppressWarnings("deprecation")
					@Override
					public void encode(RegistryFriendlyByteBuf buf, Display display) {
						RegistryFriendlyByteBuf tmpBuf = new RegistryFriendlyByteBuf(Unpooled.buffer(),
								buf.registryAccess());
						try {
							if (display instanceof DisplayExtremeRecipe.Shaped) {
								buf.writeInt(0);
								DisplayExtremeRecipe.Shaped.SERIALIZER.streamCodec().encode(tmpBuf,
										(DisplayExtremeRecipe.Shaped) display);
							} else if (display instanceof DisplayExtremeRecipe.Shapeless) {
								buf.writeInt(1);
								DisplayExtremeRecipe.Shapeless.SERIALIZER.streamCodec().encode(tmpBuf,
										(DisplayExtremeRecipe.Shapeless) display);
							} else if (display instanceof DisplayCompressorRecipe.Shapeless) {
								buf.writeInt(2);
								DisplayCompressorRecipe.Shapeless.SERIALIZER.streamCodec().encode(tmpBuf,
										(DisplayCompressorRecipe.Shapeless) display);
							} else {
								buf.writeInt(3);
								Display.streamCodec().encode(tmpBuf, display);
							}
						} catch (Throwable throwable) {
							tmpBuf.release();
							buf.writeBoolean(false);
							Avaritia.LOGGER.debug("Failed to encode display: %s".formatted(display), throwable);
							return;
						}

						buf.writeBoolean(true);
						RegistryFriendlyByteBuf.writeByteArray(buf, ByteBufUtil.getBytes(tmpBuf));
						tmpBuf.release();
					}

					@SuppressWarnings("deprecation")
					@Override
					public Display decode(RegistryFriendlyByteBuf buf) {
						int index = buf.readInt();
						boolean success = buf.readBoolean();
						if (!success) {
							return null;
						}
						RegistryFriendlyByteBuf tmpBuf = new RegistryFriendlyByteBuf(
								Unpooled.wrappedBuffer(RegistryFriendlyByteBuf.readByteArray(buf)),
								buf.registryAccess());
						try {
							if (index == 0) {
								return DisplayExtremeRecipe.Shaped.SERIALIZER.streamCodec().decode(tmpBuf);
							} else if (index == 1) {
								return DisplayExtremeRecipe.Shapeless.SERIALIZER.streamCodec().decode(tmpBuf);
							} else if (index == 2) {
								return DisplayCompressorRecipe.Shapeless.SERIALIZER.streamCodec().decode(tmpBuf);
							} else {
								return Display.streamCodec().decode(tmpBuf);
							}
						} catch (Throwable throwable) {
							return null;
						} finally {
							tmpBuf.release();
						}
					}
				}
			.apply(ByteBufCodecs.<RegistryFriendlyByteBuf, Display, Collection<Display>>collection(ArrayList::new))
					.map(list -> {
						return list.stream().filter(Objects::nonNull).toList();
					}, UnaryOperator.identity()), PacketDisplaySync::displays, PacketDisplaySync::new);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return PacketDisplaySync.TYPE;
	}

	public static class Handler implements IPayloadHandler<PacketDisplaySync> {
		@Override
		public void handle(PacketDisplaySync packet, IPayloadContext context) {
			context.enqueueWork(() -> {
				DisplayRegistryImpl registry = (DisplayRegistryImpl) DisplayRegistry.getInstance();
				registry.addJob(() -> {			
					for (Display display : packet.displays) {
						registry.add(display);
					}
				});
			});
		}
	}

}
