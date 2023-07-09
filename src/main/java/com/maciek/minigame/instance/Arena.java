package com.maciek.minigame.instance;

import com.google.common.collect.TreeMultimap;
import com.maciek.minigame.GameState;
import com.maciek.minigame.Minigame;
import com.maciek.minigame.kit.Kit;
import com.maciek.minigame.kit.KitType;
import com.maciek.minigame.kit.type.FighterKit;
import com.maciek.minigame.kit.type.MinerKit;
import com.maciek.minigame.manager.ConfigManager;
import com.maciek.minigame.team.TeamType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Arena {

    private Minigame minigame;

    private int id;
    private Location spawn;

    private GameState state;
    private List<UUID> players;
    private HashMap<UUID, Kit> kits;
    private HashMap<UUID, TeamType> teams;
    private Countdown countdown;
    private Game game;

    public Arena(Minigame minigame, int id, Location spawn) {
        this.minigame = minigame;

        this.id = id;
        this.spawn = spawn;

        this.state = GameState.RECRUITING;

        this.players = new ArrayList<>();
        this.kits = new HashMap<>();
        this.teams = new HashMap<>();

        this.countdown = new Countdown(minigame, this);
        this.game = new Game(this);
    }

    /* GAME */

    public void start() {
        game.start();
    }

    public void reset(boolean kickPlayers) {
        if (kickPlayers) {
            Location spawn = ConfigManager.getLobbySpawn();
            for (UUID uuid : players) {
                Player player = Bukkit.getPlayer(uuid);
                player.teleport(spawn);
                removeKit(player.getUniqueId());
            }
            players.clear();
            kits.clear();
            teams.clear();
        }

        sendTitle("", "");

        state = GameState.RECRUITING;
        countdown.cancel();
        countdown = new Countdown(minigame, this);
        game = new Game(this);
    }

    /* TOOLS */

    public void sendMessage(String message) {
        for (UUID uuid : players) {
            Bukkit.getPlayer(uuid).sendMessage(message);
        }
    }

    public void sendTitle(String title, String subtitle) {
        for (UUID uuid : players) {
            Bukkit.getPlayer(uuid).sendTitle(title, subtitle);
        }
    }

    /* PLAYERS MANAGEMENT */

    public void addPlayer(Player player) {
        players.add(player.getUniqueId());
        player.teleport(spawn);

        TreeMultimap<Integer, TeamType> count = TreeMultimap.create();
        for (TeamType team : teams.values()) {
            count.put(getTeamCount(team), team);
        }

        TeamType lowest = (TeamType) count.values().toArray()[0];
        setTeam(player, lowest);
        player.sendMessage(ChatColor.AQUA + "You've been automatically placed on " + lowest.getDisplay() + ChatColor.AQUA + " team");

        player.sendMessage(ChatColor.GOLD + "Choose your kit with /arena kit");

        if (state.equals(GameState.RECRUITING) && players.size() >= ConfigManager.getRequiredPlayers() ) {
            countdown.start();
        }
    }

    public void removePlayer(Player player) {
        players.remove(player.getUniqueId());
        player.teleport(ConfigManager.getLobbySpawn());
        player.sendTitle("", "");

        removeKit(player.getUniqueId());

        if (state == GameState.COUNTDOWN && players.size() < ConfigManager.getRequiredPlayers()) {
            sendMessage(ChatColor.RED + "There is not enough players. Countdown stopped.");
            reset(false);
        }

        if (state == GameState.LIVE && players.size() < ConfigManager.getRequiredPlayers()) {
            sendMessage(ChatColor.RED + "The game has ended as too many players have left.");
            reset(false);
        }
    }

    /* ARENA MANAGEMENT */

    public int getId() {
        return id;
    }

    public List<UUID> getPlayers() {
        return players;
    }

    public HashMap<UUID, Kit> getKits() { return kits; }

    public GameState getState() {
        return state;
    }

    public Game getGame() {
        return game;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    /* KITS */

    public void setKit(UUID uuid, KitType type) {
        removeKit(uuid);
        switch (type) {
            case MINER:
                kits.put(uuid, new MinerKit(minigame, uuid));
                break;
            case FIGHTER:
                kits.put(uuid, new FighterKit(minigame, uuid));
                break;
        }
    }

    public void removeKit(UUID uuid) {
        if (kits.containsKey(uuid)) {
            kits.get(uuid).remove();
            kits.remove(uuid);
        }
    }

    public KitType getKitType(Player player) {
        return kits.containsKey(player.getUniqueId()) ? kits.get(player.getUniqueId()).getType() : null;
    }

    /* TEAMS */

    public void setTeam(Player player, TeamType team) {
        removeTeam(player);
        teams.put(player.getUniqueId(), team);
    }

    public void removeTeam(Player player) {
        if (teams.containsKey(player.getUniqueId())) {
            teams.remove(player.getUniqueId());
        }
    }

    public int getTeamCount(TeamType team) {
        int amount = 0;
        for (TeamType t : teams.values()) {
            if (t == team) {
                amount++;
            }
        }
        return amount;
    }

    public TeamType getTeam(Player player) {
        return teams.get(player.getUniqueId());
    }
}
