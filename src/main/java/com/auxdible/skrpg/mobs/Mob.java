package com.auxdible.skrpg.mobs;


import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.items.ItemInfo;
import com.auxdible.skrpg.items.Items;
import com.auxdible.skrpg.items.enchantments.Enchantment;
import com.auxdible.skrpg.items.enchantments.Enchantments;
import com.auxdible.skrpg.player.PlayerData;
import com.auxdible.skrpg.player.collections.Collection;
import com.auxdible.skrpg.items.SKRPGItemStack;
import com.auxdible.skrpg.player.quests.Quests;
import com.auxdible.skrpg.player.quests.TutorialQuest;
import com.auxdible.skrpg.player.quests.royaltyquests.RoyaltyQuest;
import com.auxdible.skrpg.player.quests.royaltyquests.RoyaltyQuestType;
import com.auxdible.skrpg.player.skills.Drop;
import com.auxdible.skrpg.player.skills.DropRarity;
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
    private boolean isPoisoned;
    private boolean isFire;
    private boolean isWater;
    public Mob(MobType mobType, Entity ent) {
        this.mobType = mobType;
        this.currentHP = mobType.getMaxHP();
        this.ent = ent;
        isPoisoned = false;
        isFire = false;
        isWater = false;
    }

    public Entity getEnt() { return ent; }
    public int getCurrentHP() { return currentHP; }
    public MobType getMobType() { return mobType; }
    public void damage(Player player, int damage, SKRPG skrpg, DamageType damageType) {

        PlayerData playerData = skrpg.getPlayerManager().getPlayerData(player.getUniqueId());
        ItemInfo itemInfo = ItemInfo.parseItemInfo(player.getInventory().getItemInMainHand());
        damage = damage + 30;
        double bonusDamage = 1 + (Integer.parseInt(playerData.getCombat().getLevel().toString()
                .replace("_", "")) * 0.02);
        damage = (int) Math.round(damage * bonusDamage);
        int nerfedDamage;
        if (damageType == DamageType.NATURAL || damageType == DamageType.DROWNING
                || damageType == DamageType.FIRE || damageType == DamageType.TRUE) {
            nerfedDamage = damage;
        } else {

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


            if (damageType == DamageType.ENERGETIC) {
                nerfedDamage = nerfedDamage * (1 + (playerData.getMaxEnergy() / 100));
            }
            if (itemInfo != null) {
                for (Enchantment enchantment : itemInfo.getEnchantmentsList()) {
                    if (enchantment.getEnchantmentType() == Enchantments.SHIFT_ATTACK && player.isSneaking()) {
                        enchantmentSymbols.add(enchantment.getEnchantmentType().getEnchantmentDamageSymbol());
                    } else {
                        if (enchantment.getEnchantmentType() == Enchantments.SHARPNESS) {
                            nerfedDamage = (int) Math.round(nerfedDamage * (1 + (0.07 * enchantment.getLevel())));
                        }
                        if (enchantment.getEnchantmentType() == Enchantments.POWER) {
                            nerfedDamage = (int) Math.round(nerfedDamage * (1 + (0.07 * enchantment.getLevel())));
                        }
                        if (enchantment.getEnchantmentType() == Enchantments.FIRE_ASPECT && !isFire) {
                            isFire = true;

                            int finalNerfedDamage = nerfedDamage;
                            new BukkitRunnable() {
                                int halfSeconds = 0;

                                @Override
                                public void run() {
                                    if (getEnt().isDead()) {
                                        cancel();
                                        return;
                                    }
                                    if (halfSeconds == 20) {
                                        isFire = false;
                                        cancel();
                                    }
                                    damage(player, (finalNerfedDamage / 10), skrpg, DamageType.FIRE);
                                    if (getEnt() instanceof Creature) {
                                        ((Creature) getEnt()).damage(0.1f);
                                        ((Creature) getEnt()).setHealth(((Creature) getEnt()).getMaxHealth());
                                    }
                                    player.getWorld().playSound(getEnt().getLocation(), Sound.ENTITY_PLAYER_HURT_ON_FIRE, 1.0f, 1.0f);
                                    halfSeconds++;
                                }
                            }.runTaskTimer(skrpg, 0, 10);
                        }
                        if (enchantment.getEnchantmentType() == Enchantments.WATERLORD && !isWater) {
                            isWater = true;

                            int finalNerfedDamage = nerfedDamage;
                            new BukkitRunnable() {
                                int halfSeconds = 0;

                                @Override
                                public void run() {
                                    if (getEnt().isDead()) {
                                        cancel();
                                        return;
                                    }
                                    if (halfSeconds == 20) {
                                        isWater = false;
                                        cancel();
                                    }
                                    damage(player, (finalNerfedDamage / 20), skrpg, DamageType.DROWNING);
                                    if (getEnt() instanceof Creature) {
                                        ((Creature) getEnt()).damage(0.1f);
                                        ((Creature) getEnt()).setHealth(((Creature) getEnt()).getMaxHealth());
                                    }
                                    player.getWorld().playSound(getEnt().getLocation(), Sound.ENTITY_PLAYER_HURT_DROWN, 1.0f, 1.0f);
                                    halfSeconds++;
                                }
                            }.runTaskTimer(skrpg, 0, 10);
                        }
                        if (enchantment.getEnchantmentType() == Enchantments.POISON && !isPoisoned) {
                            isPoisoned = true;

                            int finalNerfedDamage = nerfedDamage;
                            new BukkitRunnable() {
                                int halfSeconds = 0;

                                @Override
                                public void run() {
                                    if (getEnt().isDead()) {
                                        cancel();
                                        return;
                                    }
                                    if (halfSeconds == 20) {
                                        isPoisoned = false;
                                        cancel();
                                    }
                                    damage(player, (finalNerfedDamage / 10), skrpg, DamageType.TRUE);
                                    if (getEnt() instanceof Creature) {
                                        ((Creature) getEnt()).damage(0.1f);
                                        ((Creature) getEnt()).setHealth(((Creature) getEnt()).getMaxHealth());
                                    }
                                    player.getWorld().playSound(getEnt().getLocation(), Sound.ENTITY_PLAYER_HURT_DROWN, 1.0f, 1.0f);
                                    halfSeconds++;
                                }
                            }.runTaskTimer(skrpg, 0, 10);
                        }
                        enchantmentSymbols.add(enchantment.getEnchantmentType().getEnchantmentDamageSymbol());
                    }
                }
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
        damageIndictator.setCustomName(Text.color(damageType.getPrefix().replaceAll("\\{damage}", nerfedDamage + "")));
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
            if (mobType == MobType.SPIDER) {
                double chance = Math.random();
                if (chance <= 0.005) {
                    player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 1.0f);
                    MobType.buildMob("VALISSAS_KEEPER", skrpg, ent.getLocation());
                    new BukkitRunnable() {

                        @Override
                        public void run() {
                            player.playSound(player.getLocation(), Sound.ENTITY_SPIDER_DEATH, 1.0f, 0.2f);
                        }
                    }.runTaskLater(skrpg, 10);
                    Text.applyText(player, "&c&l! &r&8| &cValissa's Keeper &7has spawned!");
                }
            }
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 2.0f);
            playerData.addRunicPoints(mobType.getLevel(), skrpg);
            if (playerData.hasQuest(Quests.TUTORIAL) && mobType == MobType.MONOLITH_DUMMY) {
                TutorialQuest tutorialQuest = (TutorialQuest) playerData.getQuest(Quests.TUTORIAL);
                if (tutorialQuest.getMonolithWarriorsKilled() < 5) {
                    tutorialQuest.setMonolithWarriorsKilled(tutorialQuest.getMonolithWarriorsKilled() + 1);
                }
            }
            for (MobKill mobKill : EnumSet.allOf(MobKill.class)) {
                if (getMobType().getName().equals(mobKill.getMobType().getName())) {
                    if (mobKill.getDrop() != null) {
                        playerData.getPlayerActionManager().addItem(new SKRPGItemStack(mobKill.getDrop().generateItemInfo(), 1));
                    }
                    for (Collection collection : playerData.getCollections()) {
                        if (collection.getCollectionType().getItem().equals(mobKill.getDrop())) {
                            collection.setCollectionAmount(collection.getCollectionAmount() + 1);
                            collection.levelUpCollection(player, playerData);
                        }
                    }
                    for (RoyaltyQuest royaltyQuest : playerData.getRoyaltyQuests()) {
                        if (mobKill.getDrop() != null) {
                            if (royaltyQuest.getRoyaltyQuestType().equals(RoyaltyQuestType.LUMBERJACK)
                                    && mobKill.getDrop().equals(Items.WOOD)) {
                                royaltyQuest.progress(1, player, skrpg);
                            } else if (royaltyQuest.getRoyaltyQuestType().equals(RoyaltyQuestType.FARMER)
                                    && mobKill.getDrop().equals(Items.WHEAT)) {
                                royaltyQuest.progress(1, player, skrpg);
                            } else if (royaltyQuest.getRoyaltyQuestType().equals(RoyaltyQuestType.MINER)
                                    && mobKill.getDrop().equals(Items.STONE)) {
                                royaltyQuest.progress(1, player, skrpg);
                            } else if (royaltyQuest.getRoyaltyQuestType().equals(RoyaltyQuestType.WARRIOR)
                                    && mobKill.getDrop().equals(Items.ROTTEN_FLESH)) {
                                royaltyQuest.progress(1, player, skrpg);
                            }
                            if (royaltyQuest.getRoyaltyQuestType().equals(RoyaltyQuestType.OBTAIN_EXP_COMBAT)) {
                                royaltyQuest.progress(mobKill.getXpGiven(), player, skrpg);
                            }
                        }
                    }
                    double randomDouble = Math.random();
                    if (mobKill.getRareDrops() != null) {
                        for (Drop drop : mobKill.getRareDrops()) {
                            if (drop.getChance() >= randomDouble) {
                                playerData.getPlayerActionManager().addExistingItem(new SKRPGItemStack(drop.getItems().generateItemInfo(), 1));
                                if (drop.getDropRarity() != DropRarity.NORMAL) {
                                    if (drop.getDropRarity().getPriority() < 3) {
                                        player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0f, 0.2f);
                                    } else {
                                        player.playSound(player.getLocation(), Sound.ITEM_TOTEM_USE, 1.0f, 2.0f);
                                    }
                                    Text.applyText(player, drop.getDropRarity().getName() + " DROP! &r&8| &r&7You dropped a " + drop.getItems().getRarity().getColor() + drop.getItems().getName());
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
            playerData.setCredits(playerData.getCredits() + (((getMobType().getLevel() / 5) * 100) / 100));
        }

    }
    public void setCurrentHP (int currentHP) {
        this.currentHP = currentHP;
        String hpColor = "&f";
        if (currentHP < getMobType().getMaxHP() / 4) {
            hpColor = "&c";
        } else if (currentHP < getMobType().getMaxHP() / 2) {
            hpColor = "&6";
        } else if (currentHP > getMobType().getMaxHP() / 2) {
            hpColor = "&a";
        }
        ent.setCustomName(Text.color("&8&l[&c" + + getMobType().getLevel() + "☠&8&l] &r&7" + getMobType().getName() + " " + hpColor +
                getCurrentHP() + "&c♥"));
    }
}
