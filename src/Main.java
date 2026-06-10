import model.Admin;
import model.Equipment;
import model.Hero;
import model.HeroType;
import model.MatchRecord;
import model.MatchResult;
import model.Person;
import model.Player;
import model.Team;

import service.AuthenticationService;
import service.FileStorageService;
import service.GameDataManager;
import service.SearchService;

import util.DataInitializer;
import util.InputHelper;

import ui.ConsolePrinter;

public class Main {
    // 1. Instantiate shared services at the class level or inside main
    private static final GameDataManager dataManager = GameDataManager.getInstance();
    private static final FileStorageService fileStorageService = new FileStorageService();
    private static final AuthenticationService authService = new AuthenticationService();
    private static final SearchService searchService = new SearchService();

    public static void main(String[] args) {
        // --- PHASE 1: SYSTEM INITIALIZATION ---
        // 1. Call FileStorageService to load CSV data.
        System.out.println("Attempting to load from saved data...");
        // 2. If lists are empty, call DataInitializer and save immediately.
        if (!fileStorageService.loadData(dataManager)) {
            DataInitializer.initialize();
            System.out.println("Data initialization complete!");
        }
        // (You already have this perfectly set up in your current Main.java!)

        // --- PHASE 2: APPLICATION LIFECYCLE (The Master Loop) ---
        boolean systemRunning = true;
        String answer = null;
        // --- PHASE 3: AUTHENTICATION STATE ---
        // 1. Display a welcome banner.
        System.out.println("===================\nWELCOME, EVERYPONY!\n===================");

                while (systemRunning) {
            // 2. Ask the user to input their Name (or type "exit" to shut down).
            String name = InputHelper.getStringInput("Please enter your name, or type \"exit\" to shut down: ");
            
            // 4. Pass the input to AuthenticationService to get a Person object.
            Person thisPerson = authService.authenticateUser(name);

            // 3. If "exit" and auth failed, set systemRunning = false and break the loop.
            if (thisPerson == null && name.equalsIgnoreCase("exit")) {
                systemRunning = false;
                break;
            }

            // --- PHASE 4: ROUTING STATE ---
            // If the Person object is NOT null:
            if (thisPerson != null) {
                // Check their Role enum.
                // If ADMIN -> runAdminMenu((Admin) person);
                if (thisPerson instanceof Admin) {
                    runAdminMenu((Admin) thisPerson);
                }
                // If PLAYER -> runPlayerMenu((Player) person);
                else if (thisPerson instanceof Player) {
                    runPlayerMenu((Player) thisPerson);
                }
            } else {
                // If the Person object IS null:
                // Print "Invalid ID or Name. Please try again."
                System.out.println(
                        "Hmmm, the system can't find your name, little pony. But fret not! You can re-enter your name or ask the princesses to help you create a new account if you don't have an existing account!");
                while (true) {
                    answer = InputHelper.getStringInput("Try again? (y/n)").toLowerCase();
                    if (answer.equals("y")) {
                        break;
                    } else if (answer.equals("n")) {
                        systemRunning = false;
                        break;
                    } else {
                        continue;
                    }
                }
            }
        }
        // --- PHASE 5: SHUTDOWN STATE ---
        // 1. Call FileStorageService.saveData() to ensure all final state changes are
        // committed to CSV.
        fileStorageService.saveData(dataManager);
        // 2. Print a graceful shutdown message (e.g., "System terminated. Goodbye!").
        System.out.println("===================\nGOODBYE, EVERYPONY!\n===================");
    }

    // ==========================================
    // HELPER MENU LOOPS
    // ==========================================

