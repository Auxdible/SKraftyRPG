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
        }
        if (args.length != 11) {
            Text.applyText(player, "&c&oUsage: /createregion (x) (z) (x2) (z2) (bannerX) (bannerY) (bannerZ) (raidX) (raidY) (raidZ) (name)");
            return false;
        }
        if (!player.hasPermission("skrpg.admin")) {
            Text.applyText(player, "&cAdmin is required to run this command!");
        }
        skrpg.getRegionManager().addRegion(skrpg.getRegionManager().getRegions().size(),
                args[10].replace("_", " "), Double.parseDouble(args[0]), Double.parseDouble(args[1]), Double.parseDouble(args[2]),
                Double.parseDouble(args[3]), player.getLocation(), new Location(Bukkit.getWorld(skrpg.getConfig().getString("rpgWorld")), Double.parseDouble(args[7]), Double.parseDouble(args[8]), Double.parseDouble(args[9]), 0.0f, 0.0f), Double.parseDouble(args[4]), Double.parseDouble(args[5]), Double.parseDouble(args[6]));

        Text.applyText(player, "&aRegion Created!");
        return false;
    }
}
