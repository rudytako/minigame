package com.maciek.minigame.kit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class KitUI {

    public KitUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, ChatColor.BLUE + "Kit Selection");

        for (KitType type : KitType.values()) {
            ItemStack is = new ItemStack(type.getMaterial());
            ItemMeta ismeta = is.getItemMeta();

            ismeta.setDisplayName(type.getDisplay());
            ismeta.setLore(Arrays.asList(type.getDescription()));
            ismeta.setLocalizedName(type.name());
            is.setItemMeta(ismeta);

            gui.addItem(is);
        }

        player.openInventory(gui);
    }

}