    private static void runAdminMenu(Admin admin) {
        boolean loggedIn = true;
        System.out.println("\nLog in success!");
        System.out.println("Hi, Princess " + admin.getName() + "! What's on your mind today?");

        while (loggedIn) {
            int choice1 = -1;
            int choice2 = -1;
            choice1 = InputHelper.getIntInput(
                    "\nPlease select: \n[1] View information\n[2] Manage Players\n[3] Manage Teams\n[4] Manage Match Records\n[5] Manage Heroes\n[6] Manage Equipment\n[0] Logout\n> ");

            switch (choice1) {
                case 0: {
                    System.out.println("Logging out...");
                    loggedIn = false;
                    break;
                }

                                case 1: {
                    choice2 = InputHelper.getIntInput(
                            "What would you like to look up?\n[1] Players\n[2] Teams\n[3] Heroes\n[4] Equipments\n[5] Match Records\n[6] Player Leaderboard\n[7] Equipment Leaderboard\n[0] Return to Last Step");
                    switch (choice2) {
                        case 1: {
                            String query = InputHelper.getStringInput("Please enter player name or ID: ");
                            Player player = searchService.findPlayerByIdOrName(query);
                            ConsolePrinter.printPlayerDetails(player);
                            break;
                        }
                        case 2: {
                            String query = InputHelper.getStringInput("Please enter team name or ID: ");
                            Team team = searchService.findTeamByIdOrName(query);
                            ConsolePrinter.printTeamDetails(team);
                            break;
                        }
                        case 3: {
                            String query = InputHelper.getStringInput("Please enter hero name or ID: ");
                            Hero hero = searchService.findHeroByIdOrName(query);
                            ConsolePrinter.printHeroDetails(hero);
                            break;
                        }
                        case 4: {
                            String query = InputHelper.getStringInput("Please enter equipment name or ID: ");
                            Equipment equipment = searchService.findEquipmentByIdOrName(query);
                            ConsolePrinter.printEquipmentDetails(equipment);
                            break;
                        }
                        case 5: {
                            ConsolePrinter.printRecentMatches(InputHelper
                                    .getIntInput("Please enter how many matches you would like to review: "));
                            break;
                        }
                        case 6: {
                            int topX = InputHelper.getIntInput("How many players to show in leaderboard?: ");
                            ConsolePrinter.printPlayerLeaderboard(topX);
                            break;
                        }
                        case 7: {
                            ConsolePrinter.printEquipmentRanking();
                            break;
                        }
                    }
                    break;
                }
                                // ==========================================
                // CASE 2: MANAGE PLAYERS
                // ==========================================
                case 2:
                    managePlayers();
                    break;
                    // ==========================================
                    // CASE 3: MANAGE TEAMS
                    // ==========================================
                case 3:
                    manageTeams();
                    break;

                // ==========================================
                // CASE 4: MANAGE MATCH RECORDS
                // ==========================================
                case 4:
                    manageMatchRecords();
                    break;

                // ==========================================
                // CASE 5: MANAGE HEROES
                // ==========================================
                case 5:
                    manageHeroes();
                    break;

                // ==========================================
                // CASE 6: MANAGE EQUIPMENT
                // ==========================================
                case 6:
                    manageEquipment();
                    break;

                default:
                    System.out.println("Please enter a valid input, Your Highness!");
                    break;
            }
        }
    }

    private static void managePlayers() {
        int choice2 = InputHelper.getIntInput(
                "\n--- Manage Players ---\n[1] Add a Player\n[2] Remove a Player\n[3] Update a Player\n[0] Return to Last Step\n> ");
        switch (choice2) {
            case 0:
                break;
                        case 1: {
                String playerToAdd = InputHelper
                        .getStringInput("Please enter the name of the player you want to add: ");
                if (playerToAdd.trim().isEmpty()) {
                    System.out.println("Player name cannot be empty!");
                    break;
                }
                dataManager.addPlayer(new Player(playerToAdd));
                System.out.println("Player added successfully!");
                break;
            }
            case 2: {
                String playerToRemoveId = InputHelper
                        .getStringInput("Please enter the ID or Name of the player you want to remove: ");
                Player p = searchService.findPlayerByIdOrName(playerToRemoveId);
                if (p != null && dataManager.removePlayer(p.getId())) {
                    System.out.println("Player removed successfully!");
                } else {
                    System.out.println("Player not found!");
                }
                break;
            }
            case 3: {
                String identifier = InputHelper
                        .getStringInput("Please enter the ID or Name of the player you want to update: ");
                Player playerToUpdate = searchService.findPlayerByIdOrName(identifier);

                if (playerToUpdate == null) {
                    System.out.println("Player not found! Returning...");
                    break;
                }

                System.out.println("Updating Player: " + playerToUpdate.getName());
                int newLevel = InputHelper
                        .getIntInput("Enter new level (current: " + playerToUpdate.getLevel() + "): ");
                playerToUpdate.setLevel(newLevel);

                String newName = InputHelper.getStringInput(
                        "Enter new name (or press enter to keep '" + playerToUpdate.getName() + "'): ");
                if (!newName.isEmpty()) {
                    playerToUpdate.setName(newName);
                }

                dataManager.updatePlayer(playerToUpdate);
                System.out.println("Player updated successfully!");
                break;
            }
        }
    }

