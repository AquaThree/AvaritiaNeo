package net.byAqua3.avaritia.item;

import net.byAqua3.avaritia.entity.EntityInfinityArrow;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

public class ItemInfinityBow extends BowItem {

	public static int DRAW_TIME = 8;

	public ItemInfinityBow(Properties properties) {
		super(properties.stacksTo(1));
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
	public boolean isFoil(ItemStack stack) {
        return false;
    }

	@Override
	public int getUseDuration(ItemStack stack, LivingEntity livingEntity) {
		return 13;
	}

	@Override
	public ItemUseAnimation getUseAnimation(ItemStack stack) {
		return ItemUseAnimation.BOW;
	}

	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		InteractionResult ret = net.neoforged.neoforge.event.EventHooks.onArrowNock(stack, level,
				player, hand, true);
		if (ret != null) {
			return ret;
		}
		player.startUsingItem(hand);
		return InteractionResult.CONSUME;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int time) {	
		if (livingEntity instanceof Player) {
			Player player = (Player) livingEntity;
			
			if (time == 1) {

				int max = getUseDuration(stack, livingEntity);
				float maxf = (float) max;
				int j = max;

				float f = j / maxf;
				f = (f * f + f * 2.0F) / 3.0F;

				if (f < 0.1) {
					return;
				}

				if (f > 1.0) {
					f = 1.0F;
				}

				EntityInfinityArrow arrow = new EntityInfinityArrow(level, false);
				arrow.setPos(player.getX(), player.getEyeY(), player.getZ());
				arrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, f * 3.0F, 1.0F);
				arrow.setBaseDamage(300.0F);
				arrow.setOwner(player);

				if (f == 1.0F) {
					arrow.setCritArrow(true);
				}

				int k = EnchantmentHelper.getItemEnchantmentLevel(level.registryAccess().holderOrThrow(Enchantments.POWER), stack);
				if (k > 0) {
					arrow.setBaseDamage(arrow.getBaseDamage() + (double) k * 0.5 + 0.5);
				}

				int l = EnchantmentHelper.getItemEnchantmentLevel(level.registryAccess().holderOrThrow(Enchantments.PUNCH), stack);
				if (l > 0) {
					arrow.setKnockback(l);
				}

				if (EnchantmentHelper.getItemEnchantmentLevel(level.registryAccess().holderOrThrow(Enchantments.FLAME), stack) > 0) {
					arrow.igniteForSeconds(100);
				}

				arrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;

				if (!level.isClientSide()) {
					level.addFreshEntity(arrow);
				}

				level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT,
						SoundSource.PLAYERS, 1.0F, 1.0F / (level.random.nextFloat() * 0.4F + 1.2F) + 0.5F);

				player.awardStat(Stats.ITEM_USED.get(this));
			}
		}
	}
	
	@Override
    public boolean releaseUsing(ItemStack stack, Level level, LivingEntity livingEntity, int time) {
		return true;
	}

	@Override
<<<<<<< HEAD
	public AbstractArrow customArrow(AbstractArrow arrow, ItemStack projectileStack, ItemStack weaponStack) {
		return super.customArrow(arrow, projectileStack, weaponStack);
		//return new EntityInfinityArrow(arrow.level(), false);
=======
        public void releaseUsing(ItemStack stack, Level level, LivingEntity livingEntity, int time) {
	}

	@Override
	public AbstractArrow customArrow(AbstractArrow arrow, ItemStack stack) {
		return new EntityInfinityArrow(arrow.level(), false);
>>>>>>> d4d1cb6c05ca8fdefe75682952d4a2a39f983c43
	}

}
