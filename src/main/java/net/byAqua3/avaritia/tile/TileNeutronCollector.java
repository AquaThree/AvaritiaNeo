package net.byAqua3.avaritia.tile;

import net.byAqua3.avaritia.loader.AvaritiaBlocks;
import net.byAqua3.avaritia.loader.AvaritiaItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class TileNeutronCollector extends TileMachine {

	public static final int PRODUCTION_TICKS = 7111;

	public final SimpleContainer result = new SimpleContainer(1) {
		@Override
		public void setChanged() {
			TileNeutronCollector.this.setChanged();
		}

		@Override
		public boolean canPlaceItem(int slot, ItemStack stack) {
			return false;
		}
	};

	private int progress;

	public final ContainerData dataAccess = new ContainerData() {
		@Override
		public int get(int index) {
			switch (index) {
			case 0:
				return TileNeutronCollector.this.progress;
			default:
				return 0;
			}
		}

		@Override
		public void set(int index, int value) {
			switch (index) {
			case 0:
				TileNeutronCollector.this.progress = value;
				break;
			}
		}

		@Override
		public int getCount() {
			return 1;
		}
	};

	public TileNeutronCollector(BlockPos pos, BlockState state) {
		super(AvaritiaBlocks.NEUTRON_COLLECTOR_TILE.get(), pos, state);
	}

	public int getProgress() {
		return this.progress;
	}

	@Override
	protected boolean canWork() {
		ItemStack itemStack = this.result.getItem(0);
		return (itemStack.isEmpty() || (itemStack.is(AvaritiaItems.NEUTRON_PILE.get()) && itemStack.getCount() < itemStack.getMaxStackSize()));
	}

	@Override
	protected void doWork() {
		if (++this.progress >= PRODUCTION_TICKS) {
			ItemStack itemStack = this.result.getItem(0);
			if (itemStack.isEmpty()) {
				this.result.setItem(0, new ItemStack(AvaritiaItems.NEUTRON_PILE.get()));
			} else {
				itemStack.setCount(itemStack.getCount() + 1);
			}
			this.progress = 0;
		}

	}

	@Override
	protected void onWorkStopped() {
		this.progress = 0;
	}

	@Override
	public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
		super.loadAdditional(tag, provider);
		this.progress = tag.getInt("progress");

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
		tag.putInt("progress", this.progress);

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
	}}
