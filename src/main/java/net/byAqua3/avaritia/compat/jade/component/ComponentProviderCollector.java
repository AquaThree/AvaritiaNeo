package net.byAqua3.avaritia.compat.jade.component;

import net.byAqua3.avaritia.compat.jade.AvaritiaJadePlugin;
import net.byAqua3.avaritia.loader.AvaritiaConfigs;
import net.byAqua3.avaritia.tile.TileNeutronCollector;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IElementHelper;

public class ComponentProviderCollector implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {

	@Override
	public ResourceLocation getUid() {
		return AvaritiaJadePlugin.COLLECTOR_UID;
	}
	
	@Override
	public void appendServerData(CompoundTag tag, BlockAccessor blockAccessor) {
		if (blockAccessor.getBlockEntity() != null && blockAccessor.getBlockEntity() instanceof TileNeutronCollector) {
			TileNeutronCollector tile = (TileNeutronCollector) blockAccessor.getBlockEntity();
			
			tag.putInt("progress", tile.dataAccess.get(0));
		}
	}

	@SuppressWarnings("unused")
	@Override
	public void appendTooltip(ITooltip tooltip, BlockAccessor blockAccessor, IPluginConfig config) {
		if (blockAccessor.getBlockEntity() != null && blockAccessor.getBlockEntity() instanceof TileNeutronCollector) {
			TileNeutronCollector tile = (TileNeutronCollector) blockAccessor.getBlockEntity();
			IElementHelper helper = IElementHelper.get();

			int progress = blockAccessor.getServerData().getInt("progress");
			tooltip.add(helper.text(Component.translatable("avaritia:container.neutron_collector.info2", String.format("%.2f%%", Float.valueOf(100.0F * progress / AvaritiaConfigs.productionTicks.get())))));
		}
	}}
