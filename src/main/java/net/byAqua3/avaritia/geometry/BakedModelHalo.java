package net.byAqua3.avaritia.geometry;

import java.util.List;
import java.util.Random;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.byAqua3.avaritia.Avaritia;
import net.byAqua3.avaritia.item.ItemSingularity;
import net.byAqua3.avaritia.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class BakedModelHalo extends BakedModelRenderer {

	private final int type;
	private final float alpha;
	private final boolean pulse;

	public BakedModelHalo(BakedModel bakedModel, int type, float alpha, boolean pulse) {
		super(bakedModel);
		this.type = type;
		this.alpha = alpha;
		this.pulse = pulse;
	}

	@Override
	public void render(ItemStack stack, ItemDisplayContext context, boolean leftHand, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, int packedOverlay, BakedModel model) {
		Minecraft mc = Minecraft.getInstance();
		ItemRenderer itemRenderer = mc.getItemRenderer();
		TextureAtlas textureAtlas = mc.getModelManager().getAtlas(InventoryMenu.BLOCK_ATLAS);

		for (BakedModel bakedModel : model.getRenderPasses(stack, true)) {
			for (RenderType renderType : bakedModel.getRenderTypes(stack, true)) {
				int type = this.type;
				float alpha = this.alpha;
				boolean pulse = this.pulse;

				VertexConsumer vertexConsumer = multiBufferSource.getBuffer(renderType);

				if (context == ItemDisplayContext.GUI) {
					if (type == 0) {
						poseStack.pushPose();
						PoseStack.Pose poseStack$pose = poseStack.last();

						RenderSystem.enableBlend();
						RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
						RenderSystem.disableDepthTest();

						poseStack.scale(2.25F, 2.25F, 1.0F);
						poseStack.translate(-0.295F, -0.265F, 0.0F);

						List<BakedQuad> quads = RenderUtils.bakeItem(new TextureAtlasSprite[] { textureAtlas.getSprite(ResourceLocation.tryBuild(Avaritia.MODID, "item/halo")) });

						for (BakedQuad quad : quads) {
							vertexConsumer.putBulkData(poseStack$pose, quad, 0.0F, 0.0F, 0.0F, alpha, packedLight, packedOverlay, true);
						}
						poseStack.popPose();
					} else if (type == 1) {
						poseStack.pushPose();
						PoseStack.Pose poseStack$pose = poseStack.last();

						poseStack.scale(2.0F, 2.0F, 1.0F);
						poseStack.translate(-0.25F, -0.255F, 0.0F);

						List<BakedQuad> quads = RenderUtils.bakeItem(new TextureAtlasSprite[] { textureAtlas.getSprite(ResourceLocation.tryBuild(Avaritia.MODID, "item/halo_noise")) });

						for (BakedQuad quad : quads) {
							vertexConsumer.putBulkData(poseStack$pose, quad, 1.0F, 1.0F, 1.0F, alpha, packedLight, packedOverlay, true);
						}
						poseStack.popPose();
					}
					if (type == 2) {
						poseStack.pushPose();
						PoseStack.Pose poseStack$pose = poseStack.last();

						poseStack.scale(1.5F, 1.5F, 1.0F);
						poseStack.translate(-0.17F, -0.155F, 0.0F);

						List<BakedQuad> quads = RenderUtils.bakeItem(new TextureAtlasSprite[] { textureAtlas.getSprite(ResourceLocation.tryBuild(Avaritia.MODID, "item/halo")) });

						for (BakedQuad quad : quads) {
							vertexConsumer.putBulkData(poseStack$pose, quad, 0.0F, 0.0F, 0.0F, alpha, packedLight, packedOverlay, true);
						}
						poseStack.popPose();
					}
					if (type == 0 || pulse) {
						float scale = new Random().nextFloat() * 0.10F + 0.95F;
						double translate = (1.0D - scale) / 2.0D;
						poseStack.scale(scale, scale, 1.0001F);
						poseStack.translate(translate, translate, 0.0F);
					}
				}
				if (stack.getItem() instanceof ItemSingularity) {
					RenderUtils.renderSingularity(stack, poseStack, vertexConsumer, packedLight, packedOverlay);
				} else {
					itemRenderer.renderModelLists(bakedModel, stack, packedLight, packedOverlay, poseStack, vertexConsumer);
				}
			}
		}
	}}
