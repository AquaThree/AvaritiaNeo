package net.byAqua3.avaritia.gui;

import com.mojang.blaze3d.systems.RenderSystem;

import net.byAqua3.avaritia.Avaritia;
import net.byAqua3.avaritia.inventory.MenuNeutronCollector;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class GuiNeutronCollector extends AbstractContainerScreen<MenuNeutronCollector> {

	public static final ResourceLocation BACKGROUND_LOCATION = ResourceLocation.tryBuild(Avaritia.MODID, "textures/gui/neutron_collector.png");
	public static final ResourceLocation DARK_BACKGROUND_LOCATION = ResourceLocation.tryBuild(Avaritia.MODID, "textures/gui/neutron_collector_dark.png");

	public GuiNeutronCollector(MenuNeutronCollector menu, Inventory inventory, Component title) {
		super(menu, inventory, title);
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(guiGraphics, mouseX, mouseY, partialTicks);
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
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
		String text = String.format("%.2f%%", Float.valueOf(100.0F * menu.getProgress() / 7111.0F));
		guiGraphics.drawString(this.font,  text, this.imageWidth / 2 - this.font.width(text) / 2, 60.0F, 4210752, false);
		guiGraphics.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, 4210752, false);
	}}
