package net.byAqua3.avaritia.tile;

import net.byAqua3.avaritia.item.ItemJsonSingularity;
import net.byAqua3.avaritia.loader.AvaritiaBlocks;
import net.byAqua3.avaritia.loader.AvaritiaDataComponents;
import net.byAqua3.avaritia.loader.AvaritiaSingularities;
import net.byAqua3.avaritia.recipe.RecipeCompressor;
import net.byAqua3.avaritia.singularity.Singularity;
import net.byAqua3.avaritia.util.RecipeUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class TileNeutroniumCompressor extends TileMachine implements WorldlyContainer {

	public static int CONSUME_TICKS = 1;

	public final SimpleContainer matrix = new SimpleContainer(1) {
		@Override
		public void setChanged() {
			TileNeutroniumCompressor.this.setChanged();
		}
	};

	public final SimpleContainer result = new SimpleContainer(1) {
		@Override
		public void setChanged() {
			TileNeutroniumCompressor.this.setChanged();
		}
	};

	private ItemStack targetStack = ItemStack.EMPTY;

	private int compressionTarget;

	private int consumptionProgress;

	private int compressionProgress;

	public final ContainerData dataAccess = new ContainerData() {
		@Override
		public int get(int index) {
			switch (index) {
			case 0:
				return TileNeutroniumCompressor.this.compressionTarget;
			case 1:
				return TileNeutroniumCompressor.this.consumptionProgress;
			case 2:
				return TileNeutroniumCompressor.this.compressionProgress;
			case 3:
				return BuiltInRegistries.ITEM.getId(TileNeutroniumCompressor.this.targetStack.getItem());
			case 4:
				if(TileNeutroniumCompressor.this.targetStack.getItem() instanceof ItemJsonSingularity) {
				   String singularityId = TileNeutroniumCompressor.this.targetStack.getOrDefault(AvaritiaDataComponents.SINGULARITY_ID, "null");
				   Singularity singularity = AvaritiaSingularities.getInstance().getSingularity(singularityId);
				   if (singularity != null) {
					   return AvaritiaSingularities.getInstance().getSingularities().indexOf(singularity);
				   }
				}
				return -1;
			default:
				return 0;
			}
		}

		@Override
		public void set(int index, int value) {
			switch (index) {
			case 0:
				TileNeutroniumCompressor.this.compressionTarget = value;
				break;
			case 1:
				TileNeutroniumCompressor.this.consumptionProgress = value;
				break;
			case 2:
				TileNeutroniumCompressor.this.compressionProgress = value;
				break;
			}
		}

		@Override
		public int getCount() {
			return 5;
		}
	};

	public TileNeutroniumCompressor(BlockPos pos, BlockState state) {
		super(AvaritiaBlocks.COMPRESSOR_TILE.get(), pos, state);
	}

	public ItemStack getTargetStack() {
		return this.targetStack;
	}

	@Override
	public void onLoad() {
		super.onLoad();

		if (this.compressionTarget == -1) {
			RecipeCompressor recipe = RecipeUtils.getCompressorRecipeFromResult(this.level, this.targetStack);
			if (recipe == null) {
				this.targetStack = ItemStack.EMPTY;
				this.compressionTarget = 0;
				this.consumptionProgress = 0;
				this.compressionProgress = 0;
			} else {
				this.compressionTarget = recipe.getCost();
			}
		}
	}

	@Override
	protected boolean canWork() {
		ItemStack matrixItem = this.matrix.getItem(0);
		if (matrixItem.isEmpty()) {
			return false;
		}
		RecipeCompressor recipe = RecipeUtils.getCompressorRecipe(this.level, matrixItem);

		if (recipe == null) {
			return false;
		}

		if (!this.targetStack.isEmpty()) {
			return ItemStack.isSameItemSameComponents(recipe.getResultItem(this.level.registryAccess()), this.targetStack);
		}

		ItemStack resultItem = this.result.getItem(0);
		if (resultItem.isEmpty()) {
			return true;
		}
		return ItemStack.isSameItemSameComponents(recipe.getResultItem(this.level.registryAccess()), resultItem)
				&& resultItem.getCount() < resultItem.getMaxStackSize();
	}

	@Override
	protected void doWork() {
		if (this.targetStack.isEmpty()) {
			ItemStack matrixItem = this.matrix.getItem(0);
			RecipeCompressor recipe = RecipeUtils.getCompressorRecipe(this.level, matrixItem);
			
			if (recipe != null) {
				this.targetStack = recipe.getResultItem(this.level.registryAccess());
				this.compressionTarget = recipe.getCost();
			}
		}

		this.consumptionProgress++;
		if (this.consumptionProgress >= CONSUME_TICKS) {
			this.consumptionProgress = 0;
			ItemStack matrixItem = this.matrix.getItem(0);
			matrixItem.setCount(matrixItem.getCount() - 1);
			this.compressionProgress++;
		}
		if (this.compressionProgress >= this.compressionTarget) {
			this.compressionProgress = 0;
			ItemStack resultItem = this.result.getItem(0);
			if (resultItem.isEmpty()) {
				this.result.setItem(0, this.targetStack.copy());
			} else {
				resultItem.setCount(resultItem.getCount() + 1);
			}
			this.targetStack = ItemStack.EMPTY;
		}
	}

	@Override
	protected void onWorkStopped() {
		this.consumptionProgress = 0;
	}

	@Override
	public int getContainerSize() {
		return 2;
	}

	@Override
	public boolean isEmpty() {
		return this.matrix.isEmpty() && this.result.isEmpty();
	}

	@Override
	public ItemStack getItem(int slot) {
		if (slot == 0) {
			return this.matrix.getItem(0);
		} else {
			return this.result.getItem(0);
		}
	}

	@Override
	public ItemStack removeItem(int slot, int amount) {
		if (slot == 0) {
			return this.matrix.removeItem(0, amount);
		} else if (slot == 1) {
			return this.result.removeItem(0, amount);
		}
		return ItemStack.EMPTY;
	}

	@Override
	public ItemStack removeItemNoUpdate(int slot) {
		if (slot == 0) {
			return this.matrix.removeItemNoUpdate(0);
		} else if (slot == 1) {
			return this.result.removeItemNoUpdate(0);
		}
		return ItemStack.EMPTY;
	}

	@Override
	public void setItem(int slot, ItemStack stack) {
		if (slot == 0) {
			this.matrix.setItem(0, stack);
		} else if (slot == 1) {
			this.result.setItem(0, stack);
		}
	}

	@Override
	public boolean stillValid(Player player) {
		BlockPos blockPos = this.getBlockPos();
		return this.getLevel().getBlockEntity(blockPos) == this && player.distanceToSqr(blockPos.getX() + 0.5D,
				blockPos.getY() + 0.5D, blockPos.getZ() + 0.5D) <= 64.0D;
	}

	@Override
	public void clearContent() {

	}

	@Override
	public int[] getSlotsForFace(Direction direction) {
		if (direction == Direction.UP) {
			return new int[] { 0 };
		} else {
			return new int[] { 1 };
		}
	}

	@Override
	public boolean canPlaceItemThroughFace(int index, ItemStack stack, Direction direction) {
		if (stack.isEmpty()) {
			return false;
		}
		if (index == 0) {
			if (targetStack.isEmpty()) {
				return true;
			}

			RecipeCompressor recipe = RecipeUtils.getCompressorRecipe(this.level, stack);

			if (recipe == null) {
				return false;
			}

			if (!this.targetStack.isEmpty()) {
				return ItemStack.isSameItemSameComponents(recipe.getResultItem(this.level.registryAccess()), this.targetStack);
			}
		}
		return false;
	}

	@Override
	public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
		if (index == 1 && direction != Direction.UP) {
			return true;
		}
		return false;
	}

	@Override
	public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
		super.loadAdditional(tag, provider);
		this.compressionTarget = -1;
		this.consumptionProgress = tag.getInt("consumptionProgress");
		this.compressionProgress = tag.getInt("compressionProgress");
		this.targetStack = ItemStack.parse(provider, tag.getCompound("target")).orElse(ItemStack.EMPTY);

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
		tag.putInt("consumptionProgress", this.consumptionProgress);
		tag.putInt("compressionProgress", this.compressionProgress);
		if(!this.targetStack.isEmpty()) {
		   tag.put("target", this.targetStack.save(provider, new CompoundTag()));
		}

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
	}}
