package util;

import model.Admin;
import model.Equipment;
import model.Hero;
import model.HeroType;
import model.Player;
import model.Team;
import model.MatchRecord;
import model.MatchResult;
import service.GameDataManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class DataInitializer {

    public static void initialize() {
        Random rand = new Random(2026);
        // 1. Initialize 20 Equipment Items
        List<Equipment> equipments = new ArrayList<>(List.of(
                new Equipment("The Master Sword"), new Equipment("Hookshot"),
                new Equipment("Hylian Shield"), new Equipment("Ocarina of Time"),
                new Equipment("Bow and Arrows"), new Equipment("Bombs"),
                new Equipment("Boomerang"), new Equipment("Empty Bottle"),
                new Equipment("Iron Boots"), new Equipment("Mirror Shield"),
                new Equipment("Lens of Truth"), new Equipment("The Wind Waker"),
                new Equipment("Majora's Mask"), new Equipment("The Minish Cap"),
                new Equipment("Grappling Hook"), new Equipment("Paraglider"),
                new Equipment("Sheikah Slate"), new Equipment("Triforce of Wisdom"),
                new Equipment("Triforce of Power"), new Equipment("Triforce of Courage")));

        // 2. Initialize 15 Heroes
        List<Hero> heroes = new ArrayList<>(List.of(
                new Hero("Gerald", HeroType.WARRIOR, 3000, 15), 
                new Hero("Magnus", HeroType.MAGE, 1200, 30), 
                new Hero("Elora", HeroType.MAGE, 2400, 25),
                new Hero("Alric", HeroType.WARRIOR, 2600, 18), 
                new Hero("Dante", HeroType.ELF, 2000, 22), 
                new Hero("Nivus", HeroType.MAGE, 1500,28),
                new Hero("Eridan", HeroType.ELF, 2400, 22), 
                new Hero("Phoenix", HeroType.DRAGON, 1000, 15), 
                new Hero("Wilbur", HeroType.DRAGON, 1500, 12),
                new Hero("Vez'nan", HeroType.MAGE, 1800, 24), 
                new Hero("Asra", HeroType.ASSASSIN, 1400, 22), 
                new Hero("Eiskalt", HeroType.DRAGON, 2000, 20),
                new Hero("Boneheart", HeroType.DRAGON, 1500, 12), 
                new Hero("Anya", HeroType.ELF, 2200, 20), 
                new Hero("Nyru", HeroType.MAGE, 1300, 26)
            ));

        // 3. Initialize 15 Players (for 3 teams of 5)
        List<Player> players = new ArrayList<>(List.of(
                new Player("Applejack"), new Player("Pinkie Pie"),
                new Player("Berry Punch"), new Player("Coco Pommel"),
                new Player("Bon Bon"), new Player("Rainbow Dash"),
                new Player("Fluttershy"), new Player("Spitfire"),
                new Player("Derpy"), new Player("Vapor Trail"),
                new Player("Rarity"), new Player("Starlight Glimmer"),
                new Player("Sunset Shimmer"), new Player("Lyra Heartstrings"),
                new Player("Minuette")));

        for (Player player : players) {

            // Shuffle the master hero list randomly every single time a player takes a turn
            Collections.shuffle(heroes);

            // Deal the top 3 heroes from the shuffled deck directly to the player
            player.addHero(heroes.get(0));
            player.addHero(heroes.get(1));
            player.addHero(heroes.get(2));
        }

        for (Hero hero : heroes) {
            Collections.shuffle(equipments);

            hero.addCompatibleEquipment(equipments.get(0));
            hero.addCompatibleEquipment(equipments.get(1));
            hero.addCompatibleEquipment(equipments.get(2));

            hero.addRecommendedEquipment(equipments.get(0));
            hero.setCurrentEquipments(new ArrayList<>(List.of(equipments.get(0), equipments.get(1))));
        }
        // 4. Initialize 3 Teams with 5 players each
        Team team1 = new Team("Team Earth Pony", players.subList(0, 5));
        Team team2 = new Team("Team Pegasus", players.subList(5, 10));
        Team team3 = new Team("Team Unicorn", players.subList(10, 15));
        List<Team> teams = List.of(team1, team2, team3);

        // 5. Initialize 10 Match Records
        List<MatchRecord> matchRecords = new ArrayList<>(List.of(
                new MatchRecord(team1, team2, MatchResult.TEAM_A_WIN),
                new MatchRecord(team2, team3, MatchResult.TEAM_A_WIN),
                new MatchRecord(team3, team1, MatchResult.TEAM_A_WIN),
                new MatchRecord(team1, team2, MatchResult.DRAW),
                new MatchRecord(team1, team3, MatchResult.TEAM_A_WIN),
                new MatchRecord(team2, team1, MatchResult.TEAM_A_WIN),
                new MatchRecord(team3, team2, MatchResult.TEAM_A_WIN),
                new MatchRecord(team2, team3, MatchResult.DRAW),
                new MatchRecord(team3, team1, MatchResult.TEAM_A_WIN),
                new MatchRecord(team1, team2, MatchResult.TEAM_A_WIN)));

        // Update statistics and populate picks based on mock match records
        for (MatchRecord record : matchRecords) {
            Team tA = record.getTeamA();
            Team tB = record.getTeamB();
            tA.setTotalMatches(tA.getTotalMatches() + 1);
            tB.setTotalMatches(tB.getTotalMatches() + 1);
            
            for (Player player : tA.getMembers()) {
                player.setTotalMatches(player.getTotalMatches() + 1);
            }
            for (Player player : tB.getMembers()) {
                player.setTotalMatches(player.getTotalMatches() + 1);
            }

            if (record.getResult() == MatchResult.TEAM_A_WIN) {
                tA.setWins(tA.getWins() + 1);
                for (Player player : tA.getMembers()) {
                    player.setWins(player.getWins() + 1);
                }
            } else if (record.getResult() == MatchResult.TEAM_B_WIN) {
                tB.setWins(tB.getWins() + 1);
                for (Player player : tB.getMembers()) {
                    player.setWins(player.getWins() + 1);
                }
            }

            // Simulate hero picks for players in this match
            for (Player p : tA.getMembers()) {
                if (!p.getOwnedHeroes().isEmpty()) {
                    Hero picked = p.getOwnedHeroes().get(rand.nextInt(p.getOwnedHeroes().size()));
                    record.addPick(p.getId(), picked.getId());
                }
            }
            for (Player p : tB.getMembers()) {
                if (!p.getOwnedHeroes().isEmpty()) {
                    Hero picked = p.getOwnedHeroes().get(rand.nextInt(p.getOwnedHeroes().size()));
                    record.addPick(p.getId(), picked.getId());
                }
            }
        }
        
        // Randomly bump player win rates and levels so they aren't all 0
        for (Player player : players) {
            player.setLevel(rand.nextInt(30) + 1); // Level 1 to 30
            if (player.getTotalMatches() > 0) {
                player.setWinRate((double) player.getWins() / player.getTotalMatches());
            } else {
                player.setWinRate(0.0);
            }
        }
        
        // Randomly bump equipment stats
        for (Equipment eq : equipments) {
            eq.setUsageCount(rand.nextInt(500));
            eq.setWins(rand.nextInt(eq.getUsageCount() + 1));
            if (eq.getUsageCount() > 0) {
                eq.setWinRate((double) eq.getWins() / eq.getUsageCount());
            } else {
                eq.setWinRate(0.0);
            }
            eq.setAverageRating(1.0 + (4.0 * rand.nextDouble())); // 1.0 to 5.0
        }

        // 6. Initialize 4 Admins
        List<Admin> admins = new ArrayList<>(List.of(
            new Admin("Twilight Sparkle"), new Admin("Celestia"), 
            new Admin("Luna"), new Admin("Cadance")
        ));
        
        GameDataManager dataManager = GameDataManager.getInstance();

        // Populate the GameDataManager
        dataManager.setEquipmentList(equipments);
        dataManager.setHeroes(heroes);
        dataManager.setPlayers(players);
        dataManager.setTeams(teams);
        dataManager.setMatchRecords(matchRecords);
        dataManager.setAdmins(admins);
    }
}