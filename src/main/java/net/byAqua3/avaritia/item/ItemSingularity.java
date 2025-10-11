package net.byAqua3.avaritia.item;

import java.awt.Color;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ItemSingularity extends Item {
	
	private Color color;
	private Color layerColor;

	public ItemSingularity(Properties properties, int color, int layerColor) {
		super(properties);
		this.color = new Color(color);
		this.layerColor = new Color(layerColor);
	}
	
	public Color getColor(ItemStack stack) {
		return this.color;
	}
	
	public Color getLayerColor(ItemStack stack) {
		return this.layerColor;
	}
}
