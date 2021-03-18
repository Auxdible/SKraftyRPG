package com.auxdible.skrpg.commands.admin;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.mobs.npcs.NpcType;
import com.auxdible.skrpg.utils.Text;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreateNPCCommand implements CommandExecutor {
    private SKRPG skrpg;
    public CreateNPCCommand(SKRPG skrpg) {
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
        if (args.length != 1) {
            Text.applyText(player, "&c&oUsage: /createnpc (type)");
            return false;
        }
        if (skrpg.getNpcManager().getNpcs() == null) {
            try {
                skrpg.getNpcManager().addNpc(args[0], 0,
                        player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(),
                        player.getLocation().getYaw(), player.getLocation().getPitch());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        } else {
            try {
                skrpg.getNpcManager().addNpc(args[0], skrpg.getNpcManager().getNpcs().size(),
                        player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(),
                        player.getLocation().getYaw(), player.getLocation().getPitch());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }

        Text.applyText(player, "&aNPC Created!");
        return false;
    }
}
