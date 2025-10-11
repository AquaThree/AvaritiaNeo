package net.byAqua3.avaritia.tile;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.byAqua3.avaritia.component.ClusterContainerContents;
import net.byAqua3.avaritia.loader.AvaritiaBlocks;
import net.byAqua3.avaritia.loader.AvaritiaDataComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TileExtremeCraftingTable extends BlockEntity {

	public final SimpleContainer matrix = new SimpleContainer(81) {
		@Override
		public void setChanged() {
			TileExtremeCraftingTable.this.setChanged();
		}
	};

	public final SimpleContainer result = new SimpleContainer(1) {
		@Override
		public void setChanged() {
			TileExtremeCraftingTable.this.setChanged();
		}
	};

	public TileExtremeCraftingTable(BlockPos pos, BlockState state) {
		super(AvaritiaBlocks.EXTREME_CRAFTING_TABLE_TILE.get(), pos, state);
	}

	@Override
	public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
		super.loadAdditional(tag, provider);
		if (tag.contains("Matrix", Tag.TAG_LIST)) {
			ListTag tagList = tag.getList("Matrix", Tag.TAG_COMPOUND);

			for (int i = 0; i < tagList.size(); ++i) {
				CompoundTag compoundTag = tagList.getCompound(i);
				int j = compoundTag.getByte("Slot") & 255;
				if (j >= 0 && j < this.matrix.getItems().size()) {
					this.matrix.getItems().set(j, ItemStack.parse(provider, compoundTag).orElse(ItemStack.EMPTY));
				}
			}
		}
		if (tag.contains("Result", Tag.TAG_LIST)) {
			ListTag tagList = tag.getList("Result", Tag.TAG_COMPOUND);

			for (int i = 0; i < tagList.size(); ++i) {
				CompoundTag compoundTag = tagList.getCompound(i);
				int j = compoundTag.getByte("Slot") & 255;
				if (j >= 0 && j < this.result.getItems().size()) {
					this.result.getItems().set(j, ItemStack.parse(provider, compoundTag).orElse(ItemStack.EMPTY));
				}
			}
		}
	}

	@Override
	protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
		super.saveAdditional(tag, provider);

		ListTag tagListMatrix = new ListTag();

		for (int i = 0; i < this.matrix.getItems().size(); ++i) {
			ItemStack itemStack = this.matrix.getItems().get(i);
			if (!itemStack.isEmpty()) {
				CompoundTag compoundTag = new CompoundTag();
				compoundTag.putByte("Slot", (byte) i);
				tagListMatrix.add(itemStack.save(provider, compoundTag));
			}
		}

		if (!tagListMatrix.isEmpty()) {
			tag.put("Matrix", tagListMatrix);
		}

		ListTag tagListResult = new ListTag();

		for (int i = 0; i < this.result.getItems().size(); ++i) {
			ItemStack itemStack = this.result.getItems().get(i);
			if (!itemStack.isEmpty()) {
				CompoundTag compoundTag = new CompoundTag();
				compoundTag.putByte("Slot", (byte) i);
				tagListResult.add(itemStack.save(provider, compoundTag));
			}
		}

		if (!tagListResult.isEmpty()) {
			tag.put("Result", tagListResult);
		}
	}

	@Override
	protected void applyImplicitComponents(BlockEntity.DataComponentInput componentInput) {
		super.applyImplicitComponents(componentInput);

		NonNullList<ItemStack> itemStacks = NonNullList.withSize(82, ItemStack.EMPTY);
		componentInput.getOrDefault(AvaritiaDataComponents.CLUSTER_CONTAINER, ClusterContainerContents.EMPTY).copyInto(itemStacks);

		if (!itemStacks.isEmpty()) {
			for (int i = 0; i < itemStacks.size(); i++) {
				if (i < itemStacks.size() - 1) {
					this.matrix.getItems().set(i, itemStacks.get(i));
				} else {
					this.result.getItems().set(0, itemStacks.get(i));
				}
			}
		}
	}

	@Override
	protected void collectImplicitComponents(DataComponentMap.Builder builder) {
		super.collectImplicitComponents(builder);

		List<ItemStack> itemStacks = Stream.concat(this.matrix.getItems().stream(), this.result.getItems().stream()).collect(Collectors.toList());
		builder.set(AvaritiaDataComponents.CLUSTER_CONTAINER, ClusterContainerContents.fromItems(itemStacks));
	}}
