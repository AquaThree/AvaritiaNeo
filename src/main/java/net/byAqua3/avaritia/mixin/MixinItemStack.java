package net.byAqua3.avaritia.mixin;

import java.util.Objects;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.byAqua3.avaritia.loader.AvaritiaDataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

@Mixin({ ItemStack.class })
public class MixinItemStack {

	@Shadow
	private Item item;

	@Inject(method = { "getMaxStackSize" }, at = { @At("HEAD") }, cancellable = true)
	public void getMaxStackSize(CallbackInfoReturnable<Integer> callbackInfo) {
		if (this.item != null) {
			ItemStack itemStack = ((ItemStack) (Object) this);
			if(itemStack.has(AvaritiaDataComponents.MAX_STACK_SIZE.get())) {
				callbackInfo.setReturnValue(itemStack.get(AvaritiaDataComponents.MAX_STACK_SIZE.get()));
				callbackInfo.cancel();
			}
		}
	}
	
	@Inject(method = { "isSameItemSameComponents" }, at = { @At("HEAD") }, cancellable = true)
	private static void isSameItemSameComponents(ItemStack stack, ItemStack other,
			CallbackInfoReturnable<Boolean> callbackInfo) {
		ItemStack copy1 = stack.copy();
		ItemStack copy2 = other.copy();
		if(copy1.has(AvaritiaDataComponents.MAX_STACK_SIZE.get()) || copy2.has(AvaritiaDataComponents.MAX_STACK_SIZE.get())) {
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

}
