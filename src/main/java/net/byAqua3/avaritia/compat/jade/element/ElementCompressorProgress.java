package net.byAqua3.avaritia.compat.jade.element;

import net.byAqua3.avaritia.Avaritia;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec2;
import snownee.jade.api.ui.Element;

public class ElementCompressorProgress extends Element {

	public static final ResourceLocation PROGRESS_BASE = ResourceLocation.tryBuild(Avaritia.MODID, "textures/gui/sprites/progress_base.png");
	public static final ResourceLocation PROGRESS = ResourceLocation.tryBuild(Avaritia.MODID, "textures/gui/sprites/progress.png");

	private float progress;

	public ElementCompressorProgress(float progress) {
		this.progress = progress;
	}

	@Override
	public Vec2 getSize() {
		return new Vec2(16.0F, 16.0F);
	}

	@Override
	public void render(GuiGraphics guiGraphics, float x, float y, float maxX, float maxY) {
		float progress = 16.0F * this.progress;
		
		guiGraphics.blit(PROGRESS_BASE, (int) x, (int) y, 0.0F, 0.0F, 16, 16, 16, 16);
		guiGraphics.blit(PROGRESS, (int) x, (int) y + (16 - (int) progress), 0.0F, 16.0F - (int) progress, 16, (int) progress, 16, 16);
	}
}
