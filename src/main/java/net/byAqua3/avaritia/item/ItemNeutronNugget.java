package net.byAqua3.avaritia.item;

import java.util.List;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

public class ItemNeutronNugget extends ItemHalo {
	
	public ItemNeutronNugget(Properties properties, int type) {
		super(properties, type);
	}
	
	@Override
	public float getAlpha() {
		return 0.4F;
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
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		tooltip.add(Component.translatable("tooltip.avaritia.neutron_nugget.desc").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.DARK_GRAY));
	}

}
