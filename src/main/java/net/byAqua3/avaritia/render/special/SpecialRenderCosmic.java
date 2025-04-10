package net.byAqua3.avaritia.render.special;

import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.byAqua3.avaritia.loader.AvaritiaAtlas;
import net.byAqua3.avaritia.shader.AvaritiaCosmicShaders;
import net.byAqua3.avaritia.shader.AvaritiaRenderType;
import net.byAqua3.avaritia.util.AvaritiaRenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.CompiledShaderProgram;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderManager;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState.LayerRenderState;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SpecialRenderCosmic implements SpecialModelRenderer<ItemStack>, SpecialModelEntity {

	private final ItemStackRenderState scratchItemStackRenderState = new ItemStackRenderState();
	
	private final ResourceLocation mark;
	
	private LivingEntity entity;

	public SpecialRenderCosmic(ResourceLocation mark) {
		this.mark = mark;
	}

	@Override
	public void render(ItemStack stack, ItemDisplayContext context, PoseStack poseStack,
			MultiBufferSource multiBufferSource, int packedLight, int packedOverlay, boolean hasFoilType) {
		poseStack.pushPose();

		RenderType COSMIC_RENDER_TYPE = AvaritiaRenderType.COSMIC_RENDER_TYPE;
		Minecraft mc = Minecraft.getInstance();

		mc.getItemModelResolver().updateForTopItem(this.scratchItemStackRenderState, stack, context, false, mc.level,
				this.entity, 42);

		for (int i = 0; i < this.scratchItemStackRenderState.activeLayerCount; i++) {
			LayerRenderState layerRenderState = this.scratchItemStackRenderState.layers[i];

			ItemRenderer.renderItem(context, poseStack, multiBufferSource, packedLight, packedOverlay,
					layerRenderState.tintLayers, layerRenderState.model,
					RenderType.itemEntityTranslucentCull(AvaritiaAtlas.BLOCK_ATLAS), layerRenderState.foilType);
		}

		if (multiBufferSource instanceof MultiBufferSource.BufferSource) {
			MultiBufferSource.BufferSource bufferSource = (MultiBufferSource.BufferSource) multiBufferSource;
			bufferSource.endBatch();
		}

		float yaw = 0.0F;
		float pitch = 0.0F;
		float scale = 1.0F;

		if (AvaritiaCosmicShaders.cosmicInventoryRender || context == ItemDisplayContext.GUI) {
			scale = 100.0F;
		} else {
			yaw = (float) ((mc.player.getYRot() * 2.0F) * Math.PI / 360.0D);
			pitch = -((float) ((mc.player.getXRot() * 2.0F) * Math.PI / 360.0D));
		}

		ShaderManager shaderManager = mc.getShaderManager();
		CompiledShaderProgram compiledShaderProgram = shaderManager.getProgram(AvaritiaCosmicShaders.cosmicShader);
		compiledShaderProgram.getUniform("time").set(mc.level.getGameTime() % Integer.MAX_VALUE);
		compiledShaderProgram.getUniform("yaw").set(yaw);
		compiledShaderProgram.getUniform("pitch").set(pitch);
		compiledShaderProgram.getUniform("externalScale").set(scale);
		compiledShaderProgram.getUniform("opacity").set(1.0F);
		compiledShaderProgram.getUniform("cosmicuvs").set(AvaritiaCosmicShaders.COSMIC_UVS);

		TextureAtlasSprite[] textureAtlasSprites = null;

		if (this.mark != null) {
			textureAtlasSprites = new TextureAtlasSprite[] {
					mc.getModelManager().getAtlas(AvaritiaAtlas.BLOCK_ATLAS).getSprite(this.mark) };
		}

		if (textureAtlasSprites != null) {
			List<BakedQuad> quads = AvaritiaRenderUtils.bakeItem(textureAtlasSprites);

			for (BakedQuad quad : quads) {
				multiBufferSource.getBuffer(COSMIC_RENDER_TYPE).putBulkData(poseStack.last(), quad, 1.0F, 1.0F, 1.0F,
						1.0F, packedLight, packedOverlay, true);
			}
		}

		poseStack.popPose();
	}

	@Override
	public ItemStack extractArgument(ItemStack stack) {
		return stack;
	}
	
	@Override
	public void extractArgument(LivingEntity entity) {
		this.entity = entity;
	}

	@OnlyIn(Dist.CLIENT)
	public static record Unbaked(ResourceLocation mark) implements SpecialModelRenderer.Unbaked {
		public static final MapCodec<SpecialRenderCosmic.Unbaked> MAP_CODEC = RecordCodecBuilder
				.mapCodec(instance -> instance
						.group(ResourceLocation.CODEC.fieldOf("mark").forGetter(SpecialRenderCosmic.Unbaked::mark))
						.apply(instance, SpecialRenderCosmic.Unbaked::new));

		@Override
		public MapCodec<SpecialRenderCosmic.Unbaked> type() {
			return MAP_CODEC;
		}

		@Override
		public SpecialModelRenderer<?> bake(EntityModelSet entityModelSet) {
			return new SpecialRenderCosmic(this.mark);
		}
	}
}
