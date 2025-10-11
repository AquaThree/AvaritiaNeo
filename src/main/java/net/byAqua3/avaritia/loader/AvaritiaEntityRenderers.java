package net.byAqua3.avaritia.loader;

import net.byAqua3.avaritia.render.RenderGapingVoid;
import net.byAqua3.avaritia.render.RenderInfinityArrow;
import net.byAqua3.avaritia.render.layer.RenderInfinityArmor;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.client.resources.PlayerSkin.Model;
import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

public class AvaritiaEntityRenderers {

	public static void registerEntityRenderers(IEventBus modEventBus) {
		modEventBus.addListener(AvaritiaEntityRenderers::onRegisterEntityRenderers);
		modEventBus.addListener(AvaritiaEntityRenderers::onAddLayers);
	}

	@SubscribeEvent
	public static void onRegisterEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(AvaritiaEntities.INFINITY_ARROW.get(), RenderInfinityArrow::new);
		event.registerEntityRenderer(AvaritiaEntities.ENDEST_PEARL.get(), ThrownItemRenderer::new);
		event.registerEntityRenderer(AvaritiaEntities.GAPING_VOID.get(), RenderGapingVoid::new);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@SubscribeEvent
	public static void onAddLayers(EntityRenderersEvent.AddLayers event) {
		for (EntityType<?> entityType : event.getEntityTypes()) {
			EntityRenderer<?> entityRenderer = event.getRenderer(entityType);
			if (entityRenderer instanceof LivingEntityRenderer) {
				LivingEntityRenderer<?, ?> livingEntityRenderer = (LivingEntityRenderer<?, ?>) entityRenderer;
				if (livingEntityRenderer.getModel() instanceof HumanoidModel) {
					livingEntityRenderer.addLayer(new RenderInfinityArmor(livingEntityRenderer, event.getEntityModels(), false));
				}
			}
		}
		for (Model model : event.getSkins()) {
			LivingEntityRenderer entityRenderer = event.getSkin(model);
			entityRenderer.addLayer(new RenderInfinityArmor(entityRenderer, event.getEntityModels(), model == PlayerSkin.Model.SLIM));
		}
	}}
