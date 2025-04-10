package net.byAqua3.avaritia.item;

import java.util.List;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
<<<<<<< HEAD
import net.minecraft.world.item.ToolMaterial;
=======
import net.minecraft.world.item.Tiers;
>>>>>>> d4d1cb6c05ca8fdefe75682952d4a2a39f983c43
import net.minecraft.world.item.TooltipFlag;

public class ItemSkullFireSword extends SwordItem {

	public ItemSkullFireSword(Properties properties) {
<<<<<<< HEAD
		super(ToolMaterial.DIAMOND, 3, -2.4F, properties);
=======
		super(Tiers.DIAMOND, 3, -2.4F, properties);
>>>>>>> d4d1cb6c05ca8fdefe75682952d4a2a39f983c43
	}

	@Override
	public boolean hasCustomEntity(ItemStack stack) {
		return true;
	}

<<<<<<< HEAD
=======
	@Override
	public int getDamage(ItemStack stack) {
		return 0;
	}

>>>>>>> d4d1cb6c05ca8fdefe75682952d4a2a39f983c43
	@Override
	public int getDamage(ItemStack stack) {
		return 0;
	}
<<<<<<< HEAD

	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		tooltip.add(Component.translatable("tooltip.avaritia.skullfire_sword.desc").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.DARK_GRAY));
	}
=======
>>>>>>> d4d1cb6c05ca8fdefe75682952d4a2a39f983c43
}
