package com.auxdible.skrpg.mobs.npcs;

import com.auxdible.skrpg.items.Items;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum NpcType {
    PLAYER_EXPORT_MERCHANT, PLAYER_BANKER, WEAPON_FORGER_SALESMAN(Arrays.asList(new PurchasableItem(Items.BASIC_HILT, 50),
            new PurchasableItem(Items.ADVANCED_HILT, 100), new PurchasableItem(Items.STARTER_SWORD, 150), new PurchasableItem(Items.ZOMBIE_SWORD, 300)));
    private List<PurchasableItem> purchasableItems;
    NpcType() {

    }
    NpcType(List<PurchasableItem> purchasableItems) {
        this.purchasableItems = purchasableItems;
    }
    public List<PurchasableItem> getPurchasableItems() { return purchasableItems; }
}
