package com.auxdible.skrpg.player.actions;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.items.ItemInfo;
import com.auxdible.skrpg.items.ItemType;
import com.auxdible.skrpg.items.Items;
import com.auxdible.skrpg.items.Quality;
import com.auxdible.skrpg.player.PlayerData;
import com.auxdible.skrpg.player.collections.Collection;
import com.auxdible.skrpg.player.economy.TradeItem;
import com.auxdible.skrpg.player.quests.Quests;
import com.auxdible.skrpg.player.skills.*;
import com.auxdible.skrpg.utils.ItemBuilder;
import com.auxdible.skrpg.utils.ItemTweaker;
import com.auxdible.skrpg.utils.Text;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

public class PlayerAction {
    private Player player;
    private PlayerData playerData;
    private SKRPG skrpg;
    public PlayerAction(Player player, PlayerData playerData, SKRPG skrpg) {
        this.player = player;
        this.playerData = playerData;
        this.skrpg = skrpg;
    }
    // ---- PLAYER DAMAGE ----
    public void damagePlayer(int damage) {
            Player player = Bukkit.getPlayer(playerData.getUuid());
            if (skrpg.getRaidManager().isInRaid(player)) {
                Text.applyText(player, "&cYou deflected a hit because you are in a raid!");
                player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 1.0f);
                return;
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
            if (playerData.getHp() <= 0) {
                this.killPlayer();
            }
            ArmorStand damageIndictator = (ArmorStand) player.getWorld()
                    .spawnEntity(player.getLocation(), EntityType.ARMOR_STAND);
            damageIndictator.setInvulnerable(true);
            damageIndictator.setCollidable(false);
            damageIndictator.setInvisible(true);
            damageIndictator.setCustomName(Text.color("&c" + nerfedDamage + " &4&l☄"));
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
    }

    public void killPlayer() {
        if (playerData.getActiveQuest() == Quests.ABANDONED_MINES) {
            player.teleport(skrpg.getLocationManager().getAbandonedMinesLocation());
        } else if (skrpg.getPlayerManager().getPlayerData(player.getUniqueId()).getRegion() != null) {
            player.teleport(skrpg.getPlayerManager().getPlayerData(player.getUniqueId()).getRegion().getSpawnLocation());
        } else {
            player.teleport(Bukkit.getWorld(skrpg.getConfig().getString("rpgWorld")).getSpawnLocation());
        }

        player.playSound(player.getLocation(), Sound.ENTITY_BLAZE_DEATH, 1.0f, 0.5f);
        player.sendTitle(Text.color("&4&l☠"), Text.color("&c&lYOU DIED"), 40, 100, 10);
        Text.applyText(player, "&cYou lost &6" + skrpg.getPlayerManager().getPlayerData(player.getUniqueId()).getCredits() / 2 + " Nuggets&c!");
        skrpg.getPlayerManager().getPlayerData(player.getUniqueId()).setCredits(skrpg.getPlayerManager().getPlayerData(player.getUniqueId()).getCredits() / 2);

        skrpg.getPlayerManager().getPlayerData(player.getUniqueId()).setHp(
                skrpg.getPlayerManager().getPlayerData(player.getUniqueId()).getMaxHP());
    }
    // ---- PLAYER DAMAGE BY PLAYER ----
    public void damageByPlayer(Player damager, int damage) {
        Player player = Bukkit.getPlayer(playerData.getUuid());
        if (player == null) { return; }
        if (skrpg.getRaidManager().isInRaid(player)) {
            Text.applyText(player, "&cYou deflected a hit from a player! (" + damager.getDisplayName() + ") because you are in a raid!");
            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 1.0f);
            return;
        }
        double damageReduction = (skrpg.getPlayerManager().getPlayerData(player.getUniqueId()).getDefence() /
                (2000.0 + skrpg.getPlayerManager().getPlayerData(player.getUniqueId()).getDefence()));
        int nerfedDamage = (int) Math.round(damage - (damage *
                damageReduction));

