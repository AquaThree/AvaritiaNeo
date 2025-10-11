package net.byAqua3.avaritia.damage;

import net.byAqua3.avaritia.loader.AvaritiaDamageTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class DamageSourceInfinity extends DamageSource {

	public DamageSourceInfinity(Entity attacker) {
		super(attacker.level().registryAccess().holderOrThrow(AvaritiaDamageTypes.INFINTY), attacker);
	}

	@Override
	public Component getLocalizedDeathMessage(LivingEntity entity) {
		Entity attacker = this.getEntity();
		int randomInt = entity.level().getRandom().nextInt(5) + 1;
		String key = "death.attack.infinity." + randomInt;
		return Component.translatable(key, entity.getDisplayName(), attacker.getDisplayName());
	}}
