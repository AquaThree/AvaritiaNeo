package net.byAqua3.avaritia.loader;

import net.byAqua3.avaritia.Avaritia;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class AvaritiaBlockTags {
	
	public static final TagKey<Block> INCORRECT_FOR_INFINITY_TOOL = BlockTags.create(ResourceLocation.tryBuild(Avaritia.MODID, "incorrect_for_infinity_tool"));
	
	public static final TagKey<Block> INFINITY_AXE = BlockTags.create(ResourceLocation.tryBuild(Avaritia.MODID, "mineable/infinity_axe"));
	public static final TagKey<Block> INFINITY_PICKAXE = BlockTags.create(ResourceLocation.tryBuild(Avaritia.MODID, "mineable/infinity_pickaxe"));
	public static final TagKey<Block> INFINITY_SHOVEL = BlockTags.create(ResourceLocation.tryBuild(Avaritia.MODID, "mineable/infinity_shovel"));}
