package service;

import model.*;
import java.util.ArrayList;
import java.util.List;

public class SearchService {

    private final GameDataManager dataManager;

    public SearchService() {
        this.dataManager = GameDataManager.getInstance();
    }

    public Player findPlayerByIdOrName(String query) {
        if (query == null || query.trim().isEmpty()) {
            return null;
        }
        for (Player player : dataManager.getPlayers()) {
            if (player.getId().equalsIgnoreCase(query) || player.getName().equalsIgnoreCase(query)) {
                return player;
            }
        }
        return null;
    }

    public Team findTeamByIdOrName(String query) {
        if (query == null || query.trim().isEmpty()) {
            return null;
        }
        for (Team team : dataManager.getTeams()) {
            if (team.getId().equalsIgnoreCase(query) || team.getName().equalsIgnoreCase(query)) {
                return team;
            }
        }
        return null;
    }

    public Hero findHeroByIdOrName(String query) {
        if (query == null || query.trim().isEmpty()) {
            return null;
        }
        for (Hero hero : dataManager.getHeroes()) {
            if (hero.getId().equalsIgnoreCase(query) || hero.getName().equalsIgnoreCase(query)) {
                return hero;
            }
        }
        return null;
    }

    public Equipment findEquipmentByIdOrName(String query) {
        if (query == null || query.trim().isEmpty()) {
            return null;
        }
        for (Equipment equipment : dataManager.getEquipmentList()) {
            if (equipment.getId().equalsIgnoreCase(query) || equipment.getName().equalsIgnoreCase(query)) {
                return equipment;
            }
        }
        return null;
    }

    public MatchRecord findMatchRecordById(String query) {
        if (query == null || query.trim().isEmpty()) {
            return null;
        }
        for (MatchRecord record: dataManager.getMatchRecords()) {
            if (record.getId().equalsIgnoreCase(query)) {
                return record;
            }
        }
        return null;
    }

    public List<MatchRecord> getMatchHistoryForPlayer(String playerId, int limit) {
        List<MatchRecord> history = new ArrayList<>();
        
        // Loop backwards to get the most recent matches
        List<MatchRecord> allMatches = dataManager.getMatchRecords();
        for (int i = allMatches.size() - 1; i >= 0; i--) {
            MatchRecord record = allMatches.get(i);
            // Check if the player participated in this match, regardless of their current team.
            if (record.getPlayerHeroPicks().containsKey(playerId)) {
                history.add(record);
                if (history.size() >= limit) {
                    break;
                }
            }
        }
        return history;
    }

    public List<MatchRecord> getMatchHistoryForTeam(String teamId, int limit) {
        List<MatchRecord> history = new ArrayList<>();

        // Loop backwards to get the most recent matches (assuming appended in chronological order)
        List<MatchRecord> allMatches = dataManager.getMatchRecords();
        for (int i = allMatches.size() - 1; i >= 0; i--) {
            MatchRecord record = allMatches.get(i);
            if (record.getTeamA().getId().equals(teamId) || record.getTeamB().getId().equals(teamId)) {
                history.add(record);
                if (history.size() >= limit) {
                    break;
                }
            }
        }
        return history;
    }

public List<Player> findPlayersByHero(String heroId) {
        List<Player> playersWithHero = new ArrayList<>();
        
        if (heroId == null || heroId.trim().isEmpty()) {
            return playersWithHero;
        }

        for (Player player : dataManager.getPlayers()) {
            for (Hero hero : player.getOwnedHeroes()) {
                if (hero.getId().equals(heroId)) {
                    playersWithHero.add(player);
                    break;
                }
            }
        }
        
        return playersWithHero;
    }

    public double calculateHeroPickRate(List<MatchRecord> matches, String heroId) {
        if (matches == null || matches.isEmpty() || heroId == null || heroId.trim().isEmpty()) {
            return 0.0;
        }

        int totalPicksAcrossMatches = 0;
        int heroPickCount = 0;

        for (MatchRecord match : matches) {
            for (String pickedHeroId : match.getPlayerHeroPicks().values()) {
                totalPicksAcrossMatches++;
                if (pickedHeroId.equals(heroId)) {
                    heroPickCount++;
                }
            }
        }

        if (totalPicksAcrossMatches == 0) {
            return 0.0;
        }

        return ((double) heroPickCount / totalPicksAcrossMatches) * 100.0;
    }
}