package net.byAqua3.avaritia.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import net.byAqua3.avaritia.Avaritia;
import net.byAqua3.avaritia.block.BlockInfinityChest;
import net.byAqua3.avaritia.loader.AvaritiaBlocks;
import net.byAqua3.avaritia.tile.TileInfinityChest;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;

public class RenderInfinityChest implements BlockEntityRenderer<TileInfinityChest> {
	
	private final ModelPart lid;
	private final ModelPart bottom;
	private final ModelPart lock;

	public RenderInfinityChest(BlockEntityRendererProvider.Context context) {
		ModelPart modelpart = context.bakeLayer(ModelLayers.CHEST);
		this.bottom = modelpart.getChild("bottom");
		this.lid = modelpart.getChild("lid");
		this.lock = modelpart.getChild("lock");
	}

	public static LayerDefinition createSingleBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		partdefinition.addOrReplaceChild("bottom", CubeListBuilder.create().texOffs(0, 19).addBox(1.0F, 0.0F, 1.0F, 14.0F, 10.0F, 14.0F), PartPose.ZERO);
		partdefinition.addOrReplaceChild("lid", CubeListBuilder.create().texOffs(0, 0).addBox(1.0F, 0.0F, 0.0F, 14.0F, 5.0F, 14.0F), PartPose.offset(0.0F, 9.0F, 1.0F));
		partdefinition.addOrReplaceChild("lock", CubeListBuilder.create().texOffs(0, 0).addBox(7.0F, -2.0F, 14.0F, 2.0F, 4.0F, 1.0F), PartPose.offset(0.0F, 9.0F, 1.0F));
		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void render(TileInfinityChest blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, int packedOverlay) {
		Level level = blockEntity.getLevel();
		boolean flag = level != null;
		BlockState blockState = flag ? blockEntity.getBlockState() : AvaritiaBlocks.INFINITY_CHEST.get().defaultBlockState().setValue(ChestBlock.FACING, Direction.SOUTH);
		ChestType chestType = ChestType.SINGLE;
		Block block = blockState.getBlock();
		if (block instanceof BlockInfinityChest) {
			poseStack.pushPose();
			float f = blockState.getValue(ChestBlock.FACING).toYRot();
			poseStack.translate(0.5F, 0.5F, 0.5F);
			poseStack.mulPose(Axis.YP.rotationDegrees(-f));
			poseStack.translate(-0.5F, -0.5F, -0.5F);

			float f1 = blockEntity.getOpenNess(partialTicks);
			f1 = 1.0F - f1;
			f1 = 1.0F - f1 * f1 * f1;
			Material material = this.getMaterial(blockEntity, chestType);
			VertexConsumer vertexconsumer = material.buffer(multiBufferSource, RenderType::entityCutout);
			this.render(poseStack, vertexconsumer, this.lid, this.lock, this.bottom, f1, packedLight, packedOverlay);

			poseStack.popPose();
		}
	}

	private void render(PoseStack poseStack, VertexConsumer vertexConsumer, ModelPart lidPart, ModelPart lockPart, ModelPart bottomPart, float lidAngle, int packedLight, int packedOverlay) {
		lidPart.xRot = -(lidAngle * (float) (Math.PI / 2));
		lockPart.xRot = lidPart.xRot;
		lidPart.render(poseStack, vertexConsumer, packedLight, packedOverlay);
		lockPart.render(poseStack, vertexConsumer, packedLight, packedOverlay);
		bottomPart.render(poseStack, vertexConsumer, packedLight, packedOverlay);
	}

	protected Material getMaterial(TileInfinityChest blockEntity, ChestType chestType) {
		return new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.tryBuild(Avaritia.MODID, "entity/chest/infinity_chest"));
	}

	@Override
	public net.minecraft.world.phys.AABB getRenderBoundingBox(TileInfinityChest blockEntity) {
		net.minecraft.core.BlockPos pos = blockEntity.getBlockPos();
		return net.minecraft.world.phys.AABB.encapsulatingFullBlocks(pos.offset(-1, 0, -1), pos.offset(1, 1, 1));
	}
}
