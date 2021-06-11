package com.auxdible.skrpg.commands.admin;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.items.forage.ForageLocation;
import com.auxdible.skrpg.items.forage.ForageType;
import com.auxdible.skrpg.mobs.Mob;
import com.auxdible.skrpg.mobs.MobSpawn;
import com.auxdible.skrpg.utils.Text;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreateForageCommand implements CommandExecutor {
    private SKRPG skrpg;
    public CreateForageCommand(SKRPG skrpg) {
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
            if (skrpg.getRegionSetup(player) != null) {
                Text.applyText(player, "&cPlease enter a valid forage!");
                return false;
            }
        } else {


        }
        if (args.length == 0) {
            Text.applyText(player, "&c&oUsage: /forage (create, remove)");
            return false;
        } else if (args[0].equalsIgnoreCase("create")) {
            if (args.length == 2) {
                skrpg.getForageManager().getLocations().add(new ForageLocation(skrpg, player.getLocation(), ForageType.getForage(args[0])));
            } else {
                Text.applyText(player, "&c&oUsage: /forage create (forage id)");
            }
        } else if (args[0].equalsIgnoreCase("remove")) {
            if (args.length == 2) {
                try {
                    Text.applyText(player, "Removed the mob spawn!");
                    int id = Integer.parseInt(args[1]);
                    if (skrpg.getMobSpawnManager().getMobSpawn(id) != null) {
                        MobSpawn removedSpawn = skrpg.getMobSpawnManager().getMobSpawn(id);
                        if (removedSpawn.getCurrentlySpawnedMobs() != null && !removedSpawn.getCurrentlySpawnedMobs().isEmpty()) {
                            for (Mob mob : removedSpawn.getCurrentlySpawnedMobs()) {
                                mob.getEnt().remove();
                            }
                            removedSpawn.getCurrentlySpawnedMobs().clear();
                        }
                        skrpg.getMobSpawnManager().getMobSpawns().remove(removedSpawn);
                    }
                } catch (NumberFormatException x) {
                    Text.applyText(player, "&cPlease enter a valid id!");
                    return false;
                }

            }

        }
        return false;
    }
}
