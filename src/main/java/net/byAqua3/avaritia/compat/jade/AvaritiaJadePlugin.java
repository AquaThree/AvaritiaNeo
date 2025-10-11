package net.byAqua3.avaritia.compat.jade;

import net.byAqua3.avaritia.Avaritia;
import net.byAqua3.avaritia.block.BlockNeutronCollector;
import net.byAqua3.avaritia.block.BlockNeutroniumCompressor;
import net.byAqua3.avaritia.compat.jade.component.ComponentProviderCollector;
import net.byAqua3.avaritia.compat.jade.component.ComponentProviderCompressor;
import net.byAqua3.avaritia.tile.TileNeutronCollector;
import net.byAqua3.avaritia.tile.TileNeutroniumCompressor;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class AvaritiaJadePlugin implements IWailaPlugin {
	
	public static final ResourceLocation COMPRESSOR_UID = ResourceLocation.tryBuild(Avaritia.MODID, "compressor");
	public static final ResourceLocation COLLECTOR_UID = ResourceLocation.tryBuild(Avaritia.MODID, "collector");
	
	@Override
	public void register(IWailaCommonRegistration registration) {
		registration.registerBlockDataProvider(new ComponentProviderCompressor(), TileNeutroniumCompressor.class);
		registration.registerBlockDataProvider(new ComponentProviderCollector(), TileNeutronCollector.class);
	}
	
	@Override
    public void registerClient(IWailaClientRegistration registration) {
		registration.registerBlockComponent(new ComponentProviderCompressor(), BlockNeutroniumCompressor.class);
        registration.registerBlockComponent(new ComponentProviderCollector(), BlockNeutronCollector.class);
    }
}
