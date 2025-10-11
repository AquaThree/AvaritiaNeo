package net.byAqua3.avaritia.compat.crafttweaker;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.action.recipe.ActionAddRecipe;
import com.blamejared.crafttweaker.api.action.recipe.ActionRemoveRecipe;
import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.ingredient.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.recipe.manager.base.IRecipeManager;
import com.blamejared.crafttweaker.api.util.RecipeUtil;

import java.util.List;

import org.openzen.zencode.java.ZenCodeType;

import net.byAqua3.avaritia.loader.AvaritiaRecipes;
import net.byAqua3.avaritia.recipe.RecipeExtremeCrafting;
import net.byAqua3.avaritia.recipe.RecipeExtremeShaped;
import net.byAqua3.avaritia.recipe.RecipeExtremeShapeless;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;

@ZenRegister
@ZenCodeType.Name("net.byAqua3.avaritia.ExtremeRecipe")
public class ExtremeRecipe implements IRecipeManager<RecipeExtremeCrafting> {
	
	public static ExtremeRecipe recipeManager = new ExtremeRecipe();
	
	@Override
    public RecipeType<RecipeExtremeCrafting> getRecipeType() {
        return AvaritiaRecipes.EXTREME_CRAFTING.get();
    }
	
	@ZenCodeType.Method
	public static void addShaped(String name, String group, IIngredient[][] ingredients, IItemStack stack) {
		ItemStack itemStack = stack.getInternal();
		
		RecipeExtremeShaped recipe = new RecipeExtremeShaped(group, RecipeUtil.createPattern(ingredients), itemStack);
        RecipeHolder<RecipeExtremeCrafting> recipeHolder = new RecipeHolder<RecipeExtremeCrafting>(ResourceLocation.tryParse(name), recipe);
        
        CraftTweakerAPI.apply(new ActionAddRecipe<RecipeExtremeCrafting>(recipeManager, recipeHolder));
    }
	
	@ZenCodeType.Method
	public static void addShapeless(String name, String group, IIngredient[] ingredients, IItemStack stack) {
		List<Ingredient> list = NonNullList.create();
		ItemStack itemStack = stack.getInternal();
		
		for(IIngredient ingredient : ingredients) {
			list.add(ingredient.asVanillaIngredient());
		}
		
		RecipeExtremeShapeless recipe = new RecipeExtremeShapeless(group, itemStack, list, false);
        RecipeHolder<RecipeExtremeCrafting> recipeHolder = new RecipeHolder<RecipeExtremeCrafting>(ResourceLocation.tryParse(name), recipe);
        
        CraftTweakerAPI.apply(new ActionAddRecipe<RecipeExtremeCrafting>(recipeManager, recipeHolder));
    }
	
	@ZenCodeType.Method
	public static void addShapeless(String name, String group, IIngredient[] ingredients, IItemStack stack, boolean hasSingularities) {
		List<Ingredient> list = NonNullList.create();
		ItemStack itemStack = stack.getInternal();
		
		for(IIngredient ingredient : ingredients) {
			list.add(ingredient.asVanillaIngredient());
		}
		
		RecipeExtremeShapeless recipe = new RecipeExtremeShapeless(group, itemStack, list, hasSingularities);
        RecipeHolder<RecipeExtremeCrafting> recipeHolder = new RecipeHolder<RecipeExtremeCrafting>(ResourceLocation.tryParse(name), recipe);
        
        CraftTweakerAPI.apply(new ActionAddRecipe<RecipeExtremeCrafting>(recipeManager, recipeHolder));
    }
	
	@ZenCodeType.Method
	public static void remove(String name) {
        CraftTweakerAPI.apply(new ActionRemoveRecipe<RecipeExtremeCrafting>(recipeManager, recipeHolder -> recipeHolder.id().equals(ResourceLocation.tryParse(name))));
    }
	
	@ZenCodeType.Method
	public static void remove(IItemStack stack) {
		ItemStack itemStack = stack.getInternal();
		
        CraftTweakerAPI.apply(new ActionRemoveRecipe<RecipeExtremeCrafting>(recipeManager, recipeHolder -> recipeHolder.value().getResultItem(RegistryAccess.EMPTY).getItem() == itemStack.getItem()));
    }
	
	@ZenCodeType.Method
	public static void removeShaped(String name) {
        CraftTweakerAPI.apply(new ActionRemoveRecipe<RecipeExtremeCrafting>(recipeManager, recipeHolder -> recipeHolder.value() instanceof RecipeExtremeShaped && recipeHolder.id().equals(ResourceLocation.tryParse(name))));
    }
	
	@ZenCodeType.Method
	public static void removeShaped(IItemStack stack) {
		ItemStack itemStack = stack.getInternal();
		
        CraftTweakerAPI.apply(new ActionRemoveRecipe<RecipeExtremeCrafting>(recipeManager, recipeHolder -> recipeHolder.value() instanceof RecipeExtremeShaped && recipeHolder.value().getResultItem(RegistryAccess.EMPTY).getItem() == itemStack.getItem()));
    }
	
	@ZenCodeType.Method
	public static void removeShapeless(String name) {
        CraftTweakerAPI.apply(new ActionRemoveRecipe<RecipeExtremeCrafting>(recipeManager, recipeHolder -> recipeHolder.value() instanceof RecipeExtremeShapeless && recipeHolder.id().equals(ResourceLocation.tryParse(name))));
    }
	
	@ZenCodeType.Method
	public static void removeShapeless(IItemStack stack) {
		ItemStack itemStack = stack.getInternal();
		
        CraftTweakerAPI.apply(new ActionRemoveRecipe<RecipeExtremeCrafting>(recipeManager, recipeHolder -> recipeHolder.value() instanceof RecipeExtremeShapeless && recipeHolder.value().getResultItem(RegistryAccess.EMPTY).getItem() == itemStack.getItem()));
    }
}
