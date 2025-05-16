package net.byAqua3.avaritia.model;

import java.awt.Color;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.byAqua3.avaritia.Avaritia;
import net.byAqua3.avaritia.event.AvaritiaClientEvent;
import net.byAqua3.avaritia.loader.AvaritiaAtlas;
import net.byAqua3.avaritia.loader.AvaritiaShaders;
import net.byAqua3.avaritia.loader.AvaritiaDataComponents;
import net.byAqua3.avaritia.loader.AvaritiaItems;
import net.byAqua3.avaritia.loader.AvaritiaRenderTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.CompiledShaderProgram;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderManager;
import net.minecraft.client.renderer.entity.state.ArmorStandRenderState;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import net.minecraft.client.renderer.entity.state.ZombieRenderState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ModelInfinityArmor extends HumanoidModel<HumanoidRenderState> {

	public static final ResourceLocation EYE = ResourceLocation.tryBuild(Avaritia.MODID, "textures/entity/equipment/infinity/infinity_armor_eyes.png");
	public static final ResourceLocation WING = ResourceLocation.tryBuild(Avaritia.MODID, "textures/entity/equipment/infinity/infinity_armor_wing.png");
	public static final ResourceLocation WING_GLOW = ResourceLocation.tryBuild(Avaritia.MODID, "textures/entity/equipment/infinity/infinity_armor_wingglow.png");
	public static final TextureAtlasSprite MASK = Minecraft.getInstance().getModelManager().getAtlas(AvaritiaAtlas.BLOCK_ATLAS).getSprite(ResourceLocation.tryBuild(Avaritia.MODID, "entity/equipment/infinity/infinity_armor_mask"));
	public static final TextureAtlasSprite MASK_INV = Minecraft.getInstance().getModelManager().getAtlas(AvaritiaAtlas.BLOCK_ATLAS).getSprite(ResourceLocation.tryBuild(Avaritia.MODID, "entity/equipment/infinity/infinity_armor_mask_inv"));
	public static final TextureAtlasSprite WING_MASK = Minecraft.getInstance().getModelManager().getAtlas(AvaritiaAtlas.BLOCK_ATLAS).getSprite(ResourceLocation.tryBuild(Avaritia.MODID, "entity/equipment/infinity/infinity_armor_mask_wings"));

	public final ModelPart root = createLayer().bakeRoot();
	public final ModelPart bodyRoot = createBodyLayer(new CubeDeformation(1.0F)).bakeRoot();

	private boolean isSilm;

	public ModelInfinityArmor(ModelPart root, boolean isSilm) {
		super(root);
		this.isSilm = isSilm;
	}

	public static LayerDefinition createLayer() {
		MeshDefinition meshDefinition = new MeshDefinition();
		PartDefinition partDefinition = meshDefinition.getRoot();
		CubeDeformation cubeDeformation = new CubeDeformation(0.0F);
		partDefinition.addOrReplaceChild("left_wing", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -11.6F, 0.0F, 0.0F, 32.0F, 32.0F, cubeDeformation), PartPose.offsetAndRotation(-1.5F, 0.0F, 2.0F, 0.0F, (float) (Math.PI * 0.4), 0.0F));
		partDefinition.addOrReplaceChild("right_wing", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(0.0F, -11.6F, 0.0F, 0.0F, 32.0F, 32.0F, cubeDeformation), PartPose.offsetAndRotation(1.5F, 0.0F, 2.0F, 0.0F, (float) (-Math.PI * 0.4), 0.0F));
		return LayerDefinition.create(meshDefinition, 64, 64);
	}

	public static LayerDefinition createBodyLayer(CubeDeformation cubDeformation) {
		MeshDefinition meshDefinition = HumanoidModel.createMesh(cubDeformation, 0.0F);
		PartDefinition partDefinition = meshDefinition.getRoot();

		partDefinition.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, cubDeformation.extend(-0.1F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		partDefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 48).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, cubDeformation.extend(-0.6F)), PartPose.offset(1.9F, 12.0F, 0.0F));
		partDefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, cubDeformation.extend(-0.6F)), PartPose.offset(-1.9F, 12.0F, 0.0F));

		partDefinition.addOrReplaceChild("left_boot", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, cubDeformation.extend(-0.1F)), PartPose.offset(1.9F, 12.0F, 0.0F));
		partDefinition.addOrReplaceChild("right_boot", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, cubDeformation.extend(-0.1F)), PartPose.offset(-1.9F, 12.0F, 0.0F));
		return LayerDefinition.create(meshDefinition, 64, 64);
	}

	public void setScale(ModelPart modelPart, float scale) {
		modelPart.xScale = scale;
		modelPart.yScale = scale;
		modelPart.zScale = scale;
	}

	@Override
	public void setupAnim(HumanoidRenderState humanoidRenderState) {
		super.setupAnim(humanoidRenderState);

		ModelPart leftWing = root.getChild("left_wing");
		leftWing.xRot = this.body.xRot;
		leftWing.yRot = this.body.yRot + (float) (Math.PI * 0.4);
		leftWing.zRot = this.body.zRot;
		ModelPart rightWing = root.getChild("right_wing");
		rightWing.xRot = this.body.xRot;
		rightWing.yRot = this.body.yRot + (float) (-Math.PI * 0.4);
		rightWing.zRot = this.body.zRot;

		if (humanoidRenderState instanceof ArmorStandRenderState) {
			ArmorStandRenderState armorStandRenderState = (ArmorStandRenderState) humanoidRenderState;
			this.head.y = 1.0F;
			this.head.xRot = (float) (Math.PI / 180.0) * armorStandRenderState.headPose.getX();
			this.head.yRot = (float) (Math.PI / 180.0) * armorStandRenderState.headPose.getY();
			this.head.zRot = (float) (Math.PI / 180.0) * armorStandRenderState.headPose.getZ();
			this.leftArm.xRot = (float) (Math.PI / 180.0) * armorStandRenderState.leftArmPose.getX();
			this.leftArm.yRot = (float) (Math.PI / 180.0) * armorStandRenderState.leftArmPose.getY();
			this.leftArm.zRot = (float) (Math.PI / 180.0) * armorStandRenderState.leftArmPose.getZ();
			this.rightArm.xRot = (float) (Math.PI / 180.0) * armorStandRenderState.rightArmPose.getX();
			this.rightArm.yRot = (float) (Math.PI / 180.0) * armorStandRenderState.rightArmPose.getY();
			this.rightArm.zRot = (float) (Math.PI / 180.0) * armorStandRenderState.rightArmPose.getZ();
			this.leftLeg.xRot = (float) (Math.PI / 180.0) * armorStandRenderState.leftLegPose.getX();
			this.leftLeg.yRot = (float) (Math.PI / 180.0) * armorStandRenderState.leftLegPose.getY();
			this.leftLeg.zRot = (float) (Math.PI / 180.0) * armorStandRenderState.leftLegPose.getZ();
			this.rightLeg.xRot = (float) (Math.PI / 180.0) * armorStandRenderState.rightLegPose.getX();
			this.rightLeg.yRot = (float) (Math.PI / 180.0) * armorStandRenderState.rightLegPose.getY();
			this.rightLeg.zRot = (float) (Math.PI / 180.0) * armorStandRenderState.rightLegPose.getZ();
		}

		ModelPart head = this.bodyRoot.getChild("head");
		head.getChild("hat").visible = false;
		head.copyFrom(this.head);
		ModelPart hat = this.bodyRoot.getChild("hat");
		hat.copyFrom(this.head);

		ModelPart body = this.bodyRoot.getChild("body");
		body.copyFrom(this.body);

		if (humanoidRenderState instanceof ZombieRenderState) {
			ZombieRenderState zombieRenderState = (ZombieRenderState) humanoidRenderState;
			AnimationUtils.animateZombieArms(this.leftArm, this.rightArm, zombieRenderState.isAggressive, zombieRenderState.attackTime, zombieRenderState.ageInTicks);
		}

		ModelPart leftArm = this.bodyRoot.getChild("left_arm");
		leftArm.copyFrom(this.leftArm);
		if (!isSilm) {
			this.setScale(this.leftArm, 1.01F);
		} else {
			this.leftArm.x -= 0.3F;
			this.leftArm.xScale = 0.8F;
			this.leftArm.yScale = 1.01F;
		}
		this.setScale(leftArm, 1.0F);
		ModelPart rightArm = this.bodyRoot.getChild("right_arm");
		rightArm.copyFrom(this.rightArm);
		if (!isSilm) {
			this.setScale(this.rightArm, 1.01F);
		} else {
			this.rightArm.x += 0.3F;
			this.rightArm.xScale = 0.8F;
			this.rightArm.yScale = 1.01F;
		}
		this.setScale(rightArm, 1.0F);

		ModelPart leftLeg = this.bodyRoot.getChild("left_leg");
		leftLeg.copyFrom(this.leftLeg);
		ModelPart rightLeg = this.bodyRoot.getChild("right_leg");
		rightLeg.copyFrom(this.rightLeg);
		ModelPart leftBoot = this.bodyRoot.getChild("left_boot");
		leftBoot.copyFrom(this.leftLeg);
		ModelPart rightBoot = this.bodyRoot.getChild("right_boot");
		rightBoot.copyFrom(this.rightLeg);

	}

	public void render(HumanoidRenderState humanoidRenderState, PoseStack poseStack, MultiBufferSource multiBufferSource, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {

		RenderType COSMIC_ARMOR_RENDER_TYPE = AvaritiaRenderTypes.COSMIC_ARMOR_RENDER_TYPE;

		Minecraft mc = Minecraft.getInstance();

		Item headItem = humanoidRenderState.headEquipment.getItem();
		Item chestItem = humanoidRenderState.chestEquipment.getItem();
		Item legsItem = humanoidRenderState.legsEquipment.getItem();
		Item feetItem = humanoidRenderState.feetEquipment.getItem();

		long time = mc.level.getGameTime();

		double pulse = Math.sin(time / 10.0D) * 0.5D + 0.5D;
		double pulse_mag_sqr = pulse * pulse * pulse * pulse * pulse * pulse;

		float yaw = 0.0F;
		float pitch = 0.0F;
		float scale = 1.0F;

		if (AvaritiaShaders.cosmicInventoryRender) {
			scale = 100.0F;
		} else {
			yaw = (float) ((humanoidRenderState.yRot * 2.0F) * Math.PI / 360.0D);
			pitch = -((float) ((humanoidRenderState.xRot * 2.0F) * Math.PI / 360.0D));
		}

		ShaderManager shaderManager = Minecraft.getInstance().getShaderManager();
		CompiledShaderProgram compiledShaderProgram = shaderManager.getProgram(AvaritiaShaders.cosmicArmorShader);
		compiledShaderProgram.getUniform("time").set(mc.level.getGameTime() % Integer.MAX_VALUE);
		compiledShaderProgram.getUniform("yaw").set(yaw);
		compiledShaderProgram.getUniform("pitch").set(pitch);
		compiledShaderProgram.getUniform("externalScale").set(scale);
		compiledShaderProgram.getUniform("opacity").set(1.0F);
		compiledShaderProgram.getUniform("cosmicuvs").set(AvaritiaShaders.COSMIC_UVS);

		if (humanoidRenderState instanceof PlayerRenderState) {
			PlayerRenderState playerRenderState = (PlayerRenderState) humanoidRenderState;
			ItemStack itemStack = playerRenderState.chestEquipment;

			if (chestItem == AvaritiaItems.INFINITY_CHESTPLATE.get() && (itemStack.has(AvaritiaDataComponents.FLY.get()) && itemStack.getOrDefault(AvaritiaDataComponents.FLY.get(), false))) {
				poseStack.pushPose();
				ModelPart leftWing = root.getChild("left_wing");
				ModelPart rightWing = root.getChild("right_wing");
				leftWing.render(poseStack, vertexConsumer, packedLight, packedOverlay);
				rightWing.render(poseStack, vertexConsumer, packedLight, packedOverlay);

				leftWing.render(poseStack, WING_MASK.wrap(multiBufferSource.getBuffer(COSMIC_ARMOR_RENDER_TYPE)), packedLight, packedOverlay, new Color(red, green, blue, alpha).getRGB());
				rightWing.render(poseStack, WING_MASK.wrap(multiBufferSource.getBuffer(COSMIC_ARMOR_RENDER_TYPE)), packedLight, packedOverlay, new Color(red, green, blue, alpha).getRGB());

				leftWing.render(poseStack, multiBufferSource.getBuffer(AvaritiaRenderTypes.WingGlow(WING_GLOW)), packedLight, packedOverlay, new Color(0.84F, 1.0F, 0.95F, (float) (pulse_mag_sqr * 0.5D)).getRGB());
				rightWing.render(poseStack, multiBufferSource.getBuffer(AvaritiaRenderTypes.WingGlow(WING_GLOW)), packedLight, packedOverlay, new Color(0.84F, 1.0F, 0.95F, (float) (pulse_mag_sqr * 0.5D)).getRGB());
				poseStack.popPose();
			}
		}

		if (headItem == AvaritiaItems.INFINITY_HELMET.get()) {
			poseStack.pushPose();

			ModelPart head = this.bodyRoot.getChild("head");
			head.render(poseStack, MASK.wrap(multiBufferSource.getBuffer(COSMIC_ARMOR_RENDER_TYPE)), packedLight, packedOverlay, new Color(red, green, blue, alpha).getRGB());
			ModelPart hat = this.bodyRoot.getChild("hat");
			hat.render(poseStack, MASK.wrap(multiBufferSource.getBuffer(COSMIC_ARMOR_RENDER_TYPE)), packedLight, packedOverlay, new Color(red, green, blue, alpha).getRGB());

			poseStack.popPose();
		}

		if (chestItem == AvaritiaItems.INFINITY_CHESTPLATE.get()) {
			poseStack.pushPose();

			ModelPart body = this.bodyRoot.getChild("body");
			body.render(poseStack, MASK.wrap(multiBufferSource.getBuffer(COSMIC_ARMOR_RENDER_TYPE)), packedLight, packedOverlay, new Color(red, green, blue, alpha).getRGB());
			body.render(poseStack, multiBufferSource.getBuffer(AvaritiaRenderTypes.WingGlow(EYE)), packedLight, packedOverlay, new Color(0.84F, 1.0F, 0.95F, (float) (pulse_mag_sqr * 0.5D)).getRGB());
			ModelPart leftArm = this.bodyRoot.getChild("left_arm");
			leftArm.render(poseStack, MASK.wrap(multiBufferSource.getBuffer(COSMIC_ARMOR_RENDER_TYPE)), packedLight, packedOverlay, new Color(red, green, blue, alpha).getRGB());
			leftArm.render(poseStack, multiBufferSource.getBuffer(AvaritiaRenderTypes.WingGlow(EYE)), packedLight, packedOverlay, new Color(0.84F, 1.0F, 0.95F, (float) (pulse_mag_sqr * 0.5D)).getRGB());
			ModelPart rightArm = this.bodyRoot.getChild("right_arm");
			rightArm.render(poseStack, MASK.wrap(multiBufferSource.getBuffer(COSMIC_ARMOR_RENDER_TYPE)), packedLight, packedOverlay, new Color(red, green, blue, alpha).getRGB());
			rightArm.render(poseStack, multiBufferSource.getBuffer(AvaritiaRenderTypes.WingGlow(EYE)), packedLight, packedOverlay, new Color(0.84F, 1.0F, 0.95F, (float) (pulse_mag_sqr * 0.5D)).getRGB());

			poseStack.popPose();

		}
		if (legsItem == AvaritiaItems.INFINITY_LEGGINGS.get()) {
			poseStack.pushPose();

			ModelPart leftLeg = this.bodyRoot.getChild("left_leg");
			leftLeg.render(poseStack, MASK.wrap(multiBufferSource.getBuffer(COSMIC_ARMOR_RENDER_TYPE)), packedLight, packedOverlay, new Color(red, green, blue, alpha).getRGB());
			leftLeg.render(poseStack, multiBufferSource.getBuffer(AvaritiaRenderTypes.WingGlow(EYE)), packedLight, packedOverlay, new Color(0.84F, 1.0F, 0.95F, (float) (pulse_mag_sqr * 0.5D)).getRGB());
			ModelPart rightLeg = this.bodyRoot.getChild("right_leg");
			rightLeg.render(poseStack, MASK.wrap(multiBufferSource.getBuffer(COSMIC_ARMOR_RENDER_TYPE)), packedLight, packedOverlay, new Color(red, green, blue, alpha).getRGB());
			rightLeg.render(poseStack, multiBufferSource.getBuffer(AvaritiaRenderTypes.WingGlow(EYE)), packedLight, packedOverlay, new Color(0.84F, 1.0F, 0.95F, (float) (pulse_mag_sqr * 0.5D)).getRGB());

			poseStack.popPose();
		}

		if (feetItem == AvaritiaItems.INFINITY_BOOTS.get()) {
			poseStack.pushPose();

			ModelPart leftBoot = this.bodyRoot.getChild("left_boot");
			leftBoot.render(poseStack, MASK.wrap(multiBufferSource.getBuffer(COSMIC_ARMOR_RENDER_TYPE)), packedLight, packedOverlay, new Color(red, green, blue, alpha).getRGB());
			ModelPart rightBoot = this.bodyRoot.getChild("right_boot");
			rightBoot.render(poseStack, MASK.wrap(multiBufferSource.getBuffer(COSMIC_ARMOR_RENDER_TYPE)), packedLight, packedOverlay, new Color(red, green, blue, alpha).getRGB());

			poseStack.popPose();
		}

		if (headItem == AvaritiaItems.INFINITY_HELMET.get() && chestItem == AvaritiaItems.INFINITY_CHESTPLATE.get() && legsItem == AvaritiaItems.INFINITY_LEGGINGS.get() && feetItem == AvaritiaItems.INFINITY_BOOTS.get()) {

			poseStack.pushPose();

			ModelPart hat = this.bodyRoot.getChild("hat");

			// Random random = new Random();
			// long frame = time / 3;
			// random.setSeed(frame * 1723609L);
			// float hue = random.nextFloat() * 6.0F;
			// float hue = time / 100.0F;
			float hue = (System.currentTimeMillis() - AvaritiaClientEvent.lastTime) / 2000.0F;

			int rgb = Color.HSBtoRGB(hue, 1.0F, 1.0F);
			float r = ((rgb >> 16) & 0xFF) / 255.0F;
			float g = ((rgb >> 8) & 0xFF) / 255.0F;
			float b = ((rgb >> 0) & 0xFF) / 255.0F;

			hat.render(poseStack, multiBufferSource.getBuffer(AvaritiaRenderTypes.Glow(EYE)), packedLight, packedOverlay, new Color(r, g, b, alpha).getRGB());

			// List<ModelPart> parts = List.of(this.head, this.body, this.rightArm,
			// this.leftArm, this.rightLeg, this.leftLeg);

			/// for(ModelPart part : parts) {
			// part.render(poseStack,
			/// MASK_INV.wrap(multiBufferSource.getBuffer(COSMIC_ARMOR_RENDER_TYPE)),
			// packedLight, packedOverlay, new Color(red, green, blue, alpha).getRGB());
			// }

			super.renderToBuffer(poseStack, MASK_INV.wrap(multiBufferSource.getBuffer(COSMIC_ARMOR_RENDER_TYPE)), packedLight, packedOverlay, new Color(red, green, blue, alpha).getRGB());

			poseStack.popPose();
		}
	}
}
