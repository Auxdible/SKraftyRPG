package com.auxdible.skrpg.mobs.npcs;

import com.auxdible.skrpg.mobs.npcs.npcs.*;

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
    STONE_FORGER(StoneForger.class),
    OLD_MINER(OldMiner.class),
    FARMER_JOE(FarmerJoe.class),
    ARBOL(Arbol.class),
    BARTENDER(Bartender.class),
    VILLAGER(VillagerNPC.class);

    private Class npc;

    NpcType(Class npc) {
        this.npc = npc;
    }

    public Class getNpc() {
        return npc;
    }
}
