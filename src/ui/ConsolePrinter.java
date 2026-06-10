package ui;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import model.*;
import service.GameDataManager;
import service.RankingService;
import service.SearchService;

public class ConsolePrinter {
    private static final SearchService searchService = new SearchService();
    private static final RankingService rankingService = new RankingService();
    private static final GameDataManager dataManager = GameDataManager.getInstance();
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static void printPlayerDetails(Player player) {
        if (player == null) {
            System.out.println("Player not found.");
            return;
        }
        System.out.println("\n=== Player Details ===");
        System.out.println("ID: " + player.getId());
        System.out.println("Name: " + player.getName());
        System.out.println("Team: " + (player.getOwnTeam() != null ? player.getOwnTeam().getName() : "None"));
        System.out.println("Level: " + player.getLevel());
        System.out.printf("Win Rate: %.2f%%\n", player.getWinRate() * 100);
        System.out.println("Total Matches: " + player.getTotalMatches());
        System.out.println("Wins: " + player.getWins());

        System.out.println("Owned Heroes:");
        if (player.getOwnedHeroes().isEmpty()) {
            System.out.println("  None");
        } else {
            for (Hero hero : player.getOwnedHeroes()) {
                System.out.println("  - " + hero.getName() + " (" + hero.getType() + ")");
                System.out.println("    Equipped Items:");
                if (hero.getCurrentEquipments().isEmpty()) {
                    System.out.println("      None");
                } else {
                    for (Equipment eq : hero.getCurrentEquipments()) {
                        System.out.println("      * " + eq.getName());
                    }
                }
            }
        }
        System.out.println("======================");
    }

    public static void printTeamDetails(Team team) {
        if (team == null) {
            System.out.println("Team not found.");
            return;
        }
        System.out.println("\n=== Team Overview ===");
        System.out.println("ID: " + team.getId());
        System.out.println("Name: " + team.getName());
        System.out.printf("Average Level: %.1f\n", team.calculateAverageLevel());
        System.out.println("Total Matches: " + team.getTotalMatches());
        System.out.printf("Win Rate: %.2f%%\n", team.calculateWinRate() * 100);

        Player topPlayer = team.getTopPlayer();
        System.out.println("Top Player: " + (topPlayer != null ? topPlayer.getName() : "N/A"));

        System.out.println("Members:");
        if (team.getMembers().isEmpty()) {
            System.out.println("  None");
        } else {
            for (Player member : team.getMembers()) {
                System.out.printf("  - %s (Level %d, Win Rate: %.2f%%)\n",
                        member.getName(), member.getLevel(), member.getWinRate() * 100);
            }
        }
        System.out.println("=====================");
    }

    public static void printHeroDetails(Hero hero) {
        if (hero == null) {
            System.out.println("Hero not found.");
            return;
        }
        System.out.println("\n=== Hero Details ===");
        System.out.println("Name: " + hero.getName());
        System.out.println("Type: " + hero.getType());
        System.out.println("Base HP: " + hero.getBaseHp());
        System.out.println("Base Attack: " + hero.getBaseAttack());

        System.out.println("\nCompatible Equipment:");
        if (hero.getCompatibleEquipments().isEmpty()) {
            System.out.println("  None");
        } else {
            for (Equipment eq : hero.getCompatibleEquipments()) {
                System.out.println("  - " + eq.getName());
            }
        }

        System.out.println("\nRecommended Equipment:");
        if (hero.getRecommendedEquipments().isEmpty()) {
            System.out.println("  None");
        } else {
            for (Equipment eq : hero.getRecommendedEquipments()) {
                System.out.println("  - " + eq.getName());
            }
        }

        List<Player> owningPlayers = searchService.findPlayersByHero(hero.getId());
        System.out.println("\nPlayers who own this hero: " + owningPlayers.size());
        if (!owningPlayers.isEmpty()) {
            String playerNames = owningPlayers.stream()
                    .map(Player::getName)
                    .collect(Collectors.joining(", "));
            System.out.println("  " + playerNames);
        }
        System.out.println("====================");
    }

    public static void printEquipmentDetails(Equipment eq) {
        if (eq == null) {
            System.out.println("Equipment not found.");
            return;
        }
        System.out.println("\n=== Equipment Details ===");
        System.out.println("ID: " + eq.getId());
        System.out.println("Name: " + eq.getName());
        System.out.println("Usage Count: " + eq.getUsageCount());
        System.out.println("Wins: " + eq.getWins());
        System.out.printf("Win Rate: %.2f%%\n", eq.getWinRate() * 100);
        System.out.printf("Average Rating: %.1f / 5.0\n", eq.getAverageRating());
        System.out.println("=========================");
    }

