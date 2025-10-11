package net.byAqua3.avaritia.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.byAqua3.avaritia.loader.AvaritiaRecipes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.level.Level;

public class RecipeExtremeShaped implements RecipeExtremeCrafting {

	public final String group;
	public final ShapedRecipePattern pattern;;
	public final ItemStack result;

	public RecipeExtremeShaped(String group, ShapedRecipePattern pattern, ItemStack result) {
		this.group = group;
		this.pattern = pattern;
		this.result = result;
	}

	@Override
	public boolean matches(CraftingInput container, Level level) {
		return this.pattern.matches(container);
	}

	@Override
	public boolean showNotification() {
		return false;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return (width >= this.pattern.width() && height >= this.pattern.height());
	}

	@Override
	public ItemStack assemble(CraftingInput container, HolderLookup.Provider registryAccess) {
		return this.getResultItem(registryAccess).copy();
	}

	@Override
	public String getGroup() {
		return this.group;
	}

	public int getWidth() {
		return this.pattern.width();
	}

	public int getHeight() {
		return this.pattern.height();
	}

	@Override
	public ItemStack getResultItem(HolderLookup.Provider registryAccess) {
		return this.result;
	}

	@Override
	public NonNullList<Ingredient> getIngredients() {
		return this.pattern.ingredients();
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return AvaritiaRecipes.EXTREME_SHAPED_RECIPE.get();
	}
	
	@Override
    public boolean isIncomplete() {
        NonNullList<Ingredient> ingredients = this.getIngredients();
        return ingredients.isEmpty() || ingredients.stream().filter(ingredient -> !ingredient.isEmpty()).anyMatch(Ingredient::hasNoItems);
    }

	public static class Serializer implements RecipeSerializer<RecipeExtremeShaped> {

		public static final MapCodec<RecipeExtremeShaped> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
				.group(Codec.STRING.optionalFieldOf("group", "").forGetter(recipe -> recipe.group),
						ShapedRecipePattern.MAP_CODEC.forGetter(recipe -> recipe.pattern),
						ItemStack.STRICT_CODEC.fieldOf("result").forGetter(recipe -> recipe.result))
				.apply(instance, RecipeExtremeShaped::new));
		public static final StreamCodec<RegistryFriendlyByteBuf, RecipeExtremeShaped> STREAM_CODEC = StreamCodec
				.of(RecipeExtremeShaped.Serializer::toNetwork, RecipeExtremeShaped.Serializer::fromNetwork);

		@Override
		public MapCodec<RecipeExtremeShaped> codec() {
			return CODEC;
		}

		@Override
		public StreamCodec<RegistryFriendlyByteBuf, RecipeExtremeShaped> streamCodec() {
			return STREAM_CODEC;
		}

		private static RecipeExtremeShaped fromNetwork(RegistryFriendlyByteBuf friendlyByteBuf) {
			String group = friendlyByteBuf.readUtf();
			ShapedRecipePattern pattern = ShapedRecipePattern.STREAM_CODEC.decode(friendlyByteBuf);
			ItemStack itemStack = ItemStack.STREAM_CODEC.decode(friendlyByteBuf);
			return new RecipeExtremeShaped(group, pattern, itemStack);
		}

		private static void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, RecipeExtremeShaped recipe) {
			friendlyByteBuf.writeUtf(recipe.group);
			ShapedRecipePattern.STREAM_CODEC.encode(friendlyByteBuf, recipe.pattern);
			ItemStack.STREAM_CODEC.encode(friendlyByteBuf, recipe.result);

		}
	}}
