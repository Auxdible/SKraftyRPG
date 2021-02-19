package com.auxdible.skrpg.commands.inventory;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class CraftingTableCommand implements CommandExecutor {
    private SKRPG skrpg;
    public CraftingTableCommand(SKRPG skrpg) {
        this.skrpg = skrpg;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            skrpg.getLogger().info("You need to be a player to use this!");
            return false;
        }
        Player p = (Player) sender;
        Inventory inventory = Bukkit.createInventory(null, 45, "Crafting Table");
        for (int i = 0; i <= 44; i++) {
            inventory.setItem(i, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE, 0).setName(" ").asItem());
        }
        inventory.setItem(10, null);
        inventory.setItem(11, null);
        inventory.setItem(12, null);
        inventory.setItem(19, null);
        inventory.setItem(20, null);
        inventory.setItem(21, null);
        inventory.setItem(28, null);
        inventory.setItem(29, null);
        inventory.setItem(30, null);
        inventory.setItem(23, new ItemBuilder(Material.ARROW, 0).setName("&aYour Crafting Result").asItem());
        inventory.setItem(25, null);
        inventory.setItem(36, new ItemBuilder(Material.ARROW, 0).setName("&aBack to Menu").asItem());
        p.openInventory(inventory);
        return false;
    }
}
