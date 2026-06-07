package service;

import java.util.ArrayList;
import java.util.List;

import model.Equipment;
import model.Player;

public class RankingService {

    private final GameDataManager dataManager;

    public RankingService() {
        this.dataManager = GameDataManager.getInstance();

    }

    public List<Equipment> getRankedEquipment() {
        List<Equipment> rankedList = new ArrayList<Equipment> (dataManager.getEquipmentList());
        // Sort by win rate descending, then by usage count descending
        rankedList.sort((e1, e2) -> {
            int usageCompare = Double.compare(e2.getWinRateContribution(), e1.getWinRateContribution());
            if (usageCompare != 0) return usageCompare;
            return Integer.compare(e2.getUsageCount(), e1.getUsageCount());
        });
        return rankedList;
    }

    public List<Player> getPlayerLeaderboard(int topX) {
        List<Player> rankedPlayers = new ArrayList<Player>(dataManager.getPlayers());
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


}