package net.byAqua3.avaritia.entity;

import net.byAqua3.avaritia.damage.DamageSourceInfinity;
import net.byAqua3.avaritia.loader.AvaritiaEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class EntityInfinityArrow extends Arrow {

	private int knockback;

	private boolean sub;
	private boolean impacted;

	public EntityInfinityArrow(EntityType<? extends EntityInfinityArrow> entityType, Level level) {
		super(entityType, level);
	}

	public EntityInfinityArrow(Level level, boolean isSub) {
		this(AvaritiaEntities.INFINITY_ARROW.get(), level);
		this.sub = isSub;
	}

	public int getKnockback() {
		return this.knockback;
	}

	public void setKnockback(int knockback) {
		this.knockback = knockback;
	}

	public boolean isSub() {
		return this.sub;
	}

	public void setSub(boolean sub) {
		this.sub = sub;
	}

	public boolean isImpacted() {
		return this.impacted;
	}

	public void setImpacted(boolean impacted) {
		this.impacted = impacted;
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		this.setSub(tag.getBoolean("isSub"));
		this.setImpacted(tag.getBoolean("isImpacted"));
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putBoolean("isSub", this.isSub());
		tag.putBoolean("isImpacted", this.isImpacted());
	}

	@Override
	protected void doKnockback(LivingEntity livingEntity, DamageSource damageSource) {
		if (this.knockback > 0) {
			double d0 = Math.max(0.0D, 1.0D - livingEntity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
			Vec3 vec3 = this.getDeltaMovement().multiply(1.0D, 0.0D, 1.0D).normalize().scale(this.knockback * 0.6D * d0);
			if (vec3.lengthSqr() > 0.0D) {
				livingEntity.push(vec3.x, 0.1D, vec3.z);
			}
		}
	}

	@Override
	protected void onHitEntity(EntityHitResult result) {
		if (this.getOwner() != null && result.getEntity() != this.getOwner()) {
			DamageSourceInfinity damageSource = new DamageSourceInfinity(this.getOwner());

			if (!this.level().isClientSide()) {
				if (result.getEntity() instanceof EnderMan) {
					result.getEntity().hurt(damageSource, (float) this.getBaseDamage());
					this.discard();
				} else if (!result.getEntity().hurt(this.damageSources().arrow(this, this.getOwner()), 0.0F)) {
					if (!(result.getEntity() instanceof Player)) {
						result.getEntity().hurt(damageSource, (float) this.getBaseDamage());
					}
					this.discard();
				}
			}
			super.onHitEntity(result);
		}
	}

	@Override
	public void tick() {
		super.tick();

		if (!this.isSub()) {
			if (this.inGround) {
				if (!this.isImpacted()) {
					for (int i = 0; i < 36; i++) {
						double angle = this.level().getRandom().nextDouble() * 2.0D * Math.PI;
						double dist = this.level().getRandom().nextGaussian() * 0.5D;

						double x = Math.sin(angle) * dist + this.getX();
						double z = Math.cos(angle) * dist + this.getZ();
						double y = this.getY() + 25.0D;

						double dangle = this.level().getRandom().nextDouble() * 2.0D * Math.PI;
						double ddist = this.level().getRandom().nextDouble() * 0.35D;
						double dx = Math.sin(dangle) * ddist;
						double dz = Math.cos(dangle) * ddist;

						EntityInfinityArrow arrow = new EntityInfinityArrow(this.level(), true);
						Vec3 motion = arrow.getDeltaMovement();
						arrow.setPos(x, y, z);
						arrow.setDeltaMovement(motion.x + dx, motion.y - (this.random.nextDouble() * 1.85 + 0.15), motion.z + dz);
						arrow.setBaseDamage(this.getBaseDamage());
						arrow.setOwner(this.getOwner());
						arrow.setCritArrow(true);
						arrow.pickup = Pickup.DISALLOWED;

						if (!this.level().isClientSide()) {
							this.level().addFreshEntity(arrow);
						}
						this.setImpacted(true);
					}
				}
				if (this.inGroundTime >= 100) {
					if (!this.level().isClientSide()) {
						this.discard();
					}
				}
			}

		} else {
			if (this.inGround && this.inGroundTime >= 20) {
				if (!this.level().isClientSide()) {
					this.discard();
				}
			}
		}
	}}
