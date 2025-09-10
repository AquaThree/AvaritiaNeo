package net.byAqua3.avaritia.render;

import java.awt.Color;
import java.util.List;

import org.joml.Matrix4f;
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
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class RenderGapingVoid<T extends EntityGapingVoid> extends EntityRenderer<T> {

	public static final ResourceLocation VOID = ResourceLocation.tryBuild(Avaritia.MODID, "textures/entity/void/void.png");
	public static final ResourceLocation VOID_HALO = ResourceLocation.tryBuild(Avaritia.MODID, "textures/entity/void/void_halo.png");

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
	public void render(EntityGapingVoid livingEntity, float entityYaw, float partialTicks, PoseStack poseStack,
			MultiBufferSource multiBufferSource, int packedLight) {
		ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
		ItemStack itemStack = new ItemStack(AvaritiaItems.GAPING_VOID.get());
		Color color = this.getColor(livingEntity.getAge());
		float scale = (float) EntityGapingVoid.getVoidScale(livingEntity.getAge());
		float r = color.getRed() / 255.0F;
		float g = color.getGreen() / 255.0F;
		float b = color.getBlue() / 255.0F;
		float a = color.getAlpha() / 255.0F;
		double halocoord = 0.58D * scale;
		double haloscaledist = 2.2D * scale;
		Vec3 camera = this.entityRenderDispatcher.camera.getPosition();
		double dx = livingEntity.getX() - camera.x();
		double dy = livingEntity.getY() - camera.y();
		double dz = livingEntity.getZ() - camera.z();
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
		RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
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

		BakedModel bakedModel = itemRenderer.getModel(itemStack, livingEntity.level(), null, livingEntity.getId());

		RandomSource randomSource = RandomSource.create();
		randomSource.setSeed(42L);

		List<BakedQuad> quads = bakedModel.getQuads(null, null, randomSource);

		for (BakedQuad quad : quads) {
			multiBufferSource.getBuffer(AvaritiaRenderTypes.VOID).putBulkData(posestack$pose, quad, r, g, b, a,
					packedLight, OverlayTexture.NO_OVERLAY, true);
		}

		poseStack.popPose();
	}

	@Override
	public ResourceLocation getTextureLocation(EntityGapingVoid entity) {
		return VOID;
	}

}