    public static void printEquipmentRanking() {
        System.out.println("\n=== Equipment Statistics Ranking ===");
        System.out.println("Ranked by Win Rate (Descending), then Usage Count");
        List<Equipment> ranked = rankingService.getRankedEquipment();

        if (ranked.isEmpty()) {
            System.out.println("No equipment data available.");
        } else {
            for (int i = 0; i < Math.min(ranked.size(), 20); i++) { // Show top 20
                Equipment eq = ranked.get(i);
                System.out.printf("%2d. %-25s | Win Rate: %5.1f%% | Usage: %4d | Avg Rating: %.1f\n",
                        (i + 1), eq.getName(), eq.getWinRate() * 100, eq.getUsageCount(), eq.getAverageRating());
            }
        }
        System.out.println("====================================");
    }

    public static void printMatchHistory(List<MatchRecord> matches, String entityName) {
        System.out.println("\n=== Match History for " + entityName + " ===");
        if (matches == null || matches.isEmpty()) {
            System.out.println("No match history found.");
        } else {
            for (MatchRecord match : matches) {
                System.out.println("Date: " + match.getMatchDate().format(DATE_FORMATTER));
                System.out.println("ID: " + match.getId());
                System.out.println(
                        "Matchup: " + (match.getTeamA() != null ? match.getTeamA().getName() : "[DELETED TEAM]")
                                + " vs " + (match.getTeamB() != null ? match.getTeamB().getName() : "[DELETED TEAM]"));
                System.out.println("Result: " + match.getResult());
                System.out.println("Picks:");
                match.getPlayerHeroPicks().forEach((playerId, heroId) -> {
                    Player p = searchService.findPlayerByIdOrName(playerId);

                    String heroName = "Unknown Hero";
                    for (Hero dmHero : dataManager.getHeroes()) {
                        if (dmHero.getId().equals(heroId)) {
                            heroName = dmHero.getName();
                            break;
                        }
                    }

                    System.out.println("  - " + (p != null ? p.getName() : playerId) + " picked " + heroName);
                });
                System.out.println("-------------------------");
            }
        }
        System.out.println("====================================");
    }

    public static void printRecentMatches(int n) {
        List<MatchRecord> allMatches = dataManager.getMatchRecords();
        System.out.println("\n=== Most Recent " + n + " Match Records ===");
        if (allMatches == null || allMatches.isEmpty()) {
            System.out.println("No match history found.");
            System.out.println("====================================");
            return;
        }

        int count = 0;
        for (int i = allMatches.size() - 1; i >= 0 && count < n; i--) {
            MatchRecord match = allMatches.get(i);
            System.out.println("Date: " + match.getMatchDate().format(DATE_FORMATTER));
            System.out.println(
                    "Matchup: " + (match.getTeamA() != null ? match.getTeamA().getName() : "Unknown") + " vs " +
                            (match.getTeamB() != null ? match.getTeamB().getName() : "Unknown"));
            System.out.println("Result: " + match.getResult());
            System.out.println("Picks:");
            match.getPlayerHeroPicks().forEach((playerId, heroId) -> {
                Player p = searchService.findPlayerByIdOrName(playerId);

                String heroName = "Unknown Hero";
                for (Hero dmHero : dataManager.getHeroes()) {
                    if (dmHero.getId().equals(heroId)) {
                        heroName = dmHero.getName();
                        break;
                    }
                }

                System.out.println("  - " + (p != null ? p.getName() : playerId) + " picked " + heroName);
            });
            System.out.println("-------------------------");
            count++;
        }
        System.out.println("====================================");
    }

    public static void printPlayerLeaderboard(int topX) {
        System.out.println("\n=== Player Leaderboard (Top " + topX + ") ===");
        System.out.println("Ranked by Win Rate (Descending), then Level");
        List<Player> leaderboard = rankingService.getPlayerLeaderboard(topX);

        if (leaderboard.isEmpty()) {
            System.out.println("No player data available.");
        } else {
            for (int i = 0; i < leaderboard.size(); i++) {
                Player p = leaderboard.get(i);
                System.out.printf("%2d. %-20s | Win Rate: %5.1f%% | Level: %2d | Matches: %3d\n",
                        (i + 1), p.getName(), p.getWinRate() * 100, p.getLevel(), p.getTotalMatches());
            }
        }
        System.out.println("======================================");
    }

    public static void printRecommendations(Hero hero, List<Equipment> recommendations) {
        System.out.println("\n=== AI Equipment Recommendations for " + hero.getName() + " ===");
        if (recommendations == null || recommendations.isEmpty()) {
            System.out.println("No recommendations available at this time.");
        } else {
            for (int i = 0; i < recommendations.size(); i++) {
                Equipment eq = recommendations.get(i);
                System.out.printf("%2d. %-20s | Global Win Rate: %5.1f%% | Popularity: %d picks\n",
                        (i + 1), eq.getName(), eq.getWinRate() * 100, eq.getUsageCount());
            }
        }
        System.out.println("==================================================");
    }

    public static void printCombatReport(List<String> report) {
        System.out.println();
        for (String line : report) {
            System.out.println(line);
        }
        System.out.println("=========================================\n");
    }
}