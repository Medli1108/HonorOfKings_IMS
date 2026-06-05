package model;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public class MatchRecord {
    private String id;
    private Team teamA;
    private Team teamB;
    
    // Could eventually be an enum (MatchResult), but keeping String for DataInitializer compat
    private String result; 
    
    private LocalDateTime matchDate;
    
    // Future architectural consideration: Track which player picked which hero in this match
    // private Map<Player, Hero> picks;

    // Minimal constructor to satisfy DataInitializer
    public MatchRecord(Team teamA, Team teamB, String result) {
        this.id = UUID.randomUUID().toString();
        this.teamA = teamA;
        this.teamB = teamB;
        this.result = result;
        this.matchDate = LocalDateTime.now();
    }

    // TODO: Implementation Agent to add getters, setters, and File I/O constructors
}