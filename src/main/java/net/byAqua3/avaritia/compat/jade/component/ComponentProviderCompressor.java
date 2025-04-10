package net.byAqua3.avaritia.compat.jade.component;

import net.byAqua3.avaritia.Avaritia;
import net.byAqua3.avaritia.compat.jade.AvaritiaJadePlugin;
import net.byAqua3.avaritia.compat.jade.element.ElementCompressorProgress;
import net.byAqua3.avaritia.item.ItemJsonSingularity;
import net.byAqua3.avaritia.loader.AvaritiaDataComponents;
import net.byAqua3.avaritia.loader.AvaritiaSingularities;
import net.byAqua3.avaritia.singularity.Singularity;
import net.byAqua3.avaritia.tile.TileNeutroniumCompressor;
import net.byAqua3.avaritia.util.TextComponent;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IElement.Align;
import snownee.jade.api.ui.IElementHelper;

public class ComponentProviderCompressor implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
	
	public static final ResourceLocation PROGRESS = ResourceLocation.tryBuild(Avaritia.MODID, "progress");
	public static final ResourceLocation PROGRESS_BASE = ResourceLocation.tryBuild(Avaritia.MODID, "progress_base");

	@Override
	public ResourceLocation getUid() {
		return AvaritiaJadePlugin.COMPRESSOR_UID;
	}

	public ItemStack getMatrixItem(int itemId) {
		Item item = BuiltInRegistries.ITEM.byId(itemId);
		if(item != null) {
			return new ItemStack(item);
		}
		return ItemStack.EMPTY;
	}
	
	public ItemStack getResultItem(int itemId, int index) {
		Item item = BuiltInRegistries.ITEM.byId(itemId);
		if(item != null) {
			ItemStack itemStack = new ItemStack(item);
			if(item instanceof ItemJsonSingularity && index != -1) {
				Singularity singularity = AvaritiaSingularities.getInstance().getSingularities().get(index);
				if (singularity != null) {
					itemStack.set(AvaritiaDataComponents.SINGULARITY_ID.get(), singularity.getId());
				}
			}
			return itemStack;
		}
		return ItemStack.EMPTY;
	}

	@Override
	public void appendServerData(CompoundTag tag, BlockAccessor blockAccessor) {
		if (blockAccessor.getBlockEntity() != null
				&& blockAccessor.getBlockEntity() instanceof TileNeutroniumCompressor) {
			TileNeutroniumCompressor tile = (TileNeutroniumCompressor) blockAccessor.getBlockEntity();

			tag.putInt("compressionTarget", tile.dataAccess.get(0));
			tag.putInt("consumptionProgress", tile.dataAccess.get(1));
			tag.putInt("compressionProgress", tile.dataAccess.get(2));
			tag.putInt("targetStackId", tile.dataAccess.get(3));
			tag.putInt("targetSingularityId", tile.dataAccess.get(4));
			tag.putInt("matrixStackId", tile.dataAccess.get(5));
		}
	}

	@SuppressWarnings("unused")
	@Override
	public void appendTooltip(ITooltip tooltip, BlockAccessor blockAccessor, IPluginConfig config) {

		if (blockAccessor.getBlockEntity() != null && blockAccessor.getBlockEntity() instanceof TileNeutroniumCompressor) {
			TileNeutroniumCompressor tile = (TileNeutroniumCompressor) blockAccessor.getBlockEntity();
			IElementHelper helper = IElementHelper.get();

			int compressionTarget = blockAccessor.getServerData().getInt("compressionTarget");
			int consumptionProgress = blockAccessor.getServerData().getInt("consumptionProgress");
			int compressionProgress = blockAccessor.getServerData().getInt("compressionProgress");
			int matrixStackId = blockAccessor.getServerData().getInt("matrixStackId");
			int targetStackId = blockAccessor.getServerData().getInt("targetStackId");
			int targetSingularityId = blockAccessor.getServerData().getInt("targetSingularityId");
			
			ItemStack matrixItem = this.getMatrixItem(matrixStackId);
			ItemStack resultItem = this.getResultItem(targetStackId, targetSingularityId);
			
			if(!resultItem.isEmpty()) {
			   tooltip.add(helper.item(matrixItem).align(Align.LEFT));
			   tooltip.append(new ElementCompressorProgress(((float) compressionProgress / compressionTarget)).align(Align.CENTER));
			   tooltip.append(helper.item(resultItem).align(Align.RIGHT));
			   tooltip.add(helper.text(TextComponent.getText(compressionProgress + " / " + compressionTarget)).align(Align.CENTER));
			}
		}
	}

}
