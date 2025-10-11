package net.byAqua3.avaritia.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.byAqua3.avaritia.loader.AvaritiaItemTags;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

@Mixin({ ItemStack.class })
public class MixinItemStack {

	@Shadow
	@Final
	private Item item;

	@Inject(method = { "canBeHurtBy" }, at = { @At("HEAD") }, cancellable = true)
	public void canBeHurtBy(DamageSource damageSource, CallbackInfoReturnable<Boolean> callbackInfo) {
		if (this.item != null) {
			ItemStack itemStack = (ItemStack) (Object) this;
			List<TagKey<Item>> tags = itemStack.getTags().toList();

			if (tags.contains(AvaritiaItemTags.IMMORTAL)) {
				if (damageSource.is(DamageTypeTags.IS_EXPLOSION)) {
					callbackInfo.setReturnValue(false);
					callbackInfo.cancel();
				}
			}
		}
	}
}
