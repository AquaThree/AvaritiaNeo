package net.byAqua3.avaritia.render;

import java.awt.Color;
import java.util.List;

import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Axis;
import com.mojang.math.Transformation;

import net.byAqua3.avaritia.Avaritia;
import net.byAqua3.avaritia.entity.EntityGapingVoid;
import net.byAqua3.avaritia.loader.AvaritiaRenderTypes;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.model.SimpleModelState;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.client.model.geometry.IGeometryBakingContext;
import net.neoforged.neoforge.client.model.obj.ObjLoader;
import net.neoforged.neoforge.client.model.obj.ObjModel;
import net.neoforged.neoforge.client.model.obj.ObjModel.ModelSettings;

public class RenderGapingVoid<T extends EntityGapingVoid> extends EntityRenderer<T> {

	public static final ResourceLocation VOID = ResourceLocation.tryBuild(Avaritia.MODID, "textures/entity/void/void.png");
	public static final ResourceLocation VOID_HALO = ResourceLocation.tryBuild(Avaritia.MODID, "textures/entity/void/void_halo.png");
	public static final Material VOID_MATERIAL = new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.tryBuild(Avaritia.MODID, "entity/void/void"));

	private ObjModel objModel;
	private final IGeometryBakingContext objModelContext = new IGeometryBakingContext() {
		@Override
		public String getModelName() {
			return "gaping_void";
		}

		@Override
		public boolean hasMaterial(String name) {
			return true;
		}

		@Override
		public Material getMaterial(String name) {
			return VOID_MATERIAL;
		}

		@Override
		public boolean isGui3d() {
			return false;
		}

		@Override
		public boolean useBlockLight() {
			return false;
		}

		@Override
		public boolean useAmbientOcclusion() {
			return false;
		}

		@Override
		public ItemTransforms getTransforms() {
			return ItemTransforms.NO_TRANSFORMS;
		}

		@Override
		public Transformation getRootTransform() {
			return Transformation.identity();
		}

		@Override
		public @Nullable ResourceLocation getRenderTypeHint() {
			return null;
		}

		@Override
		public boolean isComponentVisible(String component, boolean fallback) {
			return true;
		}
	};

	public RenderGapingVoid(EntityRendererProvider.Context context) {
		super(context);
		this.initObjModel();
	}

	private void initObjModel() {
		ModelSettings modelSettings = new ModelSettings(ResourceLocation.tryBuild(Avaritia.MODID, "model/model.obj"), false, false, false, false, "avaritia:model/model.mtl");
		this.objModel = ObjLoader.INSTANCE.loadModel(modelSettings);
	}

	private Color getColor(double age) {
		double life = age / 186.0D;
		double f = Math.max(0.0D, (life - 0.95D) / 0.050000000000000044D);
		f = Math.max(f, 1.0D - life * 30.0D);
		return new Color((float) f, (float) f, (float) f, 1.0F);
	}

	@Override
	public void render(EntityGapingVoid livingEntity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight) {
		VertexConsumer vertexConsumer = multiBufferSource.getBuffer(AvaritiaRenderTypes.VOID);
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
		RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

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
		
		PoseStack.Pose poseStack$pose = poseStack.last();

		poseStack.translate(0.5F, 0.25F, 0.5F);
		poseStack.scale(scale - 0.1F, scale - 0.1F, scale - 0.1F);

		if (this.objModel != null) {
			BakedModel bakedModel = this.objModel.bake(this.objModelContext, null, material -> material.sprite(), new SimpleModelState(Transformation.identity()), ItemOverrides.EMPTY);
			
			RandomSource randomSource = RandomSource.create();
			randomSource.setSeed(42L);

			List<BakedQuad> quads = bakedModel.getQuads(null, null, randomSource, ModelData.EMPTY, null);

			for (BakedQuad quad : quads) {
				vertexConsumer.putBulkData(poseStack$pose, quad, r, g, b, a, packedLight, OverlayTexture.NO_OVERLAY, true);
			}
		}

		poseStack.popPose();
	}

	@Override
	public ResourceLocation getTextureLocation(EntityGapingVoid entity) {
		return VOID;
	}}
