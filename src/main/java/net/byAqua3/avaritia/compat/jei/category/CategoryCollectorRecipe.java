package net.byAqua3.avaritia.compat.jei.category;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.byAqua3.avaritia.block.BlockNeutronCollector;
import net.byAqua3.avaritia.compat.jei.AvaritiaJEIPlugin;
import net.byAqua3.avaritia.gui.GuiNeutronCollector;
import net.byAqua3.avaritia.loader.AvaritiaBlocks;
import net.byAqua3.avaritia.recipe.RecipeCollector;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class CategoryCollectorRecipe implements IRecipeCategory<RecipeCollector> {

	private final IDrawable background;

	private final IDrawable icon;

	public CategoryCollectorRecipe(IGuiHelper guiHelper) {
		this.background = guiHelper.createDrawable(GuiNeutronCollector.BACKGROUND_LOCATION, 37, 29, 102, 41);
		this.icon = guiHelper.createDrawableItemStack(new ItemStack(AvaritiaBlocks.NEUTRON_COLLECTOR_ITEM.get()));
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
		return BlockNeutronCollector.TITLE;
	}

	@Override
	public RecipeType<RecipeCollector> getRecipeType() {
		return AvaritiaJEIPlugin.COLLECTOR;
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder recipeLayoutBuilder, RecipeCollector recipe, IFocusGroup group) {
		recipeLayoutBuilder.addSlot(RecipeIngredientRole.OUTPUT, 43, 6).addItemStack(recipe.getResultItem(RegistryAccess.EMPTY));
	}

	@Override
	public void draw(RecipeCollector recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
		Minecraft mc = Minecraft.getInstance();
		Font font = mc.font;
		String text = Component.translatable("avaritia:container.neutron_collector.info", 5.92F).getString();
		guiGraphics.drawString(font, text, (this.background.getWidth() / 2 - font.width(text) / 2), 31, 4210752, false);
	}}
