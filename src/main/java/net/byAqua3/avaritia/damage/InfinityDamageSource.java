package net.byAqua3.avaritia.damage;

import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class InfinityDamageSource extends DamageSource {

	public InfinityDamageSource(Holder<DamageType> damageType, Entity entity, Entity attacker) {
		super(damageType, entity, attacker);
	}

	@Override
	public Component getLocalizedDeathMessage(LivingEntity entity) {
		LivingEntity attacker = entity.getKillCredit();
		int randomInt = entity.level().getRandom().nextInt(5) + 1;
		String key = "death.attack.infinity." + randomInt;
		return Component.translatable(key, entity.getDisplayName(), attacker.getDisplayName());
	}

}
