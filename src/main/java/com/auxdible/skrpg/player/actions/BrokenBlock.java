package com.auxdible.skrpg.player.actions;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.player.skills.BlockInformation;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;

import java.util.concurrent.BlockingDeque;


public class BrokenBlock {
    private BlockInformation blockInformation;
    private Location location;
    private BlockData blockData;
    private boolean isStem;
    private boolean isCrop;
    private boolean isStone;
    private boolean isAir;
    private SKRPG skrpg;
    public BrokenBlock(SKRPG skrpg, BlockInformation blockInformation, BlockData blockData, Location location, boolean isStem, boolean isCrop, boolean isStone, boolean isAir) {
        this.blockInformation = blockInformation;
        this.location = location;
        this.isStem = isStem;
        this.isCrop = isCrop;
        this.isStone = isStone;
        this.isAir = isAir;
        this.blockData = blockData;
        this.skrpg = skrpg;
        delete();
    }

    public BlockInformation getBlockData() { return blockInformation; }

    public boolean isCrop() { return isCrop; }

    public boolean isAir() { return isAir; }

    public boolean isStem() { return isStem; }

    public boolean isStone() { return isStone; }

    public Location getLocation() { return location; }

    public void delete() {
        if (isAir || isCrop || isStem) {
            location.getBlock().setBlockData(Material.AIR.createBlockData());
        }
        if (isStone) {
            location.getBlock().setBlockData(Material.BEDROCK.createBlockData());
        }
        if (!skrpg.getBrokenBlocks().containsKey(this)) {
            skrpg.getBrokenBlocks().put(this, 45);
        }
    }
    public void create() {
        if (isAir || isStone) {
            if (blockInformation == BlockInformation.CANE) {
                return;
            }
            location.getBlock().setBlockData(blockData);

        }
        if (isCrop) {
            if (blockInformation == BlockInformation.CANE) {
                return;
            }
            location.getBlock().setBlockData(blockData);
            if (blockData instanceof Ageable) {
                Ageable ageable = (Ageable) blockData;
                ageable.setAge(ageable.getMaximumAge());
            }
        }
        if (isStem) {
            if (blockInformation == BlockInformation.CANE) {
                if (location.getBlock().getRelative(0, -1, 0).getType() != Material.AIR &&
                        location.getBlock().getRelative(0, -1, 0).getType() != Material.SUGAR_CANE) {

                    for (int y = 0; y <= 2; y++) {
                        Block toCheck = location.getBlock().getRelative(0, y, 0);
                        if (toCheck.getType() == Material.AIR) {
                            toCheck.setType(Material.SUGAR_CANE);
                        }

                    }
                }

            }
        }
    }
}
