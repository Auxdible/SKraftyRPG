package com.auxdible.skrpg.player.collections;

import com.auxdible.skrpg.items.Items;

import java.util.ArrayList;
import java.util.HashMap;

public class Collection {
    private int collectionAmount;
    private Tiers tier;
    private CollectionType collectionType;
    public Collection(int collectionAmount, Tiers tier, CollectionType collectionType) {
        this.collectionAmount = collectionAmount;
        this.tier = tier;
        this.collectionType = collectionType;
    }

    public CollectionType getCollectionType() { return collectionType; }
    public int getCollectionAmount() { return collectionAmount; }
    public void setCollectionAmount(int collectionAmount) { this.collectionAmount = collectionAmount; }
    public void setTier(Tiers tier) { this.tier = tier; }

    public Tiers getTier() { return tier; }
}
