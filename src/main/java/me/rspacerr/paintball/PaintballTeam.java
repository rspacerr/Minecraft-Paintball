package me.rspacerr.paintball;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

public class PaintballTeam {
    private Set<PaintballPlayer> players;
    private String name;

    public PaintballTeam(String name) {
        this.name = name;
    }

    public void addPlayer(PaintballPlayer p) {
        players.add(p);
    }

    public void removePlayer(PaintballPlayer p) {
        players.remove(p);
    }

    public Collection<PaintballPlayer> players() {
        return Collections.unmodifiableCollection(players);
    }

    public String name() { return name; }
}
