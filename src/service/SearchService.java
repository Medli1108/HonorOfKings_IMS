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
        synchronized (dataManager.getPlayers()) {
            for (Player player : dataManager.getPlayers()) {
                if (player.getId().equalsIgnoreCase(query) || player.getName().equalsIgnoreCase(query)) {
                    return player;
                }
            }
        }
        return null;
    }

    public Team findTeamByIdOrName(String query) {
        if (query == null || query.trim().isEmpty()) {
            return null;
        }
        synchronized (dataManager.getTeams()) {
            for (Team team : dataManager.getTeams()) {
                if (team.getId().equalsIgnoreCase(query) || team.getName().equalsIgnoreCase(query)) {
                    return team;
                }
            }
        }
        return null;
    }

    public Hero findHeroByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return null;
        }
        synchronized (dataManager.getHeroes()) {
            for (Hero hero : dataManager.getHeroes()) {
                if (hero.getName().equalsIgnoreCase(name)) {
                    return hero;
                }
            }
        }
        return null;
    }

    public List<MatchRecord> getMatchHistoryForPlayer(String playerId, int limit) {
        List<MatchRecord> history = new ArrayList<>();
        Player player = findPlayerByIdOrName(playerId);
        
        if (player == null || player.getOwnTeam() == null) {
            return history;
        }
        
        Team playerTeam = player.getOwnTeam();
        return getMatchHistoryForTeam(playerTeam.getId(), limit);
    }

    public List<MatchRecord> getMatchHistoryForTeam(String teamId, int limit) {
        List<MatchRecord> history = new ArrayList<>();
        
        // Loop backwards to get the most recent matches (assuming appended in chronological order)
        List<MatchRecord> allMatches = dataManager.getMatchRecords();
        synchronized (allMatches) {
            for (int i = allMatches.size() - 1; i >= 0; i--) {
                MatchRecord record = allMatches.get(i);
                if (record.getTeamA().getId().equals(teamId) || record.getTeamB().getId().equals(teamId)) {
                    history.add(record);
                    if (history.size() >= limit) {
                        break;
                    }
                }
            }
        }
        return history;
    }
}