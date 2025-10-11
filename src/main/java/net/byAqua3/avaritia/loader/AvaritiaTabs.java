package net.byAqua3.avaritia.loader;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.byAqua3.avaritia.Avaritia;
import net.byAqua3.avaritia.item.ItemInfinitySingularity;
import net.byAqua3.avaritia.item.ItemJsonSingularity;
import net.byAqua3.avaritia.singularity.Singularity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AvaritiaTabs {

	public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(BuiltInRegistries.CREATIVE_MODE_TAB, Avaritia.MODID);

	public static final List<Item> BLACK_ITEMS = new ArrayList<Item>();

	public static final DeferredHolder<CreativeModeTab, CreativeModeTab> AVARITIA_TAB = TABS.register(Avaritia.MODID, () -> CreativeModeTab.builder().title(Component.translatable("itemGroup.avaritia")).icon(() -> new ItemStack(AvaritiaItems.INFINITY_CATALYST.get())).displayItems((generator, output) -> {
		Iterator<DeferredHolder<Item, ? extends Item>> itemIterator = AvaritiaItems.ITEMS.getEntries().iterator();
		while (itemIterator.hasNext()) {
			DeferredHolder<Item, ? extends Item> itemHolder = itemIterator.next();
			Item item = itemHolder.get();
			if (!BLACK_ITEMS.contains(item)) {
				if (!(item instanceof ItemJsonSingularity)) {
					if (item instanceof ItemInfinitySingularity) {
						for (Singularity singularity : AvaritiaSingularities.getInstance().getSingularities()) {
							ItemStack itemStack = new ItemStack(AvaritiaItems.JSON_SINGULARITY.get());
							itemStack.set(AvaritiaDataComponents.SINGULARITY_ID.get(), singularity.getId());
							output.accept(itemStack);
						}
					}
					ItemStack itemStack = new ItemStack(item);
					output.accept(itemStack);
				}
			}
		}
	}).build());

	public static void registerTabs(IEventBus modEventBus) {
		TABS.register(modEventBus);
	}}
