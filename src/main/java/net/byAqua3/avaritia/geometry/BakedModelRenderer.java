package net.byAqua3.avaritia.geometry;

import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public abstract class BakedModelRenderer implements BakedModel {

	private final BakedModel bakedModel;

	public BakedModelRenderer(BakedModel bakedModel) {
		this.bakedModel = bakedModel;
	}

	@SuppressWarnings("deprecation")
	@Override
	public List<BakedQuad> getQuads(BlockState state, Direction direction, RandomSource randomSource) {
		return this.bakedModel.getQuads(state, direction, randomSource);
	}

	@Override
	public boolean useAmbientOcclusion() {
		return this.bakedModel.useAmbientOcclusion();
	}

	@Override
	public boolean isGui3d() {
		return this.bakedModel.isGui3d();
	}

	@Override
	public boolean usesBlockLight() {
		return this.bakedModel.usesBlockLight();
	}

	@Override
	public boolean isCustomRenderer() {
		return this.bakedModel.isCustomRenderer();
	}

	@SuppressWarnings("deprecation")
	@Override
	public TextureAtlasSprite getParticleIcon() {
		return this.bakedModel.getParticleIcon();
	}

	@SuppressWarnings("deprecation")
	@Override
	public ItemTransforms getTransforms() {
		return this.bakedModel.getTransforms();
	}

	@Override
	public ItemOverrides getOverrides() {
		return this.bakedModel.getOverrides();
	}

	public abstract void render(ItemStack stack, ItemDisplayContext context, boolean leftHand, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, int packedOverlay, BakedModel model);}