        if (skrpg.getPlayerManager().getPlayerData(player.getUniqueId()).getHp() - nerfedDamage < 0) {
            skrpg.getPlayerManager().getPlayerData(player.getUniqueId()).setHp(0);
        } else {
            skrpg.getPlayerManager().getPlayerData(player.getUniqueId()).setHp(
                    skrpg.getPlayerManager().getPlayerData(player.getUniqueId()).getHp() - nerfedDamage);
        }
        Text.applyText(player, "&c" + damager.getDisplayName() + " hit you for " + nerfedDamage + "♥!");
        Text.applyText(damager, "&cHit " + player.getDisplayName() + " for " + nerfedDamage + "♥!");
        if (playerData.getDefence() > 250 && playerData.getHp() > 100) {
            damager.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 0.5f, 0.4f);
        }
        if (playerData.getHp() <= 0) {
            killByPlayer(damager);
        }
        ArmorStand damageIndictator = (ArmorStand) player.getWorld()
                .spawnEntity(player.getLocation(), EntityType.ARMOR_STAND);
        damageIndictator.setInvulnerable(true);
        damageIndictator.setCollidable(false);
        damageIndictator.setInvisible(true);
        damageIndictator.setCustomName(Text.color("&c" + nerfedDamage + " &4&l☄"));
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


    }
    public void killByPlayer(Player killer) {
        Player player = Bukkit.getPlayer(playerData.getUuid());
        if (skrpg.getPlayerManager().getPlayerData(player.getUniqueId()).getRegion() != null) {
            player.teleport(skrpg.getPlayerManager().getPlayerData(player.getUniqueId()).getRegion().getSpawnLocation());
        } else {
            player.teleport(Bukkit.getWorld(skrpg.getConfig().getString("rpgWorld")).getSpawnLocation());
        }
        player.playSound(player.getLocation(), Sound.ENTITY_BLAZE_DEATH, 1.0f, 0.5f);
        player.sendTitle(Text.color("&4&l☠"), Text.color("&c&lYOU DIED"), 40, 100, 10);
        Text.applyText(player, "&cYou were killed by " + killer.getDisplayName() + "&c!");

        skrpg.getPlayerManager().getPlayerData(player.getUniqueId()).setHp(
                skrpg.getPlayerManager().getPlayerData(player.getUniqueId()).getMaxHP());
    }
    // ---- PLAYER BREAK BLOCK ----
    public void onBreakBlock(Block block) {
        Player p = player;
        if (p.getGameMode().equals(GameMode.CREATIVE)) { return; }
        BlockInformation blockData = BlockInformation.getBlockData(block.getType());
        if (blockData == null) { return; }
        List<Integer> bounds = new ArrayList<>();
        for (int i = blockData.getLowerBoundDropAmount(); i <= blockData.getUpperBoundDropAmount(); i++) {
            bounds.add(i);
        }
        playerData.addRunicPoints(1, skrpg);
        Random randomizer = new Random();

        int randomInt = randomizer.nextInt(bounds.size() + 1);
        for (Collection collection : playerData.getCollections()) {
            if (collection.getCollectionType().getItem().equals(blockData.getCommonDrop())) {
                collection.setCollectionAmount(collection.getCollectionAmount() + randomInt);
                collection.levelUpCollection(p, playerData);
            }
        }
        addItem(new TradeItem(blockData.getCommonDrop().generateItemInfo(), randomInt));
        double randomDouble = Math.random();
        double randomDouble2 = Math.random();
        if (blockData.getDrops() != null) {
            for (Drop drop : blockData.getDrops()) {
                if (drop.getChance() >= randomDouble) {
                    addItem(new TradeItem(drop.getItems().generateItemInfo(), 1));
                    if (drop.getDropRarity() != DropRarity.NORMAL) {
                        if (drop.getDropRarity().getPriority() < 3) {
                            p.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0f, 0.2f);
                        } else {
                            p.playSound(p.getLocation(), Sound.ITEM_TOTEM_USE, 1.0f, 2.0f);
                        }
                        Text.applyText(p, drop.getDropRarity().getName() + " DROP! &r&8| &r&7You dropped a " + drop.getItems().getRarity().getColor() + drop.getItems().getName());
                    }
                }
            }
        }
        if (blockData.getXpObtained().get(1) != 0.0) {
            if (Integer.parseInt(playerData.getMining().getLevel().toString()
                    .replace("_", "")) * 0.04 >= randomDouble2) {
                addItem(new TradeItem(blockData.getCommonDrop().generateItemInfo(), randomInt));
                playerData.addRunicPoints(1, skrpg);
            }
            playerData.getMining().setXpTillNext(playerData.getMining().getXpTillNext() +
                    blockData.getXpObtained().get(1));
            playerData.getMining().setTotalXP(playerData.getMining().getTotalXP()
                    + blockData.getXpObtained().get(1));
            playerData.getMining().levelUpSkill(p, playerData, skrpg);
            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                    Text.color("&e+ " + blockData.getXpObtained().get(1) + " Mining XP (" + playerData.getMining()
                            .getXpTillNext() + "/" + Level.valueOf("_" +
                            (Integer.parseInt(playerData.getMining().getLevel().toString()
                                    .replace("_", "")) + 1)).getXpRequired() + ")")));
            p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 2.0f);
        } else if (blockData.getXpObtained().get(2) != 0.0) {
            if (Integer.parseInt(playerData.getHerbalism().getLevel().toString()
                    .replace("_", "")) * 0.04 >= randomDouble2) {
                addItem(new TradeItem(blockData.getCommonDrop().generateItemInfo(), randomInt));
                playerData.addRunicPoints(1, skrpg);
            }
            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                    Text.color("&e+ " + blockData.getXpObtained().get(2) + " Herbalism XP (" + playerData.getHerbalism()
                            .getXpTillNext() + "/" + Level.valueOf("_" +
                            (Integer.parseInt(playerData.getHerbalism().getLevel().toString()
                                    .replace("_", "")) + 1)).getXpRequired() + ")")));
            playerData.getHerbalism().setXpTillNext(playerData.getHerbalism().getXpTillNext() +
                    blockData.getXpObtained().get(2));
            playerData.getHerbalism().setTotalXP(playerData.getHerbalism().getTotalXP()
                    + blockData.getXpObtained().get(2));
            playerData.getHerbalism().levelUpSkill(p, playerData, skrpg);
        } else if (blockData.getXpObtained().get(3) != 0.0) {
            if (Integer.parseInt(playerData.getCrafting().getLevel().toString()
                    .replace("_", "")) * 0.04 >= randomDouble2) {
                addItem(new TradeItem(blockData.getCommonDrop().generateItemInfo(), randomInt));
                playerData.addRunicPoints(1, skrpg);
            }
            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                    Text.color("&e+ " + blockData.getXpObtained().get(3) + " Crafting XP (" + playerData.getCrafting()
                            .getXpTillNext() + "/" + Level.valueOf("_" +
                            (Integer.parseInt(playerData.getCrafting().getLevel().toString()
                                    .replace("_", "")) + 1)).getXpRequired() + ")")));
            playerData.getCrafting().setXpTillNext(playerData.getCrafting().getXpTillNext() +
                    blockData.getXpObtained().get(3));
            playerData.getCrafting().setTotalXP(playerData.getCrafting().getTotalXP()
                    + blockData.getXpObtained().get(3));
            playerData.getCrafting().levelUpSkill(p, playerData, skrpg);
        }
        boolean air = !blockData.isStone();

        BrokenBlock brokenBlock = new BrokenBlock(skrpg, blockData, block.getBlockData(), block.getLocation(), false, false, blockData.isStone(), air);
        if (blockData.equals(BlockInformation.CANE)) {
            Location originLocation = block.getLocation();
            int blockBroken = block.getY();
            int roof = 0;
            int ground = 0;
            for (int i = 0; i <= blockBroken; i++) {
                if (new Location(originLocation.getWorld(), originLocation.getX(), blockBroken - i, originLocation.getZ()).getBlock().getType() != Material.SUGAR_CANE &&
                        new Location(originLocation.getWorld(), originLocation.getX(), blockBroken - i, originLocation.getZ()).getBlock().getType() != Material.AIR) {
                    ground = blockBroken - i + 1;

                    BrokenBlock brokenBlockStem = new BrokenBlock(skrpg, blockData, block.getBlockData(), block.getLocation(), true, false, false, false);
                    break;
                }
            }
            for (int i = 0; i <= blockBroken; i++) {
                if (new Location(originLocation.getWorld(), originLocation.getX(), blockBroken + i, originLocation.getZ()).getBlock().getType() != Material.SUGAR_CANE) {
                    roof = blockBroken + i - 1;
                    break;
                }
            }
            for (int i = blockBroken + 1; i <= roof; i++) {
                new Location(originLocation.getWorld(), originLocation.getX(), i, originLocation.getZ()).getBlock().setBlockData(Material.AIR.createBlockData());
                if (Integer.parseInt(playerData.getHerbalism().getLevel().toString()
                        .replace("_", "")) * 0.04 >= randomDouble2) {
                    addItem(new TradeItem(blockData.getCommonDrop().generateItemInfo(), randomInt));
                    playerData.addRunicPoints(1, skrpg);
                }
                p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                        Text.color("&e+ " + blockData.getXpObtained().get(2) + " Herbalism XP (" + playerData.getHerbalism()
                                .getXpTillNext() + "/" + Level.valueOf("_" +
                                (Integer.parseInt(playerData.getHerbalism().getLevel().toString()
                                        .replace("_", "")) + 1)).getXpRequired() + ")")));
                playerData.getHerbalism().setXpTillNext(playerData.getHerbalism().getXpTillNext() +
                        blockData.getXpObtained().get(2));
                playerData.getHerbalism().setTotalXP(playerData.getHerbalism().getTotalXP()
                        + blockData.getXpObtained().get(2));
                playerData.getHerbalism().levelUpSkill(p, playerData, skrpg);
                BrokenBlock brokenBlockCanePart = new BrokenBlock(skrpg, blockData, block.getBlockData(), block.getLocation(), false, false, false, true);

            }
        }
    }
    public void openStovetop() {
        Inventory inv = Bukkit.createInventory(null, 27, "Stovetop");
        for (int i = 0; i <= 26; i++) {
            inv.setItem(i, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE, 0).setName(" ").asItem());
        }
        inv.setItem(11, new ItemBuilder(Material.FURNACE, 0).setName("&aSmelt").setLore(Arrays.asList(" ", Text.color("&7Smelt meat and apply them to foods!"), " ", Text.color("&7Smelting meat costs &fCoal&7."), " ")).asItem());
        inv.setItem(13, new ItemBuilder(Material.IRON_AXE, 0).setName("&aProcess").setLore(Arrays.asList(" ", Text.color("&7Process crops and apply them to foods!"), " ", Text.color("&7Processing crops costs &fIron&7."), " ")).asItem());
        inv.setItem(15, new ItemBuilder(Material.MUSHROOM_STEW, 0).setName("&aCook Food").setLore(Arrays.asList(" ", Text.color("&7Apply items to food bases to create food."), Text.color("&7Food will give you temporary status effects."),  Text.color("&c&lIf you log off or die, you will lose all active effects."))).asItem());
        player.openInventory(inv);
    }
    public void openBankMenu() {
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
                    .getNameColored()), Text.color("&7Nuggets: &6" + playerData.getBanks().get(i).getCredits())));
            bank.setItemMeta(iM);
            inv.setItem(slots.get(i), bank);
        }
        if (playerData.getBanks().size() != 5) {
            inv.setItem(22, new ItemBuilder(Material.LIME_DYE, 0).setName("&7Buy Bank: &6" + (
                    2500000 * playerData.getBanks().size())).asItem());
        }
        player.openInventory(inv);
    }
    public void addItem(TradeItem tradeItem) {
        ItemInfo itemInfo = tradeItem.getItemInfo();
        if (itemInfo.getItem().getItemType() == ItemType.MATERIAL) {
            double randomChance = Math.random();
            if (randomChance <= 0.005) {
                itemInfo.setQuality(Quality.TOP_QUALITY);
            } else if (randomChance <= 0.01 && randomChance > 0.005) {
                itemInfo.setQuality(Quality.PREMIUM);
            } else if (randomChance <= 0.10 && randomChance > 0.01) {
                itemInfo.setQuality(Quality.STAR3);
            } else if (randomChance <= 0.40 && randomChance > 0.10) {
                itemInfo.setQuality(Quality.STAR2);
            } else if (randomChance <= 1.00 && randomChance > 0.40) {
                itemInfo.setQuality(Quality.STAR1);
            } else {
                itemInfo.setQuality(Quality.STAR1);
            }
        }
        if (player.getInventory().firstEmpty() == -1) {
            playerData.getStash().add(tradeItem);
            Text.applyText(player, "&4&l!!!!!!!!!!!!!!!!!!!!!!!!!!");
            Text.applyText(player, " ");
            Text.applyText(player, "&cAn item was sent to your stash!");
            Text.applyText(player, "&cDo /stash when you don't have a full inventory to pick up the items!");
            Text.applyText(player, " ");
            Text.applyText(player, "&4&l!!!!!!!!!!!!!!!!!!!!!!!!!!");
            new BukkitRunnable() {
                int seconds = 0;
                @Override
                public void run() {
                    if (seconds == 30) {
                        cancel();
                    }
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_FLUTE, 1.0f, 2.0f);
                    seconds++;
                }
            }.runTaskTimer(skrpg, 0, 2);
        } else {
            ItemStack itemStack = Items.buildItem(tradeItem.getItemInfo().getItem());
            Items.updateItem(itemStack, tradeItem.getItemInfo());
            itemStack.setAmount(tradeItem.getAmount());
        }
    }
    public void addExistingItem(TradeItem tradeItem) {
        ItemInfo itemInfo = tradeItem.getItemInfo();

        if (player.getInventory().firstEmpty() == -1) {
            playerData.getStash().add(tradeItem);
            Text.applyText(player, "&4&l!!!!!!!!!!!!!!!!!!!!!!!!!!");
            Text.applyText(player, " ");
            Text.applyText(player, "&cAn item was sent to your stash!");
            Text.applyText(player, "&cDo /stash when you don't have a full inventory to pick up the items!");
            Text.applyText(player, " ");
            Text.applyText(player, "&4&l!!!!!!!!!!!!!!!!!!!!!!!!!!");
            new BukkitRunnable() {
                int seconds = 0;
                @Override
                public void run() {
                    if (seconds == 30) {
                        cancel();
                    }
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_FLUTE, 1.0f, 2.0f);
                    seconds++;
                }
            }.runTaskTimer(skrpg, 0, 2);
        } else {
            ItemStack itemStack = Items.buildItem(tradeItem.getItemInfo().getItem());
            Items.updateItem(itemStack, tradeItem.getItemInfo());
            itemStack.setAmount(tradeItem.getAmount());
        }
    }
    public void buildRunicTable() {
        Inventory inv = Bukkit.createInventory(null, 54, Text.color("&5&k=Auxd &r&8Runic Table &r&5&kible="));
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
        for (int i = 45; i <= 53; i++) {
            inv.setItem(i, new ItemBuilder(Material.PURPLE_STAINED_GLASS_PANE, 0).asItem());
        }
        inv.setItem(19, new ItemBuilder(Material.ENCHANTED_BOOK, 0).setName("&5Enchant Item").setLore(Arrays.asList(
                " ", Text.color("&7Enchant your item with new &5Enchantments&7."),
                Text.color("&7But it comes at a &5cost&7..."), " ")).asItem());
        inv.setItem(22, new ItemBuilder(Material.ANVIL, 0).setName("&5Apply Runic Stone").setLore(
                Arrays.asList(" ", Text.color("&7Apply a &5Runic Stone &7found around the world of SKRPG."),
                        Text.color("&7You will gain &5buffs &7on the item it is applied to."), " ")).asItem());
        inv.setItem(25, new ItemBuilder(Material.DRAGON_BREATH, 0).setName("&5Spend Runic Points").setLore(
                Arrays.asList(" ", Text.color("&7Spend your &5Runic Points &7on &5Permanent Upgrades&7, &7or &5Runic Point Buffs&7."), " ")).asItem());
        inv.setItem(40, new ItemBuilder(Material.NETHERITE_AXE, 0).setName("&5Destroy an Item").setLore(
                Arrays.asList(" ", Text.color("&7Destroy one of your &5items &7to recieve &5Runic Points"),
                        Text.color("&7in return."))
        ).asItem());
        inv.setItem(4, new ItemBuilder(Material.PURPLE_DYE, 0).setName("&7Runic Points: &5" + playerData.getRunicPoints() + " ஐ").asItem());
        player.openInventory(inv);
    }
    public int calculateDamage() {
        ItemInfo helmetInfo = ItemInfo.parseItemInfo(player.getInventory().getHelmet());
        ItemInfo chestplateInfo = ItemInfo.parseItemInfo(player.getInventory().getChestplate());
        ItemInfo leggingsInfo = ItemInfo.parseItemInfo(player.getInventory().getLeggings());
        ItemInfo bootsInfo = ItemInfo.parseItemInfo(player.getInventory().getBoots());
        ItemInfo handInfo = ItemInfo.parseItemInfo(player.getInventory().getItemInMainHand());
        int damage = 1;
        int strength = 1;
        if (helmetInfo != null) {
            damage = damage + helmetInfo.getItem().getDamage() + helmetInfo.getBonusDamage();
        }
        if (chestplateInfo != null) {
            damage = damage + chestplateInfo.getItem().getDamage() + chestplateInfo.getBonusDamage();
        }
        if (leggingsInfo != null) {
            damage = damage + leggingsInfo.getItem().getDamage() + leggingsInfo.getBonusDamage();
        }
        if (bootsInfo != null) {
            damage = damage + bootsInfo.getItem().getDamage() + bootsInfo.getBonusDamage();
        }
        if (handInfo != null) {
            damage = damage + handInfo.getItem().getDamage() + handInfo.getBonusDamage();
        }
        strength = strength + playerData.getStrength();
        damage = (5 + damage + (strength / 5)) *
                (1 + strength / 100);
        return damage;
    }

}
