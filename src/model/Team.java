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
        // TODO: Implementation Agent to implement
        return 0.0;
    }

    public double calculateWinRate() {
        // TODO: Implementation Agent to implement
        return 0.0;
    }

    public Player getTopPlayer() {
        // TODO: Implementation Agent to implement based on winRate or level
        return null;
    }

    // TODO: Implementation Agent to add getters, setters, and File I/O constructors
}