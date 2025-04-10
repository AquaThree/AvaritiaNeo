package net.byAqua3.avaritia.compat.rei.display;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.display.DisplaySerializer;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.byAqua3.avaritia.compat.rei.AvaritiaREIPlugin;
import net.byAqua3.avaritia.recipe.RecipeCompressor;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.jetbrains.annotations.Nullable;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class DisplayCompressorRecipe implements Display {

	private RecipeCompressor recipe;

	public DisplayCompressorRecipe(RecipeCompressor recipe) {
		this.recipe = recipe;
	}

	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return AvaritiaREIPlugin.COMPRESSOR;
	}

	@Override
	public List<EntryIngredient> getInputEntries() {
		return EntryIngredients.ofIngredients(this.recipe.placementInfo().ingredients());
	}

	@Override
	public List<EntryIngredient> getOutputEntries() {
		return Collections.singletonList(EntryIngredients.of(this.recipe.getResultItem(RegistryAccess.EMPTY)));
	}

	public RecipeCompressor getRecipe() {
		return this.recipe;
	}

	public int getCost() {
		return this.recipe.getCost();
	}

	@Override
	public Optional<ResourceLocation> getDisplayLocation() {
		return Optional.of(AvaritiaREIPlugin.COMPRESSOR.getIdentifier());
	}

	@Override
	public @Nullable DisplaySerializer<? extends Display> getSerializer() {
		return null;
	}

	public static class Shapeless extends DisplayCompressorRecipe {
		public static final DisplaySerializer<Shapeless> SERIALIZER = DisplaySerializer.of(
				RecordCodecBuilder
						.mapCodec(instance -> instance
								.group(Codec.STRING.optionalFieldOf("group", "").forGetter(recipe -> recipe.group),
										ItemStack.STRICT_CODEC.fieldOf("result").forGetter(recipe -> recipe.result),
										Codec.INT.optionalFieldOf("cost", 0).forGetter(recipe -> recipe.cost),
										Ingredient.CODEC.listOf().fieldOf("ingredients")
												.forGetter(recipe -> recipe.ingredients))
								.apply(instance, Shapeless::new)),
				StreamCodec.composite(ByteBufCodecs.STRING_UTF8, recipe -> recipe.group, ItemStack.STREAM_CODEC,
						recipe -> recipe.result, ByteBufCodecs.INT, recipe -> recipe.cost,
						Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()), recipe -> recipe.ingredients,
						Shapeless::new),
				false);

		public String group;
		public ItemStack result;
		public int cost;
		public NonNullList<Ingredient> ingredients;
		
		public Shapeless(RecipeCompressor recipe) {
			super(recipe);
			this.group = recipe.group;
			this.result = recipe.result;
			this.cost = recipe.cost;
			this.ingredients = NonNullList.copyOf(recipe.ingredients);
		}

		public Shapeless(String group, ItemStack result, int cost, List<Ingredient> ingredients) {
			super(new RecipeCompressor(group, result, cost, ingredients));
			this.group = group;
			this.result = result;
			this.cost = cost;
			this.ingredients = NonNullList.copyOf(ingredients);
		}

		@Override
		public DisplaySerializer<? extends Display> getSerializer() {
			return SERIALIZER;
		}
	}
}