package com.auxdible.skrpg.items;

import com.auxdible.skrpg.items.ItemInfo;
import com.auxdible.skrpg.items.Items;

public class SKRPGItemStack {
    private ItemInfo itemInfo;
    private int amount;
    public SKRPGItemStack(ItemInfo itemInfo, int amount) {
        this.itemInfo = itemInfo;
        this.amount = amount;
    }
    public ItemInfo getItemInfo() {
        if (itemInfo != null) {
            return itemInfo;
        } else {
            return null;
        }
        }
    public int getAmount() { return amount; }

    public void setAmount(int i) {
        this.amount = i;
    }
}