    private static void manageTeams() {
        int choice2 = InputHelper.getIntInput(
                "\n--- Manage Teams ---\n[1] Add a Team\n[2] Remove a Team\n[3] Update a Team\n[0] Return to Last Step\n> ");
        switch (choice2) {
            case 0:
                break;
                        case 1: {
                String teamName = InputHelper.getStringInput("Please enter the name of the new team: ");
                if (teamName.trim().isEmpty()) {
                    System.out.println("Team name cannot be empty!");
                    break;
                }
                // Teams require a list of members. We start with an empty list.
                dataManager.addTeam(new Team(teamName, new java.util.ArrayList<>()));
                System.out.println("Team added successfully!");
                break;
            }
            case 2: {
                String teamIdentifier = InputHelper
                        .getStringInput("Please enter the ID or Name of the team to remove: ");
                Team t = searchService.findTeamByIdOrName(teamIdentifier);
                if (t != null && dataManager.removeTeam(t.getId())) {
                    System.out
                            .println("Team removed successfully! All associated matches were rolled back.");
                } else {
                    System.out.println("Team not found!");
                }
                break;
            }
            case 3: {
                String identifier = InputHelper
                        .getStringInput("Please enter the ID or Name of the team to update: ");
                Team teamToUpdate = searchService.findTeamByIdOrName(identifier);

                if (teamToUpdate == null) {
                    System.out.println("Team not found! Returning...");
                    break;
                }

                System.out.println("Updating Team: " + teamToUpdate.getName());
                String newName = InputHelper.getStringInput(
                        "Enter new name (or press enter to keep '" + teamToUpdate.getName() + "'): ");
                if (!newName.isEmpty()) {
                    teamToUpdate.setName(newName);
                }

                dataManager.updateTeam(teamToUpdate);
                System.out.println("Team updated successfully!");
                break;
            }
        }
    }

