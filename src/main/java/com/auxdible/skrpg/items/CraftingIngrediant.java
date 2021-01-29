package com.auxdible.skrpg.items;

public class CraftingIngrediant {
    private Items items;
    private int amount;
    public CraftingIngrediant(Items items, int amount) {
        this.items = items;
        this.amount = amount;
    }
    public Items getItems() { return items; }
    public int getAmount() { return amount; }
}
