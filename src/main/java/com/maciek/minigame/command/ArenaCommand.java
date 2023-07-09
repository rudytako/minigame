package com.maciek.minigame.command;

import com.maciek.minigame.GameState;
import com.maciek.minigame.Minigame;
import com.maciek.minigame.instance.Arena;
import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ArenaCommand implements CommandExecutor {

    private Minigame minigame;

    public ArenaCommand(Minigame minigame) {
        this.minigame = minigame;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) return false;

        Player player = (Player) sender;

        switch (args[0].toLowerCase()) {
            case "list":
                player.sendMessage(ChatColor.GREEN + "These are the available arenas:");

                for (Arena arena : minigame.getArenaManager().getArenas()) {
                    player.sendMessage(ChatColor.GREEN + "- " + arena.getId() + " (" + arena.getState().name() + ")");
                }

                break;

            case "leave":
                Arena arenaToLeave = minigame.getArenaManager().getArena(player);

                if (arenaToLeave == null) {
                    player.sendMessage(ChatColor.RED + "You are not in an arena.");
                    break;
                }

                player.sendMessage(ChatColor.RED + "You left the arena.");
                arenaToLeave.removePlayer(player);

                break;

            case "join":
                if (args.length != 2) {
                    player.sendMessage(ChatColor.RED + "Invalid usage! Try /arena join <id>");
                    break;
                }

                if (minigame.getArenaManager().getArena(player) != null) {
                    player.sendMessage(ChatColor.RED + "You are already in the arena.");
                    break;
                }

                int id;
                try {
                    id = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    player.sendMessage(ChatColor.RED + "You specified an invalid arena id.");
                    break;
                }

                if (id < 0 || id > minigame.getArenaManager().getArenas().size()) {
                    player.sendMessage(ChatColor.RED + "You specified an invalid arena id.");
                }

                Arena arenaToJoin = minigame.getArenaManager().getArena(id);
                if (arenaToJoin.getState() == GameState.LIVE) {
                    player.sendMessage(ChatColor.RED + "You cannot join this arena right now.");
                }

                player.sendMessage(ChatColor.GREEN + "You are now playing in arena " + id + ".");
                arenaToJoin.addPlayer(player);

                break;
            case "kit":
                Arena arena = minigame.getArenaManager().getArena(player);

                if (arena == null) {
                    player.sendMessage(ChatColor.RED + "You are not in an arena.");
                    break;
                }

                if (arena.getState() == GameState.LIVE) {
                    player.sendMessage(ChatColor.RED + "You cannot select a kit at this time!");
                }

                break;
            default:
                player.sendMessage(ChatColor.RED + "Invalid usage! These are the options:");
                player.sendMessage(ChatColor.RED + "- /arena list");
                player.sendMessage(ChatColor.RED + "- /arena leave");
                player.sendMessage(ChatColor.RED + "- /arena join <id>");
                player.sendMessage(ChatColor.RED + "- /arena kit");
                break;
        }

        return false;
    }
}
