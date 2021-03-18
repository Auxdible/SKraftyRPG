package com.auxdible.skrpg.player.skills;

public enum RunicUpgrades {
    MORE_RP("Runic Points Collector"), REDUCE_ENCHANTING("Enchanting Cost Reduction"),
    REDUCE_RUNIC_STONE("Runic Stone Cost Reduction"), RUNIC_POINTS_DESTROY("Item Destruction Points");

    private String runicUpgradeName;
    RunicUpgrades(String runicUpgradeName) {
        this.runicUpgradeName = runicUpgradeName;
    }
    public String getRunicUpgradeName() { return runicUpgradeName; }
}
