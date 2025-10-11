package net.byAqua3.avaritia.compat.kubejs;

import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchemaRegistry;
import net.byAqua3.avaritia.Avaritia;
import net.byAqua3.avaritia.compat.kubejs.schema.SchemaCompressorRecipe;
import net.byAqua3.avaritia.compat.kubejs.schema.SchemaExtremeShapedRecipe;
import net.byAqua3.avaritia.compat.kubejs.schema.SchemaExtremeShapelessRecipe;
import net.minecraft.resources.ResourceLocation;

public class AvaritiaKubeJSPlugin implements KubeJSPlugin {
	
	public static final ResourceLocation EXTREME_SHAPED = ResourceLocation.tryBuild(Avaritia.MODID, "extreme_shaped");
	public static final ResourceLocation EXTREME_SHAPELESS = ResourceLocation.tryBuild(Avaritia.MODID, "extreme_shapeless");
	public static final ResourceLocation COMPRESSOR = ResourceLocation.tryBuild(Avaritia.MODID, "compressor");
	
	@Override
    public void registerRecipeSchemas(RecipeSchemaRegistry event) {
		event.register(EXTREME_SHAPED, new SchemaExtremeShapedRecipe());
		event.register(EXTREME_SHAPELESS, new SchemaExtremeShapelessRecipe());
		event.register(COMPRESSOR, new SchemaCompressorRecipe());
	}}
