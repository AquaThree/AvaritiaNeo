package net.byAqua3.avaritia.item;

import net.minecraft.world.item.Item;

public class ItemHalo extends Item {	
	private int type;

	public ItemHalo(Properties properties, int type) {
		super(properties);
		this.type = type;
	}
	
	public int getType() {
		return this.type;
	}
	
	public float getAlpha() {
		return 1.0F;
	}
	
	public boolean getPulse() {
		return false;
	}
}
