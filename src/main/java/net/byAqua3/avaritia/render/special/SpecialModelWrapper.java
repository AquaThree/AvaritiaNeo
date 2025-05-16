package net.byAqua3.avaritia.render.special;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import javax.annotation.Nullable;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderers;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ResolvableModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SpecialModelWrapper<T> implements ItemModel {
	private final SpecialModelRenderer<T> specialRenderer;
	private final BakedModel baseModel;

	public SpecialModelWrapper(SpecialModelRenderer<T> specialRenderer, BakedModel baseModel) {
		this.specialRenderer = specialRenderer;
		this.baseModel = baseModel;
	}

	@Override
	public void update(ItemStackRenderState renderState, ItemStack stack, ItemModelResolver resolver, ItemDisplayContext context, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed) {
		ItemStackRenderState.LayerRenderState layerRenderState = renderState.newLayer();
		if (stack.hasFoil()) {
			layerRenderState.setFoilType(ItemStackRenderState.FoilType.STANDARD);
		}
		if (this.specialRenderer instanceof SpecialModelEntity) {
			((SpecialModelEntity) this.specialRenderer).extractArgument(entity);
		}
		layerRenderState.setupSpecialModel(this.specialRenderer, this.specialRenderer.extractArgument(stack), this.baseModel);
	}

	@OnlyIn(Dist.CLIENT)
	public static record Unbaked(ResourceLocation base, SpecialModelRenderer.Unbaked specialModel) implements ItemModel.Unbaked {
		public static final MapCodec<SpecialModelWrapper.Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(ResourceLocation.CODEC.fieldOf("base").forGetter(SpecialModelWrapper.Unbaked::base), SpecialModelRenderers.CODEC.fieldOf("model").forGetter(SpecialModelWrapper.Unbaked::specialModel)).apply(instance, SpecialModelWrapper.Unbaked::new));

		@Override
		public void resolveDependencies(ResolvableModel.Resolver resolver) {
			resolver.resolve(this.base);
		}

		@Override
		public ItemModel bake(ItemModel.BakingContext context) {
			BakedModel bakedModel = context.bake(this.base);
			SpecialModelRenderer<?> specialModelRenderer = this.specialModel.bake(context.entityModelSet());
			return (ItemModel) (specialModelRenderer == null ? context.missingItemModel() : new SpecialModelWrapper<>(specialModelRenderer, bakedModel));
		}

		@Override
		public MapCodec<SpecialModelWrapper.Unbaked> type() {
			return MAP_CODEC;
		}
	}
}
