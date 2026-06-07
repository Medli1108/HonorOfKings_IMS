package service;

import model.*;
import util.DataInitializer;

import java.util.ArrayList;
import java.util.List;

public class GameDataManager {
    
    // --- Singleton Instance ---
    private static GameDataManager instance;

    // --- Central Data Repositories ---
    private List<Player> players;
    private List<Admin> admins;
    private List<Hero> heroes;
    private List<Equipment> equipmentList;
    private List<Team> teams;
    private List<MatchRecord> matchRecords;

    // Private constructor to prevent instantiation
    private GameDataManager() {
        this.players = new ArrayList<>();
        this.admins = new ArrayList<>();
        this.heroes = new ArrayList<>();
        this.equipmentList = new ArrayList<>();
        this.teams = new ArrayList<>();
        this.matchRecords = new ArrayList<>();
    }

    // Public method to get the singleton instance
    public static GameDataManager getInstance() {
        if (instance == null) {
            instance = new GameDataManager();
        }
        return instance;
    }

    // --- 1. Initialization & Persistence ---
    
    public void initializeDummyData() {
        DataInitializer.initialize(); 
    }

    public void loadDataFromFile() {
        // TODO: Implement File I/O loading (CSV/JSON)
        // This will be implemented in a future milestone when data formats are finalized.
        System.out.println("loadDataFromFile is not yet implemented.");
    }

    public void saveDataToFile() {
        // TODO: Implement File I/O saving (CSV/JSON)
        // This will be implemented in a future milestone when data formats are finalized.
        System.out.println("saveDataToFile is not yet implemented.");
    }

    // --- 2. Statistics & Leaderboards ---

    public List<Equipment> getRankedEquipment() {
        List<Equipment> rankedList = new ArrayList<>(equipmentList);
        // Sort by win rate descending, then by usage count descending
        rankedList.sort((e1, e2) -> {
            int usageCompare = Double.compare(e2.getWinRateContribution(), e1.getWinRateContribution());
            if (usageCompare != 0) return usageCompare;
            return Integer.compare(e2.getUsageCount(), e1.getUsageCount());
        });
        return rankedList;
    }

    public List<Player> getPlayerLeaderboard(int topX) {
        List<Player> rankedPlayers = new ArrayList<>(players);
        // Sort by win rate descending, then level descending
        rankedPlayers.sort((p1, p2) -> {
            int winRateCompare = Double.compare(p2.getWinRate(), p1.getWinRate());
            if (winRateCompare != 0) return winRateCompare;
            return Integer.compare(p2.getLevel(), p1.getLevel());
        });
        
        if (topX >= rankedPlayers.size()) {
            return rankedPlayers;
        }
        return rankedPlayers.subList(0, topX);
    }

    // --- 3. CRUD Operations (Admin Data Management) ---

    public void addPlayer(Player player) {
        if (player != null) {
            players.add(player);
        }
    }

    public boolean removePlayer(String playerId) {
        return players.removeIf(p -> p.getId().equals(playerId));
    }

    public void updatePlayer(Player updatedPlayer) {
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getId().equals(updatedPlayer.getId())) {
                players.set(i, updatedPlayer);
                return;
            }
        }
    }

    // --- Getters and Setters for Data Access ---
    
    public List<Player> getPlayers() { return players; }
    public void setPlayers(List<Player> players) { this.players = players; }
    
    public List<Admin> getAdmins() { return admins; }
    public void setAdmins(List<Admin> admins) { this.admins = admins; }
    
    public List<Hero> getHeroes() { return heroes; }
    public void setHeroes(List<Hero> heroes) { this.heroes = heroes; }
    
    public List<Equipment> getEquipmentList() { return equipmentList; }
    public void setEquipmentList(List<Equipment> equipmentList) { this.equipmentList = equipmentList; }
    
    public List<Team> getTeams() { return teams; }
    public void setTeams(List<Team> teams) { this.teams = teams; }
    
    public List<MatchRecord> getMatchRecords() { return matchRecords; }
    public void setMatchRecords(List<MatchRecord> matchRecords) { this.matchRecords = matchRecords; }
}