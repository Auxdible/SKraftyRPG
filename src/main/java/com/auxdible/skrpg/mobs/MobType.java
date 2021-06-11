package com.auxdible.skrpg.mobs;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.utils.ItemBuilder;
import com.auxdible.skrpg.utils.ItemTweaker;
import com.auxdible.skrpg.utils.Text;
import net.minecraft.server.v1_16_R3.EntityLiving;
import net.minecraft.server.v1_16_R3.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import javax.naming.Name;
import java.util.EnumSet;

public enum MobType {
    /* ---- THE MONOLITH ---- */
    MONOLITH_DUMMY("Monolith Dummy", 1, 100, 0, 0, 0.1, EntityType.ZOMBIE,
            ItemTweaker.createPlayerHeadFromData("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjRiYWE5ZGNmODQyNTU3ODJiNTcyMjM1NzQzNzllNDJjMjM1MGYzYjg4ODE5YmE1YWVmZGRhNGVjMGU3In19fQ==",
                    "044e4002-0012-45a8-9e58-739c39fdfb6c"),
            ItemTweaker.dye(new ItemBuilder(Material.LEATHER_CHESTPLATE, 0).asItem(), 137, 8, 137),
            ItemTweaker.dye(new ItemBuilder(Material.LEATHER_LEGGINGS, 0).asItem(), 137, 8, 137),
            ItemTweaker.dye(new ItemBuilder(Material.LEATHER_BOOTS, 0).asItem(), 137, 8, 137), null, "MONOLITH_DUMMY"),
    /* ---- THE HOSTILE PLAINS (LVL 1) ---- */
    ZOMBIE("Zombie", 1, 100, 50,  0, 0.2, EntityType.ZOMBIE,
            null, null, null, null, null, "ZOMBIE"),
    SKELETON("Skeleton", 2, 50, 50, 0, 0.4,
            EntityType.SKELETON, null, null, null, null, new ItemBuilder(Material.BOW, 0).asItem(), "SKELETON"),
    /* ---- Farming Related Mobs ---- */
    PIG("Pig", 0, 25, 0, 0, 0.4,
            EntityType.PIG, null, null, null, null, null, "PIG"),
    COW("Cow", 0, 50, 0, 0, 0.4,
            EntityType.COW, null, null, null, null, null, "COW"),
    CHICKEN("Chicken", 0, 10, 0, 0, 0.4,
            EntityType.CHICKEN, null, null, null, null, null, "CHICKEN"),
    /* ---- FISHING MOBS ---- */
    FLOPPY_FISH("&bFloppy Fish", 1, 200, 0, 0, 0.5, EntityType.TROPICAL_FISH,
            null, null, null, null, null, "FLOPPY_FISH"),
    FISH("&bFish", 1, 5, 0, 0, 0, EntityType.SALMON, null, null, null, null, null, "FISH"),
    SQUID("&bSquid", 1, 100, 0, 0, 0.2, EntityType.SQUID, null, null, null, null, null, "SQUID"),
    WATER_WEAKLING("&bWater Weakling", 2, 50, 100, 0, 0.4, EntityType.DROWNED, null, null, null, null, null, "WATER_WEAKLING"),
    WATER_ARCHER("&bWater Archer", 3, 100, 125, 0, 0.2, EntityType.STRAY,
            ItemTweaker.dye(new ItemBuilder(Material.LEATHER_HELMET, 0).asItem(), 0, 0, 255),
            ItemTweaker.dye(new ItemBuilder(Material.LEATHER_CHESTPLATE, 0).asItem(), 0, 0, 255),
            ItemTweaker.dye(new ItemBuilder(Material.LEATHER_LEGGINGS, 0).asItem(), 0, 0, 255),
            ItemTweaker.dye(new ItemBuilder(Material.LEATHER_BOOTS, 0).asItem(), 0, 0, 255), null, "WATER_ARCHER"),
    WATER_WARRIOR("&bWater Warrior", 4, 200, 125, 0, 0.4, EntityType.DROWNED,
            null,
            ItemTweaker.dye(new ItemBuilder(Material.LEATHER_CHESTPLATE, 0).asItem(), 0, 0, 255),
            ItemTweaker.dye(new ItemBuilder(Material.LEATHER_LEGGINGS, 0).asItem(), 0, 0, 255),
            ItemTweaker.dye(new ItemBuilder(Material.LEATHER_BOOTS, 0).asItem(), 0, 0, 255),  new ItemBuilder(Material.WOODEN_SWORD, 0).asItem(), "WATER_WARRIOR"),
    // CRAB ZOMBIE FISHING LEVEL 5 MOB
    WATERFISH("&bSea Parasite", 6, 250, 200, 0, 0.5, EntityType.SILVERFISH, null, null, null, null, null, "WATERFISH"),
    SEA_HORSE("&bSea Horse", 7, 500, 0, 200, 0.5, EntityType.ZOMBIE_HORSE, null, null, null, null, null, "SEA_HORSE"),
    SWAMP_GUARDIAN("&2Swamp Guardian", 8, 400, 400, 0, 0.2, EntityType.DROWNED,
            null,
            ItemTweaker.dye(new ItemBuilder(Material.LEATHER_CHESTPLATE, 0).asItem(), 0, 255, 50),
            ItemTweaker.dye(new ItemBuilder(Material.LEATHER_LEGGINGS, 0).asItem(), 0, 255, 50),
            ItemTweaker.dye(new ItemBuilder(Material.LEATHER_BOOTS, 0).asItem(), 0, 255, 50), new ItemBuilder(Material.KELP, 0).asItem(), "SWAMP_GUARDIAN"),
    SPEEDSTER_OF_THE_SEA("&bSpeedster of The Sea", 9, 500, 300, 100, 0.6, EntityType.DROWNED,
            null,
            ItemTweaker.dye(new ItemBuilder(Material.LEATHER_CHESTPLATE, 0).asItem(), 0, 0, 255),
            ItemTweaker.dye(new ItemBuilder(Material.LEATHER_LEGGINGS, 0).asItem(), 0, 0, 255),
            ItemTweaker.dye(new ItemBuilder(Material.LEATHER_BOOTS, 0).asItem(), 255, 255, 255), new ItemBuilder(Material.SUGAR, 0).asItem(), "SPEEDSTER_OF_THE_SEA"),
    SEA_LORD("&bSea Lord", 10, 800, 500, 200, 0.4, EntityType.DROWNED,
            new ItemBuilder(Material.GOLDEN_HELMET, 0).asItem(),
            ItemTweaker.dye(new ItemBuilder(Material.LEATHER_CHESTPLATE, 0).asItem(), 0, 0, 255),
            ItemTweaker.dye(new ItemBuilder(Material.LEATHER_LEGGINGS, 0).asItem(), 0, 0, 150),
            ItemTweaker.dye(new ItemBuilder(Material.LEATHER_BOOTS, 0).asItem(), 0, 0, 100), new ItemBuilder(Material.NETHERITE_SHOVEL, 0).asItem(), "SEA_LORD"),
    /* ---- THE COASTLINE (LVL 5) ---- */
    CRAB_ZOMBIE("Crab Zombie", 5, 500, 80, 0, 0.4, EntityType.ZOMBIE,
            ItemTweaker.createPlayerHeadFromData("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3R" +
                    "leHR1cmUvNTg3MDc2MjY0MWE3ZWM0ZDFkZDhhYWFlY2JlODQ3NWFmN2ZjY" +
                    "zBiNGEyZjQ4ZGExNmZlN2FjZDY0Yzk1NGMifX19","58a95d1b-c705-4f4f-a8fa-2ab0f578c240"),
            ItemTweaker.dye(new ItemBuilder(Material.LEATHER_CHESTPLATE, 0).asItem(), 255, 0, 0),
            ItemTweaker.dye(new ItemBuilder(Material.LEATHER_LEGGINGS, 0).asItem(), 255, 0, 0),
            ItemTweaker.dye(new ItemBuilder(Material.LEATHER_BOOTS, 0).asItem(), 255, 0, 0), null, "CRAB_ZOMBIE"),
    CRAB_KING("Crab King", 50, 50000, 300, 0, 0.6, EntityType.ZOMBIE,
            ItemTweaker.createPlayerHeadFromData("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGI2ZWI0MjA4MmU0Y" +
                    "zc0MDlkOTlhYmNhMjdjNjZjZjY5N2RkNmJjZWVjNDE3NGE5NDM5YmQ5YWJmZGZmMDVlIn19fQ==",
                    "8edda98d-d25a-453b-aa3f-50e0cd4c7644"),
            ItemTweaker.dye(new ItemBuilder(Material.LEATHER_CHESTPLATE, 0).asItem(), 99, 0, 0),
            ItemTweaker.dye(new ItemBuilder(Material.LEATHER_LEGGINGS, 0).asItem(), 99, 0, 0),
            ItemTweaker.dye(new ItemBuilder(Material.LEATHER_BOOTS, 0).asItem(), 99, 0, 0), new ItemBuilder(Material.GOLDEN_SWORD, 0).asItem(), "CRAB_KING"),

