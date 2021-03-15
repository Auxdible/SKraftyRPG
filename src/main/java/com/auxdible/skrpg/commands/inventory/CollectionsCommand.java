package com.auxdible.skrpg.commands.inventory;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.items.Items;
import com.auxdible.skrpg.player.PlayerData;
import com.auxdible.skrpg.player.collections.Collection;
import com.auxdible.skrpg.player.collections.CollectionType;
import com.auxdible.skrpg.player.collections.Tiers;
import com.auxdible.skrpg.utils.ItemBuilder;
import com.auxdible.skrpg.utils.Text;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.stream.Collectors;

public class CollectionsCommand implements CommandExecutor {
    private SKRPG skrpg;
    public CollectionsCommand(SKRPG skrpg) {
        this.skrpg = skrpg;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            skrpg.getLogger().info("You can only use this as a player!");
        }
        Player p = (Player) sender;
        PlayerData playerData = skrpg.getPlayerManager().getPlayerData(p.getUniqueId());
        if (args.length == 0) {
            Inventory inv = Bukkit.createInventory(null, 54, "Your Collections");
            for (int i = 0; i <= 53; i++) {
                inv.setItem(i, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE, 0).setName(" ").asItem());
            }

            for (Collection collection : playerData.getCollections()) {
                if (collection.getCollectionAmount() != 0) {
                    inv.setItem(10 + playerData.getCollections().indexOf(collection), new ItemBuilder(
                            collection.getCollectionType().getItem().getItemStack().getType(), 0).setName(
                            "&b" + collection.getCollectionType().getItem().getName()
                    ).setLore(Arrays.asList(" ", Text.color("&7Tier &b" + SKRPG.levelToInt(collection.getTier().toString())),
                            " ",
                            Text.color("&b" + collection.getCollectionAmount() + "&8/&b" + Tiers.valueOf("_" + (SKRPG.levelToInt(collection.getTier().toString()) + 1)).getAmountRequired()))).asItem());
                } else {
                    inv.setItem(10 + playerData.getCollections().indexOf(collection), new ItemBuilder(
                            Material.BARRIER, 0).setName(
                            "&b" + collection.getCollectionType().getItem().getName()
                    ).setLore(Arrays.asList(" ", Text.color("&cYou have not unlocked this collection!"), " ")).asItem());
                }

            }
            inv.setItem(45, new ItemBuilder(Material.ARROW, 0).setName("&aBack to Menu").asItem());
            p.openInventory(inv);
        } else {
            for (Collection collection : playerData.getCollections()) {
                if (args[0].equalsIgnoreCase(collection.getCollectionType().toString())) {
                    HashMap<Tiers, List<Items>> rewardsMap = CollectionType.generateRewardsMap(collection.getCollectionType());
                    Inventory inv = Bukkit.createInventory(null, 45, collection.getCollectionType().getItem().getName() + " | Collection Viewer");
                    ItemStack collectionItem = Items.buildItem(collection.getCollectionType().getItem());
                    ItemMeta collectioniM = collectionItem.getItemMeta();
                    Tiers tiersNext = Tiers.valueOf("_" + (SKRPG.levelToInt(collection.getTier().toString()) + 1));
                    collectioniM.setDisplayName(Text.color(collection.getCollectionType().getItem().getName() + " &7Collection"));
                    collectioniM.setLore(Arrays.asList(" ", Text.color("&7Total Collected: &b" + collection.getCollectionAmount()),
                            Text.color("&7Tier &b" + tiersNext + " &7Progress: &b" + collection.getCollectionAmount() + "&7/&b" + tiersNext.getAmountRequired()),
                            " ",
                            Text.color("&7Tier &b" + SKRPG.levelToInt(collection.getTier().toString()))));
                    collectionItem.setItemMeta(collectioniM);
                    inv.setItem(4, collectionItem);
                    for (int i = 0; i <= 44; i++) {
                        inv.setItem(i, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE, 0).setName(" ").asItem());
                    }
                    for (List<Items> rewards : collection.getCollectionType().getTierRewards()) {
                        List<Tiers> rewardsList = new ArrayList<>(getKeysByValue(rewardsMap, rewards));
                        ItemStack rewardsStack = Items.buildItem(rewards.get(0));
                        ItemMeta iM = rewardsStack.getItemMeta();
                        List<String> lore = new ArrayList<>();
                        lore.add(" ");
                        for (Items items : rewards) {
                            lore.add(Text.color(items.getRarity().getColor() + items.getName() + " &7Recipe"));
                        }
                        lore.add(" ");
                        iM.setLore(lore);
                        iM.setDisplayName(Text.color("&7" + collection.getCollectionType().getItem().getName() + " &b" + SKRPG.levelToInt(
                                rewardsList.get(0).toString())));
                        rewardsStack.setItemMeta(iM);
                        inv.setItem(9 + collection.getCollectionType().getTierRewards().indexOf(rewards), rewardsStack);
                    }
                    p.openInventory(inv);
                }

            }
        }

        return false;
    }
    public static <T, E> Set<T> getKeysByValue(Map<T, E> map, E value) {
        return map.entrySet()
                .stream()
                .filter(entry -> Objects.equals(entry.getValue(), value))
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }
}
