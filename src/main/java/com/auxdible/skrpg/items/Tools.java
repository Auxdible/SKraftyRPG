package com.auxdible.skrpg.items;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

public enum Tools {
    WOODEN_PICKAXE(0, Items.WOODEN_PICKAXE, ToolType.PICKAXE),
    WOODEN_AXE(0, Items.WOODEN_AXE, ToolType.AXE),
    WOODEN_HOE(0, Items.WOODEN_HOE, ToolType.HOE),
    STONE_PICKAXE(1, Items.STONE_PICKAXE, ToolType.PICKAXE),
    STONE_AXE(1, Items.STONE_AXE, ToolType.AXE),
    STONE_HOE(1, Items.STONE_HOE, ToolType.HOE),
    IRON_PICKAXE(2, Items.IRON_PICKAXE, ToolType.PICKAXE),
    IRON_AXE(2, Items.IRON_AXE, ToolType.AXE),
    IRON_HOE(2, Items.IRON_HOE, ToolType.HOE),
    GOLDEN_PICKAXE(3, Items.GOLDEN_PICKAXE, ToolType.PICKAXE),
    GOLDEN_AXE(3, Items.GOLDEN_AXE, ToolType.AXE),
    GOLDEN_HOE(3, Items.GOLDEN_HOE, ToolType.HOE),
    DIAMOND_PICKAXE(4, Items.DIAMOND_PICKAXE, ToolType.PICKAXE),
    DIAMOND_AXE(4, Items.DIAMOND_AXE, ToolType.AXE),
    DIAMOND_HOE(4, Items.DIAMOND_HOE, ToolType.HOE);
    private int priority;
    private Items item;
    Tools(int priority, Items items, ToolType toolType) {
        this.priority = priority;
        this.item = items;
    }

    public int getPriority() { return priority; }

    public Items getItem() { return item; }

    public static Tools getTool(Items item) {
        for (Tools tools : EnumSet.allOf(Tools.class)) {
            if (tools.getItem().equals(item)) {
                return tools;
            }
        }
        return null;
    }
}
