package com.maciek.minigame.instance;

import com.maciek.minigame.GameState;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class Game {

    private Arena arena;
    private HashMap<UUID, Integer> points;

    public Game(Arena arena) {
        this.arena = arena;
        points = new HashMap<>();
    }

    public void start() {
        arena.setState(GameState.LIVE);
        arena.sendMessage(ChatColor.GREEN + "THE GAME HAS STARTED! Your objective is to break 20 blocks in the fastest time. good luck!");

        for (UUID uuid : arena.getKits().keySet()) {
            arena.getKits().get(uuid).onStart(Bukkit.getPlayer(uuid));
        }

        for (UUID uuid : arena.getPlayers()) {
            points.put(uuid, 0);
            Bukkit.getPlayer(uuid).closeInventory();
        }
    }

    public void addPoint(Player player) {
        int playerPoints = points.get(player.getUniqueId()) + 1;

        if (playerPoints == 20) {
            arena.sendMessage(ChatColor.GOLD + player.getName() + " HAS WON! Thanks for playing!");
            arena.reset(true);
            return;
        }

        player.sendMessage(ChatColor.GREEN + "+1 POINT!");
        points.replace(player.getUniqueId(), playerPoints);
    }
}
