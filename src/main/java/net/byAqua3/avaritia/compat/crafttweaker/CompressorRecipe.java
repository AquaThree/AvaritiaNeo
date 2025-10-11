package net.byAqua3.avaritia.compat.crafttweaker;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.action.recipe.ActionAddRecipe;
import com.blamejared.crafttweaker.api.action.recipe.ActionRemoveRecipe;
import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.ingredient.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.recipe.manager.base.IRecipeManager;

import org.openzen.zencode.java.ZenCodeType;

import net.byAqua3.avaritia.Avaritia;
import net.byAqua3.avaritia.loader.AvaritiaRecipes;
import net.byAqua3.avaritia.recipe.RecipeCompressor;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;

@ZenRegister
@ZenCodeType.Name("net.byAqua3.avaritia.CompressorRecipe")
public class CompressorRecipe implements IRecipeManager<RecipeCompressor> {
	
	public static CompressorRecipe recipeManager = new CompressorRecipe();
	
	@Override
    public RecipeType<RecipeCompressor> getRecipeType() {
        return AvaritiaRecipes.COMPRESSOR.get();
    }
	
	@ZenCodeType.Method
	public static void add(String name, String group, IIngredient ingredient, IItemStack stack, int cost) {
		NonNullList<Ingredient> ingredients = NonNullList.create();
		ingredients.add(ingredient.asVanillaIngredient());
		ItemStack itemStack = stack.getInternal();
		
        RecipeCompressor recipe = new RecipeCompressor(group, itemStack, cost, ingredients);
        RecipeHolder<RecipeCompressor> recipeHolder = new RecipeHolder<RecipeCompressor>(ResourceLocation.tryBuild(Avaritia.MODID, name), recipe);
        
        CraftTweakerAPI.apply(new ActionAddRecipe<RecipeCompressor>(recipeManager, recipeHolder));
    }
	
	@ZenCodeType.Method
	public static void remove(IItemStack stack) {
		ItemStack itemStack = stack.getInternal();
		
        CraftTweakerAPI.apply(new ActionRemoveRecipe<RecipeCompressor>(recipeManager, recipeHolder -> recipeHolder.value().getResultItem(RegistryAccess.EMPTY).getItem() == itemStack.getItem()));
    }
}
