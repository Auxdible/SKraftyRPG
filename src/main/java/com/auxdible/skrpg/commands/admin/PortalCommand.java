package com.auxdible.skrpg.commands.admin;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.locations.portals.Portal;
import com.auxdible.skrpg.utils.Text;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PortalCommand implements CommandExecutor {
    private SKRPG skrpg;
    public PortalCommand(SKRPG skrpg) {
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
            Text.applyText(player, "&cUsage: /portal (create, remove, leave [to quit setup])");
            return false;
        } else if (args[0].equalsIgnoreCase("create")) {
            if (skrpg.getPortalSetup(player) != null) {
                Text.applyText(player, "&cYou are in the portal setup!");
                return false;
            }
            skrpg.getPlayersInPortalSetup().add(new PortalSetup(player, skrpg));
        } else if (args[0].equalsIgnoreCase("leave")) {
            if (skrpg.getPortalSetup(player) == null) {
                Text.applyText(player, "&cYou aren't in the portal setup!");
                return false;
            }
            skrpg.getPlayersInPortalSetup().remove(skrpg.getPortalSetup(player));
        } else if (args[0].equalsIgnoreCase("remove")) {
            if (args.length == 2) {
                try {
                    Text.applyText(player, "Removed the portal!");
                    int id = Integer.parseInt(args[1]);
                    if (skrpg.getPortalManager().getPortal(id) != null) {
                        Portal removedPortal = skrpg.getPortalManager().getPortal(id);

                        skrpg.getPortalManager().getPortals().remove(removedPortal);
                    }
                } catch (NumberFormatException x) {
                    Text.applyText(player, "&cPlease enter a valid id!");
                    return false;
                }
            } else {
                Text.applyText(player, "&cUsage: /portal remove (id)");
                return false;
            }
        }

        return false;
    }
}
