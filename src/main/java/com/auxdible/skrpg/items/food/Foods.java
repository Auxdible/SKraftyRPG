package com.auxdible.skrpg.items.food;

import com.auxdible.skrpg.items.ItemInfo;
import com.auxdible.skrpg.items.Items;
import com.auxdible.skrpg.items.food.foodactions.FoodActions;
import org.bukkit.inventory.ItemStack;

import java.util.EnumSet;

public enum Foods {
    BERRYFRUIT(Items.BERRYFRUIT, FoodActions.REGENERATION_FOOD_ACTION, "Berryfruit", Items.SWEET_BERRIES,
             FoodBase.LOW_QUALITY_FRUIT, true, false);
    private Items foodItem;
    private FoodActions foodAction;
    private String name;
    private Items appliedItem;
    private boolean processed;
    private boolean cooked;
    private FoodBase originalBase;
    Foods(Items foodItem, FoodActions foodAction, String name, Items appliedItem, FoodBase originalBase,
          boolean processed, boolean cooked) {
        this.foodItem = foodItem;
        this.foodAction = foodAction;
        this.name = name;
        this.appliedItem = appliedItem;
        this.originalBase = originalBase;
        this.processed = processed;
        this.cooked = cooked;
    }

    public FoodActions getFoodAction() { return foodAction; }

    public FoodBase getOriginalBase() { return originalBase; }

    public boolean isCooked() { return cooked; }

    public boolean isProcessed() { return processed; }

    public Items getAppliedItem() { return appliedItem; }
    public Items getFoodItem() { return foodItem; }
    public String getName() { return name; }
    public static Foods getFood(Items items) {
        for (Foods foods : EnumSet.allOf(Foods.class)) {
            if (foods.getFoodItem() == items) {
                return foods;
            }
        }
        return null;
    }
    public static void updateFood(ItemStack itemStack) {
        ItemInfo itemInfo = ItemInfo.parseItemInfo(itemStack);
        if (itemInfo == null) { return; }
        Foods foods = getFood(itemInfo.getItem());
        if (foods == null) { return; }
        itemInfo.setQuality(foods.getOriginalBase().getQuality());
        Items.updateItem(itemStack, itemInfo);
    }
}
