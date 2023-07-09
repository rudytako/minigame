package com.maciek.minigame.listener;

import com.maciek.minigame.GameState;
import com.maciek.minigame.Minigame;
import com.maciek.minigame.instance.Arena;
import com.maciek.minigame.kit.KitType;
import com.maciek.minigame.team.TeamType;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;

public class GameListener implements Listener {

    private Minigame minigame;

    public GameListener(Minigame minigame) {
        this.minigame = minigame;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getCurrentItem() == null) return;

        Player player = (Player) e.getWhoClicked();
        Arena arena = minigame.getArenaManager().getArena(player);

        if (arena == null) return;

        switch (e.getView().getTitle()) {
            case "Kit Selection":
                KitType type = KitType.valueOf(e.getCurrentItem().getItemMeta().getLocalizedName());

                KitType activated = arena.getKitType(player);
                if (activated != null && activated == type) {
                    player.sendMessage(ChatColor.RED + "You already have this kit equipped.");
                    player.closeInventory();
                    return;
                }

                arena.setKit(player.getUniqueId(), type);
                player.sendMessage(ChatColor.GREEN + "You have equipped the " + type.getDisplay() + ChatColor.GREEN + " kit.");

            case "Team selection":
                TeamType team = TeamType.valueOf(e.getCurrentItem().getItemMeta().getLocalizedName());

                if (arena.getTeam(player) == team) {
                    player.sendMessage(ChatColor.RED + "You are already on this team.");
                    return;
                }

                arena.setTeam(player, team);
                player.sendMessage(ChatColor.AQUA + "You are now on " + team.getDisplay() + " team.");
        }

        e.setCancelled(true);
        player.closeInventory();
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Arena arena = minigame.getArenaManager().getArena(e.getPlayer());

        if (arena.getState().equals(GameState.LIVE)) {
            arena.getGame().addPoint(e.getPlayer());
        }
    }
}
