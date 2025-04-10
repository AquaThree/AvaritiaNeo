package net.byAqua3.avaritia.entity;

import java.util.ArrayList;
import java.util.List;

<<<<<<< HEAD
import net.byAqua3.avaritia.loader.AvaritiaConfigs;
=======
import net.byAqua3.avaritia.block.BlockInfinityChest;
>>>>>>> d4d1cb6c05ca8fdefe75682952d4a2a39f983c43
import net.byAqua3.avaritia.loader.AvaritiaEntities;
import net.byAqua3.avaritia.loader.AvaritiaSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
<<<<<<< HEAD
import net.minecraft.network.syncher.SynchedEntityData.Builder;
=======
>>>>>>> d4d1cb6c05ca8fdefe75682952d4a2a39f983c43
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
<<<<<<< HEAD
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.item.ItemEntity;
=======
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.EnderMan;
>>>>>>> d4d1cb6c05ca8fdefe75682952d4a2a39f983c43
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class EntityGapingVoid extends Entity {

	private static final EntityDataAccessor<Integer> AGE = SynchedEntityData.defineId(EntityGapingVoid.class,
			EntityDataSerializers.INT);

	public static final int MAX_LIFETIME = 186;

	public static final double COLLAPSE = 0.95D;

	private static final int SUCK_RANGE = 20;

	private Player player;

	public EntityGapingVoid(EntityType<?> entityType, Level level) {
		super(entityType, level);
	}

	public EntityGapingVoid(Level level) {
		this(AvaritiaEntities.GAPING_VOID.get(), level);
	}

	public int getAge() {
		return this.entityData.get(AGE).intValue();
	}

	public void setAge(int age) {
		this.entityData.set(AGE, Integer.valueOf(age));
	}

	public Player getPlayer() {
		return this.player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	@Override
	protected void defineSynchedData(Builder builder) {
		builder.define(AGE, Integer.valueOf(0));
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag tag) {
		this.setAge(tag.getInt("age"));
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag tag) {
		tag.putInt("age", this.getAge());
	}

	@Override
	public boolean shouldRenderAtSqrDistance(double distance) {
		double d0 = 1.0D;
		if (Double.isNaN(d0)) {
			d0 = 1.0;
		}

		d0 *= 64.0 * getViewScale();
		return distance < d0 * d0;
	}

	private static double ease(double in) {
		double t = in - 1.0D;
		return Math.sqrt(1.0D - t * t);
	}

	public static double getVoidScale(double age) {
		double curve, life = age / 186.0D;
		if (life < 0.95D) {
			curve = 0.005D + ease(1.0D - (0.95D - life) / 0.95D) * 0.995D;
		} else {
			curve = ease(1.0D - (life - 0.95D) / 0.050000000000000044D);
		}
		return 10.0D * curve;
	}

	private boolean shouldSuck(Entity entity) {
		if (entity instanceof EntityEndestPearl) {
			return false;
		}
		if (entity instanceof EntityGapingVoid) {
			return false;
		}
		if (entity instanceof Player) {
			Player player = (Player) entity;
			return !player.isCreative();
		}
		return true;
	}

	private boolean shouldAttack(Entity entity) {
		if (!(entity instanceof LivingEntity)) {
			return false;
		}
		if (entity instanceof Player) {
			Player player = (Player) entity;
			return !player.isCreative();
		}
		return true;
	}

<<<<<<< HEAD
	@SuppressWarnings("deprecation")
	public void attackRangeEntity(float range, DamageSource damageSource, float amount, boolean falloff) {
		Vec3 pos = this.position();

		AABB aabb = AABB.encapsulatingFullBlocks(
				this.blockPosition().offset(new Vec3i((int) -range, (int) -range, (int) -range)),
				this.blockPosition().offset(new Vec3i((int) range, (int) range, (int) range)));
		List<LivingEntity> entitiesToAttack = this.level().getEntitiesOfClass(LivingEntity.class, aabb,
				this::shouldAttack);
		for (Entity toAttack : entitiesToAttack) {
			if (toAttack != this) {
				double posX = pos.x - toAttack.getX();
				double posY = pos.y - toAttack.getY();
				double posZ = pos.z - toAttack.getZ();
				double len = Math.sqrt(posX * posX + posY * posY + posZ * posZ);
				if (len <= range) {
					float damage = amount;
					if (falloff) {
						damage = (float) (damage - amount * Math.max(len / range, 0.25F));
					}
					toAttack.hurtOrSimulate(damageSource, damage);
				}
			}
		}
	}

	public void breakRangeBlock(float range, boolean sound) {
		Vec3 pos = this.position();
		int blockRange = (int) Math.round(range);

		for (int y = -blockRange; y <= blockRange; y++) {
			for (int z = -blockRange; z <= blockRange; z++) {
				for (int x = -blockRange; x <= blockRange; x++) {
					Vec3 pos1 = new Vec3(x, y, z);
					Vec3 rPos = pos.add(pos1);
					BlockPos blockPos = new BlockPos(Mth.floor(rPos.x), Mth.floor(rPos.y), Mth.floor(rPos.z));
					BlockState blockState = this.level().getBlockState(blockPos);
					Block block = blockState.getBlock();
					double dist = Math.sqrt(pos1.x * pos1.x + pos1.y * pos1.y + pos1.z * pos1.z);
					if (dist <= range && !blockState.isAir()) {
						if (!AvaritiaConfigs.breakAllBlocks.get()
								&& blockState.getDestroySpeed(this.level(), blockPos) <= -1) {
							continue;
						}

						if (sound) {
							if (!(block instanceof BaseFireBlock)) {
								this.level().levelEvent(2001, blockPos, Block.getId(blockState));
							}
						}
						this.level().setBlockAndUpdate(blockPos, Blocks.AIR.defaultBlockState());
						this.level().gameEvent(GameEvent.BLOCK_DESTROY, blockPos,
								GameEvent.Context.of(player, blockState));

						if (!this.level().isClientSide()) {
							List<ItemStack> drops = new ArrayList<>();
							List<ItemStack> blockDrops = Block.getDrops(blockState, (ServerLevel) this.level(),
									blockPos, null);
							if (!blockDrops.isEmpty()) {
								drops.addAll(blockDrops);
							} else {
								ResourceLocation blockKey = BuiltInRegistries.BLOCK.getKey(block);
								Item blockItem = BuiltInRegistries.ITEM.getValue(blockKey);
								drops.add(new ItemStack(blockItem));
							}

							if (!drops.isEmpty()) {
								for (ItemStack itemStack : drops) {
									ItemEntity itemEntity = new ItemEntity(this.level(), blockPos.getX(),
											blockPos.getY(), blockPos.getZ(), itemStack);
									itemEntity.setDefaultPickUpDelay();
									this.level().addFreshEntity(itemEntity);
								}
							}
						}
					}
				}
			}
		}
	}

	@Override
	public boolean hurtServer(ServerLevel level, DamageSource damageSource, float amount) {
		return false;
	}

=======
>>>>>>> d4d1cb6c05ca8fdefe75682952d4a2a39f983c43
	@Override
	public void tick() {
		super.tick();

		int age = this.getAge();

		if (age == 0) {
			this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), AvaritiaSounds.GAPING_VOID.get(),
					SoundSource.HOSTILE, 8.0F, 1.0F, true);
		}

		this.setAge(age + 1);

		Vec3 pos = this.position();
		double voidScale = getVoidScale(age);

		for (int i = 0; i < 50; i++) {
			this.level().addParticle(ParticleTypes.PORTAL, this.getX(), this.getY(), this.getZ(),
					this.random.nextGaussian() * 3.0D, this.random.nextGaussian() * 3.0D,
					this.random.nextGaussian() * 3.0D);
		}
<<<<<<< HEAD

		double radius = voidScale * 0.5D;

		AABB aabb = AABB.encapsulatingFullBlocks(
				this.blockPosition().offset(new Vec3i(-SUCK_RANGE, -SUCK_RANGE, -SUCK_RANGE)),
				this.blockPosition().offset(new Vec3i(SUCK_RANGE, SUCK_RANGE, SUCK_RANGE)));
		List<Entity> entities = this.level().getEntitiesOfClass(Entity.class, aabb, this::shouldSuck);

		for (Entity entity : entities) {
			if (entity != this) {
				double posX = pos.x - entity.getX();
				double posY = pos.y - entity.getY();
				double posZ = pos.z - entity.getZ();
				double len = Math.sqrt(posX * posX + posY * posY + posZ * posZ);
				double lenn = len / SUCK_RANGE;
				if (len <= SUCK_RANGE) {
					double strength = (1 - lenn) * (1 - lenn);
					double power = 0.075 * radius;

					if (entity instanceof WitherBoss) {
						power *= 100;
=======

		double radius = voidScale * 0.5D;

		AABB aabb = AABB.encapsulatingFullBlocks(
				this.blockPosition().offset(new Vec3i(-SUCK_RANGE, -SUCK_RANGE, -SUCK_RANGE)),
				this.blockPosition().offset(new Vec3i(SUCK_RANGE, SUCK_RANGE, SUCK_RANGE)));
		List<Entity> entities = this.level().getEntitiesOfClass(Entity.class, aabb, this::shouldSuck);

		for (Entity entity : entities) {
			if (entity != this) {
				double posX = pos.x - entity.getX();
				double posY = pos.y - entity.getY();
				double posZ = pos.z - entity.getZ();
				double len = Math.sqrt(posX * posX + posY * posY + posZ * posZ);
				double lenn = len / SUCK_RANGE;
				if (len <= SUCK_RANGE) {
					double strength = (1 - lenn) * (1 - lenn);
					double power = 0.075 * radius;

					Vec3 motion = entity.getDeltaMovement();

					double motionX = motion.x() + (posX / len) * strength * power;
					double motionY = motion.y() + (posY / len) * strength * power;
					double motionZ = motion.z() + (posZ / len) * strength * power;

					entity.setDeltaMovement(motionX, motionY, motionZ);
				}
			}
		}

		int attackRange = (int) (radius * 0.95D);

		aabb = AABB.encapsulatingFullBlocks(
				this.blockPosition().offset(new Vec3i(-attackRange, -attackRange, -attackRange)),
				this.blockPosition().offset(new Vec3i(attackRange, attackRange, attackRange)));
		List<LivingEntity> entitiesToAttack = this.level().getEntitiesOfClass(LivingEntity.class, aabb,
				this::shouldAttack);
		for (Entity toAttack : entitiesToAttack) {
			if (toAttack != this) {
				double posX = pos.x - toAttack.getX();
				double posY = pos.y - toAttack.getY();
				double posZ = pos.z - toAttack.getZ();
				double len = Math.sqrt(posX * posX + posY * posY + posZ * posZ);
				if (len <= attackRange) {
					if (toAttack instanceof EnderMan || toAttack instanceof EnderDragon) {
						toAttack.hurt(this.damageSources().playerAttack(this.player), 10.0F);
					} else if (toAttack instanceof WitherBoss) {
						WitherBoss wither = (WitherBoss) toAttack;
						wither.setTarget(null);
						toAttack.hurt(this.damageSources().fellOutOfWorld(), 15.0F);
					} else {
						toAttack.hurt(this.damageSources().fellOutOfWorld(), 10.0F);
>>>>>>> d4d1cb6c05ca8fdefe75682952d4a2a39f983c43
					}

					Vec3 motion = entity.getDeltaMovement();

					double motionX = motion.x() + (posX / len) * strength * power;
					double motionY = motion.y() + (posY / len) * strength * power;
					double motionZ = motion.z() + (posZ / len) * strength * power;

					entity.setDeltaMovement(motionX, motionY, motionZ);
				}
			}
		}
<<<<<<< HEAD

		int attackRange = (int) (radius * 0.95D);

		this.attackRangeEntity(attackRange, this.damageSources().fellOutOfWorld(), 20.0F, false);

		attackRange += 2.0D;

		if (age % 10 == 0) {
			this.breakRangeBlock(attackRange, true);
=======
		attackRange += 2.0D;

		if (age % 10 == 0) {
			Vec3 pos1 = pos;
			int blockRange = (int) Math.round(attackRange);

			for (int y = -blockRange; y <= blockRange; y++) {
				for (int z = -blockRange; z <= blockRange; z++) {
					for (int x = -blockRange; x <= blockRange; x++) {
						Vec3 pos2 = new Vec3(x, y, z);
						Vec3 rPos = pos1.add(pos2);
						BlockPos blockPos = new BlockPos(Mth.floor(rPos.x), Mth.floor(rPos.y), Mth.floor(rPos.z));
						BlockState blockState = this.level().getBlockState(blockPos);
						Block block = blockState.getBlock();
						double dist = Math.sqrt(pos2.x * pos2.x + pos2.y * pos2.y + pos2.z * pos2.z);
						if (dist <= attackRange && !blockState.isAir() && !(block instanceof BlockInfinityChest)) {
							if (!(block instanceof BaseFireBlock)) {
								this.level().levelEvent(2001, blockPos, Block.getId(blockState));
							}
							this.level().setBlockAndUpdate(blockPos, Blocks.AIR.defaultBlockState());
							this.level().gameEvent(GameEvent.BLOCK_DESTROY, blockPos,
									GameEvent.Context.of(player, blockState));

							if (!this.level().isClientSide()) {
								List<ItemStack> drops = new ArrayList<>();
								List<ItemStack> blockDrops = Block.getDrops(blockState, (ServerLevel) this.level(),
										blockPos, null);
								if (!blockDrops.isEmpty()) {
									drops.addAll(blockDrops);
								} else {
									ResourceLocation blockKey = BuiltInRegistries.BLOCK.getKey(block);
									Item blockItem = BuiltInRegistries.ITEM.get(blockKey);
									drops.add(new ItemStack(blockItem));
								}

								if (!drops.isEmpty()) {
									for (ItemStack itemStack : drops) {
										ItemEntity itemEntity = new ItemEntity(this.level(), blockPos.getX(),
												blockPos.getY(), blockPos.getZ(), itemStack);
										itemEntity.setDefaultPickUpDelay();
										this.level().addFreshEntity(itemEntity);
									}
								}
							}

							this.level().destroyBlock(blockPos, true);
						}
					}
				}
			}
>>>>>>> d4d1cb6c05ca8fdefe75682952d4a2a39f983c43
		}

		if (age >= MAX_LIFETIME) {
			this.level().explode(this, this.getX(), this.getY(), this.getZ(), 6.0F, false,
					Level.ExplosionInteraction.BLOCK);
			this.attackRangeEntity(24.0F, this.damageSources().fellOutOfWorld(), 100.0F, true);
			this.breakRangeBlock(12.0F, false);
			this.setAge(0);
			this.remove(RemovalReason.KILLED);
		}
	}
}
