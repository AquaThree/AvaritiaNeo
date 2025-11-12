package net.byAqua3.avaritia.singularity;

import java.util.List;
import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public class Singularity {

	public static final MapCodec<Singularity> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(Codec.STRING.fieldOf("id").forGetter(singularity -> singularity.id), Codec.INT.listOf(2, 2).fieldOf("color").forGetter(singularity -> List.of(singularity.color, singularity.layerColor))).apply(instance, (id, color) -> new Singularity(id, color.get(0), color.get(1))));
	public static final StreamCodec<RegistryFriendlyByteBuf, Singularity> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.STRING_UTF8, singularity -> singularity.id, ByteBufCodecs.INT, singularity -> singularity.color, ByteBufCodecs.INT, singularity -> singularity.layerColor, Singularity::new);
	public static final Codec<Optional<net.neoforged.neoforge.common.conditions.WithConditions<Singularity>>> CONDITIONAL_CODEC = net.neoforged.neoforge.common.conditions.ConditionalOps.createConditionalCodecWithConditions(CODEC.codec());

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

	public static MapCodec<Singularity> codec() {
		return CODEC;
	}

	public static StreamCodec<RegistryFriendlyByteBuf, Singularity> streamCodec() {
		return STREAM_CODEC;
	}
}
