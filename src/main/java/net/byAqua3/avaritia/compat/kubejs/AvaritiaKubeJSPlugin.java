package net.byAqua3.avaritia.compat.kubejs;

import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;

import dev.latvian.mods.kubejs.KubeJS;
import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchemaRegistry;
import net.byAqua3.avaritia.Avaritia;
import net.minecraft.resources.ResourceLocation;

public class AvaritiaKubeJSPlugin implements KubeJSPlugin {

	public static final ResourceLocation EXTREME_SHAPED = ResourceLocation.tryBuild(Avaritia.MODID, "extreme_shaped");
	public static final ResourceLocation EXTREME_SHAPELESS = ResourceLocation.tryBuild(Avaritia.MODID, "extreme_shapeless");
	public static final ResourceLocation COMPRESSOR = ResourceLocation.tryBuild(Avaritia.MODID, "compressor");

	@Override
	public void registerRecipeSchemas(RecipeSchemaRegistry event) {
		ArtifactVersion version = new DefaultArtifactVersion("2101.7.2-build.270");
		int compareInt = KubeJS.thisMod.getModInfo().getVersion().compareTo(version);

		if (compareInt == 0) {
			AvaritiaSpecialKubeJSPlugin.registerRecipeSchemas(event);
		} else if (compareInt < 0) {
			AvaritiaOldKubeJSPlugin.registerRecipeSchemas(event);
		} else {
			AvaritiaNewKubeJSPlugin.registerRecipeSchemas(event);
		}
	}
}