    /* ---- THE THICKETS (LVL 4-5) ---- */
    NATURE_ZOMBIE("&aNature Zombie", 4, 300, 110, 0, 0.2, EntityType.ZOMBIE,
            ItemTweaker.createPlayerHeadFromData("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90Z" +
                    "Xh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2E4ZDNkM2IyZTZiYWJlY2U1NTI2N" +
                    "DEzNjA1N2EyNjgyMWMzNzA0MTMyOTY2ZWU5ZWNjYTkzYWNjOTg4N2EwIn19fQ==", "f85a124b-c8b0-43d9-87f4-74b8a53ce718"),
            ItemTweaker.dye(new ItemBuilder(Material.LEATHER_CHESTPLATE, 0).asItem(), 0, 255, 0),
            ItemTweaker.dye(new ItemBuilder(Material.LEATHER_LEGGINGS, 0).asItem(), 0, 255, 0),
            ItemTweaker.dye(new ItemBuilder(Material.LEATHER_BOOTS, 0).asItem(), 0, 255, 0), null, "NATURE_ZOMBIE"),
    CAMO_SKELETON("&aCamouflaged Skeleton", 5, 200, 300, 0, 0.5, EntityType.SKELETON,
            ItemTweaker.dye(new ItemBuilder(Material.LEATHER_HELMET, 0).asItem(), 0, 100, 0),
            ItemTweaker.dye(new ItemBuilder(Material.LEATHER_CHESTPLATE, 0).asItem(), 0, 100, 0),
            ItemTweaker.dye(new ItemBuilder(Material.LEATHER_LEGGINGS, 0).asItem(), 0, 100, 0),
            ItemTweaker.dye(new ItemBuilder(Material.LEATHER_BOOTS, 0).asItem(), 0, 100, 0), new ItemBuilder(Material.BOW, 0).asItem(), "CAMO_SKELETON"),
    MIDNIGHT_ZOMBIE("&bMidnight Zombie", 8, 750, 300, 0, 0.4, EntityType.ZOMBIE,
            ItemTweaker.createPlayerHeadFromData("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjdlMTFkYjhmMzkyNzI3YTFjNDU5ZWI1OTFkZDhhMGFhMzg2MWMyNzQ4N2UxMzg0YmIyMmY5MTU3M2JlNyJ9fX0=", "14cb891f-35b9-43ed-aa29-86a5aa77cba0"),
            ItemTweaker.dye(new ItemBuilder(Material.LEATHER_CHESTPLATE, 0).asItem(), 0, 255, 255),
            ItemTweaker.dye(new ItemBuilder(Material.LEATHER_LEGGINGS, 0).asItem(), 0, 255, 255),
            ItemTweaker.dye(new ItemBuilder(Material.LEATHER_BOOTS, 0).asItem(), 0, 255, 255), null, "MIDNIGHT_ZOMBIE"),
    MIDNIGHT_ARCHER("&bMidnight Archer", 8, 500, 450, 0, 0.4, EntityType.ZOMBIE,
            ItemTweaker.createPlayerHeadFromData("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjdlMTFkYjhmMzkyNzI3YTFjNDU5ZWI1OTFkZDhhMGFhMzg2MWMyNzQ4N2UxMzg0YmIyMmY5MTU3M2JlNyJ9fX0=", "14cb891f-35b9-43ed-aa29-86a5aa77cba0"),
            ItemTweaker.dye(new ItemBuilder(Material.LEATHER_CHESTPLATE, 0).asItem(), 0, 255, 255),
            ItemTweaker.dye(new ItemBuilder(Material.LEATHER_LEGGINGS, 0).asItem(), 0, 255, 255),
            ItemTweaker.dye(new ItemBuilder(Material.LEATHER_BOOTS, 0).asItem(), 0, 255, 255), new ItemBuilder(Material.BOW, 0).asItem(), "MIDNIGHT_ARCHER"),
    SPIDER("Spider", 4, 100, 90, 25, 0.5, EntityType.SPIDER,
            null, null, null, null, null, "SPIDER"),
    VALISSAS_KEEPER("&c&lValissa's Keeper", 50, 2500, 300, 50, 0.3, EntityType.CAVE_SPIDER, null, null, null, null, null, "VALISSAS_KEEPER"),
    VALISSA_ARACHNE("&4&lValissa Arachne", 100, 100000, 500, 0, 0.7, EntityType.SPIDER, null, null, null, null, null, "VALISSA_ARACHNE"),
    /* ---- THE MINES ---- */
    SILVERFISH("Silverfish", 1, 20, 25, 0, 0.4, EntityType.SILVERFISH,
            null, null, null, null, null, "SILVERFISH"),
    IRON_CONSTRUCT("Iron Construct", 4, 1000, 400, 500, 0.1, EntityType.ZOMBIE,
            new ItemBuilder(Material.IRON_ORE, 0).asItem(),
            ItemTweaker.dye(new ItemBuilder(Material.LEATHER_CHESTPLATE, 0).asItem(), 153, 153, 153 ),
            ItemTweaker.dye(new ItemBuilder(Material.LEATHER_LEGGINGS, 0).asItem(), 153, 153, 153),
            ItemTweaker.dye(new ItemBuilder(Material.LEATHER_BOOTS, 0).asItem(), 153, 153, 153 ), new ItemBuilder(Material.IRON_AXE, 0).asItem(), "IRON_CONSTRUCT"),
    ANCIENT_PROSPECTOR("Ancient Prospector", 6, 500, 100, 0, 0.2, EntityType.ZOMBIE,
            ItemTweaker.createPlayerHeadFromData("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dH" +
                    "A6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUv" +
                    "NjlmZDEwOGNjZmE2MTk5OTY5M2UxMjUxYTljNmE1M2YzOTM4NDliOTFmZGE5ZWFlOTI2ZDg5OTQyYWIwZGYifX19",
                    "6ee6e3d3-fc3d-4a2b-887a-04374b9cf69d"), new ItemBuilder(Material.GOLDEN_CHESTPLATE, 0).asItem(),
            new ItemBuilder(Material.GOLDEN_LEGGINGS, 0).asItem(),
            new ItemBuilder(Material.GOLDEN_BOOTS, 0).asItem(),
            new ItemBuilder(Material.GOLDEN_PICKAXE, 0).asItem(), "ANCIENT_PROSPECTOR"),
    LAPIS_ZOMBIE("Lapis Zombie", 8, 700, 150, 0, 0.2, EntityType.ZOMBIE,
            ItemTweaker.createPlayerHeadFromData("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3Rl" +
                            "eHR1cmUvYzg1NWE3NTMwMmU5YWQ4YmYxNGI0NTJjNWRiZmRmNzcxMzRmNDJjZDYzYjk3NzU3Y2NkZDk3YWNlYjk3YSJ9fX0=",
                    "cb2d0167-a068-4775-80bf-6beb918e3dd6"),
            ItemTweaker.dye(new ItemBuilder(Material.LEATHER_CHESTPLATE, 0).asItem(), 0, 0, 255),
            ItemTweaker.dye(new ItemBuilder(Material.LEATHER_LEGGINGS, 0).asItem(), 0, 0, 171),
            ItemTweaker.dye(new ItemBuilder(Material.LEATHER_BOOTS, 0).asItem(), 0, 0   , 255), new ItemBuilder(Material.LAPIS_LAZULI, 0).asItem(), "LAPIS_ZOMBIE"),
    MECHANICAL_ARCHER("Mechanical Archer", 8, 650, 125, 0, 0.4, EntityType.SKELETON,
            ItemTweaker.createPlayerHeadFromData("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5" +
                            "taW5lY3JhZnQubmV0L3RleHR1cmUvNTI2N2E3Nzlm" +
                            "MmRjMjk4MTVhMjAzMWYxOTUxNWMyMTZkYmM5MTc4OWE3M2JjYjkzZjExNDNmNmU1NTMxZTUzIn19fQ==",
                    "6f976679-47b1-49de-a092-3a2f3ef9c08f"),
            ItemTweaker.dye(new ItemBuilder(Material.LEATHER_CHESTPLATE, 0).asItem(), 255, 0, 0),
            ItemTweaker.dye(new ItemBuilder(Material.LEATHER_LEGGINGS, 0).asItem(), 171, 0, 0),
            ItemTweaker.dye(new ItemBuilder(Material.LEATHER_BOOTS, 0).asItem(), 255, 0, 0), new ItemBuilder(Material.BOW, 0).asItem(), "MECHANICAL_ARCHER"),
    CRYSTALLITE_ZOMBIE("Crystallite Zombie", 10, 1200, 175, 40, 0.2, EntityType.ZOMBIE,
            new ItemBuilder(Material.WHITE_STAINED_GLASS, 0).asItem(),
            ItemTweaker.dye(new ItemBuilder(Material.LEATHER_CHESTPLATE, 0).asItem(), 255,255, 255),
            ItemTweaker.dye(new ItemBuilder(Material.LEATHER_LEGGINGS, 0).asItem(), 255, 192, 203),
            ItemTweaker.dye(new ItemBuilder(Material.LEATHER_BOOTS, 0).asItem(), 255, 255, 255),
            new ItemBuilder(Material.QUARTZ, 0).asItem(), "CRYSTALLITE_ZOMBIE"),
    DIAMOND_REGENT("Diamond Regent", 12, 1500, 200, 0, 0.2, EntityType.ZOMBIE,
            new ItemBuilder(Material.GOLDEN_HELMET, 0).asItem(),
            new ItemBuilder(Material.DIAMOND_CHESTPLATE, 0).asItem(),
            new ItemBuilder(Material.DIAMOND_LEGGINGS, 0).asItem(),
            new ItemBuilder(Material.DIAMOND_BOOTS, 0).asItem(),
            new ItemBuilder(Material.DIAMOND_SHOVEL, 0).asItem(), "DIAMOND_REGENT"),
    OBSIDIAN_TITAN("Obsidian Titan", 14, 2300, 250, 0, 0.2, EntityType.ZOMBIE,
            new ItemBuilder(Material.OBSIDIAN, 0).asItem(),
            ItemTweaker.dye(new ItemBuilder(Material.LEATHER_CHESTPLATE, 0).asItem(), 74,0, 91),
            ItemTweaker.dye(new ItemBuilder(Material.LEATHER_LEGGINGS, 0).asItem(), 99, 0, 120),
            ItemTweaker.dye(new ItemBuilder(Material.LEATHER_BOOTS, 0).asItem(), 99, 0, 120),
            new ItemBuilder(Material.NETHERITE_SWORD, 0).asItem(), "OBSIDIAN_TITAN"),
    /* ---- MUSHROOM PLAINS (LVL 8) ---- */
    WARPED_MOOSHROOM("&bWarped Mooshroom", 12, 2000, 0, 100, 1.0, EntityType.MUSHROOM_COW,
            null, null, null, null, null, "WARPED_MOOSHROOM"),
    MOOSHROOM("&cMooshroom", 5, 100, 0, 0, 0.4, EntityType.MUSHROOM_COW,
            null, null, null, null, null, "MOOSHROOM"),
    OVERGROWN_ZOMBIE("Fungal Overgrow", 12, 2500, 1000, 0, 0.2,  EntityType.ZOMBIE,
            ItemTweaker.createPlayerHeadFromData("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6" +
                    "Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjJlOTNkODY3MzQzM2" +
                    "NlYmJiYjMyMDgzYjg1NGFiMmJlZDE2NDFhZDA1YzQ2NGE5ZWYxNTljYjRiN2NlZTkifX19", "f57e3b0c-4038-4fc2-8d02-49bd2df7eea4"),
            ItemTweaker.dye(new ItemBuilder(Material.LEATHER_CHESTPLATE, 0).asItem(), 255, 0, 0),
            ItemTweaker.dye(new ItemBuilder(Material.LEATHER_LEGGINGS, 0).asItem(), 255, 0, 0),
            ItemTweaker.dye(new ItemBuilder(Material.LEATHER_BOOTS, 0).asItem(), 255, 0, 0), null, "OVERGROWN_ZOMBIE"),
    MUSHROOM_GUARD("Mushroom Guard", 9, 1000, 500, 0, 0.2, EntityType.ZOMBIE,
            ItemTweaker.createPlayerHeadFromData("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTY2ZTFlZDhiNWU3MTlkYzgzOTgwNDlmNmZjM2VmNmRjYjBkOTk2YWE5ZGFjYjcxY2NlM2QzNzliMDc2In19fQ==", "be9f1c4a-39b3-4a54-8801-3accac9327ce"),
            ItemTweaker.dye(new ItemBuilder(Material.LEATHER_CHESTPLATE, 0).asItem(), 255, 0, 0),
            ItemTweaker.dye(new ItemBuilder(Material.LEATHER_LEGGINGS, 0).asItem(), 150, 0, 0),
            ItemTweaker.dye(new ItemBuilder(Material.LEATHER_BOOTS, 0).asItem(), 255, 0, 0), new ItemBuilder(Material.RED_MUSHROOM, 0).asItem(), "MUSHROOM_GUARD"),
    MUSHROOM_ARCHER("Mushroom Archer", 9, 750, 750, 0, 0.4, EntityType.SKELETON,
            ItemTweaker.createPlayerHeadFromData("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzE2NDNkZWU0YzM3MmU4OTdmYjJlMTMyNTBjMDliMmZmZjc0MWNjZTU0MmMxN2I5ZWQ5ZWVjZGZjMjdiYjkyIn19fQ==", "750f049b-d42c-4cb8-a4c5-8d07809ea7c0"),
            ItemTweaker.dye(new ItemBuilder(Material.LEATHER_CHESTPLATE, 0).asItem(), 0, 0, 255),
            ItemTweaker.dye(new ItemBuilder(Material.LEATHER_LEGGINGS, 0).asItem(), 0, 0, 150),
            ItemTweaker.dye(new ItemBuilder(Material.LEATHER_BOOTS, 0).asItem(), 0, 0, 255), new ItemBuilder(Material.BOW, 0).asItem(), "MUSHROOM_ARCHER"),
    MUSHROOM_BERSERK("Mushroom Berserk", 20, 10000, 4000, 0, 0.4, EntityType.ZOMBIE,
            ItemTweaker.createPlayerHeadFromData("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjc5YTYyNTgyNGQzYjA0MjUwNThiNDZkMmYxNGU1NWRiNTRjZmJjM2YzOWRlNDQ2NmJlNGQ5ODgzYWUzMWYifX19", "16eb1d8d-3dbb-434d-8c6e-3ff11a18ebef"),
            ItemTweaker.dye(new ItemBuilder(Material.LEATHER_CHESTPLATE, 0).asItem(), 162, 0, 255),
            ItemTweaker.dye(new ItemBuilder(Material.LEATHER_LEGGINGS, 0).asItem(), 162, 0, 255),
            ItemTweaker.dye(new ItemBuilder(Material.LEATHER_BOOTS, 0).asItem(), 162, 0, 255), new ItemBuilder(Material.NETHERITE_SWORD, 0).asItem(), "ROYAL_MUSHROOM_GUARDr");



