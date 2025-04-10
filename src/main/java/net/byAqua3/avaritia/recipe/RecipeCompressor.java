package net.byAqua3.avaritia.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;

import javax.annotation.Nullable;

import net.byAqua3.avaritia.loader.AvaritiaRecipes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class RecipeCompressor implements RecipeExtremeCrafting {
	public final String group;
	public final ItemStack result;
	public final int cost;
	public final NonNullList<Ingredient> ingredients;
	@Nullable
	private PlacementInfo placementInfo;
	private final boolean isSimple;

	public RecipeCompressor(String group, ItemStack result, int cost, List<Ingredient> ingredients) {
		this.group = group;
		this.result = result;
		this.cost = cost;
		this.ingredients = NonNullList.copyOf(ingredients);
		this.isSimple = ingredients.stream().allMatch(Ingredient::isSimple);
	}

	@Override
	public boolean matches(CraftingInput container, Level level) {
		if (container.ingredientCount() != this.ingredients.size()) {
			return false;
		} else if (!isSimple) {
			var nonEmptyItems = new java.util.ArrayList<ItemStack>(container.ingredientCount());
			for (var item : container.items())
				if (!item.isEmpty())
					nonEmptyItems.add(item);
			return net.neoforged.neoforge.common.util.RecipeMatcher.findMatches(nonEmptyItems,
					this.ingredients) != null;
		} else {
			return container.size() == 1 && this.ingredients.size() == 1
					? this.ingredients.getFirst().test(container.getItem(0))
					: container.stackedContents().canCraft(this, null);
		}
	}

	@Override
	public boolean showNotification() {
		return false;
	}

	@Override
	public ItemStack assemble(CraftingInput container, HolderLookup.Provider registryAccess) {
		return this.getResultItem(registryAccess).copy();
	}

	@Override
	public ItemStack getResultItem(HolderLookup.Provider registryAccess) {
		return this.result;
	}

	public int getCost() {
		return this.cost;
	}

	@Override
	public RecipeBookCategory recipeBookCategory() {
		return null;
	}

	@Override
	public PlacementInfo placementInfo() {
		if (this.placementInfo == null) {
			this.placementInfo = PlacementInfo.create(this.ingredients);
		}
		return this.placementInfo;
	}

	@Override
	public RecipeType<? extends Recipe<CraftingInput>> getType() {
		return AvaritiaRecipes.COMPRESSOR.get();
	}

	@Override
	public RecipeSerializer<? extends Recipe<CraftingInput>> getSerializer() {
		return AvaritiaRecipes.COMPRESSOR_RECIPE.get();
	}

	public static class Serializer implements RecipeSerializer<RecipeCompressor> {
		public static final MapCodec<RecipeCompressor> CODEC = RecordCodecBuilder
				.mapCodec(instance -> instance
						.group(Codec.STRING.optionalFieldOf("group", "").forGetter(recipe -> recipe.group),
								ItemStack.STRICT_CODEC.fieldOf("result").forGetter(recipe -> recipe.result),
								Codec.INT.optionalFieldOf("cost", 0).forGetter(recipe -> recipe.cost),
								Ingredient.CODEC.listOf().fieldOf("ingredients")
										.forGetter(recipe -> recipe.ingredients))
						.apply(instance, RecipeCompressor::new));
		public static final StreamCodec<RegistryFriendlyByteBuf, RecipeCompressor> STREAM_CODEC = StreamCodec.composite(
				ByteBufCodecs.STRING_UTF8, recipe -> recipe.group, ItemStack.STREAM_CODEC, recipe -> recipe.result,
				ByteBufCodecs.INT, recipe -> recipe.cost, Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()),
				recipe -> recipe.ingredients, RecipeCompressor::new);

		@Override
		public MapCodec<RecipeCompressor> codec() {
			return CODEC;
		}

		@Override
		public StreamCodec<RegistryFriendlyByteBuf, RecipeCompressor> streamCodec() {
			return STREAM_CODEC;
		}
	}
}
