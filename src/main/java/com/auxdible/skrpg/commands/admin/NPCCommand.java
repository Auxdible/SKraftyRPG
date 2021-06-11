package com.auxdible.skrpg.commands.admin;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.npcs.NPC;
import com.auxdible.skrpg.utils.Text;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NPCCommand implements CommandExecutor {
    private SKRPG skrpg;

    public NPCCommand(SKRPG skrpg) {
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
            Text.applyText(player, "&c&oUsage: /npc (create/remove/list)");
            return false;
        } else if (args[0].equalsIgnoreCase("create")) {
            if (args.length == 2) {
                if (skrpg.getNpcManager().getNpcs() == null) {
                    try {
                        skrpg.getNpcManager().addNpc(args[1],
                                player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(),
                                player.getLocation().getYaw(), player.getLocation().getPitch());
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        skrpg.getNpcManager().addNpc(args[1],
                                player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(),
                                player.getLocation().getYaw(), player.getLocation().getPitch());
                    } catch (IllegalAccessException | InstantiationException e) {
                        e.printStackTrace();
                    }
                }

                Text.applyText(player, "&aNPC Created!");
                return false;
            } else {
                Text.applyText(player, "&c&oUsage: /npc create (type)");
            }

        } else if (args[0].equalsIgnoreCase("remove")) {
            if (args.length == 2) {
                try {
                    int id = Integer.parseInt(args[1]);
                    NPC removedNpc = skrpg.getNpcManager().getNpc(id);
                    skrpg.getNpcManager().getNpcs().remove(removedNpc);
                    removedNpc.deleteNPC();
                } catch (NumberFormatException x) {
                    Text.applyText(player, "&cInvalid number/id!");
                    return false;
                }


            } else {
                Text.applyText(player, "&c&oUsage: /npc remove (id)");
            }
        } else if (args[0].equalsIgnoreCase("list")) {
            if (skrpg.getNpcManager().getNpcs() != null) {
                for (NPC npc : skrpg.getNpcManager().getNpcs()) {
                    player.sendMessage(npc.getId() + " ID | " + npc.getNpcType().toString() + " NPCTYPE | " + npc.getLocation().getX() + " " + npc.getLocation().getY() + " " + npc.getLocation().getZ() + " LOCATION");
                }
            }
        }
        return false;
    }
}

