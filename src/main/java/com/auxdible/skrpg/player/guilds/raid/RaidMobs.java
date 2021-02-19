package com.auxdible.skrpg.player.guilds.raid;

import com.auxdible.skrpg.utils.ItemBuilder;
import com.auxdible.skrpg.utils.ItemTweaker;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

public enum RaidMobs {
    ZOMBIE("&cZombie", EntityType.ZOMBIE, 15000, null,
            null,
            null, null, new ItemBuilder(Material.WOODEN_AXE, 0).asItem(),
            250, 2000, 10, 0.7),
    STONE_ZOMBIE("&7Stone Zombie", EntityType.ZOMBIE, 17500,
            new ItemBuilder(Material.STONE, 0).asItem(),
            ItemTweaker.dye(new ItemBuilder(Material.LEATHER_CHESTPLATE, 0).asItem(), 255, 0, 0),
            ItemTweaker.dye(new ItemBuilder(Material.LEATHER_CHESTPLATE, 0).asItem(), 255, 0, 0),
            ItemTweaker.dye(new ItemBuilder(Material.LEATHER_CHESTPLATE, 0).asItem(), 255, 0, 0),
            null, 125, 4000, 10, 0.3),
    BERSERK_ZOMBIE("&cBerserk Zombie", EntityType.ZOMBIE, 15000,
            null,
            ItemTweaker.dye(new ItemBuilder(Material.LEATHER_CHESTPLATE, 0).asItem(), 255, 0, 0), null, null,
            new ItemBuilder(Material.GOLDEN_AXE, 0).asItem(), 400, 1000, 10, 1.5),
    IRONCLAD("&7Iron&fclad", EntityType.IRON_GOLEM, 50000, null, null, null, null, null, 300, 6000, 40, 0.2),
    CRYSTALLITE_ATTACKER("&fCrystallite Attacker", EntityType.ZOMBIE, 50000, new ItemBuilder(Material.WHITE_STAINED_GLASS, 0).asItem(),
            ItemTweaker.dye(new ItemBuilder(Material.LEATHER_CHESTPLATE, 0).asItem(), 255,255, 255),
            ItemTweaker.dye(new ItemBuilder(Material.LEATHER_LEGGINGS, 0).asItem(), 255, 192, 203), ItemTweaker.dye(new ItemBuilder(Material.LEATHER_BOOTS, 0).asItem(), 255, 255, 255),
            new ItemBuilder(Material.QUARTZ, 0).asItem(), 400, 4000, 40, 0.7),
    ZOMBIE_PIRATE("&cZombie Pirate", EntityType.ZOMBIE, 60000,
            ItemTweaker.createPlayerHeadFromData("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA" +
                    "6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3Rle" +
                    "HR1cmUvZjU5ODE5NDViZDlhOTE3OWZiODI5N2EwNjYxYmViMjMxYmNmZGE4M2E0ZGRlZTZiMjA4N2NmZDdiNGNiZDk5OSJ9fX0=",
                    "8ed700f3-3bcd-410c-b090-04d3a96a90af"),
            ItemTweaker.dye(new ItemBuilder(Material.LEATHER_CHESTPLATE, 0).asItem(), 0,0, 0),
            ItemTweaker.dye(new ItemBuilder(Material.LEATHER_LEGGINGS, 0).asItem(), 0,0, 0),
            ItemTweaker.dye(new ItemBuilder(Material.LEATHER_BOOTS, 0).asItem(), 0,0, 0),
            new ItemBuilder(Material.IRON_SWORD, 0).asItem(), 500, 2000, 40, 1.7);
    private String name;
    private EntityType entityType;
    private int creditsCost;
    private ItemStack helmet;
    private ItemStack chestplate;
    private ItemStack leggings;
    private ItemStack boots;
    private ItemStack itemInHand;
    private int damage;
    private int hp;
    private int skillLevelRequired;
    RaidMobs(String name, EntityType entityType, int creditsCost, ItemStack helmet, ItemStack chestplate, ItemStack leggings,
             ItemStack boots, ItemStack itemInHand, int damage, int hp, int skillLevelRequired, double speed) {
        this.entityType = entityType;
        this.creditsCost = creditsCost;
        this.helmet = helmet;
        this.chestplate = chestplate;
        this.leggings = leggings;
        this.boots = boots;
        this.itemInHand = itemInHand;
        this.damage = damage;
        this.hp = hp;
        this.name = name;
        this.skillLevelRequired = skillLevelRequired;
    }
    public String getName() { return name; }
    public EntityType getEntityType() { return entityType; }
    public int getCreditsCost() { return creditsCost; }
    public int getDamage() { return damage; }
    public int getHp() { return hp; }
    public ItemStack getBoots() { return boots; }
    public ItemStack getChestplate() { return chestplate; }
    public ItemStack getHelmet() { return helmet; }
    public ItemStack getItemInHand() { return itemInHand; }
    public ItemStack getLeggings() { return leggings; }
    public int getSkillLevelRequired() { return skillLevelRequired; }
}
