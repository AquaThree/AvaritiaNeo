package net.byAqua3.avaritia.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.byAqua3.avaritia.item.ItemInfinitySingularity;
import net.byAqua3.avaritia.item.ItemJsonSingularity;
import net.byAqua3.avaritia.item.ItemSingularity;
import net.byAqua3.avaritia.loader.AvaritiaDataComponents;
import net.byAqua3.avaritia.loader.AvaritiaRecipes;
import net.byAqua3.avaritia.loader.AvaritiaSingularities;
import net.byAqua3.avaritia.singularity.Singularity;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.crafting.DataComponentIngredient;

public class RecipeExtremeShapeless implements RecipeExtremeCrafting {
	public final String group;
	public final ItemStack result;
	public final List<Ingredient> ingredients;
	public final boolean hasSingularities;
	@Nullable
	private PlacementInfo placementInfo;
	private final boolean isSimple;

	public RecipeExtremeShapeless(String group, ItemStack result, List<Ingredient> ingredients,
			boolean hasSingularities) {
		this.group = group;
		this.result = result;
		this.ingredients = NonNullList.copyOf(ingredients);
		this.hasSingularities = hasSingularities;
		this.isSimple = hasSingularities ? false : this.getIngredients().stream().allMatch(Ingredient::isSimple);
		
	}

	@Override
	public boolean matches(CraftingInput container, Level level) {
		if (container.ingredientCount() != this.getIngredients().size()) {
			return false;
		} else if (!isSimple) {
			var nonEmptyItems = new java.util.ArrayList<ItemStack>(container.ingredientCount());
			for (var item : container.items())
				if (!item.isEmpty())
					nonEmptyItems.add(item);
			return net.neoforged.neoforge.common.util.RecipeMatcher.findMatches(nonEmptyItems,
					this.getIngredients()) != null;
		} else {
			return container.size() == 1 && this.getIngredients().size() == 1
					? this.getIngredients().getFirst().test(container.getItem(0))
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

	public List<Ingredient> getIngredients() {
		if (this.hasSingularities) {
			List<Ingredient> ingredients = new ArrayList<>();
			ingredients.addAll(this.ingredients);
			for (int i = 0; i < BuiltInRegistries.ITEM.size(); i++) {
				Item item = BuiltInRegistries.ITEM.byId(i);
				if (item instanceof ItemSingularity && !(item instanceof ItemInfinitySingularity)) {
					if (!(item instanceof ItemJsonSingularity)) {
						ingredients.add(DataComponentIngredient.of(false, new ItemStack(item)));
					} else {
						for(Singularity singularity : AvaritiaSingularities.getInstance().getSingularities()) {
							ingredients.add(DataComponentIngredient.of(true, AvaritiaDataComponents.SINGULARITY_ID.get(), singularity.getId(), item));
						}
					}
				}
			}
			return NonNullList.copyOf(ingredients);
		}
		return this.ingredients;
	}

	@Override
	public RecipeBookCategory recipeBookCategory() {
		return null;
	}

	@Override
	public PlacementInfo placementInfo() {
		if (this.placementInfo == null) {
			this.placementInfo = PlacementInfo.create(this.getIngredients());
		}
		return this.placementInfo;
	}

	@Override
	public RecipeSerializer<? extends Recipe<CraftingInput>> getSerializer() {
		return AvaritiaRecipes.EXTREME_SHAPELESS_RECIPE.get();
	}

	public static class Serializer implements RecipeSerializer<RecipeExtremeShapeless> {
		public static final MapCodec<RecipeExtremeShapeless> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
				.group(Codec.STRING.optionalFieldOf("group", "").forGetter(recipe -> recipe.group),
						ItemStack.STRICT_CODEC.fieldOf("result").forGetter(recipe -> recipe.result),
						Ingredient.CODEC.listOf().fieldOf("ingredients").forGetter(recipe -> recipe.ingredients),
						Codec.BOOL.optionalFieldOf("singularities", false).forGetter(recipe -> recipe.hasSingularities))
				.apply(instance, RecipeExtremeShapeless::new));

		public static final StreamCodec<RegistryFriendlyByteBuf, RecipeExtremeShapeless> STREAM_CODEC = StreamCodec
				.composite(ByteBufCodecs.STRING_UTF8, recipe -> recipe.group, ItemStack.STREAM_CODEC,
						recipe -> recipe.result, Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()),
						recipe -> recipe.ingredients, ByteBufCodecs.BOOL, recipe -> recipe.hasSingularities,
						RecipeExtremeShapeless::new);

		@Override
		public MapCodec<RecipeExtremeShapeless> codec() {
			return CODEC;
		}

		@Override
		public StreamCodec<RegistryFriendlyByteBuf, RecipeExtremeShapeless> streamCodec() {
			return STREAM_CODEC;
		}
	}
}
