package com.auxdible.skrpg.mobs;


import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.items.ItemInfo;
import com.auxdible.skrpg.items.ItemType;
import com.auxdible.skrpg.items.Items;
import com.auxdible.skrpg.items.enchantments.Enchantments;
import com.auxdible.skrpg.player.PlayerData;
import com.auxdible.skrpg.player.collections.Collection;
import com.auxdible.skrpg.player.collections.CollectionType;
import com.auxdible.skrpg.player.collections.Tiers;
import com.auxdible.skrpg.player.skills.Drop;
import com.auxdible.skrpg.player.skills.Level;
import com.auxdible.skrpg.player.skills.MobKill;
import com.auxdible.skrpg.utils.Text;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.EnumSet;
import java.util.StringJoiner;

public class Mob {
    private MobType mobType;
    private int currentHP;
    private Entity ent;
    public Mob(MobType mobType, Entity ent) {
        this.mobType = mobType;
        this.currentHP = mobType.getMaxHP();
        this.ent = ent;
    }

    public Entity getEnt() { return ent; }
    public int getCurrentHP() { return currentHP; }
    public MobType getMobType() { return mobType; }
    public void damage(Player player, int damage, SKRPG skrpg) {
        PlayerData playerData = skrpg.getPlayerManager().getPlayerData(player.getUniqueId());
        ItemInfo itemInfo = ItemInfo.parseItemInfo(player.getInventory().getItemInMainHand());
        damage = damage + 30;
        double bonusDamage = 1 + (Integer.parseInt(playerData.getCombat().getLevel().toString()
                .replace("_", "")) * 0.02);
        damage = (int) Math.round(damage * bonusDamage);
        int nerfedDamage;
        if (damage * Math.round((getMobType().getDefence() /
                (getMobType().getDefence() + 100))) == 0) {
            nerfedDamage = damage;
        } else {
            nerfedDamage = (damage * Math.round(getMobType().getDefence() /
                    (getMobType().getDefence() + 100)));
        }
        if (player.getInventory().getItemInMainHand().getType() != Material.AIR && player.getInventory().getItemInMainHand().getItemMeta().getDisplayName()
                .contains("Zombie Sword")) {
            if (getEnt().getType().equals(EntityType.ZOMBIE) && getMobType() != MobType.CRAB_KING) {
                nerfedDamage = (int) Math.round(nerfedDamage * 1.5);
            }
        }
        if (player.getInventory().getHelmet() != null && player.getInventory().getHelmet().getType() != Material.AIR) {
            if (player.getInventory().getHelmet().getItemMeta().getDisplayName().contains("Crab Crown")) {
                nerfedDamage = nerfedDamage * 2;
            }

        }
        StringJoiner enchantmentSymbols = new StringJoiner(" ");
        if (itemInfo != null) {
            if (itemInfo.hasEnchantment(Enchantments.SHARPNESS)) {
                nerfedDamage = (int) Math.round(nerfedDamage * (1 + (0.07 * itemInfo.getEnchantment(Enchantments.SHARPNESS).getLevel())));
                enchantmentSymbols.add(Text.color("&5⚔"));
            } else if (itemInfo.hasEnchantment(Enchantments.POWER)) {
                nerfedDamage = (int) Math.round(nerfedDamage * (1 + (0.07 * itemInfo.getEnchantment(Enchantments.POWER).getLevel())));
                enchantmentSymbols.add(Text.color("&5↑"));
            }
        }
        if (skrpg.getScrollBossManager().getBoss(this) != null) {
            skrpg.getScrollBossManager().getBoss(this).damage(nerfedDamage, player);
        }

        if (getCurrentHP() - nerfedDamage < 0) {
            setCurrentHP(0);
        } else {
            setCurrentHP(getCurrentHP() - nerfedDamage);
        }

        ArmorStand damageIndictator = (ArmorStand) getEnt().getWorld()
                .spawnEntity(getEnt().getLocation(), EntityType.ARMOR_STAND);
        damageIndictator.setInvulnerable(true);
        damageIndictator.setCollidable(false);
        damageIndictator.setInvisible(true);
        damageIndictator.setCustomName(Text.color("&c" + nerfedDamage + " &4&l☄ " + enchantmentSymbols.toString()));
        damageIndictator.setCustomNameVisible(true);
        damageIndictator.setSmall(true);
        damageIndictator.setVelocity(new Vector(0, 0.5, 0));
        new BukkitRunnable() {
            int quarterSecondsAlive = 0;

            @Override
            public void run() {
                quarterSecondsAlive++;
                if (quarterSecondsAlive == 8) {
                    damageIndictator.remove();
                    cancel();
                }
            }
        }.runTaskTimer(skrpg, 0, 5);
        if (getCurrentHP() <= 0) {
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 2.0f);
            playerData.addRunicPoints(mobType.getLevel(), skrpg);
            for (MobKill mobKill : EnumSet.allOf(MobKill.class)) {
                if (getMobType().getName().equals(mobKill.getMobType().getName())) {
                    if (mobKill.getDrop() != null) {
                        player.getInventory().addItem(Items.buildItem(mobKill.getDrop()));
                    }
                    for (Collection collection : playerData.getCollections()) {
                        if (collection.getCollectionType().getItem().equals(mobKill.getDrop())) {
                            collection.setCollectionAmount(collection.getCollectionAmount() + 1);
                            collection.levelUpCollection(player, playerData);
                        }
                    }
                    if (mobKill.getRareDrops() != null) {
                        double random = Math.random();
                        if (mobKill.getRareDrops() != null) {
                            for (Drop drop : mobKill.getRareDrops()) {
                                if (drop.getChance() >= random) {
                                    player.getInventory().addItem(Items.buildItem(drop.getItems()));
                                    Text.applyText(player, "&r&5&lSPECIAL DROP! &r&8| " + drop.getItems()
                                            .getRarity().getColor() + drop.getItems().getName());
                                    Player finalPlayer = player;
                                    new BukkitRunnable() {
                                        int seconds = 1;

                                        @Override
                                        public void run() {
                                            if (seconds == 1) {
                                                finalPlayer.playSound(finalPlayer.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1.0f, 1.0f);
                                            } else if (seconds == 2) {
                                                finalPlayer.playSound(finalPlayer.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1.0f, 0.5f);
                                            } else if (seconds == 3) {
                                                finalPlayer.playSound(finalPlayer.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1.0f, 2.0f);
                                                cancel();
                                            }
                                            seconds++;
                                        }
                                    }.runTaskTimer(skrpg, 0, 4);
                                }
                            }
                        }
                    }
                    playerData.getCombat().setXpTillNext(playerData.getCombat().getXpTillNext() +
                            mobKill.getXpGiven());
                    playerData.getCombat().setTotalXP(playerData.getCombat().getTotalXP()
                            + mobKill.getXpGiven());
                    playerData.getCombat().levelUpSkill(player, playerData, skrpg);
                    if (playerData.getCombat().getLevel() != Level._50) {
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                                Text.color("&e+ " + mobKill.getXpGiven() + " Combat XP (" + playerData.getCombat()
                                        .getXpTillNext() + "/" + Level.valueOf("_" +
                                        (Integer.parseInt(playerData.getCombat().getLevel().toString()
                                                .replace("_", "")) + 1)).getXpRequired() + ")")
                        ));
                    }

                }
            }

            LivingEntity deathEntity = (LivingEntity) getEnt().getWorld().spawnEntity(getEnt()
                    .getLocation(), getEnt().getType());
            if (deathEntity instanceof Zombie) {
                ((Zombie) deathEntity).setBaby(false);
            }
            for (MobSpawn mobSpawn : skrpg.getMobSpawnManager().getMobSpawns()) {
                if (mobSpawn.getCurrentlySpawnedMobs() != null && mobSpawn.getCurrentlySpawnedMobs().size() != 0) {
                    if (mobSpawn.getCurrentlySpawnedMobs().contains(this)) {
                        mobSpawn.getCurrentlySpawnedMobs().remove(this);
                    }

                }

            }
            getEnt().remove();
            deathEntity.setHealth(0.0);
            skrpg.getMobManager().removeMob(this);
            skrpg.getLogger().info("Removed mob!");
            playerData.setCredits(playerData.getCredits() + getMobType().getLevel());
        }

    }
    public void setCurrentHP (int currentHP) {
        this.currentHP = currentHP;
        ent.setCustomName(Text.color("&7&l☠&e" + getMobType().getLevel() + " &r&8" + getMobType().getName() + " " + "&c" +
                getCurrentHP() + "♥"));
    }
}
