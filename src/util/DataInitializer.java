package util;

import model.Equipment;
import model.Hero;
import model.Player;
import model.Team;
import model.MatchRecord;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataInitializer {
    
    public static void initialize() {
        // 1. Initialize 20 Equipment Items
        List<Equipment> equipmentList = new ArrayList<>(Arrays.asList(
            new Equipment("Boots of Resistance"), new Equipment("Boots of Dexterity"),
            new Equipment("Bloodweeper"), new Equipment("Infinity Edge"),
            new Equipment("Master Sword"), new Equipment("Shadow Battleaxe"),
            new Equipment("Blade of Eternity"), new Equipment("Destiny"),
            new Equipment("Ominous Premonition"), new Equipment("Witch's Cloak"),
            new Equipment("Spikemail"), new Equipment("Glacial Buckler"),
            new Equipment("Holy Grail"), new Equipment("Echo of Ruin"),
            new Equipment("Savant's Wrath"), new Equipment("Void Staff"),
            new Equipment("Tome of Wisdom"), new Equipment("Starbreaker"),
            new Equipment("Daybreaker"), new Equipment("Purifying Roar")
        ));

        // 2. Initialize 15 Heroes
        List<Hero> heroes = new ArrayList<>(Arrays.asList(
            new Hero("Arthur"), new Hero("Diaochan"), new Hero("Luban No.7"),
            new Hero("Han Xin"), new Hero("Mulan"), new Hero("Sun Wukong"),
            new Hero("Zhao Yun"), new Hero("Li Bai"), new Hero("Angela"),
            new Hero("Hou Yi"), new Hero("Kaizer"), new Hero("Luna"),
            new Hero("Zhuge Liang"), new Hero("Marco Polo"), new Hero("Guiguzi")
        ));

        // 3. Initialize 15 Players (for 3 teams of 5)
        List<Player> players = new ArrayList<>();
        for (int i = 1; i <= 15; i++) {
            Player player = new Player("Player_" + i);
            
            // Fulfills minimum requirement: 10 total players owning 3 heroes each
            // (We assign 3 heroes to all 15 players to safely cover this)
            player.addHero(heroes.get((i * 3) % 15));
            player.addHero(heroes.get((i * 3 + 1) % 15));
            player.addHero(heroes.get((i * 3 + 2) % 15));
            
            players.add(player);
        }

        // 4. Initialize 3 Teams with 5 players each
        Team team1 = new Team("Team Alpha", players.subList(0, 5));
        Team team2 = new Team("Team Beta", players.subList(5, 10));
        Team team3 = new Team("Team Gamma", players.subList(10, 15));
        List<Team> teams = Arrays.asList(team1, team2, team3);

        // 5. Initialize 10 Match Records
        List<MatchRecord> matchRecords = new ArrayList<>(Arrays.asList(
            new MatchRecord(team1, team2, "Team Alpha won"),
            new MatchRecord(team2, team3, "Team Beta won"),
            new MatchRecord(team3, team1, "Team Gamma won"),
            new MatchRecord(team1, team2, "Draw"),
            new MatchRecord(team1, team3, "Team Alpha won"),
            new MatchRecord(team2, team1, "Team Beta won"),
            new MatchRecord(team3, team2, "Team Gamma won"),
            new MatchRecord(team2, team3, "Draw"),
            new MatchRecord(team3, team1, "Team Gamma won"),
            new MatchRecord(team1, team2, "Team Alpha won")
        ));
    }
}