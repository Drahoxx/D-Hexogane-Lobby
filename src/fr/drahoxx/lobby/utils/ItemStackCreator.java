package fr.drahoxx.lobby.utils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public class ItemStackCreator {

	public static ItemStack createItem(Material mat) {
		return new ItemStack(mat);
	}

	public static ItemStack createItem(Material mat, int nombre) {
		return new ItemStack(mat, nombre);
	}

	public static ItemStack createItem(Material mat, int nombre, int data) {
		return new ItemStack(mat, nombre, (short) data);
	}

	public static ItemStack createItem(Material mat, String name) {
		ItemStack item = new ItemStack(mat);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack createItem(Material mat, String name, int durability) {
		ItemStack item = new ItemStack(mat);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		item.setDurability((short) durability);
		item.setItemMeta(meta);
		return item;
	}
	public static ItemStack createItem(Material mat, String name,short data,int nb) {
		ItemStack item = new ItemStack(mat,nb,data);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack createItem(Material mat, Enchantment ench, int niveau, Boolean bool) {
		ItemStack item = new ItemStack(mat);
		ItemMeta meta = item.getItemMeta();
		meta.addEnchant(ench, niveau, bool);
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack createItem(Material mat, Enchantment ench, int niveau, Boolean bool, int durability) {
		ItemStack item = new ItemStack(mat);
		ItemMeta meta = item.getItemMeta();
		meta.addEnchant(ench, niveau, bool);
		item.setDurability((short) durability);
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack createItem(Material mat, Enchantment ench, int niveau, Boolean bool, String name) {
		ItemStack item = new ItemStack(mat);
		ItemMeta meta = item.getItemMeta();
		meta.addEnchant(ench, niveau, bool);
		meta.setDisplayName(name);
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack createItem(Material mat, Integer nb,Enchantment ench, int niveau, Boolean bool, String name, short data) {
		ItemStack item = new ItemStack(mat,nb,data);
		ItemMeta meta = item.getItemMeta();
		meta.addEnchant(ench, niveau, bool);
		meta.setDisplayName(name);
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack createItem(Material mat, Enchantment ench, int niveau, Boolean bool, String name, int durability) {
		ItemStack item = new ItemStack(mat);
		ItemMeta meta = item.getItemMeta();
		item.setDurability((short) durability);
		meta.addEnchant(ench, niveau, bool);
		meta.setDisplayName(name);
		item.setItemMeta(meta);
		return item;
	}

}
