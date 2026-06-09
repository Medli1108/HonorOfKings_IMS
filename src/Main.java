import model.Admin;
import model.Equipment;
import model.Hero;
import model.HeroType;
import model.MatchRecord;
import model.MatchResult;
import model.Person;
import model.Player;
import model.Role;
import model.Searchable;
import model.Team;

import service.AuthenticationService;
import service.FileStorageService;
import service.GameDataManager;
import service.RankingService;
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
    private static final RankingService rankingService = new RankingService();
    
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
        System.out.println("==========================\nWELCOME, EVERYPONY!\n==========================");
        
        while (systemRunning) {
            // 2. Ask the user to input their Name (or type "exit" to shut down).
            String name = InputHelper.getStringInput("Please enter your name, or type \"exit\" to shut down: ");
            // 3. If "exit", set systemRunning = false and break the loop.
            if (name.toLowerCase().equals("exit")) {
                systemRunning = false;
                break;
            // 4. Pass the input to AuthenticationService to get a Person object.
            } 
            Person thisPerson = authService.authenticateUser(name);
                      
            // --- PHASE 4: ROUTING STATE ---
            // If the Person object is NOT null:
            if (thisPerson != null) {
                //    Check their Role enum.
                //    If ADMIN -> runAdminMenu((Admin) person);
                if (thisPerson instanceof Admin) runAdminMenu((Admin)thisPerson);
                //    If PLAYER -> runPlayerMenu((Player) person);
                if (thisPerson instanceof Player) runPlayerMenu((Player)thisPerson);
            } else {
                // If the Person object IS null:
                //    Print "Invalid ID or Name. Please try again."
                System.out.println("Hmmm, the system can't find your name, little pony. But fret not! You can re-enter your name or ask the princesses to help you create a new account if you don't have an existing account!");
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
        // 1. Call FileStorageService.saveData() to ensure all final state changes are committed to CSV.
        fileStorageService.saveData(dataManager);
        // 2. Print a graceful shutdown message (e.g., "System terminated. Goodbye!").
        System.out.println("==========================\\nGOODBYE, EVERYPONY!\\n==========================");
    }

    // ==========================================
    // HELPER MENU LOOPS
    // ==========================================

    private static void runAdminMenu(Admin admin) {
        boolean loggedIn = true;
        System.out.println("Log in success!");
        System.out.println("Hi, Princess " + admin.getName() + "! What's in your mind today?");
        while (loggedIn) {
            int choice1 = -1;
            int choice2 = -1;
            // 1. Print Admin Menu Options:
            choice1 = InputHelper.getIntInput("Please select: \n[1] Manage Players\n[2] Manage Teams\n[3] Manage Match Records\n[4] Manage Heroes\n[5] Manage Equipments\n[0] Logout");
            switch (choice1) {
                case 0:
                    loggedIn = false;
                    break;
                case 1:
                    choice2 = InputHelper.getIntInput("Please select: \n[1] Add a Player\n[2] Remove a Player\n[3] Update a Player\n[0] Return to Last Step");
                    switch (choice2) {
                        case 0:
                            loggedIn = false;
                            break;
                        case 1:
                            String playerToAdd = InputHelper.getStringInput("Please enter the name of the player you want to add: ");
                            dataManager.addPlayer(new Player(playerToAdd));
                        case 2:
                            String playerToRemove = InputHelper.getStringInput("Please enter the name of the player you want to remove: ");
                            dataManager.removePlayer(playerToRemove);
                        case 3:
                            String playerToUpdate = InputHelper.getStringInput("Please enter the name of the player you want to update: ");
                            // Basic implementation to satisfy switch statement flow for now
                            System.out.println("Update logic to be implemented fully.");
                            break;
                        default:
                            break;
                    }
                    break;
                case 2:
                    choice2 = InputHelper.getIntInput("Please select: \n[1] Add a Player\n[2] Remove a Player\n[3] Update a Player\n[0] Return to Last Step");
                    break;
                case 3:
                    choice2 = InputHelper.getIntInput("Please select: \n[1] Add a Player\n[2] Remove a Player\n[3] Update a Player\n[0] Return to Last Step");
                    break;
                case 4:
                    choice2 = InputHelper.getIntInput("Please select: \n[1] Add a Player\n[2] Remove a Player\n[3] Update a Player\n[0] Return to Last Step");
                    break;
                case 5:
                    choice2 = InputHelper.getIntInput("Please select: \n[1] Add a Player\n[2] Remove a Player\n[3] Update a Player\n[0] Return to Last Step");
                    break;
                default:
                    System.out.println("Please enter a valid input, Your Highness! ");
                    break;
            }
        }
    }

        private static void runPlayerMenu(Player player) {
        boolean loggedIn = true;
        while (loggedIn) {
            int choice = InputHelper.getIntInput("\n=== Player Menu ===\n" +
                "[1] View My Profile\n" +
                "[2] Search Player\n" +
                "[3] Search Team Overview\n" +
                "[4] Search Hero Details\n" +
                "[5] View Equipment Statistics\n" +
                "[6] View My Match History\n" +
                "[7] View Player Leaderboard\n" +
                "[0] Logout\n" +
                "Please select: ");
            
            switch (choice) {
                case 0:
                    loggedIn = false;
                    break;
                case 1:
                    ConsolePrinter.printPlayerDetails(player);
                    break;
                case 2:
                    String pQuery = InputHelper.getStringInput("Enter Player Name or ID: ");
                    ConsolePrinter.printPlayerDetails(searchService.findPlayerByIdOrName(pQuery));
                    break;
                case 3:
                    String tQuery = InputHelper.getStringInput("Enter Team Name or ID: ");
                    ConsolePrinter.printTeamDetails(searchService.findTeamByIdOrName(tQuery));
                    break;
                case 4:
                    String hQuery = InputHelper.getStringInput("Enter Hero Name: ");
                    ConsolePrinter.printHeroDetails(searchService.findHeroByName(hQuery));
                    break;
                case 5:
                    ConsolePrinter.printEquipmentRanking();
                    break;
                case 6:
                    int limit = InputHelper.getIntInput("How many matches to show?: ");
                    ConsolePrinter.printMatchHistory(searchService.getMatchHistoryForPlayer(player.getId(), limit), player.getName());
                    break;
                case 7:
                    int topX = InputHelper.getIntInput("How many players to show in leaderboard?: ");
                    ConsolePrinter.printPlayerLeaderboard(topX);
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
}