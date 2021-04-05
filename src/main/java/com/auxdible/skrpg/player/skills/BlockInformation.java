package com.auxdible.skrpg.player.skills;

import com.auxdible.skrpg.items.Items;
import com.auxdible.skrpg.items.ToolType;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

public enum BlockInformation {
    STONE(Items.STONE, null, Material.STONE, Arrays.asList(0.0, 1.0, 0.0, 0.0), 0, false, 1, 1, ToolType.PICKAXE, true),
    STONE_1(Items.STONE, null, Material.COBBLESTONE, Arrays.asList(0.0, 1.0, 0.0, 0.0), 0, false, 1, 1, ToolType.PICKAXE, true),
    STONE_2(Items.STONE, null, Material.ANDESITE, Arrays.asList(0.0, 1.0, 0.0, 0.0), 0, false, 1, 1, ToolType.PICKAXE, true),
    STONE_3(Items.STONE, null, Material.POLISHED_ANDESITE, Arrays.asList(0.0, 1.0, 0.0, 0.0), 0, false, 1, 1, ToolType.PICKAXE, true),
    COAL(Items.COAL, Arrays.asList(Drop.GEMSTONE), Material.COAL_ORE, Arrays.asList(0.0, 3.0, 0.0, 0.0), 1, false, 1, 1, ToolType.PICKAXE, true),
    IRON(Items.IRON_INGOT, Arrays.asList(Drop.GEMSTONE), Material.IRON_ORE, Arrays.asList(0.0, 4.0, 0.0, 0.0), 1, false, 1, 1, ToolType.PICKAXE, true),
    GOLD(Items.GOLD_INGOT, Arrays.asList(Drop.PREMIUM_GOLD_EXTRACT, Drop.GEMSTONE), Material.GOLD_ORE, Arrays.asList(0.0, 5.0, 0.0, 0.0), 2, false, 1, 1, ToolType.PICKAXE, true),
    REDSTONE(Items.REDSTONE, Arrays.asList(Drop.GEMSTONE), Material.REDSTONE_ORE, Arrays.asList(0.0, 7.0, 0.0, 0.0), 2, false, 6, 8, ToolType.PICKAXE, true),
    LAPIS_LAZULI(Items.LAPIS_LAZULI, Arrays.asList(Drop.GEMSTONE), Material.LAPIS_ORE, Arrays.asList(0.0, 7.0, 0.0, 0.0), 2, false, 6, 8, ToolType.PICKAXE, true),
    DIAMOND(Items.DIAMOND, Arrays.asList(Drop.GEMSTONE), Material.DIAMOND_ORE, Arrays.asList(0.0, 10.0, 0.0, 0.0), 2, false, 1, 1, ToolType.PICKAXE, true),
    EMERALD(Items.EMERALD, Arrays.asList(Drop.GEMSTONE), Material.EMERALD_ORE, Arrays.asList(0.0, 12.0, 0.0, 0.0), 2, false, 1, 1, ToolType.PICKAXE, true),
    OBSIDIAN(Items.OBSIDIAN, Arrays.asList(Drop.GEMSTONE), Material.OBSIDIAN, Arrays.asList(0.0, 25.0, 0.0, 0.0), 4, false, 1, 1, ToolType.PICKAXE, true),
    CRYSTALITE(Items.CRYSTALLITE, Arrays.asList(Drop.GEMSTONE), Material.WHITE_STAINED_GLASS, Arrays.asList(0.0, 8.0, 0.0, 0.0), 2, false, 1, 1, ToolType.PICKAXE, true),
    NETHERRRACK(Items.NETHERRACK, null, Material.NETHERRACK, Arrays.asList(0.0, 0.1, 0.0, 0.0), 3, false, 1, 1, ToolType.PICKAXE, true),
    END_STONE(Items.END_STONE, Arrays.asList(Drop.GEMSTONE), Material.END_STONE, Arrays.asList(0.0, 16.0, 0.0, 0.0), 2, false, 1, 1, ToolType.PICKAXE, true),
    OAK_LOG(Items.WOOD, null, Material.OAK_LOG, Arrays.asList(0.0, 0.0, 0.0, 6.0), -1, false, 1, 1, null, false),
    BIRCH_LOG(Items.WOOD, null, Material.BIRCH_LOG, Arrays.asList(0.0, 0.0, 0.0, 6.0), -1, false, 1, 1, null, false),
    SPRUCE_LOG(Items.WOOD, null, Material.SPRUCE_LOG, Arrays.asList(0.0, 0.0, 0.0, 6.0), -1, false, 1, 1, null, false),
    DARK_OAK_LOG(Items.WOOD, null, Material.DARK_OAK_LOG, Arrays.asList(0.0, 0.0, 0.0, 6.0), -1, false, 1, 1, null, false),
    ACACIA_LOG(Items.WOOD, null, Material.ACACIA_LOG, Arrays.asList(0.0, 0.0, 0.0, 6.0), -1, false, 1, 1, null, false),
    JUNGLE_LOG(Items.WOOD, null, Material.JUNGLE_LOG, Arrays.asList(0.0, 0.0, 0.0, 6.0), -1, false, 1, 1, null, false),
    OAK_WOOD(Items.WOOD, null, Material.OAK_WOOD, Arrays.asList(0.0, 0.0, 0.0, 6.0), -1, false, 1, 1, null, false),
    BIRCH_WOOD(Items.WOOD, null, Material.BIRCH_WOOD, Arrays.asList(0.0, 0.0, 0.0, 6.0), -1, false, 1, 1, null, false),
    SPRUCE_WOOD(Items.WOOD, null, Material.SPRUCE_WOOD, Arrays.asList(0.0, 0.0, 0.0, 6.0), -1, false, 1, 1, null, false),
    DARK_OAK_WOOD(Items.WOOD, null, Material.DARK_OAK_WOOD, Arrays.asList(0.0, 0.0, 0.0, 6.0), -1, false, 1, 1, null, false),
    ACACIA_WOOD(Items.WOOD, null, Material.ACACIA_WOOD, Arrays.asList(0.0, 0.0, 0.0, 6.0), -1, false, 1, 1, null, false),
    JUNGLE_WOOD(Items.WOOD, null, Material.JUNGLE_WOOD, Arrays.asList(0.0, 0.0, 0.0, 6.0), -1, false, 1, 1, null, false),
    WHEAT(Items.WHEAT, null, Material.WHEAT, Arrays.asList(0.0, 0.0, 1.0, 0.0), -1, true, 1, 1, null, false),
    CARROT(Items.CARROT, null, Material.CARROTS, Arrays.asList(0.0, 0.0, 3.0, 0.0), -1, true, 1, 1, null, false),
    CANE(Items.CANE, null, Material.SUGAR_CANE, Arrays.asList(0.0, 0.0, 2.0, 0.0), -1, true, 1, 1, null, false),
    POTATO(Items.POTATO, null, Material.POTATOES, Arrays.asList(0.0, 0.0, 3.0, 0.0), -1, true, 1, 1, null, false),
    SWEET_BERRIES(Items.SWEET_BERRIES, null, Material.SWEET_BERRY_BUSH, Arrays.asList(0.0, 0.0, 3.0, 0.0), -1, true, 1, 1, null, false),
    BEETROOT(Items.BEETROOT, null, Material.BEETROOTS, Arrays.asList(0.0, 0.0, 4.0, 0.0), -1, true, 1, 1, null, false),
    PUMPKIN(Items.PUMPKIN, null, Material.PUMPKIN, Arrays.asList(0.0, 0.0, 10.0, 0.0), -1, true, 1, 1, null, false),
    MELON(Items.MELON, null, Material.MELON, Arrays.asList(0.0, 0.0, 10.0, 0.0), -1, true, 1, 1, null, false);
    private Items commonDrop;
    private int axePriority;
    private boolean overrideRegion;
    private Material blockObtainedFrom;
    private List<Double> xpObtained;
    private List<Drop> drops;
    private int lowerBoundDropAmount;
    private int upperBoundDropAmount;
    private ToolType toolType;
    private boolean stone;
    BlockInformation(Items commonDrop, List<Drop> drops, Material blockObtainedFrom, List<Double> xpObtained, int axePriority,
                     boolean overridesRegion, int lowerBoundDropAmount, int upperBoundDropAmount, ToolType toolType,
                     boolean stone) {
        this.commonDrop = commonDrop;
        this.drops = drops;
        this.blockObtainedFrom = blockObtainedFrom;
        this.xpObtained = xpObtained;
        this.overrideRegion = overridesRegion;
        this.axePriority = axePriority;
        this.lowerBoundDropAmount = lowerBoundDropAmount;
        this.upperBoundDropAmount = upperBoundDropAmount;
        this.toolType = toolType;
        this.stone = stone;
    }

    public boolean isStone() { return stone; }

    public ToolType getToolType() { return toolType; }
    public List<Drop> getDrops() { return drops; }
    public Items getCommonDrop() { return commonDrop; }
    public Material getBlockObtainedFrom() { return blockObtainedFrom; }
    public List<Double> getXpObtained() { return xpObtained; }

    public int getAxePriority() { return axePriority; }

    public boolean isOverrideRegion() { return overrideRegion; }
    public static BlockInformation getBlockData(Material material) {
        for (BlockInformation blockInformation : EnumSet.allOf(BlockInformation.class)) {
            if (blockInformation.getBlockObtainedFrom() == material) {
                return blockInformation;
            }
        }
        return null;
    }

    public int getLowerBoundDropAmount() { return lowerBoundDropAmount; }

    public int getUpperBoundDropAmount() { return upperBoundDropAmount; }
}
