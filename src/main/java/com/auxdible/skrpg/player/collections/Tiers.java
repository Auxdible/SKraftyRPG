package com.auxdible.skrpg.player.collections;

public enum Tiers {
    _0(0),
    _1(50),
    _2(100),
    _3(250),
    _4(1000),
    _5(2500),
    _6(5000),
    _7(10000),
    _8(25000),
    _9(50000),
    _10(75000);
    private int amountRequired;
    Tiers(int amountRequired) {
        this.amountRequired = amountRequired;
    }
    public int getAmountRequired() { return amountRequired; }
}
