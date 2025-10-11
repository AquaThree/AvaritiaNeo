package net.byAqua3.avaritia.singularity;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public class Singularity {
	
	public static final StreamCodec<RegistryFriendlyByteBuf, Singularity> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.STRING_UTF8, singularity -> singularity.id,
			ByteBufCodecs.INT, singularity -> singularity.color, ByteBufCodecs.INT, singularity -> singularity.layerColor, Singularity::new);

	public String id;
	public int color;
	public int layerColor;

	public Singularity(String id, int color, int layerColor) {
		this.id = id;
		this.color = color;
		this.layerColor = layerColor;
	}
	
	public String getId() {
		return this.id;
	}
	
	public int getColor() {
		return this.color;
	}
	public int getLayerColor() {
		return this.layerColor;
	}

	public static StreamCodec<RegistryFriendlyByteBuf, Singularity> streamCodec() {
		return STREAM_CODEC;
	}}
