package com.auxdible.skrpg.player.guilds.raid;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.player.PlayerData;
import com.auxdible.skrpg.player.guilds.Guild;
import com.auxdible.skrpg.regions.Region;
import com.auxdible.skrpg.utils.Text;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Raid {
    private Player raidInitiator;
    private Guild raidingGuild;
    private Region region;
    private int regionStrength;
    private int maxMobs;
    private ArrayList<RaidMob> raidMobs;
    private LivingEntity raidEnt;
    private ArmorStand raidStand;
    
    private int regionHP;
    private PlayerData playerData;
    private Raid raid;
    public Raid(Player raidInitiator, Guild raidingGuild, Region region) {
        this.raidInitiator = raidInitiator;
        this.raidingGuild = raidingGuild;
        this.region = region;
        if (region.getControllingGuild() != null && region.getControllingGuild().getOwnedRegions().size() != 0) {
            this.regionStrength = (region.getControllingGuild().getPowerLevel() / region.getControllingGuild().getOwnedRegions().size());
        } else {
            this.regionStrength = 5;
        }
        this.regionHP = regionStrength * 1000;
        if (raidingGuild.getPowerLevel() == 0) {
            this.maxMobs = 5;
        } else {
            this.maxMobs = raidingGuild.getPowerLevel() / 2;
            if (this.maxMobs > 50) {
                this.maxMobs = 50;
            }
            if (this.maxMobs < 5) {
                this.maxMobs = 5;
            }
        }

        this.raidMobs = new ArrayList<>();
        this.raidStand = (ArmorStand) region.getBannerLocation().getWorld().spawnEntity(region.getBannerLocation(), EntityType.ARMOR_STAND);
        this.raidStand.teleport(this.raidStand.getLocation().add(0.0, 1.0, 0.0));
        this.raidStand.setCustomName(Text.color("&7Region HP: &c" + regionHP));
        this.raidStand.setCustomNameVisible(true);
        this.raidStand.setInvisible(true);
        this.raidStand.setInvulnerable(true);
        this.raidStand.setGravity(false);
        this.raid = this;
        this.raidEnt = (LivingEntity) region.getBannerLocation().getWorld().spawnEntity(region.getBannerLocation(), EntityType.IRON_GOLEM);
        this.raidEnt.setInvulnerable(true);
        this.raidEnt.setGravity(false);
        this.raidEnt.setAI(false);
        this.raidEnt.setInvisible(true);
        this.raidEnt.setCustomName(Text.color("&7Region HP: &c" + regionHP));
        this.raidEnt.setCustomNameVisible(true);
        this.raidEnt.setCollidable(false);

    }
    public void start(SKRPG skrpg) {
        this.playerData = skrpg.getPlayerManager().getPlayerData(raidInitiator.getUniqueId());
        raidInitiator.teleport(region.getRaidLocation());
        Text.applyText(raidInitiator, "&c&lLet the raid begin!");
        raidInitiator.playSound(raidInitiator.getLocation(), Sound.BLOCK_ANVIL_USE, 1.0f, 0.5f);
        raidInitiator.sendTitle(Text.color("&c&lYOU ARE RAIDING"), Text.color(region.getName()), 20, 20, 20);
        Text.applyText(raidInitiator,"&7Region Defence: &c" + regionStrength);
        Text.applyText(raidInitiator,"&7Region Health: &c" + regionHP);
        raidInitiator.setAllowFlight(true);
        new BukkitRunnable() {
            @Override
            public void run() {
                List<RaidMob> removingMobs = new ArrayList<>();
                if (regionHP <= 0) {
                    cancel();
                    takeOver(skrpg);
                } else if (!skrpg.getRaidManager().getRaids().containsValue(raid)) {
                    cancel();
                }
                if (playerData.getRegion() != region) {
                    raidInitiator.teleport(region.getSpawnLocation());
                    Text.applyText(raidInitiator, "&cGet back to the fight!");
                }
                if (raidMobs != null) {
                    for (RaidMob raidMob : raidMobs) {
                        if (raidMob.getEnt() != null) {
                            if (raidMob.getEnt().getTarget() != raidEnt) {
                                raidMob.getEnt().setTarget(raidEnt);
                            }
                        }
                        if (raidMob.getEnt().getLocation().distance(raidEnt.getLocation()) < 5) {
                            removingMobs.add(raidMob);
                        }
                    }
                    for (RaidMob raidMob : removingMobs) {
                        regionHP = regionHP - raidMob.getRaidMobType().getDamage();
                        raidStand.setCustomName(Text.color("&7Region HP: &c" + regionHP));
                        raidMobs.remove(raidMob);
                        raidMob.getEnt().remove();
                        raidInitiator.playSound(raidInitiator.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 2.0f);
                    }
                }
            }
        }.runTaskTimer(skrpg, 0, 5);
        new BukkitRunnable() {

            @Override
            public void run() {
                List<RaidMob> removingMobs = new ArrayList<>();
                Random random = new Random();
                for (int i = 0; i < raidMobs.size() / 2; i++) {
                    RaidMob raidMob = raidMobs.get(random.nextInt(raidMobs.size()));
                    if (raidMob != null) {
                        if (regionStrength * 20 > 2000) {
                            raidMob.setCurrentHp(raidMob.getCurrentHp() - 2000);
                        } else {
                            raidMob.setCurrentHp(raidMob.getCurrentHp() - regionStrength * 50);
                        }

                        raidMob.getEnt().damage(0.1);
                        raidMob.getEnt().setHealth(raidMob.getEnt().getMaxHealth());
                        if (raidMob.getCurrentHp() <= 0) {
                            removingMobs.add(raidMob);
                        }
                    }
                }
                for (RaidMob raidMob : removingMobs) {
                    raidMobs.remove(raidMob);
                    raidMob.getEnt().remove();
                    raidInitiator.playSound(raidInitiator.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 0.5f);
                }
            }
        }.runTaskTimer(skrpg, 0, 20);
    }
    public void buyMob(RaidMobs raidMobType) {
        if (raidMobs.size() == maxMobs) {
            Text.applyText(raidInitiator, "&cYou already have the max amount of mobs spawned!");
            raidInitiator.playSound(raidInitiator.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
            return;
        }
        Entity ent = region.getRaidLocation().getWorld().spawnEntity(region.getRaidLocation(), raidMobType.getEntityType());
        ent.setCustomName(Text.color("&4&lRAID &r&c" + raidMobType.getName() + " " + raidMobType.getHp() + "&c♥"));
        ent.setCustomNameVisible(true);
        if (ent instanceof Zombie) {
            Zombie zomb = (Zombie) ent;
            zomb.getEquipment().setHelmet(raidMobType.getHelmet());
            zomb.getEquipment().setChestplate(raidMobType.getChestplate());
            zomb.getEquipment().setLeggings(raidMobType.getLeggings());
            zomb.getEquipment().setBoots(raidMobType.getBoots());
            zomb.getEquipment().setItemInMainHand(raidMobType.getItemInHand());
            zomb.setTarget(raidEnt);
            zomb.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(10000.0);
        }
        playerData.setCredits(playerData.getCredits() - raidMobType.getCreditsCost());
        raidMobs.add(new RaidMob((Creature) ent, raidMobType, raidMobType.getHp()));
    }
    public void fail(SKRPG skrpg) {
        for (RaidMob raidMob : raidMobs) {
            raidMob.getEnt().remove();
        }
        raidInitiator.playSound(raidInitiator.getLocation(), Sound.ENTITY_BLAZE_DEATH, 1.0f, 0.5f);
        raidEnt.remove();
        raidStand.remove();
        raidInitiator.sendTitle(Text.color("&c&lFAILURE"), Text.color("&cYou failed to raid " + region.getName() + "&c!"), 20, 20, 20);
        skrpg.getRaidManager().removeRaid(raidInitiator);
        raidInitiator.setAllowFlight(false);
    }
    public void takeOver(SKRPG skrpg) {
        for (RaidMob raidMob : raidMobs) {
            raidMob.getEnt().remove();
        }
        raidInitiator.playSound(raidInitiator.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 0.5f);
        raidEnt.remove();
        raidStand.remove();
        raidInitiator.sendTitle(Text.color("&a&lSUCCESS"), Text.color("&a&lSUCCESS! &r&aYou took over " + region.getName() + "&a!"), 20, 20, 20);
        skrpg.getRaidManager().removeRaid(raidInitiator);
        if (region.getControllingGuild() != null) {
            region.getControllingGuild().loseRegion(region, raidInitiator);
        }
        region.setControllingGuild(raidingGuild);
        raidingGuild.takeOverRegion(region, raidInitiator);
        raidInitiator.setAllowFlight(false);
    }

    public ArrayList<RaidMob> getRaidMobs() { return raidMobs; }
}
