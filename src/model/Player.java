package model;

import java.util.ArrayList;
import java.util.List;

public class Player extends Person {
    private double winRate;
    private int level;
    private List<Hero> ownedHeroes;
    private Team ownTeam;

    public Player(String name) {
        super(name, false);
        ownedHeroes = new ArrayList<>();
        this.level = 0;
        this.winRate = 0.0;

    }

    public void addHero(Hero hero) {
        this.ownedHeroes.add(hero);
    }

    public List<Hero> getownedHeroes() {
        return ownedHeroes;
    }

    public double getWinRate() {
        return winRate;
    }

    public int getlevel() {
        return this.level;
    }

    public Team getownedTeam() {
        return ownTeam;
    }





}