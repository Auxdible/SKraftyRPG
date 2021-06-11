package com.auxdible.skrpg.items.abilities;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.items.ItemInfo;
import com.auxdible.skrpg.items.Items;
import com.auxdible.skrpg.mobs.DamageType;
import com.auxdible.skrpg.mobs.Mob;
import com.auxdible.skrpg.player.PlayerData;
import com.auxdible.skrpg.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class KingStaffAbility implements Ability {
    @Override
    public String getName() {
        return "Royal Launch";
    }
    public void explode(Player p, ArmorStand natureLaunch, SKRPG skrpg) {
        PlayerData playerData = skrpg.getPlayerManager().getPlayerData(p.getUniqueId());
        p.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, natureLaunch.getLocation(), 1);
        p.getWorld().spawnParticle(Particle.BLOCK_CRACK, natureLaunch.getLocation(), 50, Material.GOLD_BLOCK.createBlockData());
        p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 0.2f);
        p.playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 1.5f);
        List<Mob> mobsInRange = new ArrayList<>();
        for (Mob mobs : skrpg.getMobManager().getMobs()) {
            if (mobs.getEnt().getLocation().distance(natureLaunch.getLocation()) <= 5.0) {
                mobsInRange.add(mobs);
            }
        }
        int damage;
        for (Mob mob : mobsInRange) {
            ItemInfo itemInfo = playerData.getPlayerInventory().getItemInMainHand().getItemInfo();
            if (itemInfo != null) {
                damage = playerData.getPlayerActionManager().calculateDamage();


                mob.damage(p, damage, skrpg, DamageType.ENERGETIC);
            }


        }
        natureLaunch.remove();
    }

    @Override
    public void ability(PlayerData playerData, SKRPG skrpg) {
        Player p = Bukkit.getPlayer(playerData.getUuid());
        p.setVelocity(p.getLocation().getDirection().multiply(-1).normalize().divide(new Vector(2, 0, 2)).setY(0.5));
        p.playSound(p.getLocation(), Sound.BLOCK_GRASS_BREAK, 1.0f, 0.2f);
        ArmorStand kingLaunch = (ArmorStand)
                p.getWorld().spawnEntity(p.getLocation(), EntityType.ARMOR_STAND);
        kingLaunch.setInvisible(true);
        kingLaunch.getEquipment().setHelmet(new ItemBuilder(Material.GOLD_BLOCK, 0).asItem());
        kingLaunch.setSmall(true);
        kingLaunch.setInvulnerable(true);
        kingLaunch.setVelocity(p.getLocation().getDirection().setY(0.8));
        new BukkitRunnable() {
            int halfSeconds = 0;
            @Override
            public void run() {
                if (halfSeconds == 6) {
                    explode(p, kingLaunch, skrpg);
                    cancel();
                }
                if (kingLaunch.isOnGround()) {
                    explode(p, kingLaunch, skrpg);
                    cancel();
                }

                halfSeconds++;
            }
        }.runTaskTimer(skrpg, 0, 10);
    }

    @Override
    public int getCost() {
        return 25;
    }

    @Override
    public boolean isAttack() {
        return true;
    }

    @Override
    public Items getItem() {
        return Items.KING_STAFF;
    }
}
