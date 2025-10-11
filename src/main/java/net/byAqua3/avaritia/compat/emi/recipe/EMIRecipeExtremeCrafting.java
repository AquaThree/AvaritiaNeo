package net.byAqua3.avaritia.compat.emi.recipe;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.SlotWidget;
import dev.emi.emi.api.widget.WidgetHolder;
import net.byAqua3.avaritia.compat.emi.AvaritiaEMIPlugin;
import net.byAqua3.avaritia.gui.GuiExtremeCraftingTable;
import net.byAqua3.avaritia.recipe.RecipeExtremeCrafting;
import net.byAqua3.avaritia.recipe.RecipeExtremeShaped;
import net.byAqua3.avaritia.util.RecipeUtils;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;

public class EMIRecipeExtremeCrafting implements EmiRecipe {

	private final RecipeExtremeCrafting recipe;
	private final ResourceLocation id;
	private final List<EmiIngredient> inputs;
	private final List<EmiStack> outputs;

	public EMIRecipeExtremeCrafting(RecipeManager recipeManager, RecipeExtremeCrafting recipe) {
		this.recipe = recipe;
		this.id = RecipeUtils.getRecipeId(recipeManager, recipe);
		this.inputs = recipe.getIngredients().stream().map(ingredient -> EmiIngredient.of(ingredient)).toList();
		this.outputs = List.of(EmiStack.of(recipe.getResultItem(RegistryAccess.EMPTY)));
	}
	
	public RecipeExtremeCrafting getRecipe() {
		return this.recipe;
	}

	@Override
	public EmiRecipeCategory getCategory() {
		return AvaritiaEMIPlugin.EXTREME_CRAFTING;
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
		return 195;
	}

	@Override
	public int getDisplayHeight() {
		return 168;
	}

	@Override
	public void addWidgets(WidgetHolder widgets) {
		widgets.addTexture(GuiExtremeCraftingTable.COMPAT_BACKGROUND_LOCATION, 3, 3, 189, 183, 0, 0, 189, 183, 256, 256);
		List<EmiIngredient> ingredients = this.getInputs();
		List<SlotWidget> inputSlots = new ArrayList<>();
		for (int y = 0; y < 9; y++) {
			for (int x = 0; x < 9; x++) {
				inputSlots.add(new SlotWidget(EmiStack.EMPTY, x * 18 + 4, y * 18 + 4).drawBack(false));
			}
		}
		if (this.recipe instanceof RecipeExtremeShaped) {
			RecipeExtremeShaped shapedRecipe = (RecipeExtremeShaped) this.recipe;

			for (int y = 0; y < 9; y++) {
				for (int x = 0; x < 9; x++) {
					int slotIndex = x + y * 9;
					int inputIndex = x + y * shapedRecipe.getWidth();
					if (inputIndex >= ingredients.size() || x >= shapedRecipe.getWidth()) {
						continue;
					}
					SlotWidget originalSlot = inputSlots.get(slotIndex);
					inputSlots.set(slotIndex, new SlotWidget(ingredients.get(inputIndex), originalSlot.getBounds().x(), originalSlot.getBounds().y()).drawBack(false));
				}
			}
		} else {
			widgets.addTexture(EmiTexture.SHAPELESS, widgets.getWidth() - 22, 5);
			
			for (int i = 0; i < ingredients.size(); i++) {
				EmiIngredient ingredient = ingredients.get(i);
				SlotWidget originalSlot = inputSlots.get(i);
				inputSlots.set(i, new SlotWidget(ingredient, originalSlot.getBounds().x(), originalSlot.getBounds().y()).drawBack(false));
			}
		}
		for (SlotWidget slot : inputSlots) {
			widgets.add(slot);
		}
		widgets.addSlot(this.getOutputs().get(0), 170, 76).drawBack(false).recipeContext(this);
	}
}
