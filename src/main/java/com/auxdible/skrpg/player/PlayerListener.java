package com.auxdible.skrpg.player;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.items.*;
import com.auxdible.skrpg.items.abilities.Abilities;
import com.auxdible.skrpg.items.enchantments.Enchantment;
import com.auxdible.skrpg.items.enchantments.Enchantments;
import com.auxdible.skrpg.items.food.FoodBase;
import com.auxdible.skrpg.items.food.Foods;
import com.auxdible.skrpg.mobs.MobSpawn;
import com.auxdible.skrpg.mobs.npcs.NpcType;
import com.auxdible.skrpg.mobs.npcs.PurchasableItem;
import com.auxdible.skrpg.mobs.npcs.NPC;
import com.auxdible.skrpg.player.collections.Collection;
import com.auxdible.skrpg.player.collections.CollectionType;
import com.auxdible.skrpg.player.collections.Tiers;
import com.auxdible.skrpg.player.economy.Bank;
import com.auxdible.skrpg.player.economy.BankLevel;
import com.auxdible.skrpg.player.economy.TradeItem;
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
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
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
                    if (skrpg.getNpcCooldown().contains(p)) {
                        return;
                    }
                    skrpg.getNpcCooldown().add(p);
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
                if (damagerData.getRegion() == null) { return; }
                if (damagerData.getRegion().getName().contains("(PVP)")) {
                    skrpg.getPlayerManager().getPlayerData(e.getEntity().getUniqueId()).getPlayerActionManager()
                            .damageByPlayer(damager, damage);
                } else {
                    e.setCancelled(true);
                }
            } else {
                Player player = (Player) e.getEntity();
                PlayerData playerData = skrpg.getPlayerManager().getPlayerData(player.getUniqueId());
                int damage = 0;
                PersistentDataContainer persistentDataContainer = damagingEntity.getPersistentDataContainer();
                if (persistentDataContainer.get(new NamespacedKey(skrpg, "mobId"), PersistentDataType.STRING) != null) {
                    MobType mob = MobType.getMob(persistentDataContainer.get(new NamespacedKey(skrpg, "mobId"), PersistentDataType.STRING));
                    damage = mob.getDamage();

                }
                playerData.getPlayerActionManager().damagePlayer(damage);
            }
        }  else {

            if (skrpg.getMobManager().getMobData(e.getEntity()) != null) {
                Mob mob = skrpg.getMobManager().getMobData(e.getEntity());



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
                int damage = playerData.getPlayerActionManager().calculateDamage();
                ItemInfo handInfo = ItemInfo.parseItemInfo(player.getInventory().getItemInMainHand());
                if (handInfo != null) {
                    if (handInfo.getItem().getItemType().equals(ItemType.BOW) && e.getDamager() instanceof Player) {
                        damage = damage / 10;
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
            } else if (e.getClickedBlock() != null && e.getClickedBlock().getType() == Material.ENCHANTING_TABLE) {
                e.setCancelled(true);
                playerData.getPlayerActionManager().buildRunicTable();
            } else if (e.getClickedBlock() != null && e.getClickedBlock().getType() == Material.SMOKER) {
                e.setCancelled(true);
                playerData.getPlayerActionManager().openStovetop();
            } else if (e.getClickedBlock() != null && e.getClickedBlock().getType() == Material.CHEST) {
                e.setCancelled(true);
            } else if (e.getClickedBlock() != null && e.getClickedBlock().getType() == Material.BARREL) {
                e.setCancelled(true);
            }
        }
        if (!e.getAction().equals(Action.RIGHT_CLICK_AIR) && !e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) { return; }
        ItemInfo itemInfo = ItemInfo.parseItemInfo(p.getInventory().getItemInMainHand());
        if (itemInfo != null) {
            if (itemInfo.getItem().getItemType().equals(ItemType.FOOD)) {
                if (Foods.getFood(itemInfo.getItem()).getFoodAction() != null) {
                    p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_BURP, 1.0f, 1.0f);
                    Text.applyText(p, "&aYou ate " + itemInfo.getItem().getName() + "!");
                    if (p.getInventory().getItemInMainHand().getAmount() != 1) {
                        p.getInventory().getItemInMainHand().setAmount(p.getInventory().getItemInMainHand().getAmount() - 1);
                    } else {
                        p.getInventory().removeItem(p.getInventory().getItemInMainHand());
                    }
                    Foods.getFood(itemInfo.getItem()).getFoodAction().getFoodAction().onEat(p, skrpg, playerData);
                }
            }
        }
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
        if (p.getGameMode() == GameMode.CREATIVE) {
            return;
        }
        PlayerData playerData = skrpg.getPlayerManager().getPlayerData(p.getUniqueId());

        playerData.getPlayerActionManager().onBreakBlock(e.getBlock());
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
        } else if (e.getView().getTitle().equals("Enchant Item")) {
            Player p  = (Player) e.getPlayer();
            if (e.getInventory().getItem(13) != null) {
                p.getInventory().addItem(e.getInventory().getItem(13));
                e.getInventory().removeItem(e.getInventory().getItem(13));
            }
        } else if (e.getView().getTitle().equals("Apply Runic Stone")) {
            Player p  = (Player) e.getPlayer();
            if (e.getInventory().getItem(30) != null) {
                p.getInventory().addItem(e.getInventory().getItem(30));
                e.getInventory().removeItem(e.getInventory().getItem(30));
            }
            if (e.getInventory().getItem(32) != null) {
                p.getInventory().addItem(e.getInventory().getItem(32));
                e.getInventory().removeItem(e.getInventory().getItem(32));
            }
        } else if (e.getView().getTitle().equals("Cook Food")) {
            Player p  = (Player) e.getPlayer();
            if (e.getInventory().getItem(30) != null) {
                p.getInventory().addItem(e.getInventory().getItem(30));
                e.getInventory().removeItem(e.getInventory().getItem(30));
            }
            if (e.getInventory().getItem(32) != null) {
                p.getInventory().addItem(e.getInventory().getItem(32));
                e.getInventory().removeItem(e.getInventory().getItem(32));
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

        } else if (e.getView().getTitle().equals("Smelt")) {
            Player p  = (Player) e.getPlayer();
            if (e.getInventory().getItem(22) != null) {
                p.getInventory().addItem(e.getInventory().getItem(22));
                e.getInventory().removeItem(e.getInventory().getItem(22));
            }
        } else if (e.getView().getTitle().equals("Process")) {
            Player p  = (Player) e.getPlayer();
            if (e.getInventory().getItem(22) != null) {
                p.getInventory().addItem(e.getInventory().getItem(22));
                e.getInventory().removeItem(e.getInventory().getItem(22));
            }
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

                ItemInfo itemInfo = ItemInfo.parseItemInfo(e.getCurrentItem());
                if (itemInfo != null) {
                    Items items = itemInfo.getItem();
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
            } else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(Text.color("&5Runic Table"))) {
                playerData.getPlayerActionManager().buildRunicTable();
            } else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(Text.color("&aManage Banks"))) {
                playerData.getPlayerActionManager().openBankMenu();
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
                } else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Runics")) {
                    p.performCommand("skills runics");
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
                if (e.getCurrentItem() != null) {
                    ItemInfo itemInfo = ItemInfo.parseItemInfo(e.getCurrentItem());
                    if (itemInfo != null) {
                        Items items = itemInfo.getItem();
                        if (e.getCurrentItem().getItemMeta().getDisplayName() != null) {
                            if (e.getCurrentItem().getItemMeta().getDisplayName().contains(items.getName())) {
                                if (playerData.getSellAboveRarity().getPriority() < items.getRarity().getPriority() || playerData.getSellAboveRarity() == Rarity.COMMON) {
                                    Text.applyText(p, "&cYour sell above rarity setting prevented you from selling this!");
                                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);

                                } else {
                                    if (items.getSellMerchant() == sellMerchant) {
                                        int multiplier = 1;
                                        if (itemInfo.getQuality() != null) {
                                            multiplier = itemInfo.getQuality().getStar();
                                        }
                                        playerData.setCredits(playerData.getCredits() + ((items.getSellPrice() * multiplier) * e.getCurrentItem().getAmount()));
                                        p.playSound(p.getLocation(), Sound.ENTITY_PHANTOM_FLAP, 1.0f, 2.0f);


                                        Text.applyText(p, "&7+ &6" + ((items.getSellPrice() * multiplier) * e.getCurrentItem().getAmount()) + " Nuggets &r&7from " +
                                                itemInfo.getRarity().getColor() + items.getName());
                                        ItemMeta iM = e.getView().getTopInventory().getItem(22).getItemMeta();
                                        iM.setDisplayName(Text.color("&7Sold for +&6" + items.getSellPrice() + " Nuggets"));
                                        e.getView().getTopInventory().getItem(22).setItemMeta(iM);
                                        p.getInventory().removeItem(e.getCurrentItem());
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
                            double amount = playerData.getCredits();
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
                            double amount = Integer.parseInt(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()
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
        } else if (e.getView().getTitle().contains("Salesman")) {
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

                    if (e.getCurrentItem() != null) {
                        ItemInfo itemInfo = ItemInfo.parseItemInfo(e.getCurrentItem());
                        if (itemInfo != null) {
                            e.setCancelled(true);
                            playerData.getTrade().addItem(p, new TradeItem(itemInfo, e.getCurrentItem().getAmount()));
                            e.getClickedInventory().setItem(e.getSlot(), null);
                        }
                    }
                    for (Items items : EnumSet.allOf(Items.class)) {

                            if (ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equals(items.getName())) {


                                break;
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
        e.getView().getTitle().equals("Your Crafting") || e.getView().getTitle().equals("Your Mining") || e.getView().getTitle().contains("| Collection Viewer")
                || e.getView().getTitle().equals(Text.color("&5Your Runics"))) {
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
                        double amount = playerData.getCredits();
                        playerData.getTrade().addCredits(p, amount);
                        p.openInventory(playerData.getTrade().getInv(p));
                    } else {
                        double amount = Double.parseDouble(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()
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
            e.setCancelled(true);
            for (CollectionType collectionType : EnumSet.allOf(CollectionType.class)) {
                if (e.getCurrentItem().getItemMeta().getDisplayName().contains(collectionType.getItem().getName())) {
                    p.performCommand("collections " + collectionType.toString());
                }
            }
        } else if (e.getView().getTitle().contains("Runic Table")) {
            e.setCancelled(true);
            if (e.getCurrentItem() != null) {
                if (e.getCurrentItem().getItemMeta().getDisplayName().contains(Text.color("&5Enchant Item"))) {
                    Inventory inv = Bukkit.createInventory(null, 45, "Enchant Item");
                    for (int i = 0; i <= 8; i++) {
                        inv.setItem(i, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE, 0).asItem());
                    }
                    for (int i = 9; i <= 17; i++) {
                        inv.setItem(i, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE, 0).asItem());
                    }
                    for (int i = 18; i <= 26; i++) {
                        inv.setItem(i, new ItemBuilder(Material.PINK_STAINED_GLASS_PANE, 0).asItem());
                    }
                    for (int i = 27; i <= 44; i++) {
                        inv.setItem(i, new ItemBuilder(Material.MAGENTA_STAINED_GLASS_PANE, 0).asItem());
                    }
                    inv.setItem(13, null);
                    inv.setItem(20, new ItemBuilder(Material.PINK_DYE, 0).setName(Text.color("&7Use &5250 Runic Points ஐ"))
                            .setLore(Arrays.asList(" ", Text.color("&7Level Chance: &51-2"), " ")).asItem());
                    inv.setItem(24, new ItemBuilder(Material.PURPLE_GLAZED_TERRACOTTA, 0).setName(Text.color("&7Use &5500 Runic Points ஐ"))
                            .setLore(Arrays.asList(" ", Text.color("&7Level Chance: &53-4"), " ")).asItem());
                    inv.setItem(31, new ItemBuilder(Material.DRAGON_BREATH, 0).setName(Text.color("&7Use &51000 Runic Points ஐ"))
                            .setLore(Arrays.asList(" ", Text.color("&7Level: &55"), " ")).asItem());
                    p.openInventory(inv);
                } else if (e.getCurrentItem().getItemMeta().getDisplayName().contains(Text.color("&5Apply Runic Stone"))) {
                    if (SKRPG.levelToInt(playerData.getRunics().getLevel().toString()) < 7) {
                        Text.applyText(p, "&cYou need to increase your &5Runic Level &cto do this! (Required Level: &57&c)");
                        p.playSound(p.getLocation(), Sound.BLOCK_RESPAWN_ANCHOR_DEPLETE, 1.0f, 1.0f);
                        return;
                    }
                    Inventory inv = Bukkit.createInventory(null, 45, "Apply Runic Stone");
                    for (int i = 0; i <= 8; i++) {
                        inv.setItem(i, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE, 0).asItem());
                    }
                    for (int i = 9; i <= 17; i++) {
                        inv.setItem(i, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE, 0).asItem());
                    }
                    for (int i = 18; i <= 26; i++) {
                        inv.setItem(i, new ItemBuilder(Material.PINK_STAINED_GLASS_PANE, 0).asItem());
                    }
                    for (int i = 27; i <= 44; i++) {
                        inv.setItem(i, new ItemBuilder(Material.MAGENTA_STAINED_GLASS_PANE, 0).asItem());
                    }
                    inv.setItem(30, null);
                    inv.setItem(32, null);
                    inv.setItem(31, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE, 0).setName(" ").setLore(
                            Arrays.asList(Text.color("&5☚ &7Your Item"), Text.color("&7Runic Stone &5☛"), " ")
                    ).asItem());
                    inv.setItem(22, new ItemBuilder(Material.DRAGON_BREATH, 0).setName("&7Click to &5Combine&7.").asItem());
                    inv.setItem(13, null);
                    p.openInventory(inv);
                } else if (e.getCurrentItem().getItemMeta().getDisplayName().contains(Text.color("&5Destroy an Item"))) {
                    if (SKRPG.levelToInt(playerData.getRunics().getLevel().toString()) < 3) {
                        Text.applyText(p, "&cYou need to increase your &5Runic Level &cto do this! (Required Level: &53&c)");
                        p.playSound(p.getLocation(), Sound.BLOCK_RESPAWN_ANCHOR_DEPLETE, 1.0f, 1.0f);
                        return;
                    }
                    Inventory inv = Bukkit.createInventory(null, 27, "Destroy an Item");
                    for (int i = 0; i <= 8; i++) {
                        inv.setItem(i, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE, 0).asItem());
                    }
                    for (int i = 9; i <= 17; i++) {
                        inv.setItem(i, new ItemBuilder(Material.PINK_STAINED_GLASS_PANE, 0).asItem());
                    }
                    for (int i = 18; i <= 26; i++) {
                        inv.setItem(i, new ItemBuilder(Material.MAGENTA_STAINED_GLASS_PANE, 0).asItem());
                    }
                    inv.setItem(13, new ItemBuilder(Material.NETHERITE_AXE, 0).setName("&7Click an item to &5destroy &7it for &5Runic Points&7.").asItem());
                    p.openInventory(inv);
                } else if (e.getCurrentItem().getItemMeta().getDisplayName().contains(Text.color("&5Spend Runic Points"))) {
                    if (SKRPG.levelToInt(playerData.getRunics().getLevel().toString()) < 3) {
                        Text.applyText(p, "&cYou need to increase your &5Runic Level &cto do this! (Required Level: &53&c)");
                        p.playSound(p.getLocation(), Sound.BLOCK_RESPAWN_ANCHOR_DEPLETE, 1.0f, 1.0f);
                        return;
                    }
                    Inventory inv = Bukkit.createInventory(null, 27, Text.color("Spend Runic Points"));
                    for (int i = 0; i <= 8; i++) {
                        inv.setItem(i, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE, 0).asItem());
                    }
                    for (int i = 9; i <= 17; i++) {
                        inv.setItem(i, new ItemBuilder(Material.PINK_STAINED_GLASS_PANE, 0).asItem());
                    }
                    for (int i = 18; i <= 26; i++) {
                        inv.setItem(i, new ItemBuilder(Material.MAGENTA_STAINED_GLASS_PANE, 0).asItem());
                    }
                    String nameCollector;
                    Material itemTypeCollector = Material.NETHERITE_HOE;
                    String costCollector;
                    if (!playerData.getRunicUpgrades().containsKey(RunicUpgrades.MORE_RP)) {
                        nameCollector = "&5Buy Runic Points Collector";
                        costCollector = 1000 + "";
                    } else {
                        nameCollector = "&5Upgrade Runic Points Collector &8| &5" + (playerData.getRunicUpgrades().get(RunicUpgrades.MORE_RP) + 1);
                        costCollector = 250 * (playerData.getRunicUpgrades().get(RunicUpgrades.MORE_RP) + 1) + "";
                    }
                    if (playerData.getRunicUpgrades().containsKey(RunicUpgrades.MORE_RP) &&
                            playerData.getRunicUpgrades().get(RunicUpgrades.MORE_RP) >= 5) {
                        nameCollector = "&6&lMAXXED! &r&5Runic Points Collector";
                        itemTypeCollector = Material.DRAGON_BREATH;
                        costCollector = "&6&lMAXXED!";
                    }
                    String nameEnchantCostDecrease;
                    Material itemTypeEnchantCostDecrease = Material.ENCHANTED_BOOK;
                    String costEnchantCostDecrease;
                    if (!playerData.getRunicUpgrades().containsKey(RunicUpgrades.REDUCE_ENCHANTING)) {
                        nameEnchantCostDecrease = "&5Buy Enchanting Cost Reduction";
                        costEnchantCostDecrease = 2000 + "";

                    } else {
                        nameEnchantCostDecrease = "&5Upgrade Enchanting Cost Reduction &8| &5" + (playerData.getRunicUpgrades().get(RunicUpgrades.REDUCE_ENCHANTING) + 1);
                        costEnchantCostDecrease = 500 * (playerData.getRunicUpgrades().get(RunicUpgrades.REDUCE_ENCHANTING) + 1) + "";
                    }
                    if (playerData.getRunicUpgrades().containsKey(RunicUpgrades.REDUCE_ENCHANTING) &&
                            playerData.getRunicUpgrades().get(RunicUpgrades.REDUCE_ENCHANTING) >= 5) {
                        nameEnchantCostDecrease = "&6&lMAXXED! &r&5Enchanting Cost Reduction";
                        itemTypeEnchantCostDecrease = Material.DRAGON_BREATH;
                        costEnchantCostDecrease = "&6&lMAXXED!";
                    }
                    String nameRunicStoneDecrease;
                    Material itemTypeRunicStoneDecrease = Material.ANVIL;
                    String runicStoneDecreaseCost;
                    if (!playerData.getRunicUpgrades().containsKey(RunicUpgrades.REDUCE_RUNIC_STONE)) {
                        nameRunicStoneDecrease = "&5Buy Runic Stone Cost Reduction";
                        runicStoneDecreaseCost = 2500 + "";
                    } else {
                        nameRunicStoneDecrease = "&5Upgrade Runic Stone Cost Reduction &8| &5" + (playerData.getRunicUpgrades().get(RunicUpgrades.REDUCE_RUNIC_STONE) + 1);
                        runicStoneDecreaseCost = 750 * (playerData.getRunicUpgrades().get(RunicUpgrades.REDUCE_RUNIC_STONE) + 1) + "";
                    }
                    if (playerData.getRunicUpgrades().containsKey(RunicUpgrades.REDUCE_RUNIC_STONE) &&
                            playerData.getRunicUpgrades().get(RunicUpgrades.REDUCE_RUNIC_STONE) >= 5) {
                        nameRunicStoneDecrease = "&6&lMAXXED! &r&5Runic Stone Cost Reduction";
                        itemTypeRunicStoneDecrease = Material.DRAGON_BREATH;
                        runicStoneDecreaseCost = "&6&lMAXXED!";
                    }
                    String nameItemDestructionPoints;
                    Material itemTypeItemDestructionPoints = Material.NETHERITE_AXE;
                    String itemDestructionPointsCost;
                    if (!playerData.getRunicUpgrades().containsKey(RunicUpgrades.RUNIC_POINTS_DESTROY)) {
                        nameItemDestructionPoints = "&5Buy Item Destruction Points";
                        itemDestructionPointsCost = 3000 + "";
                    } else {
                        nameItemDestructionPoints = "&5Upgrade Item Destruction Points &8| &5" + (playerData.getRunicUpgrades().get(RunicUpgrades.RUNIC_POINTS_DESTROY) + 1);
                        itemDestructionPointsCost = 1000 * (playerData.getRunicUpgrades().get(RunicUpgrades.RUNIC_POINTS_DESTROY) + 1) + "";
                    }
                    if (playerData.getRunicUpgrades().containsKey(RunicUpgrades.RUNIC_POINTS_DESTROY) &&
                            playerData.getRunicUpgrades().get(RunicUpgrades.RUNIC_POINTS_DESTROY) >= 5) {
                        nameItemDestructionPoints = "&6&lMAXXED! &r&5Item Destruction Points";
                        itemTypeItemDestructionPoints = Material.DRAGON_BREATH;
                        itemDestructionPointsCost = "&6&lMAXXED!";
                    }
                    inv.setItem(12, new ItemBuilder(itemTypeEnchantCostDecrease, 0).setName(nameEnchantCostDecrease).setLore(
                            Arrays.asList(" ", Text.color("&7Decrease the amount of &5Runic Points"), Text.color("&7you spend &5enchanting &7an item."), " ", Text.color("&7Cost: &5" + costEnchantCostDecrease) + " ")).asItem());
                    inv.setItem(10, new ItemBuilder(itemTypeCollector, 0).setName(nameCollector).setLore(
                            Arrays.asList(" ", Text.color("&7Get &51 Runic Point &7for every 5 Runic Points"), Text.color("&7earned per level."), " ", Text.color("&7Cost: &5" + costCollector) + " ")).asItem());
                    inv.setItem(14, new ItemBuilder(itemTypeRunicStoneDecrease, 0).setName(nameRunicStoneDecrease).setLore(
                            Arrays.asList(" ", Text.color("&7Decrease the amount of &5Runic Points"), Text.color("&7you spend &5applying a runic stone &7to an item."), " ", Text.color("&7Cost: &5" + runicStoneDecreaseCost) + " ")).asItem());
                    inv.setItem(16, new ItemBuilder(itemTypeItemDestructionPoints, 0).setName(nameItemDestructionPoints).setLore(
                            Arrays.asList(" ", Text.color("&7Increase the amount of &5Runic Points"), Text.color("&7you get &5destroying an item&7."), " ", Text.color("&7Cost: &5" + itemDestructionPointsCost) + " ")).asItem());
                    p.openInventory(inv);
                }

            }
        } else if (e.getView().getTitle().equalsIgnoreCase("Enchant Item")) {

            if (e.getCurrentItem() != null) {
                if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Runic Points")) {
                    if (e.getInventory().getItem(13) == null) {
                        Text.applyText(p, "&cYou need to put in an item to enchant first!");
                        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                        e.setCancelled(true);
                        return;
                    }
                    int amount = Integer.parseInt(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName())
                            .replace(" Runic Points ஐ", "").replace("Use ", ""));
                    List<Enchantments> validEnchantments = new ArrayList<>();
                    ItemInfo itemInfo = ItemInfo.parseItemInfo(e.getInventory().getItem(13));
                    if (itemInfo == null) {
                        Text.applyText(p, "&cYou need to put in a valid item to enchant first!");
                        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                        e.setCancelled(true);
                        return;
                    }
                    for (Enchantments enchantments : EnumSet.allOf(Enchantments.class)) {
                        if (enchantments.getApplyType().equals(itemInfo.getItem().getItemType())) {
                            validEnchantments.add(enchantments);
                        }
                    }
                    Random random = new Random();
                    if ((validEnchantments.size() - 1) < 0) {
                        Text.applyText(p, "&cThere are no enchantments availible for this item!");
                        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                        e.setCancelled(true);
                        return;
                    }
                    int randomEnchantment = random.nextInt(validEnchantments.size());
                    Enchantments selectedEnchantment = validEnchantments.get(randomEnchantment);
                    int randomLevel = random.nextInt(2);
                    if (amount == 250) {
                        if (SKRPG.levelToInt(playerData.getRunics().getLevel().toString()) < 1) {
                            Text.applyText(p, "&cYou need to increase your &5Runic Level &cto do this! (Required Level: &51&c)");
                            p.playSound(p.getLocation(), Sound.BLOCK_RESPAWN_ANCHOR_DEPLETE, 1.0f, 1.0f);
                            e.setCancelled(true);
                            return;
                        }
                        if (playerData.getRunicPoints() < 250) {
                            Text.applyText(p, "&cYou need more &5Runic Points ஐ &cto enchant this!");
                            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                            e.setCancelled(true);
                            return;
                        }
                        int price = 250;
                        if (playerData.getRunicUpgrades().containsKey(RunicUpgrades.REDUCE_ENCHANTING)) {
                            price = (int) Math.round(price / (1 + (0.25 * playerData.getRunicUpgrades().get(RunicUpgrades.REDUCE_ENCHANTING))));
                        }
                        playerData.removeRunicPoints(price);
                        if (itemInfo.hasEnchantment(selectedEnchantment) && randomLevel + 1 <= itemInfo.getEnchantment(selectedEnchantment).getLevel()) {
                            Text.applyText(p, "&4&lOOF! &r&8| &cYou failed to apply the enchantment.");
                            p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0f, 1.0f);
                            e.setCancelled(true);
                            return;

                        }
                        itemInfo.addEnchantment(new Enchantment(selectedEnchantment, randomLevel + 1));
                        Text.applyText(p, "&5&lWOOSH! &r&8| &7You applied &5" + selectedEnchantment.getName() + " " + (randomLevel + 1) + " &7to your " +
                                itemInfo.getRarity().getColor() + itemInfo.getItem().getName() + "&7!");
                    } else if (amount == 500) {
                        if (SKRPG.levelToInt(playerData.getRunics().getLevel().toString()) < 5) {
                            Text.applyText(p, "&cYou need to increase your &5Runic Level &cto do this! (Required Level: &55&c)");
                            p.playSound(p.getLocation(), Sound.BLOCK_RESPAWN_ANCHOR_DEPLETE, 1.0f, 1.0f);
                            e.setCancelled(true);
                            return;
                        }
                        if (playerData.getRunicPoints() < 500) {
                            Text.applyText(p, "&cYou need more &5Runic Points ஐ &cto enchant this!");
                            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                            e.setCancelled(true);
                            return;
                        }
                        int price = 500;
                        if (playerData.getRunicUpgrades().containsKey(RunicUpgrades.REDUCE_ENCHANTING)) {
                            price = (int) Math.round(price / (1 + (0.25 * playerData.getRunicUpgrades().get(RunicUpgrades.REDUCE_ENCHANTING))));
                        }
                        playerData.removeRunicPoints(price);
                        if (itemInfo.hasEnchantment(selectedEnchantment) && randomLevel + 3 <= itemInfo.getEnchantment(selectedEnchantment).getLevel()) {
                            Text.applyText(p, "&4&lOOF! &r&8| &cYou failed to apply the enchantment.");
                            p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0f, 1.0f);
                            e.setCancelled(true);
                            return;
                        }
                        itemInfo.addEnchantment(new Enchantment(selectedEnchantment, randomLevel + 3));
                        Text.applyText(p, "&5&lWOOSH! &r&8| &7You applied &5" + selectedEnchantment.getName() + " " + (randomLevel + 3) + " &7to your " +
                                itemInfo.getRarity().getColor() + itemInfo.getItem().getName() + "&7!");
                    } else if (amount == 1000) {
                        if (SKRPG.levelToInt(playerData.getRunics().getLevel().toString()) < 11) {
                            Text.applyText(p, "&cYou need to increase your &5Runic Level &cto do this! (Required Level: &511&c)");
                            p.playSound(p.getLocation(), Sound.BLOCK_RESPAWN_ANCHOR_DEPLETE, 1.0f, 1.0f);
                            e.setCancelled(true);
                            return;
                        }
                        if (playerData.getRunicPoints() < 1000) {
                            Text.applyText(p, "&cYou need more &5Runic Points ஐ &cto enchant this!");
                            p.playSound(p.getLocation(), Sound.BLOCK_RESPAWN_ANCHOR_DEPLETE, 1.0f, 1.0f);
                            e.setCancelled(true);
                            return;
                        }
                        int price = 1000;
                        if (playerData.getRunicUpgrades().containsKey(RunicUpgrades.REDUCE_ENCHANTING)) {
                            price = (int) Math.round(price / (1 + (0.25 * playerData.getRunicUpgrades().get(RunicUpgrades.REDUCE_ENCHANTING))));
                        }
                        playerData.removeRunicPoints(price);
                        if (itemInfo.hasEnchantment(selectedEnchantment) && randomLevel + 1 <= itemInfo.getEnchantment(selectedEnchantment).getLevel()) {
                            Text.applyText(p, "&4&lOOF! &r&8| &cYou failed to apply the enchantment.");
                            p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0f, 1.0f);
                            e.setCancelled(true);
                            return;
                        }
                        itemInfo.addEnchantment(new Enchantment(selectedEnchantment, 5));
                        Text.applyText(p, "&5&lWOOSH! &r&8| &7You applied &5" + selectedEnchantment.getName() + " 5 &7to your " +
                                itemInfo.getRarity().getColor() + itemInfo.getItem().getName() + "&7!");
                    }
                    p.playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 0.2f);
                    new BukkitRunnable() {

                        @Override
                        public void run() {
                            p.playSound(p.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.0f, 0.2f);
                        }
                    }.runTaskLater(skrpg, 4);

                    Items.updateItem(e.getInventory().getItem(13), itemInfo);
                    p.getInventory().addItem(e.getInventory().getItem(13));
                    e.getInventory().removeItem(e.getInventory().getItem(13));
                    e.setCancelled(true);
                    return;
                }
                if (e.getClickedInventory().getType().equals(InventoryType.PLAYER)) {
                    if (p.getOpenInventory().getTopInventory().getItem(13) == null) {
                        p.getOpenInventory().getTopInventory().setItem(13, e.getCurrentItem());
                        p.getInventory().removeItem(e.getCurrentItem());
                        p.playSound(p.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.0f, 1.0f);
                    }
                } else if (e.getSlot() == 13 && e.getInventory().getItem(13) != null) {
                    p.getInventory().addItem(e.getInventory().getItem(13));
                    e.getInventory().removeItem(e.getInventory().getItem(13));
                    p.playSound(p.getLocation(), Sound.BLOCK_RESPAWN_ANCHOR_DEPLETE, 1.0f, 0.2f);
                }

                e.setCancelled(true);
            }

        } else if (e.getView().getTitle().equalsIgnoreCase("Apply Runic Stone")) {

            if (e.getCurrentItem() != null) {
                if (e.getCurrentItem().getItemMeta().getDisplayName().equals(Text.color("&7Click to &5Combine&7."))) {
                    Inventory topInv = e.getView().getTopInventory();
                    if (topInv.getItem(13) == null) {
                        Text.applyText(p, "&cYou need to put an item and a &5Runic Stone &cin first!");
                        p.playSound(p.getLocation(), Sound.BLOCK_RESPAWN_ANCHOR_DEPLETE, 1.0f, 1.0f);
                        e.setCancelled(true);
                        return;
                    }
                    ItemInfo iF = ItemInfo.parseItemInfo(topInv.getItem(13));
                    int price = iF.getRunicStones().getCost() * iF.getRarity().getPriority();
                    if (playerData.getRunicUpgrades().containsKey(RunicUpgrades.REDUCE_RUNIC_STONE)) {
                        price = (int) Math.round(price / (1 + (0.25 * playerData.getRunicUpgrades().get(RunicUpgrades.REDUCE_RUNIC_STONE))));
                    }
                    if (playerData.getRunicPoints() < price) {
                        Text.applyText(p, "&cYou need more &5Runic Points &cto apply this!");
                        p.playSound(p.getLocation(), Sound.BLOCK_RESPAWN_ANCHOR_DEPLETE, 1.0f, 1.0f);
                        e.setCancelled(true);
                        return;
                    }
                    p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_USE, 1.0f, 1.0f);
                    new BukkitRunnable() {

                        @Override
                        public void run() {
                            p.playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 0.2f);
                            p.playSound(p.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.0f, 0.2f);
                        }
                    }.runTaskLater(skrpg, 17);
                    playerData.removeRunicPoints(price);
                    Items.updateItem(topInv.getItem(13), iF);
                    p.getInventory().addItem(topInv.getItem(13));
                    e.getInventory().removeItem(topInv.getItem(13));
                    e.getInventory().removeItem(topInv.getItem(32));
                    e.getInventory().removeItem(topInv.getItem(30));
                    Text.applyText(p, "&5&lNICE! &r&8| &r&7You applied &5" + iF.getRunicStones().getName() + " &7to your " + iF.getRarity().getColor() + iF.getItem().getName() + "&7!");
                }
                ItemInfo itemInfo = ItemInfo.parseItemInfo(e.getCurrentItem());
                if (itemInfo != null) {
                    if (e.getSlot() == 32 && e.getInventory().getItem(32) != null) {
                        p.getInventory().addItem(e.getCurrentItem());
                        e.getInventory().removeItem(e.getCurrentItem());
                        p.playSound(p.getLocation(), Sound.BLOCK_RESPAWN_ANCHOR_DEPLETE, 1.0f, 0.2f);
                    }
                    if (e.getSlot() == 30 && e.getInventory().getItem(30) != null) {
                        p.getInventory().addItem(e.getCurrentItem());
                        e.getInventory().removeItem(e.getCurrentItem());
                        p.playSound(p.getLocation(), Sound.BLOCK_RESPAWN_ANCHOR_DEPLETE, 1.0f, 0.2f);
                    }
                    if (itemInfo.getItem().getItemType().equals(ItemType.RUNIC_STONE)) {
                        if (e.getRawSlot() > e.getView().getTopInventory().getSize()) {
                            Inventory openInv = p.getOpenInventory().getTopInventory();
                            if (openInv.getItem(32) == null) {
                                openInv.setItem(32, e.getCurrentItem());
                                p.getInventory().removeItem(e.getCurrentItem());
                                p.playSound(p.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.0f, 1.0f);
                            }
                        }

                    } else {
                        if (e.getRawSlot() > e.getView().getTopInventory().getSize()) {
                            Inventory openInv = p.getOpenInventory().getTopInventory();
                            if (openInv.getItem(30) == null) {
                                openInv.setItem(30, e.getCurrentItem());
                                p.getInventory().removeItem(e.getCurrentItem());
                                p.playSound(p.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.0f, 1.0f);
                            }
                        }
                    }
                }
            }
            e.setCancelled(true);
            runicStoneUpdate(e.getView().getTopInventory());
        } else if (e.getView().getTitle().equalsIgnoreCase("Destroy an Item")) {
            if (e.getCurrentItem() != null) {
                e.setCancelled(true);
                if (e.getRawSlot() > e.getView().getTopInventory().getSize()) {
                    ItemInfo itemInfo = ItemInfo.parseItemInfo(e.getCurrentItem());
                    if (itemInfo != null) {
                        if (itemInfo.getItem().getItemType().equals(ItemType.WEAPON) ||
                                itemInfo.getItem().getItemType().equals(ItemType.ARMOR) ||
                                itemInfo.getItem().getItemType().equals(ItemType.BOW) ||
                                itemInfo.getItem().getItemType().equals(ItemType.TOOL)) {
                            if (playerData.getSellAboveRarity().getPriority() < itemInfo.getRarity().getPriority() || playerData.getSellAboveRarity() == Rarity.COMMON) {
                                Text.applyText(p, "&cYour sell above rarity setting prevented you from selling this!");
                                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                                return;
                            }
                            int price = (itemInfo.getItem().getSellPrice() / 4);
                            if (playerData.getRunicUpgrades().containsKey(RunicUpgrades.RUNIC_POINTS_DESTROY)) {
                                price = (int) Math.round(price * (1 + (0.50 * playerData.getRunicUpgrades().get(RunicUpgrades.RUNIC_POINTS_DESTROY))));
                            }
                            Text.applyText(p, "&7You sold " + e.getCurrentItem().getItemMeta().getDisplayName() + " &7for &5" + price + " Runic Points&7.");
                            p.getInventory().removeItem(e.getCurrentItem());
                            playerData.addRunicPoints(price, skrpg);
                            p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0f, 0.2f);
                            p.playSound(p.getLocation(), Sound.BLOCK_RESPAWN_ANCHOR_CHARGE, 1.0f, 0.2f);
                        }

                    }
                }
            }
        } else if (e.getView().getTitle().equalsIgnoreCase("Spend Runic Points")) {
            if (e.getCurrentItem() != null) {
                e.setCancelled(true);
                RunicUpgrades runicUpgrades = null;
                int price = 0;
                if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Item Destruction Points")) {
                    runicUpgrades = RunicUpgrades.RUNIC_POINTS_DESTROY;
                    if (!playerData.getRunicUpgrades().containsKey(RunicUpgrades.RUNIC_POINTS_DESTROY)) {
                        price = 3000;
                    } else {
                        price = 1000 * (playerData.getRunicUpgrades().get(RunicUpgrades.RUNIC_POINTS_DESTROY) + 1);
                    }
                } else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Enchanting Cost Reduction")) {
                    runicUpgrades = RunicUpgrades.REDUCE_ENCHANTING;
                    if (!playerData.getRunicUpgrades().containsKey(RunicUpgrades.REDUCE_ENCHANTING)) {
                        price = 2000;
                    } else {
                        price = 500 * (playerData.getRunicUpgrades().get(RunicUpgrades.REDUCE_ENCHANTING) + 1);
                    }
                }  else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Runic Stone Cost Reduction")) {
                    runicUpgrades = RunicUpgrades.REDUCE_RUNIC_STONE;
                    if (!playerData.getRunicUpgrades().containsKey(RunicUpgrades.REDUCE_RUNIC_STONE)) {
                        price = 2500;
                    } else {
                        price = 750 * (playerData.getRunicUpgrades().get(RunicUpgrades.REDUCE_RUNIC_STONE) + 1);
                    }
                }  else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Runic Points Collector")) {
                    runicUpgrades = RunicUpgrades.MORE_RP;
                    if (!playerData.getRunicUpgrades().containsKey(RunicUpgrades.MORE_RP)) {
                        price = 1000;
                    } else {
                        price = 250 * (playerData.getRunicUpgrades().get(RunicUpgrades.MORE_RP) + 1);
                    }
                }
                if (runicUpgrades == null) { return; }
                if (price > playerData.getRunicPoints()) {
                    Text.applyText(p, "&cYou don't have enough &5Runic Points &cto buy this!");
                    p.playSound(p.getLocation(), Sound.BLOCK_RESPAWN_ANCHOR_DEPLETE, 1.0f, 0.2f);
                    e.setCancelled(true);
                    return;
                }
                playerData.removeRunicPoints(price);
                if (!playerData.getRunicUpgrades().containsKey(runicUpgrades)) {
                    playerData.getRunicUpgrades().put(runicUpgrades, 1);
                } else {
                    int level = (playerData.getRunicUpgrades().get(runicUpgrades) + 1);
                    playerData.getRunicUpgrades().remove(runicUpgrades);
                    playerData.getRunicUpgrades().put(runicUpgrades, level);
                }
                Text.applyText(p,"&5&lNICE! &r&8| &7You purchased &5" + runicUpgrades.getRunicUpgradeName() + " " + playerData.getRunicUpgrades().get(runicUpgrades) + " &7for &5" + price + " Runic Points&7.");
                p.playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 0.2f);
                new BukkitRunnable() {

                    @Override
                    public void run() {
                        p.playSound(p.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.0f, 0.2f);
                    }
                }.runTaskLater(skrpg, 4);
            }

        } else if (e.getView().getTitle().equals("The Lift")) {
            e.setCancelled(true);
            if (e.getCurrentItem() != null) {
                Location teleportLocation = null;
                int level = 0;
                if (e.getCurrentItem().getItemMeta().getDisplayName().equals(Text.color("&aThe Coal Mines"))) {
                    teleportLocation = skrpg.getLocationManager().getCoalMineLocation();
                } else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(Text.color("&aIron Quarry"))) {
                    teleportLocation = skrpg.getLocationManager().getIronMineLocation();
                    level = 2;
                } else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(Text.color("&aGold Caves"))) {
                    teleportLocation = skrpg.getLocationManager().getGoldMineLocation();
                    level = 4;
                } else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(Text.color("&aLapis Tunnels"))) {
                    teleportLocation = skrpg.getLocationManager().getLapisMineLocation();
                    level = 6;
                } else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(Text.color("&aRedstone Ravine"))) {
                    teleportLocation = skrpg.getLocationManager().getRedstoneMineLocation();
                    level = 8;
                } else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(Text.color("&aCrystallite Cavern"))) {
                    teleportLocation = skrpg.getLocationManager().getCrystalliteMineLocation();
                    level = 10;
                } else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(Text.color("&aDiamond Depths"))) {
                    teleportLocation = skrpg.getLocationManager().getDiamondMineLocation();
                    level = 12;
                } else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(Text.color("&aObsidian Reserve"))) {
                    teleportLocation = skrpg.getLocationManager().getObsidianMineLocation();
                    level = 14;
                } else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(Text.color("&aSurface"))) {
                    teleportLocation = skrpg.getLocationManager().getAbandonedMinesSurfaceLocation();
                    level = 0;
                }
                if (teleportLocation != null) {
                    if (SKRPG.levelToInt(playerData.getMining().getLevel().toString()) < level) {
                        Text.applyText(p, "&cYou need a higher Mining Level to access this! &c(Mining Level Required: " + level + ")");
                        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                        return;
                    }
                    p.teleport(teleportLocation);
                    p.playSound(p.getLocation(), Sound.BLOCK_CHEST_OPEN, 1.0f, 0.2f);
                }
            }
        } else if (e.getView().getTitle().equals("Stovetop")) {
            e.setCancelled(true);
            if (e.getCurrentItem() != null) {
                if (e.getCurrentItem().getItemMeta().getDisplayName().equals(Text.color("&aSmelt"))) {
                    Inventory inv = Bukkit.createInventory(null, 36, "Smelt");
                    for (int i = 0; i <= 35; i++) {
                        inv.setItem(i, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE, 0).setName(" ").asItem());
                    }
                    inv.setItem(13, new ItemBuilder(Material.FURNACE, 0).setName("&aSmelt").setLore(Arrays.asList(" ",
                            Text.color("&7You can smelt any meat to turn it into it's cooked variant."),
                            Text.color("&7Smelting meat will cost &fCoal&7, and can be applied to Food Bases."),
                            " ")).asItem());

                    inv.setItem(22, null);
                    p.openInventory(inv);
                } else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(Text.color("&aProcess"))) {
                    Inventory inv = Bukkit.createInventory(null, 36, "Process");
                    for (int i = 0; i <= 35; i++) {
                        inv.setItem(i, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE, 0).setName(" ").asItem());
                    }
                    inv.setItem(13, new ItemBuilder(Material.IRON_AXE, 0).setName("&aProcess").setLore(Arrays.asList(" ",
                            Text.color("&7Processing crops/plants will turn them into Processed Crops,"),
                            Text.color("&7which can be used in Food Bases."),
                            Text.color("&7Processing crops will cost &fIron&7."), " ")).asItem());
                    inv.setItem(22, null);
                    p.openInventory(inv);
                } else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(Text.color("&aCook Food"))) {
                    Inventory inv = Bukkit.createInventory(null, 45, "Cook Food");
                    for (int i = 0; i <= 44; i++) {
                        inv.setItem(i, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE, 0).setName(" ").asItem());
                    }
                    inv.setItem(31, new ItemBuilder(Material.MUSHROOM_STEW, 0).setName(" ").setLore(
                            Arrays.asList(Text.color("&a☚ &7Food Base"), Text.color("&7Applied Item &a☛"), " ")
                    ).asItem());
                    inv.setItem(30, null);
                    inv.setItem(32, null);
                    inv.setItem(13, null);
                    inv.setItem(22, new ItemBuilder(Material.WOODEN_SHOVEL, 0).setName("&7Click to &aApply&7.").asItem());
                    p.openInventory(inv);
                }
            }
        } else if (e.getView().getTitle().equals("Cook Food")) {
            if (e.getCurrentItem() != null) {
                if (e.getCurrentItem().getItemMeta().getDisplayName().equals(Text.color("&7Click to &aApply&7."))) {
                    Inventory topInv = e.getView().getTopInventory();
                    if (topInv.getItem(13) == null) {
                        Text.applyText(p, "&cYou need to put a base and an item &cin first!");
                        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                        e.setCancelled(true);
                        return;
                    }
                    ItemInfo iF = ItemInfo.parseItemInfo(topInv.getItem(13));
                    if (Foods.getFood(iF.getItem()) == null) {
                        e.setCancelled(true);
                        return;
                    }
                    int price = Foods.getFood(iF.getItem()).getOriginalBase().getFarmingLevelRequired();

                    if (SKRPG.levelToInt(playerData.getHerbalism().getLevel().toString()) < price) {
                        Text.applyText(p, "&cYou need a better &aHerbalism Level &cto apply this!");
                        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                        e.setCancelled(true);
                        return;
                    }
                    p.playSound(p.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1.0f, 0.2f);
                    Items.updateItem(topInv.getItem(13), iF);
                    p.getInventory().addItem(topInv.getItem(13));
                    e.getInventory().removeItem(topInv.getItem(13));
                    if (e.getInventory().getItem(32).getAmount() != 1) {
                        e.getInventory().getItem(32).setAmount(e.getInventory().getItem(32).getAmount() - 1);
                    } else {
                        e.getInventory().removeItem(topInv.getItem(32));
                    }
                    if (e.getInventory().getItem(30).getAmount() != 1) {
                        e.getInventory().getItem(30).setAmount(e.getInventory().getItem(32).getAmount() - 1);
                    } else {
                        e.getInventory().removeItem(topInv.getItem(30));
                    }


                    Text.applyText(p, "&a&lDELICIOUS! &r&8| &r&7You made " + iF.getRarity().getColor() + iF.getItem().getName() + "&7!");
                }
                ItemInfo itemInfo = ItemInfo.parseItemInfo(e.getCurrentItem());
                if (itemInfo != null) {
                    if (e.getSlot() == 32 && e.getInventory().getItem(32) != null) {
                        p.getInventory().addItem(e.getCurrentItem());
                        e.getInventory().removeItem(e.getCurrentItem());
                        p.playSound(p.getLocation(), Sound.ENTITY_ITEM_FRAME_REMOVE_ITEM, 1.0f, 0.2f);
                    }
                    if (e.getSlot() == 30 && e.getInventory().getItem(30) != null) {
                        p.getInventory().addItem(e.getCurrentItem());
                        e.getInventory().removeItem(e.getCurrentItem());
                        p.playSound(p.getLocation(), Sound.ENTITY_ITEM_FRAME_REMOVE_ITEM, 1.0f, 0.2f);
                    }
                    if (itemInfo.getItem().getItemType().equals(ItemType.FOOD_BASE)) {
                        if (e.getRawSlot() > e.getView().getTopInventory().getSize()) {
                            Inventory openInv = p.getOpenInventory().getTopInventory();
                            if (openInv.getItem(30) == null) {
                                openInv.setItem(30, e.getCurrentItem());
                                p.getInventory().removeItem(e.getCurrentItem());
                                p.playSound(p.getLocation(), Sound.ENTITY_PHANTOM_FLAP, 1.0f, 0.2f);
                            }
                        }

                    } else {
                        if (e.getRawSlot() > e.getView().getTopInventory().getSize()) {
                            Inventory openInv = p.getOpenInventory().getTopInventory();
                            if (openInv.getItem(32) == null) {
                                openInv.setItem(32, e.getCurrentItem());
                                p.getInventory().removeItem(e.getCurrentItem());
                                p.playSound(p.getLocation(), Sound.ENTITY_PHANTOM_FLAP, 1.0f, 0.2f);
                            }
                        }
                    }
                }
            }
            e.setCancelled(true);
            stoveTopUpdate(e.getInventory());
        } else if (e.getView().getTitle().equals("Smelt")) {
            e.setCancelled(true);
            if (e.getCurrentItem() != null) {
                ItemInfo itemInfo = ItemInfo.parseItemInfo(e.getCurrentItem());
                if (itemInfo != null) {
                    if (e.getRawSlot() > e.getView().getTopInventory().getSize()) {
                        Inventory openInv = p.getOpenInventory().getTopInventory();
                        if (openInv.getItem(22) == null) {
                            openInv.setItem(22, e.getCurrentItem());
                            p.getInventory().removeItem(e.getCurrentItem());
                            p.playSound(p.getLocation(), Sound.BLOCK_FIRE_AMBIENT, 1.0f, 0.2f);
                        }
                    } else {
                        if (e.getSlot() == 22) {
                            p.getInventory().addItem(e.getInventory().getItem(22));
                            e.getInventory().removeItem(e.getInventory().getItem(22));
                            p.playSound(p.getLocation(), Sound.BLOCK_FIRE_AMBIENT, 1.0f, 0.2f);
                        }
                    }
                }
                if (e.getSlot() == 13 && e.getInventory().getItem(22) != null) {
                    ItemInfo itemItemInfo = ItemInfo.parseItemInfo(e.getInventory().getItem(22));
                    int coalTotal = 0;
                    List<ItemStack> removedCoal = new ArrayList<>();
                    if (itemItemInfo != null) {
                        if (itemItemInfo.getItem().isCookable() && !itemItemInfo.isCooked()) {
                            for (ItemStack itemStack : p.getInventory().getContents()) {
                                ItemInfo itemInfoItem = ItemInfo.parseItemInfo(itemStack);
                                if (itemInfoItem != null) {
                                    if (itemInfoItem.getItem() == Items.COAL) {
                                        coalTotal = coalTotal + itemStack.getAmount();
                                        removedCoal.add(itemStack);
                                    }
                                }
                            }
                            if (coalTotal < ((itemInfo.getRarity().getPriority() * itemInfo.getQuality().getStar() * 5)
                                    * e.getInventory().getItem(22).getAmount())) {
                                Text.applyText(p, "&cYou need more &8Coal &cto process this!");
                                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                            } else {
                                itemItemInfo.setCooked(true);
                                Items.updateItem(e.getInventory().getItem(22), itemItemInfo);
                                p.getInventory().addItem(e.getInventory().getItem(22));
                                e.getInventory().removeItem(e.getInventory().getItem(22));
                                p.playSound(p.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1.0f, 0.2f);
                                for (ItemStack itemStack : removedCoal) {
                                    if (itemStack.getAmount() > coalTotal) {
                                        itemStack.setAmount(itemStack.getAmount() - coalTotal);
                                    }
                                    coalTotal = coalTotal - itemStack.getAmount();
                                }
                            }

                        }
                    }
                }
            }
            updateCook(e.getView().getTopInventory());
        } else if (e.getView().getTitle().equals("Process")) {
            e.setCancelled(true);
            if (e.getCurrentItem() != null) {
                ItemInfo itemInfo = ItemInfo.parseItemInfo(e.getCurrentItem());
                if (itemInfo != null) {
                    if (e.getRawSlot() > e.getView().getTopInventory().getSize()) {
                        Inventory openInv = p.getOpenInventory().getTopInventory();
                        if (openInv.getItem(22) == null) {
                            openInv.setItem(22, e.getCurrentItem());
                            p.getInventory().removeItem(e.getCurrentItem());
                            p.playSound(p.getLocation(), Sound.BLOCK_SWEET_BERRY_BUSH_BREAK, 1.0f, 0.2f);
                        }
                    }  else {
                        if (e.getSlot() == 22) {
                            p.getInventory().addItem(e.getInventory().getItem(22));
                            e.getInventory().removeItem(e.getInventory().getItem(22));
                            p.playSound(p.getLocation(), Sound.BLOCK_SWEET_BERRY_BUSH_BREAK, 1.0f, 0.2f);
                        }
                    }
                }
                if (e.getSlot() == 13 && e.getInventory().getItem(22) != null) {
                    ItemInfo itemItemInfo = ItemInfo.parseItemInfo(e.getInventory().getItem(22));
                    int ironTotal = 0;
                    List<ItemStack> removedIron = new ArrayList<>();
                    if (itemItemInfo != null) {
                        if (itemItemInfo.getItem().isProcessable() && !itemItemInfo.isProcessed()) {
                            for (ItemStack itemStack : p.getInventory().getContents()) {
                                ItemInfo itemInfoItem = ItemInfo.parseItemInfo(itemStack);
                                if (itemInfoItem != null) {
                                    if (itemInfoItem.getItem() == Items.IRON_INGOT) {
                                        ironTotal = ironTotal + itemStack.getAmount();
                                        removedIron.add(itemStack);
                                    }
                                }
                            }
                            if (ironTotal < ((itemInfo.getRarity().getPriority() * itemInfo.getQuality().getStar() * 5)
                                    * e.getInventory().getItem(22).getAmount())) {
                                Text.applyText(p, "&cYou need more &fIron &cto process this!");
                                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                            } else {
                                itemItemInfo.setProcessed(true);
                                Items.updateItem(e.getInventory().getItem(22), itemItemInfo);
                                p.getInventory().addItem(e.getInventory().getItem(22));
                                e.getInventory().removeItem(e.getInventory().getItem(22));
                                p.playSound(p.getLocation(), Sound.BLOCK_NETHER_SPROUTS_BREAK, 1.0f, 0.2f);
                                for (ItemStack itemStack : removedIron) {
                                    p.getInventory().removeItem(itemStack);
                                }
                            }

                        }
                    }
                }
            }
            updateProcess(e.getView().getTopInventory());
        } else if (e.getView().getTitle().equals("Cook Food Base")) {

        }
    }
    public void updateProcess(Inventory inv) {
        if (inv.getItem(22) == null) {
            List<String> lore = Arrays.asList(" ",
                    Text.color("&7Processing crops/plants will turn them into Processed Crops,"),
                    Text.color("&7which can be used in Food Bases."),
                    Text.color("&7Processing crops will cost &fIron&7."), " ");
            ItemMeta itemMeta = inv.getItem(13).getItemMeta();
            itemMeta.setLore(lore);
            inv.getItem(13).setItemMeta(itemMeta);
            return;
        }
        ItemInfo itemInfo = ItemInfo.parseItemInfo(inv.getItem(22));
        if (!itemInfo.getItem().isProcessable()) {
            List<String> lore = Arrays.asList(" ",
                    Text.color(" "),
                    Text.color("&cThis item cannot be processed!"),
                    Text.color(""));
            ItemMeta itemMeta = inv.getItem(13).getItemMeta();
            itemMeta.setLore(lore);
            inv.getItem(13).setItemMeta(itemMeta);
            return;
        }
        List<String> lore = Arrays.asList(" ",
                Text.color(" "),
                Text.color("&7Iron Required: &f" + ((itemInfo.getRarity().getPriority() * itemInfo.getQuality().getStar() * 5) * inv.getItem(22).getAmount())),
                Text.color(""));
        ItemMeta itemMeta = inv.getItem(13).getItemMeta();
        itemMeta.setLore(lore);
        inv.getItem(13).setItemMeta(itemMeta);
    }
    public void updateCook(Inventory inv) {
        if (inv.getItem(22) == null) {
            List<String> lore = Arrays.asList(" ",
                    Text.color("&7You can smelt any meat to turn it into it's cooked variant."),
                    Text.color("&7Smelting meat will cost &fCoal&7, and can be applied to Food Bases."),
                    " ");
            ItemMeta itemMeta = inv.getItem(13).getItemMeta();
            itemMeta.setLore(lore);
            inv.getItem(13).setItemMeta(itemMeta);
            return;
        }
        ItemInfo itemInfo = ItemInfo.parseItemInfo(inv.getItem(22));
        if (!itemInfo.getItem().isCookable()) {
            List<String> lore = Arrays.asList(" ",
                    Text.color(" "),
                    Text.color("&cThis item cannot be cooked!"),
                    Text.color(""));
            ItemMeta itemMeta = inv.getItem(13).getItemMeta();
            itemMeta.setLore(lore);
            inv.getItem(13).setItemMeta(itemMeta);
            return;
        }
        List<String> lore = Arrays.asList(" ",
                Text.color(" "),
                Text.color("&7Coal Required: &8" + ((itemInfo.getRarity().getPriority() * itemInfo.getQuality().getStar() * 5) * inv.getItem(22).getAmount())),
                Text.color(""));
        ItemMeta itemMeta = inv.getItem(13).getItemMeta();
        itemMeta.setLore(lore);
        inv.getItem(13).setItemMeta(itemMeta);
    }
    public void runicStoneUpdate(Inventory inv) {
        if (inv.getItem(30) == null) { inv.setItem(13, null); return; }
        if (inv.getItem(32) == null) { inv.setItem(13, null); return; }
        ItemInfo itemInfo = ItemInfo.parseItemInfo(inv.getItem(30));
        ItemInfo itemInfoStone = ItemInfo.parseItemInfo(inv.getItem(32));
        if (itemInfo == null) { inv.setItem(13, null); return; }
        if (itemInfoStone == null) { inv.setItem(13, null); return; }
        if (RunicStones.getRunicStone(itemInfoStone.getItem()) == null) { inv.setItem(13, null); return; }
        ItemStack itemStack = inv.getItem(30).clone();
        ItemInfo clonedInfo = ItemInfo.parseItemInfo(itemStack);
        clonedInfo.setRunicStones(RunicStones.getRunicStone(itemInfoStone.getItem()));
        Items.updateItem(itemStack, clonedInfo);
        ItemMeta iM = itemStack.getItemMeta();
        List<String> lore = itemStack.getItemMeta().getLore();
        lore.add(" ");
        lore.add(" ");
        lore.add(Text.color("&7Cost: &5" + (RunicStones.getRunicStone(itemInfoStone.getItem()).getCost() * clonedInfo.getRarity().getPriority()) + " Runic Points"));
        iM.setLore(lore);
        itemStack.setItemMeta(iM);
        inv.setItem(13, itemStack);
    }
    public void stoveTopUpdate(Inventory inv) {
        if (inv.getItem(30) == null) { inv.setItem(13, null); return; }
        if (inv.getItem(32) == null) { inv.setItem(13, null); return; }
        ItemInfo itemInfoBase = ItemInfo.parseItemInfo(inv.getItem(30));
        ItemInfo itemInfoItem = ItemInfo.parseItemInfo(inv.getItem(32));
        if (itemInfoBase == null) { inv.setItem(13, null); return; }
        if (itemInfoItem == null) { inv.setItem(13, null); return; }
        Foods food = null;
        FoodBase foodBase = FoodBase.getFood(itemInfoBase.getItem());
        for (Foods foods : EnumSet.allOf(Foods.class)) {
            if (foods.getOriginalBase() == foodBase) {
                if (foods.getAppliedItem() == itemInfoItem.getItem()) {
                    if (foods.isProcessed() == itemInfoItem.isProcessed() || foods.isCooked() == itemInfoItem.isCooked()) {
                        food = foods;
                    }

                }
            }
        }
        if (food == null) { inv.setItem(13, null); return; }
        ItemStack itemStack = Items.buildItem(food.getFoodItem());
        Foods.updateFood(itemStack);
        ItemMeta iM = itemStack.getItemMeta();
        List<String> lore = itemStack.getItemMeta().getLore();
        lore.add(" ");
        lore.add(" ");
        lore.add(Text.color("&7Herbalism Level Required: &a" + foodBase.getFarmingLevelRequired()));
        iM.setLore(lore);
        itemStack.setItemMeta(iM);
        inv.setItem(13, itemStack);
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
    public void onFlyEvent(PlayerToggleFlightEvent e) {
        if (e.getPlayer().getGameMode() == GameMode.CREATIVE) {
            return;
        }
        if (skrpg.getRaidManager().getRaid(e.getPlayer()) != null) {
            return;
        }
        if (skrpg.getPlayerManager().getPlayerData(e.getPlayer().getUniqueId()).getEnergy() - 20 <= 0) {
            Text.applyText(e.getPlayer(), "&cYou don't have enough energy to double jump!");
            e.getPlayer().setAllowFlight(false);
            new BukkitRunnable() {

                @Override
                public void run() {
                    ItemInfo bootsInfo = ItemInfo.parseItemInfo(e.getPlayer().getInventory().getBoots());
                    if (bootsInfo != null) {
                        if (bootsInfo.getItem() == Items.SPIDER_BOOTS) {
                            e.getPlayer().setAllowFlight(true);
                        }
                    }

                }
            }.runTaskLater(skrpg, 5);
            e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
            e.setCancelled(true);
            return;
        }
        ItemInfo itemInfo = ItemInfo.parseItemInfo(e.getPlayer().getInventory().getBoots());
        if (itemInfo != null) {
            if (itemInfo.getItem() == Items.SPIDER_BOOTS) {
                e.setCancelled(true);
                e.getPlayer().setFlying(false);
                e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                e.getPlayer().setVelocity(e.getPlayer().getLocation().getDirection());
                e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_SPIDER_AMBIENT, 1.0f, 0.2f);
                e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_PHANTOM_FLAP, 1.0f, 0.2f);
                skrpg.getPlayerManager().getPlayerData(e.getPlayer().getUniqueId()).setEnergy(skrpg.getPlayerManager().getPlayerData(e.getPlayer().getUniqueId()).getEnergy() - 20);
                return;
            }
        } else {
            e.getPlayer().setAllowFlight(false);
        }
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
    @EventHandler
    public void onChatEvent(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        if (skrpg.getSetup(p) != null) {
            if (e.getMessage().equals("okay") && skrpg.getSetup(p).getStage() != 6) {
                skrpg.getSetup(p).nextStage(null);
            } else if (skrpg.getSetup(p).getStage() == 6) {
                skrpg.getSetup(p).nextStage(e.getMessage());
            }
        }
    }
}
