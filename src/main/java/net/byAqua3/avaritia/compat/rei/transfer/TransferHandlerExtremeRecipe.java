package net.byAqua3.avaritia.compat.rei.transfer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import it.unimi.dsi.fastutil.ints.IntSet;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.widgets.Slot;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.registry.transfer.simple.SimpleTransferHandler;
import me.shedaniel.rei.api.client.registry.transfer.simple.SimpleTransferHandler.IntRange;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.InputIngredient;
import me.shedaniel.rei.api.common.transfer.info.stack.SlotAccessor;
import net.byAqua3.avaritia.compat.rei.display.DisplayExtremeRecipe;
import net.byAqua3.avaritia.recipe.RecipeExtremeCrafting;
import net.byAqua3.avaritia.recipe.RecipeExtremeShaped;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

public class TransferHandlerExtremeRecipe {

	public static <C extends AbstractContainerMenu, D extends Display> SimpleTransferHandler create(Class<? extends C> containerClass, CategoryIdentifier<D> categoryIdentifier, IntRange inputSlots) {
		return new SimpleTransferHandler() {
			@Override
			public ApplicabilityResult checkApplicable(Context context) {
				if (!containerClass.isInstance(context.getMenu()) || !categoryIdentifier.equals(context.getDisplay().getCategoryIdentifier()) || context.getContainerScreen() == null) {
					return ApplicabilityResult.createNotApplicable();
				} else {
					return ApplicabilityResult.createApplicable();
				}
			}

			@Override
			public Iterable<SlotAccessor> getInputSlots(Context context) {
				if (context.getDisplay() instanceof DisplayExtremeRecipe) {
					DisplayExtremeRecipe display = (DisplayExtremeRecipe) context.getDisplay();
					RecipeExtremeCrafting recipe = display.getRecipe();
					List<EntryIngredient> ingredients = display.getInputEntries();
					List<SlotAccessor> inputSlots = new ArrayList<>();

					if (!display.isShapeless()) {
						RecipeExtremeShaped shapedRecipe = (RecipeExtremeShaped) recipe;
						
						for (int y = 0; y < 9; y++) {
							for (int x = 0; x < 9; x++) {
								int slotIndex = x + y * 9;
								int inputIndex = x + y * shapedRecipe.getWidth();
								if (inputIndex >= ingredients.size() || x >= shapedRecipe.getWidth()) {
									continue;
								}
								inputSlots.add(SlotAccessor.fromSlot(context.getMenu().getSlot(slotIndex + 1)));
							}
						}
						return inputSlots;
					}
				}
				return IntStream.range(inputSlots.min(), inputSlots.maxExclusive()).mapToObj(id -> SlotAccessor.fromSlot(context.getMenu().getSlot(id))).toList();
			}

			@Override
			public Iterable<SlotAccessor> getInventorySlots(Context context) {
				Minecraft mc = context.getMinecraft();
				LocalPlayer player = mc.player;
				Inventory inventory = player.getInventory();

				return IntStream.range(0, inventory.items.size()).mapToObj(index -> SlotAccessor.fromPlayerInventory(player, index)).collect(Collectors.toList());
			}

			@Override
			public void renderMissingInput(Context context, List<InputIngredient<ItemStack>> inputs, List<InputIngredient<ItemStack>> missing, IntSet missingIndices, GuiGraphics graphics, int mouseX, int mouseY, float partialTicks, List<Widget> widgets, Rectangle rectangle) {
				DisplayExtremeRecipe display = (DisplayExtremeRecipe) context.getDisplay();
				RecipeExtremeCrafting recipe = display.getRecipe();
				List<EntryIngredient> ingredients = display.getInputEntries();
				List<Slot> inputSlots = new ArrayList<>();
				for (Widget widget : widgets) {
					if (widget instanceof Slot slot && slot.getNoticeMark() == Slot.INPUT) {
						inputSlots.add(slot);
					}
				}
				if (!display.isShapeless()) {
					RecipeExtremeShaped shapedRecipe = (RecipeExtremeShaped) recipe;
					
					for (int y = 0; y < 9; y++) {
						for (int x = 0; x < 9; x++) {
							int slotIndex = x + y * 9;
							int inputIndex = x + y * shapedRecipe.getWidth();
							if (inputIndex >= ingredients.size() || x >= shapedRecipe.getWidth()) {
								continue;
							}
							
							Slot inputSlot = inputSlots.get(slotIndex);

							if (missingIndices.contains(inputIndex)) {
								graphics.pose().pushPose();
								graphics.pose().translate(0, 0, 50);
								Rectangle innerBounds = inputSlot.getInnerBounds();
								graphics.fill(innerBounds.x, innerBounds.y, innerBounds.getMaxX(), innerBounds.getMaxY(), 0x40ff0000);
								graphics.pose().popPose();
							}
						}
					}
				} else {
					for (int i = 0; i < inputSlots.size(); i++) {
						Slot inputSlot = inputSlots.get(i);

						if (missingIndices.contains(i)) {
							graphics.pose().pushPose();
							graphics.pose().translate(0, 0, 50);
							Rectangle innerBounds = inputSlot.getInnerBounds();
							graphics.fill(innerBounds.x, innerBounds.y, innerBounds.getMaxX(), innerBounds.getMaxY(), 0x40ff0000);
							graphics.pose().popPose();
						}
					}
				}
			}
		};
	}}
