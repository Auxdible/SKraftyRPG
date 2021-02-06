package com.auxdible.skrpg.commands.admin;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.regions.Region;
import com.auxdible.skrpg.utils.Text;
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
        }
        if (args.length != 5) {
            Text.applyText(player, "&c&oUsage: /createregion (x) (z) (x2) (z2) (name)");
            return false;
        }
        if (!player.hasPermission("skrpg.admin")) {
            Text.applyText(player, "&cAdmin is required to run this command!");
        }
        skrpg.getRegionManager().addRegion(skrpg.getRegionManager().getRegions().size(),
                args[4].replace("_", " "), Double.parseDouble(args[0]), Double.parseDouble(args[1]), Double.parseDouble(args[2]),
                Double.parseDouble(args[3]));
        Text.applyText(player, "&aRegion Created!");
        return false;
    }
}
