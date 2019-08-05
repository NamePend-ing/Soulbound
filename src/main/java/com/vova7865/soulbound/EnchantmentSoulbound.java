package com.vova7865.soulbound;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;

public class EnchantmentSoulbound extends Enchantment {

	public EnchantmentSoulbound(int id, int weight, EnumEnchantmentType type) {
		super(id, weight, type);
		this.setName("soulbound");
	}
	@Override
	public int getMaxLevel() {
		return 1;
	}
	
}
