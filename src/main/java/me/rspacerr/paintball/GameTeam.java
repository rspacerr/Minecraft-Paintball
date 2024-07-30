package me.rspacerr.paintball;

import me.rspacerr.paintball.players.GamePlayer;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

public class GameTeam {
    private Set<GamePlayer> players;
    private String name;

    public GameTeam(String name) {
        this.name = name;
    }

    public void addPlayer(GamePlayer p) {
        players.add(p);
    }

    public void removePlayer(GamePlayer p) {
        players.remove(p);
    }

    public Collection<GamePlayer> players() {
        return Collections.unmodifiableCollection(players);
    }

    public String name() { return name; }
}
