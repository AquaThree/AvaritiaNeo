package net.byAqua3.avaritia.geometry;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.byAqua3.avaritia.item.ItemMatterCluster;
import net.byAqua3.avaritia.loader.AvaritiaDataComponents;
import net.byAqua3.avaritia.loader.AvaritiaRenderTypes;
import net.byAqua3.avaritia.loader.AvaritiaShaders;
import net.byAqua3.avaritia.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class BakedModelCosmic extends BakedModelRenderer {

	private final ResourceLocation maskTexture;
	private final boolean isMatterCluster;

	public BakedModelCosmic(BakedModel bakedModel, ResourceLocation maskTexture, boolean isMatterCluster) {
		super(bakedModel);
		this.maskTexture = maskTexture;
		this.isMatterCluster = isMatterCluster;
	}

	@Override
	public void render(ItemStack stack, ItemDisplayContext context, boolean leftHand, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, int packedOverlay, BakedModel model) {
		RenderType COSMIC_RENDER_TYPE = AvaritiaRenderTypes.COSMIC_RENDER_TYPE;
		Minecraft mc = Minecraft.getInstance();
		ItemRenderer itemRenderer = mc.getItemRenderer();
		TextureAtlas textureAtlas = mc.getModelManager().getAtlas(InventoryMenu.BLOCK_ATLAS);
		
		for (BakedModel bakedModel : model.getRenderPasses(stack, true)) {
			for (RenderType renderType : bakedModel.getRenderTypes(stack, true)) {
				itemRenderer.renderModelLists(bakedModel, stack, packedLight, packedOverlay, poseStack, multiBufferSource.getBuffer(renderType));
			}
		}

		if (multiBufferSource instanceof MultiBufferSource.BufferSource) {
			MultiBufferSource.BufferSource bufferSource = (MultiBufferSource.BufferSource) multiBufferSource;
			bufferSource.endBatch();
		}

		float yaw = 0.0F;
		float pitch = 0.0F;
		float scale = 1.0F;

		if (AvaritiaShaders.cosmicInventoryRender || context == ItemDisplayContext.GUI) {
			scale = 100.0F;
		} else {
			yaw = (float) ((mc.player.getYRot() * 2.0F) * Math.PI / 360.0D);
			pitch = -((float) ((mc.player.getXRot() * 2.0F) * Math.PI / 360.0D));
		}

		AvaritiaShaders.timeUniform.set(mc.level.getGameTime() % Integer.MAX_VALUE);
		AvaritiaShaders.yawUniform.set(yaw);
		AvaritiaShaders.pitchUniform.set(pitch);
		AvaritiaShaders.externalScaleUniform.set(scale);
		AvaritiaShaders.opacityUniform.set(1.0F);
		AvaritiaShaders.cosmicuvsUniform.set(AvaritiaShaders.COSMIC_UVS);
		
		if (this.isMatterCluster) {
			if (stack.has(AvaritiaDataComponents.CLUSTER_CONTAINER.get()) && ItemMatterCluster.getClusterCount(ItemMatterCluster.getClusterItems(stack)) <= ItemMatterCluster.CAPACITY) {
				AvaritiaShaders.opacityUniform.set(Float.valueOf(ItemMatterCluster.getClusterCount(ItemMatterCluster.getClusterItems(stack))) / Float.valueOf(ItemMatterCluster.CAPACITY));
			}
		}

		TextureAtlasSprite[] textureAtlasSprites = new TextureAtlasSprite[] { textureAtlas.getSprite(this.maskTexture) };

		VertexConsumer vertexConsumer = multiBufferSource.getBuffer(COSMIC_RENDER_TYPE);
		itemRenderer.renderQuadList(poseStack, vertexConsumer, RenderUtils.bakeItem(textureAtlasSprites), stack, packedLight, packedOverlay);
	}}
