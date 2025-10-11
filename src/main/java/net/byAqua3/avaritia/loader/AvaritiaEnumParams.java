package net.byAqua3.avaritia.loader;

import net.minecraft.ChatFormatting;
import net.minecraft.world.item.Rarity;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;

public class AvaritiaEnumParams {
	
	public static final EnumProxy<Rarity> COSMIC_RARITY_ENUM_PROXY = new EnumProxy<>(
            Rarity.class, -100, "avaritia:cosmic", ChatFormatting.RED
    );}
