package net.byAqua3.avaritia.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Axis;

import net.byAqua3.avaritia.Avaritia;
import net.byAqua3.avaritia.entity.EntityGapingVoid;
import net.byAqua3.avaritia.loader.AvaritiaItems;
import net.byAqua3.avaritia.loader.AvaritiaRenderTypes;
import net.byAqua3.avaritia.render.state.RenderStateGapingVoid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.CoreShaders;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.awt.Color;
import java.util.List;

import org.joml.Matrix4f;

@OnlyIn(Dist.CLIENT)
public class RenderGapingVoid extends EntityRenderer<EntityGapingVoid, RenderStateGapingVoid> {
	
	public static final ResourceLocation VOID = ResourceLocation.tryBuild(Avaritia.MODID, "textures/entity/void/void.png");
	public static final ResourceLocation VOID_HALO = ResourceLocation.tryBuild(Avaritia.MODID, "textures/entity/void/void_halo.png");
	
	private final ItemStackRenderState scratchItemStackRenderState = new ItemStackRenderState();
	
    public RenderGapingVoid(EntityRendererProvider.Context context) {
        super(context);
    }
    
    private Color getColor(double age) {
		double life = age / 186.0D;
		double f = Math.max(0.0D, (life - 0.95D) / 0.050000000000000044D);
		f = Math.max(f, 1.0D - life * 30.0D);
		return new Color((float) f, (float) f, (float) f, 1.0F);
	}

    @SuppressWarnings("deprecation")
	@Override
    public void render(RenderStateGapingVoid renderState, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight) {
		ItemStack itemStack = new ItemStack(AvaritiaItems.GAPING_VOID.get());
		Color color = this.getColor(renderState.age);
		float scale = (float) EntityGapingVoid.getVoidScale(renderState.age);
		float r = color.getRed() / 255.0F;
		float g = color.getGreen() / 255.0F;
		float b = color.getBlue() / 255.0F;
		float a = color.getAlpha() / 255.0F;
		double halocoord = 0.58D * scale;
		double haloscaledist = 2.2D * scale;
		Vec3 camera = this.entityRenderDispatcher.camera.getPosition();
		double dx = renderState.x - camera.x();
		double dy = renderState.y - camera.y();
		double dz = renderState.z - camera.z();
		double xzlen = Math.sqrt(dx * dx + dz * dz);
		double len = Math.sqrt(dx * dx + dy * dy + dz * dz);
		if (len <= haloscaledist) {
			double close = (haloscaledist - len) / haloscaledist;
			halocoord *= 1.0D + close * close * close * close * 1.5D;
		}
		
		double yang = Math.atan2(xzlen, dy) * 57.29577951308232D;
	    double xang = Math.atan2(dx, dz) * 57.29577951308232D;

		poseStack.pushPose();

		poseStack.translate(0.5F, 0.5F, 0.5F);
		poseStack.mulPose(Axis.YP.rotationDegrees((float) xang));
		poseStack.mulPose(Axis.XP.rotationDegrees((float) (yang + 275.0D)));

		RenderSystem.enableBlend();
		RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
				GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
				GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		
		RenderSystem.enableDepthTest();

		RenderSystem.setShaderTexture(0, VOID_HALO);
		RenderSystem.setShader(CoreShaders.POSITION_TEX_COLOR);
		Matrix4f matrix4f = poseStack.last().pose();
		BufferBuilder bufferBuilder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
		bufferBuilder.addVertex(matrix4f, (float) -halocoord, (float) -halocoord, 0.0F).setColor(r, g, b, a).setUv(0.0F, 0.0F);
		bufferBuilder.addVertex(matrix4f, (float) -halocoord, (float) halocoord, 0.0F).setColor(r, g, b, a).setUv(0.0F, 1.0F);
		bufferBuilder.addVertex(matrix4f, (float) halocoord, (float) halocoord, 0.0F).setColor(r, g, b, a).setUv(1.0F, 1.0F);
		bufferBuilder.addVertex(matrix4f, (float) halocoord, (float) -halocoord, 0.0F).setColor(r, g, b, a).setUv(1.0F, 0.0F);
		BufferUploader.drawWithShader(bufferBuilder.buildOrThrow());
		
		RenderSystem.disableDepthTest();

		RenderSystem.disableBlend();
		RenderSystem.defaultBlendFunc();

		poseStack.popPose();

		poseStack.pushPose();

		PoseStack.Pose posestack$pose = poseStack.last();

		poseStack.translate(0.5F, 0.25F, 0.5F);
		poseStack.scale(scale - 0.1F, scale - 0.1F, scale - 0.1F);

		Minecraft.getInstance().getItemModelResolver().updateForTopItem(this.scratchItemStackRenderState, itemStack, ItemDisplayContext.NONE, false, renderState.level, null, 42);
		
		for (int i = 0; i < this.scratchItemStackRenderState.activeLayerCount; i++) {
			BakedModel bakedModel = this.scratchItemStackRenderState.layers[i].model;
			RandomSource randomSource = RandomSource.create();
			randomSource.setSeed(42L);

			List<BakedQuad> quads = bakedModel.getQuads(null, null, randomSource);

			for (BakedQuad quad : quads) {
				multiBufferSource.getBuffer(AvaritiaRenderTypes.VOID).putBulkData(posestack$pose, quad, r, g, b, a, packedLight, OverlayTexture.NO_OVERLAY, true);
			}
		}
		
		poseStack.popPose();
    }


    @Override
    public RenderStateGapingVoid createRenderState() {
        return new RenderStateGapingVoid();
    }
    
    @Override
    public void extractRenderState(EntityGapingVoid entity, RenderStateGapingVoid renderState, float partialTicks) {
        super.extractRenderState(entity, renderState, partialTicks);
        renderState.id = entity.getId();
        renderState.level = entity.level();
        renderState.x = entity.getX();
        renderState.y = entity.getY();
        renderState.z = entity.getZ();
        renderState.age = entity.getAge();
    }
}
