package net.byAqua3.avaritia.compat.rei;

import me.shedaniel.rei.api.common.entry.comparison.ItemComparatorRegistry;
import me.shedaniel.rei.api.common.plugins.REICommonPlugin;
import me.shedaniel.rei.forge.REIPluginCommon;
import net.byAqua3.avaritia.loader.AvaritiaItems;

@REIPluginCommon
public class AvaritiaREIServerPlugin implements REICommonPlugin {
	
	@Override
    public void registerItemComparators(ItemComparatorRegistry registry) {
		registry.registerComponents(AvaritiaItems.JSON_SINGULARITY.get());
	}

}
