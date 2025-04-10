package net.byAqua3.avaritia.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import net.byAqua3.avaritia.Avaritia;
import net.minecraft.client.model.ArrowModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.ArrowRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderInfinityArrow<T extends AbstractArrow, S extends ArrowRenderState> extends EntityRenderer<T, S> {
	
	public static final ResourceLocation INFINITY_ARROW = ResourceLocation.tryBuild(Avaritia.MODID, "textures/entity/infinity_arrow.png");
	
    private final ArrowModel model;

    public RenderInfinityArrow(EntityRendererProvider.Context context) {
        super(context);
        this.model = new ArrowModel(context.bakeLayer(ModelLayers.ARROW));
    }

    @Override
    public void render(S renderState, PoseStack poseStack, MultiBufferSource multiBufferSource, int p_113824_) {
    	poseStack.pushPose();
    	poseStack.mulPose(Axis.YP.rotationDegrees(renderState.yRot - 90.0F));
    	poseStack.mulPose(Axis.ZP.rotationDegrees(renderState.xRot));
        VertexConsumer vertexconsumer = multiBufferSource.getBuffer(RenderType.entityCutout(this.getTextureLocation(renderState)));
        this.model.setupAnim(renderState);
        this.model.renderToBuffer(poseStack, vertexconsumer, p_113824_, OverlayTexture.NO_OVERLAY);
        poseStack.popPose();
        super.render(renderState, poseStack, multiBufferSource, p_113824_);
    }

    @Override
    public void extractRenderState(T arrow, S renderState, float p_360538_) {
        super.extractRenderState(arrow, renderState, p_360538_);
        renderState.xRot = arrow.getXRot(p_360538_);
        renderState.yRot = arrow.getYRot(p_360538_);
        renderState.shake = (float)arrow.shakeTime - p_360538_;
    }
    
    public ResourceLocation getTextureLocation(S p_368566_) {
    	return INFINITY_ARROW;
    }

	@SuppressWarnings("unchecked")
	@Override
	public S createRenderState() {
		return (S) new ArrowRenderState();
	}
}
