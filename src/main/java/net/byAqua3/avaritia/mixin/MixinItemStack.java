package net.byAqua3.avaritia.mixin;

import java.util.List;
import java.util.Optional;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.byAqua3.avaritia.loader.AvaritiaDataComponents;
import net.byAqua3.avaritia.loader.AvaritiaItemTags;
import net.byAqua3.avaritia.tile.TileInfinityChest;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
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

	@Inject(method = { "getMaxStackSize" }, at = { @At("HEAD") }, cancellable = true)
	public void getMaxStackSize(CallbackInfoReturnable<Integer> callbackInfo) {
		if (this.item != null) {
			ItemStack itemStack = ((ItemStack) (Object) this);
			if (itemStack.has(AvaritiaDataComponents.MAX_STACK_SIZE.get())) {
				callbackInfo.setReturnValue(itemStack.get(AvaritiaDataComponents.MAX_STACK_SIZE.get()));
				callbackInfo.cancel();
			}
		}
	}
	
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

	@WrapOperation(method = { "isSameItemSameComponents" }, at = { @At(value = "INVOKE", target = "Ljava/util/Objects;equals(Ljava/lang/Object;Ljava/lang/Object;)Z") })
	private static boolean isSameItemSameComponentsEquals(Object a, Object b, Operation<Boolean> original) {
		if (a instanceof PatchedDataComponentMap && b instanceof PatchedDataComponentMap) {
			PatchedDataComponentMap components1 = (PatchedDataComponentMap) a;
			PatchedDataComponentMap components2 = (PatchedDataComponentMap) b;
			if (components1.has(AvaritiaDataComponents.MAX_STACK_SIZE.get()) || components2.has(AvaritiaDataComponents.MAX_STACK_SIZE.get())) {
				PatchedDataComponentMap copy1 = components1.copy();
				PatchedDataComponentMap copy2 = components2.copy();
				if (copy1.has(AvaritiaDataComponents.MAX_STACK_SIZE.get())) {
					copy1.remove(AvaritiaDataComponents.MAX_STACK_SIZE.get());
				}
				if (copy2.has(AvaritiaDataComponents.MAX_STACK_SIZE.get())) {
					copy2.remove(AvaritiaDataComponents.MAX_STACK_SIZE.get());
				}
				return original.call(new Object[] { copy1, copy2 });
			}
		}
		return original.call(new Object[] { a, b });
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
