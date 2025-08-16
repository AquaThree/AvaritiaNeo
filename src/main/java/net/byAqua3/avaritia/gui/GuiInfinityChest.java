package net.byAqua3.avaritia.gui;

import java.text.DecimalFormat;
import java.util.List;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.datafixers.util.Pair;

import net.byAqua3.avaritia.Avaritia;
import net.byAqua3.avaritia.inventory.MenuInfinityChest;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class GuiInfinityChest extends AbstractContainerScreen<MenuInfinityChest> {

	public static final ResourceLocation BACKGROUND_LOCATION = ResourceLocation.tryBuild(Avaritia.MODID, "textures/gui/infinity_chest.png");

	public GuiInfinityChest(MenuInfinityChest menu, Inventory inventory, Component title) {
		super(menu, inventory, title);
		this.imageWidth = 462;
		this.imageHeight = 330;
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(guiGraphics, mouseX, mouseY, partialTicks);
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
		this.renderTooltip(guiGraphics, mouseX, mouseY);
	}

	@Override
	protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		guiGraphics.blit(BACKGROUND_LOCATION, this.getGuiLeft(), this.getGuiTop(), 0.0F, 0.0F, this.imageWidth, this.imageHeight, 512, 512);
	}

	@Override
	protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
		guiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 4210752, false);
		guiGraphics.drawString(this.font, this.playerInventoryTitle, 152, 236, 4210752, false);
	}

	@Override
	protected void renderTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY) {
		if (this.menu.getCarried().isEmpty() && this.hoveredSlot != null && this.hoveredSlot.hasItem()) {
			ItemStack itemStack = this.hoveredSlot.getItem();
			List<Component> components = this.getTooltipFromContainerItem(itemStack);
			components.add(Component.translatable("avaritia:container.infinity_chest.info", itemStack.getCount(), itemStack.getMaxStackSize()));
			guiGraphics.renderTooltip(this.font, components, itemStack.getTooltipImage(), itemStack, mouseX, mouseY);
		}
	}

	private void recalculateQuickCraftRemaining() {
		ItemStack itemstack = this.menu.getCarried();
		if (!itemstack.isEmpty() && this.isQuickCrafting) {
			if (this.quickCraftingType == 2) {
				this.quickCraftingRemainder = Math.max(itemstack.getMaxStackSize(), this.menu.getTile().chest.getMaxStackSize());
			} else {
				this.quickCraftingRemainder = itemstack.getCount();

				for (Slot slot : this.quickCraftSlots) {
					ItemStack itemstack1 = slot.getItem();
					int i = itemstack1.isEmpty() ? 0 : itemstack1.getCount();
					int j = Math.max(itemstack.getMaxStackSize(), slot.getMaxStackSize(itemstack));
					int k = Math.min(AbstractContainerMenu.getQuickCraftPlaceCount(this.quickCraftSlots, this.quickCraftingType, itemstack) + i, j);
					this.quickCraftingRemainder -= k - i;
				}
			}
		}
	}

	@Override
	protected void renderSlot(GuiGraphics guiGraphics, Slot slot) {
		int i = slot.x;
		int j = slot.y;
		ItemStack itemStack = slot.getItem();
		boolean flag = false;
		boolean flag1 = slot == this.clickedSlot && !this.draggingItem.isEmpty() && !this.isSplittingStack;
		ItemStack itemstack1 = this.menu.getCarried();
		if (slot == this.clickedSlot && !this.draggingItem.isEmpty() && this.isSplittingStack && !itemStack.isEmpty()) {
			itemStack = itemStack.copyWithCount(itemStack.getCount() / 2);
		} else if (this.isQuickCrafting && this.quickCraftSlots.contains(slot) && !itemstack1.isEmpty()) {
			if (this.quickCraftSlots.size() == 1) {
				return;
			}

			if (AbstractContainerMenu.canItemQuickReplace(slot, itemstack1, true) && this.menu.canDragTo(slot)) {
				flag = true;
				int k = Math.max(itemstack1.getMaxStackSize(), slot.getMaxStackSize(itemstack1));
				int l = slot.getItem().isEmpty() ? 0 : slot.getItem().getCount();
				int i1 = AbstractContainerMenu.getQuickCraftPlaceCount(this.quickCraftSlots, this.quickCraftingType, itemstack1) + l;
				if (i1 > k) {
					i1 = k;
				}

				itemStack = itemstack1.copyWithCount(i1);
			} else {
				this.quickCraftSlots.remove(slot);
				this.recalculateQuickCraftRemaining();
			}
		}

		guiGraphics.pose().pushPose();
		guiGraphics.pose().translate(0.0F, 0.0F, 100.0F);
		if (itemStack.isEmpty() && slot.isActive()) {
			Pair<ResourceLocation, ResourceLocation> pair = slot.getNoItemIcon();
			if (pair != null) {
				TextureAtlasSprite textureatlassprite = this.minecraft.getTextureAtlas(pair.getFirst()).apply(pair.getSecond());
				guiGraphics.blit(i, j, 0, 16, 16, textureatlassprite);
				flag1 = true;
			}
		}

		if (!flag1) {
			if (flag) {
				guiGraphics.fill(i, j, i + 16, j + 16, -2130706433);
			}

			int j1 = slot.x + slot.y * this.imageWidth;
			if (slot.isFake()) {
				guiGraphics.renderFakeItem(itemStack, i, j, j1);
			} else {
				guiGraphics.renderItem(itemStack, i, j, j1);
			}

			String text = null;
			int count = itemStack.getCount();

			if (count >= 1000) {
				text = new DecimalFormat("#").format(count / 1000) + "K";
			}
			if (count >= 10000) {
				text = new DecimalFormat("#").format(count / 10000) + "W";
			}
			if (count >= 1000000) {
				text = new DecimalFormat("#").format(count / 1000000) + "M";
			}
			if (count >= 1000000000) {
				text = new DecimalFormat("#").format(count / 1000000000) + "G";
			}

			guiGraphics.renderItemDecorations(this.font, itemStack, i, j, text);
		}

		guiGraphics.pose().popPose();
	}
}
