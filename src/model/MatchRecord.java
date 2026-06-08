package model;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public class MatchRecord {
    private String id;
    private Team teamA;
    private Team teamB;
    
    private MatchResult result;
    
    private LocalDateTime matchDate;
    
    // Tracks which player picked which hero in this match (Player ID -> Hero ID)
    private Map<String, String> playerHeroPicks;

    // Minimal constructor to satisfy DataInitializer
    public MatchRecord(Team teamA, Team teamB, MatchResult result) {
        this.id = UUID.randomUUID().toString();
        this.teamA = teamA;
        this.teamB = teamB;
        this.result = result;
        this.matchDate = LocalDateTime.now();
        this.playerHeroPicks = new java.util.HashMap<>();
    }
    
    // File I/O constructor
    public MatchRecord(String id, Team teamA, Team teamB, MatchResult result, LocalDateTime matchDate, Map<String, String> playerHeroPicks) {
        this.id = id;
        this.teamA = teamA;
        this.teamB = teamB;
        this.result = result;
        this.matchDate = matchDate;
        this.playerHeroPicks = playerHeroPicks != null ? playerHeroPicks : new java.util.HashMap<>();
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

    public MatchResult getResult() {
        return result;
    }

    public void setResult(MatchResult result) {
        this.result = result;
    }

    public LocalDateTime getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(LocalDateTime matchDate) {
        this.matchDate = matchDate;
    }

    public Map<String, String> getPlayerHeroPicks() {
        return playerHeroPicks;
    }

    public void setPlayerHeroPicks(Map<String, String> playerHeroPicks) {
        this.playerHeroPicks = playerHeroPicks;
    }

    public void addPick(String playerId, String heroId) {
        this.playerHeroPicks.put(playerId, heroId);
    }
}