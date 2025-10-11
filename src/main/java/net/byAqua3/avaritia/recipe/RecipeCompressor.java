package net.byAqua3.avaritia.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;

import net.byAqua3.avaritia.loader.AvaritiaRecipes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class RecipeCompressor implements RecipeExtremeCrafting {
	public final String group;
	public final ItemStack result;
	public final int cost;
	public final NonNullList<Ingredient> ingredients;
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
		StackedContents stackedcontents = new StackedContents();
		java.util.List<ItemStack> inputs = new java.util.ArrayList<>();
		int i = 0;

		for (int j = 0; j < container.size(); ++j) {
			ItemStack itemstack = container.getItem(j);
			if (!itemstack.isEmpty()) {
				++i;
				if (this.isSimple) {
					stackedcontents.accountStack(itemstack, 1);
				} else {
					inputs.add(itemstack);
				}
			}
		}

		return i == this.ingredients.size() && (this.isSimple ? stackedcontents.canCraft(this, null) : net.neoforged.neoforge.common.util.RecipeMatcher.findMatches(inputs, this.ingredients) != null);
	}

	@Override
	public boolean showNotification() {
		return false;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return width * height >= this.ingredients.size();
	}

	@Override
	public ItemStack assemble(CraftingInput container, HolderLookup.Provider registryAccess) {
		return this.result.copy();
	}

	@Override
	public String getGroup() {
		return this.group;
	}

	@Override
	public ItemStack getResultItem(HolderLookup.Provider registryAccess) {
		return this.result;
	}

	public int getCost() {
		return this.cost;
	}

	@Override
	public NonNullList<Ingredient> getIngredients() {
		return this.ingredients;
	}

	@Override
	public RecipeType<?> getType() {
		return AvaritiaRecipes.COMPRESSOR.get();
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return AvaritiaRecipes.COMPRESSOR_RECIPE.get();
	}

	public static class Serializer implements RecipeSerializer<RecipeCompressor> {
		public static final MapCodec<RecipeCompressor> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(Codec.STRING.optionalFieldOf("group", "").forGetter(recipe -> recipe.group), ItemStack.STRICT_CODEC.fieldOf("result").forGetter(recipe -> recipe.result), Codec.INT.fieldOf("cost").forGetter(recipe -> recipe.cost), Ingredient.CODEC_NONEMPTY.listOf().fieldOf("ingredients").forGetter(recipe -> recipe.ingredients)).apply(instance, RecipeCompressor::new));
		public static final StreamCodec<RegistryFriendlyByteBuf, RecipeCompressor> STREAM_CODEC = StreamCodec.of(RecipeCompressor.Serializer::toNetwork, RecipeCompressor.Serializer::fromNetwork);

		@Override
		public MapCodec<RecipeCompressor> codec() {
			return CODEC;
		}

		@Override
		public StreamCodec<RegistryFriendlyByteBuf, RecipeCompressor> streamCodec() {
			return STREAM_CODEC;
		}

		private static RecipeCompressor fromNetwork(RegistryFriendlyByteBuf friendlyByteBuf) {
			String group = friendlyByteBuf.readUtf();
			int cost = friendlyByteBuf.readInt();
			int i = friendlyByteBuf.readVarInt();
			NonNullList<Ingredient> ingredients = NonNullList.withSize(i, Ingredient.EMPTY);

			for (int j = 0; j < ingredients.size(); ++j) {
				ingredients.set(j, Ingredient.CONTENTS_STREAM_CODEC.decode(friendlyByteBuf));
			}

			ItemStack itemStack = ItemStack.STREAM_CODEC.decode(friendlyByteBuf);
			return new RecipeCompressor(group, itemStack, cost, ingredients);
		}

		private static void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, RecipeCompressor recipe) {
			friendlyByteBuf.writeUtf(recipe.group);
			friendlyByteBuf.writeInt(recipe.cost);
			friendlyByteBuf.writeVarInt(recipe.ingredients.size());

			for (Ingredient ingredient : recipe.ingredients) {
				Ingredient.CONTENTS_STREAM_CODEC.encode(friendlyByteBuf, ingredient);
			}

			ItemStack.STREAM_CODEC.encode(friendlyByteBuf, recipe.result);
		}
	}
}
