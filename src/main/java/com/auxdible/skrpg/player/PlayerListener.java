package com.auxdible.skrpg.player;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.items.*;
import com.auxdible.skrpg.items.abilities.Abilities;
import com.auxdible.skrpg.mobs.MobSpawn;
import com.auxdible.skrpg.mobs.npcs.NpcType;
import com.auxdible.skrpg.mobs.npcs.PurchasableItem;
import com.auxdible.skrpg.mobs.npcs.NPC;
import com.auxdible.skrpg.player.collections.Collection;
import com.auxdible.skrpg.player.collections.CollectionType;
import com.auxdible.skrpg.player.collections.Tiers;
import com.auxdible.skrpg.player.economy.Bank;
import com.auxdible.skrpg.player.economy.BankLevel;
import com.auxdible.skrpg.player.guilds.raid.Raid;
import com.auxdible.skrpg.player.guilds.raid.RaidMob;
import com.auxdible.skrpg.player.guilds.raid.RaidMobs;
import com.auxdible.skrpg.player.quests.Quests;
import com.auxdible.skrpg.player.quests.TutorialQuest;
import com.auxdible.skrpg.player.skills.*;
import com.auxdible.skrpg.mobs.Mob;
import com.auxdible.skrpg.mobs.MobType;
import com.auxdible.skrpg.utils.ItemBuilder;
import com.auxdible.skrpg.utils.ItemTweaker;
import com.auxdible.skrpg.utils.Text;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class PlayerListener implements Listener {
    private SKRPG skrpg;
    public PlayerListener(SKRPG skrpg) {
        this.skrpg = skrpg;
    }
    @EventHandler
    public void raidTargetEvent(EntityTargetLivingEntityEvent e) {
        if (e.getTarget() instanceof Villager) {
            e.setCancelled(true);
        }
        if (e.getTarget() instanceof Monster) {
            e.setCancelled(true);
        }
        if (e.getTarget() instanceof Player) {
            Player p = (Player) e.getTarget();
            if (skrpg.getRaidManager().isInRaid(p)) {
                e.setCancelled(true);
            }
            }
        if (e.getEntity() instanceof IronGolem) {
            for (Raid raid : skrpg.getRaidManager().getRaids().values()) {
                for (RaidMob raidMob : raid.getRaidMobs()) {
                    if (raidMob.getEnt().equals(e.getEntity())) {
                        e.setCancelled(true);
                    }
                }
            }
        }
        }

    @EventHandler
    public void onClickEntityEvent(PlayerInteractAtEntityEvent e) {
        Player p = e.getPlayer();
        PlayerData playerData = skrpg.getPlayerManager().getPlayerData(p.getUniqueId());
        if (e.getRightClicked() instanceof Villager || e.getRightClicked() instanceof Player) {
            e.setCancelled(true);
        }
        if (skrpg.getRaidManager().isInRaid(p)) {
            Text.applyText(p, "&cYou are in a raid!");
            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
            e.setCancelled(true);
            return;
        }
        if (e.getRightClicked() instanceof Slime) {
            for (NpcType npcType : EnumSet.allOf(NpcType.class)) {
                Class npcClass = npcType.getNpc();
                NPC npc = null;
                try {
                    Class[] cArg = new Class[2];
                    cArg[0] = int.class;
                    cArg[1] = Location.class;
                    npc = (NPC) npcClass.getDeclaredConstructor(cArg).newInstance(-1, null);
                } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException x) {
                    x.printStackTrace();
                }
                if (e.getRightClicked().getCustomName().equals(npc.getTypeID())) {
                    npc.onInteract(p, playerData, skrpg);
                }

            }

        }
    }
    @EventHandler
    public void onDamageEvent(EntityDamageByEntityEvent e) {

        if (e.getEntity() instanceof Player) {
            Entity damagingEntity = e.getDamager();

            if (e.getDamager() instanceof Arrow) {
                if (((Arrow) e.getDamager()).getShooter() instanceof Skeleton) {
                    damagingEntity = (Entity) ((Arrow) e.getDamager()).getShooter();
                } else if (((Arrow) e.getDamager()).getShooter() instanceof Player) {
                    damagingEntity = (Entity) ((Arrow) e.getDamager()).getShooter();
                }
            }
            e.setDamage(0.1);
            if (damagingEntity instanceof Player) {
                int damage = 1;
                Player damager = (Player) damagingEntity;
                PlayerData damagerData = skrpg.getPlayerManager().getPlayerData(damager.getUniqueId());

                for (Items items : EnumSet.allOf(Items.class)) {
                    if (damager.getInventory().getItemInMainHand().getType().equals(Material.AIR)) {
                        damage = 1 + ((damagerData.getStrength() / 5) *
                                (1 + damagerData.getStrength() / 100));
                    } else if (damager.getInventory().getItemInMainHand().getItemMeta().getDisplayName()
                            .contains(items.getName())) {
                        damage = (5 + items.getDamage() + ((damagerData.getStrength()) / 5)) *
                                (1 + damagerData.getStrength() / 100);
                        if (items.getItemType().equals(ItemType.BOW) && e.getDamager() instanceof Player) {
                            damage = damage / 10;
                        }
                    }
                }
                if (damagerData.getRegion().getName().contains("(PVP)")) {
                    skrpg.getPlayerManager().getPlayerData(e.getEntity().getUniqueId()).damagePlayer(damager, damage, skrpg);
                }
            } else {
                Player player = (Player) e.getEntity();
                PlayerData playerData = skrpg.getPlayerManager().getPlayerData(player.getUniqueId());
                int damage = 10;
                for (MobType mob : EnumSet.allOf(MobType.class)) {
                    if (ChatColor.stripColor(damagingEntity.getName()).contains(mob.getName())) {
                        damage = mob.getDamage();
                    }
                }
                playerData.damage(damage, skrpg);
            }
        }  else {

            if (skrpg.getMobManager().getMobData(e.getEntity()) != null) {
                Mob mob = skrpg.getMobManager().getMobData(e.getEntity());
                int damage = 1;


                Player player;
                if (e.getDamager() instanceof Arrow) {
                    Arrow arrow = (Arrow) e.getDamager();
                    if (arrow.getShooter() instanceof Player) {
                        player = (Player) arrow.getShooter();
                    } else {
                        return;
                    }
                } else {
                    player = (Player) e.getDamager();
                }
                PlayerData playerData = skrpg.getPlayerManager().getPlayerData(player.getUniqueId());
                for (Items items : EnumSet.allOf(Items.class)) {
                    if (player.getInventory().getItemInMainHand().getType().equals(Material.AIR)) {
                        damage = 1 + ((playerData.getStrength() / 5) *
                                (1 + playerData.getStrength() / 100));
                    } else if (ChatColor.stripColor(player.getInventory().getItemInMainHand().getItemMeta().getDisplayName())
                            .equals(ChatColor.stripColor(items.getName()))) {
                        damage = (5 + items.getDamage() + ((playerData.getStrength()) / 5)) *
                                (1 + playerData.getStrength() / 100);
                        if (items.getItemType().equals(ItemType.BOW) && e.getDamager() instanceof Player) {
                            damage = damage / 10;
                        }
                    }
                }

                e.setDamage(0.1);
                if (e.getEntity() instanceof LivingEntity) {
                    ((LivingEntity) e.getEntity()).setHealth(((LivingEntity) e.getEntity()).getMaxHealth());
                }

                if (skrpg.getRaidManager().isInRaid(player)) {
                    Text.applyText(player, "&cYou are in a raid!");
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
                    e.setCancelled(true);
                    return;
                }
                mob.damage(player, damage, skrpg);
                }

            }
        }

    @EventHandler
    public void onUseEvent(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (skrpg.getRaidManager().isInRaid(p)) {
            Text.applyText(p, "&cYou are in a raid!");
            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
            e.setCancelled(true);
            return;
        }
        PlayerData playerData = skrpg.getPlayerManager().getPlayerData(p.getUniqueId());
        if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)||e.getAction().equals(Action.RIGHT_CLICK_AIR)){
            if(e.getClickedBlock() != null && e.getClickedBlock().getType() == Material.CRAFTING_TABLE){
                e.setCancelled(true);
                p.performCommand("ct");
            }
        }
        if (!e.getAction().equals(Action.RIGHT_CLICK_AIR) && !e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) { return; }
        if (p.getInventory().getItemInMainHand().getType() != Material.AIR) {
            Abilities.executeAbility(playerData, skrpg);
        }
    }
    @EventHandler
    public void onEntityDeathEvent(EntityDeathEvent e) {
        if (!(e.getEntity() instanceof Player)) {
            e.getDrops().clear();
            e.setDroppedExp(0);
        }
    }
    @EventHandler
    public void onDamageEvent(EntityDamageEvent e) {
        if (e.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK &&
                e.getCause() != EntityDamageEvent.DamageCause.CUSTOM &&
                e.getCause() != EntityDamageEvent.DamageCause.PROJECTILE) {
            e.setCancelled(true);
        }

    }
    @EventHandler
    public void onHungerLoseEvent(FoodLevelChangeEvent e) {
        e.setCancelled(true);
    }
    @EventHandler
    public void onBreakEvent(BlockBreakEvent e) {
        Player p = e.getPlayer();
        if (skrpg.getRaidManager().isInRaid(p)) {
            Text.applyText(p, "&cYou are in a raid!");
            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
            e.setCancelled(true);
            return;
        }
        PlayerData playerData = skrpg.getPlayerManager().getPlayerData(p.getUniqueId());
        if (p.getGameMode().equals(GameMode.CREATIVE)) { return; }
        for (MineBlock mineBlock : EnumSet.allOf(MineBlock.class)) {
            if (e.getBlock().getType() == mineBlock.getBlockObtainedFrom()) {
                p.getInventory().addItem(Items.buildItem(mineBlock.getCommonDrop()));
                for (Collection collection : playerData.getCollections()) {
                    if (collection.getCollectionType().getItem().equals(mineBlock.getCommonDrop())) {
                        collection.setCollectionAmount(collection.getCollectionAmount() + 1);
                        collection.levelUpCollection(p, playerData);
                    }
                }
                e.setCancelled(true);
                double random = Math.random();
                double random2 = Math.random();
                if (mineBlock.getDrops() != null) {
                    for (Drop drop : mineBlock.getDrops()) {
                        if (drop.getChance() >= random) {
                            p.getInventory().addItem(Items.buildItem(drop.getItems()));
                            Text.applyText(p, "&r&5&lSPECIAL DROP! &r&8| " + drop.getItems()
                                    .getRarity().getColor() + drop.getItems().getName());
                            new BukkitRunnable() {
                                int seconds = 1;
                                @Override
                                public void run() {
                                    if (seconds == 1) {
                                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1.0f, 1.0f);
                                    } else if (seconds == 2) {
                                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1.0f, 0.5f);
                                    } else if (seconds == 3) {
                                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1.0f, 2.0f);
                                        cancel();
                                    }
                                    seconds++;
                                }
                            }.runTaskTimer(skrpg, 0, 4);
                        }
                    }
                }
                if (Integer.parseInt(playerData.getMining().getLevel().toString()
                        .replace("_", "")) * 0.04 >= random2) {
                    p.getInventory().addItem(Items.buildItem(mineBlock.getCommonDrop()));
                }
                playerData.getMining().setXpTillNext(playerData.getMining().getXpTillNext() +
                        mineBlock.getXpObtained());
                playerData.getMining().setTotalXP(playerData.getMining().getTotalXP()
                        + mineBlock.getXpObtained());
                playerData.getMining().levelUpSkill(p, playerData, skrpg);

                p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                        Text.color("&e+ " + mineBlock.getXpObtained() + " Mining XP (" + playerData.getMining()
                                .getXpTillNext() + "/" + Level.valueOf("_" +
                                (Integer.parseInt(playerData.getMining().getLevel().toString()
                                        .replace("_", "")) + 1)).getXpRequired() + ")")));
                p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 2.0f);
                e.getBlock().setType(Material.BEDROCK);
                new BukkitRunnable() {
                    int seconds = 0;
                    @Override
                    public void run() {
                        seconds++;
                        if (seconds == 5) {
                            e.getBlock().setType(mineBlock.getBlockObtainedFrom());
                        }
                    }
                }.runTaskTimer(skrpg, 0, 20);
            }
        }

        for (MinePlant minePlant : EnumSet.allOf(MinePlant.class)) {
            if (e.getBlock().getType() == minePlant.getBlockObtainedFrom()) {
                p.getInventory().addItem(Items.buildItem(minePlant.getObtainedItem()));
                for (Collection collection : playerData.getCollections()) {
                    if (collection.getCollectionType().getItem().equals(minePlant.getObtainedItem())) {
                        collection.setCollectionAmount(collection.getCollectionAmount() + 1);
                        collection.levelUpCollection(p, playerData);
                    }
                }
                int caneHeight = 0;
                e.setCancelled(true);
                double random = Math.random();
                double random2 = Math.random();
                if (minePlant.getDrops() != null) {
                    for (Drop drop : minePlant.getDrops()) {
                        if (drop.getChance() >= random) {
                            p.getInventory().addItem(Items.buildItem(drop.getItems()));
                            Text.applyText(p, "&r&5&lSPECIAL DROP! &r&8| " + drop.getItems()
                                    .getRarity().getColor() + drop.getItems().getName());
                            new BukkitRunnable() {
                                int seconds = 1;
                                @Override
                                public void run() {
                                    if (seconds == 1) {
                                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1.0f, 1.0f);
                                    } else if (seconds == 2) {
                                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1.0f, 0.5f);
                                    } else if (seconds == 3) {
                                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1.0f, 2.0f);
                                        cancel();
                                    }
                                    seconds++;
                                }
                            }.runTaskTimer(skrpg, 0, 4);
                        }
                    }
                }
                if (Integer.parseInt(playerData.getHerbalism().getLevel().toString()
                        .replace("_", "")) * 0.04 >= random2) {
                    p.getInventory().addItem(Items.buildItem(minePlant.getObtainedItem()));
                }
                if (e.getBlock().getType().equals(Material.SUGAR_CANE)) {
                    if (e.getBlock().getRelative(BlockFace.UP).getType().equals(Material.SUGAR_CANE)) {
                        if (e.getBlock().getRelative(BlockFace.UP).getRelative(BlockFace.UP).getType()
                                .equals(Material.SUGAR_CANE)) {
                            e.getBlock().getRelative(BlockFace.UP).getRelative(BlockFace.UP).setType(Material.AIR);
                            e.getBlock().getRelative(BlockFace.UP).setType(Material.AIR);
                            caneHeight = 3;
                            playerData.getHerbalism().setXpTillNext(playerData.getHerbalism().getXpTillNext() +
                                    (minePlant.getXpObtained() * 3));
                            playerData.getHerbalism().setTotalXP(playerData.getHerbalism().getTotalXP()
                                    + (minePlant.getXpObtained() * 3));
                            p.getInventory().addItem(Items.buildItem(minePlant.getObtainedItem()));
                            p.getInventory().addItem(Items.buildItem(minePlant.getObtainedItem()));
                        } else {
                            e.getBlock().getRelative(BlockFace.UP).setType(Material.AIR);
                            playerData.getHerbalism().setXpTillNext(playerData.getHerbalism().getXpTillNext() +
                                    (minePlant.getXpObtained() * 2));
                            playerData.getHerbalism().setTotalXP(playerData.getHerbalism().getTotalXP()
                                    + (minePlant.getXpObtained() * 2));
                            caneHeight = 2;
                            p.getInventory().addItem(Items.buildItem(minePlant.getObtainedItem()));
                        }
                    } else {
                        playerData.getHerbalism().setXpTillNext(playerData.getHerbalism().getXpTillNext() +
                                minePlant.getXpObtained());
                        playerData.getHerbalism().setTotalXP(playerData.getHerbalism().getTotalXP()
                                + minePlant.getXpObtained());
                        caneHeight = 1;

                    }
                } else {
                    playerData.getHerbalism().setXpTillNext(playerData.getHerbalism().getXpTillNext() +
                            minePlant.getXpObtained());
                    playerData.getHerbalism().setTotalXP(playerData.getHerbalism().getTotalXP()
                            + minePlant.getXpObtained());

                }
                playerData.getHerbalism().levelUpSkill(p, playerData, skrpg);
                p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                        Text.color("&e+ " + minePlant.getXpObtained() + " Herbalism XP (" + playerData.getHerbalism()
                                .getXpTillNext() + "/" + Level.valueOf("_" +
                                (Integer.parseInt(playerData.getHerbalism().getLevel().toString()
                                        .replace("_", "")) + 1)).getXpRequired() + ")")));
                p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 2.0f);
                e.getBlock().setType(Material.AIR);
                int finalCaneHeight = caneHeight;
                new BukkitRunnable() {
                    int seconds = 0;

                    @Override
                    public void run() {
                        seconds++;
                        if (seconds == 5) {
                            e.getBlock().setType(minePlant.getBlockObtainedFrom());
                            if (e.getBlock().getBlockData() instanceof Ageable) {
                                Ageable ageable = (Ageable) e.getBlock().getBlockData();
                                ageable.setAge(ageable.getMaximumAge());
                                e.getBlock().setBlockData(ageable);
                            }
                            if (e.getBlock().getType().equals(Material.SUGAR_CANE)) {
                                for (int i = 0; i <= finalCaneHeight; i++) {
                                    if (e.getBlock().getRelative(BlockFace.DOWN).getType().equals(Material.SUGAR_CANE) &&
                                            !e.getBlock().getRelative(BlockFace.DOWN).getRelative(BlockFace.DOWN)
                                                    .getType().equals(Material.SUGAR_CANE)) {
                                        e.getBlock().getRelative(BlockFace.UP).setType(Material.SUGAR_CANE);
                                    }

                                }
                                if (!e.getBlock().getRelative(BlockFace.DOWN).getType().equals(Material.SUGAR_CANE)) {
                                    e.getBlock().getRelative(BlockFace.UP).setType(Material.SUGAR_CANE);
                                    e.getBlock().getRelative(BlockFace.UP).getRelative(BlockFace.UP).setType(Material.SUGAR_CANE);
                                }

                            }




                        }
                    }
                }.runTaskTimer(skrpg, 0, 20);
            }
        }
        if (e.getBlock().getType().toString().contains("WOOD") || e.getBlock().getType().toString().contains("LOG")) {
            e.getPlayer().getInventory().addItem(Items.buildItem(Items.WOOD));
            for (Collection collection : playerData.getCollections()) {
                if (collection.getCollectionType().getItem().equals(Items.WOOD)) {
                    collection.setCollectionAmount(collection.getCollectionAmount() + 1);
                    collection.levelUpCollection(p, playerData);
                }
            }
            e.setCancelled(true);
            playerData.getCrafting().setXpTillNext(playerData.getCrafting().getXpTillNext() +
                    10);
            playerData.getCrafting().setTotalXP(playerData.getCrafting().getTotalXP()
                    + 10);
            playerData.getCrafting().levelUpSkill(p, playerData, skrpg);
            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                    Text.color("&e+ " + 10 + " Crafting XP (" + playerData.getCrafting()
                            .getXpTillNext() + "/" + Level.valueOf("_" +
                            (Integer.parseInt(playerData.getCrafting().getLevel().toString()
                                    .replace("_", "")) + 1)).getXpRequired() + ")")));
            p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 2.0f);
            Material type = e.getBlock().getType();
            e.getBlock().setType(Material.AIR);
            new BukkitRunnable() {
                int seconds = 0;
                @Override
                public void run() {
                    seconds++;
                    if (seconds == 5) {
                        e.getBlock().setType(type);
                    }
                }
            }.runTaskTimer(skrpg, 0, 20);
        }
        e.setCancelled(true);
    }
    @EventHandler
    public void onCloseEvent(InventoryCloseEvent e) {
        if (e.getView().getTitle().equals("Crafting Table")) {
            Player p  = (Player) e.getPlayer();
            List<Integer> slots = Arrays.asList(10, 11, 12, 19, 20, 21, 28, 29, 30);
            for (int slot : slots) {
                if (e.getInventory().getItem(slot) != null) {
                    p.getInventory().addItem(e.getInventory().getItem(slot));
                    e.getInventory().removeItem(e.getInventory().getItem(slot));
                }
            }
        } else if (e.getView().getTitle().equals("Give Nuggets")) {
            Player p  = (Player) e.getPlayer();
            PlayerData playerData = skrpg.getPlayerManager().getPlayerData(p.getUniqueId());
            if (playerData.getTrade() != null) {
                p.openInventory(playerData.getTrade().getInv(p));
            }
        } else if (e.getView().getTitle().equals("Trade")) {
            Player p  = (Player) e.getPlayer();
            PlayerData playerData = skrpg.getPlayerManager().getPlayerData(p.getUniqueId());
            new BukkitRunnable() {

                @Override
                public void run() {
                    if (playerData.getTrade() != null && p.getOpenInventory().getTitle().equals("Give Nuggets")) {
                        p.openInventory(playerData.getTrade().getInv(p));
                    }
                }
            }.runTaskLater(skrpg, 10);

        }
    }
    @EventHandler
    public void onInventoryDrag(InventoryDragEvent e) {
        Player p  = (Player) e.getWhoClicked();
        if (skrpg.getRaidManager().isInRaid(p)) {
            Text.applyText(p, "&cYou are in a raid!");
            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
            e.setCancelled(true);
            return;
        }
        PlayerData playerData = skrpg.getPlayerManager().getPlayerData(p.getUniqueId());
        if (e.getView().getTitle().equals("Crafting Table")) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    craftingTableUpdate(e.getInventory(), p, playerData);
                }
            }.runTaskLater(skrpg, 1);

        }
    }

    @EventHandler
    public void onInventoryEvent(InventoryClickEvent e) {
        Player p  = (Player) e.getWhoClicked();
        PlayerData playerData = skrpg.getPlayerManager().getPlayerData(p.getUniqueId());
        if (e.getCurrentItem() != null && e.getCurrentItem().getItemMeta() != null &&
                e.getCurrentItem().getItemMeta().getDisplayName().equals(Text.color("&aBack to Menu"))) {
            p.performCommand("menu");
        }
        List<Integer> slots = Arrays.asList(10, 11, 12, 19, 20, 21, 28, 29, 30);

        if (e.getView().getTitle().equals("Crafting Table")) {
            if (e.getCurrentItem() != null) {
                if (e.getCurrentItem().getType() == Material.BLACK_STAINED_GLASS_PANE ||
                        e.getCurrentItem().getType() == Material.ARROW) {
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Your Crafting Result") || e.getCurrentItem().getItemMeta().getDisplayName().equals(" ")) {
                        e.setCancelled(true);
                        return;
                    }
                }
            }
            new BukkitRunnable() {
                @Override
                public void run() {

                    if (e.getSlot() == 25 && e.getAction().equals(InventoryAction.PLACE_ALL) || e.getSlot() == 25 && e.getAction().equals(InventoryAction.PLACE_ONE)) {
                        e.getInventory().setContents(e.getInventory().getContents());

                        p.getInventory().addItem(e.getCurrentItem());
                        p.getItemOnCursor().setType(Material.AIR);
                        return;
                    }
                    if (e.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY)) {
                        e.getInventory().setContents(e.getInventory().getContents());
                        p.getInventory().setContents(p.getInventory().getContents());
                        return;
                    }


                    craftingTableUpdate(e.getInventory(), p, playerData);

                }
            }.runTaskLater(skrpg, 1);
            if (e.getSlot() == 25 && e.getInventory().getItem(25) != null) {

                for (Items items : EnumSet.allOf(Items.class)) {
                    if (ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).contains(items.getName())) {
                        for (int integer : slots) {
                            if (e.getInventory().getItem(integer) != null) {
                                if (e.getInventory().getItem(integer).getAmount() == 1 || e.getInventory().getItem(integer).getAmount() <= items.getCraftingRecipe().get(slots.indexOf(integer)).getAmount()) {
                                    e.getInventory().setItem(integer, null);
                                } else {
                                    e.getInventory().getItem(integer).setAmount(e.getInventory().getItem(integer).getAmount() -
                                            items.getCraftingRecipe().get(slots.indexOf(integer)).getAmount());
                                }
                            }

                        }
                        playerData.getCrafting().setXpTillNext(playerData.getCrafting().getXpTillNext() +
                                items.getCraftingXPGained());
                        playerData.getCrafting().setTotalXP(playerData.getCrafting().getTotalXP()
                                + items.getCraftingXPGained());
                        playerData.getCrafting().levelUpSkill(p, playerData, skrpg);
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                                Text.color("&e+ " + items.getCraftingXPGained() + " Crafting XP (" + playerData.getCrafting()
                                        .getXpTillNext() + "/" + Level.valueOf("_" +
                                        (Integer.parseInt(playerData.getCrafting().getLevel().toString()
                                                .replace("_", "")) + 1)).getXpRequired() + ")")));
                        p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 2.0f);
                    }
                }
                p.getInventory().addItem(e.getCurrentItem());
                e.getInventory().setItem(e.getSlot(), null);

                p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_USE, 1.0f, 0.5f);

                p.getInventory().remove(p.getItemOnCursor());
            }
        } else if (e.getView().getTitle().equals("SKRPG Menu")) {
            e.setCancelled(true);
            if (e.getCurrentItem().getItemMeta().getDisplayName().equals(Text.color("&aCrafting Table"))) {
                p.performCommand("ct");
            } else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(Text.color("&aYour Statistics"))) {
                p.performCommand("stats");
            } else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(Text.color("&aSkills"))) {
                p.performCommand("skills");
            } else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(Text.color("&aCollections"))) {
                p.performCommand("collections");
            } else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(Text.color("&aSettings"))) {
                p.performCommand("settings");
            }
        } else if (e.getView().getTitle().equals("Your Skills")) {
            if (e.getCurrentItem() != null) {
                if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Combat")) {
                    p.performCommand("skills combat");
                } else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Herbalism")) {
                    p.performCommand("skills herbalism");
                } else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Crafting")) {
                    p.performCommand("skills crafting");
                } else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Mining")) {
                    p.performCommand("skills mining");
                }
            }
            e.setCancelled(true);
        } else if (e.getView().getTitle().equals("Miner") || e.getView().getTitle().equals("Adventurer") || e.getView().getTitle().equals("Farmer") ||
        e.getView().getTitle().equals("Woodcutter")) {
            SellMerchant sellMerchant = null;
            if (e.getView().getTitle().equals("Miner")) {
                sellMerchant = SellMerchant.MINING;
            } else if (e.getView().getTitle().equals("Adventurer")) {
                sellMerchant = SellMerchant.COMBAT;
            } else if (e.getView().getTitle().equals("Woodcutter")) {
                sellMerchant = SellMerchant.WOODCUTTING;
            } else if (e.getView().getTitle().equals("Farmer")) {
                sellMerchant = SellMerchant.FARMING;
            }
            for (Items items : EnumSet.allOf(Items.class)) {
                if (e.getCurrentItem() != null) {
                    if (e.getCurrentItem().getItemMeta().getDisplayName() != null) {
                        if (e.getCurrentItem().getItemMeta().getDisplayName().contains(items.getName())) {
                            if (playerData.getSellAboveRarity().getPriority() < items.getRarity().getPriority() || playerData.getSellAboveRarity() == Rarity.COMMON) {
                                Text.applyText(p, "&cYour sell above rarity setting prevented you from selling this!");
                                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);

                            } else {
                                if (items.getSellMerchant() == sellMerchant) {
                                    playerData.setCredits(playerData.getCredits() + items.getSellPrice());
                                    p.playSound(p.getLocation(), Sound.ENTITY_PHANTOM_FLAP, 1.0f, 2.0f);
                                    e.getCurrentItem().setAmount(e.getCurrentItem().getAmount() - 1);

                                    Text.applyText(p, "&7+ &6" + items.getSellPrice() + " Nuggets &r&7from " +
                                            items.getRarity().getColor() + items.getName());
                                    ItemMeta iM = e.getView().getTopInventory().getItem(22).getItemMeta();
                                    iM.setDisplayName(Text.color("&7Sold for +&6" + items.getSellPrice() + " Nuggets"));
                                    e.getView().getTopInventory().getItem(22).setItemMeta(iM);
                                } else {
                                    Text.applyText(p, "&e&l" + e.getView().getTitle() + " &r&8| &cI can't buy this from you. Try asking another merchant.");
                                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);

                                }
                            }
                        }
                    }
                }

            }
            e.setCancelled(true);
        } else if (e.getView().getTitle().equals("Banker")) {
            if (e.getCurrentItem() != null) {
                e.setCancelled(true);
                if (e.getCurrentItem().getItemMeta() != null) {
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(Text.color("&7Buy Bank"))) {
                        if (playerData.getCredits() < (2500000 * playerData.getBanks().size())) {
                            Text.applyText(p, "&cYou don't have enough coins to buy a new bank!");
                            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 0.25f);
                            p.closeInventory();
                        } else {
                            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 2.0f, 0.25f);
                            p.closeInventory();
                            Text.applyText(p, "&aYou bought a bank for &6" + (250000 * playerData.getBanks().size()) + " Nuggets");
                            playerData.setCredits(playerData.getCredits() - (250000 * playerData.getBanks().size()));
                            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_YES, 2.0f, 0.25f);
                            playerData.getBanks().add(new Bank(0, BankLevel.FREE));
                        }
                    } else if (e.getCurrentItem().getType().equals(Material.PLAYER_HEAD) && e.getCurrentItem().getItemMeta()
                            .getDisplayName().contains(Text.color("&7Bank"))) {
                        Bank bank = playerData.getBanks().get(Integer.parseInt(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName().replace("Bank ", ""))) - 1);
                        Inventory inv = Bukkit.createInventory(null, 54, ChatColor.stripColor(Text.color("&7Your " + e.getCurrentItem()
                                .getItemMeta().getDisplayName())));
                        for (int i = 0; i <= 53; i++) {
                            inv.setItem(i, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE, 0).setName(" ").asItem());
                        }
                        inv.setItem(10, new ItemBuilder(Material.GOLD_BLOCK, 0).setName("&7Deposit &6All").asItem());
                        inv.setItem(12, new ItemBuilder(Material.GOLD_BLOCK, 0).setName("&7Deposit &61000000 Nuggets").asItem());
                        inv.setItem(14, new ItemBuilder(Material.GOLD_INGOT, 0).setName("&7Deposit &6100000 Nuggets").asItem());
                        inv.setItem(16, new ItemBuilder(Material.GOLD_INGOT, 0).setName("&7Deposit &610000 Nuggets").asItem());
                        inv.setItem(20, new ItemBuilder(Material.GOLD_NUGGET, 0).setName("&7Deposit &6100 Nuggets").asItem());
                        inv.setItem(24, new ItemBuilder(Material.GOLD_NUGGET, 0).setName("&7Deposit &610 Nuggets").asItem());
                        inv.setItem(28, new ItemBuilder(Material.GOLD_BLOCK, 0).setName("&7Withdraw &6All").asItem());
                        inv.setItem(30, new ItemBuilder(Material.GOLD_BLOCK, 0).setName("&7Withdraw &61000000 Nuggets").asItem());
                        inv.setItem(32, new ItemBuilder(Material.GOLD_INGOT, 0).setName("&7Withdraw &6100000 Nuggets").asItem());
                        inv.setItem(34, new ItemBuilder(Material.GOLD_INGOT, 0).setName("&7Withdraw &610000 Nuggets").asItem());
                        inv.setItem(38, new ItemBuilder(Material.GOLD_NUGGET, 0).setName("&7Withdraw &6100 Nuggets").asItem());
                        inv.setItem(42, new ItemBuilder(Material.GOLD_NUGGET, 0).setName("&7Withdraw &610 Nuggets").asItem());
                        if (!bank.getLevel().equals(BankLevel.SUPER_DELUXE)) {
                            BankLevel bankLevel = null;
                            for (BankLevel bankLevel1 : EnumSet.allOf(BankLevel.class)) {
                                if (bankLevel1.getLevel() == bank.getLevel().getLevel() + 1) {
                                    bankLevel = bankLevel1;
                                }
                            }
                            inv.setItem(22, new ItemBuilder(Material.EMERALD_BLOCK, 0).setName("&aUpgrade to " + bankLevel.getNameColored()).setLore(Arrays.asList(" ",
                                    Text.color("&7Cost: &6" + bankLevel.getCost() + " Nuggets"))).asItem());
                        }
                        p.openInventory(inv);
                    }

                }
            }
        } else if (e.getView().getTitle().contains("Your Bank")) {
            e.setCancelled(true);
            Bank bank = playerData.getBanks().get(Integer.parseInt(e.getView().getTitle().replace("Your Bank ", "")) - 1);
            if (e.getCurrentItem() != null) {
                if (e.getCurrentItem().getItemMeta() != null) {
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Deposit")) {
                        if (e.getCurrentItem().getItemMeta().getDisplayName().contains("All")) {
                            int amount = playerData.getCredits();
                            if (bank.getCredits() == bank.getLevel().getMaxCredits()) {
                                Text.applyText(p, "&cYour bank is full!");
                                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 2.0f, 1.0f);
                                return;
                            }
                            if (bank.getLevel().getMaxCredits() < bank.getCredits() + amount) {
                                amount = Math.min(amount + bank.getCredits(), bank.getLevel().getMaxCredits());
                                Text.applyText(p, "&cYour bank is full!");
                                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 2.0f, 1.0f);
                            }

                            playerData.setCredits(playerData.getCredits() - amount);
                            Text.applyText(p, "&aDeposited &6" + amount + " Nuggets&7!");
                            bank.deposit(amount);
                            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 2.0f, 2.0f);
                        } else {
                            int amount = Integer.parseInt(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()
                                    .replace("Deposit", "").replace("Nuggets", "").replace(" ", "")));
                            if (amount > playerData.getCredits()) {
                                Text.applyText(p, "&cYou don't have enough Nuggets!");
                                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 2.0f, 1.0f);
                            } else {
                                if (bank.getCredits() == bank.getLevel().getMaxCredits()) {
                                    Text.applyText(p, "&cYour bank is full!");
                                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 2.0f, 1.0f);
                                    return;
                                }
                                if (bank.getLevel().getMaxCredits() < bank.getCredits() + amount) {
                                    amount = Math.min(amount + bank.getCredits(), bank.getLevel().getMaxCredits());
                                    Text.applyText(p, "&cYour bank is full!");
                                }
                                bank.deposit(amount);
                                playerData.setCredits(playerData.getCredits() - amount);
                                Text.applyText(p, "&aDeposited &6" + amount + " Nuggets&7!");
                                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 2.0f, 2.0f);
                            }
                        }
                    } else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Withdraw")) {
                        if (e.getCurrentItem().getItemMeta().getDisplayName().contains("All")) {
                            p.playSound(p.getLocation(), Sound.ENTITY_PHANTOM_FLAP, 2.0f, 2.0f);
                            Text.applyText(p, "&aWithdrew &6" + bank.getCredits() + " Nuggets&7!");
                            playerData.setCredits(playerData.getCredits() + bank.getCredits());
                            bank.withdraw(bank.getCredits());

                        } else {
                            int amount = Integer.parseInt(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()
                                    .replace("Withdraw", "").replace("Nuggets", "").replace(" ", "")));
                            if (amount > bank.getCredits()) {
                                Text.applyText(p, "&cYour bank doesn't have enough Nuggets!");
                                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 2.0f, 1.0f);
                            } else {
                                p.playSound(p.getLocation(), Sound.ENTITY_PHANTOM_FLAP, 2.0f, 2.0f);
                                playerData.setCredits(playerData.getCredits() + amount);
                                bank.withdraw(amount);
                                Text.applyText(p, "&aWithdrew &6" + amount + " Nuggets&7!");
                            }
                        }
                    } else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Upgrade")) {
                        BankLevel nextLevel = null;
                        for (BankLevel bankLevel : EnumSet.allOf(BankLevel.class)) {
                            if (bankLevel.getLevel() == bank.getLevel().getLevel() + 1) {
                                nextLevel = bankLevel;
                            }
                        }
                        if (playerData.getCredits() < nextLevel.getCost()) {
                            Text.applyText(p, "&cYou don't have enough Nuggets to upgrade this account!");
                            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 2.0f, 0.1f);
                            return;
                        }
                        playerData.setCredits(playerData.getCredits() - nextLevel.getCost());
                        Text.applyText(p, "&aYou upgraded a bank to level " + nextLevel.getNameColored() + "&a!");
                        playerData.setCredits(playerData.getCredits() + bank.getCredits());
                        bank.upgrade(nextLevel);
                        bank.withdraw(bank.getCredits());
                        p.closeInventory();
                        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 2.0f, 0.25f);
                    }
                }
            }
        } else if (e.getView().getTitle().contains("Salesman") || e.getView().getTitle().equals("Hiltcrafter")) {
            e.setCancelled(true);
            if (e.getCurrentItem() != null) {
                if (e.getCurrentItem().getItemMeta() != null) {
                    int cost = 0;
                    for (String lore : e.getCurrentItem().getItemMeta().getLore()) {
                        if (lore.contains("Cost:")) {
                            skrpg.getLogger().info("Cost");
                            cost = Integer.parseInt(ChatColor.stripColor(lore).replace("Cost:", "")
                            .replace("Nuggets", "").replace(" ", ""));
                        }
                    }
                    for (Items items : EnumSet.allOf(Items.class)) {
                        if (items.getName().contains(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()))) {

                            if (cost > playerData.getCredits()) {
                                Text.applyText(p, "&cYou don't have enough Nuggets to buy this!");
                                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
                                return;
                            }
                            if (p.getInventory().firstEmpty() == -1) {
                                Text.applyText(p, "&cYour inventory is full!");
                                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
                                return;
                            }
                            playerData.setCredits(playerData.getCredits() - cost);
                            p.getInventory().addItem(Items.buildItem(items));
                            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1.0f, 2.0f);
                            Text.applyText(p, "&aYou purchased " + Items.buildItem(items).getItemMeta().getDisplayName() + " for &6" + cost + " Nuggets");
                        }
                    }
                }
            }
        } else if (e.getView().getTitle().equals("Trade")) {
            if (playerData.getTrade() != null) {

                if (e.getClickedInventory() == p.getInventory()) {
                    for (Items items : EnumSet.allOf(Items.class)) {
                        if (e.getCurrentItem() != null) {
                            if (ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equals(items.getName())) {
                                e.setCancelled(true);
                                playerData.getTrade().addItem(p, new CraftingIngrediant(items, e.getCurrentItem().getAmount()));
                                e.getClickedInventory().setItem(e.getSlot(), null);

                                break;
                            }
                        }
                    }
                }
                if (e.getCurrentItem() != null) {
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains("ACCEPT")) {
                        e.setCancelled(true);
                        playerData.getTrade().accept(p);
                    } else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("DENY")) {
                        e.setCancelled(true);
                        playerData.getTrade().deny();
                    } else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Pay Nuggets")) {
                        Inventory inv = Bukkit.createInventory(null, 36, "Pay Nuggets");
                        for (int i = 0; i <= 35; i++) {
                            inv.setItem(i, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE, 0).setName(" ").asItem());
                        }
                        inv.setItem(10, new ItemBuilder(Material.GOLD_BLOCK, 0).setName("&7Give &6All").asItem());
                        inv.setItem(12, new ItemBuilder(Material.GOLD_BLOCK, 0).setName("&7Give &61000000 Nuggets").asItem());
                        inv.setItem(14, new ItemBuilder(Material.GOLD_INGOT, 0).setName("&7Give &6100000 Nuggets").asItem());
                        inv.setItem(16, new ItemBuilder(Material.GOLD_INGOT, 0).setName("&7Give &610000 Nuggets").asItem());
                        inv.setItem(20, new ItemBuilder(Material.GOLD_NUGGET, 0).setName("&7Give &6100 Nuggets").asItem());
                        inv.setItem(24, new ItemBuilder(Material.GOLD_NUGGET, 0).setName("&7Give &610 Nuggets").asItem());
                        p.openInventory(inv);
                    }
                }
                e.setCancelled(true);
            }
        } else if (e.getView().getTitle().equals("Your Statistics") ||
                e.getView().getTitle().equals("Your Combat") || e.getView().getTitle().equals("Your Herbalism") ||
        e.getView().getTitle().equals("Your Crafting") || e.getView().getTitle().equals("Your Mining") || e.getView().getTitle().contains("| Collection Viewer")) {
            e.setCancelled(true);
        } else if (e.getView().getTitle().equals("Settings")) {
            e.setCancelled(true);
            if (e.getCurrentItem() != null) {
                if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Toggle Trading")) {
                    playerData.setToggleTrade(!playerData.canTrade());
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1.0f, 1.0f);
                    Text.applyText(p, Text.color("&aToggled trading has been set to " + playerData.canTrade() + "."));

                    p.performCommand("settings");
                } else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Do not Sell Beyond This Rarity")) {
                    if (playerData.getSellAboveRarity() != Rarity.LEGENDARY) {
                        for (Rarity rarity : EnumSet.allOf(Rarity.class)) {
                            if (playerData.getSellAboveRarity().getPriority() + 1 == rarity.getPriority()) {
                                playerData.setSellAboveRarity(rarity);
                                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1.0f, 1.0f);
                                if (playerData.getSellAboveRarity() != Rarity.LEGENDARY) {
                                    Text.applyText(p, "&aSet rarity to " + rarity.getNameColored() + "&a!");
                                } else {
                                    Text.applyText(p, "&aAll rarities enabled!");
                                }
                                p.performCommand("settings");
                                return;
                            }
                        }
                    } else {
                        playerData.setSellAboveRarity(Rarity.COMMON);
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1.0f, 1.0f);
                        Text.applyText(p, "&aDisabled selling!");
                    }
                    p.performCommand("settings");
                } else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Reset My Stats")) {
                    Inventory inv = Bukkit.createInventory(null, 27, "Confirm Deletion");
                    inv.setItem(10, new ItemBuilder(Material.REDSTONE_LAMP, 0).setName("&cDelete").setLore(Arrays.asList(" ", Text.color("&cTHIS CANNOT BE UNDONE!"), " ")).asItem());
                    inv.setItem(16, new ItemBuilder(Material.LIME_CONCRETE, 0).setName("&aCancel").asItem());
                    p.openInventory(inv);
                }
            }
        } else if (e.getView().getTitle().equals("Confirm Deletion")) {
            if (e.getCurrentItem() != null) {
                if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Delete")) {
                    p.closeInventory();
                    Text.applyText(p, "&cDeleting!");
                    p.getInventory().clear();
                    p.kickPlayer(Text.color("&cResetting your SKRPG game..."));
                    skrpg.getPlayerManager().removePlayer(p.getUniqueId());
                } else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Cancel")) {
                    p.closeInventory();
                    Text.applyText(p, "&aCancelled!");
                }
            }
        } else if (e.getView().getTitle().contains("Guild Menu")) {
            e.setCancelled(true);
        } else if (e.getView().getTitle().equals("Raid Mob Shop")) {
            e.setCancelled(true);
            if (e.getCurrentItem() != null) {
                for (RaidMobs raidMobs : EnumSet.allOf(RaidMobs.class)) {
                    if (Text.color(raidMobs.getName()).equals(e.getCurrentItem().getItemMeta().getDisplayName())) {
                        if (playerData.getCredits() < raidMobs.getCreditsCost()) {
                            Text.applyText(p, "&cYou do not have enough Nuggets to buy this mob!");
                            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
                            return;
                        }
                        if (SKRPG.levelToInt(playerData.getCombat().getLevel().toString()) <
                                raidMobs.getSkillLevelRequired()) {
                            Text.applyText(p, "&cYou do not have the skill level required to buy this mob!");
                            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
                            return;
                        }
                        if (skrpg.getRaidManager().getRaid(p) != null) {
                            skrpg.getRaidManager().getRaid(p).buyMob(raidMobs);
                        } else {
                            p.closeInventory();
                            Text.applyText(p, "&cYou are not in a raid!");
                            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
                            return;
                        }
                    }
                }
            }
        } else if (e.getView().getTitle().contains("Recipe for ")) {
            e.setCancelled(true);
        } else if (e.getView().getTitle().equals("Pay Nuggets")) {
            if (e.getCurrentItem() != null) {
                e.setCancelled(true);
                if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Give")) {
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains("All")) {
                        int amount = playerData.getCredits();
                        playerData.getTrade().addCredits(p, amount);
                        p.openInventory(playerData.getTrade().getInv(p));
                    } else {
                        int amount = Integer.parseInt(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()
                                .replace("Give", "").replace("Nuggets", "").replace(" ", "")));
                        if (amount > playerData.getCredits()) {
                            Text.applyText(p, "&cYou don't have enough Nuggets!");
                            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 2.0f, 1.0f);
                        } else {
                            if (playerData.getTrade() != null) {
                                playerData.getTrade().addCredits(p, amount);
                                p.openInventory(playerData.getTrade().getInv(p));
                            }
                        }
                    }
                }

            }

        } else if (e.getView().getTitle().equals("Your Collections")) {
            for (CollectionType collectionType : EnumSet.allOf(CollectionType.class)) {
                if (e.getCurrentItem().getItemMeta().getDisplayName().contains(collectionType.getItem().getName())) {
                    p.performCommand("collections " + collectionType.toString());
                }
            }
        }
    }
    public void craftingTableUpdate(Inventory inv, Player p, PlayerData playerData) {
        HashMap<Integer, CraftingIngrediant> craftingItems = new HashMap<>();
        int slot = 0;
        List<Integer> slots = Arrays.asList(10, 11, 12, 19, 20, 21, 28, 29, 30);
        for (int integer : slots) {
            if (inv.getItem(integer) != null && inv.getItem(integer)
                    .getType() != Material.AIR) {

                for (Items items : EnumSet.allOf(Items.class)) {
                    if (ChatColor.stripColor(
                            inv.getItem(integer).getItemMeta().getDisplayName()).contains(items.getName())) {
                        if (inv.getItem(integer).getAmount() == 1) {
                            craftingItems.put(slot, new CraftingIngrediant(items,
                                    1));

                        } else {
                            craftingItems.put(slot, new CraftingIngrediant(items,
                                    inv.getItem(integer).getAmount()));

                        }


                    }
                }
            } else {
                craftingItems.put(slot, new CraftingIngrediant(Items.NONE, 0));
            }
            slot++;
        }
        Items recipeItem = null;
        if (craftingItems.size() == 9) {
            for (Items items : EnumSet.allOf(Items.class)) {
                int correctCraftingParts = 0;
                if (items.getCraftingRecipe() != null) {
                    for (int b = 0; b < items.getCraftingRecipe().size(); b++) {
                        if (items.getCraftingRecipe().get(b).getItems().equals(craftingItems.get(b).getItems()) &&
                                items.getCraftingRecipe().get(b).getAmount() <= craftingItems.get(b).getAmount()) {
                            correctCraftingParts++;
                        }
                        if (correctCraftingParts == 9) {
                            boolean collectionFound = false;
                            for (Collection collection : playerData.getCollections()) {
                                HashMap<Tiers, List<Items>> rewardsMap = CollectionType.generateRewardsMap(collection.getCollectionType());
                                for (int i = 0; i < rewardsMap.size(); i++) {
                                    for (Items items1 : rewardsMap.get(Tiers.valueOf("_" + (i + 1)))) {
                                        if (items1.getId().equals(items.getId())) {
                                            collectionFound = true;
                                            if (i + 1 > SKRPG.levelToInt(collection.getTier().toString())) {
                                                Text.applyText(p, "You do not have a high enough collection level to use this!");
                                            } else {
                                                recipeItem = items;
                                            }
                                        }
                                    }
                                }
                            }
                            if (!collectionFound) {
                                recipeItem = items;
                            }
                        }
                    }

                }

            }

        }
        if (recipeItem != null) {
            inv.setItem(25, Items.buildItem(recipeItem));
            inv.getItem(25).setAmount(recipeItem.getCraftingAmount());
            p.getInventory().setContents(p.getInventory().getContents());
        } else {
            if (inv.getItem(25) != null) {
                inv.setItem(25, null);
                p.getInventory().setContents(p.getInventory().getContents());

            }

        }
        skrpg.getServer().getScheduler().scheduleSyncDelayedTask(skrpg, () -> {
            for (HumanEntity he : inv.getViewers()) {
                Player p1 = (Player) he;
                p1.updateInventory();
            }
        }, 1L);
    }
    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent e) {
        if (e.getInventory().getType().equals(InventoryType.WORKBENCH)) {
            Player player = (Player) e.getPlayer();
            player.performCommand("ct");
        } else if (e.getInventory().getType().equals(InventoryType.MERCHANT)) {
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onEntityCombust(EntityCombustEvent event){
        if(event.getEntity() instanceof Zombie){
            event.setCancelled(true);
        }
    }
    @EventHandler
    public void onPlaceEvent(BlockPlaceEvent e) {
        if (e.getPlayer().getGameMode() != GameMode.CREATIVE) {
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onEntityChangeBlockEvent(EntityChangeBlockEvent e) {
        if (e.getEntity().getType().equals(EntityType.SILVERFISH)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onDrownedEvent(EntityTransformEvent e) {
        if (e.getTransformReason().equals(EntityTransformEvent.TransformReason.DROWNED)) { e.setCancelled(true); }
    }
    @EventHandler
    public void noUproot(PlayerInteractEvent event)
    {
        if(event.getAction() == Action.PHYSICAL && event.getClickedBlock().getType() == Material.FARMLAND) {event.setCancelled(true); }
    }
    @EventHandler
    public void onHit(ProjectileHitEvent e) {
        Projectile p = e.getEntity();
        if(p instanceof Arrow)
        {
            p.remove();
        }
        return;
    }
    @EventHandler
    public void onPlayerCraft(CraftItemEvent e) {
        Player p = (Player) e.getWhoClicked();
        e.setCancelled(true);
        Text.applyText(p, "&cYou cannot use your own crafting inventory in SKRPG! &cUse /ct instead.");
        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
    }
}
