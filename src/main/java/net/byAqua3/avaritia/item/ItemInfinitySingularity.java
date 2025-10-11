package net.byAqua3.avaritia.item;

import java.awt.Color;
import java.util.List;

import net.byAqua3.avaritia.event.AvaritiaClientEvent;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

public class ItemInfinitySingularity extends ItemSingularity {
	
	private float hue;

	public ItemInfinitySingularity(Properties properties) {
		super(properties, 0, 0);
	}
	
	@Override
	public Color getColor(ItemStack stack) {
		this.hue = (System.currentTimeMillis() - AvaritiaClientEvent.lastTime) / 2000.0F;
		int rgb = Color.HSBtoRGB(this.hue, 1.0F, 1.0F);
		float r = ((rgb >> 16) & 0xFF) / 255.0F;
		float g = ((rgb >> 8) & 0xFF) / 255.0F;
		float b = ((rgb >> 0) & 0xFF) / 255.0F;
		float a = ((rgb >> 24) & 0xFF) / 255.0F;
		return new Color(r, g, b, a);
	}
	
	@Override
	public Color getLayerColor(ItemStack stack) {
		int rgb = Color.HSBtoRGB(this.hue + 0.01F, 1.0F, 1.0F);
		float r = ((rgb >> 16) & 0xFF) / 255.0F;
		float g = ((rgb >> 8) & 0xFF) / 255.0F;
		float b = ((rgb >> 0) & 0xFF) / 255.0F;
		float a = ((rgb >> 24) & 0xFF) / 255.0F;
		return new Color(r, g, b, a);
	}
	
	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		tooltip.add(Component.translatable("tooltip.avaritia.infinity_singularity.desc").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.DARK_GRAY));
	}}
