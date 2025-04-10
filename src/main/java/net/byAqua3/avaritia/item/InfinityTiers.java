package net.byAqua3.avaritia.item;

import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

public enum InfinityTiers implements Tier {

	INFINITY(9999, 0, 9999.0F, -1.0F, 0);

	private final int level;
	private final int uses;
	private final float speed;
	private final float damage;
	private final int enchantmentValue;

	private InfinityTiers(int level, int uses, float speed, float damage, int enchantmentValue) {
		this.level = level;
		this.uses = uses;
		this.speed = speed;
		this.damage = damage;
		this.enchantmentValue = enchantmentValue;
	}

	@Override
	public int getUses() {
		return this.uses;
	}

	@Override
	public float getSpeed() {
		return this.speed;
	}

	@Override
	public float getAttackDamageBonus() {
		return this.damage;
	}

	@Override
	public int getLevel() {
		return this.level;
	}

	@Override
	public int getEnchantmentValue() {
		return this.enchantmentValue;
	}

	@Override
	public Ingredient getRepairIngredient() {
		return Ingredient.EMPTY;
	}
}
