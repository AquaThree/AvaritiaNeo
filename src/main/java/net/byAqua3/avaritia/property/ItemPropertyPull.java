package net.byAqua3.avaritia.property;

import com.mojang.serialization.MapCodec;
import javax.annotation.Nullable;

import net.byAqua3.avaritia.item.ItemInfinityBow;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public record ItemPropertyPull() implements RangeSelectItemModelProperty {
    public static final MapCodec<ItemPropertyPull> MAP_CODEC = MapCodec.unit(ItemPropertyPull::new);

    @Override
    public float get(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed) {
    	if(entity != null && entity.isUsingItem() && entity.getUseItem() == stack) {
    		return (stack.getUseDuration(entity) - entity.getUseItemRemainingTicks()) / ItemInfinityBow.DRAW_TIME;
    	}
        return 0.0F;
    }

    @Override
    public MapCodec<ItemPropertyPull> type() {
        return MAP_CODEC;
    }
}
