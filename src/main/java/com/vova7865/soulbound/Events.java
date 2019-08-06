package com.vova7865.soulbound;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class Events {
	private Map<String, ItemStack[]> itemsToRestore = new HashMap<String, ItemStack[]>();

	@SubscribeEvent
	public void death(LivingDeathEvent event) {
		if (event.entityLiving instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.entityLiving;
			boolean restore = false;
			ItemStack[] main = player.inventory.mainInventory;
			ItemStack[] armor = player.inventory.armorInventory;
			ItemStack[] itemsPerPlayer = new ItemStack[main.length + armor.length];
			for (int mainIndex = 0; mainIndex < main.length; mainIndex++) {
				ItemStack mainItem = main[mainIndex];
				if (EnchantmentHelper.getEnchantmentLevel(Main.soulbound.effectId, mainItem) > 0) {
					itemsPerPlayer[mainIndex + armor.length] = mainItem;
					restore = true;
				}
			}
			for (int armorIndex = 0; armorIndex < armor.length; armorIndex++) {
				ItemStack armorItem = armor[armorIndex];
				if (EnchantmentHelper.getEnchantmentLevel(Main.soulbound.effectId, armorItem) > 0) {
					itemsPerPlayer[armorIndex] = armorItem;
					restore = true;
				}
			}
			if (restore)
				itemsToRestore.put(player.getUniqueID().toString(), itemsPerPlayer);
		}
	}

	@SubscribeEvent
	public void respawn(PlayerEvent.Clone event) {
		EntityPlayer player = event.entityPlayer;
		if (event.wasDeath && itemsToRestore.containsKey(player.getUniqueID().toString())) {
			ItemStack[] itemsPerPlayer = itemsToRestore.get(player.getUniqueID().toString());
			System.arraycopy(itemsPerPlayer, player.inventory.armorInventory.length, player.inventory.mainInventory, 0,
					player.inventory.mainInventory.length);
			System.arraycopy(itemsPerPlayer, 0, player.inventory.armorInventory, 0,
					player.inventory.armorInventory.length);
			itemsToRestore.remove(player.getUniqueID().toString());

		}
	}

	@SubscribeEvent
	public void drop(PlayerDropsEvent event) {
		EntityPlayer player = event.entityPlayer;
		if (itemsToRestore.containsKey(player.getUniqueID().toString())) {
			final List<ItemStack> listPerPlayer = Arrays.asList(itemsToRestore.get(player.getUniqueID().toString()));
			Stream<EntityItem> stream = StreamSupport.stream(event.drops.spliterator(), false);
			Set<EntityItem> itemsToRemove = stream
					.filter(itemToFilter -> listPerPlayer.contains(itemToFilter.getEntityItem()))
					.collect(Collectors.toSet());
			event.drops.removeAll(itemsToRemove);
		}

	}

}
