package net.byAqua3.avaritia.property;

import com.mojang.serialization.MapCodec;
import javax.annotation.Nullable;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public record ItemPropertyPulling() implements ConditionalItemModelProperty {
    public static final MapCodec<ItemPropertyPulling> MAP_CODEC = MapCodec.unit(new ItemPropertyPulling());

    @Override
    public boolean get(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed, ItemDisplayContext context) {
    	if(entity != null && entity.isUsingItem() && entity.getUseItem() == stack) {
    		return true;
    	}
        return false;
    }

    @Override
    public MapCodec<ItemPropertyPulling> type() {
        return MAP_CODEC;
    }
}
