package com.auxdible.skrpg.mobs.boss.scrollboss;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.items.ItemInfo;
import com.auxdible.skrpg.items.Items;
import com.auxdible.skrpg.mobs.DamageType;
import com.auxdible.skrpg.mobs.Mob;
import com.auxdible.skrpg.mobs.MobType;
import com.auxdible.skrpg.player.PlayerData;
import com.auxdible.skrpg.utils.Text;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

public class ValissaArachneScrollBoss implements ScrollBoss {
    private SKRPG skrpg;
    private HashMap<Player, Integer> damageTotal;
    private Mob mob;
    private Player scrollSpawner;
    private Location spawnLocation;
    private Player lastDamager;
    private boolean isDead;
    public ValissaArachneScrollBoss(Location spawnLocation, Player scrollSpawner, SKRPG skrpg) {
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
                spawnLocation.getWorld().playSound(spawnLocation, Sound.ENTITY_SPIDER_DEATH, 1.0f, 0.2f);
                for (Player p : Bukkit.getOnlinePlayers()) {
                    Text.applyText(p, "&4&l! &r&8&l| &r&cValissa Arachne &7has spawned!");
                    Text.applyText(p, "&4&lValissa Arachne &r&8&l| &r&cIt is I! Queen Valissa Arachne, feared by all things! Take me on.");
                }

                mob = MobType.buildMob(bossMobType().getId(), skrpg, spawnLocation);
                new BukkitRunnable() {

                    @Override
                    public void run() {
                        if (isDead) {
                            cancel();
                            return;
                        }
                        if (mob == null) {
                            cancel();
                            return;
                        }
                        if (mob.getEnt() == null) {
                            cancel();
                            return;
                        }
                        if (skrpg.getMobManager().getMobData(mob.getEnt()) == null) {
                            cancel();
                            return;
                        }
                        Random random = new Random();
                        int attack = random.nextInt(2);
                        if (attack == 0) {
                            if (lastDamager != null) {
                                mob.getEnt().teleport(lastDamager.getLocation());
                                PlayerData damagedData = skrpg.getPlayerManager().getPlayerData(lastDamager.getUniqueId());
                                Text.applyText(lastDamager, "&4&lValissa Arachne &r&8&l| &r&cTake my poison! For you are a mere human, instead of a spider like me!");
                                new BukkitRunnable() {
                                    int halfSeconds = 0;

                                    @Override
                                    public void run() {
                                        if (halfSeconds == 40) {
                                            cancel();
                                        }
                                        lastDamager.damage(0.1f);
                                        damagedData.getPlayerActionManager().damagePlayer(20, DamageType.TRUE);
                                        lastDamager.playSound(lastDamager.getLocation(), Sound.ENTITY_SPIDER_HURT, 1.0f, 0.2f);
                                        halfSeconds++;
                                    }
                                }.runTaskTimer(skrpg, 0, 10);
                            }
                        } else if (attack == 1) {
                            Monster monsterEnt = (Monster) mob.getEnt();
                            if (lastDamager != null) {
                                monsterEnt.teleport(lastDamager.getLocation());
                                monsterEnt.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, monsterEnt.getLocation(), 3);
                                monsterEnt.getWorld().playSound(monsterEnt.getLocation(), Sound.ENTITY_SPIDER_HURT, 1.0f, 0.2f);
                                monsterEnt.getWorld().playSound(monsterEnt.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 2.0f);
                                HashMap<Location, BlockData> blockLocations = new HashMap<>();
                                Text.applyText(lastDamager, "&4&lValissa Arachne &r&8&l| &r&cHaha! Stay trapped in my webs! Your end is HERE!");
                                new BukkitRunnable() {
                                    int ticks = 0;

                                    @Override
                                    public void run() {
                                        if (ticks == 80) {
                                            for (Location location : blockLocations.keySet()) {
                                                if (blockLocations.get(location) == null) {
                                                    location.getBlock().setType(Material.AIR);
                                                }
                                                location.getBlock().setBlockData(blockLocations.get(location));
                                            }
                                            cancel();
                                        } else {
                                            if (monsterEnt.getLocation().getBlock().getType() != Material.COBWEB) {
                                                blockLocations.put(monsterEnt.getLocation(), monsterEnt.getLocation().getBlock().getBlockData());
                                                monsterEnt.getLocation().getBlock().setType(Material.COBWEB);
                                            }
                                        }

                                        ticks++;
                                    }
                                }.runTaskTimer(skrpg, 0, 1);
                                monsterEnt.setVelocity(monsterEnt.getLocation().getDirection().multiply(2));

                            }
                        }
                        if (mob.getEnt().getLocation().getBlock().getType().equals(Material.COBWEB)) {
                            if (lastDamager != null) {
                                mob.getEnt().teleport(lastDamager);
                                lastDamager.setVelocity(new Vector(0, 0.5, 0));
                            }

                        }
                    }

                }.runTaskTimer(skrpg, 0, 400);
            }
        }.runTaskLater(skrpg, 200);

    }
    @Override
    public MobType bossMobType() {
        return MobType.VALISSA_ARACHNE;
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
            Text.applyText(lastDamager, "&4&lValissa Arachne &r&8&l| &r&7You may have defeated me... but my fellow undead will avenge me! &7Be prepared, adventurer.");
            skrpg.getScrollBossManager().removeBoss(skrpg.getScrollBossManager().getBoss(mob));
        }

    }

    @Override
    public String name() {
        return "Valissa Arachne";
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
        return Items.PREMIUM_SILK;
    }

    @Override
    public List<Items> getRareItem() {
        return Arrays.asList(Items.SPIDER_BOOTS, Items.SPIDER_BOW);
    }
}
