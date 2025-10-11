package net.byAqua3.avaritia.gui;

import com.mojang.blaze3d.systems.RenderSystem;

import net.byAqua3.avaritia.Avaritia;
import net.byAqua3.avaritia.inventory.MenuNeutroniumCompressor;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class GuiNeutroniumCompressor extends AbstractContainerScreen<MenuNeutroniumCompressor> {

	public static final ResourceLocation BACKGROUND_LOCATION = ResourceLocation.tryBuild(Avaritia.MODID, "textures/gui/compressor.png");
	public static final ResourceLocation DARK_BACKGROUND_LOCATION = ResourceLocation.tryBuild(Avaritia.MODID, "textures/gui/compressor_dark.png");

	public GuiNeutroniumCompressor(MenuNeutroniumCompressor menu, Inventory inventory, Component title) {
		super(menu, inventory, title);
	}

	public boolean isMouseHover(float x, float y, float width, float height, int mouseX, int mouseY) {
		return mouseX >= x && mouseY >= y && mouseX <= x + width && mouseY <= y + height;
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(guiGraphics, mouseX, mouseY, partialTicks);
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
		if (menu.getConsumptionProgress() > 0) {
			float progress = 22.0F * ((float) menu.getConsumptionProgress() / menu.getCompressionProgress());
			guiGraphics.blit(BACKGROUND_LOCATION, this.getGuiLeft() + 62, this.getGuiTop() + 35, 176.0F, 0.0F, (int) progress, 16, 256, 256);
		}
		if (menu.getCompressionProgress() > 0) {
			float progress = 16.0F * ((float) menu.getCompressionProgress() / menu.getCompressionTarget());
			guiGraphics.blit(BACKGROUND_LOCATION, this.getGuiLeft() + 90, this.getGuiTop() + 35 + (16 - (int) progress), 176.0F, 32.0F - (int) progress, 16, (int) progress, 256, 256);
			if (this.isMouseHover(this.getGuiLeft() + 90.0F, this.getGuiTop() + 35.0F, 16.0F, 16.0F, mouseX, mouseY)) {
				String text = String.format("%.2f%%", Float.valueOf(100.0F * menu.getCompressionProgress() / menu.getCompressionTarget()));
				guiGraphics.renderTooltip(this.font, Component.literal(text), mouseX, mouseY);
			}
		}
		this.renderTooltip(guiGraphics, mouseX, mouseY);
	}

	@Override
	protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		guiGraphics.blit(BACKGROUND_LOCATION, this.getGuiLeft(), this.getGuiTop(), 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
	}

	@Override
	protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
		guiGraphics.drawString(this.font, this.title, this.imageWidth / 2 - this.font.width(this.title) / 2, this.titleLabelY, 4210752, false);
		if (menu.getCompressionProgress() > 0) {
			String text = menu.getCompressionProgress() + " / " + menu.getCompressionTarget();
			guiGraphics.drawString(this.font, text, this.imageWidth / 2 - this.font.width(text) / 2, 60.0F, 4210752, false);
		}
		guiGraphics.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, 4210752, false);
	}}
