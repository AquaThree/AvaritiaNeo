package net.byAqua3.avaritia.item;

import java.util.List;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

public class ItemRecordFragment extends Item {
	
	public ItemRecordFragment(Properties properties) {
		super(properties);
	}
	
	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		tooltip.add(Component.translatable("tooltip.avaritia.record_fragment.desc").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.DARK_GRAY));
	}}
