package model;

import java.util.List;
import java.util.UUID;

public class Team {
    private String id;
    private String name;

    // Aggregation: Team contains Players
    private List<Player> members;

    // Team statistics
    private int totalMatches;
    private int wins;

    // Minimal constructor to satisfy DataInitializer
    public Team(String name, List<Player> members) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.members = members;
        this.totalMatches = 0;
        this.wins = 0;

        // Link the players to this team
        for (Player player : members) {
            player.setOwnTeam(this);
        }
    }

    // Architectural skeleton for future calculation methods
    public double calculateAverageLevel() {
        double averageLevel = 0.0;
        for (Player player : members) {
            averageLevel += player.getLevel();
        }
        return averageLevel / members.size();
    }

    public double calculateWinRate() {
        double winRate = 0.0;
        return winRate / totalMatches;
    }

    public Player getTopPlayer() {
        Player topPlayer = members.get(0);
        for (Player player : members) {
            if (player.getLevel() > topPlayer.getLevel()) {
                topPlayer = player;
            }
        }
        return topPlayer;
    }
    // File I/O constructor
    public Team(String id, String name, List<Player> members, int totalMatches, int wins) {
        this.id = id;
        this.name = name;
        this.members = members;
        this.totalMatches = totalMatches;
        this.wins = wins;
        for (Player player : this.members) {
            player.setOwnTeam(this);
        }
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public List<Player> getMembers() {
        return members;
    }

    public void setMembers(List<Player> members) {
        this.members = members;
    }

    public int getTotalMatches() {
        return totalMatches;
    }

    public void setTotalMatches(int totalMatches) {
        this.totalMatches = totalMatches;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }
}