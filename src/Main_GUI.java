import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import model.Admin;
import model.Person;
import model.Player;

import service.AuthenticationService;
import service.CombatSimulatorService;
import service.FileStorageService;
import service.GameDataManager;
import service.RecommendationService;
import service.SearchService;

import util.DataInitializer;

public class Main_GUI extends Application {

    // --- Services ---
    // These services are shared across the application, similar to Main.java
    private static final GameDataManager dataManager = GameDataManager.getInstance();
    private static final FileStorageService fileStorageService = new FileStorageService();
    private static final AuthenticationService authService = new AuthenticationService();
    private static final SearchService searchService = new SearchService();
    private static final RecommendationService recommendationService = new RecommendationService();
    private static final CombatSimulatorService combatSimulatorService = new CombatSimulatorService();

    // Store the primary stage to switch scenes
    private Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        // --- PHASE 1: SYSTEM INITIALIZATION ---
        System.out.println("Initializing system and loading data...");
        if (!fileStorageService.loadData(dataManager)) {
            DataInitializer.initialize();
            System.out.println("Data initialization complete!");
        }

        // --- PHASE 5: SHUTDOWN STATE ---
        // Add a shutdown hook to save data when the application is closed
        primaryStage.setOnCloseRequest(e -> {
            System.out.println("Shutting down and saving data...");
            fileStorageService.saveData(dataManager);
            System.out.println("Goodbye!");
        });

        // --- PHASE 2 & 3: AUTHENTICATION ---
        // Start with the login scene
        primaryStage.setTitle("My Little Pony: Game Manager");
        showLoginScene();
        primaryStage.show();
    }

    private void showLoginScene() {
        // Layout for the login screen
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20, 20, 20, 20));
        grid.setVgap(8);
        grid.setHgap(10);

        // UI Components
        Label nameLabel = new Label("Please enter your name:");
        TextField nameInput = new TextField();
        Button loginButton = new Button("Login");
        Label messageLabel = new Label();

        // Arrange components in the grid
        GridPane.setConstraints(nameLabel, 0, 0);
        GridPane.setConstraints(nameInput, 1, 0);
        GridPane.setConstraints(loginButton, 1, 1);
        GridPane.setConstraints(messageLabel, 1, 2);

        // Action for the login button
        loginButton.setOnAction(e -> {
            String name = nameInput.getText();
            if (name.trim().isEmpty()) {
                messageLabel.setText("Name cannot be empty.");
                return;
            }

            Person thisPerson = authService.authenticateUser(name);

            if (thisPerson != null) {
                // --- PHASE 4: ROUTING ---
                if (thisPerson instanceof Admin) {
                    showAdminDashboard((Admin) thisPerson);
                } else if (thisPerson instanceof Player) {
                    showPlayerDashboard((Player) thisPerson);
                }
            } else {
                messageLabel.setText("User not found. Please try again.");
            }
        });

        grid.getChildren().addAll(nameLabel, nameInput, loginButton, messageLabel);
        Scene loginScene = new Scene(grid, 350, 200);
        primaryStage.setScene(loginScene);
        primaryStage.setTitle("Welcome!");
    }

    private void showAdminDashboard(Admin admin) {
        primaryStage.setTitle("Admin Dashboard - Princess " + admin.getName());

        BorderPane layout = new BorderPane();
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        // --- Create tabs for each management category ---

        // Tab for Viewing Data (Search, Leaderboards)
        Tab viewTab = new Tab("View Information");
        // TODO: Implement the UI for this tab.
        // It could have a search bar and a dropdown to select what to search for (Players, Teams, etc.)
        // A TableView could display results. Buttons for leaderboards.
        // Example: VBox with controls and a TableView.

        // Tab for Player Management
        Tab playerTab = new Tab("Manage Players");
        // TODO: Implement a UI with a TableView of players.
        // Include buttons for "Add Player", "Edit Player", "Remove Player".
        // Clicking these buttons should open new dialog windows (Stages) for data entry.

        // Tab for Team Management
        Tab teamTab = new Tab("Manage Teams");
        // TODO: Implement a UI similar to the Player Management tab.

        // Tab for Hero Management
        Tab heroTab = new Tab("Manage Heroes");
        // TODO: Implement a UI similar to the Player Management tab.

        // Tab for Equipment Management
        Tab equipmentTab = new Tab("Manage Equipment");
        // TODO: Implement a UI similar to the Player Management tab.

        // Tab for Match Management
        Tab matchTab = new Tab("Manage Matches");
        // TODO: Implement a UI similar to the Player Management tab.

        tabPane.getTabs().addAll(viewTab, playerTab, teamTab, heroTab, equipmentTab, matchTab);
        layout.setCenter(tabPane);
        
        // --- Logout Button ---
        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> showLoginScene());
        
        VBox topContainer = new VBox(10);
        topContainer.setPadding(new Insets(10));
        topContainer.getChildren().addAll(new Label("Welcome, Princess " + admin.getName() + "!"), logoutButton);
        layout.setTop(topContainer);

        Scene adminScene = new Scene(layout, 800, 600);
        primaryStage.setScene(adminScene);
    }


    private void showPlayerDashboard(Player player) {
        primaryStage.setTitle("Player Dashboard - " + player.getName());

        BorderPane layout = new BorderPane();
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        
        // --- Create tabs for player functionalities ---

        // Tab for Profile
        Tab profileTab = new Tab("My Profile");
        // TODO: Display player details (use ConsolePrinter logic as a guide).
        // Add a TextField and Button to allow the player to update their name.

        // Tab for viewing Teams, Heroes, etc.
        Tab viewTab = new Tab("View Data");
        // TODO: UI for searching/viewing teams and heroes. Use TableViews.

        // Tab for Match History
        Tab matchHistoryTab = new Tab("Match History");
        // TODO: UI to view global and personal match history.
        // Could use two sub-tabs or radio buttons to switch between views.
        // Display matches in a TableView.

        // Tab for Leaderboards
        Tab leaderboardsTab = new Tab("Leaderboards");
        // TODO: UI to display the player leaderboard in a TableView.

        // Tab for Tools (Recommendation & Simulation)
        Tab toolsTab = new Tab("Tools");
        // TODO: Create two sections in this tab.
        // 1. Equipment Recommendation: ComboBox to select a hero, button to get recommendations, display in a ListView.
        // 2. Combat Simulation: Two ComboBoxes (or text fields with search) to select heroes,
        //    a button to simulate, and a TextArea to show the combat report.

        tabPane.getTabs().addAll(profileTab, viewTab, matchHistoryTab, leaderboardsTab, toolsTab);
        layout.setCenter(tabPane);

        // --- Logout Button ---
        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> showLoginScene());
        
        VBox topContainer = new VBox(10);
        topContainer.setPadding(new Insets(10));
        topContainer.getChildren().addAll(new Label("Welcome, " + player.getName() + "!"), logoutButton);
        layout.setTop(topContainer);

        Scene playerScene = new Scene(layout, 800, 600);
        primaryStage.setScene(playerScene);
    }
}