package com.auxdible.skrpg.commands.admin;

import com.auxdible.skrpg.SKRPG;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpeedCommand implements CommandExecutor {
    private SKRPG skrpg;
    public SpeedCommand(SKRPG skrpg) {
        this.skrpg = skrpg;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            skrpg.getLogger().info("You must be a player in order to use this!");
        }
        Player p = (Player) sender;
        if (args.length == 1)  {
            p.setWalkSpeed((Float.parseFloat(args[0]) / 5f) * 0.01f);
        }
        return false;
    }
}
