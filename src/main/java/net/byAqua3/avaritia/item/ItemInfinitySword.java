package net.byAqua3.avaritia.item;

import net.byAqua3.avaritia.damage.DamageSourceInfinity;
import net.byAqua3.avaritia.loader.AvaritiaTiers;
import net.byAqua3.avaritia.util.ItemUtils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.neoforged.neoforge.entity.PartEntity;

public class ItemInfinitySword extends SwordItem {

	public ItemInfinitySword(Properties properties) {
		super(AvaritiaTiers.INFINITY, properties.attributes(SwordItem.createAttributes(AvaritiaTiers.INFINITY, 2, -2.4F)));
	}

	@Override
	public int getDamage(ItemStack stack) {
		return 0;
	}

	@Override
	public boolean isFoil(ItemStack stack) {
		return false;
	}

	@Override
	public boolean hurtEnemy(ItemStack stack, LivingEntity entity, LivingEntity attacker) {
		if (entity != null) {
			DamageSourceInfinity damageSource = new DamageSourceInfinity(attacker);

			if (!entity.level().isClientSide()) {
				if (entity instanceof Player && ItemUtils.isInfinityArmor((Player) entity)) {
					entity.hurt(damageSource, 4.0F);
					return true;
				}
				entity.hurt(damageSource, Float.MAX_VALUE);
				entity.setHealth(0.0F);
				entity.die(damageSource);
			}
		}
		return super.hurtEnemy(stack, entity, attacker);
	}

	@Override
	public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
		DamageSourceInfinity damageSource = new DamageSourceInfinity(player);

		if (!entity.level().isClientSide()) {
			if (entity instanceof LivingEntity) {
				LivingEntity livingEntity = (LivingEntity) entity;
				if (livingEntity.getHealth() <= 0.0F) {
					livingEntity.remove(RemovalReason.KILLED);
				} else {
					if (livingEntity instanceof Player && ItemUtils.isInfinityArmor((Player) livingEntity)) {
						livingEntity.hurt(damageSource, 4.0F);
						return true;
					}
					livingEntity.hurt(damageSource, Float.MAX_VALUE);
					livingEntity.setHealth(0.0F);
					livingEntity.die(damageSource);
				}
			} else {
				if (entity instanceof PartEntity) {
					PartEntity<?> partEntity = (PartEntity<?>) entity;

					if (partEntity.getParent() instanceof LivingEntity) {
						LivingEntity livingEntity = (LivingEntity) partEntity.getParent();
						livingEntity.hurt(damageSource, Float.MAX_VALUE);
						livingEntity.getAttribute(Attributes.MAX_HEALTH).setBaseValue(0.0D);
						livingEntity.setHealth(0.0F);
					} else {
						partEntity.getParent().hurt(damageSource, Float.MAX_VALUE);
					}
					return true;
				}
				entity.hurt(damageSource, Float.MAX_VALUE);
			}
		}
		return super.onLeftClickEntity(stack, player, entity);
	}
}
