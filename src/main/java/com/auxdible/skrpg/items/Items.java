package com.auxdible.skrpg.items;

import com.auxdible.skrpg.utils.ItemBuilder;
import com.auxdible.skrpg.utils.ItemTweaker;
import com.auxdible.skrpg.utils.Text;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum Items {
    NONE(Rarity.COMMON, "None", null, 0, 0, 0, 0, 0, 0, "NONE", ItemType.ITEM, null, 0, false),
    /* ------ NATURAL RECOURSES ------ */
    WOOD(Rarity.COMMON, "Wood", new ItemBuilder(Material.STRIPPED_OAK_LOG, 0).asItem(), 0, 0,
            0, 0, 0, 0, "WOOD", ItemType.MATERIAL, null, 2, false),
    /* ------ WOOD COLLECTION ------ */
    COMPACT_WOOD(Rarity.UNCOMMON, "Compact Wood", new ItemBuilder(Material.DARK_OAK_LOG, 0).asItem(),
            0, 0, 0, 0, 0, 0, "COMPACT_WOOD", ItemType.MATERIAL, null, Arrays.asList(
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.WOOD, 32), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.WOOD, 32), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.WOOD, 32), new CraftingIngrediant(Items.NONE, 0)), 1, 100, 300, true),
    STONE(Rarity.UNCOMMON, "Stone", new ItemBuilder(Material.COBBLESTONE, 0).asItem(), 0, 0, 0, 0, 0, 0,
            "STONE", ItemType.MATERIAL, null, 1, false),
    IRON_INGOT(Rarity.COMMON, "Iron Ingot", new ItemBuilder(Material.IRON_INGOT, 0).asItem(), 0, 0,
            0, 0, 0, 0, "IRON_INGOT", ItemType.MATERIAL, null, 10, false),
    COAL(Rarity.COMMON, "Coal", new ItemBuilder(Material.COAL, 0).asItem(), 0, 0, 0, 0, 0, 0,
            "COAL", ItemType.MATERIAL, null, 5, false),
    DIAMOND(Rarity.COMMON, "Diamond", new ItemBuilder(Material.DIAMOND, 0).asItem(), 0, 0, 0, 0, 0,
            0, "DIAMOND", ItemType.MATERIAL, null, 15, false),
    GOLD_INGOT(Rarity.COMMON, "Gold Ingot", new ItemBuilder(Material.GOLD_INGOT, 0).asItem(), 0, 0, 0, 0, 0, 0,
            "GOLD_INGOT", ItemType.MATERIAL, null, 15, false),
    REDSTONE(Rarity.COMMON, "Redstone", new ItemBuilder(Material.REDSTONE, 0).asItem(), 0, 0, 0, 0, 0, 0,
            "REDSTONE", ItemType.MATERIAL, null, 2, false),
    LAPIS_LAZULI(Rarity.COMMON, "Lapis Lazuli", new ItemBuilder(Material.LAPIS_LAZULI, 0).asItem(), 0, 0, 0, 0,
            0, 0, "LAPIS_LAZULI", ItemType.MATERIAL, null, 3, false),
    EMERALD(Rarity.COMMON, "Emerald", new ItemBuilder(Material.EMERALD, 0).asItem(), 0, 0, 0,
            0, 0, 0, "EMERALD", ItemType.MATERIAL, null, 15, false),
    OBSIDIAN(Rarity.UNCOMMON, "Obsidian", new ItemBuilder(Material.OBSIDIAN, 0).asItem(), 0, 0, 0, 0, 0, 0,
            "OBSIDIAN", ItemType.MATERIAL, null, 20, false),
    WHEAT(Rarity.COMMON, "Wheat", new ItemBuilder(Material.WHEAT, 0).asItem(), 0, 0, 0, 0, 0, 0,
            "WHEAT", ItemType.MATERIAL, null, 1, false),
    CARROT(Rarity.COMMON, "Carrot", new ItemBuilder(Material.CARROT, 0).asItem(), 0, 0, 0, 0, 0, 0,
            "CARROT", ItemType.MATERIAL, null, 1, false),
    CANE(Rarity.COMMON, "Sugar Cane", new ItemBuilder(Material.SUGAR_CANE, 0).asItem(), 0, 0, 0, 0, 0, 0,
            "CANE", ItemType.MATERIAL, null, 1, false),
    POTATO(Rarity.COMMON, "Potato", new ItemBuilder(Material.POTATO, 0).asItem(), 0, 0, 0, 0, 0, 0,
            "POTATO", ItemType.MATERIAL, null, 1, false),
    SWEET_BERRIES(Rarity.COMMON, "Sweet Berries", new ItemBuilder(Material.SWEET_BERRIES, 0).asItem(), 0, 0,
            0, 0, 0, 0,
            "POTATO", ItemType.MATERIAL, null, 5, false),
    BEETROOT(Rarity.COMMON, "Beetroot", new ItemBuilder(Material.BEETROOT, 0).asItem(), 0, 0,
            0, 0, 0, 0,
            "BEETROOT", ItemType.MATERIAL, null, 1, false),
    PUMPKIN(Rarity.COMMON, "Pumpkin", new ItemBuilder(Material.PUMPKIN, 0).asItem(), 0, 0, 0, 0, 0, 0, "PUMPKIN",
            ItemType.MATERIAL, null, 5, false),
    MELON(Rarity.COMMON, "Melon", new ItemBuilder(Material.MELON, 0).asItem(), 0, 0, 0, 0, 0, 0, "MELON",
            ItemType.MATERIAL, null, 5, false),
    ROTTEN_FLESH(Rarity.COMMON, "Rotten Flesh", new ItemBuilder(Material.ROTTEN_FLESH, 0).asItem(),
            0, 0, 0, 0, 0, 0, "ROTTEN_FLESH", ItemType.MATERIAL, null,  5, false),
    ZOMBIE_FRAGMENT(Rarity.RARE, "Zombie Fragment", new ItemBuilder(Material.ZOMBIE_HEAD, 0).asItem(),
            0,  0,0, 0, 0, 0, "ZOMBIE_FRAGMENT", ItemType.MATERIAL, null, 10000, false),
    CRYSTALLITE(Rarity.UNCOMMON, "Crystalite", new ItemBuilder(Material.QUARTZ, 0).asItem(), 0, 0, 0, 0, 0, 0, "CRYSTALLITE", ItemType.MATERIAL,
            Arrays.asList(Text.color("&dCrystalite, &7&oa mysterious resource, has unknown origins."),
                    Text.color("&7&oAll we know is that the townspeople use it for their windows.")), 100, true),
    NETHERRACK(Rarity.COMMON, "Netherrack", new ItemBuilder(Material.NETHERRACK, 0).asItem(), 0, 0, 0, 0, 0, 0, "NETHERRACK", ItemType.MATERIAL, null, 10, false),
    END_STONE(Rarity.UNCOMMON, "End Stone", new ItemBuilder(Material.END_STONE, 0).asItem(), 0, 0, 0, 0, 0, 0, "END_STONE", ItemType.MATERIAL, null, 120, false),
    CORRUPTED_ROOT(Rarity.RARE, "Corrupted Root", new ItemBuilder(Material.PURPLE_DYE, 0).asItem(),
            5, 0, 0, 0, 0, 0, "CORRUPTED_ROOT", ItemType.ITEM,
            Arrays.asList(Text.color("&7&oHow did we get here?")), 150, true),
    /* ------ MERCHANT ITEMS ------*/
    STARTER_SWORD(Rarity.COMMON, "Starter Sword", new ItemBuilder(Material.GOLDEN_SWORD, 0).asItem(), 35, 5, 10, 0, 0, 0, "STARTER_SWORD", ItemType.SWORD,
            Arrays.asList(Text.color("&8&l[&e&lABILITY&8&l] &r&e10 Energy"), Text.color("&7Gain a temporary &f+25 Speed")), 10, false),
    ZOMBIE_SWORD(Rarity.UNCOMMON, "Zombie Sword", new ItemBuilder(Material.IRON_SWORD, 0).asItem(), 40, 10, 0, 0, 0, 0, "ZOMBIE_SWORD", ItemType.SWORD,
            Arrays.asList(Text.color("&7Do &c+50% damage &7to all &cZombies.")), 10, false),
    BOW(Rarity.UNCOMMON, "Test Bow", new ItemBuilder(Material.BOW, 0).asItem(), 50, 50, 0, 0, 0, 50, "BOW", ItemType.BOW, null, 100, false),
    /* ------ CRAFTABLE ITEMS ------ */
    PLANK(Rarity.COMMON, "Plank", new ItemBuilder(Material.OAK_PLANKS, 0).asItem(), 0, 0, 0, 0, 0, 0, "PLANK",
            ItemType.MATERIAL, null, Arrays.asList(
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.WOOD, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.NONE, 0)) , 4, 2, 3, false),
    STICK(Rarity.COMMON, "Stick", new ItemBuilder(Material.STICK, 0).asItem(), 0, 0, 0, 0, 0, 0, "STICK", ItemType.MATERIAL,
            null, Arrays.asList(
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.PLANK, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.PLANK, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.NONE, 0)), 4, 3, 5, false),
    BASIC_HILT(Rarity.COMMON, "Basic Hilt", new ItemBuilder(Material.STICK, 0).asItem(), 0, 0, 0, 0, 0, 0, "BASIC_HILT", ItemType.ITEM,
            Arrays.asList(Text.color("&7Used to craft a &fCommon &7tool.")), Arrays.asList(new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.PLANK, 3), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.STICK, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.PLANK, 1), new CraftingIngrediant(Items.NONE, 0)), 1, 20, 10, false),
    ADVANCED_HILT(Rarity.UNCOMMON, "Advanced Hilt", new ItemBuilder(Material.STICK, 0).asItem(), 0, 0, 0, 0, 0, 0, "ADVANCED_HILT", ItemType.ITEM,
            Arrays.asList(Text.color("&7Used to craft a &aUncommon &7tool.")), Arrays.asList(new CraftingIngrediant(Items.IRON_INGOT, 4), new CraftingIngrediant(Items.GOLD_INGOT, 1), new CraftingIngrediant(Items.IRON_INGOT, 4),
            new CraftingIngrediant(Items.IRON_INGOT, 4), new CraftingIngrediant(Items.BASIC_HILT, 1), new CraftingIngrediant(Items.IRON_INGOT, 4),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.PLANK, 4), new CraftingIngrediant(Items.NONE, 0)), 1, 40, 20, false),
    /* ------ WOOD TOOLS ------ */
    WOODEN_PICKAXE(Rarity.COMMON, "Wooden Pickaxe", new ItemBuilder(Material.WOODEN_PICKAXE, 0).asItem(), 2, 0, 0, 0, 0, 0, "WOODEN_PICKAXE", ItemType.PICKAXE,
            null, Arrays.asList(new CraftingIngrediant(Items.PLANK, 1), new CraftingIngrediant(Items.PLANK, 1), new CraftingIngrediant(Items.PLANK, 1),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.STICK, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.BASIC_HILT, 1), new CraftingIngrediant(Items.NONE, 0)), 1, 22, 15, false),
    WOODEN_SWORD(Rarity.COMMON, "Wooden Sword", new ItemBuilder(Material.WOODEN_SWORD, 0).asItem(), 20, 0, 0, 0, 0, 0, "WOODEN_SWORD", ItemType.SWORD,
            null, Arrays.asList(new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.PLANK, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.PLANK, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.BASIC_HILT, 1), new CraftingIngrediant(Items.NONE, 0)), 1, 22, 15, false),
    WOODEN_HOE(Rarity.COMMON, "Wooden Hoe", new ItemBuilder(Material.WOODEN_HOE, 0).asItem(), 2, 0, 0, 0, 0, 0, "WOODEN_HOE", ItemType.HOE,
            null, Arrays.asList(new CraftingIngrediant(Items.PLANK, 1), new CraftingIngrediant(Items.PLANK, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.STICK, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.BASIC_HILT, 1), new CraftingIngrediant(Items.NONE, 0)), 1, 22, 15, false),
    /* ----- STONE TOOLS ----- */
    STONE_PICKAXE(Rarity.COMMON, "Stone Pickaxe", new ItemBuilder(Material.STONE_PICKAXE, 0).asItem(), 4, 0, 0, 0, 0, 0, "STONE_PICKAXE", ItemType.PICKAXE,
            null, Arrays.asList(new CraftingIngrediant(Items.STONE, 1), new CraftingIngrediant(Items.STONE, 1), new CraftingIngrediant(Items.STONE, 1),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.STICK, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.BASIC_HILT, 1), new CraftingIngrediant(Items.NONE, 0)), 1, 24, 15, false),
    STONE_SWORD(Rarity.COMMON, "Stone Sword", new ItemBuilder(Material.STONE_SWORD, 0).asItem(), 25, 0, 0, 0, 0, 0, "STONE_SWORD", ItemType.SWORD,
            null, Arrays.asList(new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.STONE, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.STONE, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.BASIC_HILT, 1), new CraftingIngrediant(Items.NONE, 0)), 1, 24, 15, false),
    STONE_HOE(Rarity.COMMON, "Stone Hoe", new ItemBuilder(Material.STONE_HOE, 0).asItem(), 4, 0, 0, 0, 0, 0, "STONE_HOE", ItemType.HOE,
            null, Arrays.asList(new CraftingIngrediant(Items.STONE, 1), new CraftingIngrediant(Items.STONE, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.STICK, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.BASIC_HILT, 1), new CraftingIngrediant(Items.NONE, 0)), 1, 24, 15, false),
    /* ----- IRON TOOLS ----- */
    IRON_PICKAXE(Rarity.COMMON, "Iron Pickaxe", new ItemBuilder(Material.IRON_PICKAXE, 0).asItem(), 6, 0, 0, 0, 0, 0, "IRON_PICKAXE", ItemType.PICKAXE,
            null, Arrays.asList(new CraftingIngrediant(Items.IRON_INGOT, 1), new CraftingIngrediant(Items.IRON_INGOT, 1), new CraftingIngrediant(Items.IRON_INGOT, 1),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.STICK, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.BASIC_HILT, 1), new CraftingIngrediant(Items.NONE, 0)), 1, 24, 20, false),
    IRON_SWORD(Rarity.COMMON, "Iron Sword", new ItemBuilder(Material.IRON_SWORD, 0).asItem(), 35, 0, 0, 0, 0, 0, "IRON_SWORD", ItemType.SWORD,
            null, Arrays.asList(new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.IRON_INGOT, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.IRON_INGOT, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.BASIC_HILT, 1), new CraftingIngrediant(Items.NONE, 0)), 1, 24, 20, false),
    IRON_HOE(Rarity.COMMON, "Iron Hoe", new ItemBuilder(Material.IRON_HOE, 0).asItem(), 6, 0, 0, 0, 0, 0, "IRON_HOE", ItemType.HOE,
            null, Arrays.asList(new CraftingIngrediant(Items.IRON_INGOT, 1), new CraftingIngrediant(Items.IRON_INGOT, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.STICK, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.BASIC_HILT, 1), new CraftingIngrediant(Items.NONE, 0)), 1, 24, 20, false),
    /* ----- GOLDEN TOOLS ----- */
    GOLDEN_PICKAXE(Rarity.UNCOMMON, "Golden Pickaxe", new ItemBuilder(Material.GOLDEN_PICKAXE, 0).asItem(), 1, 0, 0, 0, 0, 0, "GOLDEN_PICKAXE", ItemType.PICKAXE,
            null, Arrays.asList(new CraftingIngrediant(Items.GOLD_INGOT, 1), new CraftingIngrediant(Items.GOLD_INGOT, 1), new CraftingIngrediant(Items.GOLD_INGOT, 1),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.STICK, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.ADVANCED_HILT, 1), new CraftingIngrediant(Items.NONE, 0)), 1, 26, 25, false),
    GOLDEN_SWORD(Rarity.COMMON, "Golden Sword", new ItemBuilder(Material.GOLDEN_SWORD, 0).asItem(), 20, 20, 0, 0, 0, 0, "GOLDEN_SWORD", ItemType.SWORD,
            null, Arrays.asList(new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.GOLD_INGOT, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.GOLD_INGOT, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.ADVANCED_HILT, 1), new CraftingIngrediant(Items.NONE, 0)), 1, 26, 25, false),
    GOLDEN_HOE(Rarity.UNCOMMON, "Golden Hoe", new ItemBuilder(Material.GOLDEN_HOE, 0).asItem(), 4, 0, 0, 0, 0, 0, "GOLDEN_HOE", ItemType.HOE,
            null, Arrays.asList(new CraftingIngrediant(Items.GOLD_INGOT, 1), new CraftingIngrediant(Items.GOLD_INGOT, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.STICK, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.ADVANCED_HILT, 1), new CraftingIngrediant(Items.NONE, 0)), 1, 24, 25, false),
    /* ----- DIAMOND TOOLS  -----*/
    DIAMOND_PICKAXE(Rarity.UNCOMMON, "Diamond Pickaxe", new ItemBuilder(Material.DIAMOND_PICKAXE, 0).asItem(), 10, 0, 0, 0, 0, 0, "DIAMOND_PICKAXE", ItemType.PICKAXE,
            null, Arrays.asList(new CraftingIngrediant(Items.DIAMOND, 1), new CraftingIngrediant(Items.DIAMOND, 1), new CraftingIngrediant(Items.DIAMOND, 1),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.STICK, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.ADVANCED_HILT, 1), new CraftingIngrediant(Items.NONE, 0)), 1, 30, 30, false),
    DIAMOND_SWORD(Rarity.UNCOMMON, "Diamond Sword", new ItemBuilder(Material.DIAMOND_SWORD, 0).asItem(), 50, 0, 0, 0, 0, 0, "DIAMOND_SWORD", ItemType.SWORD,
            null, Arrays.asList(new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.DIAMOND, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.DIAMOND, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.ADVANCED_HILT, 1), new CraftingIngrediant(Items.NONE, 0)), 1, 30, 30, false),
    DIAMOND_HOE(Rarity.UNCOMMON, "Diamond Hoe", new ItemBuilder(Material.DIAMOND_HOE, 0).asItem(), 10, 0, 0, 0,  0,0, "DIAMOND_HOE", ItemType.HOE,
            null, Arrays.asList(new CraftingIngrediant(Items.DIAMOND, 1), new CraftingIngrediant(Items.DIAMOND, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.STICK, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.ADVANCED_HILT, 1), new CraftingIngrediant(Items.NONE, 0)), 1, 30, 30, false),
    /* ----- IRON ARMOR ------ */
    IRON_HELMET(Rarity.COMMON, "Iron Helmet", new ItemBuilder(Material.IRON_HELMET, 0).asItem(), 0, 0, 20, 0, 0, 0, "IRON_HELMET", ItemType.ARMOR, null,
            Arrays.asList(new CraftingIngrediant(Items.IRON_INGOT, 1), new CraftingIngrediant(Items.IRON_INGOT, 1), new CraftingIngrediant(Items.IRON_INGOT, 1),
                    new CraftingIngrediant(Items.IRON_INGOT, 1), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.IRON_INGOT, 1),
                    new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.NONE, 0)), 1, 24, 20, false),
    IRON_CHESTPLATE(Rarity.COMMON, "Iron Chestplate", new ItemBuilder(Material.IRON_CHESTPLATE, 0).asItem(), 0, 0, 20, 0, 0, 0, "IRON_CHESTPLATE", ItemType.ARMOR, null,
            Arrays.asList(new CraftingIngrediant(Items.IRON_INGOT, 1), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.IRON_INGOT, 1),
                    new CraftingIngrediant(Items.IRON_INGOT, 1), new CraftingIngrediant(Items.IRON_INGOT, 1), new CraftingIngrediant(Items.IRON_INGOT, 1),
                    new CraftingIngrediant(Items.IRON_INGOT, 1), new CraftingIngrediant(Items.IRON_INGOT, 1), new CraftingIngrediant(Items.IRON_INGOT, 1)), 1, 24, 20, false),
    IRON_LEGGINGS(Rarity.COMMON, "Iron Leggings", new ItemBuilder(Material.IRON_LEGGINGS, 0).asItem(), 0, 0, 20, 0, 0, 0, "IRON_LEGGINGS", ItemType.ARMOR, null,
            Arrays.asList(new CraftingIngrediant(Items.IRON_INGOT, 1), new CraftingIngrediant(Items.IRON_INGOT, 1), new CraftingIngrediant(Items.IRON_INGOT, 1),
                    new CraftingIngrediant(Items.IRON_INGOT, 1), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.IRON_INGOT, 1),
                    new CraftingIngrediant(Items.IRON_INGOT, 1), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.IRON_INGOT, 1)), 1, 24, 20, false),
    IRON_BOOTS(Rarity.COMMON, "Iron Boots", new ItemBuilder(Material.IRON_BOOTS, 0).asItem(), 0, 0, 20, 0, 0, 0, "IRON_BOOTS", ItemType.ARMOR, null,
            Arrays.asList(new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.NONE, 0),
                    new CraftingIngrediant(Items.IRON_INGOT, 1), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.IRON_INGOT, 1),
                    new CraftingIngrediant(Items.IRON_INGOT, 1), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.IRON_INGOT, 1)), 1, 24, 20, false),
    /* ----- DIAMOND ARMOR ----- */
    DIAMOND_HELMET(Rarity.UNCOMMON, "Diamond Helmet", new ItemBuilder(Material.DIAMOND_HELMET, 0).asItem(), 0, 0, 40, 0, 0, 10, "DIAMOND_HELMET", ItemType.ARMOR, null,
            Arrays.asList(new CraftingIngrediant(Items.DIAMOND, 1), new CraftingIngrediant(Items.DIAMOND, 1), new CraftingIngrediant(Items.DIAMOND, 1),
                    new CraftingIngrediant(Items.DIAMOND, 1), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.DIAMOND, 1),
                    new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.NONE, 0)), 1, 30, 20, false),
    DIAMOND_CHESTPLATE(Rarity.UNCOMMON, "Diamond Chestplate", new ItemBuilder(Material.DIAMOND_CHESTPLATE, 0).asItem(), 0, 0, 40, 0, 0, 10, "DIAMOND_CHESTPLATE", ItemType.ARMOR, null,
            Arrays.asList(new CraftingIngrediant(Items.DIAMOND, 1), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.DIAMOND, 1),
                    new CraftingIngrediant(Items.DIAMOND, 1), new CraftingIngrediant(Items.DIAMOND, 1), new CraftingIngrediant(Items.DIAMOND, 1),
                    new CraftingIngrediant(Items.DIAMOND, 1), new CraftingIngrediant(Items.DIAMOND, 1), new CraftingIngrediant(Items.DIAMOND, 1)), 1, 30, 20, false),
    DIAMOND_LEGGINGS(Rarity.UNCOMMON, "Diamond Leggings", new ItemBuilder(Material.DIAMOND_LEGGINGS, 0).asItem(), 0, 0, 40, 0, 0, 10, "DIAMOND_LEGGINGS", ItemType.ARMOR, null,
            Arrays.asList(new CraftingIngrediant(Items.DIAMOND, 1), new CraftingIngrediant(Items.DIAMOND, 1), new CraftingIngrediant(Items.DIAMOND, 1),
                    new CraftingIngrediant(Items.DIAMOND, 1), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.DIAMOND, 1),
                    new CraftingIngrediant(Items.DIAMOND, 1), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.DIAMOND, 1)), 1, 30, 20, false),
    DIAMOND_BOOTS(Rarity.UNCOMMON, "Diamond Boots", new ItemBuilder(Material.DIAMOND_BOOTS, 0).asItem(), 0, 0, 40, 0, 0, 10, "DIAMOND_BOOTS", ItemType.ARMOR, null,
            Arrays.asList(new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.NONE, 0),
                    new CraftingIngrediant(Items.DIAMOND, 1), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.DIAMOND, 1),
                    new CraftingIngrediant(Items.DIAMOND, 1), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.DIAMOND, 1)), 1, 30, 20, false),
    /* ----- ZOMBIE ARMOR ----- */
    ZOMBIE_HELMET(Rarity.RARE, "Zombie Helmet", new ItemBuilder(Material.ZOMBIE_HEAD, 0).asItem(), 0, 0, 60, 0, 0, 100, "ZOMBIE_HELMET", ItemType.ARMOR, null,
            Arrays.asList(new CraftingIngrediant(Items.ZOMBIE_FRAGMENT, 1), new CraftingIngrediant(Items.ZOMBIE_FRAGMENT, 1), new CraftingIngrediant(Items.ZOMBIE_FRAGMENT, 1),
                    new CraftingIngrediant(Items.ZOMBIE_FRAGMENT, 1), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.ZOMBIE_FRAGMENT, 1),
                    new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.NONE, 0)), 1, 30, 25000, false),
    ZOMBIE_CHESTPLATE(Rarity.RARE, "Zombie Chestplate", ItemTweaker.dye(new ItemBuilder(Material.LEATHER_CHESTPLATE, 0).asItem(), 0, 153, 51), 0, 0, 60, 0, 0, 100, "ZOMBIE_CHESTPLATE", ItemType.ARMOR, null,
            Arrays.asList(new CraftingIngrediant(Items.ZOMBIE_FRAGMENT, 1), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.ZOMBIE_FRAGMENT, 1),
                    new CraftingIngrediant(Items.ZOMBIE_FRAGMENT, 1), new CraftingIngrediant(Items.ZOMBIE_FRAGMENT, 1), new CraftingIngrediant(Items.ZOMBIE_FRAGMENT, 1),
                    new CraftingIngrediant(Items.ZOMBIE_FRAGMENT, 1), new CraftingIngrediant(Items.ZOMBIE_FRAGMENT, 1), new CraftingIngrediant(Items.ZOMBIE_FRAGMENT, 1)), 1, 30, 25000, false),
    ZOMBIE_LEGGINGS(Rarity.RARE, "Zombie Leggings", ItemTweaker.dye(new ItemBuilder(Material.LEATHER_LEGGINGS, 0).asItem(), 0, 153, 51), 0, 0, 60, 0, 0, 100, "ZOMBIE_LEGGINGS", ItemType.ARMOR, null,
            Arrays.asList(new CraftingIngrediant(Items.ZOMBIE_FRAGMENT, 1), new CraftingIngrediant(Items.ZOMBIE_FRAGMENT, 1), new CraftingIngrediant(Items.ZOMBIE_FRAGMENT, 1),
                    new CraftingIngrediant(Items.ZOMBIE_FRAGMENT, 1), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.ZOMBIE_FRAGMENT, 1),
                    new CraftingIngrediant(Items.ZOMBIE_FRAGMENT, 1), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.ZOMBIE_FRAGMENT, 1)), 1, 30, 25000, false),
    ZOMBIE_BOOTS(Rarity.RARE, "Zombie Boots", ItemTweaker.dye(new ItemBuilder(Material.LEATHER_BOOTS, 0).asItem(), 0, 153, 51), 0, 0, 60, 0,50, 100, "ZOMBIE_BOOTS", ItemType.ARMOR, null,
            Arrays.asList(new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.NONE, 0),
                    new CraftingIngrediant(Items.ZOMBIE_FRAGMENT, 1), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.ZOMBIE_FRAGMENT, 1),
                    new CraftingIngrediant(Items.ZOMBIE_FRAGMENT, 1), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.ZOMBIE_FRAGMENT, 1)), 1, 30, 25000, false),
    /* ----- GOLD ARMOR ----- */
    GOLD_HELMET(Rarity.UNCOMMON, "Gold Helmet", new ItemBuilder(Material.GOLDEN_HELMET, 0).asItem(), 0, 0, 30, 0, 0, 0, "GOLDEN_HELMET", ItemType.ARMOR, null,
            Arrays.asList(new CraftingIngrediant(Items.GOLD_INGOT, 1), new CraftingIngrediant(Items.GOLD_INGOT, 1), new CraftingIngrediant(Items.GOLD_INGOT, 1),
                    new CraftingIngrediant(Items.GOLD_INGOT, 1), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.GOLD_INGOT, 1),
                    new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.NONE, 0)), 1, 30, 20, false),
    GOLD_CHESTPLATE(Rarity.UNCOMMON, "Gold Chestplate", new ItemBuilder(Material.GOLDEN_CHESTPLATE, 0).asItem(), 0, 0, 30, 0,0, 0, "GOLDEN_CHESTPLATE", ItemType.ARMOR, null,
            Arrays.asList(new CraftingIngrediant(Items.GOLD_INGOT, 1), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.GOLD_INGOT, 1),
                    new CraftingIngrediant(Items.GOLD_INGOT, 1), new CraftingIngrediant(Items.GOLD_INGOT, 1), new CraftingIngrediant(Items.GOLD_INGOT, 1),
                    new CraftingIngrediant(Items.GOLD_INGOT, 1), new CraftingIngrediant(Items.GOLD_INGOT, 1), new CraftingIngrediant(Items.GOLD_INGOT, 1)), 1, 30, 20, false),
    GOLD_LEGGINGS(Rarity.UNCOMMON, "Gold Leggings", new ItemBuilder(Material.GOLDEN_LEGGINGS, 0).asItem(), 0, 0, 30, 0, 0, 0, "GOLDEN_LEGGINGS", ItemType.ARMOR, null,
            Arrays.asList(new CraftingIngrediant(Items.GOLD_INGOT, 1), new CraftingIngrediant(Items.GOLD_INGOT, 1), new CraftingIngrediant(Items.GOLD_INGOT, 1),
                    new CraftingIngrediant(Items.GOLD_INGOT, 1), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.GOLD_INGOT, 1),
                    new CraftingIngrediant(Items.GOLD_INGOT, 1), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.GOLD_INGOT, 1)), 1, 30, 20, false),
    GOLD_BOOTS(Rarity.UNCOMMON, "Gold Boots", new ItemBuilder(Material.GOLDEN_BOOTS, 0).asItem(), 0, 0, 30, 0, 25, 0, "GOLDEN_BOOTS", ItemType.ARMOR, null,
            Arrays.asList(new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.NONE, 0),
                    new CraftingIngrediant(Items.GOLD_INGOT, 1), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.GOLD_INGOT, 1),
                    new CraftingIngrediant(Items.GOLD_INGOT, 1), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.GOLD_INGOT, 1)), 1, 30, 20, false),
    /* ---- MISC ---- */
    NATURE_STAFF(Rarity.RARE, "Nature Staff", new ItemBuilder(Material.STICK, 0).asItem(), 50, 30, 0, 50, 0, 100, "NATURE_STAFF", ItemType.ITEM, null, 20, true);
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
    private boolean glows;
    Items(Rarity rarity, String name, ItemStack itemStack, int damage, int strength, int defence, int energy, int speed,
          int hp, String id, ItemType itemType, List<String> lore, List<CraftingIngrediant> craftingRecipe, int craftingAmount, int craftingXPGained,
          int sellPrice, boolean glows) {

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
        this.glows = glows;
    }
    Items(Rarity rarity, String name, ItemStack itemStack, int damage, int strength, int defence, int energy, int hp, int speed,
          String id, ItemType itemType, List<String> lore, int sellPrice, boolean glows) {
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
        this.glows = glows;
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
        if (items == Items.NONE) {
            return itemStack;
        }
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
        ItemMeta iM = itemStack.getItemMeta();
        iM.setDisplayName(Text.color(items.getRarity().getColor() + items.getName()));
        iM.setLore(lore);
        if (items.doesGlow()) {
            iM.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
        }
        iM.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        iM.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        iM.setUnbreakable(true);
        iM.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemStack.setItemMeta(iM);
        ItemStack item = itemStack;
        return item;
    }
    public int getSpeed() { return speed; }

    public boolean doesGlow() { return glows; }
}
