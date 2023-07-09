package com.maciek.minigame.kit;

import com.maciek.minigame.Minigame;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import java.net.ProtocolException;
import java.util.UUID;

public abstract class Kit implements Listener {

    protected KitType type;
    protected UUID uuid;


    public Kit(Minigame minigame, KitType kitType, UUID uuid) {
        this.type = kitType;
        this.uuid = uuid;

        Bukkit.getPluginManager().registerEvents(this, minigame);
    }

    public KitType getType() {
        return type;
    }

    public UUID getUuid() {
        return uuid;
    }

    public abstract void onStart(Player player);

    public void remove() {
        HandlerList.unregisterAll(this);
    }

}
