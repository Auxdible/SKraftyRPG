package com.auxdible.skrpg.player.economy;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.items.Items;
import com.auxdible.skrpg.items.SKRPGItemStack;
import com.auxdible.skrpg.player.PlayerData;
import com.auxdible.skrpg.utils.ItemBuilder;
import com.auxdible.skrpg.utils.Text;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Trade {
    private Player player1;
    private Player player2;
    private PlayerData playerData1;
    private PlayerData playerData2;
    private ArrayList<SKRPGItemStack> player1Offer;
    private ArrayList<SKRPGItemStack> player2Offer;
    private Inventory tradeInv1;
    private Inventory tradeInv2;
    private double player1CreditsOffer;
    private double player2CreditsOffer;
    private boolean player1Accepted;
    private boolean player2Accepted;
    public Trade(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.player1Accepted = false;
        this.player2Accepted = false;
        this.playerData1 = null;
        this.playerData2 = null;
        this.player1Offer = new ArrayList<>();
        this.player2Offer = new ArrayList<>();
        this.player1CreditsOffer = 0;
        this.player2CreditsOffer = 0;
    }
    public Player getPlayer1() { return player1; }
    public Player getPlayer2() { return player2; }
    public void setPlayer2(Player player2) { this.player2 = player2; }
    public void addCredits(Player player, double creditsOffered) {
        if (creditsOffered == 0) {
            return;
        }
        if (player == player1) {
            player1CreditsOffer = player1CreditsOffer + creditsOffered;
            tradeInv1.setItem(4, new ItemBuilder(Material.GOLD_NUGGET, 0).setName("&7Your Offer: &6" + player1CreditsOffer).asItem());
            tradeInv2.setItem(22, new ItemBuilder(Material.GOLD_NUGGET, 0).setName("&7" +
                    player1.getDisplayName() + "'s Offer: &6" + player1CreditsOffer).asItem());
            Text.applyText(player, "&aAdded &6" + creditsOffered + " Nuggets &ato the trade!");
            Text.applyText(player2, "&a" + player.getDisplayName() + " added &6" + creditsOffered + " Nuggets &ato the trade!");
        } else {
            player2CreditsOffer = player2CreditsOffer + creditsOffered;
            tradeInv2.setItem(4, new ItemBuilder(Material.GOLD_NUGGET, 0).setName("&7Your Offer: &6" + player2CreditsOffer).asItem());
            tradeInv1.setItem(22, new ItemBuilder(Material.GOLD_NUGGET, 0).setName("&7" +
                    player2.getDisplayName() + "'s Offer: &6" + player2CreditsOffer).asItem());
            Text.applyText(player, "&aAdded &6" + creditsOffered + " Nuggets &ato the trade!");
            Text.applyText(player1, "&a" + player.getDisplayName() + " added &6" + creditsOffered + " Nuggets &ato the trade!");
        }
    }
    public Inventory getInv(Player p) {
        if (p == player1) {
            return tradeInv1;
        } else {
            return tradeInv2;
        }
    }
    public PlayerData getPlayerData1() { return playerData1; }
    public Inventory getTradeInv1() { return tradeInv1; }
    public Inventory getTradeInv2() { return tradeInv2; }
    public void start(SKRPG skrpg) {
        playerData1 = skrpg.getPlayerManager().getPlayerData(player1.getUniqueId());
        playerData2 = skrpg.getPlayerManager().getPlayerData(player2.getUniqueId());
        if (!player1.isOnline() || !player2.isOnline()) {
            player1.closeInventory();
            player2.closeInventory();
            playerData1.setTrade(null);
            playerData2.setTrade(null);
            return;
        }
        tradeInv1 = Bukkit.createInventory(null, 27, "Trade");
        tradeInv2 = Bukkit.createInventory(null, 27, "Trade");
        List<Integer> outline = Arrays.asList(9, 4, 13, 22, 8, 17, 26);
        for (int i : outline) {
            tradeInv1.setItem(i, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE, 0).setName(" ").asItem());
            tradeInv2.setItem(i, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE, 0).setName(" ").asItem());
        }
        tradeInv1.setItem(0, new ItemBuilder(Material.RED_CONCRETE, 0).setName("&c&lDENY").asItem());
        tradeInv1.setItem(18, new ItemBuilder(Material.LIME_CONCRETE, 0).setName("&a&lACCEPT").asItem());
        tradeInv1.setItem(9, new ItemBuilder(Material.GOLD_NUGGET, 0).setName("&6Pay Nuggets").asItem());
        tradeInv2.setItem(0, new ItemBuilder(Material.RED_CONCRETE, 0).setName("&c&lDENY").asItem());
        tradeInv2.setItem(18, new ItemBuilder(Material.LIME_CONCRETE, 0).setName("&a&lACCEPT").asItem());
        tradeInv2.setItem(9, new ItemBuilder(Material.GOLD_NUGGET, 0).setName("&6Pay Nuggets").asItem());
        player1.openInventory(tradeInv1);
        player2.openInventory(tradeInv2);
    }
    public void addItem(Player player, SKRPGItemStack SKRPGItemStack) {
        List<Integer> tradeSlots1 = Arrays.asList(1, 2, 3, 10, 11, 12, 19, 20, 21);
        List<Integer> tradeSlots2 = Arrays.asList(5, 6, 7, 14, 15, 16, 23, 24, 25);
        Items item = SKRPGItemStack.getItemInfo().getItem();
        if (player1.getUniqueId() == player.getUniqueId()) {
            player1Offer.add(SKRPGItemStack);
            for (int i : tradeSlots1) {
                if (tradeInv1.getItem(i) == null) {
                    ItemStack itemStack = Items.buildItem(item);
                    itemStack.setAmount(SKRPGItemStack.getAmount());
                    tradeInv1.setItem(i, itemStack);
                    break;
                }
            }
            for (int i : tradeSlots2) {
                if (tradeInv2.getItem(i) == null) {
                    ItemStack itemStack = Items.buildItem(item);
                    itemStack.setAmount(SKRPGItemStack.getAmount());
                    tradeInv2.setItem(i, itemStack);
                    break;
                }
            }
        } else {
            player2Offer.add(SKRPGItemStack);
            for (int i : tradeSlots1) {
                if (tradeInv2.getItem(i) == null) {
                    ItemStack itemStack = Items.buildItem(item);
                    itemStack.setAmount(SKRPGItemStack.getAmount());
                    tradeInv2.setItem(i, itemStack);
                    break;
                }
            }
            for (int i : tradeSlots2) {
                if (tradeInv1.getItem(i) == null) {
                    ItemStack itemStack = Items.buildItem(item);
                    itemStack.setAmount(SKRPGItemStack.getAmount());
                    tradeInv1.setItem(i, itemStack);
                    break;
                }
            }
        }
    }
    public void deny() {
        for (SKRPGItemStack SKRPGItemStack : player1Offer) {

            ItemStack itemStack = Items.buildItem(SKRPGItemStack.getItemInfo().getItem());
            Items.updateItem(itemStack, SKRPGItemStack.getItemInfo());
            itemStack.setAmount(SKRPGItemStack.getAmount());
            player1.getInventory().addItem(itemStack);
        }
        for (SKRPGItemStack SKRPGItemStack : player2Offer) {
            ItemStack itemStack = Items.buildItem(SKRPGItemStack.getItemInfo().getItem());
            Items.updateItem(itemStack, SKRPGItemStack.getItemInfo());
            itemStack.setAmount(SKRPGItemStack.getAmount());
            player2.getInventory().addItem(itemStack);
        }
        Text.applyText(player1, "&cTrade cancelled!");
        Text.applyText(player2, "&cTrade cancelled!");
        player1.playSound(player1.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
        player2.playSound(player2.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
        playerData1.setTrade(null);
        playerData2.setTrade(null);
        player1.closeInventory();
        player2.closeInventory();
    }
    public void accept(Player player) {
        if (player.getUniqueId() == player1.getUniqueId()) {
            player1.playSound(player1.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
            player2.playSound(player2.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
            Text.applyText(player1, "&aAccepted the trade!");
            Text.applyText(player2, "&a" + player1.getDisplayName() + " accepted the trade!");
            player1Accepted = true;
        } else {
            player1.playSound(player1.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
            player2.playSound(player2.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
            Text.applyText(player2, "&aAccepted the trade!");
            Text.applyText(player1, "&a" + player2.getDisplayName() + " accepted the trade!");
            player2Accepted = true;
        }
        if (player1Accepted && player2Accepted) {
            for (SKRPGItemStack SKRPGItemStack : player1Offer) {
                ItemStack itemStack = Items.buildItem(SKRPGItemStack.getItemInfo().getItem());
                Items.updateItem(itemStack, SKRPGItemStack.getItemInfo());
                itemStack.setAmount(SKRPGItemStack.getAmount());
                player2.getInventory().addItem(itemStack);
            }
            for (SKRPGItemStack SKRPGItemStack : player2Offer) {
                ItemStack itemStack = Items.buildItem(SKRPGItemStack.getItemInfo().getItem());
                Items.updateItem(itemStack, SKRPGItemStack.getItemInfo());
                itemStack.setAmount(SKRPGItemStack.getAmount());
                player1.getInventory().addItem(itemStack);
            }
            Text.applyText(player1, "&aTrade &e" + player1.getDisplayName() + " &e☛ " +
                      " ☚ " + player2.getDisplayName() + " &aCOMPLETE!");
            Text.applyText(player2, "&aTrade &e" + player2.getDisplayName() + " &e☛ " +
                    " ☚ " + player1.getDisplayName() + " &aCOMPLETE!");
            for (SKRPGItemStack SKRPGItemStack : player1Offer) {
                Items item = SKRPGItemStack.getItemInfo().getItem();
                Text.applyText(player1, "&7x" + SKRPGItemStack.getAmount() + " &c&l- &r" +
                        item.getRarity().getColor() + item.getName());
                Text.applyText(player2, "&7x" + SKRPGItemStack.getAmount() + " &a&l+ &r" +
                        item.getRarity().getColor() + item.getName());
            }
            for (SKRPGItemStack SKRPGItemStack : player2Offer) {
                Items item = SKRPGItemStack.getItemInfo().getItem();
                Text.applyText(player2, "&7x" + SKRPGItemStack.getAmount() + " &c&l- &r" +
                        item.getRarity().getColor() + item.getName());
                Text.applyText(player1, "&7x" + SKRPGItemStack.getAmount() + " &a&l+ &r" +
                        item.getRarity().getColor() + item.getName());
            }
            if (player1CreditsOffer != 0) {
                playerData1.setCredits(playerData1.getCredits() - player1CreditsOffer);
                playerData2.setCredits(playerData2.getCredits() + player1CreditsOffer);
                Text.applyText(player1, "&c&l- &6" +
                        player1CreditsOffer + " Nuggets");
                Text.applyText(player2, "&a&l+ &6" +
                        player1CreditsOffer + " Nuggets");
            }
            if (player2CreditsOffer != 0) {
                playerData1.setCredits(playerData1.getCredits() + player2CreditsOffer);
                playerData2.setCredits(playerData2.getCredits() - player2CreditsOffer);
                Text.applyText(player2, "&c&l- &6" +
                        player2CreditsOffer + " Nuggets");
                Text.applyText(player1, "&a&l+ &6" +
                        player2CreditsOffer + " Nuggets");
            }
            player1.playSound(player1.getLocation(), Sound.ENTITY_VILLAGER_CELEBRATE, 1.0f, 0.5f);
            player2.playSound(player2.getLocation(), Sound.ENTITY_VILLAGER_CELEBRATE, 1.0f, 0.5f);
            playerData1.setTrade(null);
            playerData2.setTrade(null);
            player1.closeInventory();
            player2.closeInventory();
        }
    }
}
