package net.byAqua3.avaritia.geometry;

import java.util.function.Function;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import net.byAqua3.avaritia.loader.AvaritiaModels;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.geometry.BlockGeometryBakingContext;
import net.neoforged.neoforge.client.model.geometry.IGeometryBakingContext;
import net.neoforged.neoforge.client.model.geometry.IGeometryLoader;
import net.neoforged.neoforge.client.model.geometry.IUnbakedGeometry;

public class GeometryLoaderCosmic implements IGeometryLoader<GeometryLoaderCosmic.ModelGeometryCosmic> {

	@Override
	public ModelGeometryCosmic read(JsonObject jsonObject, JsonDeserializationContext deserializationContext) throws JsonParseException {
		jsonObject.remove("loader");

		BlockModel blockModel = BlockModel.fromString(jsonObject.toString());
		JsonObject cosmic = jsonObject.getAsJsonObject("cosmic");
		String maskTexture = cosmic.get("mask").getAsString();
		boolean isMatterCluster = cosmic.has("isMatterCluster") ? cosmic.get("isMatterCluster").getAsBoolean() : false;

		return new ModelGeometryCosmic(blockModel, ResourceLocation.tryParse(maskTexture), isMatterCluster);
	}

	public static class ModelGeometryCosmic implements IUnbakedGeometry<ModelGeometryCosmic>, IBakedModelRenderer {

		private final BlockModel blockModel;
		private final ResourceLocation maskTexture;
		private final boolean isMatterCluster;

		public ModelGeometryCosmic(BlockModel baseModel, ResourceLocation maskTexture, boolean isMatterCluster) {
			this.blockModel = baseModel;
			this.maskTexture = maskTexture;
			this.isMatterCluster = isMatterCluster;
		}
		
		@Override
		public void resolveParents(Function<ResourceLocation, UnbakedModel> modelGetter, IGeometryBakingContext context) {
			ResourceLocation parentId = this.blockModel.getParentLocation();
			if (context instanceof BlockGeometryBakingContext) {
				BlockGeometryBakingContext blockContext = (BlockGeometryBakingContext) context;
				AvaritiaModels.LOAD_MODELS.put(ResourceLocation.tryParse(blockContext.getModelName()), this.blockModel);
				AvaritiaModels.LOAD_ITEM_MODELS.put(ResourceLocation.tryParse(blockContext.getModelName().replace("item/", "")), this);
			}
			if (!AvaritiaModels.LOAD_MODELS.containsKey(parentId)) {
				this.blockModel.resolveParents(modelGetter);
			} else {
				this.blockModel.resolveParents(resourceLocation -> AvaritiaModels.LOAD_MODELS.get(resourceLocation));
			}
		}

		@Override
		public BakedModel bake(IGeometryBakingContext context, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides) {
			BakedModel bakedModel = this.blockModel.bake(baker, this.blockModel, spriteGetter, modelState, true);
			return new BakedModelCosmic(bakedModel, this.maskTexture, this.isMatterCluster);
		}

		@Override
		public BakedModel getBakedModel(BakedModel bakedModel) {
			return new BakedModelCosmic(bakedModel, this.maskTexture, this.isMatterCluster);
		}
	}}
