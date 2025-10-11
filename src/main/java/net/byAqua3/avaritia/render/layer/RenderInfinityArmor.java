package net.byAqua3.avaritia.render.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.byAqua3.avaritia.model.ModelInfinityArmor;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderInfinityArmor<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {
	private final ModelInfinityArmor model;

	public RenderInfinityArmor(RenderLayerParent<T, M> renderer, EntityModelSet modelSet, boolean isSilm) {
		super(renderer);
		this.model = new ModelInfinityArmor(modelSet.bakeLayer(ModelLayers.PLAYER), isSilm);
	}

	private HumanoidModel.ArmPose getArmPose(LivingEntity livingEntity, InteractionHand hand) {
		ItemStack itemStack = livingEntity.getItemInHand(hand);
		if (itemStack.isEmpty()) {
			return HumanoidModel.ArmPose.EMPTY;
		} else {
			if (livingEntity.getUsedItemHand() == hand && livingEntity.getUseItemRemainingTicks() > 0) {
				UseAnim useAnim = itemStack.getUseAnimation();
				if (useAnim == UseAnim.BLOCK) {
					return HumanoidModel.ArmPose.BLOCK;
				}

				if (useAnim == UseAnim.BOW) {
					return HumanoidModel.ArmPose.BOW_AND_ARROW;
				}

				if (useAnim == UseAnim.SPEAR) {
					return HumanoidModel.ArmPose.THROW_SPEAR;
				}

				if (useAnim == UseAnim.CROSSBOW && hand == livingEntity.getUsedItemHand()) {
					return HumanoidModel.ArmPose.CROSSBOW_CHARGE;
				}

				if (useAnim == UseAnim.SPYGLASS) {
					return HumanoidModel.ArmPose.SPYGLASS;
				}

				if (useAnim == UseAnim.TOOT_HORN) {
					return HumanoidModel.ArmPose.TOOT_HORN;
				}

				if (useAnim == UseAnim.BRUSH) {
					return HumanoidModel.ArmPose.BRUSH;
				}
			} else if (!livingEntity.swinging && itemStack.getItem() instanceof CrossbowItem && CrossbowItem.isCharged(itemStack)) {
				return HumanoidModel.ArmPose.CROSSBOW_HOLD;
			}
			HumanoidModel.ArmPose forgeArmPose = net.neoforged.neoforge.client.extensions.common.IClientItemExtensions.of(itemStack).getArmPose(livingEntity, hand, itemStack);
			if (forgeArmPose != null)
				return forgeArmPose;

			return HumanoidModel.ArmPose.ITEM;
		}
	}

	private void setModelProperties(LivingEntity livingEntity, float ageInTicks) {
		this.model.crouching = livingEntity.isCrouching();
		this.model.young = livingEntity.isBaby();
		this.model.riding = livingEntity.isPassenger();

		HumanoidModel.ArmPose mainArmPose = this.getArmPose(livingEntity, InteractionHand.MAIN_HAND);
		HumanoidModel.ArmPose offArmPose = this.getArmPose(livingEntity, InteractionHand.OFF_HAND);
		if (mainArmPose.isTwoHanded()) {
			offArmPose = livingEntity.getOffhandItem().isEmpty() ? HumanoidModel.ArmPose.EMPTY : HumanoidModel.ArmPose.ITEM;
		}

		if (livingEntity.getMainArm() == HumanoidArm.RIGHT) {
			this.model.leftArmPose = offArmPose;
			this.model.rightArmPose = mainArmPose;
		} else {
			this.model.leftArmPose = mainArmPose;
			this.model.rightArmPose = offArmPose;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, T livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		this.getParentModel().copyPropertiesTo((EntityModel<T>) this.model);
		this.setModelProperties(livingEntity, ageInTicks);
		this.model.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		poseStack.pushPose();
		VertexConsumer vertexconsumer = multiBufferSource.getBuffer(RenderType.armorCutoutNoCull(ModelInfinityArmor.WING));
		this.model.render(livingEntity, poseStack, multiBufferSource, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		poseStack.popPose();
	}
}
