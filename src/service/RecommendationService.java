package service;

import model.Equipment;
import model.Hero;
import model.Player;
import java.util.ArrayList;
import java.util.List;

public class RecommendationService {

    private final GameDataManager dataManager;

    public RecommendationService() {
        this.dataManager = GameDataManager.getInstance();
    }

    /**
     * Recommends equipment for a specific player and hero.
     * * FORMULA EXPLANATION:
     * The recommendation score is calculated out of a maximum of 100 points:
     * 1. Win Rate (Up to 50 points): The equipment's win rate multiplied by 50.
     * 2. Usage Popularity (Up to 30 points): The equipment's usage count divided by the 
     * maximum usage count across all equipment, multiplied by 30.
     * 3. Compatibility Bonus (20 points): A flat 20-point bonus if the item is listed 
     * in the specific hero's compatible equipments list.
     */
    public List<Equipment> recommendEquipment(Player player, Hero hero, int limit) {
        List<Equipment> allEquipments = dataManager.getEquipmentList();
        
        int maxUsage = 0;
        for (Equipment eq : allEquipments) {
            if (eq.getUsageCount() > maxUsage) {
                maxUsage = eq.getUsageCount();
            }
        }
        
        final int finalMaxUsage = maxUsage == 0 ? 1 : maxUsage;

        List<Equipment> recommendedList = new ArrayList<>(allEquipments);
        
        recommendedList.sort((e1, e2) -> {
            double score1 = calculateScore(e1, hero, finalMaxUsage);
            double score2 = calculateScore(e2, hero, finalMaxUsage);
            return Double.compare(score2, score1);
        });

        if (limit >= recommendedList.size()) {
            return recommendedList;
        }
        return new ArrayList<>(recommendedList.subList(0, limit));
    }

    private double calculateScore(Equipment eq, Hero hero, int maxUsage) {
        double winRateScore = eq.getWinRate() * 50.0;
        double usageScore = ((double) eq.getUsageCount() / maxUsage) * 30.0;
        double compatibilityBonus = 0.0;
        
        if (hero != null) {
            for (Equipment compEq : hero.getCompatibleEquipments()) {
                if (compEq.getId().equals(eq.getId())) {
                    compatibilityBonus = 20.0;
                    break;
                }
            }
        }
        
        return winRateScore + usageScore + compatibilityBonus;
    }
}