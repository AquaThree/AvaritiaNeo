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
import net.byAqua3.avaritia.block.BlockNeutronCollector;
import net.byAqua3.avaritia.compat.rei.AvaritiaREIPlugin;
import net.byAqua3.avaritia.compat.rei.display.DisplayCollectorRecipe;
import net.byAqua3.avaritia.gui.GuiNeutronCollector;
import net.byAqua3.avaritia.loader.AvaritiaBlocks;
import net.byAqua3.avaritia.loader.AvaritiaConfigs;
import net.minecraft.network.chat.Component;

public class CategoryCollectorRecipe implements DisplayCategory<DisplayCollectorRecipe> {

	private final Renderer icon;

	public CategoryCollectorRecipe() {
		this.icon = EntryStacks.of(AvaritiaBlocks.NEUTRON_COLLECTOR_ITEM.get());
	}

	@Override
	public CategoryIdentifier<DisplayCollectorRecipe> getCategoryIdentifier() {
		return AvaritiaREIPlugin.COLLECTOR;
	}

	@Override
	public Renderer getIcon() {
		return this.icon;
	}

	@Override
	public Component getTitle() {
		return BlockNeutronCollector.TITLE;
	}

	@Override
	public int getDisplayWidth(DisplayCollectorRecipe display) {
		return 120;
	}

	@Override
	public int getDisplayHeight() {
		return 55;
	}

	@Override
	public List<Widget> setupDisplay(DisplayCollectorRecipe display, Rectangle rectangle) {
		Point startPoint = new Point(rectangle.getX(), rectangle.getY());
		List<Widget> widgets = new ArrayList<>();
		widgets.add(Widgets.createRecipeBase(rectangle));
		widgets.add(Widgets.withTranslate(Widgets.createDrawableWidget((guiGraphics, mouseX, mouseY, partialTicks) -> {
			guiGraphics.blit(REIRuntime.getInstance().isDarkThemeEnabled() ? GuiNeutronCollector.DARK_BACKGROUND_LOCATION : GuiNeutronCollector.BACKGROUND_LOCATION, 0, 0, 37, 29, 102, 41, 256, 256);
		}), startPoint.getX() + 10, startPoint.getY() + 10, 0));
		String time = String.format("%.2f", Float.valueOf(AvaritiaConfigs.productionTicks.get() / 20.0F / 60.0F));
		Component text = Component.translatable("avaritia:container.neutron_collector.info", time);
		widgets.add(Widgets.createLabel(new Point(rectangle.getCenterX(), rectangle.getMaxY() - 15), text).color(-12566464, -4473925).noShadow().centered());
		widgets.add(Widgets.createSlot(new Point(startPoint.getX() + 53, rectangle.getCenterY() - 11)).entries(display.getOutputEntries().get(0)).disableBackground().markOutput());
		return widgets;
	}
	
}