    private static void manageMatchRecords() {
        int choice2 = InputHelper.getIntInput(
                "\n--- Manage Match Records ---\n[1] Add a Match Record\n[2] Remove a Match Record\n[3] Update Match Record\n[0] Return to Last Step\n> ");
        switch (choice2) {
            case 0:
                break;
            case 1: {
                String ta = InputHelper.getStringInput("Enter Team A name or ID: ");
                Team teamA = searchService.findTeamByIdOrName(ta);
                String tb = InputHelper.getStringInput("Enter Team B name or ID: ");
                Team teamB = searchService.findTeamByIdOrName(tb);
                if (teamA == null || teamB == null) {
                    System.out.println("One or both teams could not be found. Aborting.");
                    break;
                }

                int res = InputHelper.getIntInput("Who won? [1] Team A  [2] Team B  [3] Draw : ");
                MatchResult mr = (res == 1) ? MatchResult.TEAM_A_WIN
                        : (res == 2) ? MatchResult.TEAM_B_WIN : MatchResult.DRAW;

                dataManager.addMatchRecord(new MatchRecord(teamA, teamB, mr));
                System.out.println("Match recorded successfully! Team statistics have been updated.");
                break;
            }
            case 2: {
                String matchId = InputHelper
                        .getStringInput("Please enter the exact ID of the match to remove: ");
                if (dataManager.removeMatchRecord(matchId)) {
                    System.out.println(
                            "Match removed! Team and player statistics have been safely rolled back.");
                } else {
                    System.out.println("Match record not found.");
                }
                break;
            }
            case 3: {
                String matchId = InputHelper
                        .getStringInput("Please enter the exact ID of the match to update: ");

                MatchRecord recordToUpdate = searchService.findMatchRecordById(matchId);

                if (recordToUpdate == null) {
                    System.out.println("Match record not found! Returning...");
                    break;
                }

                System.out.println("Updating Match: " + recordToUpdate.getTeamA().getName() + " vs "
                        + recordToUpdate.getTeamB().getName());
                System.out.println("Current Result: " + recordToUpdate.getResult());

                int newRes = InputHelper
                        .getIntInput("Enter corrected result [1] Team A Win  [2] Team B Win  [3] Draw : ");
                MatchResult newResult = (newRes == 1) ? MatchResult.TEAM_A_WIN
                        : (newRes == 2) ? MatchResult.TEAM_B_WIN : MatchResult.DRAW;

                if (newResult == recordToUpdate.getResult()) {
                    System.out.println("Result is the same. No changes made.");
                    break;
                }

                MatchRecord tempUpdatedRecord = new MatchRecord(
                        recordToUpdate.getId(),
                        recordToUpdate.getTeamA(),
                        recordToUpdate.getTeamB(),
                        newResult,
                        recordToUpdate.getMatchDate(),
                        new java.util.HashMap<>(recordToUpdate.getPlayerHeroPicks()));

                dataManager.updateMatchRecord(tempUpdatedRecord);
                System.out.println(
                        "Match record updated! Team and player statistics have been safely recalculated.");
                break;
            }
        }
    }

    private static void manageHeroes() {
        int choice2 = InputHelper.getIntInput(
                "\n--- Manage Heroes ---\n[1] Add a Hero\n[2] Remove a Hero\n[3] Update a Hero\n[0] Return to Last Step\n> ");
        switch (choice2) {
            case 0:
                break;
            case 1: {
                String name = InputHelper.getStringInput("Enter Hero Name: ");
                String typeStr = InputHelper
                        .getStringInput("Enter Hero Type (WARRIOR, ASSASSIN, MAGE, DRAGON, ELF): ")
                        .toUpperCase();
                try {
                    HeroType type = HeroType.valueOf(typeStr);
                    int hp = InputHelper.getIntInput("Enter Base HP: ");
                    int atk = InputHelper.getIntInput("Enter Base Attack: ");
                    dataManager.addHero(new Hero(name, type, hp, atk));
                    System.out.println("Hero added successfully!");
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid Hero Type! Aborting.");
                }
                break;
            }
            case 2: {
                String heroIdentifier = InputHelper
                        .getStringInput("Please enter the ID or name of the hero to remove: ");
                Hero h = searchService.findHeroByIdOrName(heroIdentifier);
                if (h != null && dataManager.removeHero(h.getId())) {
                    System.out.println(
                            "Hero removed successfully! Automatically deleted from player inventories.");
                } else {
                    System.out.println("Hero not found!");
                }
                break;
            }
            case 3: {
                String identifier = InputHelper
                        .getStringInput("Please enter the ID or name of the hero to update: ");
                Hero heroToUpdate = searchService.findHeroByIdOrName(identifier);

                if (heroToUpdate == null) {
                    System.out.println("Hero not found! Returning...");
                    break;
                }

                System.out.println("Updating Hero: " + heroToUpdate.getName());
                String newName = InputHelper.getStringInput(
                        "Enter new name (or press enter to keep '" + heroToUpdate.getName() + "'): ");
                if (!newName.isEmpty()) {
                    heroToUpdate.setName(newName);
                }

                int newHp = InputHelper
                        .getIntInput("Enter new Base HP (current: " + heroToUpdate.getBaseHp() + "): ");
                heroToUpdate.setBaseHp(newHp);

                dataManager.updateHero(heroToUpdate);
                System.out.println("Hero updated successfully!");
                break;
            }
        }
    }

