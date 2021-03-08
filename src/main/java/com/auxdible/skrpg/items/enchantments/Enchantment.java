package com.auxdible.skrpg.items.enchantments;

import com.auxdible.skrpg.items.ItemType;
import com.auxdible.skrpg.items.Items;
import com.auxdible.skrpg.utils.Text;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public enum Enchantment {
    SHARPNESS("Sharpness", "SHARPNESS", ItemType.SWORD),
    PROTECTION("Protection", "PROTECTION", ItemType.ARMOR),
    HEALER("Healer", "HEALER", ItemType.ARMOR);
    private String name;
    private String id;
    private ItemType applyType;
    Enchantment(String name, String id, ItemType applyType) {
        this.name = name;
        this.id = id;
        this.applyType = applyType;
    }

    public String getName() { return name; }
    public ItemType getApplyType() { return applyType; }
    public String getId() { return id; }
    public static void applyEnchantment(ItemStack itemStack, Items items, Enchantment enchantment, int level) {
        if (itemStack.getItemMeta().getLore().contains(Text.color("&7NO ENCHANTMENTS"))) {
            itemStack.getItemMeta().getLore().set(itemStack.getItemMeta().getLore().indexOf(Text.color("&7NO ENCHANTMENTS")), Text.color("&7ENCHANTMENTS:"));
        }
        if (itemStack.getItemMeta().getLore().contains(Text.color("&7ENCHANTMENTS:"))) {
            itemStack.getItemMeta().getLore().get(itemStack.getItemMeta().getLore().indexOf(Text.color("&7ENCHANTMENTS:")) + 1)
        }
    }
}
