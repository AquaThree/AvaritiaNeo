package net.byAqua3.avaritia.mixin;

import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Transformation;

import net.byAqua3.avaritia.Avaritia;
import net.byAqua3.avaritia.item.ItemHalo;
import net.byAqua3.avaritia.item.ItemInfinityBow;
import net.byAqua3.avaritia.item.ItemInfinitySword;
import net.byAqua3.avaritia.item.ItemMatterCluster;
import net.byAqua3.avaritia.item.ItemSingularity;
import net.byAqua3.avaritia.shader.AvaritiaCosmicShaders;
import net.byAqua3.avaritia.shader.AvaritiaRenderType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockElement;
import net.minecraft.client.renderer.block.model.BlockElementFace;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.block.model.ItemModelGenerator;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.model.SimpleModelState;

@Mixin({ ItemRenderer.class })
public class MixinItemRenderer {

	private static final ItemModelGenerator ITEM_MODEL_GENERATOR = new ItemModelGenerator();
	private static final FaceBakery FACE_BAKERY = new FaceBakery();

	private static List<BakedQuad> bakeItem(TextureAtlasSprite... sprites) {
		return bakeItem(Transformation.identity(), sprites);
	}

	private static List<BakedQuad> bakeItem(Transformation state, TextureAtlasSprite... sprites) {
		List<BakedQuad> quads = new LinkedList<>();

		for (int i = 0; i < sprites.length; ++i) {
			TextureAtlasSprite sprite = sprites[i];
			List<BlockElement> unbaked = ITEM_MODEL_GENERATOR.processFrames(i, "layer" + i, sprite.contents());

			for (BlockElement element : unbaked) {
				for (Entry<Direction, BlockElementFace> directionBlockElementFaceEntry : element.faces.entrySet()) {
					quads.add(FACE_BAKERY.bakeQuad(element.from, element.to, directionBlockElementFaceEntry.getValue(),
							sprite, directionBlockElementFaceEntry.getKey(),
							new SimpleModelState(Transformation.identity()), element.rotation, element.shade,
							new ResourceLocation(Avaritia.MODID, "dynamic")));
				}
			}
		}

		return quads;
	}

