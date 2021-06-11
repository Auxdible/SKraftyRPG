package com.auxdible.skrpg.npcs;


import com.auxdible.skrpg.npcs.npcs.*;
import com.auxdible.skrpg.npcs.npcs.monolith.*;

public enum NpcType {
    BANKER(Banker.class), WEAPON_FORGER_SALESMAN(WeaponForgerSalesman.class),
    TUTORIAL_NPC_VILLAGER(TutorialNPCVillager.class),
    CITY_GUARD(CityGuard.class),
    BEACHMASTER(Beachmaster.class),
    HILTCRAFTER(Hiltcrafter.class),
    FARMER_SELLER(FarmerSeller.class),
    COMBAT_SELLER(CombatSeller.class),
    WOODCUTTING_SELLER(WoodcuttingSeller.class),
    MINING_SELLER(MiningSeller.class),
    FISHING_SELLER(FishingSeller.class),
    STONE_FORGER(StoneForger.class),
    OLD_MINER(OldMiner.class),
    FARMER_JOE(FarmerJoe.class),
    ARBOL(Arbol.class),
    BARTENDER(Bartender.class),
    VILLAGER(VillagerNPC.class),
    LORD_BRYAN(LordBryan.class),
    FISHERMAN(Fisherman.class),
    HEROLD(Herold.class),
    // MONOLITH NPCS
    TOTEM1(Totem1.class),
    TOTEM2(Totem2.class),
    TOTEM3(Totem3.class),
    TOTEM4(Totem4.class),
    FORGOTTEN_SWORD(ForgottenSword.class),
    RUGGED_AXE(RuggedAxe.class),
    CRAFTERS_WORKBENCH(CraftersWorkbench.class),
    MONOLITH_DOOR(MonolithDoor.class);

    private Class npc;

    NpcType(Class npc) {
        this.npc = npc;
    }

    public Class getNpc() {
        return npc;
    }
}
