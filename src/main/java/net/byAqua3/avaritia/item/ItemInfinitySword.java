package net.byAqua3.avaritia.item;

import net.byAqua3.avaritia.damage.InfinityDamageSource;
import net.byAqua3.avaritia.event.AvaritiaEvent;
import net.byAqua3.avaritia.loader.AvaritiaToolMaterials;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;

public class ItemInfinitySword extends SwordItem {

	public ItemInfinitySword(Properties properties) {
		super(AvaritiaToolMaterials.INFINITY, 2, -2.4F, properties);
	}

	@Override
	public boolean hasCustomEntity(ItemStack stack) {
		return true;
	}

	@Override
	public boolean onEntityItemUpdate(ItemStack stack, ItemEntity itemEntity) {
		itemEntity.setInvulnerable(true);
		return super.onEntityItemUpdate(stack, itemEntity);
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
			if (entity.level() instanceof ServerLevel) {
				ServerLevel serverlevel = (ServerLevel) entity.level();
				Holder<DamageType> damageType = entity.level().registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE)
						.getOrThrow(DamageTypes.GENERIC_KILL);
				InfinityDamageSource infinityDamageSource = new InfinityDamageSource(damageType, entity, attacker);
				if (entity instanceof Player && AvaritiaEvent.isInfinityArmor((Player) entity)) {
					entity.hurtServer(serverlevel, infinityDamageSource, 4.0F);
					return true;
				}
				entity.hurtServer(serverlevel, infinityDamageSource, Float.MAX_VALUE);
				entity.setHealth(0.0F);
				entity.die(infinityDamageSource);
			}
		}
		return super.hurtEnemy(stack, entity, attacker);
	}

	@Override
	public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
		if (entity instanceof LivingEntity) {
			LivingEntity livingEntity = ((LivingEntity) entity);
			if (livingEntity.getHealth() <= 0) {
				livingEntity.remove(RemovalReason.KILLED);
			} else {
				if (entity.level() instanceof ServerLevel) {
					ServerLevel serverlevel = (ServerLevel) entity.level();
					Holder<DamageType> damageType = livingEntity.level().registryAccess()
							.lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(DamageTypes.GENERIC_KILL);
					InfinityDamageSource infinityDamageSource = new InfinityDamageSource(damageType, livingEntity,
							player);
					if (entity instanceof Player && AvaritiaEvent.isInfinityArmor((Player) entity)) {
						entity.hurtServer(serverlevel, infinityDamageSource, 4.0F);
						return true;
					}
					livingEntity.hurtServer(serverlevel, infinityDamageSource, Float.MAX_VALUE);
					livingEntity.setHealth(0.0F);
					livingEntity.die(infinityDamageSource);
				}
			}
		}
		return super.onLeftClickEntity(stack, player, entity);
	}
}
