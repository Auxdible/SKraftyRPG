package com.auxdible.skrpg.player.collections;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.items.Items;
import com.auxdible.skrpg.player.PlayerData;
import com.auxdible.skrpg.utils.Text;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
        if (getCollectionAmount() == 1 && tier == Tiers._0) {
            Text.applyText(player, "&a&lCOLLECTION UNLOCKED! &r&8| " + collectionType.getItem().getRarity().getColor() + collectionType.getItem().getName());
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 2.0f);
        }
        if (getCollectionAmount() > Tiers.valueOf("_" + (SKRPG.levelToInt(getTier().toString()) + 1)).getAmountRequired()) {
            setTier(Tiers.valueOf("_" + (SKRPG.levelToInt(getTier().toString()) + 1)));
            HashMap<Tiers, List<Items>> rewardsMap = CollectionType.generateRewardsMap(getCollectionType());
            Text.applyText(player, "&8&m>                                          ");
            Text.applyText(player, "&b&l           TIER UP!");
            Text.applyText(player, " ");
            Text.applyText(player, "&7You tiered up to " + getCollectionType().getItem().getName() + " &b" +
                    Integer.parseInt(getTier().toString().replace("_", "")));

            Text.applyText(player, "&7Recipes Unlocked:");
            for (Items itemLists : rewardsMap.get(getTier())) {
                Text.applyText(player, itemLists.getRarity().getColor() + itemLists.getName());
            }
            Text.applyText(player, "&8&m>                                          ");
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 0.2f);
        }

    }
    public Tiers getTier() { return tier; }
}
