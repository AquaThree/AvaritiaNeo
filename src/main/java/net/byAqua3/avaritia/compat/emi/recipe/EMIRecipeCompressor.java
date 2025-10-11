package net.byAqua3.avaritia.compat.emi.recipe;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.TextWidget.Alignment;
import dev.emi.emi.api.widget.WidgetHolder;
import net.byAqua3.avaritia.compat.emi.AvaritiaEMIPlugin;
import net.byAqua3.avaritia.gui.GuiNeutroniumCompressor;
import net.byAqua3.avaritia.recipe.RecipeCompressor;
import net.byAqua3.avaritia.util.RecipeUtils;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;

public class EMIRecipeCompressor implements EmiRecipe {

	private final RecipeCompressor recipe;
	private final ResourceLocation id;
	private final List<EmiIngredient> inputs;
	private final List<EmiStack> outputs;

	public EMIRecipeCompressor(RecipeManager recipeManager, RecipeCompressor recipe) {
		this.recipe = recipe;
		this.id = RecipeUtils.getRecipeId(recipeManager, recipe);
		this.inputs = recipe.getIngredients().stream().map(ingredient -> EmiIngredient.of(ingredient)).toList();
		this.outputs = List.of(EmiStack.of(recipe.getResultItem(RegistryAccess.EMPTY)));
	}

	public RecipeCompressor getRecipe() {
		return this.recipe;
	}

	@Override
	public EmiRecipeCategory getCategory() {
		return AvaritiaEMIPlugin.COMPRESSOR;
	}

	@Override
	public @Nullable ResourceLocation getId() {
		return this.id;
	}

	@Override
	public List<EmiIngredient> getInputs() {
		return this.inputs;
	}

	@Override
	public List<EmiStack> getOutputs() {
		return this.outputs;
	}

	@Override
	public int getDisplayWidth() {
		return 120;
	}

	@Override
	public int getDisplayHeight() {
		return 55;
	}

	@Override
	public void addWidgets(WidgetHolder widgets) {
		widgets.addTexture(GuiNeutroniumCompressor.BACKGROUND_LOCATION, 11, 11, 102, 41, 37, 29, 102, 41, 256, 256);
		widgets.addAnimatedTexture(GuiNeutroniumCompressor.BACKGROUND_LOCATION, 64, 17, 16, 16, 176, 16, this.recipe.getCost() * 3, false, true, false);
		widgets.addText(Component.translatable("avaritia:container.neutronium_compressor.info", this.recipe.getCost()), widgets.getWidth() / 2, widgets.getHeight() - 12, -12566464, false).horizontalAlign(Alignment.CENTER);
		widgets.addSlot(this.getInputs().get(0), 12, widgets.getHeight() / 2 - 11).drawBack(false);
		widgets.addSlot(this.getOutputs().get(0), widgets.getWidth() - 30, widgets.getHeight() / 2 - 11).drawBack(false).recipeContext(this);
	}
}
