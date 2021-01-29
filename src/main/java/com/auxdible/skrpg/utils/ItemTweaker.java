package com.auxdible.skrpg.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Skull;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.Colorable;
import org.bukkit.material.Dye;
import org.bukkit.Color;
import java.awt.*;
import java.lang.reflect.Field;
import java.util.UUID;


public class ItemTweaker {
    public static ItemStack dye(ItemStack itemStack, String hex) {
        int r = java.awt.Color.decode(hex).getRed();
        int g = java.awt.Color.decode(hex).getGreen();
        int b = java.awt.Color.decode(hex).getBlue();
        if (itemStack.getItemMeta() instanceof LeatherArmorMeta) {
            LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) itemStack.getItemMeta();
            leatherArmorMeta.setColor(Color.fromRGB(r, g, b));
            itemStack.setItemMeta(leatherArmorMeta);
        }
        return itemStack;
    }
    public static ItemStack dye(ItemStack itemStack, int r, int g, int b) {
        if (itemStack.getItemMeta() instanceof LeatherArmorMeta) {
            LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) itemStack.getItemMeta();
            leatherArmorMeta.setColor(Color.fromRGB(r, g, b));
            itemStack.setItemMeta(leatherArmorMeta);
        }
        return itemStack;
    }
    public static ItemStack createPlayerHead(String name) {
        ItemStack itemStack = new ItemBuilder(Material.PLAYER_HEAD, 0).asItem();
        SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
        skullMeta.setOwningPlayer(Bukkit.getPlayer(name));
        itemStack.setItemMeta(skullMeta);
        return itemStack;
    }
    public static ItemStack createPlayerHeadFromData(String value, String signature) {
        ItemStack itemStack = new ItemBuilder(Material.PLAYER_HEAD, 0).asItem();
        SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), null);
        gameProfile.getProperties().put("textures", new Property("textures", value, signature));
        Field profileField;
        try {
            profileField = skullMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(skullMeta, gameProfile);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException exception) {
            exception.printStackTrace();
        }
        itemStack.setItemMeta(skullMeta);
        return itemStack;
    }
}
