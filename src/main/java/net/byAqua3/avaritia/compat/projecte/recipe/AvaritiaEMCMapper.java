package net.byAqua3.avaritia.compat.projecte.recipe;

import moze_intel.projecte.api.mapper.EMCMapper;
import moze_intel.projecte.api.mapper.IEMCMapper;
import moze_intel.projecte.api.mapper.collector.IMappingCollector;
import moze_intel.projecte.api.nss.NSSItem;
import moze_intel.projecte.api.nss.NormalizedSimpleStack;
import net.byAqua3.avaritia.loader.AvaritiaItems;

import net.minecraft.core.RegistryAccess;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.server.packs.resources.ResourceManager;

@EMCMapper
public class AvaritiaEMCMapper implements IEMCMapper<NormalizedSimpleStack, Long> {

	@Override
	public String getName() {
		return "Avaritia EMC Mapper";
	}

	@Override
	public String getDescription() {
		return "Avaritia EMC Mapper.";
	}

	@Override
	public String getTranslationKey() {
		return "mapping.mapper.avaritia_emc";
	}

	@Override
	public void addMappings(IMappingCollector<NormalizedSimpleStack, Long> mapper, ReloadableServerResources serverResources, RegistryAccess registryAccess, ResourceManager resourceManager) {
		mapper.setValueBefore(NSSItem.createItem(AvaritiaItems.NEUTRON_PILE.get()), 711100L);
	}}
