package com.auxdible.skrpg.items.forage;

import com.auxdible.skrpg.items.forage.forageclass.Apple;
import com.auxdible.skrpg.items.forage.forageclass.MidnightFruit;

import java.util.EnumSet;

public enum ForageType {
    APPLE(new Apple()),
    MIDNIGHT_FRUIT(new MidnightFruit());
    private Forage forage;
    ForageType(Forage forage) {
        this.forage = forage;
    }
    public Forage getForage() { return forage; }
    public static ForageType getForage(String string) {
        for (ForageType forageType : EnumSet.allOf(ForageType.class)) {
            if (forageType.toString().equals(string)) {
                return forageType;
            }
        }
        return null;
    }

}
