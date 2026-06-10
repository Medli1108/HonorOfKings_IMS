package service;

import model.Hero;
import model.Equipment;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CombatSimulatorService {
    
    private final Random random;

    public CombatSimulatorService() {
        this.random = new Random();
    }

    public List<String> simulateCombat(Hero playerHero, Hero opponentHero) {
        List<String> report = new ArrayList<>();
        report.add("=========================================");
        report.add("         ⚔️ COMBAT SIMULATION ⚔️        ");
        report.add("=========================================");
        report.add(playerHero.getName() + " VS " + opponentHero.getName());
        
        // Calculate effective stats using base stats + equipment ratings
        double hp1 = playerHero.getBaseHp() + getEquipmentHpBonus(playerHero);
        double atk1 = playerHero.getBaseAttack() + getEquipmentAtkBonus(playerHero);
        
        double hp2 = opponentHero.getBaseHp() + getEquipmentHpBonus(opponentHero);
        double atk2 = opponentHero.getBaseAttack() + getEquipmentAtkBonus(opponentHero);

        report.add(String.format("%s [Effective HP: %.0f | Effective ATK: %.0f]", playerHero.getName(), hp1, atk1));
        report.add(String.format("%s [Effective HP: %.0f | Effective ATK: %.0f]", opponentHero.getName(), hp2, atk2));
        report.add("-----------------------------------------");

        int turn = 1;
        while (hp1 > 0 && hp2 > 0) {
            report.add("\n--- Turn " + turn + " ---");
            
            // Player's Hero attacks Opponent
            hp2 -= executeAttack(playerHero.getName(), atk1, opponentHero.getName(), report);
            if (hp2 <= 0) break;
            
            // Opponent attacks Player's Hero
            hp1 -= executeAttack(opponentHero.getName(), atk2, playerHero.getName(), report);
            
            turn++;
            if (turn > 50) { 
                report.add("\nThe battle dragged on too long and ended in a draw out of exhaustion!");
                break;
            }
        }

        report.add("\n-----------------------------------------");
        report.add("              🏁 RESULT 🏁               ");
        report.add("-----------------------------------------");
        if (hp1 > 0 && hp2 <= 0) {
            report.add("WINNER: " + playerHero.getName() + " stands victorious!");
        } else if (hp2 > 0 && hp1 <= 0) {
            report.add("WINNER: " + opponentHero.getName() + " stands victorious!");
        } else {
            report.add("WINNER: It's a draw!");
        }

        return report;
    }

    private double executeAttack(String attacker, double attackerAtk, String defender, List<String> report) {
        // Dodge calculation: 15% chance to dodge
        if (random.nextDouble() < 0.15) {
            report.add(defender + " nimbly DODGED the attack from " + attacker + "!");
            return 0;
        }

        double damage = attackerAtk;
        boolean isCrit = false;

        // Critical hit calculation: 20% chance for 1.5x damage
        if (random.nextDouble() < 0.20) {
            damage *= 1.5; 
            isCrit = true;
        }

        // Damage variation (+/- 15%)
        double variation = 0.85 + (0.30 * random.nextDouble());
        damage = Math.round(damage * variation);

        if (isCrit) {
            report.add("💥 CRITICAL HIT! " + attacker + " strikes " + defender + " for " + (int)damage + " damage!");
        } else {
            report.add("⚔️ " + attacker + " attacks " + defender + " for " + (int)damage + " damage.");
        }

        return damage;
    }

    private double getEquipmentHpBonus(Hero hero) {
        double bonus = 0;
        if (hero.getCurrentEquipments() != null) {
            for (Equipment eq : hero.getCurrentEquipments()) {
                // 50 HP per point of average rating
                bonus += eq.getAverageRating() * 50; 
            }
        }
        return bonus;
    }

    private double getEquipmentAtkBonus(Hero hero) {
        double bonus = 0;
        if (hero.getCurrentEquipments() != null) {
            for (Equipment eq : hero.getCurrentEquipments()) {
                // 5 ATK per point of average rating
                bonus += eq.getAverageRating() * 5; 
            }
        }
        return bonus;
    }
}