	@Inject(method = { "render" }, at = { @At("HEAD") }, cancellable = true)
	public void render(ItemStack stack, ItemDisplayContext context, boolean leftHand, PoseStack poseStack,
			MultiBufferSource multiBufferSource, int packedLight, int packedOverlay, BakedModel model,
			CallbackInfo callbackInfo) {
		if (stack.getItem() instanceof ItemInfinitySword || stack.getItem() instanceof ItemInfinityBow
				|| stack.getItem() instanceof ItemMatterCluster) {
			callbackInfo.cancel();

			poseStack.pushPose();
			model = net.neoforged.neoforge.client.ClientHooks.handleCameraTransforms(poseStack, model, context,
					leftHand);
			poseStack.translate(-0.5F, -0.5F, -0.5F);

			Minecraft mc = Minecraft.getInstance();
			ItemRenderer itemRenderer = mc.getItemRenderer();
			for (BakedModel bakedModel : model.getRenderPasses(stack, true)) {
				for (RenderType renderType : bakedModel.getRenderTypes(stack, true)) {
					itemRenderer.renderModelLists(bakedModel, stack, packedLight, packedOverlay, poseStack,
							multiBufferSource.getBuffer(renderType));
				}
			}

			if (multiBufferSource instanceof MultiBufferSource.BufferSource) {
				MultiBufferSource.BufferSource bufferSource = (MultiBufferSource.BufferSource) multiBufferSource;
				bufferSource.endBatch();
			}

			RenderType COSMIC_RENDER_TYPE = AvaritiaRenderType.COSMIC_RENDER_TYPE;

			float yaw = 0.0F;
			float pitch = 0.0F;
			float scale = 1.0F;

			if (AvaritiaCosmicShaders.cosmicInventoryRender || context == ItemDisplayContext.GUI) {
				scale = 100.0F;
			} else {
				yaw = (float) ((mc.player.getYRot() * 2.0F) * Math.PI / 360.0D);
				pitch = -((float) ((mc.player.getXRot() * 2.0F) * Math.PI / 360.0D));
			}

			AvaritiaCosmicShaders.timeUniform.set(mc.level.getGameTime() % Integer.MAX_VALUE);
			AvaritiaCosmicShaders.yawUniform.set(yaw);
			AvaritiaCosmicShaders.pitchUniform.set(pitch);
			AvaritiaCosmicShaders.externalScaleUniform.set(scale);
			AvaritiaCosmicShaders.opacityUniform.set(1.0F);

			for (int i = 0; i < AvaritiaCosmicShaders.COSMIC_SPRITES.length; i++) {
				TextureAtlasSprite sprite = AvaritiaCosmicShaders.COSMIC_SPRITES[i];
				AvaritiaCosmicShaders.COSMIC_UVS[i * 4 + 0] = sprite.getU0();
				AvaritiaCosmicShaders.COSMIC_UVS[i * 4 + 1] = sprite.getV0();
				AvaritiaCosmicShaders.COSMIC_UVS[i * 4 + 2] = sprite.getU1();
				AvaritiaCosmicShaders.COSMIC_UVS[i * 4 + 3] = sprite.getV1();
			}
			AvaritiaCosmicShaders.cosmicuvsUniform.set(AvaritiaCosmicShaders.COSMIC_UVS);

			TextureAtlasSprite[] textureAtlasSprites = null;

			if (stack.getItem() instanceof ItemInfinitySword) {
				textureAtlasSprites = new TextureAtlasSprite[] {
						Minecraft.getInstance().getModelManager().getAtlas(InventoryMenu.BLOCK_ATLAS)
								.getSprite(new ResourceLocation(Avaritia.MODID, "item/tools/infinity_sword_mask")) };
			} else if (stack.getItem() instanceof ItemInfinityBow) {
				textureAtlasSprites = new TextureAtlasSprite[] {
						Minecraft.getInstance().getModelManager().getAtlas(InventoryMenu.BLOCK_ATLAS)
								.getSprite(new ResourceLocation(Avaritia.MODID, "item/tools/bow/idle_mask")),
						Minecraft.getInstance().getModelManager().getAtlas(InventoryMenu.BLOCK_ATLAS)
								.getSprite(new ResourceLocation(Avaritia.MODID, "item/tools/bow/pull_0_mask")),
						Minecraft.getInstance().getModelManager().getAtlas(InventoryMenu.BLOCK_ATLAS)
								.getSprite(new ResourceLocation(Avaritia.MODID, "item/tools/bow/pull_1_mask")),
						Minecraft.getInstance().getModelManager().getAtlas(InventoryMenu.BLOCK_ATLAS)
								.getSprite(new ResourceLocation(Avaritia.MODID, "item/tools/bow/pull_2_mask")) };
			} else if (stack.getItem() instanceof ItemMatterCluster) {
				if (stack.hasTag() && stack.getOrCreateTag().contains("items", Tag.TAG_LIST) && ItemMatterCluster
						.getClusterCount(ItemMatterCluster.getClusterItems(stack)) == ItemMatterCluster.CAPACITY) {
					textureAtlasSprites = new TextureAtlasSprite[] {
							Minecraft.getInstance().getModelManager().getAtlas(InventoryMenu.BLOCK_ATLAS)
									.getSprite(new ResourceLocation(Avaritia.MODID, "item/matter_cluster_full_mask")) };
				} else {
					textureAtlasSprites = new TextureAtlasSprite[] {
							Minecraft.getInstance().getModelManager().getAtlas(InventoryMenu.BLOCK_ATLAS).getSprite(
									new ResourceLocation(Avaritia.MODID, "item/matter_cluster_empty_mask")) };
				}
			}

			VertexConsumer vertexConsumer = multiBufferSource.getBuffer(COSMIC_RENDER_TYPE);
			itemRenderer.renderQuadList(poseStack, vertexConsumer, bakeItem(textureAtlasSprites), stack, packedLight,
					packedOverlay);

			poseStack.popPose();
		} else if (stack.getItem() instanceof ItemHalo) {
			callbackInfo.cancel();

			poseStack.pushPose();

			PoseStack.Pose posestack$pose = poseStack.last();

			model = net.neoforged.neoforge.client.ClientHooks.handleCameraTransforms(poseStack, model, context,
					leftHand);
			poseStack.translate(-0.5F, -0.5F, -0.5F);

			ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
			for (BakedModel bakedModel : model.getRenderPasses(stack, true)) {
				for (RenderType renderType : bakedModel.getRenderTypes(stack, true)) {
					int type = ((ItemHalo) stack.getItem()).getType();

					if (context == ItemDisplayContext.GUI) {
						if (type == 0) {
							poseStack.pushPose();

							posestack$pose = poseStack.last();

							float scale = new Random().nextFloat() * 0.15F + 2.45F;
							poseStack.scale(scale, scale, 1.0F);
							poseStack.translate(-0.30F, -0.30F, 0.0F);

							List<BakedQuad> quads = bakeItem(new TextureAtlasSprite[] {
									Minecraft.getInstance().getModelManager().getAtlas(InventoryMenu.BLOCK_ATLAS)
											.getSprite(new ResourceLocation(Avaritia.MODID, "item/halo128")) });

							for (BakedQuad quad : quads) {
								multiBufferSource.getBuffer(renderType).putBulkData(posestack$pose, quad, 0.0F, 0.0F,
										0.0F, 0.6F, packedLight, packedOverlay, true);
							}
							poseStack.popPose();

							scale = new Random().nextFloat() * 0.10F + 0.95F;
							poseStack.scale(scale, scale, 1.0F);
						} else if (type == 1) {
							poseStack.pushPose();

							poseStack.scale(2.0F, 2.0F, 1.0F);
							poseStack.translate(-0.25F, -0.25F, 0.0F);

							List<BakedQuad> quads = bakeItem(new TextureAtlasSprite[] {
									Minecraft.getInstance().getModelManager().getAtlas(InventoryMenu.BLOCK_ATLAS)
											.getSprite(new ResourceLocation(Avaritia.MODID, "item/halo_noise")) });

							itemRenderer.renderQuadList(poseStack, multiBufferSource.getBuffer(renderType), quads,
									stack, packedLight, packedOverlay);

							poseStack.popPose();
						} else if (type == 2) {
							poseStack.pushPose();

							posestack$pose = poseStack.last();

							poseStack.scale(1.5F, 1.5F, 1.5F);
							poseStack.translate(-0.15F, -0.15F, 0.0F);

							List<BakedQuad> quads = bakeItem(new TextureAtlasSprite[] {
									Minecraft.getInstance().getModelManager().getAtlas(InventoryMenu.BLOCK_ATLAS)
											.getSprite(new ResourceLocation(Avaritia.MODID, "item/halo")) });

							for (BakedQuad quad : quads) {
								multiBufferSource.getBuffer(renderType).putBulkData(posestack$pose, quad, 0.0F, 0.0F,
										0.0F, 0.6F, packedLight, packedOverlay, true);
							}
							poseStack.popPose();
						}

					}
					if (stack.getItem() instanceof ItemSingularity) {
						poseStack.pushPose();

						posestack$pose = poseStack.last();

						ItemSingularity singularity = ((ItemSingularity) stack.getItem());

						for (int i = 0; i < 2; i++) {
							List<BakedQuad> quads = bakeItem(new TextureAtlasSprite[] {
									Minecraft.getInstance().getModelManager().getAtlas(InventoryMenu.BLOCK_ATLAS)
											.getSprite(new ResourceLocation(Avaritia.MODID,
													"item/singularity/singularity_layer_" + i)) });

							float r = (i == 0 ? singularity.getColor().getRed() : singularity.getLayerColor().getRed())
									/ 255.0F;
							float g = (i == 0 ? singularity.getColor().getGreen()
									: singularity.getLayerColor().getGreen()) / 255.0F;
							float b = (i == 0 ? singularity.getColor().getBlue()
									: singularity.getLayerColor().getBlue()) / 255.0F;
							float a = (i == 0 ? singularity.getColor().getAlpha()
									: singularity.getLayerColor().getAlpha()) / 255.0F;

							for (BakedQuad quad : quads) {
								multiBufferSource.getBuffer(renderType).putBulkData(posestack$pose, quad, r, g, b, a,
										packedLight, packedOverlay, false);
							}

						}

						poseStack.popPose();
					} else {
						itemRenderer.renderModelLists(bakedModel, stack, packedLight, packedOverlay, poseStack,
								multiBufferSource.getBuffer(renderType));
					}
				}
			}
			poseStack.popPose();
		}
	}
}
