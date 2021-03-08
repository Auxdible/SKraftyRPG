package com.auxdible.skrpg.commands.inventory;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.player.PlayerData;
import com.auxdible.skrpg.player.collections.Collection;
import com.auxdible.skrpg.player.collections.Tiers;
import com.auxdible.skrpg.utils.ItemBuilder;
import com.auxdible.skrpg.utils.Text;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;
import java.util.Collections;

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
        return false;
    }
}
