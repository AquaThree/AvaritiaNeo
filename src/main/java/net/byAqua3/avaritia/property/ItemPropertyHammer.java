package net.byAqua3.avaritia.property;

import com.mojang.serialization.MapCodec;
import javax.annotation.Nullable;

import net.byAqua3.avaritia.loader.AvaritiaDataComponents;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public record ItemPropertyHammer() implements ConditionalItemModelProperty {
    public static final MapCodec<ItemPropertyHammer> MAP_CODEC = MapCodec.unit(ItemPropertyHammer::new);

    @Override
    public boolean get(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed, ItemDisplayContext context) {
        return stack.has(AvaritiaDataComponents.HAMMER.get()) && stack.getOrDefault(AvaritiaDataComponents.HAMMER.get(), false);
    }

    @Override
    public MapCodec<ItemPropertyHammer> type() {
        return MAP_CODEC;
    }
}
