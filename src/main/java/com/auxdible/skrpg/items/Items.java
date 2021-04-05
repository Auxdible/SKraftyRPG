package com.auxdible.skrpg.items;

import com.auxdible.skrpg.items.enchantments.Enchantments;
import com.auxdible.skrpg.utils.ItemBuilder;
import com.auxdible.skrpg.utils.ItemTweaker;
import com.auxdible.skrpg.utils.Text;
import net.minecraft.server.v1_16_R3.NBTTagCompound;
import net.minecraft.server.v1_16_R3.NBTTagString;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public enum Items {
    NONE(Rarity.COMMON, "How Did You Do That???", null, 0, 0, 0, 0, 0, 0, "NONE", ItemType.ITEM, null, SellMerchant.COMBAT, 0, false),
    /* ------ NATURAL RECOURSES ------ */
    WOOD(Rarity.COMMON, "Wood", new ItemBuilder(Material.STRIPPED_OAK_LOG, 0).asItem(), 0, 0,
            0, 0, 0, 0, "WOOD", ItemType.MATERIAL, null, SellMerchant.WOODCUTTING, 2, false),
    /* ------ WOOD COLLECTION ------ */
    COMPACT_WOOD(Rarity.UNCOMMON, "Compact Wood", new ItemBuilder(Material.DARK_OAK_LOG, 0).asItem(),
            0, 0, 0, 0, 0, 0, "COMPACT_WOOD", ItemType.MATERIAL, null, Arrays.asList(
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.WOOD, 32), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.WOOD, 32), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.WOOD, 32), new CraftingIngrediant(Items.NONE, 0)), 1, 100, SellMerchant.WOODCUTTING, 300, true),
    STONE(Rarity.COMMON, "Stone", new ItemBuilder(Material.COBBLESTONE, 0).asItem(), 0, 0, 0, 0, 0, 0,
            "STONE", ItemType.MATERIAL, null, SellMerchant.MINING, 1, false),
    /* ------ STONE COLLECTION ------ */
    COMPACT_STONE(Rarity.RARE, "Compact Stone", new ItemBuilder(Material.STONE, 0).asItem(),
            0, 0, 0, 0, 0, 0, "COMPACT_STONE", ItemType.MATERIAL, null, Arrays.asList(
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.STONE, 32), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.STONE, 32), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.STONE, 32), new CraftingIngrediant(Items.NONE, 0)), 1, 50, SellMerchant.MINING, 100, true),
    GEMSTONE_ORE(Rarity.UNCOMMON, "Gemstone Ore", new ItemBuilder(Material.EMERALD, 0).asItem(), 0, 0, 0, 0, 0, 0, "GEMSTONE", ItemType.MATERIAL, Arrays.asList(Text.color("&7An uncommon material extracted from any ore.")), SellMerchant.MINING, 50, true),
    COMPACT_GEMSTONE(Rarity.RARE, "Compact Gemstone", new ItemBuilder(Material.EMERALD_BLOCK, 0).asItem(),
            0, 0, 0, 0, 0, 0, "COMPACT_GEMSTONE", ItemType.MATERIAL, null, Arrays.asList(
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.GEMSTONE_ORE, 32), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.GEMSTONE_ORE, 32), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.GEMSTONE_ORE, 32), new CraftingIngrediant(Items.NONE, 0)), 1, 100, SellMerchant.MINING, 100000, true),
    IRON_INGOT(Rarity.COMMON, "Iron Ingot", new ItemBuilder(Material.IRON_INGOT, 0).asItem(), 0, 0,
            0, 0, 0, 0, "IRON_INGOT", ItemType.MATERIAL, null,  SellMerchant.MINING, 10, false),
    /* ------ IRON COLLECTION ------ */
    COMPACT_IRON(Rarity.UNCOMMON, "Compact Iron", new ItemBuilder(Material.IRON_INGOT, 0).asItem(),
            0, 0, 0, 0, 0, 0, "COMPACT_IRON", ItemType.MATERIAL, null, Arrays.asList(
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.IRON_INGOT, 32), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.IRON_INGOT, 32), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.IRON_INGOT, 32), new CraftingIngrediant(Items.NONE, 0)), 1, 50, SellMerchant.MINING, 250, true),
    COAL(Rarity.COMMON, "Coal", new ItemBuilder(Material.COAL, 0).asItem(), 0, 0, 0, 0, 0, 0,
            "COAL", ItemType.MATERIAL, null,  SellMerchant.MINING, 5, false),
    /* ------ COAL COLLECTION ------ */
    DIAMOND(Rarity.COMMON, "Diamond", new ItemBuilder(Material.DIAMOND, 0).asItem(), 0, 0, 0, 0, 0,
            0, "DIAMOND", ItemType.MATERIAL, null,  SellMerchant.MINING, 15, false),
    COMPACT_DIAMOND(Rarity.UNCOMMON, "Compact Diamond", new ItemBuilder(Material.DIAMOND, 0).asItem(),
            0, 0, 0, 0, 0, 0, "COMPACT_DIAMOND", ItemType.MATERIAL, null, Arrays.asList(
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.DIAMOND, 32), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.DIAMOND, 32), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.DIAMOND, 32), new CraftingIngrediant(Items.NONE, 0)), 1, 50, SellMerchant.MINING, 250, true),
    /* ------ DIAMOND COLLECTION ------ */
    GOLD_INGOT(Rarity.COMMON, "Gold Ingot", new ItemBuilder(Material.GOLD_INGOT, 0).asItem(), 0, 0, 0, 0, 0, 0,
            "GOLD_INGOT", ItemType.MATERIAL, null, SellMerchant.MINING, 15, false),
    PREMIUM_GOLD_EXTRACT(Rarity.RARE, "Premium Gold Extract", new ItemBuilder(Material.GOLD_NUGGET, 0).asItem(), 0, 0, 0, 0, 0, 0, "PREMIUM_GOLD_EXTRACT", ItemType.MATERIAL, Arrays.asList(Text.color("&7A rare extract found from &6Gold&7.")), SellMerchant.MINING, 5000, true),
    /* ------ GOLD COLLECTION ------ */
    REDSTONE(Rarity.COMMON, "Redstone", new ItemBuilder(Material.REDSTONE, 0).asItem(), 0, 0, 0, 0, 0, 0,
            "REDSTONE", ItemType.MATERIAL, null, SellMerchant.MINING, 2, false),
    /* ------ REDSTONE COLLECTION ------ */
    LAPIS_LAZULI(Rarity.COMMON, "Lapis Lazuli", new ItemBuilder(Material.LAPIS_LAZULI, 0).asItem(), 0, 0, 0, 0,
            0, 0, "LAPIS_LAZULI", ItemType.MATERIAL, null, SellMerchant.MINING, 3, false),
    /* ------ LAPIS COLLECTION ------ */
    EMERALD(Rarity.COMMON, "Emerald", new ItemBuilder(Material.EMERALD, 0).asItem(), 0, 0, 0,
            0, 0, 0, "EMERALD", ItemType.MATERIAL, null, SellMerchant.MINING, 15, false),
    /* ------ EMERALD COLLECTION ------ */
    OBSIDIAN(Rarity.UNCOMMON, "Obsidian", new ItemBuilder(Material.OBSIDIAN, 0).asItem(), 0, 0, 0, 0, 0, 0,
            "OBSIDIAN", ItemType.MATERIAL, null, SellMerchant.MINING, 20, false),
    /* ------ OBSIDIAN COLLECTION ------ */
    COMPACT_OBSIDIAN(Rarity.RARE, "Compact Obsidian", new ItemBuilder(Material.OBSIDIAN, 0).asItem(),
            0, 0, 0, 0, 0, 0, "COMPACT_OBSIDIAN", ItemType.MATERIAL, null, Arrays.asList(
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.OBSIDIAN, 16), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.OBSIDIAN, 16), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.OBSIDIAN, 16), new CraftingIngrediant(Items.NONE, 0)), 1, 50, SellMerchant.MINING, 250, true),
    MINER_HELMET(Rarity.UNCOMMON, "Miner Helmet", ItemTweaker.dye(new ItemBuilder(Material.LEATHER_HELMET, 0).asItem(), 53, 0, 68), 0, 0, 100, 0, 0, 125, "MINER_HELMET", ItemType.ARMOR, null,
            Arrays.asList(new CraftingIngrediant(Items.COMPACT_OBSIDIAN, 1), new CraftingIngrediant(Items.COMPACT_OBSIDIAN, 1), new CraftingIngrediant(Items.COMPACT_OBSIDIAN, 1),
                    new CraftingIngrediant(Items.COMPACT_OBSIDIAN, 1), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.COMPACT_OBSIDIAN, 1),
                    new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.NONE, 0)), 1, 24, SellMerchant.COMBAT, 25000, true),
    MINER_CHESTPLATE(Rarity.UNCOMMON, "Miner Chestplate", ItemTweaker.dye(new ItemBuilder(Material.LEATHER_CHESTPLATE, 0).asItem(), 53, 0, 68), 0, 0, 100, 0, 0, 125, "MINER_CHESTPLATE", ItemType.ARMOR, null,
            Arrays.asList(new CraftingIngrediant(Items.COMPACT_OBSIDIAN, 1), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.COMPACT_OBSIDIAN, 1),
                    new CraftingIngrediant(Items.COMPACT_OBSIDIAN, 1), new CraftingIngrediant(Items.COMPACT_STONE, 2), new CraftingIngrediant(Items.COMPACT_OBSIDIAN, 1),
                    new CraftingIngrediant(Items.COMPACT_OBSIDIAN, 1), new CraftingIngrediant(Items.COMPACT_OBSIDIAN, 1), new CraftingIngrediant(Items.COMPACT_OBSIDIAN, 1)), 1, 24, SellMerchant.COMBAT, 25000, true),
    MINER_LEGGINGS(Rarity.UNCOMMON, "Miner Leggings", ItemTweaker.dye(new ItemBuilder(Material.LEATHER_LEGGINGS, 0).asItem(), 53, 0, 68), 0, 0, 100, 0, 0, 125, "MINER_LEGGINGS", ItemType.ARMOR, null,
            Arrays.asList(new CraftingIngrediant(Items.COMPACT_OBSIDIAN, 1), new CraftingIngrediant(Items.COMPACT_DIAMOND, 1), new CraftingIngrediant(Items.COMPACT_OBSIDIAN, 1),
                    new CraftingIngrediant(Items.COMPACT_OBSIDIAN, 1), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.COMPACT_OBSIDIAN, 1),
                    new CraftingIngrediant(Items.COMPACT_OBSIDIAN, 1), new CraftingIngrediant(Items.COMPACT_DIAMOND, 1), new CraftingIngrediant(Items.COMPACT_OBSIDIAN, 1)), 1, 24, SellMerchant.COMBAT, 25000, true),
    MINER_BOOTS(Rarity.UNCOMMON, "Miner Boots", ItemTweaker.dye(new ItemBuilder(Material.LEATHER_BOOTS, 0).asItem(), 53, 0, 68), 0, 0, 100, 0, 0, 125, "MINER_BOOTS", ItemType.ARMOR, null,
            Arrays.asList(new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.NONE, 0),
                    new CraftingIngrediant(Items.COMPACT_OBSIDIAN, 1), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.COMPACT_OBSIDIAN, 1),
                    new CraftingIngrediant(Items.COMPACT_OBSIDIAN, 1), new CraftingIngrediant(Items.GEMSTONE_ORE, 2), new CraftingIngrediant(Items.COMPACT_OBSIDIAN, 1)), 1, 24, SellMerchant.COMBAT, 25000, true),
    WHEAT(Rarity.COMMON, "Wheat", new ItemBuilder(Material.WHEAT, 0).asItem(), 0, 0, 0, 0, 0, 0,
            "WHEAT", ItemType.MATERIAL, null, SellMerchant.FARMING, 1, false, false, true),
    CARROT(Rarity.COMMON, "Carrot", new ItemBuilder(Material.CARROT, 0).asItem(), 0, 0, 0, 0, 0, 0,
            "CARROT", ItemType.MATERIAL, null, SellMerchant.FARMING, 1, false, false, true),
    CANE(Rarity.COMMON, "Sugar Cane", new ItemBuilder(Material.SUGAR_CANE, 0).asItem(), 0, 0, 0, 0, 0, 0,
            "CANE", ItemType.MATERIAL, null, SellMerchant.FARMING, 1, false, false, true),
    POTATO(Rarity.COMMON, "Potato", new ItemBuilder(Material.POTATO, 0).asItem(), 0, 0, 0, 0, 0, 0,
            "POTATO", ItemType.MATERIAL, null, SellMerchant.FARMING, 1, false, false, true),
    SWEET_BERRIES(Rarity.COMMON, "Sweet Berries", new ItemBuilder(Material.SWEET_BERRIES, 0).asItem(), 0, 0,
            0, 0, 0, 0,
            "SWEET_BERRIES", ItemType.MATERIAL, null, SellMerchant.FARMING, 5, false, false, true),
    BEETROOT(Rarity.COMMON, "Beetroot", new ItemBuilder(Material.BEETROOT, 0).asItem(), 0, 0,
            0, 0, 0, 0,
            "BEETROOT", ItemType.MATERIAL, null, SellMerchant.FARMING, 1, false, false, true),
    PUMPKIN(Rarity.COMMON, "Pumpkin", new ItemBuilder(Material.PUMPKIN, 0).asItem(), 0, 0, 0, 0, 0, 0, "PUMPKIN",
            ItemType.MATERIAL, null, SellMerchant.FARMING, 5,  false, false, true),
    MELON(Rarity.COMMON, "Melon", new ItemBuilder(Material.MELON, 0).asItem(), 0, 0, 0, 0, 0, 0, "MELON",
            ItemType.MATERIAL, null, SellMerchant.FARMING, 5, false, false, true),
    RAW_CHICKEN(Rarity.COMMON, "Raw Chicken", new ItemBuilder(Material.CHICKEN, 0).asItem(),
            0, 0, 0, 0, 0, 0, "RAW_CHICKEN", ItemType.MATERIAL, null, SellMerchant.FARMING,5,false, true, false),
    RAW_BEEF(Rarity.COMMON, "Raw Beef", new ItemBuilder(Material.BEEF, 0).asItem(),
            0, 0, 0, 0, 0, 0, "RAW_BEEF", ItemType.MATERIAL, null, SellMerchant.FARMING,5,false, true, false),
    RAW_PORK(Rarity.COMMON, "Raw Pork", new ItemBuilder(Material.PORKCHOP, 0).asItem(),
            0, 0, 0, 0, 0, 0, "RAW_PORK", ItemType.MATERIAL, null, SellMerchant.FARMING,5,false, true, false),
    ROTTEN_FLESH(Rarity.COMMON, "Rotten Flesh", new ItemBuilder(Material.ROTTEN_FLESH, 0).asItem(),
            0, 0, 0, 0, 0, 0, "ROTTEN_FLESH", ItemType.MATERIAL, null, SellMerchant.COMBAT,5,false, true, false),
    BONE(Rarity.COMMON, "Bone", new ItemBuilder(Material.BONE, 0).asItem(),
            0, 0, 0, 0, 0, 0, "BONE", ItemType.MATERIAL, null, SellMerchant.COMBAT,5,false),
    STRING(Rarity.COMMON, "String", new ItemBuilder(Material.STRING, 0).asItem(),
            0, 0, 0, 0, 0, 0, "STRING", ItemType.MATERIAL, null, SellMerchant.COMBAT,5,false),
    /* ----- STRING COLLECTION ----- */
    COMPACT_STRING(Rarity.RARE, "Compact String", new ItemBuilder(Material.STRING, 0).asItem(), 0, 0, 0, 0, 0, 0, "COMPACT_STRING", ItemType.MATERIAL, null,
            Arrays.asList(
                    new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.STRING, 16), new CraftingIngrediant(Items.NONE, 0),
                    new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.STRING, 16), new CraftingIngrediant(Items.NONE, 0),
                    new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.STRING, 16), new CraftingIngrediant(Items.NONE, 0)), 1, 50, SellMerchant.COMBAT, 250, true),
    ZOMBIE_FRAGMENT(Rarity.RARE, "Zombie Fragment", new ItemBuilder(Material.ZOMBIE_HEAD, 0).asItem(),
            0,  0,0, 0, 0, 0, "ZOMBIE_FRAGMENT", ItemType.MATERIAL, null, SellMerchant.COMBAT, 5000, false),
    CRYSTALLITE(Rarity.UNCOMMON, "Crystalite", new ItemBuilder(Material.QUARTZ, 0).asItem(), 0, 0, 0, 0, 0, 0, "CRYSTALLITE", ItemType.MATERIAL,
            Arrays.asList(Text.color("&dCrystalite, &7&oa mysterious resource, has unknown origins."),
                    Text.color("&7&oAll we know is that the townspeople use it for their windows.")), SellMerchant.MINING, 100, true),
    /* ------ CRYSTALLITE COLLECTION ------ */
    COMPACT_CRYSTALLITE(Rarity.RARE, "Compact Crystallite", new ItemBuilder(Material.WHITE_STAINED_GLASS, 0).asItem(),
            0, 0, 0, 0, 0, 0, "COMPACT_CRYSTALLITE", ItemType.MATERIAL, null, Arrays.asList(
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.CRYSTALLITE, 16), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.CRYSTALLITE, 16), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.CRYSTALLITE, 16), new CraftingIngrediant(Items.NONE, 0)), 1, 50, SellMerchant.MINING, 250, true),
    NETHERRACK(Rarity.COMMON, "Netherrack", new ItemBuilder(Material.NETHERRACK, 0).asItem(), 0, 0, 0, 0, 0, 0, "NETHERRACK", ItemType.MATERIAL, null, SellMerchant.MINING, 10, false),
    END_STONE(Rarity.UNCOMMON, "End Stone", new ItemBuilder(Material.END_STONE, 0).asItem(), 0, 0, 0, 0, 0, 0, "END_STONE", ItemType.MATERIAL, null, SellMerchant.MINING, 120, false),
    CORRUPTED_ROOT(Rarity.RARE, "Corrupted Root", new ItemBuilder(Material.PURPLE_DYE, 0).asItem(),
            5, 0, 0, 0, 0, 0, "CORRUPTED_ROOT", ItemType.ITEM,
            Arrays.asList(Text.color("&7&oHow did we get here?")), SellMerchant.MINING, 150, true),
    /* ------ MERCHANT ITEMS ------*/
    STARTER_SWORD(Rarity.COMMON, "Starter Sword", new ItemBuilder(Material.GOLDEN_SWORD, 0).asItem(), 35, 5, 10, 0, 0, 0, "STARTER_SWORD", ItemType.WEAPON,
            Arrays.asList(Text.color("&8&l[&e&lABILITY&8&l] &r&e10 Energy"), Text.color("&7Gain a temporary &f+25 Speed")), SellMerchant.COMBAT, 10, false),
    ZOMBIE_SWORD(Rarity.UNCOMMON, "Zombie Sword", new ItemBuilder(Material.IRON_SWORD, 0).asItem(), 50, 0, 0, 0, 0, 0, "ZOMBIE_SWORD", ItemType.WEAPON,
            Arrays.asList(Text.color("&7Do &c+50% damage &7to all &cZombies.")), SellMerchant.COMBAT, 10, false),


    ARROW(Rarity.COMMON, "Arrow", new ItemBuilder(Material.ARROW, 0).asItem(), 2, 0, 0, 0, 0, 0, "ARROW", ItemType.ITEM, null, SellMerchant.COMBAT, 10, false),
    /* ------ CRAFTABLE ITEMS ------ */
    PLANK(Rarity.COMMON, "Plank", new ItemBuilder(Material.OAK_PLANKS, 0).asItem(), 0, 0, 0, 0, 0, 0, "PLANK",
            ItemType.MATERIAL, null, Arrays.asList(
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.WOOD, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.NONE, 0)) , 4, 2, SellMerchant.WOODCUTTING, 3, false),
    STICK(Rarity.COMMON, "Stick", new ItemBuilder(Material.STICK, 0).asItem(), 0, 0, 0, 0, 0, 0, "STICK", ItemType.MATERIAL,
            null, Arrays.asList(
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.PLANK, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.PLANK, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.NONE, 0)), 4, 3, SellMerchant.WOODCUTTING, 5, false),
    BASIC_HILT(Rarity.COMMON, "Basic Hilt", new ItemBuilder(Material.STICK, 0).asItem(), 0, 0, 0, 0, 0, 0, "BASIC_HILT", ItemType.ITEM,
            Arrays.asList(Text.color("&7Used to craft any &fCommon &7tool.")), Arrays.asList(new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.PLANK, 3), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.STICK, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.PLANK, 1), new CraftingIngrediant(Items.NONE, 0)), 1, 20, SellMerchant.COMBAT, 10, false),

    ADVANCED_HILT(Rarity.UNCOMMON, "Advanced Hilt", new ItemBuilder(Material.STICK, 0).asItem(), 0, 0, 0, 0, 0, 0, "ADVANCED_HILT", ItemType.ITEM,
            Arrays.asList(Text.color("&7Used to craft a &aUncommon &7tool.")), Arrays.asList(new CraftingIngrediant(Items.IRON_INGOT, 4), new CraftingIngrediant(Items.GOLD_INGOT, 1), new CraftingIngrediant(Items.IRON_INGOT, 4),
            new CraftingIngrediant(Items.IRON_INGOT, 4), new CraftingIngrediant(Items.BASIC_HILT, 1), new CraftingIngrediant(Items.IRON_INGOT, 4),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.PLANK, 4), new CraftingIngrediant(Items.NONE, 0)), 1, 40, SellMerchant.COMBAT, 20, true),
    EXPERTISE_HILT(Rarity.RARE, "Expertise Hilt", new ItemBuilder(Material.END_ROD, 0).asItem(), 0, 0, 0, 0, 0, 0, "EXPERTISE_HILT", ItemType.ITEM,
            Arrays.asList(Text.color("&7Used to craft a &1Rare &7tool.")),
            Arrays.asList(new CraftingIngrediant(Items.OBSIDIAN, 16), new CraftingIngrediant(Items.COMPACT_WOOD, 8), new CraftingIngrediant(Items.OBSIDIAN, 16),
            new CraftingIngrediant(Items.OBSIDIAN, 16), new CraftingIngrediant(Items.ADVANCED_HILT, 1), new CraftingIngrediant(Items.OBSIDIAN, 16),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.COMPACT_WOOD, 8), new CraftingIngrediant(Items.NONE, 0)), 1, 200, SellMerchant.COMBAT, 1000, false),
    LEGENDS_HILT(Rarity.EPIC, "Legends Hilt", new ItemBuilder(Material.BLAZE_ROD, 0).asItem(), 0, 0, 0, 0, 0, 0, "LEGENDS_HILT", ItemType.ITEM,
            Arrays.asList(Text.color("&7Used to craft a &5Epic &7tool.")),
            Arrays.asList(new CraftingIngrediant(Items.COMPACT_OBSIDIAN, 8), new CraftingIngrediant(Items.PREMIUM_GOLD_EXTRACT, 2), new CraftingIngrediant(Items.COMPACT_OBSIDIAN, 4),
                    new CraftingIngrediant(Items.COMPACT_OBSIDIAN, 8), new CraftingIngrediant(Items.EXPERTISE_HILT, 1), new CraftingIngrediant(Items.COMPACT_OBSIDIAN, 4),
                    new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.COMPACT_WOOD, 16), new CraftingIngrediant(Items.NONE, 0)), 1, 200, SellMerchant.COMBAT, 50000, false),
    MASTERS_HILT(Rarity.LEGENDARY, "Masters Hilt", new ItemBuilder(Material.BLAZE_ROD, 0).asItem(), 0, 0, 0, 0, 0, 0, "MASTERS_HILT", ItemType.ITEM,
            Arrays.asList(Text.color("&7Used to craft a &eLegendary &7tool.")),
            Arrays.asList(new CraftingIngrediant(Items.COMPACT_OBSIDIAN, 16), new CraftingIngrediant(Items.COMPACT_GEMSTONE, 1), new CraftingIngrediant(Items.COMPACT_OBSIDIAN, 16),
                    new CraftingIngrediant(Items.COMPACT_OBSIDIAN, 16), new CraftingIngrediant(Items.LEGENDS_HILT, 1), new CraftingIngrediant(Items.COMPACT_OBSIDIAN, 16),
                    new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.COMPACT_WOOD, 32), new CraftingIngrediant(Items.NONE, 0)), 1, 200, SellMerchant.COMBAT, 50000, false),
    /* ------ WOOD TOOLS ------ */
    BOW(Rarity.UNCOMMON, "Bow", new ItemBuilder(Material.BOW, 0).asItem(), 60, 0, 0, 0, 50, 0, "BOW", ItemType.BOW, null, Arrays.asList(
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.STICK, 1), new CraftingIngrediant(Items.STRING, 1),
            new CraftingIngrediant(Items.ADVANCED_HILT, 1), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.STRING, 1),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.STICK, 1), new CraftingIngrediant(Items.STRING, 1)), 1, 40, SellMerchant.COMBAT,  100, false),
    WOODEN_PICKAXE(Rarity.COMMON, "Wooden Pickaxe", new ItemBuilder(Material.WOODEN_PICKAXE, 0).asItem(), 2, 0, 0, 0, 0, 0, "WOODEN_PICKAXE", ItemType.TOOL,
            null, Arrays.asList(new CraftingIngrediant(Items.PLANK, 1), new CraftingIngrediant(Items.PLANK, 1), new CraftingIngrediant(Items.PLANK, 1),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.STICK, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.BASIC_HILT, 1), new CraftingIngrediant(Items.NONE, 0)), 1, 22, SellMerchant.MINING, 15, false),
    WOODEN_SWORD(Rarity.COMMON, "Wooden Sword", new ItemBuilder(Material.WOODEN_SWORD, 0).asItem(), 20, 0, 0, 0, 0, 0, "WOODEN_SWORD", ItemType.WEAPON,
            null, Arrays.asList(new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.PLANK, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.PLANK, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.BASIC_HILT, 1), new CraftingIngrediant(Items.NONE, 0)), 1, 22, SellMerchant.COMBAT, 15, false),
    WOODEN_HOE(Rarity.COMMON, "Wooden Hoe", new ItemBuilder(Material.WOODEN_HOE, 0).asItem(), 2, 0, 0, 0, 0, 0, "WOODEN_HOE", ItemType.TOOL,
            null, Arrays.asList(new CraftingIngrediant(Items.PLANK, 1), new CraftingIngrediant(Items.PLANK, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.STICK, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.BASIC_HILT, 1), new CraftingIngrediant(Items.NONE, 0)), 1, 22, SellMerchant.FARMING, 15, false),
    WOODEN_AXE(Rarity.COMMON, "Wooden Axe", new ItemBuilder(Material.WOODEN_AXE, 0).asItem(), 10, 0, 0, 0, 0, 0, "WOODEN_AXE", ItemType.TOOL, null, Arrays.asList(
            new CraftingIngrediant(Items.PLANK, 1), new CraftingIngrediant(Items.PLANK, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.PLANK, 1), new CraftingIngrediant(Items.STICK, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.BASIC_HILT, 1), new CraftingIngrediant(Items.NONE, 0)), 1, 22, SellMerchant.WOODCUTTING, 15, false),
    /* ----- STONE TOOLS ----- */
    STONE_PICKAXE(Rarity.COMMON, "Stone Pickaxe", new ItemBuilder(Material.STONE_PICKAXE, 0).asItem(), 4, 0, 0, 0, 0, 0, "STONE_PICKAXE", ItemType.TOOL,
            null, Arrays.asList(new CraftingIngrediant(Items.STONE, 1), new CraftingIngrediant(Items.STONE, 1), new CraftingIngrediant(Items.STONE, 1),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.STICK, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.BASIC_HILT, 1), new CraftingIngrediant(Items.NONE, 0)), 1, 24, SellMerchant.MINING, 15, false),
    STONE_SWORD(Rarity.COMMON, "Stone Sword", new ItemBuilder(Material.STONE_SWORD, 0).asItem(), 25, 0, 0, 0, 0, 0, "STONE_SWORD", ItemType.WEAPON,
            null, Arrays.asList(new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.STONE, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.STONE, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.BASIC_HILT, 1), new CraftingIngrediant(Items.NONE, 0)), 1, 24, SellMerchant.COMBAT, 15, false),
    STONE_HOE(Rarity.COMMON, "Stone Hoe", new ItemBuilder(Material.STONE_HOE, 0).asItem(), 4, 0, 0, 0, 0, 0, "STONE_HOE", ItemType.TOOL,
            null, Arrays.asList(new CraftingIngrediant(Items.STONE, 1), new CraftingIngrediant(Items.STONE, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.STICK, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.BASIC_HILT, 1), new CraftingIngrediant(Items.NONE, 0)), 1, 24, SellMerchant.FARMING, 15, false),
    STONE_AXE(Rarity.COMMON, "Stone Axe", new ItemBuilder(Material.STONE_AXE, 0).asItem(), 15, 5, 0, 0, 0, 0, "STONE_AXE", ItemType.TOOL, null, Arrays.asList(
            new CraftingIngrediant(Items.STONE, 1), new CraftingIngrediant(Items.STONE, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.STONE, 1), new CraftingIngrediant(Items.STICK, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.BASIC_HILT, 1), new CraftingIngrediant(Items.NONE, 0)), 1, 24, SellMerchant.WOODCUTTING, 15, false),
    /* ----- IRON TOOLS ----- */
    IRON_PICKAXE(Rarity.COMMON, "Iron Pickaxe", new ItemBuilder(Material.IRON_PICKAXE, 0).asItem(), 6, 0, 0, 0, 0, 0, "IRON_PICKAXE", ItemType.TOOL,
            null, Arrays.asList(new CraftingIngrediant(Items.IRON_INGOT, 1), new CraftingIngrediant(Items.IRON_INGOT, 1), new CraftingIngrediant(Items.IRON_INGOT, 1),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.STICK, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.BASIC_HILT, 1), new CraftingIngrediant(Items.NONE, 0)), 1, 24, SellMerchant.MINING, 20, false),
    IRON_SWORD(Rarity.COMMON, "Iron Sword", new ItemBuilder(Material.IRON_SWORD, 0).asItem(), 30, 0, 0, 0, 0, 0, "IRON_SWORD", ItemType.WEAPON,
            null, Arrays.asList(new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.IRON_INGOT, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.IRON_INGOT, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.BASIC_HILT, 1), new CraftingIngrediant(Items.NONE, 0)), 1, 24, SellMerchant.COMBAT, 20, false),
    IRON_HOE(Rarity.COMMON, "Iron Hoe", new ItemBuilder(Material.IRON_HOE, 0).asItem(), 6, 0, 0, 0, 0, 0, "IRON_HOE", ItemType.TOOL,
            null, Arrays.asList(new CraftingIngrediant(Items.IRON_INGOT, 1), new CraftingIngrediant(Items.IRON_INGOT, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.STICK, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.BASIC_HILT, 1), new CraftingIngrediant(Items.NONE, 0)), 1, 24, SellMerchant.FARMING, 20, false),
    IRON_AXE(Rarity.COMMON, "Iron Axe", new ItemBuilder(Material.IRON_AXE, 0).asItem(), 20, 10, 0, 0, 0, 0, "IRON_AXE", ItemType.TOOL, null, Arrays.asList(
            new CraftingIngrediant(Items.IRON_INGOT, 1), new CraftingIngrediant(Items.IRON_INGOT, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.IRON_INGOT, 1), new CraftingIngrediant(Items.STICK, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.BASIC_HILT, 1), new CraftingIngrediant(Items.NONE, 0)), 1, 24, SellMerchant.WOODCUTTING, 15, false),
    /* ----- GOLDEN TOOLS ----- */
    GOLDEN_PICKAXE(Rarity.RARE, "Golden Pickaxe", new ItemBuilder(Material.GOLDEN_PICKAXE, 0).asItem(), 1, 0, 0, 0, 0, 0, "GOLDEN_PICKAXE", ItemType.TOOL,
            null, Arrays.asList(new CraftingIngrediant(Items.GOLD_INGOT, 1), new CraftingIngrediant(Items.GOLD_INGOT, 1), new CraftingIngrediant(Items.GOLD_INGOT, 1),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.STICK, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.EXPERTISE_HILT, 1), new CraftingIngrediant(Items.NONE, 0)), 1, 26, SellMerchant.MINING, 25, false),
    GOLDEN_SWORD(Rarity.RARE, "Golden Sword", new ItemBuilder(Material.GOLDEN_SWORD, 0).asItem(), 20, 20, 0, 0, 0, 0, "GOLDEN_SWORD", ItemType.WEAPON,
            null, Arrays.asList(new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.GOLD_INGOT, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.GOLD_INGOT, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.EXPERTISE_HILT, 1), new CraftingIngrediant(Items.NONE, 0)), 1, 26, SellMerchant.COMBAT, 25, false),
    GOLDEN_HOE(Rarity.RARE, "Golden Hoe", new ItemBuilder(Material.GOLDEN_HOE, 0).asItem(), 4, 0, 0, 0, 0, 0, "GOLDEN_HOE", ItemType.TOOL,
            null, Arrays.asList(new CraftingIngrediant(Items.GOLD_INGOT, 1), new CraftingIngrediant(Items.GOLD_INGOT, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.STICK, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.EXPERTISE_HILT, 1), new CraftingIngrediant(Items.NONE, 0)), 1, 24, SellMerchant.FARMING, 25, false),
    GOLDEN_AXE(Rarity.RARE, "Golden Axe", new ItemBuilder(Material.GOLDEN_AXE, 0).asItem(), 4, 0, 0, 0, 0, 0, "GOLDEN_AXE", ItemType.TOOL, null, Arrays.asList(
            new CraftingIngrediant(Items.GOLD_INGOT, 1), new CraftingIngrediant(Items.GOLD_INGOT, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.GOLD_INGOT, 1), new CraftingIngrediant(Items.STICK, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.EXPERTISE_HILT, 1), new CraftingIngrediant(Items.NONE, 0)), 1, 26, SellMerchant.WOODCUTTING, 25, false),
    /* ----- DIAMOND TOOLS  -----*/
    DIAMOND_PICKAXE(Rarity.UNCOMMON, "Diamond Pickaxe", new ItemBuilder(Material.DIAMOND_PICKAXE, 0).asItem(), 10, 0, 0, 0, 0, 0, "DIAMOND_PICKAXE", ItemType.TOOL,
            null, Arrays.asList(new CraftingIngrediant(Items.DIAMOND, 1), new CraftingIngrediant(Items.DIAMOND, 1), new CraftingIngrediant(Items.DIAMOND, 1),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.STICK, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.ADVANCED_HILT, 1), new CraftingIngrediant(Items.NONE, 0)), 1, 30, SellMerchant.MINING, 30, false),
    DIAMOND_SWORD(Rarity.UNCOMMON, "Diamond Sword", new ItemBuilder(Material.DIAMOND_SWORD, 0).asItem(), 35, 0, 0, 0, 0, 0, "DIAMOND_SWORD", ItemType.WEAPON,
            null, Arrays.asList(new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.DIAMOND, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.DIAMOND, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.ADVANCED_HILT, 1), new CraftingIngrediant(Items.NONE, 0)), 1, 30, SellMerchant.COMBAT, 30, false),
    DIAMOND_HOE(Rarity.UNCOMMON, "Diamond Hoe", new ItemBuilder(Material.DIAMOND_HOE, 0).asItem(), 10, 0, 0, 0,  0,0, "DIAMOND_HOE", ItemType.TOOL,
            null, Arrays.asList(new CraftingIngrediant(Items.DIAMOND, 1), new CraftingIngrediant(Items.DIAMOND, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.STICK, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.ADVANCED_HILT, 1), new CraftingIngrediant(Items.NONE, 0)), 1, 30, SellMerchant.FARMING, 30, false),
    DIAMOND_AXE(Rarity.UNCOMMON, "Diamond Axe", new ItemBuilder(Material.DIAMOND_AXE, 0).asItem(), 30, 10, 10, 0, 0, 0, "DIAMOND_AXE", ItemType.TOOL, null, Arrays.asList(
            new CraftingIngrediant(Items.DIAMOND, 1), new CraftingIngrediant(Items.DIAMOND, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.DIAMOND, 1), new CraftingIngrediant(Items.STICK, 1), new CraftingIngrediant(Items.NONE, 0),
            new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.ADVANCED_HILT, 1), new CraftingIngrediant(Items.NONE, 0)), 1, 30, SellMerchant.WOODCUTTING, 30, false),
    /* ----- IRON ARMOR ------ */
    IRON_HELMET(Rarity.COMMON, "Iron Helmet", new ItemBuilder(Material.IRON_HELMET, 0).asItem(), 0, 0, 20, 0, 0, 0, "IRON_HELMET", ItemType.ARMOR, null,
            Arrays.asList(new CraftingIngrediant(Items.IRON_INGOT, 1), new CraftingIngrediant(Items.IRON_INGOT, 1), new CraftingIngrediant(Items.IRON_INGOT, 1),
                    new CraftingIngrediant(Items.IRON_INGOT, 1), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.IRON_INGOT, 1),
                    new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.NONE, 0)), 1, 24, SellMerchant.COMBAT,20, false),
    IRON_CHESTPLATE(Rarity.COMMON, "Iron Chestplate", new ItemBuilder(Material.IRON_CHESTPLATE, 0).asItem(), 0, 0, 20, 0, 0, 0, "IRON_CHESTPLATE", ItemType.ARMOR, null,
            Arrays.asList(new CraftingIngrediant(Items.IRON_INGOT, 1), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.IRON_INGOT, 1),
                    new CraftingIngrediant(Items.IRON_INGOT, 1), new CraftingIngrediant(Items.IRON_INGOT, 1), new CraftingIngrediant(Items.IRON_INGOT, 1),
                    new CraftingIngrediant(Items.IRON_INGOT, 1), new CraftingIngrediant(Items.IRON_INGOT, 1), new CraftingIngrediant(Items.IRON_INGOT, 1)), 1, 24, SellMerchant.COMBAT, 20, false),
    IRON_LEGGINGS(Rarity.COMMON, "Iron Leggings", new ItemBuilder(Material.IRON_LEGGINGS, 0).asItem(), 0, 0, 20, 0, 0, 0, "IRON_LEGGINGS", ItemType.ARMOR, null,
            Arrays.asList(new CraftingIngrediant(Items.IRON_INGOT, 1), new CraftingIngrediant(Items.IRON_INGOT, 1), new CraftingIngrediant(Items.IRON_INGOT, 1),
                    new CraftingIngrediant(Items.IRON_INGOT, 1), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.IRON_INGOT, 1),
                    new CraftingIngrediant(Items.IRON_INGOT, 1), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.IRON_INGOT, 1)), 1, 24, SellMerchant.COMBAT, 20, false),
    IRON_BOOTS(Rarity.COMMON, "Iron Boots", new ItemBuilder(Material.IRON_BOOTS, 0).asItem(), 0, 0, 20, 0, 0, 0, "IRON_BOOTS", ItemType.ARMOR, null,
            Arrays.asList(new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.NONE, 0),
                    new CraftingIngrediant(Items.IRON_INGOT, 1), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.IRON_INGOT, 1),
                    new CraftingIngrediant(Items.IRON_INGOT, 1), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.IRON_INGOT, 1)), 1, 24, SellMerchant.COMBAT, 20, false),
    /* ----- DIAMOND ARMOR ----- */
    DIAMOND_HELMET(Rarity.UNCOMMON, "Diamond Helmet", new ItemBuilder(Material.DIAMOND_HELMET, 0).asItem(), 0, 0, 40, 0, 0, 10, "DIAMOND_HELMET", ItemType.ARMOR, null,
            Arrays.asList(new CraftingIngrediant(Items.DIAMOND, 1), new CraftingIngrediant(Items.DIAMOND, 1), new CraftingIngrediant(Items.DIAMOND, 1),
                    new CraftingIngrediant(Items.DIAMOND, 1), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.DIAMOND, 1),
                    new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.NONE, 0)), 1, 30, SellMerchant.COMBAT, 20, false),
    DIAMOND_CHESTPLATE(Rarity.UNCOMMON, "Diamond Chestplate", new ItemBuilder(Material.DIAMOND_CHESTPLATE, 0).asItem(), 0, 0, 40, 0, 0, 10, "DIAMOND_CHESTPLATE", ItemType.ARMOR, null,
            Arrays.asList(new CraftingIngrediant(Items.DIAMOND, 1), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.DIAMOND, 1),
                    new CraftingIngrediant(Items.DIAMOND, 1), new CraftingIngrediant(Items.DIAMOND, 1), new CraftingIngrediant(Items.DIAMOND, 1),
                    new CraftingIngrediant(Items.DIAMOND, 1), new CraftingIngrediant(Items.DIAMOND, 1), new CraftingIngrediant(Items.DIAMOND, 1)), 1, 30, SellMerchant.COMBAT, 20, false),
    DIAMOND_LEGGINGS(Rarity.UNCOMMON, "Diamond Leggings", new ItemBuilder(Material.DIAMOND_LEGGINGS, 0).asItem(), 0, 0, 40, 0, 0, 10, "DIAMOND_LEGGINGS", ItemType.ARMOR, null,
            Arrays.asList(new CraftingIngrediant(Items.DIAMOND, 1), new CraftingIngrediant(Items.DIAMOND, 1), new CraftingIngrediant(Items.DIAMOND, 1),
                    new CraftingIngrediant(Items.DIAMOND, 1), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.DIAMOND, 1),
                    new CraftingIngrediant(Items.DIAMOND, 1), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.DIAMOND, 1)), 1, 30, SellMerchant.COMBAT, 20, false),
    DIAMOND_BOOTS(Rarity.UNCOMMON, "Diamond Boots", new ItemBuilder(Material.DIAMOND_BOOTS, 0).asItem(), 0, 0, 40, 0, 0, 10, "DIAMOND_BOOTS", ItemType.ARMOR, null,
            Arrays.asList(new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.NONE, 0),
                    new CraftingIngrediant(Items.DIAMOND, 1), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.DIAMOND, 1),
                    new CraftingIngrediant(Items.DIAMOND, 1), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.DIAMOND, 1)), 1, 30, SellMerchant.COMBAT, 20, false),
    /* ----- ZOMBIE ARMOR ----- */
    ZOMBIE_HELMET(Rarity.RARE, "Zombie Helmet", new ItemBuilder(Material.ZOMBIE_HEAD, 0).asItem(), 0, 0, 60, 0, 0, 100, "ZOMBIE_HELMET", ItemType.ARMOR, null,
            Arrays.asList(new CraftingIngrediant(Items.ZOMBIE_FRAGMENT, 1), new CraftingIngrediant(Items.ZOMBIE_FRAGMENT, 1), new CraftingIngrediant(Items.ZOMBIE_FRAGMENT, 1),
                    new CraftingIngrediant(Items.ZOMBIE_FRAGMENT, 1), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.ZOMBIE_FRAGMENT, 1),
                    new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.NONE, 0)), 1, 30, SellMerchant.COMBAT, 25000, false),
    ZOMBIE_CHESTPLATE(Rarity.RARE, "Zombie Chestplate", ItemTweaker.dye(new ItemBuilder(Material.LEATHER_CHESTPLATE, 0).asItem(), 0, 153, 51), 0, 0, 60, 0, 0, 100, "ZOMBIE_CHESTPLATE", ItemType.ARMOR, null,
            Arrays.asList(new CraftingIngrediant(Items.ZOMBIE_FRAGMENT, 1), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.ZOMBIE_FRAGMENT, 1),
                    new CraftingIngrediant(Items.ZOMBIE_FRAGMENT, 1), new CraftingIngrediant(Items.ZOMBIE_FRAGMENT, 1), new CraftingIngrediant(Items.ZOMBIE_FRAGMENT, 1),
                    new CraftingIngrediant(Items.ZOMBIE_FRAGMENT, 1), new CraftingIngrediant(Items.ZOMBIE_FRAGMENT, 1), new CraftingIngrediant(Items.ZOMBIE_FRAGMENT, 1)), 1, 30, SellMerchant.COMBAT, 25000, false),
    ZOMBIE_LEGGINGS(Rarity.RARE, "Zombie Leggings", ItemTweaker.dye(new ItemBuilder(Material.LEATHER_LEGGINGS, 0).asItem(), 0, 153, 51), 0, 0, 60, 0, 0, 100, "ZOMBIE_LEGGINGS", ItemType.ARMOR, null,
            Arrays.asList(new CraftingIngrediant(Items.ZOMBIE_FRAGMENT, 1), new CraftingIngrediant(Items.ZOMBIE_FRAGMENT, 1), new CraftingIngrediant(Items.ZOMBIE_FRAGMENT, 1),
                    new CraftingIngrediant(Items.ZOMBIE_FRAGMENT, 1), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.ZOMBIE_FRAGMENT, 1),
                    new CraftingIngrediant(Items.ZOMBIE_FRAGMENT, 1), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.ZOMBIE_FRAGMENT, 1)), 1, 30, SellMerchant.COMBAT, 25000, false),
    ZOMBIE_BOOTS(Rarity.RARE, "Zombie Boots", ItemTweaker.dye(new ItemBuilder(Material.LEATHER_BOOTS, 0).asItem(), 0, 153, 51), 0, 0, 60, 0,50, 100, "ZOMBIE_BOOTS", ItemType.ARMOR, null,
            Arrays.asList(new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.NONE, 0),
                    new CraftingIngrediant(Items.ZOMBIE_FRAGMENT, 1), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.ZOMBIE_FRAGMENT, 1),
                    new CraftingIngrediant(Items.ZOMBIE_FRAGMENT, 1), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.ZOMBIE_FRAGMENT, 1)), 1, 30, SellMerchant.COMBAT, 25000, false),
    /* ----- GOLD ARMOR ----- */
    GOLD_HELMET(Rarity.UNCOMMON, "Gold Helmet", new ItemBuilder(Material.GOLDEN_HELMET, 0).asItem(), 0, 0, 30, 0, 0, 0, "GOLDEN_HELMET", ItemType.ARMOR, null,
            Arrays.asList(new CraftingIngrediant(Items.GOLD_INGOT, 1), new CraftingIngrediant(Items.GOLD_INGOT, 1), new CraftingIngrediant(Items.GOLD_INGOT, 1),
                    new CraftingIngrediant(Items.GOLD_INGOT, 1), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.GOLD_INGOT, 1),
                    new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.NONE, 0)), 1, 30, SellMerchant.COMBAT, 20, false),
    GOLD_CHESTPLATE(Rarity.UNCOMMON, "Gold Chestplate", new ItemBuilder(Material.GOLDEN_CHESTPLATE, 0).asItem(), 0, 0, 30, 0,0, 0, "GOLDEN_CHESTPLATE", ItemType.ARMOR, null,
            Arrays.asList(new CraftingIngrediant(Items.GOLD_INGOT, 1), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.GOLD_INGOT, 1),
                    new CraftingIngrediant(Items.GOLD_INGOT, 1), new CraftingIngrediant(Items.GOLD_INGOT, 1), new CraftingIngrediant(Items.GOLD_INGOT, 1),
                    new CraftingIngrediant(Items.GOLD_INGOT, 1), new CraftingIngrediant(Items.GOLD_INGOT, 1), new CraftingIngrediant(Items.GOLD_INGOT, 1)), 1, 30, SellMerchant.COMBAT, 20, false),
    GOLD_LEGGINGS(Rarity.UNCOMMON, "Gold Leggings", new ItemBuilder(Material.GOLDEN_LEGGINGS, 0).asItem(), 0, 0, 30, 0, 0, 0, "GOLDEN_LEGGINGS", ItemType.ARMOR, null,
            Arrays.asList(new CraftingIngrediant(Items.GOLD_INGOT, 1), new CraftingIngrediant(Items.GOLD_INGOT, 1), new CraftingIngrediant(Items.GOLD_INGOT, 1),
                    new CraftingIngrediant(Items.GOLD_INGOT, 1), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.GOLD_INGOT, 1),
                    new CraftingIngrediant(Items.GOLD_INGOT, 1), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.GOLD_INGOT, 1)), 1, 30, SellMerchant.COMBAT, 20, false),
    GOLD_BOOTS(Rarity.UNCOMMON, "Gold Boots", new ItemBuilder(Material.GOLDEN_BOOTS, 0).asItem(), 0, 0, 30, 0, 25, 0, "GOLDEN_BOOTS", ItemType.ARMOR, null,
            Arrays.asList(new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.NONE, 0),
                    new CraftingIngrediant(Items.GOLD_INGOT, 1), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.GOLD_INGOT, 1),
                    new CraftingIngrediant(Items.GOLD_INGOT, 1), new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.GOLD_INGOT, 1)), 1, 30, SellMerchant.COMBAT, 20, false),
    /* ----- CRAB ITEMS ----- */
    CRAB_FRAGMENT(Rarity.UNCOMMON, "Crab Fragment", new ItemBuilder(Material.RED_DYE, 0).asItem(), 0, 0, 0, 0, 0, 0, "CRAB_FRAGMENT", ItemType.ITEM, Arrays.asList(Text.color("&7&oOne of the &eBeachmaster's &7&ofavorite things."), Text.color("&7&oA remmnant of one of the &cCrab King's &7&ominions.")), SellMerchant.COMBAT, 5000, true),
    CRAB_SCROLL(Rarity.EPIC, "Crab Scroll", new ItemBuilder(Material.PAPER, 0).asItem(), 0, 0, 0, 0, 0, 0, "CRAB_SCROLL", ItemType.ITEM, Arrays.asList(Text.color("&8&l[&e&lABILITY&8&l] &r&cSummon"), Text.color("&7Summon the &cCrab King&7.")), SellMerchant.COMBAT, 60000, true),
    CRAB_CLAW(Rarity.EPIC, "Crab Claw", new ItemBuilder(Material.MUTTON, 0).asItem(), 150, 75, 0, 0, 0, 0, "CRAB_CLAW", ItemType.WEAPON, Arrays.asList(Text.color("&7&oThe weapon of the now defeated &cCrab King&7&o.")), SellMerchant.COMBAT, 70000, true),
    CRAB_CROWN(Rarity.EPIC, "Crab Crown", ItemTweaker.dye(new ItemBuilder(Material.LEATHER_HELMET, 0).asItem(), 99, 0, 0), 0, 50, 50, 100, 0, 50, "CRAB_CROWN", ItemType.ARMOR, Arrays.asList(Text.color("&7&oThe crown of the now dethroned &cCrab King&7&o."), Text.color(" "), Text.color("&7Do &c+200%&7 more damage, but your health is set to &c75♥&7.")), SellMerchant.COMBAT, 70000, true),
    /* ----- SPIDER ITEMS ----- */
    VALISSA_ARACHNE_FRAGMENT(Rarity.RARE, "Valissa Arachne Fragment", new ItemBuilder(Material.SPIDER_EYE, 0).asItem(), 0, 0, 0, 0, 0, 0, "VALISSA_ARACHNE_FRAGMENT", ItemType.ITEM, Arrays.asList(Text.color("&7Used to craft &cValissa Arachne's Crystal&7.")), SellMerchant.COMBAT, 5000, true),
    VALISSA_ARACHNE_CRYSTAL(Rarity.LEGENDARY, "Valissa Arachne Crystal", ItemTweaker.createPlayerHeadFromData("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjI0MDcwYzliNjY1OWVkMjViMmNhMTI2OTE1ZjRkODgyMGZhZmNlNDMyNGVkOWE4ZjRiOGE1MDYzNDUzMDdmIn19fQ==",
            "d49c2625-cb64-4728-9acd-860bc0ef4272"), 0, 0, 0, 0, 0, 0, "VALISSA_ARACHNE_CRYSTAL", ItemType.ITEM, Arrays.asList(Text.color("&8&l[&e&lABILITY&8&l] &r&cSummon"), Text.color("&7Summon &cValissa Arachne&7.")),
            Arrays.asList(new CraftingIngrediant(Items.COMPACT_CRYSTALLITE, 4), new CraftingIngrediant(Items.VALISSA_ARACHNE_FRAGMENT, 1), new CraftingIngrediant(Items.COMPACT_CRYSTALLITE, 4),
                    new CraftingIngrediant(Items.VALISSA_ARACHNE_FRAGMENT, 1), new CraftingIngrediant(Items.COMPACT_STRING, 8), new CraftingIngrediant(Items.VALISSA_ARACHNE_FRAGMENT, 1),
                    new CraftingIngrediant(Items.COMPACT_CRYSTALLITE, 4), new CraftingIngrediant(Items.VALISSA_ARACHNE_FRAGMENT, 1), new CraftingIngrediant(Items.COMPACT_CRYSTALLITE, 4)), 1, 1000, SellMerchant.COMBAT, 60000, true),
    PREMIUM_SILK(Rarity.UNCOMMON, "Premium Silk", new ItemBuilder(Material.COBWEB, 0).asItem(), 0, 0, 0, 0, 0, 0, "PREMIUM_SILK", ItemType.ITEM,
            Arrays.asList(Text.color("&7A remmnant of &cValissa Arachne&7,"), Text.color("&7and her beautiful silk...")), SellMerchant.COMBAT, 500, true),
    SPIDER_BOW(Rarity.EPIC, "Spider Bow", new ItemBuilder(Material.BOW, 0).asItem(), 120, 50, 0, 0, 0, 0, "SPIDER_BOW", ItemType.BOW, Arrays.asList(Text.color("&7A weapon of a long defeated warrior,"), Text.color("&cValissa Arachne &7took his bow, and made it her prized possession.")), SellMerchant.COMBAT, 60000, false),
    SPIDER_BOOTS(Rarity.EPIC, "Spider Boots", ItemTweaker.dye(new ItemBuilder(Material.LEATHER_BOOTS, 0).asItem(), 255, 255, 255), 50, 50, 50, 50, 0, 100, "SPIDER_BOOTS", ItemType.ARMOR, Arrays.asList(Text.color("&7The boots of a long lost warrior, made from the silk"), Text.color("&7of long forgotten spiders."), " ", Text.color("&7Allows you to &cdouble jump &7in the air.")), SellMerchant.COMBAT, 50, false),
    /* ---- MISC ---- */
    NATURE_STAFF(Rarity.COMMON, "Nature Staff", new ItemBuilder(Material.STICK, 0).asItem(), 100, 0, 0, 50, 0, 50, "NATURE_STAFF", ItemType.WEAPON, null,
            Arrays.asList(new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.COMPACT_WOOD, 1), new CraftingIngrediant(Items.NONE, 0),
                    new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.WOOD, 64), new CraftingIngrediant(Items.NONE, 0),
                    new CraftingIngrediant(Items.NONE, 0), new CraftingIngrediant(Items.ADVANCED_HILT, 0), new CraftingIngrediant(Items.NONE, 0)), 1, 250,
            SellMerchant.COMBAT, 10000, true),
    /* ------ RUNIC STONES ------ */
    ENCRESTED_OBSIDIAN(Rarity.COMMON, "Encrested Obsidian", ItemTweaker.createPlayerHeadFromData("ewogICJ0aW1lc3RhbXAiIDogMTYxMzQ2NDYyMTUxNSwKICAicHJvZmlsZUlkIiA6ICJmZDYwZjM2ZjU4NjE0ZjEyYjNjZDQ3YzJkODU1Mjk5YSIsCiAgInByb2ZpbGVOYW1lIiA6ICJSZWFkIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2ZhNGY3MDI3YTkwYmNjYmZhNDRiZDczNjU4NzU2NGE0NzQxOGM1OTU2OTA4NWQ4Y2I2ZWJiMzVhNzc4OGExNDEiLAogICAgICAibWV0YWRhdGEiIDogewogICAgICAgICJtb2RlbCIgOiAic2xpbSIKICAgICAgfQogICAgfQogIH0KfQ==",
            "PdYxQ5vK2mtEc6duWFpKQerX5l11WBEhSbZYbq2x+sMBF3OdH5osW+JkUvmDxGHUp4PsEtaNfLyG9MLdDOZIzPPhYT8vt2kZt5ZjTBJnPUWaJxCM/3eTtNKd2AIyxQu/cNItGp0OmJo4ln82nLjcz2GFbad6JXZjUnwIXSdYhE5VuzYa1BHpDQCy9in+KDCr/tbfXiakJV0MqYzAHdLNYsrxOUykrw7z3vOD1U10gY7SQcWzNHprhx+zlzHM9KWhplcPs/uT7rl3lLk03gO4s9JPYLmiAfjVvStxA6oo32spHGNK8t5mwHJMqME9WBTfXq/XRzwy0cEtlL+wif5C/U5dILwIdpMkseqlmCc1uvDSsG2fd0UZ6FFaUx+jr3kg7zEgKfebkuA3aeagCXIWxCqXNO2ygqleFuE1tLe9R/mFMg0LJXJbMhFDN6KUGm7ZDLLiesiUb04/+8+z05I7lX1u3+aACKmapBwf/GkmsnnuKAhgPw5LC4xZCt7CdWLcZIVlsP1CIvd1iALt0ev9LCumWmfoN8v1mXzD+MtYzq0tU0Y53r8K1QRS1KA+S1eeQIJKf1AzLo8w/ujki9Y6BUfXwkCYZri8HEMmFKl9mQEej4bx7bP8/OFd+1A73BZbppsM+fQQ1fo/nwXmtozkDC1aKXw7YWAskbE+y+e/oMY=")
            , 0, 0, 0, 0, 0, 0, "ENCRESTED_OBSIDIAN", ItemType.RUNIC_STONE, Arrays.asList(Text.color("&7Apply the &5Encrested &7Runic Stone to your item.")), SellMerchant.COMBAT, 5, true),
    SWEET_CANE(Rarity.COMMON, "Sweet Cane", ItemTweaker.createPlayerHeadFromData("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzNjYTlkOGIxZDVmMmMyNjU0NzY0NDAzNGE1NWE0YTI0NjNlMTY2ZjYwZmViMGM3YzVhZjNmNzI0YmFlIn19fQ==",
            "27469789-1229-45e5-bbef-e7bb290983ad")
            , 0, 0, 0, 0, 0, 0, "SWEET_CANE", ItemType.RUNIC_STONE, Arrays.asList(Text.color("&7Apply the &5Speedy &7Runic Stone to your item.")), SellMerchant.COMBAT, 7, true),
    CRAB_SCALE(Rarity.COMMON, "Crab Scale", new ItemBuilder(Material.RED_CARPET, 0).asItem(), 0, 0, 0, 0, 0, 0, "CRAB_SCALE", ItemType.RUNIC_STONE, Arrays.asList("&7Apply the &5Ferocious &7Runic Stone to your item."), SellMerchant.COMBAT, 50, true),
    REDSTONE_CIRCUIT(Rarity.COMMON, "Redstone Circuit", ItemTweaker.createPlayerHeadFromData("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjA3YTdjNzE1NGY1NDUzZTRhYzA0MDgzOGYyM2E3NTNiMjk0MzEyZGViZDgzYmE2OGUxMjI0MGNjZmZlYzZhIn19fQ==",
            "56f88d31-bbf3-497a-923c-2b4c5c29e4d6"), 0, 0, 0, 0, 0, 0, "REDSTONE_CIRCUIT", ItemType.RUNIC_STONE, Arrays.asList(Text.color("&7Apply the &5Circuited &7Runic Stone to your item.")), SellMerchant.COMBAT, 7, true),
    /* ------ FOODS ------- */
    /* --- Food Bases --- */
    LOW_QUALITY_FRUIT(Rarity.COMMON, "Low Quality Fruit", new ItemBuilder(Material.APPLE, 0).asItem(), 0, 0, 0, 0, 0, 0, "LOW_QUALITY_FRUIT",
            ItemType.FOOD_BASE, null, SellMerchant.FARMING, 1, false),
    /* --- Foods --- */
    BERRYFRUIT(Rarity.COMMON, "Berryfruit", ItemTweaker.createPlayerHeadFromData("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmVlOTc2NDliZDk5OTk1NTQxM2ZjYmYwYjI2OWM5MWJlNDM0MmIxMGQwNzU1YmFkN2ExN2U5NWZjZWZkYWIwIn19fQ==",
            "2c382f99-71f3-4461-9861-4a9a361b8474"), 0, 0, 0, 0, 0, 0, "BERRYFRUIT", ItemType.FOOD, Arrays.asList(Text.color("&7Grants &cRegeneration 1 &7for &a30 &7seconds when used.")), SellMerchant.FARMING, 7, false),
    STRENGTH_COLA(Rarity.COMMON, "Strength Cola", ItemTweaker.createPlayerHeadFromData("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTNiMDFmYjJmNmJhNDdjOWQ3NjM4NDkxZjM3Y2Q4NTgyYTkzNzczMTE4NmRmNGQxZWNjZDU5YjY1YmYzNyJ9fX0=",
            "8a145e5e-957d-418c-b000-511c971ae698"), 0, 0, 0, 0, 0, 0, "STRENGTH_COLA", ItemType.FOOD, Arrays.asList(Text.color("&7Grants &4Strength 2 &7for &a30 &7seconds when used.")), SellMerchant.COMBAT, 1, false),
    OVERLY_CAFFINATED_COFFEE(Rarity.COMMON, "Overly-Caffeinated Coffee", ItemTweaker.createPlayerHeadFromData("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGYxMzIzYzlmZWYyMDY5Y2I0NWE0OTE0M2IzZjI1ZDg4NjRhYTY0YTQ5OTgxOWIxZjQyZDkzMzY3YTU5MzNlIn19fQ==",
            "daa163ef-1f93-471f-995f-6c63d04fcea8"), 0, 0, 0, 0, 0, 0, "OVERLY_CAFFINATED_COFFEE", ItemType.FOOD, Arrays.asList(Text.color("&7Grants &fSpeed 2 &7for &a30 &7seconds when used.")), SellMerchant.COMBAT, 1, false),
    SUPER_CANDY(Rarity.COMMON, "Super Candy", ItemTweaker.createPlayerHeadFromData("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGNjM2Y3ODFjOTIzYTI4ODdmMTRjMWVlYTExMDUwMTY2OTY2ZjI2MDI1Nzg0MDFmMTQ1MWU2MDk3Yjk3OWRmIn19fQ==",
            "201bdf0f-79ec-444f-a5ec-1a855fcddaf7"), 0, 0, 0, 0, 0, 0, "SUPER_CANDY", ItemType.FOOD, Arrays.asList(Text.color("&7Grants &eEnergetic 2 &7for &a30 &7seconds when used.")), SellMerchant.COMBAT, 1, false),
    LEMON_TEA(Rarity.COMMON, "Lemon Tea", ItemTweaker.createPlayerHeadFromData("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDhlOTRkZGQ3NjlhNWJlYTc0ODM3NmI0ZWM3MzgzZmQzNmQyNjc4OTRkN2MzYmVlMDExZThlNGY1ZmNkNyJ9fX0=",
            "2a3545d4-3ea5-4d26-9033-c280476696e0"), 0, 0, 0, 0, 0, 0, "LEMON_TEA", ItemType.FOOD, Arrays.asList(Text.color("&7Grants &aPlated 2 &7for &a30 &7seconds when used.")), SellMerchant.COMBAT, 1, false);
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
    private SellMerchant sellMerchant;
    private boolean cookable;
    private boolean processable;
    Items(Rarity rarity, String name, ItemStack itemStack, int damage, int strength, int defence, int energy, int speed,
          int hp, String id, ItemType itemType, List<String> lore,
          SellMerchant sellingPlace, int sellPrice, boolean glows, boolean cookable, boolean processable) {

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
        this.sellMerchant = sellingPlace;
        this.cookable = cookable;
        this.processable = processable;
    }
    Items(Rarity rarity, String name, ItemStack itemStack, int damage, int strength, int defence, int energy, int speed,
          int hp, String id, ItemType itemType, List<String> lore, List<CraftingIngrediant> craftingRecipe, int craftingAmount, int craftingXPGained,
          SellMerchant sellingPlace, int sellPrice, boolean glows) {

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
        this.sellMerchant = sellingPlace;
        this.cookable = false;
        this.processable = false;
    }
    Items(Rarity rarity, String name, ItemStack itemStack, int damage, int strength, int defence, int energy, int hp, int speed,
          String id, ItemType itemType, List<String> lore, SellMerchant sellingPlace, int sellPrice, boolean glows) {
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
        this.sellMerchant = sellingPlace;
        this.cookable = false;
        this.processable = false;
    }

    public boolean isCookable() { return cookable; }
    public boolean isProcessable() { return processable; }
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
    public ItemInfo generateItemInfo() { return new ItemInfo(id, rarity, new ArrayList<>(), 0, 0, 0, 0, 0, 0,
            null, null, false, false); }
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
            lore.add(Text.color("&7Damage: &c+" + items.getDamage() + " &8(+ 0)"));
        }
        if (items.getStrength() != 0) {
            lore.add(Text.color("&7Strength: &4+" + items.getStrength() + " &8(+ 0)"));
        }

        if (items.getEnergy() != 0) {
            lore.add(" ");
            lore.add(Text.color("&7Energy: &e+" + items.getEnergy() + " &8(+ 0)"));
        }
        if (items.getDefence() != 0) {
            lore.add(Text.color("&7Defence: &a+" + items.getDefence() + " &8(+ 0)"));
        }
        if (items.getHp() != 0) {
            lore.add(Text.color("&7Health: &c+" + items.getHp() + " &8(+ 0)"));
        }
        if (items.getSpeed() != 0) {
            lore.add(" ");
            lore.add(Text.color("&7Speed: &f+" + items.getSpeed() + " &8(+ 0)"));
        }
        if (items.getLore() != null) {
            lore.add(" ");
            for (int i = 0; i < items.getLore().size(); i++) {
                lore.add(Text.color(items.getLore().get(i)));
            }
        }

        lore.add(" ");
        lore.add(" ");
        lore.add(Text.color(items.getRarity().getNameColored() + items.getItemType().getName()));
        ItemMeta iM = itemStack.getItemMeta();
        iM.setDisplayName(Text.color(items.getRarity().getColor() + items.getName()));
        iM.setLore(lore);
        if (items.doesGlow()) {
            iM.addEnchant(org.bukkit.enchantments.Enchantment.DAMAGE_ALL, 1, true);
        }
        iM.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        iM.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        iM.setUnbreakable(true);
        iM.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemStack.setItemMeta(iM);
        ItemStack item = itemStack;
        net.minecraft.server.v1_16_R3.ItemStack nbtStack = CraftItemStack.asNMSCopy(item);
        NBTTagCompound nbtTagCompound = (nbtStack.hasTag()) ? nbtStack.getTag() : new NBTTagCompound();
        nbtTagCompound.setBoolean("rpgItem",
                true);
        nbtTagCompound.setString("enchantmentsRpg", "NONE");
        nbtTagCompound.setString("runicStoneRpg", "NONE");
        nbtTagCompound.setString("itemIdRpg", items.getId());
        nbtTagCompound.setString("rarityRpg", items.getRarity().toString());

        item = CraftItemStack.asBukkitCopy(nbtStack);
        return item;
    }
    public static void updateItem(ItemStack itemStack, ItemInfo itemInfo) {
        Items items = itemInfo.getItem();

        ArrayList<String> lore = new ArrayList<>();
        if (items.getDamage() != 0 || itemInfo.getBonusDamage() != 0) {
            lore.add(Text.color("&7Damage: &c+" + (items.getDamage() + itemInfo.getBonusDamage()) + " &8(+ " + itemInfo.getBonusDamage() + ")"));
        }
        if (items.getStrength() != 0 || itemInfo.getBonusStrength() != 0) {
            lore.add(Text.color("&7Strength: &4+" + (items.getStrength() + itemInfo.getBonusStrength())  + " &8(+ " + itemInfo.getBonusStrength() + ")"));
        }

        if (items.getEnergy() != 0 || itemInfo.getBonusEnergy() != 0) {
            lore.add(" ");
            lore.add(Text.color("&7Energy: &e+" + (items.getEnergy() + itemInfo.getBonusEnergy())  + " &8(+ " + itemInfo.getBonusEnergy() + ")"));
        }
        if (items.getDefence() != 0 || itemInfo.getBonusDefence() != 0) {
            lore.add(Text.color("&7Defence: &a+" + (items.getDefence() + itemInfo.getBonusDefence())  + " &8(+ " + itemInfo.getBonusDefence() + ")"));
        }
        if (items.getHp() != 0 || itemInfo.getBonusHealth() != 0) {
            lore.add(Text.color("&7Health: &c+" + (items.getHp() + itemInfo.getBonusHealth())  + " &8(+ " + itemInfo.getBonusHealth() + ")"));
        }
        if (items.getSpeed() != 0) {
            lore.add(" ");
            lore.add(Text.color("&7Speed: &f+" + (items.getSpeed() + itemInfo.getBonusSpeed())  + " &8(+ " + itemInfo.getBonusSpeed() + ")"));
        }
        if (items.getLore() != null) {
            lore.add(" ");
            for (int i = 0; i < items.getLore().size(); i++) {
                lore.add(Text.color(items.getLore().get(i)));
            }
        }

        lore.add(" ");
        StringJoiner unformattedEnchantments = new StringJoiner("#");
        if (!itemInfo.getEnchantmentsList().isEmpty()) {
            int amount = 0;
            List<StringJoiner> allStringJoiners = new ArrayList<>();
            StringJoiner currentStringJoiner = null;

            for (int i = 0; i < itemInfo.getEnchantmentsList().size(); i++) {
                if (amount == 3) {
                    allStringJoiners.add(currentStringJoiner);
                    currentStringJoiner = new StringJoiner(", ");
                }
                if (currentStringJoiner == null) {
                    currentStringJoiner = new StringJoiner(", ");
                }
                if (i == itemInfo.getEnchantmentsList().size() - 1) {
                    allStringJoiners.add(currentStringJoiner);
                }
                currentStringJoiner.add(
                        Text.color("&5" + itemInfo.getEnchantmentsList().get(i).getEnchantmentType().getName() + " " +
                                itemInfo.getEnchantmentsList().get(i).getLevel()));
                unformattedEnchantments.add(itemInfo.getEnchantmentsList().get(i).getEnchantmentType().toString() + ":" +
                        itemInfo.getEnchantmentsList().get(i).getLevel());
                amount++;
            }
            for (StringJoiner stringJoiner : allStringJoiners) {
                lore.add(Text.color(stringJoiner.toString()));
            }
        }
        lore.add(" ");
        lore.add(" ");
        if (itemInfo.isCooked()) {
            lore.add(Text.color("&8&lCOOKED"));
        }
        if (itemInfo.isProcessed()) {
            lore.add(Text.color("&8&lPROCESSED"));
        }
        lore.add(Text.color(itemInfo.getRarity().getNameColored() + items.getItemType().getName()));
        ItemMeta iM = itemStack.getItemMeta();
        String runicStoneName = "";
        if (itemInfo.getRunicStones() != null) {
            runicStoneName = itemInfo.getRunicStones().getName() + " ";
        }
        String quality = "";
        if (itemInfo.getQuality() != null) {
            quality = itemInfo.getQuality().getColoredName();
        }
        iM.setDisplayName(Text.color(quality + itemInfo.getRarity().getColor() + runicStoneName + items.getName()));
        iM.setLore(lore);

        if (items.doesGlow() || !itemInfo.getEnchantmentsList().isEmpty()) {
            iM.addEnchant(org.bukkit.enchantments.Enchantment.DAMAGE_ALL, 1, true);
        }
        if (itemInfo.hasEnchantment(Enchantments.EFFICIENCY)) {
            iM.addEnchant(Enchantment.DIG_SPEED, itemInfo.getEnchantment(Enchantments.EFFICIENCY).getLevel(), true);
        }
        iM.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        iM.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        iM.setUnbreakable(true);
        iM.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemStack.setItemMeta(iM);
        String runicStone = "NONE";
        if (itemInfo.getRunicStones() != null) {
            runicStone = itemInfo.getRunicStones().toString();
        }
        net.minecraft.server.v1_16_R3.ItemStack nbtStack = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound nbtTagCompound = (nbtStack.hasTag()) ? nbtStack.getTag() : new NBTTagCompound();
        nbtTagCompound.setBoolean("rpgItem",
                true);
        nbtTagCompound.setString("enchantmentsRpg", unformattedEnchantments.toString());
        nbtTagCompound.setString("runicStoneRpg", runicStone);
        nbtTagCompound.setString("itemIdRpg", items.getId());
        nbtTagCompound.setString("rarityRpg", itemInfo.getRarity().toString());
        if (itemInfo.getQuality() != null) {
            nbtTagCompound.setInt("qualityRpg", itemInfo.getQuality().getStar());
        }
        if (itemInfo.isCooked()) {
            nbtTagCompound.setBoolean("cookedRpg", itemInfo.isCooked());
        }
        if (itemInfo.isProcessed()) {
            nbtTagCompound.setBoolean("processedRpg", itemInfo.isProcessed());
        }
        itemStack.setItemMeta(CraftItemStack.getItemMeta(nbtStack));
    }
    public int getSpeed() { return speed; }

    public SellMerchant getSellMerchant() { return sellMerchant; }

    public boolean doesGlow() { return glows; }
    public static Items getItem(String id) {
        for (Items items : EnumSet.allOf(Items.class)) {
            if (items.getId().equals(id)) {
                return items;
            }
        }
        return null;
    }
}
