package net.byAqua3.avaritia.loader;

import net.byAqua3.avaritia.Avaritia;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;

public class AvaritiaDamageTypes {
	
	public static final ResourceKey<DamageType> INFINTY = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.tryBuild(Avaritia.MODID, "infinity"));}
