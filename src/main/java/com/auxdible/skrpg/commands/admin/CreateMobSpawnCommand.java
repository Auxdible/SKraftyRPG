package com.auxdible.skrpg.commands.admin;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.mobs.MobType;
import com.auxdible.skrpg.mobs.npcs.NpcType;
import com.auxdible.skrpg.utils.Text;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreateMobSpawnCommand implements CommandExecutor {
    private SKRPG skrpg;
    public CreateMobSpawnCommand(SKRPG skrpg) {
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
        if (args.length != 1) {
            Text.applyText(player, "&c&oUsage: /createmobspawn (type)");
            return false;
        }
        if (skrpg.getMobSpawnManager().getMobSpawns() == null) {
            skrpg.getMobSpawnManager().addMobSpawn(0, MobType.valueOf(args[0]),
                    player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());
        } else {
            skrpg.getMobSpawnManager().addMobSpawn(skrpg.getNpcManager().getNpcs().size(), MobType.valueOf(args[0]),
                    player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());
        }

        Text.applyText(player, "&aMob Spawn Created!");
        return false;
    }
}
