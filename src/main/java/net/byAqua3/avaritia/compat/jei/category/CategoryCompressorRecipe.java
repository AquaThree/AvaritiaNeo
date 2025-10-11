package net.byAqua3.avaritia.compat.jei.category;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.byAqua3.avaritia.block.BlockNeutroniumCompressor;
import net.byAqua3.avaritia.compat.jei.AvaritiaJEIPlugin;
import net.byAqua3.avaritia.gui.GuiNeutroniumCompressor;
import net.byAqua3.avaritia.loader.AvaritiaBlocks;
import net.byAqua3.avaritia.recipe.RecipeCompressor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class CategoryCompressorRecipe implements IRecipeCategory<RecipeCompressor> {

	private final IDrawable background;

	private final IDrawable icon;

	private final LoadingCache<Integer, IDrawableAnimated> singularities;

	public CategoryCompressorRecipe(IGuiHelper guiHelper) {
		this.background = guiHelper.createDrawable(GuiNeutroniumCompressor.BACKGROUND_LOCATION, 37, 29, 102, 41);
		this.icon = guiHelper.createDrawableItemStack(new ItemStack(AvaritiaBlocks.COMPRESSOR_ITEM.get()));
		this.singularities = CacheBuilder.newBuilder().maximumSize(25L).build(new CacheLoader<Integer, IDrawableAnimated>() {
			@Override
			public IDrawableAnimated load(Integer key) {
				return guiHelper.drawableBuilder(GuiNeutroniumCompressor.BACKGROUND_LOCATION, 176, 16, 16, 16).buildAnimated(Math.max(key.intValue() / 16, 1), IDrawableAnimated.StartDirection.BOTTOM, false);
			}
		});
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
		return BlockNeutroniumCompressor.TITLE;
	}

	@Override
	public RecipeType<RecipeCompressor> getRecipeType() {
		return AvaritiaJEIPlugin.COMPRESSOR;
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder recipeLayoutBuilder, RecipeCompressor recipe, IFocusGroup group) {
		IRecipeSlotBuilder input = recipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT, 2, 6);
		recipe.getIngredients().forEach(input::addIngredients);
		recipeLayoutBuilder.addSlot(RecipeIngredientRole.OUTPUT, 80, 6).addItemStack(recipe.getResultItem(RegistryAccess.EMPTY));
	}

	@Override
	public void draw(RecipeCompressor recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
		IDrawableAnimated singularity = this.singularities.getUnchecked(recipe.getCost());
		singularity.draw(guiGraphics, 53, 6);
		Minecraft mc = Minecraft.getInstance();
		Font font = mc.font;
		String text = Component.translatable("avaritia:container.neutronium_compressor.info", recipe.getCost()).getString();
		guiGraphics.drawString(font, text, (this.background.getWidth() / 2 - font.width(text) / 2), 31, 4210752, false);
	}}
