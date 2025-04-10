package net.byAqua3.avaritia.loader;

import net.byAqua3.avaritia.Avaritia;
import net.byAqua3.avaritia.recipe.RecipeCompressor;
import net.byAqua3.avaritia.recipe.RecipeExtremeCrafting;
import net.byAqua3.avaritia.recipe.RecipeExtremeShaped;
import net.byAqua3.avaritia.recipe.RecipeExtremeShapeless;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AvaritiaRecipes {
	
	public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(BuiltInRegistries.RECIPE_TYPE, Avaritia.MODID);
	public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, Avaritia.MODID);

	public static final DeferredHolder<RecipeType<?>, RecipeType<RecipeExtremeCrafting>> EXTREME_CRAFTING = RECIPE_TYPES.register("extreme_crafting", () -> new RecipeType<RecipeExtremeCrafting>() {});
	public static final DeferredHolder<RecipeType<?>, RecipeType<RecipeCompressor>> COMPRESSOR = RECIPE_TYPES.register("compressor", () -> new RecipeType<RecipeCompressor>() {});
	
	public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<RecipeExtremeShaped>> EXTREME_SHAPED_RECIPE = RECIPE_SERIALIZERS.register("extreme_shaped", RecipeExtremeShaped.Serializer::new);
	public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<RecipeExtremeShapeless>> EXTREME_SHAPELESS_RECIPE = RECIPE_SERIALIZERS.register("extreme_shapeless", RecipeExtremeShapeless.Serializer::new);
	public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<RecipeCompressor>> COMPRESSOR_RECIPE = RECIPE_SERIALIZERS.register("compressor", RecipeCompressor.Serializer::new);
	
	public static void registerRecipes(IEventBus modEventBus) {
		RECIPE_TYPES.register(modEventBus);
		RECIPE_SERIALIZERS.register(modEventBus);
		ShapedRecipePattern.setCraftingSize(9, 9);
	}
}
