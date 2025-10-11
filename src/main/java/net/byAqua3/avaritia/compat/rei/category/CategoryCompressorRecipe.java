package net.byAqua3.avaritia.compat.rei.category;

import java.util.ArrayList;
import java.util.List;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.REIRuntime;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.byAqua3.avaritia.block.BlockNeutroniumCompressor;
import net.byAqua3.avaritia.compat.rei.AvaritiaREIPlugin;
import net.byAqua3.avaritia.compat.rei.display.DisplayCompressorRecipe;
import net.byAqua3.avaritia.event.AvaritiaClientEvent;
import net.byAqua3.avaritia.gui.GuiNeutroniumCompressor;
import net.byAqua3.avaritia.loader.AvaritiaBlocks;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public class CategoryCompressorRecipe implements DisplayCategory<DisplayCompressorRecipe> {

	private final Renderer icon;

	public CategoryCompressorRecipe() {
		this.icon = EntryStacks.of(AvaritiaBlocks.COMPRESSOR_ITEM.get());
	}

	@Override
	public CategoryIdentifier<DisplayCompressorRecipe> getCategoryIdentifier() {
		return AvaritiaREIPlugin.COMPRESSOR;
	}

	@Override
	public Renderer getIcon() {
		return this.icon;
	}

	@Override
	public Component getTitle() {
		return BlockNeutroniumCompressor.TITLE;
	}

	@Override
	public int getDisplayWidth(DisplayCompressorRecipe display) {
		return 120;
	}

	@Override
	public int getDisplayHeight() {
		return 55;
	}

	@Override
	public List<Widget> setupDisplay(DisplayCompressorRecipe display, Rectangle rectangle) {
		Point startPoint = new Point(rectangle.getX(), rectangle.getY());
		List<Widget> widgets = new ArrayList<>();
		widgets.add(Widgets.createRecipeBase(rectangle));
		widgets.add(Widgets.withTranslate(Widgets.createDrawableWidget((guiGraphics, mouseX, mouseY, partialTicks) -> {
			guiGraphics.blit(REIRuntime.getInstance().isDarkThemeEnabled() ? GuiNeutroniumCompressor.DARK_BACKGROUND_LOCATION : GuiNeutroniumCompressor.BACKGROUND_LOCATION, 0, 0, 37, 29, 102, 41, 256, 256);
		}), startPoint.getX() + 10, startPoint.getY() + 10, 0));
		widgets.add(Widgets.withTranslate(Widgets.createDrawableWidget((guiGraphics, mouseX, mouseY, partialTicks) -> {
			int offset = Mth.floor((System.currentTimeMillis() - AvaritiaClientEvent.lastTime) / 2.5D) % display.getCost();
			float progress = 16.0F * ((float) offset / display.getCost());
			guiGraphics.blit(REIRuntime.getInstance().isDarkThemeEnabled() ? GuiNeutroniumCompressor.DARK_BACKGROUND_LOCATION : GuiNeutroniumCompressor.BACKGROUND_LOCATION, 0, 0 + (16 - (int) progress), 176.0F, 32.0F - (int) progress, 16, (int) progress, 256, 256);
		}), startPoint.getX() + 63, startPoint.getY() + 16, 0));
		widgets.add(Widgets.createLabel(new Point(rectangle.getCenterX(), rectangle.getMaxY() - 15), Component.translatable("avaritia:container.neutronium_compressor.info", display.getCost())).color(-12566464, -4473925).noShadow().centered());
		widgets.add(Widgets.createSlot(new Point(startPoint.getX() + 12, rectangle.getCenterY() - 11)).disableBackground().entries(display.getInputEntries().get(0)).markInput());
		widgets.add(Widgets.createSlot(new Point(startPoint.getX() + rectangle.getWidth() - 30, rectangle.getCenterY() - 11)).entries(display.getOutputEntries().get(0)).disableBackground().markOutput());
		return widgets;

	}}
