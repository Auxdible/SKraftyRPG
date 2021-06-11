package com.auxdible.skrpg.player.actions;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.items.*;
import com.auxdible.skrpg.mobs.DamageType;
import com.auxdible.skrpg.player.PlayerData;
import com.auxdible.skrpg.player.collections.Collection;
import com.auxdible.skrpg.items.SKRPGItemStack;
import com.auxdible.skrpg.player.economy.Bank;
import com.auxdible.skrpg.player.quests.Quests;
import com.auxdible.skrpg.player.quests.TutorialQuest;
import com.auxdible.skrpg.player.quests.royaltyquests.RoyaltyQuest;
import com.auxdible.skrpg.player.quests.royaltyquests.RoyaltyQuestDifficulty;
import com.auxdible.skrpg.player.quests.royaltyquests.RoyaltyQuestType;
import com.auxdible.skrpg.player.quests.royaltyquests.RoyaltyUpgrades;
import com.auxdible.skrpg.player.skills.*;
import com.auxdible.skrpg.locations.regions.RegionFlags;
import com.auxdible.skrpg.utils.ItemBuilder;
import com.auxdible.skrpg.utils.ItemTweaker;
import com.auxdible.skrpg.utils.Text;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_16_R3.NBTTagCompound;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.Hash;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.lang.reflect.Array;
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
    public void damagePlayer(int damage, DamageType damageType) {
        double damageReduction = (skrpg.getPlayerManager().getPlayerData(player.getUniqueId()).getDefence() /
                (100.0 + skrpg.getPlayerManager().getPlayerData(player.getUniqueId()).getDefence()));
        int nerfedDamage = (int) Math.round(damage - (damage *
                damageReduction));

        if (damageType == DamageType.NATURAL || damageType == DamageType.DROWNING
                || damageType == DamageType.FIRE || damageType == DamageType.TRUE) {
            nerfedDamage = damage;
        }

            Player player = Bukkit.getPlayer(playerData.getUuid());
            if (skrpg.getRaidManager().isInRaid(player)) {
                Text.applyText(player, "&cYou deflected a hit because you are in a raid!");
                player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 1.0f);
                return;
            }
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
    }

    public void killPlayer() {
        if (playerData.getRoyaltyUpgrades().containsKey(RoyaltyUpgrades.LONG_LIVE_THE_KING)) {
            double randomChance = Math.random();
            double chance = playerData.getRoyaltyUpgrades().get(RoyaltyUpgrades.LONG_LIVE_THE_KING) * 0.01;
            if (chance >= randomChance) {
                player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 2.0f, 0.2f);
                player.playSound(player.getLocation(), Sound.ITEM_TOTEM_USE, 2.0f, 0.2f);
                player.playSound(player.getLocation(), Sound.EVENT_RAID_HORN, 2.0f, 0.2f);
                playerData.setHp(playerData.getMaxHP());
                Text.applyText(player, "&6&LONG LIVE THE KING!");
                player.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, player.getLocation(), 1);
                player.getWorld().spawnParticle(Particle.TOTEM, player.getLocation(), 1);
                return;
            }
        }
        if (playerData.hasQuest(Quests.ABANDONED_MINES)) {
            player.teleport(skrpg.getLocationManager().getAbandonedMinesLocation());
        } else if (skrpg.getPlayerManager().getPlayerData(player.getUniqueId()).getRegion() != null) {
            player.teleport(playerData.getRegion().getSpawnLocation());
        } else {
            player.teleport(Bukkit.getWorld(skrpg.getConfig().getString("rpgWorld")).getSpawnLocation());
        }

        player.playSound(player.getLocation(), Sound.ENTITY_BLAZE_DEATH, 1.0f, 0.5f);
        player.sendTitle(Text.color("&4&l☠"), Text.color("&c&lYOU DIED"), 40, 100, 10);
        double cost = (Math.round(playerData.getCredits() * 100.0) / 100.0) / 2;
        if (playerData.getRoyaltyUpgrades().containsKey(RoyaltyUpgrades.INCREASED_SECURITY)) {
            cost = Math.round((playerData.getCredits() - (playerData.getCredits() * ((0.01 * playerData.getRoyaltyUpgrades().get(RoyaltyUpgrades.INCREASED_SECURITY)))) * 100.0) / 100.0);
        }
        Text.applyText(player, "&cYou lost &6" + cost + " Nuggets&c!");
        playerData.setCredits(playerData.getCredits() - cost);

        playerData.setHp(playerData.getMaxHP());
    }
    // ---- PLAYER DAMAGE BY PLAYER ----
    public void damageByPlayer(Player damager, int damage, DamageType damageType) {
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
        if (playerData.getRoyaltyUpgrades().containsKey(RoyaltyUpgrades.LONG_LIVE_THE_KING)) {
            double randomChance = Math.random();
            double chance = playerData.getRoyaltyUpgrades().get(RoyaltyUpgrades.LONG_LIVE_THE_KING) * 0.01;
            if (chance >= randomChance) {
                player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 2.0f, 0.2f);
                player.playSound(player.getLocation(), Sound.ITEM_TOTEM_USE, 2.0f, 0.2f);
                player.playSound(player.getLocation(), Sound.EVENT_RAID_HORN, 2.0f, 0.2f);
                playerData.setHp(playerData.getMaxHP() / 2);
                Text.applyText(player, "&6&LONG LIVE THE KING!");
                player.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, player.getLocation(), 1);
                player.getWorld().spawnParticle(Particle.TOTEM, player.getLocation(), 1);
                return;
            }
        }
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
        if (playerData.getRegion() != null && playerData.getRegion().getRegionFlagsList().contains(RegionFlags.DECORATION_REGION)) {
            p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0f, 0.2f);
            return;
        }
        BlockInformation blockData = BlockInformation.getBlockData(block.getType());
        if (blockData == null) { return; }
        ItemInfo itemInfo = null;
        if (playerData.getPlayerInventory().getItemInMainHand() != null) {
             itemInfo = playerData.getPlayerInventory().getItemInMainHand().getItemInfo();
        }
        if (blockData.getToolType() != null && itemInfo == null) {
            p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0f, 0.2f);
            return;
        }
        if (blockData.getAxePriority() != -1 && itemInfo == null) {
            p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0f, 0.2f);
            return;
        }
        if (itemInfo != null) {
            Tools tools = Tools.getTool(itemInfo.getItem());
            if (tools != null && blockData.getAxePriority() > tools.getPriority()) {
                p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0f, 0.2f);
                return;
            }
        }
        p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 2.0f);
        List<Integer> bounds = new ArrayList<>();
        for (int i = blockData.getLowerBoundDropAmount(); i <= blockData.getUpperBoundDropAmount(); i++) {
            bounds.add(i);
        }
        playerData.addRunicPoints(1, skrpg);
        Random randomizer = new Random();

        int randomInt = randomizer.nextInt(bounds.size() + 1);


        if (randomInt != 0) {
            randomInt--;
        }
        for (Collection collection : playerData.getCollections()) {
            if (collection.getCollectionType().getItem().equals(blockData.getCommonDrop())) {
                collection.setCollectionAmount(collection.getCollectionAmount() + bounds.get(randomInt));
                collection.levelUpCollection(p, playerData);
            }
        }
        if (playerData.hasQuest(Quests.TUTORIAL)) {
            TutorialQuest tutorialQuest = (TutorialQuest) playerData.getQuest(Quests.TUTORIAL);
            if (blockData.getCommonDrop() == Items.WOOD) {
                if (tutorialQuest.getWoodCollected() < 5) {
                    tutorialQuest.setWoodCollected(tutorialQuest.getWoodCollected() + 1);
                }
            } else if (blockData.getCommonDrop() == Items.STONE && !tutorialQuest.getCrystalsGathered().contains("CRAFTING")) {
                Text.applyText(player, "&6&lCrafter's Workbench &r&8| &r&7You are a wise and powerful crafter. Go forth.");
                 tutorialQuest.obtainCrystal("CRAFTING");
            }
        }
        for (int i = 0; i < playerData.getRoyaltyQuests().size(); i++) {
            RoyaltyQuest royaltyQuest = playerData.getRoyaltyQuests().get(i);
            if (royaltyQuest.getRoyaltyQuestType().equals(RoyaltyQuestType.LUMBERJACK)
                    && blockData.getCommonDrop().equals(Items.WOOD)) {
                royaltyQuest.progress(bounds.get(randomInt), player, skrpg);
            } else if (royaltyQuest.getRoyaltyQuestType().equals(RoyaltyQuestType.FARMER)
                    && blockData.getCommonDrop().equals(Items.WHEAT)) {
                royaltyQuest.progress(bounds.get(randomInt), player, skrpg);
            } else if (royaltyQuest.getRoyaltyQuestType().equals(RoyaltyQuestType.MINER)
                    && blockData.getCommonDrop().equals(Items.STONE)) {
                royaltyQuest.progress(bounds.get(randomInt), player, skrpg);
            } else if (royaltyQuest.getRoyaltyQuestType().equals(RoyaltyQuestType.WARRIOR)
                    && blockData.getCommonDrop().equals(Items.ROTTEN_FLESH)) {
                royaltyQuest.progress(bounds.get(randomInt), player, skrpg);
            }
            if (royaltyQuest.getRoyaltyQuestType().equals(RoyaltyQuestType.OBTAIN_EXP_MINING)
                    && blockData.getXpObtained().get(1) != 0.0) {
                royaltyQuest.progress((int) Math.round(blockData.getXpObtained().get(1)), player, skrpg);
            }
            if (royaltyQuest.getRoyaltyQuestType().equals(RoyaltyQuestType.OBTAIN_EXP_FARMING)
                    && blockData.getXpObtained().get(2) != 0.0) {
                royaltyQuest.progress((int) Math.round(blockData.getXpObtained().get(2)), player, skrpg);
            }
            if (royaltyQuest.getRoyaltyQuestType().equals(RoyaltyQuestType.OBTAIN_EXP_WOODCUTTING)
                    && blockData.getXpObtained().get(3) != 0.0) {
                royaltyQuest.progress((int) Math.round(blockData.getXpObtained().get(3)), player, skrpg);
            }
        }
        addItem(new SKRPGItemStack(blockData.getCommonDrop().generateItemInfo(), bounds.get(randomInt)));
        double randomDouble = Math.random();
        double randomDouble2 = Math.random();
        if (blockData.getDrops() != null) {
            for (Drop drop : blockData.getDrops()) {
                if (drop.getChance() >= randomDouble) {
                    addItem(new SKRPGItemStack(drop.getItems().generateItemInfo(), 1));
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
        double totalMiningGained = blockData.getXpObtained().get(1);
        double totalHerbalismGained = blockData.getXpObtained().get(2);
        double totalCraftingGained = blockData.getXpObtained().get(3);
        p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 2.0f);

        boolean air = !blockData.isStone();


        if (blockData.equals(BlockInformation.CANE)) {
            for (int y = 2; y >= 0; y--) {
                Block toCheck = block.getRelative(0, y, 0);
                if (toCheck.getType() == Material.SUGAR_CANE) {

                    if (Integer.parseInt(playerData.getHerbalism().getLevel().toString()
                            .replace("_", "")) * 0.04 >= randomDouble2) {
                        addItem(new SKRPGItemStack(blockData.getCommonDrop().generateItemInfo(), bounds.get(randomInt)));
                        playerData.addRunicPoints(1, skrpg);
                    }
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                            Text.color("&e+ " + blockData.getXpObtained().get(2) + " Herbalism XP (" + playerData.getHerbalism()
                                    .getXpTillNext() + "/" + Level.valueOf("_" +
                                    (Integer.parseInt(playerData.getHerbalism().getLevel().toString()
                                            .replace("_", "")) + 1)).getXpRequired() + ")")));
                    totalHerbalismGained = totalHerbalismGained + totalHerbalismGained;
                    playerData.getHerbalism().setTotalXP(playerData.getHerbalism().getTotalXP()
                            + blockData.getXpObtained().get(2));
                    playerData.getHerbalism().levelUpSkill(p, playerData, skrpg);
                    if (y == 0) {
                        addItem(new SKRPGItemStack(blockData.getCommonDrop().generateItemInfo(), bounds.get(randomInt)));
                        BrokenBlock brokenBlockCanePart = new BrokenBlock(skrpg, blockData, toCheck.getBlockData(), toCheck.getLocation(), true, false, false, false);
                    } else {
                        addItem(new SKRPGItemStack(blockData.getCommonDrop().generateItemInfo(), bounds.get(randomInt)));
                        BrokenBlock brokenBlockCanePart = new BrokenBlock(skrpg, blockData, toCheck.getBlockData(), toCheck.getLocation(), false, false, false, true);
                    }

                }
            }
        } else {
            BrokenBlock brokenBlock = new BrokenBlock(skrpg, blockData, block.getBlockData(), block.getLocation(), (blockData == BlockInformation.CANE) , false, blockData.isStone(), air);
        }
        if (totalMiningGained != 0.0) {
            if (Integer.parseInt(playerData.getMining().getLevel().toString()
                    .replace("_", "")) * 0.04 >= randomDouble2) {
                addItem(new SKRPGItemStack(blockData.getCommonDrop().generateItemInfo(), randomInt));
                playerData.addRunicPoints(1, skrpg);
            }
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
            if (Integer.parseInt(playerData.getHerbalism().getLevel().toString()
                    .replace("_", "")) * 0.04 >= randomDouble2) {
                addItem(new SKRPGItemStack(blockData.getCommonDrop().generateItemInfo(), randomInt));
                playerData.addRunicPoints(1, skrpg);
            }
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
            if (Integer.parseInt(playerData.getCrafting().getLevel().toString()
                    .replace("_", "")) * 0.04 >= randomDouble2) {
                addItem(new SKRPGItemStack(blockData.getCommonDrop().generateItemInfo(), randomInt));
                playerData.addRunicPoints(1, skrpg);
            }
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
    public String generateInventoryData() {
        StringJoiner items = new StringJoiner(",");

        List<String> equipmentKeys = new ArrayList<>();
        List<Integer> equipmentValues = new ArrayList<>();

        for (ItemStack itemStack : player.getInventory().getContents()) {
            if (player.getInventory().getItemInOffHand() != itemStack) {
                if (!Arrays.asList(9, 10, 11, 12).contains(Arrays.asList(player.getInventory().getContents()).indexOf(itemStack))) {
                    if (ItemInfo.parseItemInfo(itemStack) == null) {
                        items.add("0");
                        player.getInventory().remove(itemStack);
                    } else {
                        items.add(itemStack.getAmount() + "*" + ItemInfo.parseItemInfo(itemStack).generateItemInfoString());
                    }
                } else {
                    if (ItemInfo.parseItemInfo(itemStack) == null) {
                        equipmentKeys.add("0");
                        equipmentValues.add(0);
                    } else {
                        equipmentKeys.add(ItemInfo.parseItemInfo(itemStack).generateItemInfoString());
                        equipmentValues.add(itemStack.getAmount());


                    }
                }
            }
        }
        for (String string : equipmentKeys) {
            if (string.equals("0")) {
                items.add("0");
            } else {
                skrpg.getLogger().info(string);
                items.add(equipmentValues.get(equipmentKeys.indexOf(string)) + "*" + string);
            }

        }
        skrpg.getLogger().info(items.length() + "");
        playerData.createInventory(items.toString());
        skrpg.getLogger().info(playerData.getPlayerInventory().getInventoryEquipment().getRing() + " " +
                playerData.getPlayerInventory().getInventoryEquipment().getHeadband() + " " +
                playerData.getPlayerInventory().getInventoryEquipment().getNecklace() + " " +
                playerData.getPlayerInventory().getInventoryEquipment().getArtifact() + " " +
                playerData.getPlayerInventory().getInventoryEquipment().getHelmet() + " " +
                playerData.getPlayerInventory().getInventoryEquipment().getChestplate() + " " +
                playerData.getPlayerInventory().getInventoryEquipment().getLeggings() + " " +
                playerData.getPlayerInventory().getInventoryEquipment().getBoots() + " ");
        return items.toString();

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
    public void completeRoyaltyQuest(RoyaltyQuest royaltyQuest) {
        if (royaltyQuest.getProgressInteger() < royaltyQuest.getAmountNeeded()) { return; }
        List<Drop> royaltyDrops = Arrays.asList(Drop.WARRIOR_BLADE, Drop.ARCHER_LONGBOW, Drop.TANK_PLATE, Drop.HEALER_BOOK, Drop.TRICKSTER_WAND);
        player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0f, 2.0f);
        Text.applyText(player, "&8&m>                                          ");
        Text.applyText(player, "&6&l           QUEST COMPELTE!");
        Text.applyText(player, " ");
        Text.applyText(player, "&7You completed the " + royaltyQuest.getDifficulty().getColor() + royaltyQuest.getRoyaltyQuestType().getName() + " &7quest!");
        int randomPoints = new Random().nextInt(91);
        randomPoints = randomPoints + 10;
        randomPoints = randomPoints * (royaltyQuest.getDifficulty().getPriority() + 1);
        playerData.setRoyaltyPoints(playerData.getRoyaltyPoints() + randomPoints);
        Text.applyText(player, "&6+ " + randomPoints + " Royalty Points");
        int randomNuggets = new Random().nextInt(2500);
        randomNuggets = randomNuggets + 500;
        randomNuggets = randomNuggets * (royaltyQuest.getDifficulty().getPriority() + 1);
        playerData.setCredits(playerData.getCredits() + randomNuggets);
        Text.applyText(player, "&6+ " + randomNuggets + " Nuggets");
        Text.applyText(player, "&8&m>                                          ");
        double randomDouble2 = Math.random();
        for (Drop drop : royaltyDrops) {
            if (randomDouble2 <= drop.getChance()) {
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
        if (royaltyQuest.getDifficulty() == RoyaltyQuestDifficulty.INSANE) {
            new BukkitRunnable() {

                @Override
                public void run() {
                    double randomDouble = Math.random();
                    if (randomDouble <= 0.00001) {
                        int randomInt = new Random().nextInt(2);
                        player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_DEATH, 1.0f, 0.2f);
                        player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 0.2f);
                        player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0f, 0.2f);
                        if (randomInt == 1) {
                            playerData.getPlayerActionManager().addExistingItem(new SKRPGItemStack(Drop.KINGS_STAFF.getItems().generateItemInfo(), 1));
                            Text.applyText(player, Drop.KINGS_STAFF.getDropRarity().getName() + " DROP! &r&8| &r&7You dropped a " + Drop.KINGS_STAFF.getItems().getRarity().getColor() + Drop.KINGS_STAFF.getItems().getName());
                            for (Player players : Bukkit.getOnlinePlayers()) {
                                players.playSound(players.getLocation(), Sound.ENTITY_WITHER_DEATH, 1.0f, 0.2f);
                                Text.applyText(players, Drop.KINGS_STAFF.getDropRarity().getName() + " PLAYER DROP! &r&8| &r&6" + player.getDisplayName() + " &7dropped a " + Drop.KINGS_STAFF.getItems().getRarity().getColor() + Drop.KINGS_STAFF.getItems().getName() );
                            }
                        } else {
                            playerData.getPlayerActionManager().addExistingItem(new SKRPGItemStack(Drop.EXCALIBUR.getItems().generateItemInfo(), 1));
                            Text.applyText(player, Drop.EXCALIBUR.getDropRarity().getName() + " DROP! &r&8| &r&7You dropped a " + Drop.EXCALIBUR.getItems().getRarity().getColor() + Drop.EXCALIBUR.getItems().getName());
                            for (Player players : Bukkit.getOnlinePlayers()) {
                                players.playSound(players.getLocation(), Sound.ENTITY_WITHER_DEATH, 1.0f, 0.2f);
                                Text.applyText(players, Drop.EXCALIBUR.getDropRarity().getName() + " PLAYER DROP! &r&8| &r&6" + player.getDisplayName() + " &7dropped a " + Drop.EXCALIBUR.getItems().getRarity().getColor() + Drop.EXCALIBUR.getItems().getName() );
                            }
                        }
                    }

                }
            }.runTaskLater(skrpg, 20);

        }
        playerData.getRoyaltyQuests().remove(royaltyQuest);

    }
    public void openRoyaltyShop() {
        Inventory inv = Bukkit.createInventory(null, 27, Text.color("&6♛ &7Royalty Shop &6♛"));
        for (int i = 0; i <= 26; i++) {
            inv.setItem(i, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE, 0).setName(" ").asItem());
        }
        String levelAllowance = "";
        int priceAllowance = 1000;
        if (playerData.getRoyaltyUpgrades().containsKey(RoyaltyUpgrades.ALLOWANCE)) {
            levelAllowance = "" + playerData.getRoyaltyUpgrades().get(RoyaltyUpgrades.ALLOWANCE);
            priceAllowance = priceAllowance * playerData.getRoyaltyUpgrades().get(RoyaltyUpgrades.ALLOWANCE);
        }
        ItemStack allowance = new ItemBuilder(Material.GOLD_NUGGET, 0).setName("&7Allowance " + levelAllowance).setLore(
                Arrays.asList(" ", Text.color("&7Gain 1000 coins per level whenever you log on daily."), " ", Text.color("&7Cost: &6" + priceAllowance + " Royalty Points"))).asItem();
        net.minecraft.server.v1_16_R3.ItemStack nbtStack = CraftItemStack.asNMSCopy(allowance);
        NBTTagCompound nbtTagCompound = (nbtStack.hasTag()) ? nbtStack.getTag() : new NBTTagCompound();
        nbtTagCompound.setInt("price",
                priceAllowance);

        inv.setItem(10, CraftItemStack.asBukkitCopy(nbtStack));
        String levelReduction = "";
        int priceReduction = 1500;
        if (playerData.getRoyaltyUpgrades().containsKey(RoyaltyUpgrades.REDUCE_PRICE)) {
            levelReduction = "" + playerData.getRoyaltyUpgrades().get(RoyaltyUpgrades.REDUCE_PRICE);
            priceReduction = priceReduction * playerData.getRoyaltyUpgrades().get(RoyaltyUpgrades.REDUCE_PRICE);
        }
        ItemStack reduction = new ItemBuilder(Material.ARROW, 0).setName("&6Price Reduction " + levelReduction).setLore(
                Arrays.asList(" ", Text.color("&7Gain 0.5% off buying items from NPCs per level."), " ", Text.color("&7Cost: &6" + priceReduction + " Royalty Points"))).asItem();
        net.minecraft.server.v1_16_R3.ItemStack reductionStack = CraftItemStack.asNMSCopy(reduction);
        NBTTagCompound reductionTagCompound = (reductionStack.hasTag()) ? reductionStack.getTag() : new NBTTagCompound();
        reductionTagCompound.setInt("price",
                priceReduction);
        inv.setItem(12, CraftItemStack.asBukkitCopy(reductionStack));
        String levelLongLiveTheKing = "";
        int priceLongLiveTheKing = 3500;
        if (playerData.getRoyaltyUpgrades().containsKey(RoyaltyUpgrades.LONG_LIVE_THE_KING)) {
            levelLongLiveTheKing = "" + playerData.getRoyaltyUpgrades().get(RoyaltyUpgrades.LONG_LIVE_THE_KING);
            priceLongLiveTheKing = priceLongLiveTheKing * playerData.getRoyaltyUpgrades().get(RoyaltyUpgrades.LONG_LIVE_THE_KING);
        }
        ItemStack longLiveKing = new ItemBuilder(Material.GOLDEN_HELMET, 0).setName("&6Long Live The King " + levelLongLiveTheKing).setLore(
                Arrays.asList(" ", Text.color("&7Every level, gain an additional 1% chance to revive yourself upon death."), " ", Text.color("&7Cost: &6" + priceLongLiveTheKing + " Royalty Points"))).asItem();
        net.minecraft.server.v1_16_R3.ItemStack longLiveKingStack = CraftItemStack.asNMSCopy(longLiveKing);
        NBTTagCompound longLiveKingTagCompound = (longLiveKingStack.hasTag()) ? longLiveKingStack.getTag() : new NBTTagCompound();
        longLiveKingTagCompound.setInt("price",
                priceLongLiveTheKing);
        inv.setItem(14, CraftItemStack.asBukkitCopy(longLiveKingStack));
        String levelIncreasedSecurity = "";
        int priceIncreasedSecurity = 3000;
        if (playerData.getRoyaltyUpgrades().containsKey(RoyaltyUpgrades.INCREASED_SECURITY)) {
            levelIncreasedSecurity = "" + playerData.getRoyaltyUpgrades().get(RoyaltyUpgrades.INCREASED_SECURITY);
            priceIncreasedSecurity = priceIncreasedSecurity * playerData.getRoyaltyUpgrades().get(RoyaltyUpgrades.INCREASED_SECURITY);
        }
        ItemStack increasedSecurity = new ItemBuilder(Material.IRON_SWORD, 0).setName("&6Increased Security " + levelIncreasedSecurity).setLore(
                Arrays.asList(" ", Text.color("&7Lose 1% less &6Nuggets &7per level when you die."), " ", Text.color("&7Cost: &6" + priceIncreasedSecurity + " Royalty Points"))).asItem();
        net.minecraft.server.v1_16_R3.ItemStack increasedSecurityStack = CraftItemStack.asNMSCopy(increasedSecurity);
        NBTTagCompound increasedSecurityTagCompound = (increasedSecurityStack.hasTag()) ? increasedSecurityStack.getTag() : new NBTTagCompound();
        increasedSecurityTagCompound.setInt("price",
                priceIncreasedSecurity);
        inv.setItem(16, CraftItemStack.asBukkitCopy(increasedSecurityStack));
        player.openInventory(inv);
    }
    public void openRoyaltyMenu() {
        Inventory inv = Bukkit.createInventory(null, 54, Text.color("&6♛ &7Royalty Menu &6♛"));
        for (int i = 0; i <= 53; i++) {
            inv.setItem(i, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE, 0).setName(" ").asItem());
        }
        inv.setItem(49, new ItemBuilder(Material.GOLD_NUGGET, 0).setName("&7Your Royalty Points: &6" + playerData.getRoyaltyPoints()).asItem());
        List<Integer> slots = Arrays.asList(11, 13, 15, 29, 33);
        for (int i = 0; i < playerData.getRoyaltyQuests().size(); i++) {
            RoyaltyQuest royaltyQuest = playerData.getRoyaltyQuests().get(i);
            inv.setItem(slots.get(i), new ItemBuilder(royaltyQuest.getRoyaltyQuestType().getIcon(), 0)
                    .setName(royaltyQuest.getDifficulty().getColor() + royaltyQuest.getRoyaltyQuestType().getName()).setLore(
                            Arrays.asList(" ", Text.color("&7" + royaltyQuest.getRoyaltyQuestType().getObjective()), " ", Text.color("&6" + royaltyQuest.getProgressInteger() + "&7/&6" + royaltyQuest.getAmountNeeded()), " ", Text.color(royaltyQuest.getDifficulty().getColoredName().toUpperCase()))
                    ).asItem());
        }
        for (Integer slot : slots) {
            if (inv.getItem(slot).getType() == Material.BLACK_STAINED_GLASS_PANE) {
                inv.setItem(slot, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE, 0).setName("&cNo Active Quest").asItem());
            }
        }
        inv.setItem(48, new ItemBuilder(Material.GOLD_BLOCK, 0).setName("&6Royalty Shop").asItem());
        if (!playerData.hasRefreshed()) {
            inv.setItem(50, new ItemBuilder(Material.ARROW, 0).setName("&7Refresh for &61000 Royalty Points").asItem());
        }

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
    public void addItem(SKRPGItemStack SKRPGItemStack) {
        ItemInfo itemInfo = SKRPGItemStack.getItemInfo();
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
            playerData.getStash().add(SKRPGItemStack);
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
            ItemStack itemStack = Items.buildItem(SKRPGItemStack.getItemInfo().getItem());
            Items.updateItem(itemStack, SKRPGItemStack.getItemInfo());
            itemStack.setAmount(SKRPGItemStack.getAmount());
            player.getInventory().addItem(itemStack);
        }
    }
    public void addExistingItem(SKRPGItemStack SKRPGItemStack) {
        ItemInfo itemInfo = SKRPGItemStack.getItemInfo();

        if (player.getInventory().firstEmpty() == -1) {
            playerData.getStash().add(SKRPGItemStack);
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
            ItemStack itemStack = Items.buildItem(SKRPGItemStack.getItemInfo().getItem());
            Items.updateItem(itemStack, SKRPGItemStack.getItemInfo());
            itemStack.setAmount(SKRPGItemStack.getAmount());
            player.getInventory().addItem(itemStack);
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
                Arrays.asList(" ", Text.color("&7Apply a &5Runic Stone &7found around the world of SKQuest."),
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
        SKRPGItemStack helmetInfo = playerData.getPlayerInventory().getInventoryEquipment().getHelmet();
        SKRPGItemStack chestplateInfo = playerData.getPlayerInventory().getInventoryEquipment().getChestplate();
        SKRPGItemStack leggingsInfo = playerData.getPlayerInventory().getInventoryEquipment().getLeggings();
        SKRPGItemStack bootsInfo = playerData.getPlayerInventory().getInventoryEquipment().getBoots();
        SKRPGItemStack ringInfo = playerData.getPlayerInventory().getInventoryEquipment().getRing();
        SKRPGItemStack necklaceInfo = playerData.getPlayerInventory().getInventoryEquipment().getNecklace();
        SKRPGItemStack headbandInfo = playerData.getPlayerInventory().getInventoryEquipment().getHeadband();
        SKRPGItemStack artifactInfo = playerData.getPlayerInventory().getInventoryEquipment().getArtifact();
        SKRPGItemStack handInfo = playerData.getPlayerInventory().getItemInMainHand();

        int damage = 1;
        int strength = 1;
        if (helmetInfo != null) {
            damage = damage + helmetInfo.getItemInfo().getItem().getDamage() + helmetInfo.getItemInfo().getBonusDamage();
        }
        if (chestplateInfo != null) {
            damage = damage + chestplateInfo.getItemInfo().getItem().getDamage() + chestplateInfo.getItemInfo().getBonusDamage();
        }
        if (leggingsInfo != null) {
            damage = damage + leggingsInfo.getItemInfo().getItem().getDamage() + leggingsInfo.getItemInfo().getBonusDamage();
        }
        if (bootsInfo != null) {
            damage = damage + bootsInfo.getItemInfo().getItem().getDamage() + bootsInfo.getItemInfo().getBonusDamage();
        }
        if (handInfo.getItemInfo() != null && Arrays.asList(ItemCatagory.WEAPON, ItemCatagory.BOW).contains(handInfo.getItemInfo().getItem().getItemType().getItemCatagory())) {
            damage = damage + handInfo.getItemInfo().getItem().getDamage() + handInfo.getItemInfo().getBonusDamage();
        }
        if (ringInfo != null) {
            damage = damage + ringInfo.getItemInfo().getItem().getDamage() + ringInfo.getItemInfo().getBonusDamage();
        }
        if (headbandInfo != null) {
            damage = damage + headbandInfo.getItemInfo().getItem().getDamage() + headbandInfo.getItemInfo().getBonusDamage();
        }
        if (necklaceInfo != null) {
            damage = damage + necklaceInfo.getItemInfo().getItem().getDamage() + necklaceInfo.getItemInfo().getBonusDamage();
        }
        if (artifactInfo != null) {
            damage = damage + artifactInfo.getItemInfo().getItem().getDamage() + artifactInfo.getItemInfo().getBonusDamage();
        }
        strength = strength + playerData.getStrength();
        damage = (5 + damage + (strength / 5)) *
                (1 + strength / 100);
        return damage;
    }
    public void applyAttackSpeed() {
        ItemInfo handInfo = ItemInfo.parseItemInfo(player.getInventory().getItemInMainHand());
        if (handInfo != null) {

            skrpg.getHitCooldown().put(player, handInfo.getItem().getAttackSpeed().getTicksPerHit());
            for (ItemStack skrpgItemStack : player.getInventory().getContents()) {
                if (skrpgItemStack != null) {
                    player.setCooldown(skrpgItemStack.getType(), handInfo.getItem().getAttackSpeed().getTicksPerHit());
                }
            }
        }
    }
    // player join/leave
    public void playerDaily(Calendar updatedCalendar) {
        playerData.setIntrestDate(updatedCalendar.getTime());
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 2.0f, 0.25f);
        for (Bank bank : playerData.getBanks()) {
            playerData.setCredits(playerData.getCredits() + (double) Math.round(((bank.getCredits() * 0.005) * 100) / 100));
            Text.applyText(player, "&aYou earned &6" + (double) Math.round(((bank.getCredits() * 0.005) * 100) / 100) + " Nuggets &afrom bank intrest!");
        }
        if (playerData.getRoyaltyUpgrades().containsKey(RoyaltyUpgrades.ALLOWANCE)) {
            playerData.setCredits(playerData.getCredits() + (1000 * playerData.getRoyaltyUpgrades().get(RoyaltyUpgrades.ALLOWANCE)));
            Text.applyText(player, "&6&lALLOWANCE! &r&8| &r&aYou got &6" + (1000 * playerData.getRoyaltyUpgrades().get(RoyaltyUpgrades.ALLOWANCE)) + " Nuggets &7from your Allowance perk!");
        }
        new BukkitRunnable() {

            @Override
            public void run() {
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_YES, 1.0f, 0.4f);
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_FLUTE, 1.0f, 0.2f);
                Text.applyText(player, "&aYour &6♛ Royalty Quests &aare refreshed!");
                Text.applyText(player, " ");
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
                    Text.applyText(player, "&8&l>  &r&6" + royaltyQuest.getRoyaltyQuestType().getName() + " &8&l| &r" + royaltyQuest.getDifficulty().getColoredName());
                    Text.applyText(player, "&7" + royaltyQuest.getRoyaltyQuestType().getObjective());
                    Text.applyText(player, " ");
                    Text.applyText(player, "&6" + royaltyQuest.getProgressInteger() + "&7/&6" + royaltyQuest.getAmountNeeded());

                    playerData.getRoyaltyQuests().add(royaltyQuest);

                }

            }
        }.runTaskLater(skrpg, 40);
    }
}
