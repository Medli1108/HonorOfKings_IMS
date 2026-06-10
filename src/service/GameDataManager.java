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

    public synchronized void addMatchRecord(MatchRecord record) {
        if (record != null) {
            matchRecords.add(record);

            Team teamA = record.getTeamA();
            Team teamB = record.getTeamB();
            
            if (teamA != null) {
                updateTeamPostMatch(teamA, record, record.getResult() == MatchResult.TEAM_A_WIN, 1, 1);
            }

            if (teamB != null) {
                updateTeamPostMatch(teamB, record, record.getResult() == MatchResult.TEAM_B_WIN, 1, 1);
            }
        }
    }

    private void updateTeamPostMatch(Team team, MatchRecord record, boolean isWinner, int matchDelta, int winDelta) {
        team.setTotalMatches(Math.max(0, team.getTotalMatches() + matchDelta));
        if (isWinner) {
            team.setWins(Math.max(0, team.getWins() + winDelta));
        }
        
        synchronized (team.getMembers()) {
            for (Player player : team.getMembers()) {
                player.setTotalMatches(Math.max(0, player.getTotalMatches() + matchDelta));
                if (isWinner) {
                    player.setWins(Math.max(0, player.getWins() + winDelta));
                }
                player.setWinRate(player.getTotalMatches() > 0 ? (double)player.getWins() / player.getTotalMatches() : 0.0);
                
                String pickedHeroId = record.getPlayerHeroPicks().get(player.getId());
                if (pickedHeroId != null) {
                    for (Hero hero : player.getOwnedHeroes()) {
                        if (hero.getId().equals(pickedHeroId)) {
                            for (Equipment equipment : hero.getCurrentEquipments()) {
                                equipment.setUsageCount(Math.max(0, equipment.getUsageCount() + matchDelta));
                                if (isWinner) {
                                    equipment.setWins(Math.max(0, equipment.getWins() + winDelta));
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
                    hero.getCurrentEquipments().removeIf(e -> e.getId().equals(equipmentId));
                }
            }
        }
        return removed;
    }

    public boolean removeTeam(String teamId) {
        // Verify team exists before we begin cascading deletes
        boolean teamExists = false;
        synchronized (teams) {
            for (Team t : teams) {
                if (t.getId().equals(teamId)) {
                    teamExists = true;
                    break;
                }
            }
        }
        
        if (!teamExists) {
            return false;
        }

        // 1. Identify all matches involving this team
        List<String> matchesToRemove = new ArrayList<>();
        synchronized (matchRecords) {
            for (MatchRecord m : matchRecords) {
                if ((m.getTeamA() != null && m.getTeamA().getId().equals(teamId)) || 
                    (m.getTeamB() != null && m.getTeamB().getId().equals(teamId))) {
                    matchesToRemove.add(m.getId());
                }
            }
        }

        // 2. Safely rollback stats for all matches involving this team using the existing rigorous logic
        for (String matchId : matchesToRemove) {
            removeMatchRecord(matchId);
        }

        // 3. Remove the team itself
        boolean removed = teams.removeIf(t -> t.getId().equals(teamId));
        if (removed) {
            synchronized (players) {
                for (Player player : players) {
                    if (player.getOwnTeam() != null && player.getOwnTeam().getId().equals(teamId)) {
                        player.setOwnTeam(null);
                    }
                }
            }
        }
        return removed;
    }

    public synchronized boolean removeMatchRecord(String recordId) {
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
                    updateTeamPostMatch(teamA, targetRecord, targetRecord.getResult() == MatchResult.TEAM_A_WIN, -1, -1);
                }
                if (teamB != null && teams.contains(teamB)) {
                    updateTeamPostMatch(teamB, targetRecord, targetRecord.getResult() == MatchResult.TEAM_B_WIN, -1, -1);
                }
            }
            return true;
        }
        
        return false;
    }

    public void updatePlayer(Player updatedPlayer) {
        synchronized (players) {
            for (Player existingPlayer : players) {
                if (existingPlayer.getId().equals(updatedPlayer.getId())) {
                    // Update properties while keeping the exact same memory reference
                    existingPlayer.setName(updatedPlayer.getName());
                    existingPlayer.setLevel(updatedPlayer.getLevel());
                    // The existingPlayer's ownTeam and ownedHeroes references stay intact
                    return;
                }
            }
        }
    }

    public void updateHero(Hero updatedHero) {
        synchronized (heroes) {
            for (Hero existingHero : heroes) {
                if (existingHero.getId().equals(updatedHero.getId())) {
                    existingHero.setName(updatedHero.getName());
                    existingHero.setType(updatedHero.getType());
                    existingHero.setBaseHp(updatedHero.getBaseHp());
                    existingHero.setBaseAttack(updatedHero.getBaseAttack());
                    // References to equipment lists stay intact, but you could clear and re-add if full replacement is desired:
                    // existingHero.getCompatibleEquipments().clear();
                    // existingHero.getCompatibleEquipments().addAll(updatedHero.getCompatibleEquipments());
                    return;
                }
            }
        }
    }

    public void updateEquipment(Equipment updatedEquipment) {
        synchronized (equipmentList) {
            for (Equipment existingEquipment : equipmentList) {
                if (existingEquipment.getId().equals(updatedEquipment.getId())) {
                    existingEquipment.setName(updatedEquipment.getName());
                    existingEquipment.setAverageRating(updatedEquipment.getAverageRating());
                    return;
                }
            }
        }
    }

    public void updateTeam(Team updatedTeam) {
        synchronized (teams) {
            for (Team existingTeam : teams) {
                if (existingTeam.getId().equals(updatedTeam.getId())) {
                    existingTeam.setName(updatedTeam.getName());
                    // We don't overwrite members list directly to prevent ghost reference bugs
                    return;
                }
            }
        }
    }

    public synchronized void updateMatchRecord(MatchRecord updatedRecord) {
        synchronized (matchRecords) {
            MatchRecord existingRecord = null;
            for (MatchRecord m : matchRecords) {
                if (m.getId().equals(updatedRecord.getId())) {
                    existingRecord = m;
                    break;
                }
            }
            
            if (existingRecord != null) {
                // 1. Rollback old stats
                removeMatchRecord(existingRecord.getId());
                
                // 2. Update record data
                existingRecord.setResult(updatedRecord.getResult());
                existingRecord.setMatchDate(updatedRecord.getMatchDate());
                existingRecord.setTeamA(updatedRecord.getTeamA());
                existingRecord.setTeamB(updatedRecord.getTeamB());
                existingRecord.getPlayerHeroPicks().clear();
                existingRecord.getPlayerHeroPicks().putAll(updatedRecord.getPlayerHeroPicks());
                
                // 3. Re-apply the stats with new data
                addMatchRecord(existingRecord);
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

