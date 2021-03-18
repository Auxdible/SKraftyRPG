package com.auxdible.skrpg.commands.admin;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.items.Items;
import com.auxdible.skrpg.mobs.Mob;
import com.auxdible.skrpg.mobs.MobType;
import com.auxdible.skrpg.utils.Text;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.EnumSet;

public class SpawnEntityCommand implements CommandExecutor {
    private SKRPG skrpg;
    public SpawnEntityCommand(SKRPG skrpg) {
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
            skrpg.getLogger().info("Please name a mob!");
        }
        for (MobType mobType : EnumSet.allOf(MobType.class)) {
            if (args[0].equalsIgnoreCase(mobType.getId())) {
                MobType.buildMob(mobType.getId(), skrpg, player);
            }
        }
        return false;
    }
}
