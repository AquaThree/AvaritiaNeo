package net.byAqua3.avaritia.loader;

import net.byAqua3.avaritia.Avaritia;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class AvaritiaItemTags {
	
	public static final TagKey<Item> IMMORTAL = ItemTags.create(ResourceLocation.tryBuild(Avaritia.MODID, "immortal"));
	public static final TagKey<Item> FAST_PICKUP = ItemTags.create(ResourceLocation.tryBuild(Avaritia.MODID, "fast_pickup"));}
