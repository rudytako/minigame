package com.maciek.minigame.listener;

import com.maciek.minigame.GameState;
import com.maciek.minigame.Minigame;
import com.maciek.minigame.instance.Arena;
import com.maciek.minigame.kit.KitType;
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
    public void onClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();

        if (!(e.getView().getTitle().contains("Kit Selection")) || e.getCurrentItem() == null) return;

        KitType type = KitType.valueOf(e.getCurrentItem().getItemMeta().getLocalizedName());

        Arena arena = minigame.getArenaManager().getArena(player);
        if (arena == null) return;

        KitType activated = arena.getKitType(player);
        if (activated != null && activated == type) {
            player.sendMessage(ChatColor.RED + "You already have this kit equipped.");
            player.closeInventory();
            return;
        }

        arena.setKit(player.getUniqueId(), type);
        player.sendMessage(ChatColor.GREEN + "You have equipped the " + type.getDisplay() + ChatColor.GREEN + " kit.");
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
