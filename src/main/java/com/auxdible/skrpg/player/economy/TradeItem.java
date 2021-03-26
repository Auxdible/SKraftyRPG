package com.auxdible.skrpg.player.economy;

import com.auxdible.skrpg.items.ItemInfo;
import com.auxdible.skrpg.items.Items;

public class TradeItem {
    private ItemInfo itemInfo;
    private int amount;
    public TradeItem(ItemInfo itemInfo, int amount) {
        this.itemInfo = itemInfo;
        this.amount = amount;
    }
    public ItemInfo getItemInfo() { return itemInfo; }
    public int getAmount() { return amount; }
}
