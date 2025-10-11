package net.byAqua3.avaritia.compat.rei.category;

import java.util.ArrayList;
import java.util.List;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.REIRuntime;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Slot;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.byAqua3.avaritia.block.BlockExtremeCraftingTable;
import net.byAqua3.avaritia.compat.rei.AvaritiaREIPlugin;
import net.byAqua3.avaritia.compat.rei.display.DisplayExtremeRecipe;
import net.byAqua3.avaritia.gui.GuiExtremeCraftingTable;
import net.byAqua3.avaritia.loader.AvaritiaBlocks;
import net.byAqua3.avaritia.recipe.RecipeExtremeCrafting;
import net.byAqua3.avaritia.recipe.RecipeExtremeShaped;
import net.minecraft.network.chat.Component;

public class CategoryExtremeRecipe implements DisplayCategory<DisplayExtremeRecipe> {

	private final Renderer icon;

	public CategoryExtremeRecipe() {
		this.icon = EntryStacks.of(AvaritiaBlocks.EXTREME_CRAFTING_TABLE_ITEM.get());
	}

	@Override
	public CategoryIdentifier<DisplayExtremeRecipe> getCategoryIdentifier() {
		return AvaritiaREIPlugin.EXTREME_CRAFTING;
	}

	@Override
	public Renderer getIcon() {
		return this.icon;
	}

	@Override
	public Component getTitle() {
		return BlockExtremeCraftingTable.TITLE;
	}

	@Override
	public int getDisplayWidth(DisplayExtremeRecipe display) {
		return 195;
	}

	@Override
	public int getDisplayHeight() {
		return 168;
	}

	@Override
	public List<Widget> setupDisplay(DisplayExtremeRecipe display, Rectangle rectangle) {
		Point startPoint = new Point(rectangle.getX(), rectangle.getY());
		List<Widget> widgets = new ArrayList<>();
		widgets.add(Widgets.createRecipeBase(rectangle));
		widgets.add(Widgets.withTranslate(Widgets.createDrawableWidget((guiGraphics, mouseX, mouseY, partialTicks) -> {
			guiGraphics.blit(REIRuntime.getInstance().isDarkThemeEnabled() ? GuiExtremeCraftingTable.COMPAT_DARK_BACKGROUND_LOCATION : GuiExtremeCraftingTable.COMPAT_BACKGROUND_LOCATION, 0, 0, 0, 0, 189, 163, 256, 256);
		}), startPoint.getX() + 2, startPoint.getY() + 2, 0));
		RecipeExtremeCrafting recipe = display.getRecipe();
		List<EntryIngredient> ingredients = display.getInputEntries();
		List<Slot> inputSlots = new ArrayList<>();
		for (int y = 0; y < 9; y++) {
			for (int x = 0; x < 9; x++) {
				inputSlots.add(Widgets.createSlot(new Point(startPoint.getX() + x * 18 + 4, startPoint.getY() + y * 18 + 4)).disableBackground().markInput());
			}
		}
		if (!display.isShapeless()) {
			RecipeExtremeShaped shapedRecipe = (RecipeExtremeShaped) recipe;

			for (int y = 0; y < 9; y++) {
				for (int x = 0; x < 9; x++) {
					int slotIndex = x + y * 9;
					int inputIndex = x + y * shapedRecipe.getWidth();
					if (inputIndex >= ingredients.size() || x >= shapedRecipe.getWidth()) {
						continue;
					}
					inputSlots.get(slotIndex).entries(ingredients.get(inputIndex));
				}
			}
		} else {
			widgets.add(Widgets.createShapelessIcon(rectangle));

			for (int i = 0; i < ingredients.size(); i++) {
				EntryIngredient ingredient = ingredients.get(i);
				inputSlots.get(i).entries(ingredient);
			}
		}
		widgets.addAll(inputSlots);
		widgets.add(Widgets.createSlot(new Point(startPoint.getX() + rectangle.getWidth() - 25, rectangle.getCenterY() - 8)).entries(display.getOutputEntries().get(0)).disableBackground().markOutput());
		return widgets;

	}
}
