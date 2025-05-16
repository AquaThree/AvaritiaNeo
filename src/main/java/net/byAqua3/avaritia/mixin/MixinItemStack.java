package net.byAqua3.avaritia.mixin;

import java.util.Objects;
import java.util.Optional;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.byAqua3.avaritia.loader.AvaritiaDataComponents;
import net.byAqua3.avaritia.tile.TileInfinityChest;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

@Mixin({ ItemStack.class })
public class MixinItemStack {

	@Shadow
	@Final
	private Item item;

	@Inject(method = { "getMaxStackSize" }, at = { @At("HEAD") }, cancellable = true)
	public void getMaxStackSize(CallbackInfoReturnable<Integer> callbackInfo) {
		if (this.item != null) {
			ItemStack itemStack = (ItemStack) (Object) this;
			if (itemStack.has(AvaritiaDataComponents.MAX_STACK_SIZE.get())) {
				callbackInfo.setReturnValue(itemStack.get(AvaritiaDataComponents.MAX_STACK_SIZE.get()));
				callbackInfo.cancel();
			}
		}
	}

	@Inject(method = { "isSameItemSameComponents" }, at = { @At("HEAD") }, cancellable = true)
	private static void isSameItemSameComponents(ItemStack stack, ItemStack other, CallbackInfoReturnable<Boolean> callbackInfo) {
		if (stack.has(AvaritiaDataComponents.MAX_STACK_SIZE.get()) || other.has(AvaritiaDataComponents.MAX_STACK_SIZE.get())) {
			ItemStack copy1 = stack.copy();
			ItemStack copy2 = other.copy();

			if (copy1.has(AvaritiaDataComponents.MAX_STACK_SIZE.get())) {
				copy1.remove(AvaritiaDataComponents.MAX_STACK_SIZE.get());
			}
			if (copy2.has(AvaritiaDataComponents.MAX_STACK_SIZE.get())) {
				copy2.remove(AvaritiaDataComponents.MAX_STACK_SIZE.get());
			}
			callbackInfo.setReturnValue(Objects.equals(copy1.getComponents(), copy2.getComponents()));
			callbackInfo.cancel();
		}
	}

	@Inject(method = { "parse" }, at = { @At("HEAD") }, cancellable = true)
	private static void parse(HolderLookup.Provider registries, Tag tag, CallbackInfoReturnable<Optional<ItemStack>> callbackInfo) {
		Optional<ItemStack> optional = TileInfinityChest.CODEC.parse(registries.createSerializationContext(NbtOps.INSTANCE), tag).resultOrPartial();
		if (optional != null && optional.isPresent()) {
			ItemStack itemStack = optional.get();

			if (itemStack.has(AvaritiaDataComponents.MAX_STACK_SIZE.get())) {
				callbackInfo.setReturnValue(optional);
				callbackInfo.cancel();
			}
		}
	}

	@Inject(method = { "save(Lnet/minecraft/core/HolderLookup$Provider;)Lnet/minecraft/nbt/Tag;" }, at = { @At("HEAD") }, cancellable = true)
	public void save(HolderLookup.Provider registries, CallbackInfoReturnable<Tag> callbackInfo) {
		ItemStack itemStack = (ItemStack) (Object) this;
		if (itemStack.has(AvaritiaDataComponents.MAX_STACK_SIZE.get())) {
			callbackInfo.setReturnValue(TileInfinityChest.CODEC.encodeStart(registries.createSerializationContext(NbtOps.INSTANCE), itemStack).getOrThrow());
			callbackInfo.cancel();
		}
	}

}
