package com.auxdible.skrpg.items.food.foodactions;

public enum FoodActions {
    REGENERATION_FOOD_ACTION(new BerryfruitAction());
    private FoodAction foodAction;
    FoodActions(FoodAction foodAction) {
        this.foodAction = foodAction;
    }

    public FoodAction getFoodAction() { return foodAction; }
}
