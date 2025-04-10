package net.byAqua3.avaritia.render.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.byAqua3.avaritia.model.ModelInfinityArmor;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderInfinityArmor extends RenderLayer<HumanoidRenderState, HumanoidModel<HumanoidRenderState>> {
	private final ModelInfinityArmor model;
	
	public RenderInfinityArmor(RenderLayerParent<HumanoidRenderState, HumanoidModel<HumanoidRenderState>> renderer, EntityModelSet modelSet, boolean isSilm) {
		super(renderer);
		this.model = new ModelInfinityArmor(modelSet.bakeLayer(ModelLayers.PLAYER), isSilm);
	}

	@Override
	public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, HumanoidRenderState humanoidRenderState, float netHeadYaw,
			float headPitch) {
	    this.getParentModel().copyPropertiesTo(this.model);
        this.model.setupAnim(humanoidRenderState);
        poseStack.pushPose();
		VertexConsumer vertexconsumer = multiBufferSource.getBuffer(RenderType.armorCutoutNoCull(ModelInfinityArmor.WING));
		this.model.render(humanoidRenderState, poseStack, multiBufferSource, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		poseStack.popPose();
		
	}
}
