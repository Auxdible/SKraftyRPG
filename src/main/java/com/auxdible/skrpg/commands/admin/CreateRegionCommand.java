package com.auxdible.skrpg.commands.admin;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.regions.Region;
import com.auxdible.skrpg.utils.Text;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreateRegionCommand implements CommandExecutor {
    private SKRPG skrpg;
    public CreateRegionCommand(SKRPG skrpg) {
        this.skrpg = skrpg;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            skrpg.getLogger().info("You can only use this command as a player!");
            return false;
        }
        Player player = (Player) sender;

        if (!player.hasPermission("skrpg.admin")) {
            Text.applyText(player, "&cAdmin is required to run this command!");
            return false;
        }
        if (args.length == 0) {
            if (skrpg.getSetup(player) != null) {
                Text.applyText(player, "&cYou are in the region setup!");
                return false;
            }
            skrpg.getPlayersInRegionSetup().add(new RegionSetup(player, skrpg));
        } else if (args[0].equalsIgnoreCase("leave")) {
            if (skrpg.getSetup(player) == null) {
                Text.applyText(player, "&cYou aren't in the region setup!");
                return false;
            }
            skrpg.getPlayersInRegionSetup().remove(skrpg.getSetup(player));
        }

        return false;
    }
}
