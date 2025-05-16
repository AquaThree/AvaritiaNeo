package net.byAqua3.avaritia.render.special;

import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public interface SpecialModelEntity {

	void extractArgument(LivingEntity entity);

}
