package com.auxdible.skrpg.mobs;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.utils.ItemBuilder;
import com.auxdible.skrpg.utils.ItemTweaker;
import com.auxdible.skrpg.utils.Text;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;

import java.util.EnumSet;

public enum MobType {
    ZOMBIE("Zombie", 1, 100, 50,  1, EntityType.ZOMBIE,
            null, null, null, null, null, "ZOMBIE"),
    CAVE_ZOMBIE("Cave Zombie", 5, 300, 100, 10, EntityType.ZOMBIE,
            null, ItemTweaker.dye(new ItemBuilder(Material.LEATHER_CHESTPLATE, 0).asItem(), 0, 88, 35),
            ItemTweaker.dye(new ItemBuilder(Material.LEATHER_LEGGINGS, 0).asItem(), 88, 44, 0),
            ItemTweaker.dye(new ItemBuilder(Material.LEATHER_BOOTS, 0).asItem(), 0, 88, 35),
            new ItemBuilder(Material.IRON_PICKAXE, 0).asItem(), "CAVE_ZOMBIE"),
    SKELETON("Skeleton", 2, 50, 100, 25,
            EntityType.SKELETON, null, null, null, null, new ItemBuilder(Material.BOW, 0).asItem(), "SKELETON"),
    PIG("Pig", 0, 10, 0, 0, EntityType.PIG, null, null, null, null, null, "PIG");
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
    MobType(String name, int level, int maxHP, int damage, int defence, EntityType entity, ItemStack helmet,
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
    }
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
