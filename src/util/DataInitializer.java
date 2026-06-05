package util;

import model.Equipment;
import model.Hero;
import model.Player;
import model.Team;
import model.MatchRecord;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DataInitializer {

    public static void initialize() {
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
                new Hero("Gerald"), new Hero("Magnus"), new Hero("Elora"),
                new Hero("Alric"), new Hero("Dante"), new Hero("Nivus"),
                new Hero("Eridan"), new Hero("Phoenix"), new Hero("Wilbur"),
                new Hero("Vez'nan"), new Hero("Asra"), new Hero("Eiskalt"),
                new Hero("Boneheart"), new Hero("Anya"), new Hero("Nyru")));

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

            hero.addEquipment(equipments.get(0));
            hero.addEquipment(equipments.get(1));
        }
        // 4. Initialize 3 Teams with 5 players each
        Team team1 = new Team("Team Earth Pony", players.subList(0, 5));
        Team team2 = new Team("Team Pegasus", players.subList(5, 10));
        Team team3 = new Team("Team Unicorn", players.subList(10, 15));
        List<Team> teams = List.of(team1, team2, team3);

        // 5. Initialize 10 Match Records
        List<MatchRecord> matchRecords = new ArrayList<>(List.of(
                new MatchRecord(team1, team2, "Team Earth Pony won"),
                new MatchRecord(team2, team3, "Team Pegasus won"),
                new MatchRecord(team3, team1, "Team Unicorn won"),
                new MatchRecord(team1, team2, "Draw"),
                new MatchRecord(team1, team3, "Team Earth Pony won"),
                new MatchRecord(team2, team1, "Team Pegasus won"),
                new MatchRecord(team3, team2, "Team Unicorn won"),
                new MatchRecord(team2, team3, "Draw"),
                new MatchRecord(team3, team1, "Team Unicorn won"),
                new MatchRecord(team1, team2, "Team Earth Pony won")));
    }
}