package com.auxdible.skrpg.player.quests.royaltyquests;


import org.bukkit.Material;

public enum RoyaltyQuestType {
    LUMBERJACK("Lumberjack", "&7Obtain &fWood &7by cutting down trees.", 32, Material.STRIPPED_BIRCH_LOG),
    FARMER("Farmer", "&7Obtain &fWheat &7by harvesting it in fields.", 64, Material.WHEAT),
    MINER("Miner", "&7Obtain &fStone &7in the mines.", 32, Material.STONE),
    WARRIOR("Warrior", "&7Obtain &fRotten Flesh &7from Zombies.", 32, Material.ROTTEN_FLESH),
    OBTAIN_EXP_COMBAT("Combatant", "&7Earn Combat XP.", 300, Material.IRON_SWORD),
    OBTAIN_EXP_MINING("Caveman", "&7Earn Mining XP.", 300, Material.IRON_PICKAXE),
    OBTAIN_EXP_WOODCUTTING("Craftsman", "&7Earn Crafting XP.", 300, Material.IRON_AXE),
    OBTAIN_EXP_FARMING("Herbalist", "&7Earn Herbalism XP.", 300, Material.IRON_HOE);
    private String name;
    private String objective;
    private int amountNeeded;
    private Material icon;
    RoyaltyQuestType(String name, String objective, int amountNeeded, Material icon) {
        this.icon = icon;
        this.name = name;
        this.objective = objective;
        this.amountNeeded = amountNeeded;
    }

    public int getAmountNeeded() { return amountNeeded; }

    public String getObjective() { return objective; }

    public String getName() { return name; }

    public Material getIcon() { return icon; }
}
