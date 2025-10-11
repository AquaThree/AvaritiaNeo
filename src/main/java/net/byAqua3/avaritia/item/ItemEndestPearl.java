package net.byAqua3.avaritia.item;

import net.byAqua3.avaritia.entity.EntityEndestPearl;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ItemEndestPearl extends Item {

	public ItemEndestPearl(Properties properties) {
		super(properties);
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		
		level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENDER_PEARL_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (level.random.nextFloat() * 0.4F + 0.8F));
		player.getCooldowns().addCooldown(stack.getItem(), 20);
		
		if(!level.isClientSide()){
			EntityEndestPearl endestPearl = new EntityEndestPearl(level);
			endestPearl.setItem(stack);
			endestPearl.setOwner(player);
			endestPearl.setPos(player.getX(), player.getEyeY(), player.getZ());
			endestPearl.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
            level.addFreshEntity(endestPearl);
            
            CriteriaTriggers.SUMMONED_ENTITY.trigger((ServerPlayer) player, endestPearl);
		}
		
		if (!player.isCreative()) {
            stack.shrink(1);
        }
		return InteractionResultHolder.success(stack);
	}}
