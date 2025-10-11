package net.byAqua3.avaritia.compat.jei.category;

import java.util.ArrayList;
import java.util.List;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.byAqua3.avaritia.block.BlockExtremeCraftingTable;
import net.byAqua3.avaritia.compat.jei.AvaritiaJEIPlugin;
import net.byAqua3.avaritia.gui.GuiExtremeCraftingTable;
import net.byAqua3.avaritia.loader.AvaritiaBlocks;
import net.byAqua3.avaritia.recipe.RecipeExtremeCrafting;
import net.byAqua3.avaritia.recipe.RecipeExtremeShaped;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class CategoryExtremeRecipe implements IRecipeCategory<RecipeExtremeCrafting> {

	private final IDrawable background;

	private final IDrawable icon;

	public CategoryExtremeRecipe(IGuiHelper guiHelper) {
		this.background = guiHelper.createDrawable(GuiExtremeCraftingTable.COMPAT_BACKGROUND_LOCATION, 0, 0, 189, 163);
		this.icon = guiHelper.createDrawableItemStack(new ItemStack(AvaritiaBlocks.EXTREME_CRAFTING_TABLE_ITEM.get()));
	}

	@Override
	public IDrawable getBackground() {
		return this.background;
	}

	@Override
	public IDrawable getIcon() {
		return this.icon;
	}

	@Override
	public Component getTitle() {
		return BlockExtremeCraftingTable.TITLE;
	}

	@Override
	public RecipeType<RecipeExtremeCrafting> getRecipeType() {
		return AvaritiaJEIPlugin.EXTREME_CRAFTING;
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder recipeLayoutBuilder, RecipeExtremeCrafting recipe, IFocusGroup group) {
		List<List<ItemStack>> inputs = recipe.getIngredients().stream().map(ingredient -> List.of(ingredient.getItems())).toList();
		ItemStack resultItem = recipe.getResultItem(RegistryAccess.EMPTY);

		List<IRecipeSlotBuilder> inputSlots = new ArrayList<>();
		for (int y = 0; y < 9; y++) {
			for (int x = 0; x < 9; x++) {
				inputSlots.add(recipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT, x * 18 + 2, y * 18 + 2));
			}
		}
		if (recipe instanceof RecipeExtremeShaped) {
			RecipeExtremeShaped shapedRecipe = (RecipeExtremeShaped) recipe;

			for (int y = 0; y < 9; y++) {
				for (int x = 0; x < 9; x++) {
					int slotIndex = x + y * 9;
					int inputIndex = x + y * shapedRecipe.getWidth();
					if (inputIndex >= inputs.size() || x >= shapedRecipe.getWidth()) {
						continue;
					}
					inputSlots.get(slotIndex).addItemStacks(inputs.get(inputIndex));
				}
			}
		} else {
			recipeLayoutBuilder.setShapeless();

			for (int i = 0; i < inputs.size(); i++) {
				inputSlots.get(i).addItemStacks(inputs.get(i));
			}
		}
		recipeLayoutBuilder.addSlot(RecipeIngredientRole.OUTPUT, 168, 74).addItemStack(resultItem);
	}
}
