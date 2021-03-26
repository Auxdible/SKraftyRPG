package com.auxdible.skrpg.items;

import com.auxdible.skrpg.utils.Text;

import java.util.EnumSet;

public enum Quality {
    STAR1(Text.color("&8[&6★&7☆☆&8] "), 1),
    STAR2(Text.color("&8[&6★★&7☆&8] "), 2),
    STAR3(Text.color("&8[&6★★★&8] "), 3),
    PREMIUM(Text.color("&8[&5✯&8] "), 4),
    TOP_QUALITY(Text.color("&8[&c☼&8] "), 5);

    private String coloredName;
    private int star;
    Quality(String coloredName, int star) {
        this.coloredName = coloredName;
        this.star = star;
    }

    public String getColoredName() { return coloredName; }
    public int getStar() { return star; }
    public static Quality getQualityFromStar(int star) {
        for (Quality quality : EnumSet.allOf(Quality.class)) {
            if (quality.getStar() == star) { return quality; }
        }
        return null;
    }
}
