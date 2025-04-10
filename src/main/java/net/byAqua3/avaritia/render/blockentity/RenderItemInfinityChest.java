package net.byAqua3.avaritia.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.client.model.ChestModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.special.NoDataSpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderItemInfinityChest implements NoDataSpecialModelRenderer {
	
	private final ChestModel model;

    public RenderItemInfinityChest(ChestModel model) {
    	this.model = model;
    }

    @Override
    public void render(ItemDisplayContext context, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, int packedOverlay, boolean hasFoilType) {
    	VertexConsumer vertexconsumer = RenderInfinityChest.getMaterial().buffer(multiBufferSource, RenderType::entitySolid);
        this.model.setupAnim(0.0F);
        this.model.renderToBuffer(poseStack, vertexconsumer, packedLight, packedOverlay);
    }

    @OnlyIn(Dist.CLIENT)
    public static record Unbaked(ResourceLocation texture) implements SpecialModelRenderer.Unbaked {
        public static final MapCodec<RenderItemInfinityChest.Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(
            p_388545_ -> p_388545_.group(
                        ResourceLocation.CODEC.fieldOf("texture").forGetter(RenderItemInfinityChest.Unbaked::texture)
                    )
                    .apply(p_388545_, RenderItemInfinityChest.Unbaked::new)
        );

        @Override
        public MapCodec<RenderItemInfinityChest.Unbaked> type() {
            return MAP_CODEC;
        }

        @Override
        public SpecialModelRenderer<?> bake(EntityModelSet entityModelSet) {
        	ChestModel chestModel = new ChestModel(entityModelSet.bakeLayer(ModelLayers.CHEST));
            return new RenderItemInfinityChest(chestModel);
        }
    }
}
