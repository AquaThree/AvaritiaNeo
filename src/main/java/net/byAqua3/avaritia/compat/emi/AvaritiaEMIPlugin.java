package net.byAqua3.avaritia.compat.emi;

import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories;
import dev.emi.emi.api.stack.Comparison;
import dev.emi.emi.api.stack.EmiStack;
import net.byAqua3.avaritia.Avaritia;
import net.byAqua3.avaritia.compat.emi.handler.RecipeHandlerCompressor;
import net.byAqua3.avaritia.compat.emi.handler.RecipeHandlerExtremeCrafting;
import net.byAqua3.avaritia.compat.emi.recipe.EMIRecipeCollector;
import net.byAqua3.avaritia.compat.emi.recipe.EMIRecipeCompressor;
import net.byAqua3.avaritia.compat.emi.recipe.EMIRecipeExtremeCrafting;
import net.byAqua3.avaritia.loader.AvaritiaBlocks;
import net.byAqua3.avaritia.loader.AvaritiaItems;
import net.byAqua3.avaritia.loader.AvaritiaMenus;
import net.byAqua3.avaritia.recipe.RecipeCollector;
import net.byAqua3.avaritia.recipe.RecipeCompressor;
import net.byAqua3.avaritia.recipe.RecipeExtremeCrafting;
import net.byAqua3.avaritia.util.RecipeUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;

@EmiEntrypoint
public class AvaritiaEMIPlugin implements EmiPlugin {

	public static final EmiRecipeCategory EXTREME_CRAFTING = new EmiRecipeCategory(ResourceLocation.tryBuild(Avaritia.MODID, "extreme_crafting"), EmiStack.of(AvaritiaBlocks.EXTREME_CRAFTING_TABLE_ITEM.get()));
	public static final EmiRecipeCategory COMPRESSOR = new EmiRecipeCategory(ResourceLocation.tryBuild(Avaritia.MODID, "compressor"), EmiStack.of(AvaritiaBlocks.COMPRESSOR_ITEM.get()));
	public static final EmiRecipeCategory COLLECTOR = new EmiRecipeCategory(ResourceLocation.tryBuild(Avaritia.MODID, "collector"), EmiStack.of(AvaritiaBlocks.NEUTRON_COLLECTOR_ITEM.get()));

	public void registerCategories(EmiRegistry registry) {
		registry.addCategory(EXTREME_CRAFTING);
		registry.addCategory(COMPRESSOR);
		registry.addCategory(COLLECTOR);
	}

	public void registerRecipes(EmiRegistry registry) {
		RecipeManager recipeManager = registry.getRecipeManager();

		for (RecipeExtremeCrafting recipe : RecipeUtils.getExtremeCraftingRecipes(recipeManager)) {
			registry.addRecipe(new EMIRecipeExtremeCrafting(recipeManager, recipe));
		}
		for (RecipeCompressor recipe : RecipeUtils.getCompressorRecipes(recipeManager)) {
			registry.addRecipe(new EMIRecipeCompressor(recipeManager, recipe));
		}
		for (RecipeCollector recipe : RecipeUtils.getCollectorRecipes()) {
			registry.addRecipe(new EMIRecipeCollector(recipeManager, recipe));
		}
	}

	public void registerWorkstations(EmiRegistry registry) {
		registry.addWorkstation(VanillaEmiRecipeCategories.CRAFTING, EmiStack.of(AvaritiaBlocks.COMPRESSED_CRAFTING_TABLE_ITEM.get()));
		registry.addWorkstation(VanillaEmiRecipeCategories.CRAFTING, EmiStack.of(AvaritiaBlocks.DOUBLE_COMPRESSED_CRAFTING_TABLE_ITEM.get()));
		registry.addWorkstation(EXTREME_CRAFTING, EmiStack.of(AvaritiaBlocks.EXTREME_CRAFTING_TABLE_ITEM.get()));
		registry.addWorkstation(COMPRESSOR, EmiStack.of(AvaritiaBlocks.COMPRESSOR_ITEM.get()));
		registry.addWorkstation(COLLECTOR, EmiStack.of(AvaritiaBlocks.NEUTRON_COLLECTOR_ITEM.get()));
	}

	public void registerRecipeHandlers(EmiRegistry registry) {
		registry.addRecipeHandler(AvaritiaMenus.EXTREME_CRAFTING.get(), new RecipeHandlerExtremeCrafting());
		registry.addRecipeHandler(AvaritiaMenus.COMPRESSOR.get(), new RecipeHandlerCompressor());
	}
	
	public void registerDefaultComparisons(EmiRegistry registry) {
		registry.setDefaultComparison(AvaritiaItems.JSON_SINGULARITY.get(), Comparison.of((stack1, stack2) -> ItemStack.isSameItemSameComponents(stack1.getItemStack(), stack2.getItemStack())));
	}

	public void removeEmiStacks(EmiRegistry registry) {
		registry.removeEmiStacks(EmiStack.of(AvaritiaItems.MATTER_CLUSTER.get()));
	}

	@Override
	public void register(EmiRegistry registry) {
		this.registerCategories(registry);
		this.registerRecipes(registry);
		this.registerWorkstations(registry);
		this.registerRecipeHandlers(registry);
		this.registerDefaultComparisons(registry);
		this.removeEmiStacks(registry);
	}
}
