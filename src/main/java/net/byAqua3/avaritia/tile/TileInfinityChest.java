package net.byAqua3.avaritia.tile;

import java.util.List;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.byAqua3.avaritia.Avaritia;
import net.byAqua3.avaritia.component.ClusterContainerContents;
import net.byAqua3.avaritia.loader.AvaritiaBlocks;
import net.byAqua3.avaritia.loader.AvaritiaDataComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.util.DataComponentUtil;

public class TileInfinityChest extends BlockEntity {

	public final SimpleContainer chest = new SimpleContainer(300) {
		@Override
		public void setChanged() {
			TileInfinityChest.this.setChanged();
		}

		@Override
		public int getMaxStackSize() {
			return Integer.MAX_VALUE;
		}

		@Override
		public void fillStackedContents(StackedItemContents stackedContents) {
			for (ItemStack itemStack : this.getItems()) {
				stackedContents.accountStack(itemStack, Integer.MAX_VALUE);
			}
		}
	};

	public boolean start;
	public boolean stop;
	public float lidAngle;
	
	public static final Codec<ItemStack> CODEC = Codec.lazyInitialized(
	        () -> RecordCodecBuilder.create(
	        		instance -> instance.group(
	        				Item.CODEC.fieldOf("id").forGetter(ItemStack::getItemHolder),
	                            ExtraCodecs.intRange(1, Integer.MAX_VALUE).fieldOf("count").orElse(1).forGetter(ItemStack::getCount),
	                            DataComponentPatch.CODEC
	                                .optionalFieldOf("components", DataComponentPatch.EMPTY)
	                                .forGetter(itemStack -> itemStack.getComponentsPatch())
	                        )
	                        .apply(instance, ItemStack::new)
	            )
	    );

	public TileInfinityChest(BlockPos pos, BlockState state) {
		super(AvaritiaBlocks.INFINITY_CHEST_TILE.get(), pos, state);
	}

	private void playSound(SoundEvent sound) {
		Level level = this.getLevel();
		BlockPos blockPos = this.getBlockPos();
		level.playLocalSound(blockPos, sound, SoundSource.BLOCKS, 0.5F, level.random.nextFloat() * 0.1F + 0.9F, false);
	}

	public void onStart() {
		if (lidAngle < 1.0F) {
			this.playSound(SoundEvents.CHEST_OPEN);
		}
		this.start = true;
		this.stop = false;
	}

	public void onStop() {
		if (lidAngle > 0.0F) {
			this.playSound(SoundEvents.CHEST_CLOSE);
		}
		this.start = false;
		this.stop = true;
	}

	public void update() {
		if (start) {
			this.lidAngle += 0.1F;

			if (this.lidAngle > 1.0F) {
				this.lidAngle = 1.0F;
				start = false;
			}
		}
		if (stop) {
			this.lidAngle -= 0.1F;

			if (this.lidAngle < 0.0F) {
				this.lidAngle = 0.0F;
				stop = false;
			}
		}
	}

	@Override
	public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
		super.loadAdditional(tag, provider);

		if (tag.contains("Items", Tag.TAG_LIST)) {
			ListTag tagList = tag.getList("Items", Tag.TAG_COMPOUND);

			for (int i = 0; i < tagList.size(); ++i) {
				CompoundTag compoundTag = tagList.getCompound(i);
				int j = compoundTag.getInt("Slot");
				if (j >= 0 && j < this.chest.getItems().size()) {
					ItemStack itemStack = CODEC.parse(provider.createSerializationContext(NbtOps.INSTANCE), compoundTag).resultOrPartial(error -> Avaritia.LOGGER.error("Tried to load invalid item: '{}'", error)).orElse(ItemStack.EMPTY);
					this.chest.getItems().set(j, itemStack);
				}
			}
		}
	}

	@Override
	protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
		super.saveAdditional(tag, provider);

		ListTag tagList = new ListTag();

		for (int i = 0; i < this.chest.getItems().size(); ++i) {
			ItemStack itemStack = this.chest.getItems().get(i);
			if (!itemStack.isEmpty()) {
				CompoundTag compoundTag = new CompoundTag();
				compoundTag.putInt("Slot", i);
				tagList.add(DataComponentUtil.wrapEncodingExceptions(itemStack, CODEC, provider, compoundTag));
			}
		}

		if (!tagList.isEmpty()) {
			tag.put("Items", tagList);
		}
	}
	
	
	@Override
    protected void applyImplicitComponents(BlockEntity.DataComponentInput componentInput) {
        super.applyImplicitComponents(componentInput);
        
        NonNullList<ItemStack> itemStacks = NonNullList.withSize(300, ItemStack.EMPTY);
        componentInput.getOrDefault(AvaritiaDataComponents.CLUSTER_CONTAINER, ClusterContainerContents.EMPTY).copyInto(itemStacks);
        
        if(!itemStacks.isEmpty()) {
        	for(int i = 0; i < itemStacks.size(); i++) {
        		this.chest.getItems().set(i, itemStacks.get(i));
        	}
        }
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder builder) {
        super.collectImplicitComponents(builder);
        
        List<ItemStack> itemStacks = this.chest.getItems();
        builder.set(AvaritiaDataComponents.CLUSTER_CONTAINER, ClusterContainerContents.fromItems(itemStacks));
    }

}
