package net.byAqua3.avaritia.item;

import net.byAqua3.avaritia.loader.AvaritiaTabs;
import net.minecraft.world.item.Item;

public class ItemGapingVoid extends Item {

	public ItemGapingVoid(Properties properties) {
		super(properties.stacksTo(1));
		AvaritiaTabs.BLACK_ITEMS.add(this);
	}
	
}