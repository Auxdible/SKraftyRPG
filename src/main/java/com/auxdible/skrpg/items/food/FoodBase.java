package com.auxdible.skrpg.items.food;

import com.auxdible.skrpg.items.ItemInfo;
import com.auxdible.skrpg.items.Items;
import com.auxdible.skrpg.items.Quality;
import com.auxdible.skrpg.player.skills.Level;
import org.bukkit.inventory.ItemStack;

import java.util.EnumSet;

public enum FoodBase {
    LOW_QUALITY_FRUIT(Items.LOW_QUALITY_FRUIT, Quality.STAR1, 2);
    private Items foodBaseItem;
    private Quality quality;
    private int farmingLevelRequired;
    FoodBase(Items foodBaseItem, Quality quality, int farmingLevelRequired) {
        this.foodBaseItem = foodBaseItem;
        this.quality = quality;
        this.farmingLevelRequired = farmingLevelRequired;
    }

    public int getFarmingLevelRequired() { return farmingLevelRequired; }
    public Items getFoodBaseItem() { return foodBaseItem; }
    public Quality getQuality() { return quality; }
    public static FoodBase getFood(Items items) {
        for (FoodBase foods : EnumSet.allOf(FoodBase.class)) {
            if (foods.getFoodBaseItem() == items) {
                return foods;
            }
        }
        return null;
    }
    public static void updateFoodBase(ItemStack itemStack) {
        ItemInfo itemInfo = ItemInfo.parseItemInfo(itemStack);
        if (itemInfo == null) { return; }
        FoodBase foodBase = getFood(itemInfo.getItem());
        if (foodBase == null) { return; }
        itemInfo.setQuality(foodBase.getQuality());
        Items.updateItem(itemStack, itemInfo);
    }
}
