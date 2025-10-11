package net.byAqua3.avaritia.gui;

import com.mojang.blaze3d.systems.RenderSystem;

import net.byAqua3.avaritia.Avaritia;
import net.byAqua3.avaritia.inventory.MenuExtremeCrafting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class GuiExtremeCraftingTable extends AbstractContainerScreen<MenuExtremeCrafting> {

	public static final ResourceLocation BACKGROUND_LOCATION = ResourceLocation.tryBuild(Avaritia.MODID,
			"textures/gui/extreme_crafting.png");
	public static final ResourceLocation COMPAT_BACKGROUND_LOCATION = ResourceLocation.tryBuild(Avaritia.MODID,
			"textures/gui/extreme_crafting_compat.png");
	public static final ResourceLocation COMPAT_DARK_BACKGROUND_LOCATION = ResourceLocation.tryBuild(Avaritia.MODID,
			"textures/gui/extreme_crafting_compat_dark.png");

	public GuiExtremeCraftingTable(MenuExtremeCrafting menu, Inventory inventory, Component title) {
		super(menu, inventory, title);
		this.imageWidth = 238;
		this.imageHeight = 256;
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
		guiGraphics.blit(BACKGROUND_LOCATION, this.getGuiLeft(), this.getGuiTop(), 0.0F, 0.0F, this.imageWidth,
				this.imageHeight, 256, 256);
	}

	@Override
	protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
	}}
