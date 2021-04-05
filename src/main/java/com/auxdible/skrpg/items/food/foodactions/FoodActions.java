package com.auxdible.skrpg.items.food.foodactions;

public enum FoodActions {
    REGENERATION_FOOD_ACTION(new BerryfruitAction()),
    STRENGTH_COLA_ACTION(new StrengthColaAction()),
    OVERLY_CAFFINATED_COFFEE_ACTION(new OverlyCaffinatedCoffeeAction()),
    LEMON_TEA_ACTION(new LemonTeaAction()),
    SUPER_CANDY_ACTION(new SuperCandyAction());
    private FoodAction foodAction;
    FoodActions(FoodAction foodAction) {
        this.foodAction = foodAction;
    }

    public FoodAction getFoodAction() { return foodAction; }
}
