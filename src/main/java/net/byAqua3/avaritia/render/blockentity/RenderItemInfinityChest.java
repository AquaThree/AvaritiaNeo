package net.byAqua3.avaritia.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;

import net.byAqua3.avaritia.loader.AvaritiaBlocks;
import net.byAqua3.avaritia.tile.TileInfinityChest;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class RenderItemInfinityChest extends BlockEntityWithoutLevelRenderer {

	private final BlockEntityRenderDispatcher blockEntityRenderDispatcher;

	private final TileInfinityChest infinityChest = new TileInfinityChest(BlockPos.ZERO, AvaritiaBlocks.INFINITY_CHEST.get().defaultBlockState());

	public RenderItemInfinityChest(BlockEntityRenderDispatcher blockEntityRenderDispatcher, EntityModelSet entityModelSet) {
		super(blockEntityRenderDispatcher, entityModelSet);
		this.blockEntityRenderDispatcher = blockEntityRenderDispatcher;
	}

	@Override
	public void renderByItem(ItemStack stack, ItemDisplayContext context, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, int packedOverlay) {
		this.blockEntityRenderDispatcher.renderItem(this.infinityChest, poseStack, multiBufferSource, packedLight, packedOverlay);
	}}
