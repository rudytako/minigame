package com.maciek.minigame.kit.type;

import com.maciek.minigame.Minigame;
import com.maciek.minigame.kit.Kit;
import com.maciek.minigame.kit.KitType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

public class MinerKit extends Kit {
    public MinerKit(Minigame minigame, UUID uuid) {
        super(minigame, KitType.MINER, uuid);
    }

    @Override
    public void onStart(Player player) {
        player.getInventory().addItem(new ItemStack(Material.IRON_PICKAXE));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 3));
    }
}
