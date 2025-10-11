package net.byAqua3.avaritia.item;

import java.awt.Color;

import net.byAqua3.avaritia.loader.AvaritiaDataComponents;
import net.byAqua3.avaritia.loader.AvaritiaSingularities;
import net.byAqua3.avaritia.singularity.Singularity;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class ItemJsonSingularity extends ItemSingularity {

	public ItemJsonSingularity(Properties properties) {
		super(properties, 0, 0);
	}

	@Override
	public Color getColor(ItemStack stack) {
		String singularityId = stack.getOrDefault(AvaritiaDataComponents.SINGULARITY_ID, "null");
		Singularity singularity = AvaritiaSingularities.getInstance().getSingularity(singularityId);

		if (singularity != null) {
			return new Color(singularity.color);
		}

		return new Color(255, 255, 255);
	}

	@Override
	public Color getLayerColor(ItemStack stack) {
		String singularityId = stack.getOrDefault(AvaritiaDataComponents.SINGULARITY_ID, "null");
		Singularity singularity = AvaritiaSingularities.getInstance().getSingularity(singularityId);
		if (singularity != null) {
			return new Color(singularity.layerColor);
		}
		return new Color(255, 255, 255);
	}

	@Override
	public Component getName(ItemStack stack) {
		String singularityId = stack.getOrDefault(AvaritiaDataComponents.SINGULARITY_ID, "null");
		return Component.translatable("item.avaritia." + singularityId);
	}}
