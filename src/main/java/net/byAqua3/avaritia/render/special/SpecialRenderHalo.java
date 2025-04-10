package net.byAqua3.avaritia.render.special;

import java.util.List;
import java.util.Random;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.serialization.MapCodec;

import net.byAqua3.avaritia.Avaritia;
import net.byAqua3.avaritia.item.ItemHalo;
import net.byAqua3.avaritia.item.ItemSingularity;
import net.byAqua3.avaritia.loader.AvaritiaAtlas;
import net.byAqua3.avaritia.util.AvaritiaRenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
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
public class SpecialRenderHalo implements SpecialModelRenderer<ItemHalo>, SpecialModelEntity {

	private final ItemStackRenderState scratchItemStackRenderState = new ItemStackRenderState();
	
	private LivingEntity entity;

	@Override
	public void render(ItemHalo halo, ItemDisplayContext context, PoseStack poseStack,
			MultiBufferSource multiBufferSource, int packedLight, int packedOverlay, boolean hasFoilType) {
		poseStack.pushPose();

		Minecraft mc = Minecraft.getInstance();

		ItemStack itemStack = new ItemStack(halo);
		int type = halo.getType();
		float alpha = halo.getAlpha();

		VertexConsumer vertexConsumer = multiBufferSource
				.getBuffer(RenderType.itemEntityTranslucentCull(AvaritiaAtlas.BLOCK_ATLAS));

		if (context == ItemDisplayContext.GUI) {
			if (type == 0) {
				poseStack.pushPose();

				PoseStack.Pose posestack$pose = poseStack.last();

				RenderSystem.enableBlend();
				RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA,
						GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
				RenderSystem.disableDepthTest();

				poseStack.scale(2.25F, 2.25F, 1.0F);
				poseStack.translate(-0.295F, -0.265F, 0.0F);

				List<BakedQuad> quads = AvaritiaRenderUtils
						.bakeItem(new TextureAtlasSprite[] { mc.getModelManager().getAtlas(AvaritiaAtlas.BLOCK_ATLAS)
								.getSprite(ResourceLocation.tryBuild(Avaritia.MODID, "item/halo")) });

				for (BakedQuad quad : quads) {
					vertexConsumer.putBulkData(posestack$pose, quad, 0.0F, 0.0F, 0.0F, alpha, packedLight,
							packedOverlay, true);
				}

				poseStack.popPose();

			} else if (type == 1) {
				poseStack.pushPose();

				PoseStack.Pose posestack$pose = poseStack.last();

				poseStack.scale(2.0F, 2.0F, 1.0F);
				poseStack.translate(-0.25F, -0.255F, 0.0F);

				List<BakedQuad> quads = AvaritiaRenderUtils
						.bakeItem(new TextureAtlasSprite[] { mc.getModelManager().getAtlas(AvaritiaAtlas.BLOCK_ATLAS)
								.getSprite(ResourceLocation.tryBuild(Avaritia.MODID, "item/halo_noise")) });

				for (BakedQuad quad : quads) {
					vertexConsumer.putBulkData(posestack$pose, quad, 1.0F, 1.0F, 1.0F, alpha, packedLight,
							packedOverlay, true);
				}

				poseStack.popPose();
			}
			if (type == 2) {
				poseStack.pushPose();

				PoseStack.Pose posestack$pose = poseStack.last();

				poseStack.scale(1.5F, 1.5F, 1.0F);
				poseStack.translate(-0.17F, -0.155F, 0.0F);

				List<BakedQuad> quads = AvaritiaRenderUtils
						.bakeItem(new TextureAtlasSprite[] { mc.getModelManager().getAtlas(AvaritiaAtlas.BLOCK_ATLAS)
								.getSprite(ResourceLocation.tryBuild(Avaritia.MODID, "item/halo")) });

				for (BakedQuad quad : quads) {
					vertexConsumer.putBulkData(posestack$pose, quad, 0.0F, 0.0F, 0.0F, alpha, packedLight,
							packedOverlay, true);
				}

				poseStack.popPose();
			}

			if (type == 0 || halo.getPulse()) {
				float scale = new Random().nextFloat() * 0.10F + 0.95F;
				double translate = (1.0D - scale) / 2.0D;
				poseStack.scale(scale, scale, 1.0001F);
				poseStack.translate(translate, translate, 0.0F);
			}
		}

		if (halo instanceof ItemSingularity) {
			poseStack.pushPose();

			PoseStack.Pose posestack$pose = poseStack.last();

			ItemSingularity singularity = ((ItemSingularity) halo);

			for (int i = 0; i < 2; i++) {
				List<BakedQuad> quads = AvaritiaRenderUtils.bakeItem(new TextureAtlasSprite[] {
						mc.getModelManager().getAtlas(AvaritiaAtlas.BLOCK_ATLAS).getSprite(ResourceLocation
								.tryBuild(Avaritia.MODID, "item/singularity/singularity_layer_" + i)) });

				float r = (i == 0 ? singularity.getColor(itemStack).getRed()
						: singularity.getLayerColor(itemStack).getRed()) / 255.0F;
				float g = (i == 0 ? singularity.getColor(itemStack).getGreen()
						: singularity.getLayerColor(itemStack).getGreen()) / 255.0F;
				float b = (i == 0 ? singularity.getColor(itemStack).getBlue()
						: singularity.getLayerColor(itemStack).getBlue()) / 255.0F;
				float a = (i == 0 ? singularity.getColor(itemStack).getAlpha()
						: singularity.getLayerColor(itemStack).getAlpha()) / 255.0F;

				for (BakedQuad quad : quads) {
					vertexConsumer.putBulkData(posestack$pose, quad, r, g, b, a, packedLight, packedOverlay, true);
				}
			}

			poseStack.popPose();
		} else {
			mc.getItemModelResolver().updateForTopItem(this.scratchItemStackRenderState, itemStack, context, false, mc.level,
					this.entity, 42);

			for (int i = 0; i < this.scratchItemStackRenderState.activeLayerCount; i++) {
				LayerRenderState layerRenderState = this.scratchItemStackRenderState.layers[i];

				ItemRenderer.renderItem(context, poseStack, multiBufferSource, packedLight, packedOverlay,
						layerRenderState.tintLayers, layerRenderState.model,
						RenderType.itemEntityTranslucentCull(AvaritiaAtlas.BLOCK_ATLAS), layerRenderState.foilType);
			}
		}

		poseStack.popPose();
	}

	@Override
	public ItemHalo extractArgument(ItemStack stack) {
		if (stack.getItem() instanceof ItemHalo) {
			return (ItemHalo) stack.getItem();
		}
		return null;
	}
	
	@Override
	public void extractArgument(LivingEntity entity) {
		this.entity = entity;
	}

	@OnlyIn(Dist.CLIENT)
	public static record Unbaked() implements SpecialModelRenderer.Unbaked {
		public static final MapCodec<SpecialRenderHalo.Unbaked> MAP_CODEC = MapCodec.unit(SpecialRenderHalo.Unbaked::new);

		@Override
		public MapCodec<SpecialRenderHalo.Unbaked> type() {
			return MAP_CODEC;
		}

		@Override
		public SpecialModelRenderer<?> bake(EntityModelSet entityModelSet) {
			return new SpecialRenderHalo();
		}
	}
	
}
