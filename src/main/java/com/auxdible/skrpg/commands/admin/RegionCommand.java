package com.auxdible.skrpg.commands.admin;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.locations.regions.Region;
import com.auxdible.skrpg.utils.Text;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RegionCommand implements CommandExecutor {
    private SKRPG skrpg;
    public RegionCommand(SKRPG skrpg) {
        this.skrpg = skrpg;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            skrpg.getLogger().info("You can only use this command as a player!");
            return false;
        }
        Player player = (Player) sender;

        /*if (!player.hasPermission("skrpg.admin")) {
            Text.applyText(player, "&cAdmin is required to run this command!");
            return false;
        }*/
        if (args.length == 0) {
            Text.applyText(player, "&cUsage: /region (create, setloc, remove, leave [to quit setup])");
            return false;
        } else if (args[0].equalsIgnoreCase("create")) {
            if (skrpg.getRegionSetup(player) != null) {
                Text.applyText(player, "&cYou are in the region setup!");
                return false;
            }
            skrpg.getPlayersInRegionSetup().add(new RegionSetup(player, skrpg));
        } else if (args[0].equalsIgnoreCase("leave")) {
            if (skrpg.getRegionSetup(player) == null) {
                Text.applyText(player, "&cYou aren't in the region setup!");
                return false;
            }
            skrpg.getPlayersInRegionSetup().remove(skrpg.getRegionSetup(player));
        } else if (args[0].equalsIgnoreCase("setloc")) {
            if (args.length == 1) {
                Text.applyText(player, "&cEnter a location ID!");
                return false;
            }
            skrpg.getLocationManager().getLocations().put(args[1], player.getLocation());
        } else if (args[0].equalsIgnoreCase("remove")) {
            if (args.length == 2) {
                try {
                    Text.applyText(player, "Removed the region!");
                    int id = Integer.parseInt(args[1]);
                    if (skrpg.getRegionManager().getRegion(id) != null) {
                        Region removedRegion = skrpg.getRegionManager().getRegion(id);
                        removedRegion.setControllingGuild(null);
                        removedRegion.getBannerLocation().getBlock().setType(Material.AIR);
                        removedRegion.getStand().remove();
                        removedRegion.setControllingGuild(null);
                        skrpg.getRegionManager().getRegions().remove(removedRegion);
                    }
                } catch (NumberFormatException x) {
                    Text.applyText(player, "&cPlease enter a valid id!");
                    return false;
                }
            } else {
                Text.applyText(player, "&cUsage: /region remove (id)");
                return false;
            }
        }

        return false;
    }
}
