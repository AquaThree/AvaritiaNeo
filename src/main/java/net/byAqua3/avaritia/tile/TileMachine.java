package net.byAqua3.avaritia.tile;

import net.byAqua3.avaritia.block.BlockMachine;
import net.byAqua3.avaritia.util.TimeTracker;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class TileMachine extends BlockEntity {

	private boolean isActive;

	private boolean wasActive;

	private final TimeTracker offTracker = new TimeTracker();

	private boolean updateClient;

	public TileMachine(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state) {
		super(blockEntityType, pos, state);
	}

	protected abstract boolean canWork();

	protected abstract void doWork();

	protected abstract void onWorkStopped();

	public final void updateServer() {
		if (this.canWork()) {
			if (!this.isActive && !this.wasActive) {
				this.updateClient = true;
			}
			this.isActive = true;
			this.wasActive = false;
			this.doWork();
		} else {
			if (this.isActive) {
				this.onWorkStopped();
				this.wasActive = true;
				this.offTracker.markTime();
			}
			this.isActive = false;
		}
		this.updateCheck();
	}

	private void updateCheck() {
		if (this.wasActive && this.offTracker.hasDelayPassed(100)) {
			this.wasActive = false;
			this.updateClient = true;
		}
		if (this.updateClient) {
			this.updateClient = false;
			BlockState state = this.level.getBlockState(this.getBlockPos());
			this.level.setBlock(this.getBlockPos(), state.setValue(BlockMachine.ACTIVE, Boolean.valueOf(this.isActive)), 3);
		}
	}

	@Override
	public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
		super.loadAdditional(tag, provider);
		this.isActive = tag.getBoolean("active");
	}

	@Override
	protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
		super.saveAdditional(tag, provider);
		tag.putBoolean("active", this.isActive);
	}}
