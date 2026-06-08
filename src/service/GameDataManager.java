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
    public static synchronized GameDataManager getInstance() {
        if (instance == null) {
            instance = new GameDataManager();
        }
        return instance;
    }

    // Initialization & Persistence 
    
    public void initializeDummyData() {
        DataInitializer.initialize(); 
    }

    public void loadDataFromFile() {
        FileStorageService storageService = new FileStorageService();
        storageService.loadData(this);
    }

    public void saveDataToFile() {
        FileStorageService storageService = new FileStorageService();
        storageService.saveData(this);
    }


    // CRUD Operations (Admin Data Management)

    public void addPlayer(Player player) {
        if (player != null) {
            players.add(player);
        }
    }

    public boolean removePlayer(String playerId) {
        return players.removeIf(p -> p.getId().equals(playerId));
    }

    public boolean removeHero(String heroId) {
        boolean removed = heroes.removeIf(h -> h.getId().equals(heroId));
        if (removed) {
            for (Player player : players) {
                player.getOwnedHeroes().removeIf(h -> h.getId().equals(heroId));
            }
        }
        return removed;
    }

    public boolean removeEquipment(String equipmentId) {
        boolean removed = equipmentList.removeIf(e -> e.getId().equals(equipmentId));
        if (removed) {
            for (Hero hero : heroes) {
                hero.getCompatibleEquipments().removeIf(e -> e.getId().equals(equipmentId));
                hero.getRecommendedEquipments().removeIf(e -> e.getId().equals(equipmentId));
            }
        }
        return removed;
    }

    public void updatePlayer(Player updatedPlayer) {
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getId().equals(updatedPlayer.getId())) {
                players.set(i, updatedPlayer);
                return;
            }
        }
    }

    // Getters and Setters for Data Access
    
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