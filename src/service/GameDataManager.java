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
        // TODO: Adapt DataInitializer to populate these lists
        DataInitializer.initialize(); 
    }

    public void loadDataFromFile() {
        // TODO: Implement File I/O loading (CSV/JSON)
    }

    public void saveDataToFile() {
        // TODO: Implement File I/O saving (CSV/JSON)
    }

    // --- 2. Statistics & Leaderboards ---

    public List<Equipment> getRankedEquipment() {
        // TODO: Implement sorting logic based on usage/win-rate/custom score
        return new ArrayList<>();
    }

    public List<Player> getPlayerLeaderboard(int topX) {
        // TODO: Implement sorting logic for top players (win rate/level)
        return new ArrayList<>();
    }

    // --- 3. CRUD Operations (Admin Data Management) ---

    public void addPlayer(Player player) {
        players.add(player);
    }

    public boolean removePlayer(String playerId) {
        // TODO: Implement removal logic
        return false;
    }

    public void updatePlayer(Player updatedPlayer) {
        // TODO: Implement update logic
    }

    // --- 4. Authentication ---

    public Person authenticateUser(String nameOrId) {
        // TODO: Check against Admins, then Players. Return the authenticated Person.
        return null;
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