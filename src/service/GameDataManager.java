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
        this.players = java.util.Collections.synchronizedList(new ArrayList<>());
        this.admins = java.util.Collections.synchronizedList(new ArrayList<>());
        this.heroes = java.util.Collections.synchronizedList(new ArrayList<>());
        this.equipmentList = java.util.Collections.synchronizedList(new ArrayList<>());
        this.teams = java.util.Collections.synchronizedList(new ArrayList<>());
        this.matchRecords = java.util.Collections.synchronizedList(new ArrayList<>());
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

    public void addHero(Hero hero) {
        if (hero != null) {
            heroes.add(hero);
        }
    }

    public void addEquipment(Equipment equipment) {
        if (equipment != null) {
            equipmentList.add(equipment);
        }
    }

    public void addTeam(Team team) {
        if (team != null) {
            teams.add(team);
        }
    }

    public void addMatchRecord(MatchRecord record) {
        if (record != null) {
            matchRecords.add(record);

            Team teamA = record.getTeamA();
            Team teamB = record.getTeamB();
            
            if (teamA != null) {
                teamA.setTotalMatches(teamA.getTotalMatches() + 1);
                for (Player player : teamA.getMembers()) {
                    player.setTotalMatches(player.getTotalMatches() + 1);
                    player.setWinRate(player.getTotalMatches() > 0 ? (double)player.getWins() / player.getTotalMatches() : 0.0);
                    
                    String pickedHeroId = record.getPlayerHeroPicks().get(player.getId());
                    if (pickedHeroId != null) {
                        for (Hero hero : player.getOwnedHeroes()) {
                            if (hero.getId().equals(pickedHeroId)) {
                                for (Equipment equipment : hero.getCurrentEquipments()) {
                                    equipment.setUsageCount(equipment.getUsageCount() + 1);
                                    equipment.setWinRate(equipment.getUsageCount() > 0 ? (double)equipment.getWins() / equipment.getUsageCount() : 0.0);
                                }
                                break;
                            }
                        }
                    }
                }
            }

            if (teamB != null) {
                teamB.setTotalMatches(teamB.getTotalMatches() + 1);
                for (Player player : teamB.getMembers()) {
                    player.setTotalMatches(player.getTotalMatches() + 1);
                    player.setWinRate(player.getTotalMatches() > 0 ? (double)player.getWins() / player.getTotalMatches() : 0.0);
                    
                    String pickedHeroId = record.getPlayerHeroPicks().get(player.getId());
                    if (pickedHeroId != null) {
                        for (Hero hero : player.getOwnedHeroes()) {
                            if (hero.getId().equals(pickedHeroId)) {
                                for (Equipment equipment : hero.getCurrentEquipments()) {
                                    equipment.setUsageCount(equipment.getUsageCount() + 1);
                                    equipment.setWinRate(equipment.getUsageCount() > 0 ? (double)equipment.getWins() / equipment.getUsageCount() : 0.0);
                                }
                                break;
                            }
                        }
                    }
                }
            }

            if (record.getResult() == MatchResult.TEAM_A_WIN && teamA != null) {
                teamA.setWins(teamA.getWins() + 1);
                for (Player player : teamA.getMembers()) {
                    player.setWins(player.getWins() + 1);
                    player.setWinRate(player.getTotalMatches() > 0 ? (double)player.getWins() / player.getTotalMatches() : 0.0);
                    
                    String pickedHeroId = record.getPlayerHeroPicks().get(player.getId());
                    if (pickedHeroId != null) {
                        for (Hero hero : player.getOwnedHeroes()) {
                            if (hero.getId().equals(pickedHeroId)) {
                                for (Equipment equipment : hero.getCurrentEquipments()) {
                                    equipment.setWins(equipment.getWins() + 1);
                                    equipment.setWinRate(equipment.getUsageCount() > 0 ? (double)equipment.getWins() / equipment.getUsageCount() : 0.0);
                                }
                                break;
                            }
                        }
                    }
                }
            } else if (record.getResult() == MatchResult.TEAM_B_WIN && teamB != null) {
                teamB.setWins(teamB.getWins() + 1);
                for (Player player : teamB.getMembers()) {
                    player.setWins(player.getWins() + 1);
                    player.setWinRate(player.getTotalMatches() > 0 ? (double)player.getWins() / player.getTotalMatches() : 0.0);
                    
                    String pickedHeroId = record.getPlayerHeroPicks().get(player.getId());
                    if (pickedHeroId != null) {
                        for (Hero hero : player.getOwnedHeroes()) {
                            if (hero.getId().equals(pickedHeroId)) {
                                for (Equipment equipment : hero.getCurrentEquipments()) {
                                    equipment.setWins(equipment.getWins() + 1);
                                    equipment.setWinRate(equipment.getUsageCount() > 0 ? (double)equipment.getWins() / equipment.getUsageCount() : 0.0);
                                }
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean removePlayer(String playerId) {
        boolean removed = players.removeIf(p -> p.getId().equals(playerId));
        if (removed) {
            synchronized (teams) {
                for (Team team : teams) {
                    team.getMembers().removeIf(p -> p.getId().equals(playerId));
                }
            }
            synchronized (matchRecords) {
                for (MatchRecord record : matchRecords) {
                    record.getPlayerHeroPicks().remove(playerId);
                }
            }
        }
        return removed;
    }

    public boolean removeHero(String heroId) {
        boolean removed = heroes.removeIf(h -> h.getId().equals(heroId));
        if (removed) {
            synchronized (players) {
                for (Player player : players) {
                    player.getOwnedHeroes().removeIf(h -> h.getId().equals(heroId));
                }
            }
            synchronized (matchRecords) {
                for (MatchRecord record : matchRecords) {
                    record.getPlayerHeroPicks().values().removeIf(id -> id.equals(heroId));
                }
            }
        }
        return removed;
    }

    public boolean removeEquipment(String equipmentId) {
        boolean removed = equipmentList.removeIf(e -> e.getId().equals(equipmentId));
        if (removed) {
            synchronized (heroes) {
                for (Hero hero : heroes) {
                    hero.getCompatibleEquipments().removeIf(e -> e.getId().equals(equipmentId));
                    hero.getRecommendedEquipments().removeIf(e -> e.getId().equals(equipmentId));
                }
            }
        }
        return removed;
    }

    public boolean removeTeam(String teamId) {
        boolean removed = teams.removeIf(t -> t.getId().equals(teamId));
        if (removed) {
            synchronized (players) {
                for (Player player : players) {
                    if (player.getOwnTeam() != null && player.getOwnTeam().getId().equals(teamId)) {
                        player.setOwnTeam(null);
                    }
                }
            }
            synchronized (matchRecords) {
                matchRecords.removeIf(m -> m.getTeamA().getId().equals(teamId) || m.getTeamB().getId().equals(teamId));
            }
        }
        return removed;
    }

    public boolean removeMatchRecord(String recordId) {
        MatchRecord targetRecord = null;
        
        synchronized (matchRecords) {
            for (MatchRecord m : matchRecords) {
                if (m.getId().equals(recordId)) {
                    targetRecord = m;
                    break;
                }
            }
            if (targetRecord != null) {
                matchRecords.remove(targetRecord);
            }
        }

        if (targetRecord != null) {
            Team teamA = targetRecord.getTeamA();
            Team teamB = targetRecord.getTeamB();

            synchronized (teams) {
                if (teamA != null && teams.contains(teamA)) {
                    teamA.setTotalMatches(Math.max(0, teamA.getTotalMatches() - 1));
                    if (targetRecord.getResult() == MatchResult.TEAM_A_WIN) {
                        teamA.setWins(Math.max(0, teamA.getWins() - 1));
                    }
                    
                    for (Player player : teamA.getMembers()) {
                        player.setTotalMatches(Math.max(0, player.getTotalMatches() - 1));
                        if (targetRecord.getResult() == MatchResult.TEAM_A_WIN) {
                            player.setWins(Math.max(0, player.getWins() - 1));
                        }
                        player.setWinRate(player.getTotalMatches() > 0 ? (double)player.getWins() / player.getTotalMatches() : 0.0);
                        
                        String pickedHeroId = targetRecord.getPlayerHeroPicks().get(player.getId());
                        if (pickedHeroId != null) {
                            for (Hero hero : player.getOwnedHeroes()) {
                                if (hero.getId().equals(pickedHeroId)) {
                                    for (Equipment equipment : hero.getCurrentEquipments()) {
                                        equipment.setUsageCount(Math.max(0, equipment.getUsageCount() - 1));
                                        if (targetRecord.getResult() == MatchResult.TEAM_A_WIN) {
                                            equipment.setWins(Math.max(0, equipment.getWins() - 1));
                                        }
                                        equipment.setWinRate(equipment.getUsageCount() > 0 ? (double)equipment.getWins() / equipment.getUsageCount() : 0.0);
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
                if (teamB != null && teams.contains(teamB)) {
                    teamB.setTotalMatches(Math.max(0, teamB.getTotalMatches() - 1));
                    if (targetRecord.getResult() == MatchResult.TEAM_B_WIN) {
                        teamB.setWins(Math.max(0, teamB.getWins() - 1));
                    }
                    
                    for (Player player : teamB.getMembers()) {
                        player.setTotalMatches(Math.max(0, player.getTotalMatches() - 1));
                        if (targetRecord.getResult() == MatchResult.TEAM_B_WIN) {
                            player.setWins(Math.max(0, player.getWins() - 1));
                        }
                        player.setWinRate(player.getTotalMatches() > 0 ? (double)player.getWins() / player.getTotalMatches() : 0.0);
                        
                        String pickedHeroId = targetRecord.getPlayerHeroPicks().get(player.getId());
                        if (pickedHeroId != null) {
                            for (Hero hero : player.getOwnedHeroes()) {
                                if (hero.getId().equals(pickedHeroId)) {
                                    for (Equipment equipment : hero.getCurrentEquipments()) {
                                        equipment.setUsageCount(Math.max(0, equipment.getUsageCount() - 1));
                                        if (targetRecord.getResult() == MatchResult.TEAM_B_WIN) {
                                            equipment.setWins(Math.max(0, equipment.getWins() - 1));
                                        }
                                        equipment.setWinRate(equipment.getUsageCount() > 0 ? (double)equipment.getWins() / equipment.getUsageCount() : 0.0);
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            return true;
        }
        
        return false;
    }

    public void updatePlayer(Player updatedPlayer) {
        synchronized (players) {
            for (int i = 0; i < players.size(); i++) {
                if (players.get(i).getId().equals(updatedPlayer.getId())) {
                    players.set(i, updatedPlayer);
                    return;
                }
            }
        }
    }

    public void updateHero(Hero updatedHero) {
        synchronized (heroes) {
            for (int i = 0; i < heroes.size(); i++) {
                if (heroes.get(i).getId().equals(updatedHero.getId())) {
                    heroes.set(i, updatedHero);
                    return;
                }
            }
        }
    }

    public void updateEquipment(Equipment updatedEquipment) {
        synchronized (equipmentList) {
            for (int i = 0; i < equipmentList.size(); i++) {
                if (equipmentList.get(i).getId().equals(updatedEquipment.getId())) {
                    equipmentList.set(i, updatedEquipment);
                    return;
                }
            }
        }
    }

    public void updateTeam(Team updatedTeam) {
        synchronized (teams) {
            for (int i = 0; i < teams.size(); i++) {
                if (teams.get(i).getId().equals(updatedTeam.getId())) {
                    teams.set(i, updatedTeam);
                    return;
                }
            }
        }
    }

    public void updateMatchRecord(MatchRecord updatedRecord) {
        synchronized (matchRecords) {
            for (int i = 0; i < matchRecords.size(); i++) {
                if (matchRecords.get(i).getId().equals(updatedRecord.getId())) {
                    matchRecords.set(i, updatedRecord);
                    return;
                }
            }
        }
    }

    // Getters and Setters for Data Access

    public List<Player> getPlayers() {
        synchronized (players) {
            return new ArrayList<>(players);
        }
    }

    public void setPlayers(List<Player> players) {
        this.players = java.util.Collections.synchronizedList(new ArrayList<>(players));
    }

    public List<Admin> getAdmins() {
        synchronized (admins) {
            return new ArrayList<>(admins);
        }
    }

    public void setAdmins(List<Admin> admins) {
        this.admins = java.util.Collections.synchronizedList(new ArrayList<>(admins));
    }

    public List<Hero> getHeroes() {
        synchronized (heroes) {
            return new ArrayList<>(heroes);
        }
    }

    public void setHeroes(List<Hero> heroes) {
        this.heroes = java.util.Collections.synchronizedList(new ArrayList<>(heroes));
    }

    public List<Equipment> getEquipmentList() {
        synchronized (equipmentList) {
            return new ArrayList<>(equipmentList);
        }
    }

    public void setEquipmentList(List<Equipment> equipmentList) {
        this.equipmentList= java.util.Collections.synchronizedList(new ArrayList<>(equipmentList));
    }

    public List<Team> getTeams() {
        synchronized (teams) {
            return new ArrayList<>(teams);
        }
    }

    public void setTeams(List<Team> teams) {
        this.teams = java.util.Collections.synchronizedList(new ArrayList<>(teams));
    }

    public List<MatchRecord> getMatchRecords() {
        synchronized (matchRecords) {
            return new ArrayList<>(matchRecords);
        }
    }

    public void setMatchRecords(List<MatchRecord> matchRecords) {
        this.matchRecords = java.util.Collections.synchronizedList(new ArrayList<>(matchRecords));
    }
}