    private String name;
    private int level;
    private int maxHP;
    private int damage;
    private int defence;
    private EntityType entity;
    private ItemStack helmet;
    private ItemStack chestplate;
    private ItemStack leggings;
    private ItemStack boots;
    private ItemStack itemInHand;
    private String id;
    private double speed;
    MobType(String name, int level, int maxHP, int damage, int defence, double speed, EntityType entity, ItemStack helmet,
            ItemStack chestplate, ItemStack leggings, ItemStack boots, ItemStack itemInHand, String id) {
        this.name = name;
        this.level = level;
        this.maxHP = maxHP;
        this.damage = damage;
        this.defence = defence;
        this.entity = entity;
        this.helmet = helmet;
        this.chestplate = chestplate;
        this.leggings = leggings;
        this.boots = boots;
        this.itemInHand = itemInHand;
        this.id = id;
        this.speed = speed;
    }
    public double getSpeed() { return speed; }
    public EntityType getEntity() { return entity; }
    public int getDamage() { return damage; }
    public int getDefence() { return defence; }
    public int getLevel() { return level; }
    public int getMaxHP() { return maxHP; }
    public String getName() { return name; }
    public ItemStack getBoots() { return boots; }
    public ItemStack getChestplate() { return chestplate; }
    public ItemStack getHelmet() { return helmet; }
    public ItemStack getItemInHand() { return itemInHand; }
    public ItemStack getLeggings() { return leggings; }
    public String getId() { return id; }
    public static void buildMob(String id, SKRPG skrpg, MobSpawn spawn) {
        for (MobType mobType : EnumSet.allOf(MobType.class)) {
            if (id.equalsIgnoreCase(mobType.getId())) {
                Entity entity = Bukkit.getWorld(skrpg.getConfig().getString("rpgWorld"))
                        .spawnEntity(spawn.getLocation(), mobType.getEntity());
                if (entity instanceof Creature) {
                    Creature creature = (Creature) entity;
                    creature.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(mobType.getSpeed());
                    creature.setRemoveWhenFarAway(false);
                    if (mobType == MobType.MONOLITH_DUMMY) {
                        ((Creature) entity).setAI(false);
                    }
                }
                if (entity instanceof Monster) {
                    Monster monster = (Monster) entity;
                    monster.getEquipment().setHelmet(mobType.getHelmet());
                    monster.getEquipment().setChestplate(mobType.getChestplate());
                    monster.getEquipment().setLeggings(mobType.getLeggings());
                    monster.getEquipment().setBoots(mobType.getBoots());
                    monster.getEquipment().setItemInMainHand(mobType.getItemInHand());
                    monster.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(15.0);
                    if (monster instanceof Zombie) {
                        ((Zombie) monster).setBaby(false);
                    }
                }
                entity.setPassenger(null);
                entity.setCustomName(Text.color("&8&l[&c" + + mobType.getLevel() + "☠&8&l] &r&7" + mobType.getName() + " &a" +
                        mobType.getMaxHP() + "&c♥"));
                entity.setCustomNameVisible(true);
                PersistentDataContainer persistentDataContainer = entity.getPersistentDataContainer();
                persistentDataContainer.set(new NamespacedKey(skrpg, "mobId"),  PersistentDataType.STRING, mobType.getId());
                Mob mob = new Mob(mobType, entity);
                skrpg.getMobManager().addMob(mob);
                spawn.getCurrentlySpawnedMobs().add(mob);
            }
        }

    }
    public static void buildMob(String id, SKRPG skrpg, Player player) {
        for (MobType mobType : EnumSet.allOf(MobType.class)) {
            if (id.equalsIgnoreCase(mobType.getId())) {
                Entity entity = Bukkit.getWorld(skrpg.getConfig().getString("rpgWorld"))
                        .spawnEntity(player.getLocation(), mobType.getEntity());
                if (entity instanceof Creature) {
                    Creature creature = (Creature) entity;
                    creature.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(mobType.getSpeed());
                    creature.setRemoveWhenFarAway(false);
                    if (mobType == MobType.MONOLITH_DUMMY) {
                        ((Creature) entity).setAI(false);
                    }
                }
                if (entity instanceof Monster) {
                    Monster monster = (Monster) entity;
                    monster.getEquipment().setHelmet(mobType.getHelmet());
                    monster.getEquipment().setChestplate(mobType.getChestplate());
                    monster.getEquipment().setLeggings(mobType.getLeggings());
                    monster.getEquipment().setBoots(mobType.getBoots());
                    monster.getEquipment().setItemInMainHand(mobType.getItemInHand());
                    monster.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(15.0);
                    if (monster instanceof Zombie) {
                        ((Zombie) monster).setBaby(false);
                    }
                }
                entity.setPassenger(null);
                entity.setCustomName(Text.color("&8&l[&c" + + mobType.getLevel() + "☠&8&l] &r&7" + mobType.getName() + " &a" +
                        mobType.getMaxHP() + "&c♥"));

                entity.setCustomNameVisible(true);
                PersistentDataContainer persistentDataContainer = entity.getPersistentDataContainer();
                persistentDataContainer.set(new NamespacedKey(skrpg, "mobId"),  PersistentDataType.STRING, mobType.getId());
                Mob mob = new Mob(mobType, entity);
                skrpg.getMobManager().addMob(mob);
            }
        }

    }
    public static Mob buildMob(String id, SKRPG skrpg, Location loc) {
        for (MobType mobType : EnumSet.allOf(MobType.class)) {
            if (id.equalsIgnoreCase(mobType.getId())) {
                Entity entity = Bukkit.getWorld(skrpg.getConfig().getString("rpgWorld"))
                        .spawnEntity(loc, mobType.getEntity());
                if (entity instanceof Creature) {
                    Creature creature = (Creature) entity;
                    creature.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(mobType.getSpeed());
                    creature.setRemoveWhenFarAway(false);
                    if (mobType == MobType.MONOLITH_DUMMY) {
                        ((Creature) entity).setAI(false);
                    }
                }
                if (entity instanceof Monster) {
                    Monster monster = (Monster) entity;
                    monster.getEquipment().setHelmet(mobType.getHelmet());
                    monster.getEquipment().setChestplate(mobType.getChestplate());
                    monster.getEquipment().setLeggings(mobType.getLeggings());
                    monster.getEquipment().setBoots(mobType.getBoots());
                    monster.getEquipment().setItemInMainHand(mobType.getItemInHand());
                    monster.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(15.0);
                    if (monster instanceof Zombie) {
                        ((Zombie) monster).setBaby(false);
                    }
                }

                entity.setCustomName(Text.color("&8&l[&c" + + mobType.getLevel() + "☠&8&l] &r&7" + mobType.getName() + " &a" +
                        mobType.getMaxHP() + "&c♥"));
                entity.setCustomNameVisible(true);
                PersistentDataContainer persistentDataContainer = entity.getPersistentDataContainer();
                persistentDataContainer.set(new NamespacedKey(skrpg, "mobId"),  PersistentDataType.STRING, mobType.getId());

                Mob mob = new Mob(mobType, entity);
                skrpg.getMobManager().addMob(mob);
                return mob;
            }
        }
        return null;
    }
    public static MobType getMob(String id) {
        for (MobType mobType : EnumSet.allOf(MobType.class)) {
            if (mobType.getId() == id) {
                return mobType;
            }
        }
        return null;
    }
}
