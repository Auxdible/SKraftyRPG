package com.auxdible.skrpg.player.collections;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.items.Items;
import com.auxdible.skrpg.player.PlayerData;
import com.auxdible.skrpg.utils.Text;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

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
    public void levelUpCollection(Player player, PlayerData playerData) {
        if (getCollectionAmount() > Tiers.valueOf("_" + (SKRPG.levelToInt(getTier().toString()) + 1)).getAmountRequired()) {
            setTier(Tiers.valueOf("_" + (SKRPG.levelToInt(getTier().toString()) + 1)));
            Text.applyText(player, "&8&m>                                          ");
            Text.applyText(player, "&b&l           TIER UP!");
            Text.applyText(player, " ");
            Text.applyText(player, "&7You tiered up to " + getCollectionType().getItem().getName() + " &b" +
                    Integer.parseInt(getTier().toString()));
            Text.applyText(player, "&7Recipe Unlocked: " + CollectionType.generateRewardsMap(getCollectionType()).get(getTier()).getRarity().getColor() + CollectionType.generateRewardsMap(getCollectionType()).get(getTier()).getName());
            Text.applyText(player, "&8&m>                                          ");
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 0.2f);
        }

    }
    public Tiers getTier() { return tier; }
}
