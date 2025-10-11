package net.byAqua3.avaritia.inventory;

import net.byAqua3.avaritia.inventory.slot.SlotFake;
import net.byAqua3.avaritia.item.ItemJsonSingularity;
import net.byAqua3.avaritia.loader.AvaritiaDataComponents;
import net.byAqua3.avaritia.loader.AvaritiaMenus;
import net.byAqua3.avaritia.loader.AvaritiaSingularities;
import net.byAqua3.avaritia.recipe.RecipeCompressor;
import net.byAqua3.avaritia.singularity.Singularity;
import net.byAqua3.avaritia.tile.TileNeutroniumCompressor;
import net.byAqua3.avaritia.util.RecipeUtils;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

public class MenuNeutroniumCompressor extends MenuMachine<TileNeutroniumCompressor> {

	public final ContainerData dataAccess;

	public MenuNeutroniumCompressor(int id, Inventory inventory, FriendlyByteBuf friendlyByteBuf) {
		this(id, inventory, (TileNeutroniumCompressor) inventory.player.level().getBlockEntity(friendlyByteBuf.readBlockPos()), new SimpleContainerData(5));
	}

	public MenuNeutroniumCompressor(int id, Inventory inventory, TileNeutroniumCompressor machineTile, ContainerData dataAccess) {
		super(AvaritiaMenus.COMPRESSOR.get(), id, inventory, machineTile);
		this.addSlot(new Slot(machineTile.matrix, 0, 39, 35));
		this.addSlot(new Slot(machineTile.result, 0, 117, 35) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return false;
			}
		});
		this.addPlayerInventory(8, 84);
		this.dataAccess = dataAccess;
		this.addDataSlots(this.dataAccess);
		this.addSlot(new SlotFake(new SimpleContainer(1), 0, 13, 35) {
			@Override
			public ItemStack getItem() {
				ItemStack stack = MenuNeutroniumCompressor.this.getMatrixItem().copy();
				stack.setCount(1);
				return stack;
			}
		});
		this.addSlot(new SlotFake(new SimpleContainer(1), 0, 147, 35) {
			@Override
			public ItemStack getItem() {
				ItemStack stack = MenuNeutroniumCompressor.this.getResultItem().copy();
				stack.setCount(1);
				return stack;
			}
		});
	}

	protected boolean canRecipe(Level level, ItemStack stack) {
		RecipeCompressor recipe = RecipeUtils.getCompressorRecipe(level, stack);
		if (recipe != null) {
			return recipe.getCost() > 0;
		}
		return false;
	}

	public int getCompressionTarget() {
		return this.dataAccess.get(0);
	}

	public int getConsumptionProgress() {
		return this.dataAccess.get(1);
	}

	public int getCompressionProgress() {
		return this.dataAccess.get(2);
	}

	public ItemStack getMatrixItem() {
		RecipeCompressor recipe = RecipeUtils.getCompressorRecipeFromResult(this.machineTile.getLevel(), this.getResultItem());
		if (!this.getResultItem().isEmpty()) {
			if (recipe != null) {
				NonNullList<Ingredient> ingredients = recipe.getIngredients();
				for (Ingredient ingredient : ingredients) {
					return ingredient.getItems()[0];
				}
			} else if (this.getResultItem().has(AvaritiaDataComponents.SINGULARITY_ID)) {
				recipe = RecipeUtils.getCompressorRecipeFromResult(this.machineTile.getLevel(), this.getResultItem(), AvaritiaDataComponents.SINGULARITY_ID.get());
				if (recipe != null) {
					NonNullList<Ingredient> ingredients = recipe.getIngredients();
					for (Ingredient ingredient : ingredients) {
						return ingredient.getItems()[0];
					}
				}
			}
		}
		return ItemStack.EMPTY;
	}

	public ItemStack getResultItem() {
		Item item = BuiltInRegistries.ITEM.byId(this.dataAccess.get(3));
		int index = this.dataAccess.get(4);
		if (item != null) {
			ItemStack itemStack = new ItemStack(item);
			if (item instanceof ItemJsonSingularity && index != -1) {
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
	public ItemStack quickMoveStack(Player player, int index) {
		ItemStack resultStack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		if (slot != null && slot.hasItem()) {
			ItemStack slotStack = slot.getItem();
			resultStack = slotStack.copy();
			if (index == 1) {
				if (!this.moveItemStackTo(slotStack, 2, 38, true)) {
					return ItemStack.EMPTY;
				}

				slot.onQuickCraft(slotStack, resultStack);
			} else if (index >= 2 && index < 38) {
				if (!this.canRecipe(player.level(), slotStack) || !this.moveItemStackTo(slotStack, 0, 1, false)) {
					if (index < 29) {
						if (!this.moveItemStackTo(slotStack, 29, 38, false)) {
							return ItemStack.EMPTY;
						}
					} else if (!this.moveItemStackTo(slotStack, 2, 29, false)) {
						return ItemStack.EMPTY;
					}
				}
			} else if (!this.moveItemStackTo(slotStack, 2, 38, false)) {
				return ItemStack.EMPTY;
			}
			if (slotStack.isEmpty()) {
				slot.setByPlayer(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}
			if (slotStack.getCount() == resultStack.getCount()) {
				return ItemStack.EMPTY;
			}
			slot.onTake(player, slotStack);
		}

		return resultStack;
	}}
