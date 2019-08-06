package com.vova7865.soulbound;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;

public class EnchantmentSoulbound extends Enchantment {

	public EnchantmentSoulbound(int id, int weight, EnumEnchantmentType type) {
		super(id, weight, type);
		this.setName("soulbound");
	}

	public int getMinEnchantability(int par1) {
		return 0;
	}

	public int getMaxEnchantibility(int par1) {
		return 30;
	}

	@Override
	public int getMaxLevel() {
		return 1;
	}

}
