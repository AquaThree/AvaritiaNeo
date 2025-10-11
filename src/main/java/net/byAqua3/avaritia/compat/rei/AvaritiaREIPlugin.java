package net.byAqua3.avaritia.compat.rei;

import java.util.List;

import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.entry.filtering.base.BasicFilteringRule;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry;
import me.shedaniel.rei.api.client.registry.transfer.TransferHandlerRegistry;
import me.shedaniel.rei.api.client.registry.transfer.simple.SimpleTransferHandler;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.forge.REIPluginClient;
import me.shedaniel.rei.plugin.common.BuiltinPlugin;
import net.byAqua3.avaritia.Avaritia;
import net.byAqua3.avaritia.compat.rei.category.CategoryCollectorRecipe;
import net.byAqua3.avaritia.compat.rei.category.CategoryCompressorRecipe;
import net.byAqua3.avaritia.compat.rei.category.CategoryExtremeRecipe;
import net.byAqua3.avaritia.compat.rei.display.DisplayCollectorRecipe;
import net.byAqua3.avaritia.compat.rei.display.DisplayCompressorRecipe;
import net.byAqua3.avaritia.compat.rei.display.DisplayExtremeRecipe;
import net.byAqua3.avaritia.compat.rei.transfer.TransferHandlerExtremeRecipe;
import net.byAqua3.avaritia.gui.GuiExtremeCraftingTable;
import net.byAqua3.avaritia.gui.GuiNeutroniumCompressor;
import net.byAqua3.avaritia.inventory.MenuExtremeCrafting;
import net.byAqua3.avaritia.inventory.MenuNeutroniumCompressor;
import net.byAqua3.avaritia.loader.AvaritiaBlocks;
import net.byAqua3.avaritia.loader.AvaritiaItems;
import net.byAqua3.avaritia.recipe.RecipeCollector;
import net.byAqua3.avaritia.recipe.RecipeCompressor;
import net.byAqua3.avaritia.recipe.RecipeExtremeCrafting;
import net.byAqua3.avaritia.util.RecipeUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;

@REIPluginClient
public class AvaritiaREIPlugin implements REIClientPlugin {

	public static final CategoryIdentifier<DisplayExtremeRecipe> EXTREME_CRAFTING = CategoryIdentifier.of(Avaritia.MODID, "extreme_crafting");
	public static final CategoryIdentifier<DisplayCompressorRecipe> COMPRESSOR = CategoryIdentifier.of(Avaritia.MODID, "compressor");
	public static final CategoryIdentifier<DisplayCollectorRecipe> COLLECTOR = CategoryIdentifier.of(Avaritia.MODID, "collector");

	@Override
	public void registerCategories(CategoryRegistry registry) {
		registry.add(new CategoryExtremeRecipe());
		registry.add(new CategoryCompressorRecipe());
		registry.add(new CategoryCollectorRecipe());
		registry.addWorkstations(BuiltinPlugin.CRAFTING, EntryStacks.of(AvaritiaBlocks.COMPRESSED_CRAFTING_TABLE_ITEM.get()));
		registry.addWorkstations(BuiltinPlugin.CRAFTING, EntryStacks.of(AvaritiaBlocks.DOUBLE_COMPRESSED_CRAFTING_TABLE_ITEM.get()));
		registry.addWorkstations(EXTREME_CRAFTING, EntryStacks.of(AvaritiaBlocks.EXTREME_CRAFTING_TABLE_ITEM.get()));
		registry.addWorkstations(COMPRESSOR, EntryStacks.of(AvaritiaBlocks.COMPRESSOR_ITEM.get()));
		registry.addWorkstations(COLLECTOR, EntryStacks.of(AvaritiaBlocks.NEUTRON_COLLECTOR_ITEM.get()));
	}

	@Override
	public void registerDisplays(DisplayRegistry registry) {
		Minecraft mc = Minecraft.getInstance();
		ClientLevel level = mc.level;
		for (RecipeExtremeCrafting recipe : RecipeUtils.getExtremeCraftingRecipes(level)) {
			registry.add(new DisplayExtremeRecipe(recipe));
		}
		for (RecipeCompressor recipe : RecipeUtils.getCompressorRecipes(level)) {
			registry.add(new DisplayCompressorRecipe(recipe));
		}
		for (RecipeCollector recipe : RecipeUtils.getCollectorRecipes()) {
			registry.add(new DisplayCollectorRecipe(recipe));
		}
	}

	@Override
	public void registerTransferHandlers(TransferHandlerRegistry registry) {
		registry.register(TransferHandlerExtremeRecipe.create(MenuExtremeCrafting.class, EXTREME_CRAFTING, new SimpleTransferHandler.IntRange(1, 82)));
		registry.register(SimpleTransferHandler.create(MenuNeutroniumCompressor.class, COMPRESSOR, new SimpleTransferHandler.IntRange(0, 1)));
	}

	@Override
	public void registerScreens(ScreenRegistry registry) {
		registry.registerContainerClickArea(new Rectangle(175, 79, 28, 26), GuiExtremeCraftingTable.class, EXTREME_CRAFTING);
		registry.registerContainerClickArea(new Rectangle(62, 35, 22, 15), GuiNeutroniumCompressor.class, COMPRESSOR);
	}

	@Override
	public void registerBasicEntryFiltering(BasicFilteringRule<?> rule) {
		rule.hide(List.of(EntryStacks.of(AvaritiaItems.MATTER_CLUSTER.get())));
	}
}
