package com.auxdible.skrpg.mobs;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.utils.ItemBuilder;
import com.auxdible.skrpg.utils.ItemTweaker;
import com.auxdible.skrpg.utils.Text;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;

import java.util.EnumSet;

public enum MobType {
    /* ---- THE HOSTILE PLAINS (LVL 1) ---- */
    ZOMBIE("Zombie", 1, 100, 50,  0, 0.7, EntityType.ZOMBIE,
            null, null, null, null, null, "ZOMBIE"),
    SKELETON("Skeleton", 2, 50, 50, 0, 0.7,
            EntityType.SKELETON, null, null, null, null, new ItemBuilder(Material.BOW, 0).asItem(), "SKELETON"),
    /* ---- GENERIC MOBS (NO LVL) ---- */
    CITIZEN("Citizen", 0, 100000, 0, 100000, 0.4,
            EntityType.VILLAGER, null, null, null, null, null, "CITIZEN"),
    /* ---- Farming Related Mobs ---- */
    PIG("Pig", 0, 25, 0, 0, 0.7,
            EntityType.PIG, null, null, null, null, null, "PIG"),
    COW("Cow", 0, 50, 0, 0, 0.7,
            EntityType.COW, null, null, null, null, null, "COW"),
    CHICKEN("Chicken", 0, 10, 0, 0, 0.7,
            EntityType.CHICKEN, null, null, null, null, null, "CHICKEN"),
    /* ---- THE COASTLINE (LVL 2) ---- */
    CRAB_ZOMBIE("Crab Zombie", 3, 200, 80, 0, 1.5, EntityType.ZOMBIE,
            ItemTweaker.createPlayerHeadFromData("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3R" +
                    "leHR1cmUvNTg3MDc2MjY0MWE3ZWM0ZDFkZDhhYWFlY2JlODQ3NWFmN2ZjY" +
                    "zBiNGEyZjQ4ZGExNmZlN2FjZDY0Yzk1NGMifX19","58a95d1b-c705-4f4f-a8fa-2ab0f578c240"),
            ItemTweaker.dye(new ItemBuilder(Material.LEATHER_CHESTPLATE, 0).asItem(), 255, 0, 0),
            ItemTweaker.dye(new ItemBuilder(Material.LEATHER_LEGGINGS, 0).asItem(), 255, 0, 0),
            ItemTweaker.dye(new ItemBuilder(Material.LEATHER_BOOTS, 0).asItem(), 255, 0, 0), null, "CRAB_ZOMBIE"),
    /* ---- THE THICKETS (LVL 4-5) ---- */
    NATURE_ZOMBIE("&aNature Zombie", 4, 300, 110, 0, 1.0, EntityType.ZOMBIE,
            ItemTweaker.createPlayerHeadFromData("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90Z" +
                    "Xh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2E4ZDNkM2IyZTZiYWJlY2U1NTI2N" +
                    "DEzNjA1N2EyNjgyMWMzNzA0MTMyOTY2ZWU5ZWNjYTkzYWNjOTg4N2EwIn19fQ==", "f85a124b-c8b0-43d9-87f4-74b8a53ce718"),
            ItemTweaker.dye(new ItemBuilder(Material.LEATHER_CHESTPLATE, 0).asItem(), 0, 255, 0),
            ItemTweaker.dye(new ItemBuilder(Material.LEATHER_LEGGINGS, 0).asItem(), 0, 255, 0),
            ItemTweaker.dye(new ItemBuilder(Material.LEATHER_BOOTS, 0).asItem(), 0, 255, 0), null, "NATURE_ZOMBIE"),
    CAMO_SKELETON("&aCamouflaged Skeleton", 5, 200, 300, 0, 0.2, EntityType.SKELETON,
            ItemTweaker.dye(new ItemBuilder(Material.LEATHER_HELMET, 0).asItem(), 0, 255, 0),
            ItemTweaker.dye(new ItemBuilder(Material.LEATHER_CHESTPLATE, 0).asItem(), 0, 255, 0),
            ItemTweaker.dye(new ItemBuilder(Material.LEATHER_LEGGINGS, 0).asItem(), 0, 255, 0),
            ItemTweaker.dye(new ItemBuilder(Material.LEATHER_BOOTS, 0).asItem(), 0, 255, 0), new ItemBuilder(Material.BOW, 0).asItem(), "CAMO_SKELETON"),
    SPIDER("Spider", 4, 100, 90, 25, 1.0, EntityType.SPIDER,
            null, null, null, null, null, "SPIDER"),
    /* ---- MOUNTAIN AREAS 1 (LVL 5-10) ---- */
    CAVE_ZOMBIE("Cave Zombie", 5, 300, 200, 0, 0.7, EntityType.ZOMBIE,
            null, ItemTweaker.dye(new ItemBuilder(Material.LEATHER_CHESTPLATE, 0).asItem(), 0, 88, 35),
            ItemTweaker.dye(new ItemBuilder(Material.LEATHER_LEGGINGS, 0).asItem(), 88, 44, 0),
            ItemTweaker.dye(new ItemBuilder(Material.LEATHER_BOOTS, 0).asItem(), 0, 88, 35),
            new ItemBuilder(Material.IRON_PICKAXE, 0).asItem(), "CAVE_ZOMBIE"),
    /* ---- MUSHROOM FOREST (LVL 8) ---- */
    WARPED_MOOSHROOM("&bWarped Mooshroom", 6, 500, 0, 100, 3.0, EntityType.MUSHROOM_COW,
            null, null, null, null, null, "WARPED_MOOSHROOM"),
    MOOSHROOM("&cMooshroom", 5, 10, 0, 0, 0.7, EntityType.MUSHROOM_COW,
            null, null, null, null, null, "MOOSHROOM"),
    OVERGROWN_ZOMBIE("Overgrown Zombie", 8, 1000, 300, 0, 0.7,  EntityType.ZOMBIE,
            ItemTweaker.createPlayerHeadFromData("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6" +
                    "Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjJlOTNkODY3MzQzM2" +
                    "NlYmJiYjMyMDgzYjg1NGFiMmJlZDE2NDFhZDA1YzQ2NGE5ZWYxNTljYjRiN2NlZTkifX19", "f57e3b0c-4038-4fc2-8d02-49bd2df7eea4"),
            ItemTweaker.dye(new ItemBuilder(Material.LEATHER_CHESTPLATE, 0).asItem(), 255, 0, 0),
            ItemTweaker.dye(new ItemBuilder(Material.LEATHER_LEGGINGS, 0).asItem(), 255, 0, 0),
            ItemTweaker.dye(new ItemBuilder(Material.LEATHER_BOOTS, 0).asItem(), 255, 0, 0), null, "OVERGROWN_ZOMBIE");
    /* ---- CRYSTALLITE HILLS (LVL 15) ---- */


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
                }
                if (entity instanceof Monster) {
                    Monster monster = (Monster) entity;
                    monster.getEquipment().setHelmet(mobType.getHelmet());
                    monster.getEquipment().setChestplate(mobType.getChestplate());
                    monster.getEquipment().setLeggings(mobType.getLeggings());
                    monster.getEquipment().setBoots(mobType.getBoots());
                    monster.getEquipment().setItemInMainHand(mobType.getItemInHand());
                    if (monster instanceof Zombie) {
                        ((Zombie) monster).setBaby(false);
                    }
                }
                entity.setCustomName(Text.color("&8[LVL &e" + mobType.getLevel() + "&8] &r&c" + mobType.getName() + " " + "&f" +
                        mobType.getMaxHP() + "&c♥"));

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
                if (entity instanceof Monster) {
                    Monster monster = (Monster) entity;
                    monster.getEquipment().setHelmet(mobType.getHelmet());
                    monster.getEquipment().setChestplate(mobType.getChestplate());
                    monster.getEquipment().setLeggings(mobType.getLeggings());
                    monster.getEquipment().setBoots(mobType.getBoots());
                    monster.getEquipment().setItemInMainHand(mobType.getItemInHand());
                    if (monster instanceof Zombie) {
                        ((Zombie) monster).setBaby(false);
                    }
                }

                entity.setCustomName(Text.color("&7&l☠&e" + mobType.getLevel() + "&8 &r&8" + mobType.getName() + " " + "&c" +
                        mobType.getMaxHP() + "&c♥"));
                entity.setCustomNameVisible(true);
                Mob mob = new Mob(mobType, entity);
                skrpg.getMobManager().addMob(mob);
            }
        }

    }
}
