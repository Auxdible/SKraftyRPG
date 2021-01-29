package com.auxdible.skrpg.utils;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemBuilder {

	private ItemStack itemStack;

	public ItemBuilder(Material mat, int data) {
		itemStack = new ItemStack(mat, 1, (short) data);
		ItemMeta iM = itemStack.getItemMeta();
		iM.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		itemStack.setItemMeta(iM);
	}

	public ItemBuilder(ItemStack item) {
		this.itemStack = item;
		ItemMeta iM = itemStack.getItemMeta();
		iM.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		itemStack.setItemMeta(iM);
	}

	public ItemBuilder setType(Material mat) {
		itemStack.setType(mat);
		return this;
	}

	public ItemBuilder setName(String display) {
		ItemMeta meta = itemStack.getItemMeta();
		meta.setDisplayName(Text.color(display));
		itemStack.setItemMeta(meta);
		return this;
	}

	public ItemBuilder setLore(List<String> lore) {
		List<String> s = new ArrayList<String>();
		lore.forEach(l -> s.add(Text.color(l)));
		ItemMeta meta = itemStack.getItemMeta();
		meta.setLore(s);
		itemStack.setItemMeta(meta);
		return this;
	}


	public ItemStack asItem() {
		return itemStack;
	}

	public ItemBuilder setColor(Color c) {
		LeatherArmorMeta meta = (LeatherArmorMeta) itemStack.getItemMeta();
		meta.setColor(c);
		itemStack.setItemMeta(meta);
		return this;
	}

	public ItemBuilder setAmount(int i) {
		itemStack.setAmount(i);
		return this;
	}
}
