package model;

import java.time.LocalDateTime;
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
    
    // File I/O constructor
    public MatchRecord(String id, Team teamA, Team teamB, String result, LocalDateTime matchDate) {
        this.id = id;
        this.teamA = teamA;
        this.teamB = teamB;
        this.result = result;
        this.matchDate = matchDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Team getTeamA() {
        return teamA;
    }

    public void setTeamA(Team teamA) {
        this.teamA = teamA;
    }

    public Team getTeamB() {
        return teamB;
    }

    public void setTeamB(Team teamB) {
        this.teamB = teamB;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public LocalDateTime getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(LocalDateTime matchDate) {
        this.matchDate = matchDate;
    }
}