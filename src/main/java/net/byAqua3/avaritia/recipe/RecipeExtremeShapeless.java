package net.byAqua3.avaritia.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.ArrayList;
import java.util.List;

import net.byAqua3.avaritia.item.ItemInfinitySingularity;
import net.byAqua3.avaritia.item.ItemJsonSingularity;
import net.byAqua3.avaritia.item.ItemSingularity;
import net.byAqua3.avaritia.loader.AvaritiaDataComponents;
import net.byAqua3.avaritia.loader.AvaritiaItems;
import net.byAqua3.avaritia.loader.AvaritiaRecipes;
import net.byAqua3.avaritia.loader.AvaritiaSingularities;
import net.byAqua3.avaritia.singularity.Singularity;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.crafting.DataComponentIngredient;

public class RecipeExtremeShapeless implements RecipeExtremeCrafting {
	public final String group;
	public final ItemStack result;
	public final NonNullList<Ingredient> ingredients;
	public final boolean hasSingularities;
	private final boolean isSimple;

	public RecipeExtremeShapeless(String group, ItemStack result, List<Ingredient> ingredients, boolean hasSingularities) {
		this.group = group;
		this.result = result;
		this.ingredients = NonNullList.copyOf(ingredients);
		this.hasSingularities = hasSingularities;
		this.isSimple = hasSingularities ? false : this.getIngredients().stream().allMatch(Ingredient::isSimple);
	}

	@Override
	public boolean matches(CraftingInput container, Level level) {
		StackedContents stackedContents = new StackedContents();
		java.util.List<ItemStack> inputs = new java.util.ArrayList<>();
		int i = 0;

		for (int j = 0; j < container.size(); ++j) {
			ItemStack itemStack = container.getItem(j);
			if (!itemStack.isEmpty()) {
				++i;
				if (this.isSimple) {
					stackedContents.accountStack(itemStack, 1);
				} else {
					inputs.add(itemStack);
				}
			}
		}

		return i == this.getIngredients().size() && (this.isSimple ? stackedContents.canCraft(this, null)
				: net.neoforged.neoforge.common.util.RecipeMatcher.findMatches(inputs, this.getIngredients()) != null);
	}

	@Override
	public boolean showNotification() {
		return false;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return width * height >= this.getIngredients().size();
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

	@Override
	public NonNullList<Ingredient> getIngredients() {
		if (this.hasSingularities) {
			List<Ingredient> ingredients = new ArrayList<>();
			ingredients.addAll(this.ingredients);
			for (int i = 0; i < BuiltInRegistries.ITEM.size(); i++) {
				Item item = BuiltInRegistries.ITEM.byId(i);
				if (item instanceof ItemSingularity && !(item instanceof ItemJsonSingularity) && !(item instanceof ItemInfinitySingularity)) {
					ingredients.add(Ingredient.of(item));
				}
			}
			for(Singularity singularity : AvaritiaSingularities.getInstance().getSingularities()) {
				ItemStack itemStack = new ItemStack(AvaritiaItems.JSON_SINGULARITY.get());
				itemStack.set(AvaritiaDataComponents.SINGULARITY_ID.get(), singularity.getId());
				ingredients.add(DataComponentIngredient.of(true, itemStack));
			}
			return NonNullList.copyOf(ingredients);
		}
		return this.ingredients;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return AvaritiaRecipes.EXTREME_SHAPELESS_RECIPE.get();
	}

	public static class Serializer implements RecipeSerializer<RecipeExtremeShapeless> {
		public static final MapCodec<RecipeExtremeShapeless> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
				.group(Codec.STRING.optionalFieldOf("group", "").forGetter(recipe -> recipe.group),
						ItemStack.STRICT_CODEC.fieldOf("result").forGetter(recipe -> recipe.result),
						Ingredient.CODEC_NONEMPTY.listOf().fieldOf("ingredients")
								.forGetter(recipe -> recipe.ingredients),
								Codec.BOOL.optionalFieldOf("singularities", false)
								.forGetter(recipe -> recipe.hasSingularities))
				.apply(instance, RecipeExtremeShapeless::new));
		public static final StreamCodec<RegistryFriendlyByteBuf, RecipeExtremeShapeless> STREAM_CODEC = StreamCodec
				.of(RecipeExtremeShapeless.Serializer::toNetwork, RecipeExtremeShapeless.Serializer::fromNetwork);

		@Override
		public MapCodec<RecipeExtremeShapeless> codec() {
			return CODEC;
		}
		
		@Override
		public StreamCodec<RegistryFriendlyByteBuf, RecipeExtremeShapeless> streamCodec() {
			return STREAM_CODEC;
		}

		private static RecipeExtremeShapeless fromNetwork(RegistryFriendlyByteBuf friendlyByteBuf) {
			String group = friendlyByteBuf.readUtf();
			int i = friendlyByteBuf.readVarInt();
			NonNullList<Ingredient> ingredients = NonNullList.withSize(i, Ingredient.EMPTY);

			for (int j = 0; j < ingredients.size(); ++j) {
				ingredients.set(j, Ingredient.CONTENTS_STREAM_CODEC.decode(friendlyByteBuf));
			}

			ItemStack itemStack = ItemStack.STREAM_CODEC.decode(friendlyByteBuf);
			boolean hasSingularities = friendlyByteBuf.readBoolean();

			return new RecipeExtremeShapeless(group, itemStack, ingredients, hasSingularities);
		}

		private static void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, RecipeExtremeShapeless recipe) {
			friendlyByteBuf.writeUtf(recipe.group);
			friendlyByteBuf.writeVarInt(recipe.ingredients.size());

			for (Ingredient ingredient : recipe.ingredients) {
				Ingredient.CONTENTS_STREAM_CODEC.encode(friendlyByteBuf, ingredient);
			}

			ItemStack.STREAM_CODEC.encode(friendlyByteBuf, recipe.result);
			friendlyByteBuf.writeBoolean(recipe.hasSingularities);
		}
	}
}
