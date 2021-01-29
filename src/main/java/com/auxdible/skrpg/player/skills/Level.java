package com.auxdible.skrpg.player.skills;

public enum Level {
    _0(0),
    _1(50),
    _2(125),
    _3(200),
    _4(300),
    _5(500),
    _6(750),
    _7(1000),
    _8(1500),
    _9(2000),
    _10(3500),
    _11(5000),
    _12(7500),
    _13(10000),
    _14(15000),
    _15(20000),
    _16(30000),
    _17(50000),
    _18(75000),
    _19(100000),
    _20(200000),
    _21(300000),
    _22(400000),
    _23(500000),
    _24(600000),
    _25(700000);

    private int xpRequired;
    Level(int xpRequired) {
        this.xpRequired = xpRequired;
    }
    public int getXpRequired() { return xpRequired; }
}
