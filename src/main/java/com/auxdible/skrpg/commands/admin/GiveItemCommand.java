package com.auxdible.skrpg.commands.admin;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.items.Items;
import com.auxdible.skrpg.player.PlayerData;
import com.auxdible.skrpg.utils.ItemBuilder;
import com.auxdible.skrpg.utils.Text;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;

public class GiveItemCommand implements CommandExecutor {
    private SKRPG skrpg;
    public GiveItemCommand(SKRPG skrpg) {
        this.skrpg = skrpg;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            skrpg.getLogger().info("You cannot use this command as the console!");
            return false;
        }
        Player player = (Player) sender;
        if (!player.hasPermission("skrpg.admin")) {
            Text.applyText(player, "&cAdmin is required to run this command!");
            return false;
        }
        if (args.length == 0) {
            skrpg.getLogger().info("Please name an item!");
        }
        for (Items items : EnumSet.allOf(Items.class)) {
            if (args[0].equalsIgnoreCase(items.getId())) {
                player.getInventory().addItem(Items.buildItem(items));
            }
        }
        return false;
    }
}
