package com.auxdible.skrpg.player.skills;

import com.auxdible.skrpg.items.Items;
import org.bukkit.Material;
import org.bukkit.entity.Item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum MineBlock {
    STONE(Items.STONE, null, Material.STONE, 1),
    COAL(Items.COAL, null, Material.COAL_ORE, 5),
    IRON(Items.IRON_INGOT, null, Material.IRON_ORE, 5),
    GOLD(Items.GOLD_INGOT, null, Material.GOLD_ORE, 5),
    REDSTONE(Items.REDSTONE, null, Material.REDSTONE_ORE, 7),
    LAPIS_LAZULI(Items.LAPIS_LAZULI, null, Material.LAPIS_ORE, 8),
    DIAMOND(Items.DIAMOND, null, Material.DIAMOND_ORE, 10),
    EMERALD(Items.EMERALD, null, Material.EMERALD_ORE, 10),
    OBSIDIAN(Items.OBSIDIAN, null, Material.OBSIDIAN, 50),
    CRYSTALITE(Items.CRYSTALLITE, null, Material.WHITE_STAINED_GLASS, 25),
    NETHERRRACK(Items.NETHERRACK, null, Material.NETHERRACK, 2),
    END_STONE(Items.END_STONE, null, Material.END_STONE, 27),
    CORRUPTED_ROOT(Items.CORRUPTED_ROOT, null, Material.MYCELIUM, 30);
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
