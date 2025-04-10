package net.byAqua3.avaritia.compat.rei.display;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.display.DisplaySerializer;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.byAqua3.avaritia.compat.rei.AvaritiaREIPlugin;
import net.byAqua3.avaritia.recipe.RecipeExtremeCrafting;
import net.byAqua3.avaritia.recipe.RecipeExtremeShaped;
import net.byAqua3.avaritia.recipe.RecipeExtremeShapeless;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.neoforged.neoforge.common.crafting.DataComponentIngredient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.jetbrains.annotations.Nullable;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class DisplayExtremeRecipe implements Display {
	private RecipeExtremeCrafting recipe;

	public DisplayExtremeRecipe(RecipeExtremeCrafting recipe) {
		this.recipe = recipe;
	}

	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return AvaritiaREIPlugin.EXTREME_CRAFTING;
	}

	@SuppressWarnings("deprecation")
	@Override
	public List<EntryIngredient> getInputEntries() {
		if (!this.isShapeless()) {
			List<EntryIngredient> ingredients = new ArrayList<>();
			RecipeExtremeShaped recipe = (RecipeExtremeShaped) this.recipe;

			for (Optional<Ingredient> optional : recipe.pattern.ingredients()) {
				if (optional.isPresent()) {
					ingredients.add(EntryIngredients.ofIngredient(optional.get()));
				} else {
					ingredients.add(EntryIngredient.empty());
				}
			}
			return ingredients;
		} else {
			List<EntryIngredient> ingredients = new ArrayList<>();
			for (Ingredient ingredient : this.recipe.placementInfo().ingredients()) {
				if(ingredient.getCustomIngredient() != null && ingredient.getCustomIngredient() instanceof DataComponentIngredient) {
					DataComponentIngredient dataComponentIngredient = (DataComponentIngredient) ingredient.getCustomIngredient();
					List<ItemStack> itemStacks = dataComponentIngredient.items().map(holder -> new ItemStack(holder, 1, dataComponentIngredient.components().asPatch())).toList();
					ingredients.add(EntryIngredients.ofItemStacks(itemStacks));
				} else {
					List<ItemStack> itemStacks = ingredient.items().map(holder -> new ItemStack(holder)).toList();
					ingredients.add(EntryIngredients.ofItemStacks(itemStacks));
				}
			}
			return ingredients;
		}
	}

	@Override
	public List<EntryIngredient> getOutputEntries() {
		return Collections.singletonList(EntryIngredients.of(recipe.getResultItem(RegistryAccess.EMPTY)));
	}

	public RecipeExtremeCrafting getRecipe() {
		return this.recipe;
	}

	public boolean isShapeless() {
		return this.recipe instanceof RecipeExtremeShapeless;
	}

	@Override
	public Optional<ResourceLocation> getDisplayLocation() {
		return Optional.of(AvaritiaREIPlugin.EXTREME_CRAFTING.getIdentifier());
	}

	@Override
	public @Nullable DisplaySerializer<? extends Display> getSerializer() {
		return null;
	}

	public static class Shaped extends DisplayExtremeRecipe {
		public static final DisplaySerializer<Shaped> SERIALIZER = DisplaySerializer
				.of(RecordCodecBuilder.mapCodec(instance -> instance
						.group(Codec.STRING.optionalFieldOf("group", "").forGetter(recipe -> recipe.group),
								ShapedRecipePattern.MAP_CODEC.forGetter(recipe -> recipe.pattern),
								ItemStack.STRICT_CODEC.fieldOf("result").forGetter(recipe -> recipe.result))
						.apply(instance, Shaped::new)), StreamCodec.of(Shaped::toNetwork, Shaped::fromNetwork), false);

		public final String group;
		public final ShapedRecipePattern pattern;
		public final ItemStack result;

		public Shaped(RecipeExtremeShaped recipe) {
			super(recipe);
			this.group = recipe.group;
			this.pattern = recipe.pattern;
			this.result = recipe.result;
		}

		public Shaped(String group, ShapedRecipePattern pattern, ItemStack result) {
			super(new RecipeExtremeShaped(group, pattern, result));
			this.group = group;
			this.pattern = pattern;
			this.result = result;
		}

		private static Shaped fromNetwork(RegistryFriendlyByteBuf friendlyByteBuf) {
			String group = friendlyByteBuf.readUtf();
			ShapedRecipePattern pattern = ShapedRecipePattern.STREAM_CODEC.decode(friendlyByteBuf);
			ItemStack itemStack = ItemStack.STREAM_CODEC.decode(friendlyByteBuf);
			return new Shaped(group, pattern, itemStack);
		}

		private static void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, Shaped recipe) {
			friendlyByteBuf.writeUtf(recipe.group);
			ShapedRecipePattern.STREAM_CODEC.encode(friendlyByteBuf, recipe.pattern);
			ItemStack.STREAM_CODEC.encode(friendlyByteBuf, recipe.result);
		}

		@Override
		public DisplaySerializer<? extends Display> getSerializer() {
			return SERIALIZER;
		}
	}

	public static class Shapeless extends DisplayExtremeRecipe {
		public static final DisplaySerializer<Shapeless> SERIALIZER = DisplaySerializer.of(
				RecordCodecBuilder.mapCodec(instance -> instance.group(
						Codec.STRING.optionalFieldOf("group", "").forGetter(recipe -> recipe.group),
						ItemStack.STRICT_CODEC.fieldOf("result").forGetter(recipe -> recipe.result),
						Ingredient.CODEC.listOf().fieldOf("ingredients").forGetter(recipe -> recipe.ingredients),
						Codec.BOOL.optionalFieldOf("singularities", false).forGetter(recipe -> recipe.hasSingularities))
						.apply(instance, Shapeless::new)),
				StreamCodec.composite(ByteBufCodecs.STRING_UTF8, recipe -> recipe.group, ItemStack.STREAM_CODEC,
						recipe -> recipe.result, Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()),
						recipe -> recipe.ingredients, ByteBufCodecs.BOOL, recipe -> recipe.hasSingularities,
						Shapeless::new),
				false);

		public String group;
		public ItemStack result;
		public List<Ingredient> ingredients;
		public boolean hasSingularities;

		public Shapeless(RecipeExtremeShapeless recipe) {
			super(recipe);
			this.group = recipe.group;
			this.result = recipe.result;
			this.ingredients = NonNullList.copyOf(recipe.ingredients);
			this.hasSingularities = recipe.hasSingularities;
		}

		public Shapeless(String group, ItemStack result, List<Ingredient> ingredients, boolean hasSingularities) {
			super(new RecipeExtremeShapeless(group, result, ingredients, hasSingularities));
			this.group = group;
			this.result = result;
			this.ingredients = NonNullList.copyOf(ingredients);
			this.hasSingularities = hasSingularities;
		}

		@Override
		public DisplaySerializer<? extends Display> getSerializer() {
			return SERIALIZER;
		}
	}
}