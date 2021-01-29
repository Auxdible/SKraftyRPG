package com.auxdible.skrpg.mobs.npcs;

import com.auxdible.skrpg.items.Items;

public class PurchasableItem {
    private Items item;
    private int cost;
    public PurchasableItem(Items item, int cost) {
        this.item = item;
        this.cost = cost;
    }
    public int getCost() { return cost; }
    public Items getItem() { return item; }
}
