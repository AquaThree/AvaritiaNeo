package net.byAqua3.avaritia.util;

import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Transformation;

import net.byAqua3.avaritia.Avaritia;
import net.byAqua3.avaritia.item.ItemSingularity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockElement;
import net.minecraft.client.renderer.block.model.BlockElementFace;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.block.model.ItemModelGenerator;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.model.SimpleModelState;

public class RenderUtils {

	public static final ItemModelGenerator ITEM_MODEL_GENERATOR = new ItemModelGenerator();
	public static final FaceBakery FACE_BAKERY = new FaceBakery();

	public static List<BakedQuad> bakeItem(TextureAtlasSprite... sprites) {
		return bakeItem(Transformation.identity(), sprites);
	}

	public static List<BakedQuad> bakeItem(Transformation state, TextureAtlasSprite... sprites) {
		List<BakedQuad> quads = new LinkedList<>();

		for (int i = 0; i < sprites.length; ++i) {
			TextureAtlasSprite sprite = sprites[i];
			List<BlockElement> unbaked = ITEM_MODEL_GENERATOR.processFrames(i, "layer" + i, sprite.contents());

			for (BlockElement element : unbaked) {
				for (Entry<Direction, BlockElementFace> directionBlockElementFaceEntry : element.faces.entrySet()) {
					quads.add(FACE_BAKERY.bakeQuad(element.from, element.to, directionBlockElementFaceEntry.getValue(), sprite, directionBlockElementFaceEntry.getKey(), new SimpleModelState(state), element.rotation, element.shade));
				}
			}
		}

		return quads;
	}

	public static void renderSingularity(ItemStack stack, PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay) {
		Minecraft mc = Minecraft.getInstance();
		ItemSingularity singularity = (ItemSingularity) stack.getItem();
		TextureAtlas textureAtlas = mc.getModelManager().getAtlas(InventoryMenu.BLOCK_ATLAS);

		poseStack.pushPose();
		PoseStack.Pose posestack$pose = poseStack.last();

		for (int i = 0; i < 2; i++) {
			List<BakedQuad> quads = RenderUtils.bakeItem(new TextureAtlasSprite[] { textureAtlas.getSprite(ResourceLocation.tryBuild(Avaritia.MODID, "item/singularity/singularity_layer_" + i)) });

			float r = (i == 0 ? singularity.getColor(stack).getRed() : singularity.getLayerColor(stack).getRed()) / 255.0F;
			float g = (i == 0 ? singularity.getColor(stack).getGreen() : singularity.getLayerColor(stack).getGreen()) / 255.0F;
			float b = (i == 0 ? singularity.getColor(stack).getBlue() : singularity.getLayerColor(stack).getBlue()) / 255.0F;
			float a = (i == 0 ? singularity.getColor(stack).getAlpha() : singularity.getLayerColor(stack).getAlpha()) / 255.0F;

			for (BakedQuad quad : quads) {
				vertexConsumer.putBulkData(posestack$pose, quad, r, g, b, a, packedLight, packedOverlay, true);
			}
		}
		
		poseStack.popPose();
	}}
