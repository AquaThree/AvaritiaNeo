package net.byAqua3.avaritia.entity;

import net.byAqua3.avaritia.loader.AvaritiaEntities;
import net.byAqua3.avaritia.loader.AvaritiaItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

public class EntityEndestPearl extends ThrowableItemProjectile {

	public EntityEndestPearl(EntityType<? extends EntityEndestPearl> entityType, Level level) {
		super(entityType, level);
	}

	public EntityEndestPearl(Level level) {
		super(AvaritiaEntities.ENDEST_PEARL.get(), level);
	}

	@Override
	protected Item getDefaultItem() {
		return AvaritiaItems.ENDEST_PEARL.get();
	}

	@Override
	protected void onHitBlock(BlockHitResult result) {
		if (!this.level().isClientSide()) {
			for (int i = 0; i < 100; i++) {
				this.level().addParticle(ParticleTypes.PORTAL, this.getX(), this.getY(), this.getZ(),
						this.random.nextGaussian() * 3.0D, this.random.nextGaussian() * 3.0D,
						this.random.nextGaussian() * 3.0D);
			}

			EntityGapingVoid gapingVoid = new EntityGapingVoid(level());
			BlockPos blockPos = result.getBlockPos();
			if(this.getOwner() instanceof Player) {
			   gapingVoid.setPlayer((Player) this.getOwner());
			}
			gapingVoid.setPos(blockPos.getX(), blockPos.getY(), blockPos.getZ());
			this.level().addFreshEntity(gapingVoid);

			this.remove(RemovalReason.KILLED);
		}
	}
}
