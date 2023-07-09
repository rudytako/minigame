package com.maciek.minigame;

import com.maciek.minigame.command.ArenaCommand;
import com.maciek.minigame.listener.ConnectListener;
import com.maciek.minigame.listener.GameListener;
import com.maciek.minigame.manager.ArenaManager;
import com.maciek.minigame.manager.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Minigame extends JavaPlugin {

    private ArenaManager arenaManager;

    @Override
    public void onEnable() {
        ConfigManager.setupConfig(this);

        Bukkit.getPluginManager().registerEvents(new ConnectListener(this), this);
        Bukkit.getPluginManager().registerEvents(new GameListener(this), this);

        arenaManager  = new ArenaManager(this);

        getCommand("arena").setExecutor(new ArenaCommand(this));
    }

    public ArenaManager getArenaManager() {
        return arenaManager;
    }
}
