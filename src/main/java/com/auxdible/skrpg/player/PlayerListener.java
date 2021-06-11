package com.auxdible.skrpg.player;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.items.*;
import com.auxdible.skrpg.items.abilities.Abilities;
import com.auxdible.skrpg.items.enchantments.Enchantment;
import com.auxdible.skrpg.items.enchantments.Enchantments;
import com.auxdible.skrpg.items.food.FoodBase;
import com.auxdible.skrpg.items.food.Foods;
import com.auxdible.skrpg.items.forage.ForageType;
import com.auxdible.skrpg.mobs.DamageType;
import com.auxdible.skrpg.npcs.NpcType;
import com.auxdible.skrpg.npcs.NPC;
import com.auxdible.skrpg.player.collections.Collection;
import com.auxdible.skrpg.player.collections.CollectionType;
import com.auxdible.skrpg.player.collections.Tiers;
import com.auxdible.skrpg.player.economy.Bank;
import com.auxdible.skrpg.player.economy.BankLevel;
import com.auxdible.skrpg.items.SKRPGItemStack;
import com.auxdible.skrpg.player.guilds.GuildRank;
import com.auxdible.skrpg.player.guilds.raid.Raid;
import com.auxdible.skrpg.player.guilds.raid.RaidMob;
import com.auxdible.skrpg.player.guilds.raid.RaidMobs;
import com.auxdible.skrpg.player.quests.royaltyquests.RoyaltyQuest;
import com.auxdible.skrpg.player.quests.royaltyquests.RoyaltyQuestDifficulty;
import com.auxdible.skrpg.player.quests.royaltyquests.RoyaltyQuestType;
import com.auxdible.skrpg.player.quests.royaltyquests.RoyaltyUpgrades;
import com.auxdible.skrpg.player.skills.*;
import com.auxdible.skrpg.mobs.Mob;
import com.auxdible.skrpg.mobs.MobType;
import com.auxdible.skrpg.locations.regions.RegionFlags;
import com.auxdible.skrpg.utils.ItemBuilder;
import com.auxdible.skrpg.utils.Text;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_16_R3.NBTTagCompound;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

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
        if (e.getRightClicked() instanceof Villager || e.getRightClicked() instanceof Player || e.getRightClicked() instanceof ArmorStand) {
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
                    cArg[0] = SKRPG.class;
                    cArg[1] = Location.class;
                    npc = (NPC) npcClass.getDeclaredConstructor(cArg).newInstance(skrpg, null);
                } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException x) {
                    x.printStackTrace();
                }
                if (e.getRightClicked().getCustomName().equals(npc.getTypeID())) {
                    if (skrpg.getNpcCooldown().containsKey(p)) {
                        return;
                    }
                    skrpg.getNpcCooldown().put(p, 5);
                    npc.onInteract(p, playerData, skrpg);
                }

            }

        }
        if (skrpg.getForageManager().getForageLocation(e.getRightClicked()) != null) {
            ForageType forageType = skrpg.getForageManager().getForageLocation(e.getRightClicked()).getForageType();
            skrpg.getForageManager().getForageLocation(e.getRightClicked()).remove();
            Text.applyText(p, "&aYou found a " + forageType.getForage().itemDrop().getRarity().getColor() + forageType.getForage().itemDrop().getName() + "&a!");
            playerData.getPlayerActionManager().addExistingItem(new SKRPGItemStack(forageType.getForage().itemDrop().generateItemInfo(), 1));
            p.playSound(p.getLocation(), Sound.BLOCK_GRASS_BREAK, 2.0f, 0.2f);
            double totalMiningGained = forageType.getForage().expEarned().get(1);
            double totalHerbalismGained = forageType.getForage().expEarned().get(2);
            double totalCraftingGained = forageType.getForage().expEarned().get(3);
            if (totalMiningGained != 0.0) {
                playerData.getMining().setXpTillNext(playerData.getMining().getXpTillNext() +
                        totalMiningGained);
                playerData.getMining().setTotalXP(playerData.getMining().getTotalXP()
                        + totalMiningGained);
                playerData.getMining().levelUpSkill(p, playerData, skrpg);
                p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                        Text.color("&e+ " + totalMiningGained + " Mining XP (" + playerData.getMining()
                                .getXpTillNext() + "/" + Level.valueOf("_" +
                                (Integer.parseInt(playerData.getMining().getLevel().toString()
                                        .replace("_", "")) + 1)).getXpRequired() + ")")));

            } else if (totalHerbalismGained != 0.0) {
                p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                        Text.color("&e+ " + totalHerbalismGained + " Herbalism XP (" + playerData.getHerbalism()
                                .getXpTillNext() + "/" + Level.valueOf("_" +
                                (Integer.parseInt(playerData.getHerbalism().getLevel().toString()
                                        .replace("_", "")) + 1)).getXpRequired() + ")")));
                playerData.getHerbalism().setXpTillNext(playerData.getHerbalism().getXpTillNext() +
                        totalHerbalismGained);
                playerData.getHerbalism().setTotalXP(playerData.getHerbalism().getTotalXP()
                        + totalHerbalismGained);
                playerData.getHerbalism().levelUpSkill(p, playerData, skrpg);
            } else if (totalCraftingGained != 0.0) {
                p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                        Text.color("&e+ " + totalCraftingGained + " Crafting XP (" + playerData.getCrafting()
                                .getXpTillNext() + "/" + Level.valueOf("_" +
                                (Integer.parseInt(playerData.getCrafting().getLevel().toString()
                                        .replace("_", "")) + 1)).getXpRequired() + ")")));
                playerData.getCrafting().setXpTillNext(playerData.getCrafting().getXpTillNext() +
                        totalCraftingGained);
                playerData.getCrafting().setTotalXP(playerData.getCrafting().getTotalXP()
                        + totalCraftingGained);
                playerData.getCrafting().levelUpSkill(p, playerData, skrpg);
            }
        }
    }
    @EventHandler(priority = EventPriority.LOWEST)
    public void onEnviornmentalDamageEvent(EntityDamageEvent e) {
        if (e.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK ||
                e.getCause() == EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK) {
            return;
        }
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            PlayerData playerData = skrpg.getPlayerManager().getPlayerData(p.getUniqueId());
            if (e.getCause() == EntityDamageEvent.DamageCause.FALL) {
                double damage = e.getDamage();
                double percent = (damage * 5) / 100;
                playerData.getPlayerActionManager().damagePlayer((int) Math.round(playerData.getMaxHP() * percent), DamageType.NATURAL);
            } else if (e.getCause() == EntityDamageEvent.DamageCause.LAVA) {
                playerData.getPlayerActionManager().damagePlayer(playerData.getMaxHP() / 5, DamageType.FIRE);
            } else if (e.getCause() == EntityDamageEvent.DamageCause.FIRE) {
                playerData.getPlayerActionManager().damagePlayer(playerData.getMaxHP() / 40, DamageType.FIRE);
            } else if (e.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK) {
                playerData.getPlayerActionManager().damagePlayer(playerData.getMaxHP() / 40, DamageType.FIRE);
            } else if (e.getCause() == EntityDamageEvent.DamageCause.DROWNING) {
                playerData.getPlayerActionManager().damagePlayer(playerData.getMaxHP() / 10, DamageType.DROWNING);
            } else if (e.getCause() == EntityDamageEvent.DamageCause.SUFFOCATION) {
                playerData.getPlayerActionManager().damagePlayer(playerData.getMaxHP() / 10, DamageType.NATURAL);
            } else if (e.getCause() == EntityDamageEvent.DamageCause.VOID) {
                playerData.getPlayerActionManager().damagePlayer(playerData.getMaxHP(), DamageType.NATURAL);
            } else {
                e.setCancelled(true);
            }
        } else {
            e.setCancelled(true);
        }
        e.setDamage(0.1);
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
                ItemInfo itemInfo = ItemInfo.parseItemInfo(damager.getInventory().getItemInMainHand());
                if (itemInfo != null) {
                    damage = damagerData.getPlayerActionManager().calculateDamage();
                }

                if (damagerData.getRegion() == null) { e.setCancelled(true); return; }
                if (damagerData.getRegion().getRegionFlagsList().contains(RegionFlags.PVP_ALLOWED)) {
                    if (skrpg.getHitCooldown().containsKey(damager)) {
                        e.setCancelled(true);
                        return;
                    }
                    skrpg.getPlayerManager().getPlayerData(e.getEntity().getUniqueId()).getPlayerActionManager()
                            .damageByPlayer(damager, damage, DamageType.REGULAR);
                    damagerData.getPlayerActionManager().applyAttackSpeed();
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

                playerData.getPlayerActionManager().damagePlayer(damage, DamageType.REGULAR);
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
                if (skrpg.getHitCooldown().containsKey(player)) {
                    e.setCancelled(true);
                    return;
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

                mob.damage(player, damage, skrpg, DamageType.REGULAR);
                playerData.getPlayerActionManager().applyAttackSpeed();
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
        if (playerData.getPlayerInventory().getItemInMainHand() != null) {
            SKRPGItemStack skrpgItemStack = playerData.getPlayerInventory().getItemInMainHand();
            if (skrpgItemStack != null) {
                ItemInfo itemInfo = playerData.getPlayerInventory().getItemInMainHand().getItemInfo();
                if (itemInfo != null) {
                    if (itemInfo.getItem().getItemType().equals(ItemType.FOOD)) {
                        if (Foods.getFood(itemInfo.getItem()).getFoodAction() != null) {
                            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_BURP, 1.0f, 1.0f);
                            Text.applyText(p, "&aYou ate " + itemInfo.getItem().getName() + "!");
                            if (playerData.getPlayerInventory().getItemInMainHand().getAmount() != 1) {
                                playerData.getPlayerInventory().getItemInMainHand().setAmount(playerData.getPlayerInventory().getItemInMainHand().getAmount() - 1);
                            } else {
                                playerData.getPlayerInventory().removeItem(playerData.getPlayerInventory().getItemInMainHand().getItemInfo().getItem());
                            }
                            Foods.getFood(itemInfo.getItem()).getFoodAction().getFoodAction().onEat(p, skrpg, playerData);
                        }
                    }
                }
            }
        }
        if (playerData.getPlayerInventory().getItemInMainHand() != null) {
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
        if (playerData.getRegion() != null) {
            if (playerData.getRegion().getRegionFlagsList().contains(RegionFlags.DECORATION_REGION)) {
                p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0f, 1.0f);
                e.setCancelled(true);
                return;
            }
        }
        playerData.getPlayerActionManager().onBreakBlock(e.getBlock());
        e.setCancelled(true);
    }
    @EventHandler
    public void onCloseEvent(InventoryCloseEvent e) {
        if (e.getView().getTitle().equals("Crafting Table")) {
            Player p  = (Player) e.getPlayer();


            PlayerData playerData = skrpg.getPlayerManager().getPlayerData(p.getUniqueId());
            playerData.getPlayerInventory().addItem(new SKRPGItemStack(ItemInfo.parseItemInfo(p.getItemOnCursor()), p.getItemOnCursor().getAmount()));
            p.setItemOnCursor(null);
            List<Integer> slots = Arrays.asList(10, 11, 12, 19, 20, 21, 28, 29, 30);
            for (int slot : slots) {
                if (e.getInventory().getItem(slot) != null) {
                    playerData.getPlayerInventory().addItem(new SKRPGItemStack(ItemInfo.parseItemInfo(e.getInventory().getItem(slot)), e.getInventory().getItem(slot).getAmount()));
                    e.getInventory().removeItem(e.getInventory().getItem(slot));
                }
            }
        } else if (e.getView().getTitle().equals("Enchant Item")) {
            Player p  = (Player) e.getPlayer();
            PlayerData playerData = skrpg.getPlayerManager().getPlayerData(p.getUniqueId());
            if (e.getInventory().getItem(13) != null) {
                playerData.getPlayerInventory().addItem(new SKRPGItemStack(ItemInfo.parseItemInfo(e.getInventory().getItem(13)), e.getInventory().getItem(13).getAmount()));
                e.getInventory().removeItem(e.getInventory().getItem(13));
            }
        } else if (e.getView().getTitle().equals("Apply Runic Stone")) {
            Player p  = (Player) e.getPlayer();
            PlayerData playerData = skrpg.getPlayerManager().getPlayerData(p.getUniqueId());
            if (e.getInventory().getItem(30) != null) {
                playerData.getPlayerInventory().addItem(new SKRPGItemStack(ItemInfo.parseItemInfo(e.getInventory().getItem(30)), e.getInventory().getItem(30).getAmount()));
                e.getInventory().removeItem(e.getInventory().getItem(30));
            }
            if (e.getInventory().getItem(32) != null) {
                playerData.getPlayerInventory().addItem(new SKRPGItemStack(ItemInfo.parseItemInfo(e.getInventory().getItem(32)), e.getInventory().getItem(32).getAmount()));
                e.getInventory().removeItem(e.getInventory().getItem(32));
            }
        } else if (e.getView().getTitle().equals("Cook Food")) {
            Player p  = (Player) e.getPlayer();
            PlayerData playerData = skrpg.getPlayerManager().getPlayerData(p.getUniqueId());
            if (e.getInventory().getItem(30) != null) {
                playerData.getPlayerInventory().addItem(new SKRPGItemStack(ItemInfo.parseItemInfo(e.getInventory().getItem(30)), e.getInventory().getItem(30).getAmount()));
                e.getInventory().removeItem(e.getInventory().getItem(30));
            }
            if (e.getInventory().getItem(32) != null) {
                playerData.getPlayerInventory().addItem(new SKRPGItemStack(ItemInfo.parseItemInfo(e.getInventory().getItem(32)), e.getInventory().getItem(32).getAmount()));
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
            PlayerData playerData = skrpg.getPlayerManager().getPlayerData(p.getUniqueId());
            if (e.getInventory().getItem(22) != null) {
                playerData.getPlayerInventory().addItem(new SKRPGItemStack(ItemInfo.parseItemInfo(e.getInventory().getItem(22)), e.getInventory().getItem(22).getAmount()));
                e.getInventory().removeItem(e.getInventory().getItem(22));
            }
        } else if (e.getView().getTitle().equals("Process")) {
            Player p  = (Player) e.getPlayer();
            PlayerData playerData = skrpg.getPlayerManager().getPlayerData(p.getUniqueId());
            if (e.getInventory().getItem(22) != null) {
                playerData.getPlayerInventory().addItem(new SKRPGItemStack(ItemInfo.parseItemInfo(e.getInventory().getItem(22)), e.getInventory().getItem(22).getAmount()));
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
        if (e.getClickedInventory() != null && e.getClickedInventory().getType().equals(InventoryType.PLAYER)) {
            playerData.getPlayerActionManager().generateInventoryData();
            playerData.getPlayerInventory().updateInventory(p);
        }
        if (e.getSlotType().equals(InventoryType.SlotType.ARMOR)) {
            for (ItemStack itemStack : p.getInventory().getArmorContents()) {
                if (itemStack != null && itemStack.getType() != Material.AIR) {
                    playerData.getPlayerInventory().getInventoryEquipment().addItem(new SKRPGItemStack(ItemInfo.parseItemInfo(itemStack), itemStack.getAmount()));
                }
            }
        }
        if (e.getClickedInventory() != null && e.getClickedInventory().getType().equals(InventoryType.PLAYER) &&
        e.getCurrentItem() != null) {

            net.minecraft.server.v1_16_R3.ItemStack nbtStack = CraftItemStack.asNMSCopy(e.getCurrentItem());
            if (nbtStack.hasTag()) {
                if (nbtStack.getTag().hasKey("item")) {
                    skrpg.getLogger().info(e.getAction().toString());
                    if (Arrays.asList(9, 10, 11, 12).contains(e.getRawSlot())) {
                        if (e.getAction().equals(InventoryAction.SWAP_WITH_CURSOR)) {
                        e.setCancelled(true);
                        String item = nbtStack.getTag().getString("item");
                        skrpg.getLogger().info(item);
                        ItemType itemType = ItemType.valueOf(item);
                        if (e.getCurrentItem() != null) {
                            ItemInfo itemSlotInfo = ItemInfo.parseItemInfo(e.getCurrentItem());
                            if (ItemInfo.parseItemInfo(p.getItemOnCursor()) != null) {
                                ItemInfo itemInfo = ItemInfo.parseItemInfo(p.getItemOnCursor());
                                if (itemInfo.getItem().getItemType() == itemType) {
                                    if (itemSlotInfo != null) {
                                        ItemStack itemStack = e.getCurrentItem().clone();
                                        net.minecraft.server.v1_16_R3.ItemStack itemStackNMS = CraftItemStack.asNMSCopy(itemStack);
                                        NBTTagCompound itemStackCompound = (itemStackNMS.hasTag()) ? itemStackNMS.getTag() : new NBTTagCompound();
                                        itemStackCompound.remove("item");
                                        skrpg.getLogger().info("1 number");
                                        playerData.getPlayerInventory().removeItem(itemInfo.getItem());
                                        skrpg.getLogger().info("2 number");
                                        playerData.getPlayerInventory().getInventoryEquipment().addItem(new SKRPGItemStack(itemInfo, p.getItemOnCursor().getAmount()));
                                        skrpg.getLogger().info("3 number");
                                        p.getInventory().setItem(e.getSlot(), p.getItemOnCursor());
                                        playerData.getPlayerInventory().updateInventory(p);
                                        p.setItemOnCursor(CraftItemStack.asBukkitCopy(itemStackNMS));


                                    } else {
                                        if (e.getCurrentItem().getItemMeta().getDisplayName().contains(Text.color("&cNO "))) {
                                            skrpg.getLogger().info("1 number");
                                            playerData.getPlayerInventory().removeItem(itemInfo.getItem());
                                            skrpg.getLogger().info("2 number");
                                            playerData.getPlayerInventory().getInventoryEquipment().addItem(new SKRPGItemStack(itemInfo, p.getItemOnCursor().getAmount()));
                                            skrpg.getLogger().info("3 number");
                                            p.getInventory().setItem(e.getSlot(), p.getItemOnCursor());
                                            playerData.getPlayerInventory().updateInventory(p);
                                            p.setItemOnCursor(null);
                                        }
                                    }
                                }

                        }
                    }
                        } else if (e.getAction().equals(InventoryAction.PICKUP_ALL)) {
                            ItemInfo itemSlotInfo = ItemInfo.parseItemInfo(e.getCurrentItem());
                            e.setCancelled(true);
                            if (itemSlotInfo != null) {
                                ItemStack itemStack = e.getCurrentItem().clone();
                                net.minecraft.server.v1_16_R3.ItemStack itemStackNMS = CraftItemStack.asNMSCopy(itemStack);
                                NBTTagCompound itemStackCompound = (itemStackNMS.hasTag()) ? itemStackNMS.getTag() : new NBTTagCompound();
                                itemStackCompound.remove("item");
                                p.setItemOnCursor(CraftItemStack.asBukkitCopy(itemStackNMS));
                                playerData.getPlayerInventory().getInventoryEquipment().removeItem(itemSlotInfo.getItem().getItemType());
                                p.getInventory().setItem(e.getSlot(), null);
                                playerData.getPlayerInventory().updateInventory(p);
                            } else {

                            }
                        }
                    }
                }
            }
        }
        if (e.getClickedInventory() != null && e.getClickedInventory().getType().equals(InventoryType.PLAYER)) {
            playerData.getPlayerActionManager().generateInventoryData();
            playerData.getPlayerInventory().updateInventory(p);
        }
        if (e.getAction().equals(InventoryAction.SWAP_WITH_CURSOR) && e.getClickedInventory().getType().equals(InventoryType.PLAYER)) {
            if (ItemInfo.parseItemInfo(p.getItemOnCursor()) != null && ItemInfo.parseItemInfo(p.getItemOnCursor()).getItem() == Items.ENCHANTMENT_SCROLL) {
                ItemInfo cursorInfo = ItemInfo.parseItemInfo(p.getItemOnCursor());
                if (cursorInfo != null) {
                    if (cursorInfo.getEnchantmentScrollEnchantment() != null) {

                        e.setCancelled(true);
                        if (cursorInfo.getRunicPointCost() > playerData.getRunicPoints()) {
                            Text.applyText(p, "&c&lNOPE! &r&8| &cYou dont have enough &5Runic Points&c!");
                            return;
                        }
                        ItemInfo itemInfo = ItemInfo.parseItemInfo(e.getCurrentItem());
                        if (itemInfo.hasEnchantment(cursorInfo.getEnchantmentScrollEnchantment().getEnchantmentType())) {
                            if (itemInfo.getEnchantment(cursorInfo.getEnchantmentScrollEnchantment().getEnchantmentType()).getLevel() > cursorInfo.getEnchantmentScrollEnchantment().getLevel()) {
                                Text.applyText(p, "&c&lNOPE! &r&8| &cYour item has a higher level of this enchantment!");
                                return;
                            }
                            itemInfo.getEnchantmentsList().remove(itemInfo.getEnchantment(cursorInfo.getEnchantmentScrollEnchantment().getEnchantmentType()));
                        }
                        if (cursorInfo.getEnchantmentScrollEnchantment().getEnchantmentType().getCatagory().contains(itemInfo.getItem().getItemType().getItemCatagory())) {
                            if (cursorInfo.getEnchantmentScrollEnchantment().getEnchantmentType().getExclusiveItemType() != null) {
                                if (cursorInfo.getEnchantmentScrollEnchantment().getEnchantmentType().getExclusiveItemType().contains(itemInfo.getItem().getItemType())) {
                                    p.setItemOnCursor(null);
                                    p.updateInventory();
                                    playerData.removeRunicPoints(cursorInfo.getRunicPointCost());
                                    Text.applyText(p, "&5&lWOOSH! &r&8| &7You applied &5" + cursorInfo.getEnchantmentScrollEnchantment().getEnchantmentType().getName() + " " + cursorInfo.getEnchantmentScrollEnchantment().getLevel() + " &7to your " +
                                            itemInfo.getRarity().getColor() + itemInfo.getItem().getName() + "&7!");
                                    p.playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 0.2f);
                                    new BukkitRunnable() {

                                        @Override
                                        public void run() {
                                            p.playSound(p.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.0f, 0.2f);
                                        }
                                    }.runTaskLater(skrpg, 4);
                                    itemInfo.addEnchantment(cursorInfo.getEnchantmentScrollEnchantment());
                                    Items.updateItem(e.getCurrentItem(), itemInfo);
                                } else {
                                    Text.applyText(p, "&c&lNOPE! &r&8| &cThis enchantment is not compatible with this item!");
                                    return;

                                }

                            } else {
                                p.setItemOnCursor(null);
                                p.updateInventory();
                                playerData.removeRunicPoints(cursorInfo.getRunicPointCost());
                                Text.applyText(p, "&5&lWOOSH! &r&8| &7You applied &5" + cursorInfo.getEnchantmentScrollEnchantment().getEnchantmentType().getName() + " " + cursorInfo.getEnchantmentScrollEnchantment().getLevel() + " &7to your " +
                                        itemInfo.getRarity().getColor() + itemInfo.getItem().getName() + "&7!");
                                p.playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 0.2f);
                                new BukkitRunnable() {

                                    @Override
                                    public void run() {
                                        p.playSound(p.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.0f, 0.2f);
                                    }
                                }.runTaskLater(skrpg, 4);
                                itemInfo.addEnchantment(cursorInfo.getEnchantmentScrollEnchantment());
                                Items.updateItem(e.getCurrentItem(), itemInfo);
                            }

                        } else {
                            Text.applyText(p, "&c&lNOPE! &r&8| &cThis enchantment is not compatible with this item!");
                            return;
                        }
                    }
                }
            }
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

                        playerData.getPlayerInventory().addItem(new SKRPGItemStack(ItemInfo.parseItemInfo(e.getCurrentItem()), e.getCurrentItem().getAmount()));
                        e.getInventory().setItem(25, null);
                        p.getItemOnCursor().setType(Material.AIR);
                        playerData.getPlayerActionManager().generateInventoryData();

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
                    skrpg.getLogger().info(itemInfo.getItem().toString() + " " + items.getCraftingAmount());

                    p.performCommand("ct");
                    p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_USE, 1.0f, 0.5f);

                    p.setItemOnCursor(null);
                }

            }
        } else if (e.getView().getTitle().equals("SKQuest Menu")) {
            e.setCancelled(true);
            if (e.getCurrentItem().getItemMeta().getDisplayName().equals(Text.color("&aCrafting Table"))) {
                p.performCommand("ct");
            } else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(Text.color("&aYour Statistics"))) {
                p.performCommand("stats");
            } else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(Text.color("&aSkills"))) {
                p.performCommand("skills");
            } else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(Text.color("&aAccumulations"))) {
                p.performCommand("accumulations");
            } else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(Text.color("&aSettings"))) {
                p.performCommand("settings");
            } else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(Text.color("&aQuests"))) {
                p.performCommand("quests");
            } else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(Text.color("&5Runic Table"))) {
                playerData.getPlayerActionManager().buildRunicTable();
            } else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(Text.color("&aManage Banks"))) {
                playerData.getPlayerActionManager().openBankMenu();
            } else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(Text.color("&aStovetop"))) {
                playerData.getPlayerActionManager().openStovetop();
            } else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(Text.color("&2Your Guild"))) {
                p.performCommand("guild menu");
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
                } else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Fishing")) {
                    p.performCommand("skills fishing");
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
            } else if (e.getView().getTitle().equals("Fisherman")) {
                sellMerchant = SellMerchant.FISHING;
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
                                        playerData.getPlayerInventory().removeItem(ItemInfo.parseItemInfo(e.getCurrentItem()).getItem());
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
                    double cost = 0;
                    for (String lore : e.getCurrentItem().getItemMeta().getLore()) {
                        if (lore.contains("Cost:")) {
                            skrpg.getLogger().info("Cost");
                            cost = Double.parseDouble(ChatColor.stripColor(lore).replace("Cost:", "")
                            .replace("Nuggets", "").replace(" ", ""));
                        }
                    }
                    ItemInfo itemInfo = ItemInfo.parseItemInfo(e.getCurrentItem());
                        if (itemInfo != null) {
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
                            playerData.getPlayerInventory().addItem(new SKRPGItemStack(itemInfo, 1));
                            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1.0f, 2.0f);
                            Text.applyText(p, "&aYou purchased " + itemInfo.getItem().getRarity().getColor() + itemInfo.getItem().getName() + " for &6" + cost + " Nuggets");
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
                            playerData.getTrade().addItem(p, new SKRPGItemStack(itemInfo, e.getCurrentItem().getAmount()));
                            e.getClickedInventory().setItem(e.getSlot(), null);
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
        e.getView().getTitle().equals("Your Crafting") || e.getView().getTitle().equals("Your Mining") || e.getView().getTitle().contains("| Accumulation Viewer")
                || e.getView().getTitle().equals(Text.color("&5Your Runics")) || e.getView().getTitle().equals(Text.color("Your Fishing")) || e.getView().getTitle().equals(Text.color("Your Quests")) ) {
            e.setCancelled(true);
        } else if (e.getView().getTitle().equals("Settings")) {
            e.setCancelled(true);
            if (e.getCurrentItem() != null) {
                if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Toggle Trading")) {
                    playerData.setToggleTrade(!playerData.canTrade());
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1.0f, 1.0f);
                    if (playerData.canTrade()) {
                        Text.applyText(p, Text.color("&aYou can now recieve and accept trade requests from other players."));
                    } else {
                        Text.applyText(p, Text.color("&cYou can no longer recieve and accept trade requests."));
                    }

                    p.performCommand("settings");
                } else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Dropping Items")) {
                    playerData.setCanDrop(!playerData.canDrop());
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1.0f, 1.0f);
                    if (playerData.canDrop()) {
                        Text.applyText(p, Text.color("&aYou can now drop items! Be careful, this may cause you to lose items if you drop them."));
                    } else {
                        Text.applyText(p, Text.color("&cYou can no longer drop items."));
                    }


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
                    playerData.createInventory();
                    playerData.getPlayerInventory().updateInventory(p);
                    if (playerData.getGuild() != null) {
                            if (playerData.getGuild().getPlayersInGuild().get(playerData) == GuildRank.OWNER) {
                        for (PlayerData playerData1 : playerData.getGuild().getPlayersInGuild().keySet()) {
                            if (Bukkit.getPlayer(playerData1.getUuid()) != null) {
                                Text.applyText(Bukkit.getPlayer(playerData1.getUuid()), "&c" + p.getDisplayName() + " disbanded your guild. (Profile Deleted)");
                                Bukkit.getPlayer(playerData1.getUuid()).playSound(Bukkit.getPlayer(playerData1.getUuid()).getLocation(), Sound.ENTITY_WITHER_DEATH, 1.0f, 0.5f);
                            }
                        }
                        skrpg.getGuildManager().deleteGuild(playerData.getGuild().getId());
                    } else {
                        playerData.getGuild().getPlayersInGuild().remove(playerData);
                        for (PlayerData playerData1 : playerData.getGuild().getPlayersInGuild().keySet()) {
                            if (Bukkit.getPlayer(playerData1.getUuid()) != null) {
                                Text.applyText(Bukkit.getPlayer(playerData1.getUuid()), "&c" + p.getDisplayName() + " left your guild. (Profile Deleted)");
                                Bukkit.getPlayer(playerData1.getUuid()).playSound(Bukkit.getPlayer(playerData1.getUuid()).getLocation(), Sound.ENTITY_WITHER_DEATH, 1.0f, 0.5f);
                            }
                        }
                    }
                    }
                    skrpg.getPlayerManager().removePlayer(p.getUniqueId());
                    p.kickPlayer(Text.color("&cResetting your SKQuest game...")); 

                    skrpg.getPlayerManager().createPlayer(p.getUniqueId());
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

        } else if (e.getView().getTitle().equals("Your Accumulations")) {
            e.setCancelled(true);
            for (CollectionType collectionType : EnumSet.allOf(CollectionType.class)) {
                if (e.getCurrentItem().getItemMeta().getDisplayName().contains(collectionType.getItem().getName())) {
                    p.performCommand("accumulations " + collectionType.toString());
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
                       if (enchantments.isTableEnchantment() && itemInfo.getItem() == Items.ENCHANTMENT_SCROLL) {
                           validEnchantments.add(enchantments);
                       } else if (enchantments.getCatagory().contains(itemInfo.getItem().getItemType().getItemCatagory())
                        && enchantments.isTableEnchantment()) {
                            if (enchantments.getExclusiveItemType() != null) {
                                if (enchantments.getExclusiveItemType().contains(itemInfo.getItem().getItemType())) {
                                    validEnchantments.add(enchantments);
                                }

                            } else {
                                validEnchantments.add(enchantments);
                            }

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
                        if (itemInfo.getItem().getItemType() != ItemType.ENCHANTMENT_SCROLL) {
                            itemInfo.addEnchantment(new Enchantment(selectedEnchantment, randomLevel + 1));
                            Items.updateItem(e.getInventory().getItem(13), itemInfo);
                        } else {
                            e.getInventory().setItem(13, Items.buildEnchantmentScroll(new Enchantment(selectedEnchantment, randomLevel + 1)));
                        }
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
                        if (itemInfo.getItem().getItemType() != ItemType.ENCHANTMENT_SCROLL) {
                            itemInfo.addEnchantment(new Enchantment(selectedEnchantment, randomLevel + 3));
                            Items.updateItem(e.getInventory().getItem(13), itemInfo);
                        } else {
                            ItemStack itemStack = Items.buildEnchantmentScroll(new Enchantment(selectedEnchantment, randomLevel + 3));
                            e.getInventory().setItem(13, itemStack);
                        }
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
                        if (itemInfo.getItem().getItemType() != ItemType.ENCHANTMENT_SCROLL) {
                            itemInfo.addEnchantment(new Enchantment(selectedEnchantment, 5));
                            Items.updateItem(e.getInventory().getItem(13), itemInfo);
                        } else {
                            ItemStack itemStack = Items.buildEnchantmentScroll(new Enchantment(selectedEnchantment, 5));
                            e.getInventory().setItem(13, itemStack);
                        }
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
                    playerData.getPlayerInventory().addItem(new SKRPGItemStack(ItemInfo.parseItemInfo(e.getInventory().getItem(13)), e.getInventory().getItem(13).getAmount()));
                    e.getInventory().removeItem(e.getInventory().getItem(13));
                    e.setCancelled(true);
                    return;
                }
                if (e.getClickedInventory().getType().equals(InventoryType.PLAYER)) {
                    if (p.getOpenInventory().getTopInventory().getItem(13) == null) {
                        p.getOpenInventory().getTopInventory().setItem(13, e.getCurrentItem());
                        playerData.getPlayerInventory().removeItem(ItemInfo.parseItemInfo(e.getCurrentItem()).getItem());
                        p.playSound(p.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.0f, 1.0f);
                    }
                } else if (e.getSlot() == 13 && e.getInventory().getItem(13) != null) {
                    playerData.getPlayerInventory().addItem(new SKRPGItemStack(ItemInfo.parseItemInfo(e.getInventory().getItem(13)), e.getInventory().getItem(13).getAmount()));
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
                    playerData.getPlayerInventory().addItem(new SKRPGItemStack(iF, topInv.getItem(13).getAmount()));
                    e.getInventory().removeItem(topInv.getItem(13));
                    e.getInventory().removeItem(topInv.getItem(32));
                    e.getInventory().removeItem(topInv.getItem(30));
                    Text.applyText(p, "&5&lNICE! &r&8| &r&7You applied &5" + iF.getRunicStones().getName() + " &7to your " + iF.getRarity().getColor() + iF.getItem().getName() + "&7!");
                }
                ItemInfo itemInfo = ItemInfo.parseItemInfo(e.getCurrentItem());
                if (itemInfo != null) {
                    if (e.getSlot() == 32 && e.getInventory().getItem(32) != null) {
                        playerData.getPlayerInventory().addItem(new SKRPGItemStack(ItemInfo.parseItemInfo(e.getCurrentItem()), e.getCurrentItem().getAmount()));
                        e.getInventory().removeItem(e.getCurrentItem());
                        p.playSound(p.getLocation(), Sound.BLOCK_RESPAWN_ANCHOR_DEPLETE, 1.0f, 0.2f);
                    }
                    if (e.getSlot() == 30 && e.getInventory().getItem(30) != null) {
                        playerData.getPlayerInventory().addItem(new SKRPGItemStack(ItemInfo.parseItemInfo(e.getCurrentItem()), e.getCurrentItem().getAmount()));
                        e.getInventory().removeItem(e.getCurrentItem());
                        p.playSound(p.getLocation(), Sound.BLOCK_RESPAWN_ANCHOR_DEPLETE, 1.0f, 0.2f);
                    }
                    if (itemInfo.getItem().getItemType().equals(ItemType.RUNIC_STONE)) {
                        if (e.getRawSlot() > e.getView().getTopInventory().getSize()) {
                            Inventory openInv = p.getOpenInventory().getTopInventory();
                            if (openInv.getItem(32) == null) {
                                openInv.setItem(32, e.getCurrentItem());
                                playerData.getPlayerInventory().removeItem(ItemInfo.parseItemInfo(e.getCurrentItem()).getItem());
                                p.playSound(p.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.0f, 1.0f);
                            }
                        }

                    } else {
                        if (e.getRawSlot() > e.getView().getTopInventory().getSize()) {
                            Inventory openInv = p.getOpenInventory().getTopInventory();
                            if (openInv.getItem(30) == null) {
                                openInv.setItem(30, e.getCurrentItem());
                                playerData.getPlayerInventory().removeItem(ItemInfo.parseItemInfo(e.getCurrentItem()).getItem());
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
                        if (itemInfo.getItem().getItemType().getItemCatagory().equals(ItemCatagory.WEAPON) ||
                                itemInfo.getItem().getItemType().getItemCatagory().equals(ItemCatagory.ARMOR) ||
                                itemInfo.getItem().getItemType().getItemCatagory().equals(ItemCatagory.BOW) ||
                                itemInfo.getItem().getItemType().getItemCatagory().equals(ItemCatagory.TOOL)) {
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
                            playerData.getPlayerInventory().removeItem(ItemInfo.parseItemInfo(e.getCurrentItem()).getItem());
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
                    playerData.getPlayerInventory().addItem(new SKRPGItemStack(ItemInfo.parseItemInfo(topInv.getItem(13)), topInv.getItem(13).getAmount()));
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
                        playerData.getPlayerInventory().addItem(new SKRPGItemStack(ItemInfo.parseItemInfo(e.getCurrentItem()), e.getCurrentItem().getAmount()));
                        e.getInventory().removeItem(e.getCurrentItem());
                        p.playSound(p.getLocation(), Sound.ENTITY_ITEM_FRAME_REMOVE_ITEM, 1.0f, 0.2f);
                    }
                    if (e.getSlot() == 30 && e.getInventory().getItem(30) != null) {
                        playerData.getPlayerInventory().addItem(new SKRPGItemStack(ItemInfo.parseItemInfo(e.getCurrentItem()), e.getCurrentItem().getAmount()));
                        e.getInventory().removeItem(e.getCurrentItem());
                        p.playSound(p.getLocation(), Sound.ENTITY_ITEM_FRAME_REMOVE_ITEM, 1.0f, 0.2f);
                    }
                    if (itemInfo.getItem().getItemType().equals(ItemType.FOOD_BASE)) {
                        if (e.getRawSlot() > e.getView().getTopInventory().getSize()) {
                            Inventory openInv = p.getOpenInventory().getTopInventory();
                            if (openInv.getItem(30) == null) {
                                openInv.setItem(30, e.getCurrentItem());
                                playerData.getPlayerInventory().removeItem(ItemInfo.parseItemInfo(e.getCurrentItem()).getItem());
                                p.playSound(p.getLocation(), Sound.ENTITY_PHANTOM_FLAP, 1.0f, 0.2f);
                            }
                        }

                    } else {
                        if (e.getRawSlot() > e.getView().getTopInventory().getSize()) {
                            Inventory openInv = p.getOpenInventory().getTopInventory();
                            if (openInv.getItem(32) == null) {
                                openInv.setItem(32, e.getCurrentItem());
                                playerData.getPlayerInventory().removeItem(ItemInfo.parseItemInfo(e.getCurrentItem()).getItem());
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
                            playerData.getPlayerInventory().removeItem(ItemInfo.parseItemInfo(e.getCurrentItem()).getItem());
                            p.playSound(p.getLocation(), Sound.BLOCK_FIRE_AMBIENT, 1.0f, 0.2f);
                        }
                    } else {
                        if (e.getSlot() == 22) {
                            playerData.getPlayerInventory().addItem(new SKRPGItemStack(ItemInfo.parseItemInfo(e.getInventory().getItem(22)), e.getInventory().getItem(22).getAmount()));
                            e.getInventory().removeItem(e.getInventory().getItem(22));
                            p.playSound(p.getLocation(), Sound.BLOCK_FIRE_AMBIENT, 1.0f, 0.2f);
                        }
                    }
                }
                if (e.getSlot() == 13 && e.getInventory().getItem(22) != null) {
                    ItemInfo itemItemInfo = ItemInfo.parseItemInfo(e.getInventory().getItem(22));
                    int coalTotal = 0;
                    List<SKRPGItemStack> removedCoal = new ArrayList<>();
                    if (itemItemInfo != null) {
                        if (itemItemInfo.getItem().isCookable() && !itemItemInfo.isCooked()) {
                            for (SKRPGItemStack itemStack : playerData.getPlayerInventory().getContents()) {
                                ItemInfo itemInfoItem = itemStack.getItemInfo();
                                if (itemInfoItem != null) {
                                    if (itemInfoItem.getItem() == Items.COAL) {
                                        coalTotal = coalTotal + itemStack.getAmount();
                                        removedCoal.add(itemStack);
                                    }
                                }
                            }
                            if (coalTotal > ((itemItemInfo.getRarity().getPriority() * itemItemInfo.getQuality().getStar() * 5)
                                    * e.getInventory().getItem(22).getAmount())) {
                                coalTotal = ((itemItemInfo.getRarity().getPriority() * itemItemInfo.getQuality().getStar() * 5)
                                        * e.getInventory().getItem(22).getAmount());
                            }
                            if (coalTotal < ((itemItemInfo.getRarity().getPriority() * itemItemInfo.getQuality().getStar() * 5)
                                    * e.getInventory().getItem(22).getAmount())) {
                                Text.applyText(p, "&cYou need more &8Coal &cto process this!");
                                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                            } else {
                                itemItemInfo.setCooked(true);
                                Items.updateItem(e.getInventory().getItem(22), itemItemInfo);
                                playerData.getPlayerInventory().addItem(new SKRPGItemStack(itemItemInfo, e.getInventory().getItem(22).getAmount()));
                                e.getInventory().removeItem(e.getInventory().getItem(22));
                                p.playSound(p.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1.0f, 0.2f);
                                for (SKRPGItemStack itemStack : removedCoal) {
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
                            playerData.getPlayerInventory().removeItem(ItemInfo.parseItemInfo(e.getCurrentItem()).getItem());
                            p.playSound(p.getLocation(), Sound.BLOCK_SWEET_BERRY_BUSH_BREAK, 1.0f, 0.2f);
                        }
                    }  else {
                        if (e.getSlot() == 22) {
                            playerData.getPlayerInventory().addItem(new SKRPGItemStack(ItemInfo.parseItemInfo(e.getInventory().getItem(22)), e.getInventory().getItem(22).getAmount()));
                            e.getInventory().removeItem(e.getInventory().getItem(22));
                            p.playSound(p.getLocation(), Sound.BLOCK_SWEET_BERRY_BUSH_BREAK, 1.0f, 0.2f);
                        }
                    }
                }
                if (e.getSlot() == 13 && e.getInventory().getItem(22) != null) {
                    ItemInfo itemItemInfo = ItemInfo.parseItemInfo(e.getInventory().getItem(22));
                    int ironTotal = 0;
                    List<SKRPGItemStack> removedIron = new ArrayList<>();
                    if (itemItemInfo != null) {
                        if (itemItemInfo.getItem().isProcessable() && !itemItemInfo.isProcessed()) {
                            for (SKRPGItemStack itemStack : playerData.getPlayerInventory().getContents()) {

                                if (itemStack != null) {
                                    if (itemStack.getItemInfo().getItem() == Items.IRON_INGOT) {
                                        ironTotal = ironTotal + itemStack.getAmount();
                                        removedIron.add(itemStack);
                                    }
                                }
                            }
                            if (ironTotal > ((itemItemInfo.getRarity().getPriority() * itemItemInfo.getQuality().getStar() * 5)
                                    * e.getInventory().getItem(22).getAmount())) {
                                ironTotal = ((itemItemInfo.getRarity().getPriority() * itemItemInfo.getQuality().getStar() * 5)
                                        * e.getInventory().getItem(22).getAmount());
                            }
                            if (ironTotal < ((itemItemInfo.getRarity().getPriority() * itemItemInfo.getQuality().getStar() * 5)
                                    * e.getInventory().getItem(22).getAmount())) {
                                Text.applyText(p, "&cYou need more &fIron &cto process this!");
                                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                            } else {
                                itemItemInfo.setProcessed(true);
                                Items.updateItem(e.getInventory().getItem(22), itemItemInfo);
                                playerData.getPlayerInventory().addItem(new SKRPGItemStack(itemItemInfo, e.getInventory().getItem(22).getAmount()));
                                e.getInventory().removeItem(e.getInventory().getItem(22));
                                p.playSound(p.getLocation(), Sound.BLOCK_NETHER_SPROUTS_BREAK, 1.0f, 0.2f);
                                for (SKRPGItemStack itemStack : removedIron) {
                                    if (itemStack.getAmount() > ironTotal) {
                                        itemStack.setAmount(itemStack.getAmount() - ironTotal);
                                    }
                                    ironTotal = ironTotal - itemStack.getAmount();
                                }
                            }

                        }
                    }
                }
            }
            updateProcess(e.getView().getTopInventory());
        } else if (e.getView().getTitle().equalsIgnoreCase(Text.color("&6♛ &7Royalty Menu &6♛"))) {
            e.setCancelled(true);
            if (e.getCurrentItem() != null) {
                if (e.getCurrentItem().getItemMeta().getDisplayName().equals(Text.color("&7Refresh for &61000 Royalty Points"))) {
                    if (!playerData.hasRefreshed()) {
                        if (playerData.getRoyaltyPoints() < 1000) {
                            Text.applyText(p, "&cYou need more Royalty Points to refresh!");
                            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 0.2f);
                        } else {
                            p.closeInventory();
                            playerData.getRoyaltyQuests().clear();
                            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_YES, 1.0f, 0.4f);
                            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_FLUTE, 1.0f, 0.2f);
                            Text.applyText(p, "&aYour &6♛ Royalty Quests &aare refreshed!");
                            Text.applyText(p, " ");
                            List<RoyaltyQuestType> royaltyQuestTypes = new ArrayList<>(EnumSet.allOf(RoyaltyQuestType.class));
                            for (int i = 0; i <= playerData.getRoyaltyQuestSlots(); i++) {
                                int randomQuest = new Random().nextInt(royaltyQuestTypes.size());
                                List<RoyaltyQuestDifficulty> validDifficulties = new ArrayList<>();

                                for (RoyaltyQuestDifficulty royaltyQuestDifficulty : EnumSet.allOf(RoyaltyQuestDifficulty.class)) {
                                    if (SKRPG.levelToInt(playerData.getCombat().getLevel().toString()) >= SKRPG.levelToInt(royaltyQuestDifficulty.getMinLevel().toString())) {
                                        validDifficulties.add(royaltyQuestDifficulty);
                                    }
                                }
                                int randomDifficulty = new Random().nextInt(validDifficulties.size());
                                RoyaltyQuest royaltyQuest = new RoyaltyQuest(0,
                                        royaltyQuestTypes.get(randomQuest).getAmountNeeded() * (validDifficulties.get(randomDifficulty).getPriority() + 1), royaltyQuestTypes.get(randomQuest), validDifficulties.get(randomDifficulty));
                                Text.applyText(p, "&8&l>  &r&6" + royaltyQuest.getRoyaltyQuestType().getName() + " &8&l| &r" + royaltyQuest.getDifficulty().getColoredName());
                                Text.applyText(p, "&7" + royaltyQuest.getRoyaltyQuestType().getObjective());
                                Text.applyText(p, " ");
                                Text.applyText(p, "&6" + royaltyQuest.getProgressInteger() + "&7/&6" + royaltyQuest.getAmountNeeded());

                                playerData.getRoyaltyQuests().add(royaltyQuest);
                            }
                        }
                    }

                } else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(Text.color("&6Royalty Shop"))) {
                    playerData.getPlayerActionManager().openRoyaltyShop();
                }
            }
        } else if (e.getView().getTitle().equals(Text.color("&6♛ &7Royalty Shop &6♛"))) {
            e.setCancelled(true);
            if (e.getCurrentItem() != null) {
                net.minecraft.server.v1_16_R3.ItemStack stack = CraftItemStack.asNMSCopy(e.getCurrentItem());
                NBTTagCompound nbtTagCompound = stack.getTag();
                if (nbtTagCompound != null) {
                    if (nbtTagCompound.hasKey("price")) {
                        RoyaltyUpgrades royaltyUpgrades = null;
                        switch (ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName())) {
                            case "Increased Security":
                                royaltyUpgrades = RoyaltyUpgrades.INCREASED_SECURITY;
                                break;
                            case "Long Live The King":
                                royaltyUpgrades = RoyaltyUpgrades.LONG_LIVE_THE_KING;
                                break;
                            case "Price Reduction":
                                royaltyUpgrades = RoyaltyUpgrades.REDUCE_PRICE;
                                break;
                            case "Allowance":
                                royaltyUpgrades = RoyaltyUpgrades.ALLOWANCE;
                                break;
                        }
                        if (royaltyUpgrades != null) {
                            int price = nbtTagCompound.getInt("price");
                            if (playerData.getRoyaltyPoints() < price) {
                                Text.applyText(p, "&cYou need more Royalty Points to buy this!");
                                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 0.2f);
                                return;
                            }

                            if (!playerData.getRoyaltyUpgrades().containsKey(royaltyUpgrades)) {
                                playerData.getRoyaltyUpgrades().put(royaltyUpgrades, 1);
                            } else {
                                if (playerData.getRoyaltyUpgrades().get(royaltyUpgrades) == royaltyUpgrades.getMaxTier()) {
                                    Text.applyText(p, "&cYou already have the maximum level for this upgrade!");
                                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 0.2f);
                                    return;
                                }
                                playerData.getRoyaltyUpgrades().put(royaltyUpgrades, playerData.getRoyaltyUpgrades().get(royaltyUpgrades) + 1);
                            }
                            playerData.setRoyaltyPoints(playerData.getRoyaltyPoints() - price);
                            Text.applyText(p, "&7You purchased &6" + royaltyUpgrades.getRoyaltyUpgradeName() + " " + playerData.getRoyaltyUpgrades().get(royaltyUpgrades) + " &7for &6" + price + " Royalty Points&7.");
                            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 0.2f);
                            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_YES, 1.0f, 0.2f);
                        }
                    }
                }
            }
        }
        if (e.getInventory().getType() == InventoryType.PLAYER) {
            playerData.getPlayerActionManager().generateInventoryData();
            playerData.getPlayerInventory().updateInventory(p);
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
                    if (foods.getOriginalBase() != null) {
                        if (foods.isProcessed() == itemInfoItem.isProcessed() || foods.isCooked() == itemInfoItem.isCooked()) {
                            food = foods;
                        }
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
                ItemInfo itemInfo = ItemInfo.parseItemInfo(inv.getItem(integer));
                if (itemInfo != null) {
                        if (inv.getItem(integer).getAmount() == 1) {
                            craftingItems.put(slot, new CraftingIngrediant(itemInfo.getItem(),
                                    1));

                        } else {
                            craftingItems.put(slot, new CraftingIngrediant(itemInfo.getItem(),
                                    inv.getItem(integer).getAmount()));

                        }
                }
            }  else {
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
            playerData.getPlayerActionManager().generateInventoryData();
        } else {
            if (inv.getItem(25) != null) {
                inv.setItem(25, null);
                playerData.getPlayerActionManager().generateInventoryData();

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
        Player player = (Player) e.getPlayer();
        PlayerData playerData = skrpg.getPlayerManager().getPlayerData(player.getUniqueId());
        playerData.getPlayerActionManager().generateInventoryData();
        playerData.getPlayerInventory().updateInventory(player);
        if (e.getInventory().getType().equals(InventoryType.WORKBENCH)) {
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
        Text.applyText(p, "&cYou cannot use your own crafting inventory in SKQuest! &cUse /ct instead.");
        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
    }
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerRod(PlayerFishEvent e) {
        Player p = e.getPlayer();
        PlayerData playerData = skrpg.getPlayerManager().getPlayerData(p.getUniqueId());
        if (e.getState() == PlayerFishEvent.State.CAUGHT_FISH) {

            ItemInfo itemInfo = ItemInfo.parseItemInfo(e.getPlayer().getInventory().getItemInMainHand());
            if (itemInfo != null) {
                if (itemInfo.getItem().getItemType() == ItemType.FISHING_ROD) {
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 0.2f);
                    int marineSeaCreatureCatchChance = new Random().nextInt(101);
                    boolean hasFished = false;
                    if (playerData.getMarineLifeCatchChance() >= marineSeaCreatureCatchChance) {

                        List<MobType> marineSeaCreatures = Arrays.asList(MobType.FISH, MobType.FLOPPY_FISH, MobType.SQUID,
                                MobType.WATER_WEAKLING, MobType.WATER_ARCHER, MobType.WATER_WARRIOR, MobType.CRAB_ZOMBIE,
                                MobType.WATERFISH, MobType.WATERFISH, MobType.SEA_HORSE, MobType.SPEEDSTER_OF_THE_SEA, MobType.SEA_LORD);
                        List<MobType> validSeaCreatures = new ArrayList<>();
                        for (MobType marineCreatures : marineSeaCreatures) {
                            if (marineCreatures.getLevel() <= SKRPG.levelToInt(playerData.getFishing().getLevel().toString())) {
                                validSeaCreatures.add(marineCreatures);
                            }
                        }
                        if (!validSeaCreatures.isEmpty()) {
                            int randomCreature = new Random().nextInt(validSeaCreatures.size());
                            MobType marineCreature = validSeaCreatures.get(randomCreature);
                            Mob marineCreatureMob = MobType.buildMob(marineCreature.getId(), skrpg, e.getHook().getLocation());
                            marineCreatureMob.getEnt().setVelocity(e.getCaught().getVelocity());
                            marineCreatureMob.getEnt().setVelocity(marineCreatureMob.getEnt().getLocation().getDirection());
                            playerData.getFishing().setXpTillNext(playerData.getFishing().getXpTillNext() + 10 * marineCreature.getLevel());
                            playerData.getFishing().setTotalXP(playerData.getFishing().getTotalXP() + 10 * marineCreature.getLevel());
                            playerData.getFishing().levelUpSkill(p, playerData, skrpg);
                            Text.applyText(p, "&3You caught a " + marineCreature.getName() + "&3!");
                            hasFished = true;
                        }
                    }
                    double randomDoubleTreasure = Math.random();

                    if (randomDoubleTreasure >= (SKRPG.levelToInt(playerData.getFishing().getLevel().toString()) * 0.5) + 2 && !hasFished) {

                        List<Drop> treasure = Arrays.asList(Drop.GOLD_FISHING_DROP, Drop.DIAMOND_FISHING_DROP, Drop.GEMSTONE_FISHING_DROP, Drop.CRAB_FRAGMENT_FISHING, Drop.EXPERT_ROD);
                        int randomTreasureXp = new Random().nextInt(11);
                        double randomDouble = Math.random();
                        for (Drop drop : treasure) {
                            if (randomDouble <= drop.getChance()) {
                                Text.applyText(p, "&6You found a " + drop.getItems().getRarity().getColor() + drop.getItems().getName() + "&6!");
                                hasFished = true;
                                playerData.getPlayerActionManager().addExistingItem(new SKRPGItemStack(drop.getItems().generateItemInfo(), 1));
                                if (drop.getDropRarity() != DropRarity.NORMAL) {
                                    if (drop.getDropRarity().getPriority() < 3) {
                                        p.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0f, 0.2f);
                                    } else {
                                        p.playSound(p.getLocation(), Sound.ITEM_TOTEM_USE, 1.0f, 2.0f);
                                    }
                                    Text.applyText(p, drop.getDropRarity().getName() + " DROP! &r&8| &r&7You dropped a " + drop.getItems().getRarity().getColor() + drop.getItems().getName());
                                }
                            } else {
                                randomDouble = randomDouble + drop.getChance();
                            }
                            playerData.getFishing().setXpTillNext(playerData.getFishing().getXpTillNext() + 100 * randomTreasureXp);
                            playerData.getFishing().setTotalXP(playerData.getFishing().getTotalXP() + 100 * randomTreasureXp);
                            playerData.getFishing().levelUpSkill(p, playerData, skrpg);
                        }

                    }
                    if (!hasFished) {
                        List<Items> items = Arrays.asList(Items.COD, Items.SALMON, Items.PUFFERFISH, Items.LILY_PAD,
                                Items.SEASHELL, Items.SEAGRASS, Items.INK_SAC);
                        int randomItem = new Random().nextInt(items.size());
                        Items item = items.get(randomItem);
                        Text.applyText(p, "&aYou fished up a " + item.getRarity().getColor() + item.getName() + "&a!");
                        playerData.getPlayerActionManager().addItem(new SKRPGItemStack(item.generateItemInfo(), 1));
                    }

                }
            }
            e.getCaught().remove();
        }
    }
    @EventHandler
    public void onChatEvent(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        if (skrpg.getRegionSetup(p) != null) {
            if (e.getMessage().equals("okay") && skrpg.getRegionSetup(p).getStage() != 6 && skrpg.getRegionSetup(p).getStage() != 7) {
                skrpg.getRegionSetup(p).nextStage(null);
            } else if (skrpg.getRegionSetup(p).getStage() == 6 || skrpg.getRegionSetup(p).getStage() == 7) {
                skrpg.getRegionSetup(p).nextStage(e.getMessage());
            }
        }
        if (skrpg.getPortalSetup(p) != null) {
            if (e.getMessage().equals("okay")) {
                skrpg.getPortalSetup(p).nextStage();
            }
        }
    }
    @EventHandler
    public void leafDecayEvent(LeavesDecayEvent e) {
        e.setCancelled(true);
    }
    @EventHandler
    public void onDropEvent(PlayerDropItemEvent e) {
        PlayerData playerData = skrpg.getPlayerManager().getPlayerData(e.getPlayer().getUniqueId());
        if (!playerData.canDrop()) {
            e.setCancelled(true);
        }
    }
}
