package com.auxdible.skrpg.mobs.boss.scrollboss;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.items.Items;
import com.auxdible.skrpg.mobs.Mob;
import com.auxdible.skrpg.mobs.MobType;
import com.auxdible.skrpg.utils.Text;
import org.bukkit.*;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class KingCrabScrollBoss implements ScrollBoss {
    private SKRPG skrpg;
    private HashMap<Player, Integer> damageTotal;
    private Mob mob;
    private Player scrollSpawner;
    private Location spawnLocation;
    private Player lastDamager;
    private boolean isDead;
    public KingCrabScrollBoss(Location spawnLocation, Player scrollSpawner, SKRPG skrpg) {
        damageTotal = new HashMap<>();
        this.spawnLocation = spawnLocation;
        this.scrollSpawner = scrollSpawner;
        this.skrpg = skrpg;
        isDead = false;
    }
    @Override
    public HashMap<Player, Integer> damageTotal() {
        return damageTotal;
    }

    @Override
    public void spawnBoss() {
        new BukkitRunnable() {

            @Override
            public void run() {
                spawnLocation.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, spawnLocation, 5);
                spawnLocation.getWorld().playSound(spawnLocation, Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 0.2f);
                spawnLocation.getWorld().playSound(spawnLocation, Sound.ENTITY_SILVERFISH_HURT, 1.0f, 0.2f);
                for (Player p : Bukkit.getOnlinePlayers()) {
                    Text.applyText(p, "&4&l! &r&8&l| &r&7The &cCrab King &7has spawned!");
                }

                mob = MobType.buildMob(bossMobType().getId(), skrpg, spawnLocation);

            }
        }.runTaskLater(skrpg, 200);
        new BukkitRunnable() {

            @Override
            public void run() {
                if (isDead) { cancel(); return; }
                if (mob == null) {
                    cancel();
                    return;
                }
                if (mob.getEnt() == null) { cancel(); return; }
                if (skrpg.getMobManager().getMobData(mob.getEnt()) == null) { cancel(); return; }
                Random random = new Random();
                int attack = random.nextInt(4);
                if (attack == 0) {
                    if (lastDamager != null) {
                        mob.getEnt().teleport(lastDamager.getLocation());
                        mob.getEnt().setVelocity(new Vector(0.0, 1.5, 0.0));
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                new BukkitRunnable() {

                                    @Override
                                    public void run() {
                                        if (mob.getEnt().isOnGround()) {
                                            mob.getEnt().getLocation().getWorld().playSound(mob.getEnt().getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 0.2f);
                                            mob.getEnt().getWorld().spawnParticle(Particle.EXPLOSION_HUGE, mob.getEnt().getLocation(), 3);
                                            for (Player player : Bukkit.getOnlinePlayers()) {
                                                if (mob.getEnt().getLocation().distance(player.getLocation()) <= 7) {
                                                    player.setVelocity(new Vector(0.0, 2.0, 0.0));
                                                    skrpg.getPlayerManager().getPlayerData(player.getUniqueId()).getPlayerActionManager().damagePlayer(1500);
                                                    player.damage(0.1);
                                                    Text.applyText(player, "&4&l! &r&8| &cCrab King &7stomped you for &c1500 Damage&7.");

                                                }
                                            }
                                            cancel();
                                        }
                                    }
                                }.runTaskTimer(skrpg, 0, 4);
                            }

                        }.runTaskLater(skrpg, 4);
                    }
                } else if (attack == 1) {
                    Monster monsterEnt = (Monster) mob.getEnt();
                    if (lastDamager != null) {
                        monsterEnt.teleport(lastDamager.getLocation());
                        monsterEnt.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, monsterEnt.getLocation(), 3);
                        monsterEnt.getWorld().playSound(monsterEnt.getLocation(), Sound.ENTITY_PHANTOM_FLAP, 1.0f, 0.2f);
                        monsterEnt.getWorld().playSound(monsterEnt.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 2.0f);
                        monsterEnt.setVelocity(monsterEnt.getLocation().getDirection().multiply(2));
                    }
                }
                if (mob.getEnt().getLocation().getBlock().getType().equals(Material.WATER)) {
                    if (lastDamager != null) {
                        mob.getEnt().teleport(lastDamager);
                        lastDamager.setVelocity(new Vector(0, 0.5, 0));
                    }

                }
            }
        }.runTaskTimer(skrpg, 0, 160);
    }

    @Override
    public MobType bossMobType() {
        return MobType.CRAB_KING;
    }

    @Override
    public Mob getMob() {
        return mob;
    }

    @Override
    public void damage(int amount, Player player) {
        if (!damageTotal.containsKey(player)) {
            damageTotal.put(player, amount);
        } else {
            damageTotal.put(player, damageTotal.get(player) + amount);
        }
        lastDamager = player;
        if (mob.getCurrentHP() - amount <= 0) {
            skrpg.getScrollBossManager().rewards(damageTotal, this);
            isDead = true;
            skrpg.getScrollBossManager().removeBoss(skrpg.getScrollBossManager().getBoss(mob));
        }

    }

    @Override
    public String name() {
        return "King Crab";
    }

    @Override
    public Player getScrollSpawner() {
        return scrollSpawner;
    }

    @Override
    public Location getSpawnLocation() {
        return spawnLocation;
    }

    @Override
    public Items getCommonItem() {
        return Items.CRAB_FRAGMENT;
    }

    @Override
    public List<Items> getRareItem() {
        return Arrays.asList(Items.CRAB_CROWN, Items.CRAB_CLAW);
    }
}
