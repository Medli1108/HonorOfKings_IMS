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
        
        while (systemRunning) {
            // --- PHASE 3: AUTHENTICATION STATE ---
            // 1. Display a welcome banner.
            System.out.println("==========================\nWELCOME, EVERYPONY!\n==========================");
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
                System.out.println("Hmmm, the system can't find your name, little pony. " +
                "But fret not! You can re-enter your name or ask the princesses to help you create a new account" +
                "if you don't have an existing account!");
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
        // 2. Print a graceful shutdown message (e.g., "System terminated. Goodbye!").
    }

    // ==========================================
    // HELPER MENU LOOPS
    // ==========================================

    private static void runAdminMenu(Admin admin) {
        boolean loggedIn = true;
        while (loggedIn) {
            // 1. Print Admin Menu Options:
            //    [1] Manage Players
            //    [2] Manage Teams
            //    [3] Manage Match Records
            //    ...
            //    [0] Logout
            
            // 2. Use a switch statement to handle InputHelper.getIntInput()
            
            // 3. If [0] is selected -> set loggedIn = false (returns to Authentication Phase)
        }
    }

    private static void runPlayerMenu(Player player) {
        boolean loggedIn = true;
        while (loggedIn) {
            // 1. Print Player Menu Options:
            //    [1] View My Profile & Edit Info
            //    [2] Search Hero Details
            //    [3] View Leaderboards
            //    ...
            //    [0] Logout
            
            // 2. Use a switch statement to route to specific view functions
            
            // 3. If [0] is selected -> set loggedIn = false (returns to Authentication Phase)
        }
    }
}