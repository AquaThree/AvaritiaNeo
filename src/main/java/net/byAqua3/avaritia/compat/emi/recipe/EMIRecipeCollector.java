package net.byAqua3.avaritia.compat.emi.recipe;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.TextWidget.Alignment;
import dev.emi.emi.api.widget.WidgetHolder;
import net.byAqua3.avaritia.Avaritia;
import net.byAqua3.avaritia.compat.emi.AvaritiaEMIPlugin;
import net.byAqua3.avaritia.gui.GuiNeutronCollector;
import net.byAqua3.avaritia.loader.AvaritiaConfigs;
import net.byAqua3.avaritia.recipe.RecipeCollector;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;

public class EMIRecipeCollector implements EmiRecipe {

	private final RecipeCollector recipe;
	private final ResourceLocation id;
	private final List<EmiIngredient> inputs;
	private final List<EmiStack> outputs;

	public EMIRecipeCollector(RecipeManager recipeManager, RecipeCollector recipe) {
		this.recipe = recipe;
		this.id = ResourceLocation.tryBuild(Avaritia.MODID, "/collector");
		this.inputs = recipe.getIngredients().stream().map(ingredient -> EmiIngredient.of(ingredient)).toList();
		this.outputs = List.of(EmiStack.of(recipe.getResultItem(RegistryAccess.EMPTY)));
	}

	public RecipeCollector getRecipe() {
		return this.recipe;
	}

	@Override
	public EmiRecipeCategory getCategory() {
		return AvaritiaEMIPlugin.COLLECTOR;
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
		widgets.addTexture(GuiNeutronCollector.BACKGROUND_LOCATION, 11, 11, 102, 41, 37, 29, 102, 41, 256, 256);
		String time = String.format("%.2f", Float.valueOf(AvaritiaConfigs.productionTicks.get() / 20.0F / 60.0F));
		Component text = Component.translatable("avaritia:container.neutron_collector.info", time);
		widgets.addText(text, widgets.getWidth() / 2, widgets.getHeight() - 12, -12566464, false).horizontalAlign(Alignment.CENTER);
		widgets.addSlot(this.getOutputs().get(0), 53, widgets.getHeight() / 2 - 11).drawBack(false).recipeContext(this);
	}
}
