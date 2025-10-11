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

public class GeometryLoaderHalo implements IGeometryLoader<GeometryLoaderHalo.ModelGeometryHalo> {

	@Override
	public ModelGeometryHalo read(JsonObject jsonObject, JsonDeserializationContext deserializationContext) throws JsonParseException {
		jsonObject.remove("loader");

		BlockModel blockModel = BlockModel.fromString(jsonObject.toString());
		JsonObject halo = jsonObject.getAsJsonObject("halo");
		int type = jsonObject.has("halo") && halo.has("type") ? halo.get("type").getAsInt() : 0;
		float alpha = jsonObject.has("halo") && halo.has("alpha") ? halo.get("alpha").getAsFloat() : 1.0F;
		boolean pulse = jsonObject.has("halo") && halo.has("pulse") ? halo.get("pulse").getAsBoolean() : false;

		return new ModelGeometryHalo(blockModel, type, alpha, pulse);
	}

	public static class ModelGeometryHalo implements IUnbakedGeometry<ModelGeometryHalo>, IBakedModelRenderer {

		private final BlockModel blockModel;
		private final int type;
		private final float alpha;
		private final boolean pulse;

		public ModelGeometryHalo(BlockModel baseModel, int type, float alpha, boolean pulse) {
			this.blockModel = baseModel;
			this.type = type;
			this.alpha = alpha;
			this.pulse = pulse;
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
			return new BakedModelHalo(bakedModel, type, alpha, pulse);
		}

		@Override
		public BakedModel getBakedModel(BakedModel bakedModel) {
			return new BakedModelHalo(bakedModel, type, alpha, pulse);
		}
	}}
