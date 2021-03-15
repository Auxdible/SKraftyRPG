package com.auxdible.skrpg.mobs.npcs;

import com.auxdible.skrpg.items.Items;
import com.auxdible.skrpg.mobs.npcs.npcs.*;
import com.auxdible.skrpg.player.economy.Bank;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum NpcType {
    BANKER(Banker.class), WEAPON_FORGER_SALESMAN(WeaponForgerSalesman.class),
    TUTORIAL_NPC_VILLAGER(TutorialNPCVillager.class),
    CITY_GUARD(CityGuard.class),
    BEACHMASTER(Beachmaster.class),
    HILTCRAFTER(Hiltcrafter.class),
    FARMER_SELLER(FarmerSeller.class),
    COMBAT_SELLER(CombatSeller.class),
    WOODCUTTING_SELLER(WoodcuttingSeller.class),
    MINING_SELLER(MiningSeller.class);

    private Class npc;

    NpcType(Class npc) {
        this.npc = npc;
    }

    public Class getNpc() {
        return npc;
    }
}
