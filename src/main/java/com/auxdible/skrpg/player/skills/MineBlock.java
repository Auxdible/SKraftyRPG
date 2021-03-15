package com.auxdible.skrpg.player.skills;

import com.auxdible.skrpg.items.Items;
import org.bukkit.Material;
import org.bukkit.entity.Item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum MineBlock {
    STONE(Items.STONE, null, Material.STONE, 1),
    COAL(Items.COAL, Arrays.asList(Drop.GEMSTONE), Material.COAL_ORE, 5),
    IRON(Items.IRON_INGOT, Arrays.asList(Drop.GEMSTONE), Material.IRON_ORE, 5),
    GOLD(Items.GOLD_INGOT, Arrays.asList(Drop.PREMIUM_GOLD_EXTRACT, Drop.GEMSTONE), Material.GOLD_ORE, 5),
    REDSTONE(Items.REDSTONE, Arrays.asList(Drop.GEMSTONE), Material.REDSTONE_ORE, 5),
    LAPIS_LAZULI(Items.LAPIS_LAZULI, Arrays.asList(Drop.GEMSTONE), Material.LAPIS_ORE, 8),
    DIAMOND(Items.DIAMOND, Arrays.asList(Drop.GEMSTONE), Material.DIAMOND_ORE, 10),
    EMERALD(Items.EMERALD, Arrays.asList(Drop.GEMSTONE), Material.EMERALD_ORE, 12),
    OBSIDIAN(Items.OBSIDIAN, Arrays.asList(Drop.GEMSTONE), Material.OBSIDIAN, 50),
    CRYSTALITE(Items.CRYSTALLITE, Arrays.asList(Drop.GEMSTONE), Material.WHITE_STAINED_GLASS, 14),
    NETHERRRACK(Items.NETHERRACK, Arrays.asList(Drop.GEMSTONE), Material.NETHERRACK, 1),
    END_STONE(Items.END_STONE, Arrays.asList(Drop.GEMSTONE), Material.END_STONE, 16);
    private Items commonDrop;

    private Material blockObtainedFrom;
    private int xpObtained;
    private List<Drop> drops;
    MineBlock(Items commonDrop, List<Drop> drops, Material blockObtainedFrom, int xpObtained) {
        this.commonDrop = commonDrop;
        this.drops = drops;
        this.blockObtainedFrom = blockObtainedFrom;
        this.xpObtained = xpObtained;
    }
    public List<Drop> getDrops() { return drops; }
    public Items getCommonDrop() { return commonDrop; }
    public Material getBlockObtainedFrom() { return blockObtainedFrom; }
    public int getXpObtained() { return xpObtained; }
}
