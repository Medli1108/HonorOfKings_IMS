package model;

import java.util.ArrayList;
import java.util.List;

public class Player extends Person {
    private double winRate;
    private int level;
    private List<Hero> ownedHeroes;
    private Team ownTeam;

    public Player(String name) {
        super(name, Role.PLAYER);
        ownedHeroes = new ArrayList<>();
        this.level = 0;
        this.winRate = 0.0;
    }

    // Constructor for loading an existing player from storage
    public Player(String id, String name, double winRate, int level) {
        super(id, name, Role.PLAYER);
        this.ownedHeroes = new ArrayList<>();
        this.winRate = winRate;
        this.level = level;
    }

    public void addHero(Hero hero) {
        this.ownedHeroes.add(hero);
    }

    public List<Hero> getOwnedHeroes() {
        return ownedHeroes;
    }

    public double getWinRate() {
        return winRate;
    }

    public void setWinRate(double winRate) {
        this.winRate = winRate;
    }

    public int getLevel() {
        return this.level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Team getOwnTeam() {
        return ownTeam;
    }

    public void setOwnTeam(Team ownTeam) {
        this.ownTeam = ownTeam;
    }

}