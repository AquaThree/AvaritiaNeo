package net.byAqua3.avaritia.compat.jei;

import java.util.List;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import mezz.jei.api.registration.IRuntimeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import mezz.jei.api.runtime.IIngredientManager;
import net.byAqua3.avaritia.Avaritia;
import net.byAqua3.avaritia.compat.jei.category.CategoryCollectorRecipe;
import net.byAqua3.avaritia.compat.jei.category.CategoryCompressorRecipe;
import net.byAqua3.avaritia.compat.jei.category.CategoryExtremeRecipe;
import net.byAqua3.avaritia.gui.GuiExtremeCraftingTable;
import net.byAqua3.avaritia.gui.GuiNeutroniumCompressor;
import net.byAqua3.avaritia.inventory.MenuExtremeCrafting;
import net.byAqua3.avaritia.inventory.MenuNeutroniumCompressor;
import net.byAqua3.avaritia.loader.AvaritiaBlocks;
import net.byAqua3.avaritia.loader.AvaritiaDataComponents;
import net.byAqua3.avaritia.loader.AvaritiaItems;
import net.byAqua3.avaritia.loader.AvaritiaMenus;
import net.byAqua3.avaritia.recipe.RecipeCollector;
import net.byAqua3.avaritia.recipe.RecipeCompressor;
import net.byAqua3.avaritia.recipe.RecipeExtremeCrafting;
import net.byAqua3.avaritia.util.RecipeUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

@JeiPlugin
public class AvaritiaJEIPlugin implements IModPlugin {

	public static final ResourceLocation PLUGIN_UID = ResourceLocation.tryBuild(Avaritia.MODID, "plugin");

	public static final RecipeType<RecipeExtremeCrafting> EXTREME_CRAFTING = RecipeType.create(Avaritia.MODID, "extreme_crafting", RecipeExtremeCrafting.class);
	public static final RecipeType<RecipeCompressor> COMPRESSOR = RecipeType.create(Avaritia.MODID, "compressor", RecipeCompressor.class);
	public static final RecipeType<RecipeCollector> COLLECTOR = RecipeType.create(Avaritia.MODID, "collector", RecipeCollector.class);

	@Override
	public ResourceLocation getPluginUid() {
		return PLUGIN_UID;
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registration) {
		registration.addRecipeCategories(new CategoryExtremeRecipe(registration.getJeiHelpers().getGuiHelper()));
		registration.addRecipeCategories(new CategoryCompressorRecipe(registration.getJeiHelpers().getGuiHelper()));
		registration.addRecipeCategories(new CategoryCollectorRecipe(registration.getJeiHelpers().getGuiHelper()));
	}

	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		Minecraft mc = Minecraft.getInstance();
		ClientLevel level = mc.level;
		registration.addRecipes(EXTREME_CRAFTING, RecipeUtils.getExtremeCraftingRecipes(level));
		registration.addRecipes(COMPRESSOR, RecipeUtils.getCompressorRecipes(level));
		registration.addRecipes(COLLECTOR, RecipeUtils.getCollectorRecipes());
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
		registration.addRecipeCatalyst(new ItemStack(AvaritiaBlocks.COMPRESSED_CRAFTING_TABLE_ITEM.get()), RecipeTypes.CRAFTING);
		registration.addRecipeCatalyst(new ItemStack(AvaritiaBlocks.DOUBLE_COMPRESSED_CRAFTING_TABLE_ITEM.get()), RecipeTypes.CRAFTING);
		registration.addRecipeCatalyst(new ItemStack(AvaritiaBlocks.EXTREME_CRAFTING_TABLE_ITEM.get()), EXTREME_CRAFTING);
		registration.addRecipeCatalyst(new ItemStack(AvaritiaBlocks.COMPRESSOR_ITEM.get()), COMPRESSOR);
		registration.addRecipeCatalyst(new ItemStack(AvaritiaBlocks.NEUTRON_COLLECTOR_ITEM.get()), COLLECTOR);
	}

	@Override
	public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
		registration.addRecipeTransferHandler(MenuExtremeCrafting.class, AvaritiaMenus.EXTREME_CRAFTING.get(), EXTREME_CRAFTING, 1, 81, 82, 36);
		registration.addRecipeTransferHandler(MenuNeutroniumCompressor.class, AvaritiaMenus.COMPRESSOR.get(), COMPRESSOR, 0, 1, 2, 36);
	}

	@Override
	public void registerGuiHandlers(IGuiHandlerRegistration registration) {
		registration.addRecipeClickArea(GuiExtremeCraftingTable.class, 175, 79, 28, 26, EXTREME_CRAFTING);
		registration.addRecipeClickArea(GuiNeutroniumCompressor.class, 62, 35, 22, 15, COMPRESSOR);
	}

	@Override
	public void registerItemSubtypes(ISubtypeRegistration registration) {
		registration.registerSubtypeInterpreter(AvaritiaItems.JSON_SINGULARITY.get(), (stack, context) -> {
			String singularityId = stack.getOrDefault(AvaritiaDataComponents.SINGULARITY_ID, "null");
			return singularityId;
		});
	}

	@Override
	public void registerRuntime(IRuntimeRegistration registration) {
		IIngredientManager ingredientManager = registration.getIngredientManager();
		ingredientManager.removeIngredientsAtRuntime(VanillaTypes.ITEM_STACK, List.of(new ItemStack(AvaritiaItems.MATTER_CLUSTER.get())));
	}
}
