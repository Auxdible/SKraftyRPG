package com.auxdible.skrpg.player.skills;

import com.auxdible.skrpg.items.Items;
import com.auxdible.skrpg.mobs.MobType;
import org.bukkit.entity.Item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum MobKill {
    // todo mob drops
    ZOMBIE(MobType.ZOMBIE, 5, Items.ROTTEN_FLESH, Arrays.asList(Drop.ZOMBIE_FRAGMENT)),
    SKELETON(MobType.SKELETON, 5, Items.BONE, null),
    CHICKEN(MobType.CHICKEN, 0, Items.RAW_CHICKEN, null),
    COW(MobType.COW, 0, Items.RAW_BEEF, null),
    PIG(MobType.PIG, 0, Items.RAW_PORK, null),
    CRAB_KING(MobType.CRAB_KING, 2500, null, null),
    NATURE_ZOMBIE(MobType.NATURE_ZOMBIE, 10, Items.WOOD, null),
    CAMO_SKELETON(MobType.CAMO_SKELETON, 7, Items.WOOD, null),
    SPIDER(MobType.SPIDER, 5, Items.STRING, null),
    SILVERFISH(MobType.SILVERFISH, 2, Items.COAL, null),
    IRON_CONSTRUCT(MobType.IRON_CONSTRUCT, 9, Items.IRON_INGOT, Arrays.asList(Drop.GEMSTONE)),
    ANCIENT_PROSPECTOR(MobType.ANCIENT_PROSPECTOR, 10, Items.GOLD_INGOT, Arrays.asList(Drop.PREMIUM_GOLD_EXTRACT, Drop.GEMSTONE)),
    LAPIS_ZOMBIE(MobType.LAPIS_ZOMBIE, 11, Items.LAPIS_LAZULI, Arrays.asList(Drop.GEMSTONE)),
    MECHANICAL_ARCHER(MobType.MECHANICAL_ARCHER, 12, Items.REDSTONE, Arrays.asList(Drop.GEMSTONE)),
    CRYSTALLITE_ZOMBIE(MobType.CRYSTALLITE_ZOMBIE, 12, Items.CRYSTALLITE, Arrays.asList(Drop.GEMSTONE)),
    DIAMOND_REGENT(MobType.DIAMOND_REGENT, 14, Items.DIAMOND, Arrays.asList(Drop.GEMSTONE)),
    OBSIDIAN_TITAN(MobType.OBSIDIAN_TITAN, 16, Items.OBSIDIAN, Arrays.asList(Drop.GEMSTONE)),
    CRAB_ZOMBIE(MobType.CRAB_ZOMBIE, 20, Items.ROTTEN_FLESH, Arrays.asList(Drop.CRAB_FRAGMENT)),
    VALISSAS_KEEPER(MobType.VALISSAS_KEEPER, 1000, Items.VALISSA_ARACHNE_FRAGMENT, null),
    VALISSA_ARACHNE(MobType.VALISSA_ARACHNE, 5000, null, null),
    FLOPPY_FISH(MobType.FLOPPY_FISH, 5, Items.COD, null),
    FISH(MobType.FISH, 7, Items.COD, null),
    SQUID(MobType.SQUID, 10, Items.INK_SAC, null),
    WATER_WEAKLING(MobType.WATER_WEAKLING, 15, Items.ROTTEN_FLESH, null),
    WATER_WARRIOR(MobType.WATER_WARRIOR, 20, Items.ROTTEN_FLESH, null),
    WATER_ARCHER(MobType.WATER_ARCHER, 20, Items.ROTTEN_FLESH, null),
    WATERFISH(MobType.WATERFISH, 50, Items.SEASHELL, null),
    SEA_HORSE(MobType.SEA_HORSE, 40, Items.ROTTEN_FLESH, null),
    SWAMP_GUARDIAN(MobType.SWAMP_GUARDIAN, 70, Items.ROTTEN_FLESH, null),
    SPEEDSTER_OF_THE_SEA(MobType.SPEEDSTER_OF_THE_SEA, 75, Items.ROTTEN_FLESH, null),
    SEA_LORD(MobType.SEA_LORD, 100, Items.COMPACT_GOLD, null),
    MIDNIGHT_ZOMBIE(MobType.MIDNIGHT_ZOMBIE, 40, Items.WOOD, null),
    MIDNIGHT_ARCHER(MobType.MIDNIGHT_ARCHER, 40, Items.WOOD, null),
    WARPED_MOOSHROOM(MobType.WARPED_MOOSHROOM, 45, Items.MUSHROOM, null),
    MOOSHROOM(MobType.MOOSHROOM, 0, Items.MUSHROOM, null),
    MUSHROOM_GUARD(MobType.MUSHROOM_GUARD, 10, Items.MUSHROOM, null),
    MUSHROOM_ARCHER(MobType.MUSHROOM_ARCHER, 25, Items.MUSHROOM, null),
    MUSHROOM_BERSERK(MobType.MUSHROOM_BERSERK, 50, Items.MUSHROOM, null),
    OVERGROWN_ZOMBIE(MobType.OVERGROWN_ZOMBIE, 0, Items.MUSHROOM, null);
    private MobType mobType;
    private int xpGiven;
    private Items commonDrop;
    private List<Drop> rareDrops;
    MobKill(MobType mobType, int xpGiven, Items commonDrop, List<Drop> rareDrops) {
        this.mobType = mobType;
        this.xpGiven = xpGiven;
        this.commonDrop = commonDrop;
        this.rareDrops = rareDrops;
    }
    public List<Drop> getRareDrops() { return rareDrops; }
    public int getXpGiven() { return xpGiven; }
    public Items getDrop() { return commonDrop; }
    public MobType getMobType() { return mobType; }
}
