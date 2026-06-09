package service;

import model.*;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class FileStorageService {
    private static final String DATA_DIR = "data/";
    private static final String ADMINS_FILE = DATA_DIR + "admins.csv";
    private static final String PLAYERS_FILE = DATA_DIR + "players.csv";
    private static final String HEROES_FILE = DATA_DIR + "heroes.csv";
    private static final String EQUIPMENTS_FILE = DATA_DIR + "equipments.csv";
    private static final String TEAMS_FILE = DATA_DIR + "teams.csv";
    private static final String MATCH_RECORDS_FILE = DATA_DIR + "match_records.csv";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    // Write utilities
    private static void writeLines(String filePath, List<String> lines) {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath))) {
            for (String line : lines) {
                pw.println(line);
            }
        } catch (IOException e) {
            System.err.println("Error writing to file " + filePath + ": " + e.getMessage());
        }
    }

    private static List<String> readLines(String filePath) {
        List<String> lines = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) {
            return lines;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading from file " + filePath + ": " + e.getMessage());
        }
        return lines;
    }

    // Save methods
    public void saveData(GameDataManager dataManager) {
        saveAdmins(dataManager.getAdmins());
        saveEquipments(dataManager.getEquipmentList());
        saveHeroes(dataManager.getHeroes());
        savePlayers(dataManager.getPlayers());
        saveTeams(dataManager.getTeams());
        saveMatchRecords(dataManager.getMatchRecords());
    }

    private void saveAdmins(List<Admin> admins) {
        List<String> lines = new ArrayList<>();
        lines.add("id,name"); // Header
        for (Admin admin : admins) {
            lines.add(admin.getId() + "," + escape(admin.getName()));
        }
        writeLines(ADMINS_FILE, lines);
    }

    private void saveEquipments(List<Equipment> equipments) {
        List<String> lines = new ArrayList<>();
        lines.add("id,name,usageCount,winRate,averageRating,wins");
        for (Equipment eq : equipments) {
            lines.add(eq.getId() + "," + escape(eq.getName()) + "," + eq.getUsageCount() + "," +
                    eq.getWinRate() + "," + eq.getAverageRating() + "," + eq.getWins());
        }
        writeLines(EQUIPMENTS_FILE, lines);
    }

    private void saveHeroes(List<Hero> heroes) {
        List<String> lines = new ArrayList<>();
        lines.add("id,name,type,baseHp,baseAttack,compatibleEquipments,recommendedEquipments,currentEquipments");
        for (Hero hero : heroes) {
            String compEq = hero.getCompatibleEquipments().stream().map(Equipment::getId).collect(Collectors.joining("|"));
            String recEq = hero.getRecommendedEquipments().stream().map(Equipment::getId).collect(Collectors.joining("|"));
            String currEq = hero.getCurrentEquipments().stream().map(Equipment::getId).collect(Collectors.joining("|"));
            lines.add(hero.getId() + "," + escape(hero.getName()) + "," +
                    (hero.getType() != null ? hero.getType().name() : "") + "," +
                    hero.getBaseHp() + "," + hero.getBaseAttack() + "," +
                    compEq + "," + recEq + "," + currEq);
        }
        writeLines(HEROES_FILE, lines);
    }

    private void savePlayers(List<Player> players) {
        List<String> lines = new ArrayList<>();
        lines.add("id,name,winRate,level,totalMatches,wins,ownedHeroes");
        for (Player p : players) {
            String ownedHeroes = p.getOwnedHeroes().stream().map(Hero::getId).collect(Collectors.joining("|"));
            lines.add(p.getId() + "," + escape(p.getName()) + "," + p.getWinRate() + "," + p.getLevel() + "," + p.getTotalMatches() + "," + p.getWins() + "," + ownedHeroes);
        }
        writeLines(PLAYERS_FILE, lines);
    }

    private void saveTeams(List<Team> teams) {
        List<String> lines = new ArrayList<>();
        lines.add("id,name,totalMatches,wins,members");
        for (Team t : teams) {
            String members = t.getMembers().stream().map(Player::getId).collect(Collectors.joining("|"));
            lines.add(t.getId() + "," + escape(t.getName()) + "," + t.getTotalMatches() + "," + t.getWins() + "," + members);
        }
        writeLines(TEAMS_FILE, lines);
    }

    private void saveMatchRecords(List<MatchRecord> records) {
        List<String> lines = new ArrayList<>();
        lines.add("id,teamA_id,teamB_id,result,matchDate,picks");
        for (MatchRecord r : records) {
            String picksStr = r.getPlayerHeroPicks().entrySet().stream()
                .map(e -> e.getKey() + ":" + e.getValue())
                .collect(Collectors.joining("|"));
            lines.add(r.getId() + "," + r.getTeamA().getId() + "," + r.getTeamB().getId() + "," + r.getResult().name() + "," + r.getMatchDate().format(DATE_FORMATTER) + "," + picksStr);
        }
        writeLines(MATCH_RECORDS_FILE, lines);
    }

    // Load methods
    public boolean loadData(GameDataManager dataManager) {
        List<Admin> admins = loadAdmins();
        List<Equipment> equipments = loadEquipments();
        Map<String, Equipment> eqMap = equipments.stream().collect(Collectors.toMap(Equipment::getId, e -> e));
        
        List<Hero> heroes = loadHeroes(eqMap);
        Map<String, Hero> heroMap = heroes.stream().collect(Collectors.toMap(Hero::getId, h -> h));
        
        List<Player> players = loadPlayers(heroMap);
        Map<String, Player> playerMap = players.stream().collect(Collectors.toMap(Player::getId, p -> p));
        
        List<Team> teams = loadTeams(playerMap);
        Map<String, Team> teamMap = teams.stream().collect(Collectors.toMap(Team::getId, t -> t));
        
        List<MatchRecord> records = loadMatchRecords(teamMap);

        if (!equipments.isEmpty() || !heroes.isEmpty() || !players.isEmpty() || !teams.isEmpty()) {
            dataManager.setAdmins(admins);
            dataManager.setEquipmentList(equipments);
            dataManager.setHeroes(heroes);
            dataManager.setPlayers(players);
            dataManager.setTeams(teams);
            dataManager.setMatchRecords(records);
            System.out.println("Data loaded successfully from CSV files!");
            return true;
        } else {
            System.out.println("No existing data found. Start initializing...");
            return false;
        }
    }

    private List<Admin> loadAdmins() {
        List<Admin> admins = new ArrayList<>();
        List<String> lines = readLines(ADMINS_FILE);
        for (int i = 1; i < lines.size(); i++) {
            String[] parts = lines.get(i).split(",", -1);
            if (parts.length >= 2) {
                admins.add(new Admin(parts[0], unescape(parts[1])));
            }
        }
        return admins;
    }

    private List<Equipment> loadEquipments() {
        List<Equipment> equipments = new ArrayList<>();
        List<String> lines = readLines(EQUIPMENTS_FILE);
        for (int i = 1; i < lines.size(); i++) {
            String[] parts = lines.get(i).split(",", -1);
            if (parts.length >= 6) {
                equipments.add(new Equipment(parts[0], unescape(parts[1]), Integer.parseInt(parts[2]),
                        Double.parseDouble(parts[3]), Double.parseDouble(parts[4]), Integer.parseInt(parts[5])));
            }
        }
        return equipments;
    }

    private List<Hero> loadHeroes(Map<String, Equipment> eqMap) {
        List<Hero> heroes = new ArrayList<>();
        List<String> lines = readLines(HEROES_FILE);
        for (int i = 1; i < lines.size(); i++) {
            String[] parts = lines.get(i).split(",", -1);
            if (parts.length >= 8) {
                String id = parts[0];
                String name = unescape(parts[1]);
                HeroType type = parts[2].isEmpty() ? null : HeroType.valueOf(parts[2]);
                int baseHp = Integer.parseInt(parts[3]);
                int baseAttack = Integer.parseInt(parts[4]);
                
                ArrayList<Equipment> compEq = new ArrayList<>();
                if (!parts[5].isEmpty()) {
                    for (String eqId : parts[5].split("\\|")) {
                        if (eqMap.containsKey(eqId)) compEq.add(eqMap.get(eqId));
                    }
                }
                
                ArrayList<Equipment> recEq = new ArrayList<>();
                if (!parts[6].isEmpty()) {
                    for (String eqId : parts[6].split("\\|")) {
                        if (eqMap.containsKey(eqId)) recEq.add(eqMap.get(eqId));
                    }
                }

                ArrayList<Equipment> currEq = new ArrayList<>();
                if (!parts[7].isEmpty()) {
                    for (String eqId : parts[7].split("\\|")) {
                        if (eqMap.containsKey(eqId)) currEq.add(eqMap.get(eqId));
                    }
                }
                
                heroes.add(new Hero(id, name, type, baseHp, baseAttack, compEq, recEq, currEq));
            }
        }
        return heroes;
    }

    private List<Player> loadPlayers(Map<String, Hero> heroMap) {
        List<Player> players = new ArrayList<>();
        List<String> lines = readLines(PLAYERS_FILE);
        for (int i = 1; i < lines.size(); i++) {
            String[] parts = lines.get(i).split(",", -1);
            if (parts.length >= 7) {
                String id = parts[0];
                String name = unescape(parts[1]);
                double winRate = Double.parseDouble(parts[2]);
                int level = Integer.parseInt(parts[3]);
                int totalMatches = Integer.parseInt(parts[4]);
                int wins = Integer.parseInt(parts[5]);
                
                Player p = new Player(id, name, winRate, level, totalMatches, wins);
                if (!parts[6].isEmpty()) {
                    for (String hId : parts[6].split("\\|")) {
                        if (heroMap.containsKey(hId)) p.addHero(heroMap.get(hId));
                    }
                }
                players.add(p);
            }
        }
        return players;
    }

    private List<Team> loadTeams(Map<String, Player> playerMap) {
        List<Team> teams = new ArrayList<>();
        List<String> lines = readLines(TEAMS_FILE);
        for (int i = 1; i < lines.size(); i++) {
            String[] parts = lines.get(i).split(",", -1);
            if (parts.length >= 5) {
                String id = parts[0];
                String name = unescape(parts[1]);
                int totalMatches = Integer.parseInt(parts[2]);
                int wins = Integer.parseInt(parts[3]);
                
                List<Player> members = new ArrayList<>();
                if (!parts[4].isEmpty()) {
                    for (String pId : parts[4].split("\\|")) {
                        if (playerMap.containsKey(pId)) members.add(playerMap.get(pId));
                    }
                }
                
                teams.add(new Team(id, name, members, totalMatches, wins));
            }
        }
        return teams;
    }

    private List<MatchRecord> loadMatchRecords(Map<String, Team> teamMap) {
        List<MatchRecord> records = new ArrayList<>();
        List<String> lines = readLines(MATCH_RECORDS_FILE);
        for (int i = 1; i < lines.size(); i++) {
            String[] parts = lines.get(i).split(",", -1);
            if (parts.length >= 5) {
                String id = parts[0];
                Team teamA = teamMap.get(parts[1]);
                Team teamB = teamMap.get(parts[2]);
                MatchResult result = MatchResult.valueOf(parts[3]);
                LocalDateTime date = LocalDateTime.parse(parts[4], DATE_FORMATTER);
                
                Map<String, String> picks = new HashMap<>();
                if (parts.length >= 6 && !parts[5].isEmpty()) {
                    for (String pair : parts[5].split("\\|")) {
                        String[] kv = pair.split(":");
                        if (kv.length == 2) {
                            picks.put(kv[0], kv[1]);
                        }
                    }
                }
                
                if (teamA != null && teamB != null) {
                    records.add(new MatchRecord(id, teamA, teamB, result, date, picks));
                }
            }
        }
        return records;
    }

    // Helper for basic CSV escaping
    private static String escape(String s) {
        if (s == null) return "";
        if (s.contains(",") || s.contains("\"") || s.contains("\n")) {
            return "\"" + s.replace("\"", "\"\"") + "\"";
        }
        return s;
    }

    private static String unescape(String s) {
        if (s == null) return "";
        if (s.startsWith("\"") && s.endsWith("\"")) {
            s = s.substring(1, s.length() - 1);
            s = s.replace("\"\"", "\"");
        }
        return s;
    }
}