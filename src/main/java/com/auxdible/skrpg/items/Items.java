package com.auxdible.skrpg.items;

import com.auxdible.skrpg.utils.ItemBuilder;
import com.auxdible.skrpg.utils.ItemTweaker;
import com.auxdible.skrpg.utils.Text;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum Items {
    NONE(Rarity.COMMON, "None", null, 0, 0, 0, 0, 0, 0, "NONE", ItemType.ITEM, null, 0),
    /* ------ NATURAL RECOURSES ------ */
    WOOD(Rarity.COMMON, "Wood", new ItemBuilder(Material.STRIPPED_OAK_LOG, 0).asItem(), 0, 0,
            0, 0, 0, 0, "WOOD", ItemType.MATERIAL, null, 2),
    STONE(Rarity.UNCOMMON, "Stone", new ItemBuilder(Material.COBBLESTONE, 0).asItem(), 0, 0, 0, 0, 0, 0,
            "STONE", ItemType.MATERIAL, null, 1),
    IRON_INGOT(Rarity.COMMON, "Iron Ingot", new ItemBuilder(Material.IRON_INGOT, 0).asItem(), 0, 0,
            0, 0, 0, 0, "IRON_INGOT", ItemType.MATERIAL, null, 10),
    COAL(Rarity.COMMON, "Coal", new ItemBuilder(Material.COAL, 0).asItem(), 0, 0, 0, 0, 0, 0,
            "COAL", ItemType.MATERIAL, null, 5),
    DIAMOND(Rarity.COMMON, "Diamond", new ItemBuilder(Material.DIAMOND, 0).asItem(), 0, 0, 0, 0, 0,
            0, "DIAMOND", ItemType.MATERIAL, null, 15),
    GOLD_INGOT(Rarity.COMMON, "Gold Ingot", new ItemBuilder(Material.GOLD_INGOT, 0).asItem(), 0, 0, 0, 0, 0, 0,
            "GOLD_INGOT", ItemType.MATERIAL, null, 15),
    REDSTONE(Rarity.COMMON, "Redstone", new ItemBuilder(Material.REDSTONE, 0).asItem(), 0, 0, 0, 0, 0, 0,
            "REDSTONE", ItemType.MATERIAL, null, 2),
    LAPIS_LAZULI(Rarity.COMMON, "Lapis Lazuli", new ItemBuilder(Material.LAPIS_LAZULI, 0).asItem(), 0, 0, 0, 0,
            0, 0, "LAPIS_LAZULI", ItemType.MATERIAL, null, 3),
    EMERALD(Rarity.COMMON, "Emerald", new ItemBuilder(Material.EMERALD, 0).asItem(), 0, 0, 0,
            0, 0, 0, "EMERALD", ItemType.MATERIAL, null, 15),
    OBSIDIAN(Rarity.UNCOMMON, "Obsidian", new ItemBuilder(Material.OBSIDIAN, 0).asItem(), 0, 0, 0, 0, 0, 0,
            "OBSIDIAN", ItemType.MATERIAL, null, 20),
    WHEAT(Rarity.COMMON, "Wheat", new ItemBuilder(Material.WHEAT, 0).asItem(), 0, 0, 0, 0, 0, 0,
            "WHEAT", ItemType.MATERIAL, null, 1),
    CARROT(Rarity.COMMON, "Carrot", new ItemBuilder(Material.CARROT, 0).asItem(), 0, 0, 0, 0, 0, 0,
            "CARROT", ItemType.MATERIAL, null, 1),
    CANE(Rarity.COMMON, "Sugar Cane", new ItemBuilder(Material.SUGAR_CANE, 0).asItem(), 0, 0, 0, 0, 0, 0,
            "CANE", ItemType.MATERIAL, null, 1),
    POTATO(Rarity.COMMON, "Potato", new ItemBuilder(Material.POTATO, 0).asItem(), 0, 0, 0, 0, 0, 0,
            "POTATO", ItemType.MATERIAL, null, 1),
    SWEET_BERRIES(Rarity.COMMON, "Sweet Berries", new ItemBuilder(Material.SWEET_BERRIES, 0).asItem(), 0, 0,
            0, 0, 0, 0,
            "POTATO", ItemType.MATERIAL, null, 5),
    BEETROOT(Rarity.COMMON, "Beetroot", new ItemBuilder(Material.BEETROOT, 0).asItem(), 0, 0,
            0, 0, 0, 0,
            "BEETROOT", ItemType.MATERIAL, null, 1),
    PUMPKIN(Rarity.COMMON, "Pumpkin", new ItemBuilder(Material.PUMPKIN, 0).asItem(), 0, 0, 0, 0, 0, 0, "PUMPKIN",
            ItemType.MATERIAL, null, 5),
    MELON(Rarity.COMMON, "Melon", new ItemBuilder(Material.MELON, 0).asItem(), 0, 0, 0, 0, 0, 0, "MELON",
            ItemType.MATERIAL, null, 5),
    ROTTEN_FLESH(Rarity.COMMON, "Rotten Flesh", new ItemBuilder(Material.ROTTEN_FLESH, 0).asItem(), 0, 0, 0, 0, 0, 0, "ROTTEN_FLESH", ItemType.MATERIAL, null,  5),
    ZOMBIE_FRAGMENT(Rarity.RARE, "Zombie Fragment", new ItemBuilder(Material.ZOMBIE_HEAD, 0).asItem(), 0,  0,0, 0, 0, 0, "ZOMBIE_FRAGMENT", ItemType.MATERIAL, null, 10000),
    /* ------ MERCHANT ITEMS ------*/
    STARTER_SWORD(Rarity.COMMON, "Starter Sword", new ItemBuilder(Material.GOLDEN_SWORD, 0).asItem(), 35, 5, 10, 0, 0, 0, "STARTER_SWORD", ItemType.SWORD,
            Arrays.asList(Text.color("&8&l[&e&lABILITY&8&l] &r&e10 Energy"), Text.color("&7Gain a temporary &f+25 Speed")), 10),
    ZOMBIE_SWORD(Rarity.UNCOMMON, "Zombie Sword", new ItemBuilder(Material.IRON_SWORD, 0).asItem(), 40, 10, 0, 0, 0, 0, "ZOMBIE_SWORD", ItemType.SWORD,
            Arrays.asList(Text.color("&7Do &c+50% damage &7to all &cZombies.")), 10),
    BOW(Rarity.UNCOMMON, "Test Bow", new ItemBuilder(Material.BOW, 0).asItem(), 50, 50, 0, 0, 0, 50, "BOW", ItemType.BOW, null, 100),
    /* ------ CRAFTABLE ITEMS ------ */
    PLANK(Rarity.COMMON, "Plank", new ItemBuilder(Material.OAK_PLANKS, 0).asItem(), 0, 0, 0, 0, 0, 0, "PLANK",
            ItemType.MATERIAL, null, Arrays.asList(
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.WOOD, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.NONE, 0)),4, 2, 3),
    STICK(Rarity.COMMON, "Stick", new ItemBuilder(Material.STICK, 0).asItem(), 0, 0, 0, 0, 0, 0, "STICK", ItemType.MATERIAL,
            null, Arrays.asList(
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.PLANK, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.PLANK, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.NONE, 0)), 4, 3, 5),
    BASIC_HILT(Rarity.COMMON, "Basic Hilt", new ItemBuilder(Material.STICK, 0).asItem(), 0, 0, 0, 0, 0, 0, "BASIC_HILT", ItemType.ITEM,
            Arrays.asList(Text.color("&7Used to craft a &fCommon &7tool.")), Arrays.asList(new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.PLANK, 3), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.STICK, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.PLANK, 1), new CraftingIngrediant(Items.NONE, 0)), 1, 20, 10),
    ADVANCED_HILT(Rarity.UNCOMMON, "Advanced Hilt", new ItemBuilder(Material.STICK, 0).asItem(), 0, 0, 0, 0, 0, 0, "ADVANCED_HILT", ItemType.ITEM,
            Arrays.asList(Text.color("&7Used to craft a &aUncommon &7tool.")), Arrays.asList(new CraftingIngrediant(Items.IRON_INGOT, 4), new CraftingIngrediant(Items.GOLD_INGOT, 1), new CraftingIngrediant(Items.IRON_INGOT, 4),
            new CraftingIngrediant(Items.IRON_INGOT, 4), new CraftingIngrediant(Items.BASIC_HILT, 1), new CraftingIngrediant(Items.IRON_INGOT, 4),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.PLANK, 4), new CraftingIngrediant(Items.NONE, 0)), 1, 40, 20),
    /* ------ WOOD TOOLS ------ */
    WOODEN_PICKAXE(Rarity.COMMON, "Wooden Pickaxe", new ItemBuilder(Material.WOODEN_PICKAXE, 0).asItem(), 2, 0, 0, 0, 0, 0, "WOODEN_PICKAXE", ItemType.PICKAXE,
            null, Arrays.asList(new CraftingIngrediant(Items.PLANK, 1), new CraftingIngrediant(Items.PLANK, 1), new CraftingIngrediant(Items.PLANK, 1),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.STICK, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.BASIC_HILT, 1), new CraftingIngrediant(Items.NONE, 0)), 1, 22, 15),
    WOODEN_SWORD(Rarity.COMMON, "Wooden Sword", new ItemBuilder(Material.WOODEN_SWORD, 0).asItem(), 20, 0, 0, 0, 0, 0, "WOODEN_SWORD", ItemType.SWORD,
            null, Arrays.asList(new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.PLANK, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.PLANK, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.BASIC_HILT, 1), new CraftingIngrediant(Items.NONE, 0)), 1, 22, 15),
    WOODEN_HOE(Rarity.COMMON, "Wooden Hoe", new ItemBuilder(Material.WOODEN_HOE, 0).asItem(), 2, 0, 0, 0, 0, 0, "WOODEN_HOE", ItemType.HOE,
            null, Arrays.asList(new CraftingIngrediant(Items.PLANK, 1), new CraftingIngrediant(Items.PLANK, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.STICK, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.BASIC_HILT, 1), new CraftingIngrediant(Items.NONE, 0)), 1, 22, 15),
    /* ----- STONE TOOLS ----- */
    STONE_PICKAXE(Rarity.COMMON, "Stone Pickaxe", new ItemBuilder(Material.STONE_PICKAXE, 0).asItem(), 4, 0, 0, 0, 0, 0, "STONE_PICKAXE", ItemType.PICKAXE,
            null, Arrays.asList(new CraftingIngrediant(Items.STONE, 1), new CraftingIngrediant(Items.STONE, 1), new CraftingIngrediant(Items.STONE, 1),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.STICK, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.BASIC_HILT, 1), new CraftingIngrediant(Items.NONE, 0)), 1, 24, 15),
    STONE_SWORD(Rarity.COMMON, "Stone Sword", new ItemBuilder(Material.STONE_SWORD, 0).asItem(), 25, 0, 0, 0, 0, 0, "STONE_SWORD", ItemType.SWORD,
            null, Arrays.asList(new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.STONE, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.STONE, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.BASIC_HILT, 1), new CraftingIngrediant(Items.NONE, 0)), 1, 24, 15),
    STONE_HOE(Rarity.COMMON, "Stone Hoe", new ItemBuilder(Material.STONE_HOE, 0).asItem(), 4, 0, 0, 0, 0, 0, "STONE_HOE", ItemType.HOE,
            null, Arrays.asList(new CraftingIngrediant(Items.STONE, 1), new CraftingIngrediant(Items.STONE, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.STICK, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.BASIC_HILT, 1), new CraftingIngrediant(Items.NONE, 0)), 1, 24, 15),
    /* ----- IRON TOOLS ----- */
    IRON_PICKAXE(Rarity.COMMON, "Iron Pickaxe", new ItemBuilder(Material.IRON_PICKAXE, 0).asItem(), 6, 0, 0, 0, 0, 0, "IRON_PICKAXE", ItemType.PICKAXE,
            null, Arrays.asList(new CraftingIngrediant(Items.IRON_INGOT, 1), new CraftingIngrediant(Items.IRON_INGOT, 1), new CraftingIngrediant(Items.IRON_INGOT, 1),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.STICK, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.BASIC_HILT, 1), new CraftingIngrediant(Items.NONE, 0)), 1, 24, 20),
    IRON_SWORD(Rarity.COMMON, "Iron Sword", new ItemBuilder(Material.IRON_SWORD, 0).asItem(), 35, 0, 0, 0, 0, 0, "IRON_SWORD", ItemType.SWORD,
            null, Arrays.asList(new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.IRON_INGOT, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.IRON_INGOT, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.BASIC_HILT, 1), new CraftingIngrediant(Items.NONE, 0)), 1, 24, 20),
    IRON_HOE(Rarity.COMMON, "Iron Hoe", new ItemBuilder(Material.IRON_HOE, 0).asItem(), 6, 0, 0, 0, 0, 0, "IRON_HOE", ItemType.HOE,
            null, Arrays.asList(new CraftingIngrediant(Items.IRON_INGOT, 1), new CraftingIngrediant(Items.IRON_INGOT, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.STICK, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.BASIC_HILT, 1), new CraftingIngrediant(Items.NONE, 0)), 1, 24, 20),
    /* ----- GOLDEN TOOLS ----- */
    GOLDEN_PICKAXE(Rarity.UNCOMMON, "Golden Pickaxe", new ItemBuilder(Material.GOLDEN_PICKAXE, 0).asItem(), 1, 0, 0, 0, 0, 0, "GOLDEN_PICKAXE", ItemType.PICKAXE,
            null, Arrays.asList(new CraftingIngrediant(Items.GOLD_INGOT, 1), new CraftingIngrediant(Items.GOLD_INGOT, 1), new CraftingIngrediant(Items.GOLD_INGOT, 1),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.STICK, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.ADVANCED_HILT, 1), new CraftingIngrediant(Items.NONE, 0)), 1, 26, 25),
    GOLDEN_SWORD(Rarity.COMMON, "Golden Sword", new ItemBuilder(Material.GOLDEN_SWORD, 0).asItem(), 20, 20, 0, 0, 0, 0, "GOLDEN_SWORD", ItemType.SWORD,
            null, Arrays.asList(new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.GOLD_INGOT, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.GOLD_INGOT, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.ADVANCED_HILT, 1), new CraftingIngrediant(Items.NONE, 0)), 1, 26, 25),
    GOLDEN_HOE(Rarity.UNCOMMON, "Golden Hoe", new ItemBuilder(Material.GOLDEN_HOE, 0).asItem(), 4, 0, 0, 0, 0, 0, "GOLDEN_HOE", ItemType.HOE,
            null, Arrays.asList(new CraftingIngrediant(Items.GOLD_INGOT, 1), new CraftingIngrediant(Items.GOLD_INGOT, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.STICK, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.ADVANCED_HILT, 1), new CraftingIngrediant(Items.NONE, 0)), 1, 24, 25),
    /* ----- DIAMOND TOOLS  -----*/
    DIAMOND_PICKAXE(Rarity.UNCOMMON, "Diamond Pickaxe", new ItemBuilder(Material.DIAMOND_PICKAXE, 0).asItem(), 10, 0, 0, 0, 0, 0, "DIAMOND_PICKAXE", ItemType.PICKAXE,
            null, Arrays.asList(new CraftingIngrediant(Items.DIAMOND, 1), new CraftingIngrediant(Items.DIAMOND, 1), new CraftingIngrediant(Items.DIAMOND, 1),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.STICK, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.ADVANCED_HILT, 1), new CraftingIngrediant(Items.NONE, 0)), 1, 30, 30),
    DIAMOND_SWORD(Rarity.UNCOMMON, "Diamond Sword", new ItemBuilder(Material.DIAMOND_SWORD, 0).asItem(), 50, 0, 0, 0, 0, 0, "DIAMOND_SWORD", ItemType.SWORD,
            null, Arrays.asList(new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.DIAMOND, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.DIAMOND, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.ADVANCED_HILT, 1), new CraftingIngrediant(Items.NONE, 0)), 1, 30, 30),
    DIAMOND_HOE(Rarity.UNCOMMON, "Diamond Hoe", new ItemBuilder(Material.DIAMOND_HOE, 0).asItem(), 10, 0, 0, 0,  0,0, "DIAMOND_HOE", ItemType.HOE,
            null, Arrays.asList(new CraftingIngrediant(Items.DIAMOND, 1), new CraftingIngrediant(Items.DIAMOND, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.STICK, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.ADVANCED_HILT, 1), new CraftingIngrediant(Items.NONE, 0)), 1, 30, 30),
    /* ----- IRON ARMOR ------ */
    IRON_HELMET(Rarity.COMMON, "Iron Helmet", new ItemBuilder(Material.IRON_HELMET, 0).asItem(), 0, 0, 20, 0, 0, 0, "IRON_HELMET", ItemType.ARMOR, null,
            Arrays.asList(new CraftingIngrediant(Items.IRON_INGOT, 1), new CraftingIngrediant(Items.IRON_INGOT, 1), new CraftingIngrediant(Items.IRON_INGOT, 1),
                    new CraftingIngrediant(Items.IRON_INGOT, 1), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.IRON_INGOT, 1),
                    new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.NONE, 0)), 1, 24, 20),
    IRON_CHESTPLATE(Rarity.COMMON, "Iron Chestplate", new ItemBuilder(Material.IRON_CHESTPLATE, 0).asItem(), 0, 0, 20, 0, 0, 0, "IRON_CHESTPLATE", ItemType.ARMOR, null,
            Arrays.asList(new CraftingIngrediant(Items.IRON_INGOT, 1), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.IRON_INGOT, 1),
                    new CraftingIngrediant(Items.IRON_INGOT, 1), new CraftingIngrediant(Items.IRON_INGOT, 1), new CraftingIngrediant(Items.IRON_INGOT, 1),
                    new CraftingIngrediant(Items.IRON_INGOT, 1), new CraftingIngrediant(Items.IRON_INGOT, 1), new CraftingIngrediant(Items.IRON_INGOT, 1)), 1, 24, 20),
    IRON_LEGGINGS(Rarity.COMMON, "Iron Leggings", new ItemBuilder(Material.IRON_LEGGINGS, 0).asItem(), 0, 0, 20, 0, 0, 0, "IRON_LEGGINGS", ItemType.ARMOR, null,
            Arrays.asList(new CraftingIngrediant(Items.IRON_INGOT, 1), new CraftingIngrediant(Items.IRON_INGOT, 1), new CraftingIngrediant(Items.IRON_INGOT, 1),
                    new CraftingIngrediant(Items.IRON_INGOT, 1), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.IRON_INGOT, 1),
                    new CraftingIngrediant(Items.IRON_INGOT, 1), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.IRON_INGOT, 1)), 1, 24, 20),
    IRON_BOOTS(Rarity.COMMON, "Iron Boots", new ItemBuilder(Material.IRON_BOOTS, 0).asItem(), 0, 0, 20, 0, 0, 0, "IRON_BOOTS", ItemType.ARMOR, null,
            Arrays.asList(new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.NONE, 0),
                    new CraftingIngrediant(Items.IRON_INGOT, 1), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.IRON_INGOT, 1),
                    new CraftingIngrediant(Items.IRON_INGOT, 1), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.IRON_INGOT, 1)), 1, 24, 20),
    /* ----- DIAMOND ARMOR ----- */
    DIAMOND_HELMET(Rarity.UNCOMMON, "Diamond Helmet", new ItemBuilder(Material.DIAMOND_HELMET, 0).asItem(), 0, 0, 40, 0, 0, 10, "DIAMOND_HELMET", ItemType.ARMOR, null,
            Arrays.asList(new CraftingIngrediant(Items.DIAMOND, 1), new CraftingIngrediant(Items.DIAMOND, 1), new CraftingIngrediant(Items.DIAMOND, 1),
                    new CraftingIngrediant(Items.DIAMOND, 1), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.DIAMOND, 1),
                    new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.NONE, 0)), 1, 30, 20),
    DIAMOND_CHESTPLATE(Rarity.UNCOMMON, "Diamond Chestplate", new ItemBuilder(Material.DIAMOND_CHESTPLATE, 0).asItem(), 0, 0, 40, 0, 0, 10, "DIAMOND_CHESTPLATE", ItemType.ARMOR, null,
            Arrays.asList(new CraftingIngrediant(Items.DIAMOND, 1), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.DIAMOND, 1),
                    new CraftingIngrediant(Items.DIAMOND, 1), new CraftingIngrediant(Items.DIAMOND, 1), new CraftingIngrediant(Items.DIAMOND, 1),
                    new CraftingIngrediant(Items.DIAMOND, 1), new CraftingIngrediant(Items.DIAMOND, 1), new CraftingIngrediant(Items.DIAMOND, 1)), 1, 30, 20),
    DIAMOND_LEGGINGS(Rarity.UNCOMMON, "Diamond Leggings", new ItemBuilder(Material.DIAMOND_LEGGINGS, 0).asItem(), 0, 0, 40, 0, 0, 10, "DIAMOND_LEGGINGS", ItemType.ARMOR, null,
            Arrays.asList(new CraftingIngrediant(Items.DIAMOND, 1), new CraftingIngrediant(Items.DIAMOND, 1), new CraftingIngrediant(Items.DIAMOND, 1),
                    new CraftingIngrediant(Items.DIAMOND, 1), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.DIAMOND, 1),
                    new CraftingIngrediant(Items.DIAMOND, 1), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.DIAMOND, 1)), 1, 30, 20),
    DIAMOND_BOOTS(Rarity.UNCOMMON, "Diamond Boots", new ItemBuilder(Material.DIAMOND_BOOTS, 0).asItem(), 0, 0, 40, 0, 0, 10, "DIAMOND_BOOTS", ItemType.ARMOR, null,
            Arrays.asList(new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.NONE, 0),
                    new CraftingIngrediant(Items.DIAMOND, 1), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.DIAMOND, 1),
                    new CraftingIngrediant(Items.DIAMOND, 1), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.DIAMOND, 1)), 1, 30, 20),
    /* ----- ZOMBIE ARMOR ----- */
    ZOMBIE_HELMET(Rarity.RARE, "Zombie Helmet", new ItemBuilder(Material.ZOMBIE_HEAD, 0).asItem(), 0, 0, 60, 0, 0, 100, "ZOMBIE_HELMET", ItemType.ARMOR, null,
            Arrays.asList(new CraftingIngrediant(Items.ZOMBIE_FRAGMENT, 1), new CraftingIngrediant(Items.ZOMBIE_FRAGMENT, 1), new CraftingIngrediant(Items.ZOMBIE_FRAGMENT, 1),
                    new CraftingIngrediant(Items.ZOMBIE_FRAGMENT, 1), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.ZOMBIE_FRAGMENT, 1),
                    new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.NONE, 0)), 1, 30, 25000),
    ZOMBIE_CHESTPLATE(Rarity.RARE, "Zombie Chestplate", ItemTweaker.dye(new ItemBuilder(Material.LEATHER_CHESTPLATE, 0).asItem(), 0, 153, 51), 0, 0, 60, 0, 0, 100, "ZOMBIE_CHESTPLATE", ItemType.ARMOR, null,
            Arrays.asList(new CraftingIngrediant(Items.ZOMBIE_FRAGMENT, 1), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.ZOMBIE_FRAGMENT, 1),
                    new CraftingIngrediant(Items.ZOMBIE_FRAGMENT, 1), new CraftingIngrediant(Items.ZOMBIE_FRAGMENT, 1), new CraftingIngrediant(Items.ZOMBIE_FRAGMENT, 1),
                    new CraftingIngrediant(Items.ZOMBIE_FRAGMENT, 1), new CraftingIngrediant(Items.ZOMBIE_FRAGMENT, 1), new CraftingIngrediant(Items.ZOMBIE_FRAGMENT, 1)), 1, 30, 25000),
    ZOMBIE_LEGGINGS(Rarity.RARE, "Zombie Leggings", ItemTweaker.dye(new ItemBuilder(Material.LEATHER_LEGGINGS, 0).asItem(), 0, 153, 51), 0, 0, 60, 0, 0, 100, "ZOMBIE_LEGGINGS", ItemType.ARMOR, null,
            Arrays.asList(new CraftingIngrediant(Items.ZOMBIE_FRAGMENT, 1), new CraftingIngrediant(Items.ZOMBIE_FRAGMENT, 1), new CraftingIngrediant(Items.ZOMBIE_FRAGMENT, 1),
                    new CraftingIngrediant(Items.ZOMBIE_FRAGMENT, 1), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.ZOMBIE_FRAGMENT, 1),
                    new CraftingIngrediant(Items.ZOMBIE_FRAGMENT, 1), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.ZOMBIE_FRAGMENT, 1)), 1, 30, 25000),
    ZOMBIE_BOOTS(Rarity.RARE, "Zombie Boots", ItemTweaker.dye(new ItemBuilder(Material.LEATHER_BOOTS, 0).asItem(), 0, 153, 51), 0, 0, 60, 0,50, 100, "ZOMBIE_BOOTS", ItemType.ARMOR, null,
            Arrays.asList(new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.NONE, 0),
                    new CraftingIngrediant(Items.ZOMBIE_FRAGMENT, 1), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.ZOMBIE_FRAGMENT, 1),
                    new CraftingIngrediant(Items.ZOMBIE_FRAGMENT, 1), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.ZOMBIE_FRAGMENT, 1)), 1, 30, 25000),
    /* ----- GOLD ARMOR ----- */
    GOLD_HELMET(Rarity.UNCOMMON, "Gold Helmet", new ItemBuilder(Material.GOLDEN_HELMET, 0).asItem(), 0, 0, 30, 0, 0, 0, "GOLDEN_HELMET", ItemType.ARMOR, null,
            Arrays.asList(new CraftingIngrediant(Items.GOLD_INGOT, 1), new CraftingIngrediant(Items.GOLD_INGOT, 1), new CraftingIngrediant(Items.GOLD_INGOT, 1),
                    new CraftingIngrediant(Items.GOLD_INGOT, 1), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.GOLD_INGOT, 1),
                    new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.NONE, 0)), 1, 30, 20),
    GOLD_CHESTPLATE(Rarity.UNCOMMON, "Gold Chestplate", new ItemBuilder(Material.GOLDEN_CHESTPLATE, 0).asItem(), 0, 0, 30, 0,0, 0, "GOLDEN_CHESTPLATE", ItemType.ARMOR, null,
            Arrays.asList(new CraftingIngrediant(Items.GOLD_INGOT, 1), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.GOLD_INGOT, 1),
                    new CraftingIngrediant(Items.GOLD_INGOT, 1), new CraftingIngrediant(Items.GOLD_INGOT, 1), new CraftingIngrediant(Items.GOLD_INGOT, 1),
                    new CraftingIngrediant(Items.GOLD_INGOT, 1), new CraftingIngrediant(Items.GOLD_INGOT, 1), new CraftingIngrediant(Items.GOLD_INGOT, 1)), 1, 30, 20),
    GOLD_LEGGINGS(Rarity.UNCOMMON, "Gold Leggings", new ItemBuilder(Material.GOLDEN_LEGGINGS, 0).asItem(), 0, 0, 30, 0, 0, 0, "GOLDEN_LEGGINGS", ItemType.ARMOR, null,
            Arrays.asList(new CraftingIngrediant(Items.GOLD_INGOT, 1), new CraftingIngrediant(Items.GOLD_INGOT, 1), new CraftingIngrediant(Items.GOLD_INGOT, 1),
                    new CraftingIngrediant(Items.GOLD_INGOT, 1), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.GOLD_INGOT, 1),
                    new CraftingIngrediant(Items.GOLD_INGOT, 1), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.GOLD_INGOT, 1)), 1, 30, 20),
    GOLD_BOOTS(Rarity.UNCOMMON, "Gold Boots", new ItemBuilder(Material.GOLDEN_BOOTS, 0).asItem(), 0, 0, 30, 0, 25, 0, "GOLDEN_BOOTS", ItemType.ARMOR, null,
            Arrays.asList(new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.NONE, 0),
                    new CraftingIngrediant(Items.GOLD_INGOT, 1), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.GOLD_INGOT, 1),
                    new CraftingIngrediant(Items.GOLD_INGOT, 1), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.GOLD_INGOT, 1)), 1, 30, 20);

    private Rarity rarity;
    private String name;
    private ItemStack itemStack;
    private int damage;
    private int strength;
    private int defence;
    private int hp;
    private String id;
    private ItemType itemType;
    private List<CraftingIngrediant> craftingRecipe;
    private int energy;
    private List<String> lore;
    private int craftingXPGained;
    private int sellPrice;
    private int craftingAmount;
    private int speed;
    Items(Rarity rarity, String name, ItemStack itemStack, int damage, int strength, int defence, int energy, int speed,
          int hp, String id, ItemType itemType, List<String> lore, List<CraftingIngrediant> craftingRecipe, int craftingAmount, int craftingXPGained,
          int sellPrice) {

        this.rarity = rarity;
        this.name = name;
        this.itemStack = itemStack;
        this.damage = damage;
        this.strength = strength;
        this.defence = defence;
        this.id = id;
        this.itemType = itemType;
        this.craftingRecipe = craftingRecipe;
        this.energy = energy;
        this.lore = lore;
        this.hp = hp;
        this.craftingXPGained = craftingXPGained;
        this.sellPrice = sellPrice;
        this.craftingAmount = craftingAmount;
        this.speed = speed;
    }
    Items(Rarity rarity, String name, ItemStack itemStack, int damage, int strength, int defence, int energy, int hp, int speed,
          String id, ItemType itemType, List<String> lore, int sellPrice) {
        this.rarity = rarity;
        this.name = name;
        this.itemStack = itemStack;
        this.damage = damage;
        this.strength = strength;
        this.defence = defence;
        this.id = id;
        this.itemType = itemType;
        this.energy = energy;
        this.lore = lore;
        this.hp = hp;
        this.sellPrice = sellPrice;
        this.speed = speed;
    }
    public int getDamage() { return damage; }
    public int getSellPrice() { return sellPrice; }
    public int getDefence() { return defence; }
    public int getStrength() { return strength; }
    public ItemStack getItemStack() { return itemStack; }
    public Rarity getRarity() { return rarity; }
    public String getName() { return name; }
    public String getId() { return id; }
    public ItemType getItemType() { return itemType; }
    public int getEnergy() { return energy; }
    public List<CraftingIngrediant> getCraftingRecipe() { return craftingRecipe; }
    public int getCraftingAmount() { return craftingAmount; }

    public List<String> getLore() { return lore; }
    public int getHp() { return hp; }
    public int getCraftingXPGained() { return craftingXPGained; }
    public static ItemStack buildItem(Items items) {
        ItemStack itemStack = items.getItemStack();
        ItemMeta iM = itemStack.getItemMeta();
        ArrayList<String> lore = new ArrayList<>();
        if (items.getDamage() != 0) {
            lore.add(Text.color("&7Damage: &c+" + items.getDamage()));
        }
        if (items.getStrength() != 0) {
            lore.add(Text.color("&7Strength: &4+" + items.getStrength()));
        }

        if (items.getEnergy() != 0) {
            lore.add(" ");
            lore.add(Text.color("&7Energy: &e+" + items.getStrength()));
        }
        if (items.getDefence() != 0) {
            lore.add(Text.color("&7Defence: &a+" + items.getDefence()));
        }
        if (items.getHp() != 0) {
            lore.add(Text.color("&7Health: &c+" + items.getHp()));
        }
        if (items.getSpeed() != 0) {
            lore.add(" ");
            lore.add(Text.color("&7Speed: &f+" + items.getSpeed()));
        }
        if (items.getLore() != null) {
            lore.add(" ");
            for (int i = 0; i < items.getLore().size(); i++) {
                lore.add(Text.color(items.getLore().get(i)));
            }
        }
        lore.add(" ");
        lore.add(" ");
        lore.add(Text.color(items.getRarity().getNameColored() + " " + items.getItemType()));
        iM.setDisplayName(Text.color(items.getRarity().getColor() + items.getName()));
        iM.setLore(lore);
        itemStack.setItemMeta(iM);
        ItemStack item = itemStack;
        return item;
    }
    public int getSpeed() { return speed; }
}
