package com.auxdible.skrpg.items.forage.forageclass;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.items.Items;
import com.auxdible.skrpg.items.forage.Forage;
import com.auxdible.skrpg.utils.ItemBuilder;
import com.auxdible.skrpg.utils.ItemTweaker;
import com.auxdible.skrpg.utils.Text;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MidnightFruit implements Forage {
    private Entity entity;
    private ArmorStand clickStand;
    public MidnightFruit() {
    }

    @Override
    public void onSpawn(SKRPG skrpg, Location location) {
        ArmorStand armorStand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        this.entity = armorStand;
        armorStand.setInvisible(true);
        armorStand.setInvulnerable(true);
        armorStand.setCustomName(Text.color("&aMidnight Fruit"));
        armorStand.setCustomNameVisible(true);
        armorStand.setHeadPose(new EulerAngle(0, Math.toRadians(new Random().nextInt(361)),0));
        armorStand.getEquipment().setHelmet(ItemTweaker.createPlayerHeadFromData("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWZkOTYxMzQ5M2RjNGZhMmM4ZmI0OWEzNmQ2ZjJkY2U5ZWZhOGU5OGE3M2MwYTU3MDE1ZjNlMmE3NTZkMTkifX19",
                "5329c7f8-80df-431a-8b2c-62e9abb191af"));
        armorStand.getLocation().subtract(0.0, 0.5, 0.0);
    }

    @Override
    public void onRemove(SKRPG skrpg, Location location) {
        if (entity != null) {
            entity.remove();
            if (clickStand != null) {
                clickStand.remove();
            }

        }
    }

    public void setClickStand(ArmorStand clickStand) { this.clickStand = clickStand; }

    @Override
    public Items itemDrop() {
        return Items.LOW_QUALITY_FRUIT;
    }

    @Override
    public Entity clickEntity() {
        return entity;
    }

    @Override
    public ArmorStand clickStand() {
        return clickStand;
    }

    @Override
    public List<Double> expEarned() {
        return Arrays.asList(0.0, 0.0, 75.0, 0.0, 0.0);
    }
}
