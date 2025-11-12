package net.byAqua3.avaritia.compat.kubejs;

import dev.latvian.mods.kubejs.recipe.schema.RecipeSchemaRegistry;

public class AvaritiaOldKubeJSPlugin {

	public static void registerRecipeSchemas(RecipeSchemaRegistry event) {
		event.register(AvaritiaKubeJSPlugin.EXTREME_SHAPED, new net.byAqua3.avaritia.compat.kubejs.schema.old.SchemaExtremeShapedRecipe());
		event.register(AvaritiaKubeJSPlugin.EXTREME_SHAPELESS, new net.byAqua3.avaritia.compat.kubejs.schema.old.SchemaExtremeShapelessRecipe());
		event.register(AvaritiaKubeJSPlugin.COMPRESSOR, new net.byAqua3.avaritia.compat.kubejs.schema.old.SchemaCompressorRecipe());
	}
}