    private static void manageEquipment() {
        int choice2 = InputHelper.getIntInput(
                "\n--- Manage Equipment ---\n[1] Add Equipment\n[2] Remove Equipment\n[3] Update Equipment\n[0] Return to Last Step\n> ");
        switch (choice2) {
            case 0:
                break;
            case 1: {
                String eqName = InputHelper.getStringInput("Enter the name of the new equipment: ");
                dataManager.addEquipment(new Equipment(eqName));
                System.out.println("Equipment added successfully!");
                break;
            }
            case 2: {
                String eqName = InputHelper
                        .getStringInput("Enter the exact name or ID of the equipment to remove: ");
                Equipment foundEq = searchService.findEquipmentByIdOrName(eqName);

                if (foundEq != null && dataManager.removeEquipment(foundEq.getId())) {
                    System.out.println("Equipment removed successfully! Safely detached from all heroes.");
                } else {
                    System.out.println("Equipment not found!");
                }
                break;
            }
            case 3: {
                String eqName = InputHelper
                        .getStringInput("Enter the name or ID of the equipment to update: ");
                Equipment eqToUpdate = searchService.findEquipmentByIdOrName(eqName);

                if (eqToUpdate == null) {
                    System.out.println("Equipment not found! Returning...");
                    break;
                }

                System.out.println("Updating Equipment: " + eqToUpdate.getName());
                String newName = InputHelper.getStringInput(
                        "Enter new name (or press enter to keep '" + eqToUpdate.getName() + "'): ");
                if (!newName.isEmpty()) {
                    eqToUpdate.setName(newName);
                }

                dataManager.updateEquipment(eqToUpdate);
                System.out.println("Equipment updated successfully!");
                break;
            }
        }
    }

    private static void runPlayerMenu(Player player) {
        boolean loggedIn = true;
        int choice = -1;
        System.out.println("Log in success!");
        System.out.println("Hi, " + player.getName() + "! You are such a cute little pony~ What's in your mind today?");
        while (loggedIn) {
            choice = InputHelper.getIntInput(
                    "\nPlease select: \n[1] View My Profile\n[2] Edit My Name\n[3] View Teams\n[4] View Heroes\n[5] View Global Match Records\n[6] View Own Match Records\n[7] View Leaderboards\n[0] Log out");

            switch (choice) {
                case 0: {
                    loggedIn = false;
                    break;
                }

                case 1: {
                    ConsolePrinter.printPlayerDetails(player);
                    break;
                }
                case 2: {
                    String newName = InputHelper.getStringInput(
                            "Please enter your new name (or press enter to keep '" + player.getName() + "'): ");
                    if (!newName.isEmpty()) {
                        player.setName(newName);
                        dataManager.updatePlayer(player); // <-- ADD THIS TO ENSURE IT SAVES
                        System.out.println("Name updated successfully!"); // Give the user feedback
                    }
                    break;
                }
                case 3: {
                    String query = InputHelper.getStringInput("Please enter team name: ");
                    Team team = searchService.findTeamByIdOrName(query);
                    ConsolePrinter.printTeamDetails(team);
                    break;
                }
                case 4: {
                    String query = InputHelper.getStringInput("Please enter hero name or ID: ");
                    Hero hero = searchService.findHeroByIdOrName(query);
                    ConsolePrinter.printHeroDetails(hero);
                    break;
                }
                case 5: {
                    ConsolePrinter.printRecentMatches(InputHelper
                            .getIntInput("Please enter how many matches you would like to review: "));
                    break;
                }
                case 6:
                    int limit = InputHelper.getIntInput("How many matches to show?: ");
                    ConsolePrinter.printMatchHistory(searchService.getMatchHistoryForPlayer(player.getId(), limit),
                            player.getName());
                    break;
                case 7:
                    int topX = InputHelper.getIntInput("How many players to show in leaderboard?: ");
                    ConsolePrinter.printPlayerLeaderboard(topX);
                    break;
                default:
                    System.out.println("Please enter a valid input, sugarcube! ");
            }
        }
    }
}

