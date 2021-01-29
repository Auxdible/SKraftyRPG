package com.auxdible.skrpg.player;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.items.CraftingIngrediant;
import com.auxdible.skrpg.items.ItemType;
import com.auxdible.skrpg.mobs.MobSpawn;
import com.auxdible.skrpg.mobs.npcs.NpcType;
import com.auxdible.skrpg.mobs.npcs.PurchasableItem;
import com.auxdible.skrpg.player.economy.Bank;
import com.auxdible.skrpg.player.economy.BankLevel;
import com.auxdible.skrpg.player.skills.*;
import com.auxdible.skrpg.items.Items;
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
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

public class PlayerListener implements Listener {
    private SKRPG skrpg;
    public PlayerListener(SKRPG skrpg) {
        this.skrpg = skrpg;
    }
    @EventHandler
    public void onClickEntityEvent(PlayerInteractAtEntityEvent e) {
        Player p = e.getPlayer();
        PlayerData playerData = skrpg.getPlayerManager().getPlayerData(p.getUniqueId());
        if (e.getRightClicked().getType().equals(EntityType.VILLAGER)) {
            e.setCancelled(true);
            return;
        }
        if (e.getRightClicked().getCustomName().contains("exportmerchant")) {
            if (!(Integer.parseInt(playerData.getCombat().getLevel().toString().replace("_", "")) >= 7) ||
                    !(Integer.parseInt(playerData.getMining().getLevel().toString().replace("_", "")) >= 7) ||
                    !(Integer.parseInt(playerData.getCrafting().getLevel().toString().replace("_", "")) >= 7) ||
                    !(Integer.parseInt(playerData.getHerbalism().getLevel().toString().replace("_", "")) >= 7)) {
            Text.applyText(p, "&cYou must have Combat 7, Mining 7, Herbalism 7 and Crafting 7 &cin order to use the Export Merchant!");
            return;
            }
            Inventory inv = Bukkit.createInventory(null, 54, "Export Merchant");
            for (int i = 0; i <= 53; i++) {
                inv.setItem(i, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE, 0).setName(" ").asItem());
            }
            inv.setItem(49, null);
            inv.setItem(40, new ItemBuilder(Material.LIGHT_BLUE_STAINED_GLASS_PANE, 0)
                    .setName("&bPlease insert an item.").asItem());
            inv.setItem(31, new ItemBuilder(Material.LIGHT_BLUE_STAINED_GLASS_PANE, 0)
                    .setName("&bPlease insert an item.").asItem());
            ItemStack creditsHead = ItemTweaker.createPlayerHeadFromData("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA" +
                            "6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmQzNzI2YzFiMzY4OTNmZ" +
                            "TMzNDQ5Mzk1ODQ0ZGJhMTZlZjIyNDFkZGI1MzM1NWMyOTdmMDQxMzhmZWJjY2FmIn19fQ==",
                    "8fa29c20-1475-44c7-a77b-1c87bc10b3b2");
            ItemMeta itemMeta = creditsHead.getItemMeta();
            itemMeta.setDisplayName(Text.color("Please put in an item..."));
            creditsHead.setItemMeta(itemMeta);
            inv.setItem(22, creditsHead);
            e.getPlayer().openInventory(inv);
        } else if (e.getRightClicked().getCustomName().contains("banker")) {
            Inventory inv = Bukkit.createInventory(null, 27, "Banker");
            for (int i = 0; i <= 26; i++) {
                inv.setItem(i, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE, 0).setName(" ").asItem());
            }
            List<Integer> slots = Arrays.asList(11, 12, 13, 14, 15);
            for (int i = 0; i < playerData.getBanks().size(); i++) {
                ItemStack bank = ItemTweaker.createPlayerHeadFromData("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmY3NWQxYjc4NWQxOGQ0N2IzZWE4ZjBhN2UwZmQ0YTFmYWU5ZTdkMzIzY2YzYjEzOGM4Yzc4Y2ZlMjRlZTU5In19fQ==", "1d2bf3fe-1b67-495f-995d-435693e90fa0");
                ItemMeta iM = bank.getItemMeta();
                iM.setDisplayName(Text.color("&7Bank &a") + (i + 1));
                iM.setLore(Arrays.asList(" ", Text.color("&7Level: " + playerData.getBanks().get(i).getLevel()
                        .getNameColored()), Text.color("&7Credits: &b" + playerData.getBanks().get(i).getCredits() + " C$")));
                bank.setItemMeta(iM);
                inv.setItem(slots.get(i), bank);
            }
            if (playerData.getBanks().size() != 5) {
                inv.setItem(22, new ItemBuilder(Material.LIME_DYE, 0).setName("&7Buy Bank: &b" + (
                        2500000 * playerData.getBanks().size())).asItem());
            }
            p.openInventory(inv);
        } else if (e.getRightClicked().getCustomName().contains("salesman")) {
            NpcType salesman = null;
            Inventory inventory = Bukkit.createInventory(null, 54, "Salesman");
            if (e.getRightClicked().getCustomName().contains("weaponsalesman")) {
                inventory = Bukkit.createInventory(null, 54, "Weapon Salesman");
                salesman = NpcType.WEAPON_FORGER_SALESMAN;
            }
            List<Integer> outlineSlots = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35, 36, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53);
            for (int i : outlineSlots) {
                inventory.setItem(i, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE, 0).setName(" ").asItem());
            }
            if (salesman != null && salesman.getPurchasableItems() != null) {

                for (PurchasableItem purchasableItem : salesman.getPurchasableItems()) {
                    boolean found = false;
                    List<ItemStack> contents = Arrays.asList(inventory.getContents());
                    for (ItemStack itemStack : contents) {
                        if (itemStack == null && !found) {
                            ItemStack itemStackBuild = Items.buildItem(purchasableItem.getItem());
                            ItemMeta iM = itemStackBuild.getItemMeta();
                            List<String> lore = iM.getLore();
                            lore.add(" ");
                            lore.add(Text.color("&7Cost: &b" + purchasableItem.getCost() + " C$"));
                            iM.setLore(lore);
                            itemStackBuild.setItemMeta(iM);
                            inventory.setItem(contents.indexOf(itemStack), itemStackBuild);
                            found = true;
                        }
                    }
                }
            }
            p.openInventory(inventory);
        }
    }
    @EventHandler
    public void onDamageEvent(EntityDamageByEntityEvent e) {

        if (e.getEntity() instanceof Player) {
            Entity damager = e.getDamager();
            if (e.getDamager() instanceof Arrow) {
                if (((Arrow) e.getDamager()).getShooter() instanceof Skeleton) {
                    damager = (Entity) ((Arrow) e.getDamager()).getShooter();
                }
            }
            e.setCancelled(true);
            Player player = (Player) e.getEntity();
            int damage = 10;
            for (MobType mob : EnumSet.allOf(MobType.class)) {
                if (ChatColor.stripColor(damager.getName()).contains(mob.getName())) {
                    damage = mob.getDamage();
                }
            }
            double damageReduction = (skrpg.getPlayerManager().getPlayerData(player.getUniqueId()).getDefence() /
                    (100.0 + skrpg.getPlayerManager().getPlayerData(player.getUniqueId()).getDefence()));
            int nerfedDamage = (int) Math.round(damage - (damage *
                    damageReduction));
            if (skrpg.getPlayerManager().getPlayerData(player.getUniqueId()).getHp() - nerfedDamage < 0) {
                skrpg.getPlayerManager().getPlayerData(player.getUniqueId()).setHp(0);
            } else {
                skrpg.getPlayerManager().getPlayerData(player.getUniqueId()).setHp(
                        skrpg.getPlayerManager().getPlayerData(player.getUniqueId()).getHp() - nerfedDamage);
            }

            if (skrpg.getPlayerManager().getPlayerData(player.getUniqueId()).getHp() <= 0) {
                player.teleport(Bukkit.getWorld(skrpg.getConfig().getString("rpgWorld")).getSpawnLocation());
                player.playSound(player.getLocation(), Sound.ENTITY_BLAZE_DEATH, 1.0f, 0.5f);
                player.sendTitle(Text.color("&4&l☠"), Text.color("&c&lYOU DIED"), 40, 100, 10);
                Text.applyText(player, "&cYou lost &b" + skrpg.getPlayerManager().getPlayerData(player.getUniqueId()).getCredits() / 2 + " C$&c!");
                skrpg.getPlayerManager().getPlayerData(player.getUniqueId()).setCredits(skrpg.getPlayerManager().getPlayerData(player.getUniqueId()).getCredits() / 2);

                skrpg.getPlayerManager().getPlayerData(player.getUniqueId()).setHp(
                        skrpg.getPlayerManager().getPlayerData(player.getUniqueId()).getMaxHP());
            }
        }    else {
            if (skrpg.getMobManager().getMobData(e.getEntity()) != null) {
                Mob mob = skrpg.getMobManager().getMobData(e.getEntity());
                int damage = 1;
                if (e.getDamager() instanceof Player || e.getDamager() instanceof Arrow) {
                    Player player;
                    e.setCancelled(true);
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
                        } else if (player.getInventory().getItemInMainHand().getItemMeta().getDisplayName()
                                .contains(items.getName())) {
                            damage = (5 + items.getDamage() + ((playerData.getStrength()) / 5)) *
                                    (1 + playerData.getStrength() / 100);
                            if (items.getItemType().equals(ItemType.BOW) && e.getDamager() instanceof Player) {
                                damage = damage / 10;
                            }
                        }
                    }

                    double bonusDamage = 1 + (Integer.parseInt(playerData.getCombat().getLevel().toString()
                            .replace("_", "")) * 0.02);
                    damage = (int) Math.round(damage * bonusDamage);
                    int nerfedDamage;
                    if (damage * Math.round((mob.getMobType().getDefence() /
                            (mob.getMobType().getDefence() + 100))) == 0) {
                        nerfedDamage = damage;
                    } else {
                        nerfedDamage = (damage * Math.round(mob.getMobType().getDefence() /
                                (mob.getMobType().getDefence() + 100)));
                    }
                    if (player.getInventory().getItemInMainHand().getType() != Material.AIR && player.getInventory().getItemInMainHand().getItemMeta().getDisplayName()
                            .contains("Zombie Sword")) {
                        if (mob.getEnt().getType().equals(EntityType.ZOMBIE)) {
                            nerfedDamage = (int) (nerfedDamage * 1.5);
                        }
                    }
                    mob.setCurrentHP(mob.getCurrentHP() - nerfedDamage);
                    ArmorStand damageIndictator = (ArmorStand) mob.getEnt().getWorld()
                            .spawnEntity(mob.getEnt().getLocation(), EntityType.ARMOR_STAND);
                    damageIndictator.setInvisible(true);
                    damageIndictator.setCustomName(Text.color("&c" + damage + " &4&l☄"));
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
                    if (mob.getEnt() instanceof LivingEntity) {
                        LivingEntity livingEntity = (LivingEntity) mob.getEnt();
                        livingEntity.damage(1.0);
                        livingEntity.setHealth(20.0);
                        livingEntity.setVelocity(new Vector(player.getLocation().getDirection().getX() / 3, 0.3, player.getLocation().getDirection().getZ() / 3));
                    }
                    if (mob.getCurrentHP() <= 0) {
                        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 2.0f);

                        for (MobKill mobKill : EnumSet.allOf(MobKill.class)) {
                            if (mob.getEnt().getCustomName()    .contains(Text.color(mobKill.getMobType().getName()))) {
                                if (mobKill.getDrop() != null) {
                                    player.getInventory().addItem(Items.buildItem(mobKill.getDrop()));
                                }
                                if (mobKill.getRareDrops() != null) {
                                    double random = Math.random();
                                    double random2 = Math.random();
                                    if (mobKill.getRareDrops() != null) {
                                        for (Drop drop : mobKill.getRareDrops()) {
                                            if (drop.getChance() >= random) {
                                                player.getInventory().addItem(Items.buildItem(drop.getItems()));
                                                Text.applyText(player, "&r&5&lSPECIAL DROP! &r&8| " + drop.getItems()
                                                        .getRarity().getColor() + drop.getItems().getName());
                                                Player finalPlayer = player;
                                                new BukkitRunnable() {
                                                    private int seconds = 1;
                                                    @Override
                                                    public void run() {
                                                        if (seconds == 1) {
                                                            finalPlayer.playSound(finalPlayer.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 0.0f, 1.0f);
                                                        } else if (seconds == 2) {
                                                            finalPlayer.playSound(finalPlayer.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 0.0f, 0.5f);
                                                        } else if (seconds == 3) {
                                                            finalPlayer.playSound(finalPlayer.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 0.0f, 2.0f);
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
                                if (playerData.getCombat().getXpTillNext() >= Level.valueOf("_" +
                                        (Integer.parseInt(playerData.getCombat().getLevel().toString()
                                                .replace("_", "")) + 1)).getXpRequired()) {
                                    int creditsEarned = playerData.getCombat().getLevel().getXpRequired() / 2;
                                    playerData.getCombat().setLevel(Integer.parseInt(playerData.getCombat()
                                            .getLevel().toString()
                                            .replace("_", "")) + 1);
                                    playerData.getCombat().setXpTillNext(playerData.getCombat().getXpTillNext() -
                                            playerData.getCombat().getLevel().getXpRequired());
                                    Text.applyText(player, "&8&m>                                          ");
                                    Text.applyText(player, "&6&l           SKILL UP!");
                                    Text.applyText(player, " ");
                                    Text.applyText(player, "&7You leveled up to Combat &e" + playerData.getCombat().getLevel().toString().replace("_", ""));
                                    Text.applyText(player, "&7Earned &4+2 Strength ☄ &r&7and &c2% more damage!");
                                    Text.applyText(player, "&7+&b" + creditsEarned + " C$ Credits");
                                    Text.applyText(player, "&7Total Extra Damage: &c" + (2 *
                                            Integer.parseInt(playerData.getCombat()
                                                    .getLevel().toString()
                                                    .replace("_", ""))) + "%");
                                    Text.applyText(player, "&8&m>                                          ");
                                    playerData.setBaseStrength(playerData.getBaseStrength() + 2);
                                    playerData.setCredits(playerData.getCredits() + creditsEarned);
                                }
                                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                                        Text.color("&e+ " + mobKill.getXpGiven() + " Combat XP (" + playerData.getCombat()
                                                .getXpTillNext() + "/" + Level.valueOf("_" +
                                                (Integer.parseInt(playerData.getCombat().getLevel().toString()
                                                        .replace("_", "")) + 1)).getXpRequired() + ")")
                                ));
                            }
                        }

                        LivingEntity deathEntity = (LivingEntity) mob.getEnt().getWorld().spawnEntity(mob.getEnt()
                                .getLocation(), mob.getEnt().getType());
                        if (deathEntity instanceof Zombie) {
                            ((Zombie) deathEntity).setBaby(false);
                        }
                        for (MobSpawn mobSpawn : skrpg.getMobSpawnManager().getMobSpawns()) {
                            if (mobSpawn.getCurrentlySpawnedMobs() != null && mobSpawn.getCurrentlySpawnedMobs().size() != 0) {
                                mobSpawn.getCurrentlySpawnedMobs()
                                        .removeIf(mobs -> mob.getEnt().getEntityId() == mobs.getEnt().getEntityId());
                            }

                        }
                        mob.getEnt().remove();
                        deathEntity.setHealth(0.0);
                        skrpg.getMobManager().removeMob(mob);
                        skrpg.getLogger().info("Removed mob!");
                        playerData.setCredits(playerData.getCredits() + mob.getMobType().getLevel());


                    }

                }
            }
        }
    }
    @EventHandler
    public void onUseEvent(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        PlayerData playerData = skrpg.getPlayerManager().getPlayerData(p.getUniqueId());
        if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)||e.getAction().equals(Action.RIGHT_CLICK_AIR)){
            if(e.getClickedBlock() != null && e.getClickedBlock().getType() == Material.CRAFTING_TABLE){
                e.setCancelled(true);
                p.performCommand("ct");
            }
        }
        if (!e.getAction().equals(Action.RIGHT_CLICK_AIR) && !e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) { return; }
        if (p.getInventory().getItemInMainHand().getType() != Material.AIR) {
            if (p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains("Starter Sword")) {
                if (playerData.getEnergy() < 50) {
                    Text.applyText(p, "&cYou need more Energy to do this!");
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                    return;
                }
                Text.applyText(p, "&7Used &eSpeed Boost&7! &7[&e-50 Energy&7]");
                p.playSound(p.getLocation(), Sound.BLOCK_BREWING_STAND_BREW, 1.0f, 1.0f);
                playerData.setEnergy(playerData.getEnergy() - 50);
                playerData.setBaseSpeed(playerData.getBaseSpeed() + 25);
                new BukkitRunnable() {
                    @Override
                    public void run() {

                        playerData.setBaseSpeed(playerData.getBaseSpeed() - 25);
                    }
                }.runTaskLater(skrpg, 200);
            }
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
        PlayerData playerData = skrpg.getPlayerManager().getPlayerData(p.getUniqueId());
        if (p.getGameMode().equals(GameMode.CREATIVE)) { return; }
        for (MineBlock mineBlock : EnumSet.allOf(MineBlock.class)) {
            if (e.getBlock().getType() == mineBlock.getBlockObtainedFrom()) {
                p.getInventory().addItem(Items.buildItem(mineBlock.getCommonDrop()));
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
                                private int seconds = 1;
                                @Override
                                public void run() {
                                    if (seconds == 1) {
                                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 0.0f, 1.0f);
                                    } else if (seconds == 2) {
                                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 0.0f, 0.5f);
                                    } else if (seconds == 3) {
                                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 0.0f, 2.0f);
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
                if (playerData.getMining().getXpTillNext() >= Level.valueOf("_" +
                        (Integer.parseInt(playerData.getMining().getLevel().toString()
                                .replace("_", "")) + 1)).getXpRequired()) {
                    int creditsEarned = playerData.getMining().getLevel().getXpRequired() / 2;
                    playerData.getMining().setLevel(Integer.parseInt(playerData.getMining()
                            .getLevel().toString()
                            .replace("_", "")) + 1);
                    playerData.getMining().setXpTillNext(playerData.getMining().getXpTillNext() -
                            playerData.getMining().getLevel().getXpRequired());
                    Text.applyText(p, "&8&m>                                          ");
                    Text.applyText(p, "&6&l           SKILL UP!");
                    Text.applyText(p, " ");
                    Text.applyText(p, "&7You leveled up to Mining &e" + playerData.getMining().getLevel().toString().replace("_", ""));
                    Text.applyText(p, "&7Earned &a+2 Defence ✿ &r&7and &64% more double drop chance!");
                    Text.applyText(p, "&7+&b" + creditsEarned + " C$ Credits");
                    Text.applyText(p, "&7Total Double Drop Chance: &6" + (4 *
                            Integer.parseInt(playerData.getMining()
                                    .getLevel().toString()
                                    .replace("_", ""))) + "%");
                    Text.applyText(p, "&8&m>                                          ");
                    playerData.setBaseDefence(playerData.getBaseDefence() + 2);
                    playerData.setCredits(playerData.getCredits() + creditsEarned);
                }
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
                                private int seconds = 1;
                                @Override
                                public void run() {
                                    if (seconds == 1) {
                                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 0.0f, 1.0f);
                                    } else if (seconds == 2) {
                                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 0.0f, 0.5f);
                                    } else if (seconds == 3) {
                                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 0.0f, 2.0f);
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

                if (playerData.getHerbalism().getXpTillNext() >= Level.valueOf("_" +
                        (Integer.parseInt(playerData.getHerbalism().getLevel().toString()
                                .replace("_", "")) + 1)).getXpRequired()) {
                    int creditsEarned = playerData.getHerbalism().getLevel().getXpRequired() / 2;
                    playerData.getHerbalism().setLevel(Integer.parseInt(playerData.getHerbalism()
                            .getLevel().toString()
                            .replace("_", "")) + 1);
                    playerData.getHerbalism().setXpTillNext(playerData.getHerbalism().getXpTillNext() -
                            playerData.getHerbalism().getLevel().getXpRequired());
                    Text.applyText(p, "&8&m>                                          ");
                    Text.applyText(p, "&6&l           SKILL UP!");
                    Text.applyText(p, " ");
                    Text.applyText(p, "&7You leveled up to Herbalism &e" + playerData.getHerbalism().getLevel().toString().replace("_", ""));
                    Text.applyText(p, "&7Earned &c+2 HP ♥ &r&7and &64% more double drop chance!");
                    Text.applyText(p, "&7+&b" + creditsEarned + " C$ Credits");
                    Text.applyText(p, "&7Total Double Drop Chance: &6" + (4 *
                            Integer.parseInt(playerData.getHerbalism()
                                    .getLevel().toString()
                                    .replace("_", ""))) + "%");
                    Text.applyText(p, "&8&m>                                         ");
                    playerData.setBaseHP(playerData.getBaseHP() + 2);
                    playerData.setCredits(playerData.getCredits() + creditsEarned);
                }
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
            e.setCancelled(true);
            playerData.getCrafting().setXpTillNext(playerData.getCrafting().getXpTillNext() +
                    10);
            playerData.getCrafting().setTotalXP(playerData.getCrafting().getTotalXP()
                    + 10);
            if (playerData.getCrafting().getXpTillNext() >= Level.valueOf("_" +
                    (Integer.parseInt(playerData.getCrafting().getLevel().toString()
                            .replace("_", "")) + 1)).getXpRequired()) {
                int creditsEarned = playerData.getCrafting().getLevel().getXpRequired() / 2;
                playerData.getCrafting().setLevel(Integer.parseInt(playerData.getCrafting()
                        .getLevel().toString()
                        .replace("_", "")) + 1);
                playerData.getCrafting().setXpTillNext(playerData.getCrafting().getXpTillNext() -
                        playerData.getCrafting().getLevel().getXpRequired());
                Text.applyText(p, "&8&m>                                          ");
                Text.applyText(p, "&6&l           SKILL UP!");
                Text.applyText(p, " ");
                Text.applyText(p, "&7You leveled up to Crafting &e" + playerData.getCrafting().getLevel().toString().replace("_", ""));
                Text.applyText(p, "&7Earned &a+1 Defence ✿ &r&7and &4+1 Strength ☄");
                Text.applyText(p, "&7+&b" + creditsEarned + " C$ Credits");
                Text.applyText(p, "&8&m>                                         ");
                playerData.setBaseDefence(playerData.getBaseDefence() + 1);
                playerData.setBaseStrength(playerData.getBaseStrength() + 1);
                playerData.setCredits(playerData.getCredits() + creditsEarned);
            }
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
            if (e.getSlot() == 25 && e.getAction().equals(InventoryAction.PLACE_ALL)) {
                e.setCancelled(true);
                return;
            }
            if (e.getCurrentItem() != null) {
                if (e.getCurrentItem().getType() == Material.BLACK_STAINED_GLASS_PANE ||
                        e.getCurrentItem().getType() == Material.ARROW) {

                    e.setCancelled(true);
                }
            }
            if (e.getSlot() == 25 && e.getInventory().getItem(25) == null && e.getInventory().equals(p.getOpenInventory().getTopInventory())) {
                e.setCancelled(true);
            }
            if (e.getSlot() == 25 && e.getInventory().getItem(25) != null) {
                    for (int integer : slots) {
                        e.getInventory().setItem(integer, null);
                    }
                for (Items items : EnumSet.allOf(Items.class)) {
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(items.getName())) {
                        playerData.getCrafting().setXpTillNext(playerData.getCrafting().getXpTillNext() +
                                items.getCraftingXPGained());
                        playerData.getCrafting().setTotalXP(playerData.getCrafting().getTotalXP()
                                + items.getCraftingXPGained());
                        if (playerData.getCrafting().getXpTillNext() >= Level.valueOf("_" +
                                (Integer.parseInt(playerData.getCrafting().getLevel().toString()
                                        .replace("_", "")) + 1)).getXpRequired()) {
                            int creditsEarned = playerData.getCrafting().getLevel().getXpRequired() / 2;
                            playerData.getCrafting().setLevel(Integer.parseInt(playerData.getCrafting()
                                    .getLevel().toString()
                                    .replace("_", "")) + 1);
                            playerData.getCrafting().setXpTillNext(playerData.getCrafting().getXpTillNext() -
                                    playerData.getCrafting().getLevel().getXpRequired());
                            Text.applyText(p, "&8&m>                                          ");
                            Text.applyText(p, "&6&l           SKILL UP!");
                            Text.applyText(p, " ");
                            Text.applyText(p, "&7You leveled up to Crafting &e" + playerData.getCrafting().getLevel().toString().replace("_", ""));
                            Text.applyText(p, "&7Earned &a+1 Defence ✿ &r&7and &4+1 Strength ☄");
                            Text.applyText(p, "&7+&b" + creditsEarned + " C$ Credits");
                            Text.applyText(p, "&8&m>                                         ");
                            playerData.setBaseDefence(playerData.getBaseDefence() + 1);
                            playerData.setBaseStrength(playerData.getBaseStrength() + 1);
                            playerData.setCredits(playerData.getCredits() + creditsEarned);
                        }
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

                    e.setCancelled(true);
            }

            HashMap<Integer, CraftingIngrediant> craftingItems = new HashMap<>();
                    int slot = 0;

                    for (int integer : slots)  {
                        if (e.getInventory().getItem(integer) != null && e.getInventory().getItem(integer)
                                .getType() != Material.AIR) {

                            for (Items items : EnumSet.allOf(Items.class)) {
                                if (ChatColor.stripColor(
                                        e.getInventory().getItem(integer).getItemMeta().getDisplayName()).contains(items.getName())) {
                                        if (e.getInventory().getItem(integer).getAmount() == 1) {
                                            craftingItems.put(slot, new CraftingIngrediant(items,
                                                    1));
                                            skrpg.getLogger().info("Added " + slot + " with item " + items.getName() + " with amount " + 1);
                                        } else {
                                            craftingItems.put(slot, new CraftingIngrediant(items,
                                                    e.getInventory().getItem(integer).getAmount()));
                                            skrpg.getLogger().info("Added " + slot + " with item " + items.getName() + " with amount " + e.getInventory().getItem(integer).getAmount());
                                        }


                                }
                            }
                        } else {
                            craftingItems.put(slot, new CraftingIngrediant(Items.NONE, 0));
                        }
                        slot++;
                    }
                    if (craftingItems.size() == 9) {
                        for (Items items : EnumSet.allOf(Items.class)) {
                            int correctCraftingParts = 0;
                            if (items.getCraftingRecipe() != null) {
                                for (int b = 0; b < items.getCraftingRecipe().size(); b++) {
                                    if (items.getCraftingRecipe().get(b) == null) {
                                        skrpg.getLogger().info("Null crafting part!");
                                    }
                                    if (items.getCraftingRecipe().get(b).getItems().equals(craftingItems.get(b).getItems()) &&
                                    items.getCraftingRecipe().get(b).getAmount() == craftingItems.get(b).getAmount()) {
                                        correctCraftingParts++;
                                    } else {
                                        if (p.getOpenInventory().getItem(25) != null) {
                                            p.getOpenInventory().setItem(25, null);
                                        }
                                    }
                                    if (correctCraftingParts == 9) {
                                        p.getOpenInventory().setItem(25, Items.buildItem(items));
                                        p.getOpenInventory().getItem(25).setAmount(items.getCraftingAmount());
                                        return;
                                    }
                                }

                            }

                        }
                    }
        } else if (e.getView().getTitle().equals("SKRPG Menu")) {
            e.setCancelled(true);
            if (e.getCurrentItem().getItemMeta().getDisplayName().equals(Text.color("&aCrafting Table"))) {
                p.performCommand("ct");
            } else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(Text.color("&aYour Statistics"))) {
                p.performCommand("stats");
            } else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(Text.color("&aSkills"))) {
                p.performCommand("skills");
            }
        } else if (e.getView().getTitle().equals("Your Skills")) {
            e.setCancelled(true);
        } else if (e.getView().getTitle().equals("Export Merchant")) {
            for (Items items : EnumSet.allOf(Items.class)) {
                if (e.getCurrentItem() != null) {
                    if (e.getCurrentItem().getItemMeta().getDisplayName() != null) {
                        if (e.getCurrentItem().getItemMeta().getDisplayName().contains(items.getName())) {

                            playerData.setCredits(playerData.getCredits() + items.getSellPrice());
                            p.playSound(p.getLocation(), Sound.ENTITY_PHANTOM_FLAP, 1.0f, 2.0f);
                            e.getCurrentItem().setAmount(e.getCurrentItem().getAmount() - 1);

                            Text.applyText(p, "&7+ &b" + items.getSellPrice() + " C$ Credits &r&7from " +
                                    items.getRarity().getColor() + items.getName());
                            ItemMeta iM = e.getView().getTopInventory().getItem(22).getItemMeta();
                            iM.setDisplayName(Text.color("&7Sold for +&b" + items.getSellPrice() + " C$"));
                            e.getView().getTopInventory().getItem(22).setItemMeta(iM);
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
                            return;
                        } else {
                            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 2.0f, 0.25f);
                            p.closeInventory();
                            Text.applyText(p, "&aYou bought a bank for &b" + (250000 * playerData.getBanks().size()) + " C$");
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
                        inv.setItem(10, new ItemBuilder(Material.CHEST, 0).setName("&7Deposit All").asItem());
                        inv.setItem(12, new ItemBuilder(Material.CHEST, 0).setName("&7Deposit &b1000000 C$").asItem());
                        inv.setItem(14, new ItemBuilder(Material.CHEST, 0).setName("&7Deposit &b100000 C$").asItem());
                        inv.setItem(16, new ItemBuilder(Material.CHEST, 0).setName("&7Deposit &b10000 C$").asItem());
                        inv.setItem(20, new ItemBuilder(Material.CHEST, 0).setName("&7Deposit &b100 C$").asItem());
                        inv.setItem(24, new ItemBuilder(Material.CHEST, 0).setName("&7Deposit &b10 C$").asItem());
                        inv.setItem(28, new ItemBuilder(Material.HOPPER, 0).setName("&7Withdraw All").asItem());
                        inv.setItem(30, new ItemBuilder(Material.HOPPER, 0).setName("&7Withdraw &b1000000 C$").asItem());
                        inv.setItem(32, new ItemBuilder(Material.HOPPER, 0).setName("&7Withdraw &b100000 C$").asItem());
                        inv.setItem(34, new ItemBuilder(Material.HOPPER, 0).setName("&7Withdraw &b10000 C$").asItem());
                        inv.setItem(38, new ItemBuilder(Material.HOPPER, 0).setName("&7Withdraw &b100 C$").asItem());
                        inv.setItem(42, new ItemBuilder(Material.HOPPER, 0).setName("&7Withdraw &b10 C$").asItem());
                        if (!bank.getLevel().equals(BankLevel.SUPER_DELUXE)) {
                            BankLevel bankLevel = null;
                            for (BankLevel bankLevel1 : EnumSet.allOf(BankLevel.class)) {
                                if (bankLevel1.getLevel() == bank.getLevel().getLevel() + 1) {
                                    bankLevel = bankLevel1;
                                }
                            }
                            inv.setItem(22, new ItemBuilder(Material.EMERALD_BLOCK, 0).setName("&aUpgrade to " + bankLevel.getNameColored()).setLore(Arrays.asList(" ",
                                    Text.color("&7Cost: &b" + bankLevel.getCost() + " C$"))).asItem());
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
                            Text.applyText(p, "&aDeposited &b" + amount + " C$&7!");
                            bank.deposit(amount);
                            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 2.0f, 2.0f);
                        } else {
                            int amount = Integer.parseInt(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()
                                    .replace("Deposit", "").replace("C$", "").replace(" ", "")));
                            if (amount > playerData.getCredits()) {
                                Text.applyText(p, "&cYou don't have enough credits!");
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
                                Text.applyText(p, "&aDeposited &b" + amount + " C$&7!");
                                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 2.0f, 2.0f);
                            }
                        }
                    } else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Withdraw")) {
                        if (e.getCurrentItem().getItemMeta().getDisplayName().contains("All")) {
                            p.playSound(p.getLocation(), Sound.ENTITY_PHANTOM_FLAP, 2.0f, 2.0f);
                            Text.applyText(p, "&aWithdrew &b" + bank.getCredits() + " C$&7!");
                            playerData.setCredits(playerData.getCredits() + bank.getCredits());
                            bank.withdraw(bank.getCredits());

                        } else {
                            int amount = Integer.parseInt(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()
                                    .replace("Withdraw", "").replace("C$", "").replace(" ", "")));
                            if (amount > bank.getCredits()) {
                                Text.applyText(p, "&cYour bank doesn't have enough credits!");
                                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 2.0f, 1.0f);
                            } else {
                                p.playSound(p.getLocation(), Sound.ENTITY_PHANTOM_FLAP, 2.0f, 2.0f);
                                playerData.setCredits(playerData.getCredits() + amount);
                                bank.withdraw(amount);
                                Text.applyText(p, "&aWithdrew &b" + amount + " C$&7!");
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
                            Text.applyText(p, "&cYou don't have enough credits to upgrade this account!");
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
        } else if (e.getView().getTitle().contains("Salesman")) {
            e.setCancelled(true);
            if (e.getCurrentItem() != null) {
                if (e.getCurrentItem().getItemMeta() != null) {
                    int cost = 0;
                    for (String lore : e.getCurrentItem().getItemMeta().getLore()) {
                        if (lore.contains("Cost:")) {
                            skrpg.getLogger().info("Cost");
                            cost = Integer.parseInt(ChatColor.stripColor(lore).replace("Cost:", "")
                            .replace("C$", "").replace(" ", ""));
                        }
                    }
                    for (Items items : EnumSet.allOf(Items.class)) {
                        if (items.getName().contains(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()))) {

                            if (cost > playerData.getCredits()) {
                                Text.applyText(p, "&cYou don't have enough credits to buy this!");
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
                            Text.applyText(p, "&aYou purchased " + Items.buildItem(items).getItemMeta().getDisplayName() + " for &b" + cost + " C$");
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
                    }
                }

            }
        }
    }
    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent e) {
        if (e.getInventory().getType().equals(InventoryType.WORKBENCH)) {
            Player player = (Player) e.getPlayer();
            player.performCommand("ct");
        }
    }
    @EventHandler
    public void onEntityCombust(EntityCombustEvent event){
        if(event.getEntity() instanceof Zombie){
            event.setCancelled(true);
        }
    }
}
