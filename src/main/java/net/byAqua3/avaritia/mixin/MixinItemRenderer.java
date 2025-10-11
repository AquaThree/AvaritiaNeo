package net.byAqua3.avaritia.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.byAqua3.avaritia.geometry.BakedModelRenderer;
import net.byAqua3.avaritia.item.ItemSingularity;
import net.byAqua3.avaritia.loader.AvaritiaModels;
import net.byAqua3.avaritia.util.RenderUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

@Mixin({ ItemRenderer.class })
public class MixinItemRenderer {

	@Inject(method = { "render" }, at = { @At("HEAD") }, cancellable = true)
	public void render(ItemStack stack, ItemDisplayContext context, boolean leftHand, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, int packedOverlay, BakedModel model, CallbackInfo callbackInfo) {
		if (!(model instanceof BakedModelRenderer)) {
			ResourceLocation resourceLocation = BuiltInRegistries.ITEM.getKey(stack.getItem());
			if (AvaritiaModels.LOAD_ITEM_MODELS.containsKey(resourceLocation)) {
				model = AvaritiaModels.LOAD_ITEM_MODELS.get(resourceLocation).getBakedModel(model);
			}
		}
		if (model instanceof BakedModelRenderer) {
			callbackInfo.cancel();

			poseStack.pushPose();
			model = net.neoforged.neoforge.client.ClientHooks.handleCameraTransforms(poseStack, model, context, leftHand);
			poseStack.translate(-0.5F, -0.5F, -0.5F);

			BakedModelRenderer renderer = (BakedModelRenderer) model;
			renderer.render(stack, context, leftHand, poseStack, multiBufferSource, packedLight, packedOverlay, model);

			poseStack.popPose();
		} else if (stack.getItem() instanceof ItemSingularity) {
			callbackInfo.cancel();

			poseStack.pushPose();
			model = net.neoforged.neoforge.client.ClientHooks.handleCameraTransforms(poseStack, model, context, leftHand);
			poseStack.translate(-0.5F, -0.5F, -0.5F);

			for (BakedModel bakedModel : model.getRenderPasses(stack, true)) {
				for (RenderType renderType : bakedModel.getRenderTypes(stack, true)) {
					VertexConsumer vertexConsumer = multiBufferSource.getBuffer(renderType);
					RenderUtils.renderSingularity(stack, poseStack, vertexConsumer, packedLight, packedOverlay);
				}
			}

			poseStack.popPose();
		}
	}}
