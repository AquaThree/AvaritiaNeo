package net.byAqua3.avaritia.compat.rei.event;

import java.util.ArrayList;
import java.util.List;

import me.shedaniel.rei.api.common.display.Display;
import net.byAqua3.avaritia.Avaritia;
import net.byAqua3.avaritia.compat.rei.display.DisplayCompressorRecipe;
import net.byAqua3.avaritia.compat.rei.display.DisplayExtremeRecipe;
import net.byAqua3.avaritia.compat.rei.network.PacketDisplaySync;
import net.byAqua3.avaritia.recipe.RecipeCompressor;
import net.byAqua3.avaritia.recipe.RecipeExtremeCrafting;
import net.byAqua3.avaritia.recipe.RecipeExtremeShaped;
import net.byAqua3.avaritia.recipe.RecipeExtremeShapeless;
import net.byAqua3.avaritia.util.AvaritiaRecipeUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.crafting.RecipeManager;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.network.PacketDistributor;

public class AvaritiaREIEvent {

	@SubscribeEvent
	public void onDatapackSync(OnDatapackSyncEvent event) {
		ServerPlayer player = event.getPlayer();
		List<Display> displays = new ArrayList<>();

		if (player == null) {
			RecipeManager recipeManager = event.getPlayerList().getServer().getRecipeManager();

			for (RecipeExtremeCrafting recipe : AvaritiaRecipeUtils.getExtremeCraftingRecipes(recipeManager)) {
				if (recipe instanceof RecipeExtremeShaped) {
					displays.add(new DisplayExtremeRecipe.Shaped((RecipeExtremeShaped) recipe));
				} else if (recipe instanceof RecipeExtremeShapeless) {
					displays.add(new DisplayExtremeRecipe.Shapeless((RecipeExtremeShapeless) recipe));
				}
		    }
			for (RecipeCompressor recipe : AvaritiaRecipeUtils.getCompressorRecipes(recipeManager)) {
				displays.add(new DisplayCompressorRecipe.Shapeless(recipe));
			}
			Avaritia.LOGGER.info("DisplayExtremeRecipe: " + displays.size());
			PacketDistributor.sendToAllPlayers(new PacketDisplaySync(displays));
		} else {
			RecipeManager recipeManager = player.getServer().getRecipeManager();

			for (RecipeExtremeCrafting recipe : AvaritiaRecipeUtils.getExtremeCraftingRecipes(recipeManager)) {
				if (recipe instanceof RecipeExtremeShaped) {
					displays.add(new DisplayExtremeRecipe.Shaped((RecipeExtremeShaped) recipe));
				} else if (recipe instanceof RecipeExtremeShapeless) {
					displays.add(new DisplayExtremeRecipe.Shapeless((RecipeExtremeShapeless) recipe));
				}
			}
			for (RecipeCompressor recipe : AvaritiaRecipeUtils.getCompressorRecipes(recipeManager)) {
				displays.add(new DisplayCompressorRecipe.Shapeless(recipe));
			}
			Avaritia.LOGGER.info("DisplayCompressorRecipe: " + displays.size());
			PacketDistributor.sendToPlayer(player, new PacketDisplaySync(displays));
		}
	}

}
