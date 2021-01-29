package com.auxdible.skrpg.commands;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.player.PlayerData;
import com.auxdible.skrpg.utils.ItemBuilder;
import com.auxdible.skrpg.utils.ItemTweaker;
import com.auxdible.skrpg.utils.Text;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;

public class MenuCommand implements CommandExecutor {
    private SKRPG skrpg;
    public MenuCommand(SKRPG skrpg) {
        this.skrpg = skrpg;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            skrpg.getLogger().info("You must be a player to use this command!");
        }
        Player player = (Player) sender;
        PlayerData playerData = skrpg.getPlayerManager().getPlayerData(player.getUniqueId());
        Inventory inv = Bukkit.createInventory(null, 54, "SKRPG Menu");
        for (int i = 0; i <= 53; i++) {
            inv.setItem(i, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE, 0).setName(" ").asItem());
        }
        inv.setItem(13, new ItemBuilder(ItemTweaker.createPlayerHead(player.getName()))
                .setName(Text.color("&aYour Statistics")).setLore(Arrays.asList(
                        Text.color("&8&m>               <"),
                        Text.color("&fMax HP: &c" + playerData.getMaxHP() + " ♥"),
                        Text.color("&fDefence: &a" + playerData.getDefence() + " ✿"),
                        Text.color("&fStrength: &4" + playerData.getStrength() + " ☄"),
                        Text.color("&fMax Energy: &e" + playerData.getMaxEnergy() + " ☢"),
                        Text.color("&fSpeed: &f" + playerData.getSpeed() + " ≈"),
                        Text.color("&fCredits: &b" + playerData.getCredits() + " C$"),
                        Text.color("&8&m>               <")
                )).asItem());
        inv.setItem(30, new ItemBuilder(Material.STONE_SWORD, 0).setName("&aSkills").asItem());
        inv.setItem(31, new ItemBuilder(Material.CRAFTING_TABLE, 0).setName("&aCrafting Table").asItem());
        inv.setItem(32, new ItemBuilder(Material.CHEST, 0).setName("&aCollections").asItem());
        player.openInventory(inv);
        return false;
    }
}
