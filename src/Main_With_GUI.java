import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

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
import service.CombatSimulatorService;
import service.FileStorageService;
import service.GameDataManager;
import service.RecommendationService;
import service.SearchService;

import util.DataInitializer;
import ui.ConsolePrinter;

public class Main_With_GUI {
    private static final GameDataManager dataManager = GameDataManager.getInstance();
    private static final FileStorageService fileStorageService = new FileStorageService();
    private static final AuthenticationService authService = new AuthenticationService();
    private static final SearchService searchService = new SearchService();
    private static final RecommendationService recommendationService = new RecommendationService();
    private static final CombatSimulatorService combatSimulatorService = new CombatSimulatorService();

    private static Player currentPlayer = null;

    private static JFrame frame;
    private static CardLayout cardLayout;
    private static JPanel sideControlPanel;
    private static JTextArea outputArea;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> setupWindowFramework());
    }

    private static void setupWindowFramework() {
        frame = new JFrame("Honor of Kings System Dashboard");
        frame.setSize(1200, 750);
        frame.setLocationRelativeTo(null); 
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        outputArea = new JTextArea();
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        outputArea.setEditable(false);
        outputArea.setBackground(new Color(248, 249, 250));
        JScrollPane textScrollPane = new JScrollPane(outputArea);
        textScrollPane.setBorder(BorderFactory.createTitledBorder("System Output"));
        frame.add(textScrollPane, BorderLayout.CENTER);

        interceptSystemStreams();

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                fileStorageService.saveData(dataManager);
                System.out.println("===================\nGOODBYE, EVERYPONY!\n===================");
                System.exit(0);
            }
        });

        cardLayout = new CardLayout();
        sideControlPanel = new JPanel(cardLayout);
        sideControlPanel.setPreferredSize(new Dimension(380, 750));
        sideControlPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        sideControlPanel.add(buildLoginPanel(), "LOGIN");
        sideControlPanel.add(buildAdminPanel(), "ADMIN");
        sideControlPanel.add(buildPlayerPanel(), "PLAYER");

        frame.add(sideControlPanel, BorderLayout.WEST);
        frame.setVisible(true);

        System.out.println("Attempting to load from saved data...");
        if (!fileStorageService.loadData(dataManager)) {
            DataInitializer.initialize();
            System.out.println("Data initialization complete!");
        }

        System.out.println("===================\nWELCOME, EVERYPONY!\n===================");
    }

    private static void interceptSystemStreams() {
        try {
            PrintStream GUIStream = new PrintStream(new OutputStream() {
                @Override
                public void write(int b) {}
                @Override
                public void write(byte[] b, int off, int len) {
                    String cleanText = new String(b, off, len, java.nio.charset.StandardCharsets.UTF_8);
                    SwingUtilities.invokeLater(() -> {
                        outputArea.append(cleanText);
                        outputArea.setCaretPosition(outputArea.getDocument().getLength());
                    });
                }
            }, true, "UTF-8");
            System.setOut(GUIStream);
            System.setErr(GUIStream);
        } catch (UnsupportedEncodingException e) {
            System.err.println("Encoding error.");
        }
    }

    private static String getGUIStringInput(String prompt) {
        String result = JOptionPane.showInputDialog(frame, prompt);
        return result != null ? result : "";
    }

    private static int getGUIIntInput(String prompt) {
        String result = JOptionPane.showInputDialog(frame, prompt);
        if (result == null || result.trim().isEmpty()) return -1;
        try {
            return Integer.parseInt(result.trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number.");
            return -1;
        }
    }

    private static JPanel buildLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel promptLabel = new JLabel("Please enter your name, or type \"exit\" to shut down:");
        gbc.gridy = 0; panel.add(promptLabel, gbc);

        JTextField nameInputField = new JTextField(15);
        gbc.gridy = 1; panel.add(nameInputField, gbc);

        JButton loginBtn = new JButton("Login");
        gbc.gridy = 2; panel.add(loginBtn, gbc);

        loginBtn.addActionListener(e -> {
            String name = nameInputField.getText().trim();
            if (name.equalsIgnoreCase("exit")) {
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                return;
            }

            Person thisPerson = authService.authenticateUser(name);

            if (thisPerson != null) {
                nameInputField.setText(""); 
                outputArea.setText(""); 

                if (thisPerson instanceof Admin) {
                    System.out.println("\nLog in success!");
                    System.out.println("Hi, Princess " + thisPerson.getName() + "! What's on your mind today?");
                    cardLayout.show(sideControlPanel, "ADMIN");
                } else if (thisPerson instanceof Player) {
                    currentPlayer = (Player) thisPerson;
                    System.out.println("\nLog in success!");
                    System.out.println("Hi, " + currentPlayer.getName() + "! You are such a cute little pony~ What's on your mind today?");
                    cardLayout.show(sideControlPanel, "PLAYER");
                }
            } else {
                JOptionPane.showMessageDialog(frame, 
                    "Hmmm, the system can't find your name, little pony. But fret not! You can re-enter your name or ask the princesses to help you create a new account if you don't have an existing account!", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
    }

    private static JPanel buildAdminPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        buttonsPanel.add(new JLabel("--- [1] View Information ---"));
        buttonsPanel.add(createBtn("View Players", e -> {
            String query = getGUIStringInput("Please enter player name or ID: ");
            if(!query.isEmpty()) ConsolePrinter.printPlayerDetails(searchService.findPlayerByIdOrName(query));
        }));
        buttonsPanel.add(createBtn("View Teams", e -> {
            String query = getGUIStringInput("Please enter team name or ID: ");
            if(!query.isEmpty()) ConsolePrinter.printTeamDetails(searchService.findTeamByIdOrName(query));
        }));
        buttonsPanel.add(createBtn("View Heroes", e -> {
            String query = getGUIStringInput("Please enter hero name or ID: ");
            if(!query.isEmpty()) ConsolePrinter.printHeroDetails(searchService.findHeroByIdOrName(query));
        }));
        buttonsPanel.add(createBtn("View Equipments", e -> {
            String query = getGUIStringInput("Please enter equipment name or ID: ");
            if(!query.isEmpty()) ConsolePrinter.printEquipmentDetails(searchService.findEquipmentByIdOrName(query));
        }));
        buttonsPanel.add(createBtn("View Match Records", e -> {
            int limit = getGUIIntInput("Please enter how many matches you would like to review: ");
            if (limit > 0) ConsolePrinter.printRecentMatches(limit);
        }));
        buttonsPanel.add(createBtn("Player Leaderboard", e -> {
            int topX = getGUIIntInput("How many players to show in leaderboard?: ");
            if (topX > 0) ConsolePrinter.printPlayerLeaderboard(topX);
        }));
        buttonsPanel.add(createBtn("Equipment Leaderboard", e -> ConsolePrinter.printEquipmentRanking()));

        buttonsPanel.add(Box.createVerticalStrut(10));

        buttonsPanel.add(new JLabel("--- [2] Manage Players ---"));
        buttonsPanel.add(createBtn("Add a Player", e -> managePlayers(1)));
        buttonsPanel.add(createBtn("Remove a Player", e -> managePlayers(2)));
        buttonsPanel.add(createBtn("Update a Player", e -> managePlayers(3)));

        buttonsPanel.add(Box.createVerticalStrut(10));

        buttonsPanel.add(new JLabel("--- [3] Manage Teams ---"));
        buttonsPanel.add(createBtn("Add a Team", e -> manageTeams(1)));
        buttonsPanel.add(createBtn("Remove a Team", e -> manageTeams(2)));
        buttonsPanel.add(createBtn("Update a Team", e -> manageTeams(3)));

        buttonsPanel.add(Box.createVerticalStrut(10));

        buttonsPanel.add(new JLabel("--- [4] Manage Match Records ---"));
        buttonsPanel.add(createBtn("Add a Match Record", e -> manageMatchRecords(1)));
        buttonsPanel.add(createBtn("Remove a Match Record", e -> manageMatchRecords(2)));
        buttonsPanel.add(createBtn("Update Match Record", e -> manageMatchRecords(3)));

        buttonsPanel.add(Box.createVerticalStrut(10));

        buttonsPanel.add(new JLabel("--- [5] Manage Heroes ---"));
        buttonsPanel.add(createBtn("Add a Hero", e -> manageHeroes(1)));
        buttonsPanel.add(createBtn("Remove a Hero", e -> manageHeroes(2)));
        buttonsPanel.add(createBtn("Update a Hero", e -> manageHeroes(3)));

        buttonsPanel.add(Box.createVerticalStrut(10));

        buttonsPanel.add(new JLabel("--- [6] Manage Equipment ---"));
        buttonsPanel.add(createBtn("Add Equipment", e -> manageEquipment(1)));
        buttonsPanel.add(createBtn("Remove Equipment", e -> manageEquipment(2)));
        buttonsPanel.add(createBtn("Update Equipment", e -> manageEquipment(3)));

        buttonsPanel.add(Box.createVerticalStrut(15));
        
        JButton logoutBtn = createBtn("[0] Logout", e -> {
            System.out.println("Logging out...");
            outputArea.setText("");
            cardLayout.show(sideControlPanel, "LOGIN");
        });
        logoutBtn.setBackground(new Color(217, 83, 79));
        logoutBtn.setForeground(Color.WHITE);
        buttonsPanel.add(logoutBtn);

        JScrollPane scrollPane = new JScrollPane(buttonsPanel);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private static JPanel buildPlayerPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(createBtn("[1] View My Profile", e -> ConsolePrinter.printPlayerDetails(currentPlayer)));
        panel.add(createBtn("[2] Edit My Name", e -> {
            String newName = getGUIStringInput("Please enter your new name (or press enter to keep '" + currentPlayer.getName() + "'): ");
            if (!newName.isEmpty()) {
                currentPlayer.setName(newName);
                dataManager.updatePlayer(currentPlayer);
                System.out.println("Name updated successfully!"); 
            }
        }));
        panel.add(createBtn("[3] View Teams", e -> {
            String query = getGUIStringInput("Please enter team name: ");
            if(!query.isEmpty()) ConsolePrinter.printTeamDetails(searchService.findTeamByIdOrName(query));
        }));
        panel.add(createBtn("[4] View Heroes", e -> {
            String query = getGUIStringInput("Please enter hero name or ID: ");
            if(!query.isEmpty()) ConsolePrinter.printHeroDetails(searchService.findHeroByIdOrName(query));
        }));
        panel.add(createBtn("[5] View Global Match Records", e -> {
            int limit = getGUIIntInput("Please enter how many matches you would like to review: ");
            if(limit > 0) ConsolePrinter.printRecentMatches(limit);
        }));
        panel.add(createBtn("[6] View Own Match Records", e -> {
            int limit = getGUIIntInput("How many matches to show?: ");
            if(limit > 0) ConsolePrinter.printMatchHistory(searchService.getMatchHistoryForPlayer(currentPlayer.getId(), limit), currentPlayer.getName());
        }));
        panel.add(createBtn("[7] View Leaderboards", e -> {
            int topX = getGUIIntInput("How many players to show in leaderboard?: ");
            if(topX > 0) ConsolePrinter.printPlayerLeaderboard(topX);
        }));
        panel.add(createBtn("[8] Equipment Recommendation", e -> {
            String query = getGUIStringInput("Which hero do you want equipment recommendations for? (Enter name/ID): ");
            Hero hero = searchService.findHeroByIdOrName(query);
            if (hero != null) {
                int limit = getGUIIntInput("How many items should we recommend?: ");
                if(limit > 0) ConsolePrinter.printRecommendations(hero, recommendationService.recommendEquipment(currentPlayer, hero, limit));
            } else {
                System.out.println("We couldn't find that hero in the database!");
            }
        }));
        panel.add(createBtn("[9] Combat Simulation", e -> {
            String myHeroQuery = getGUIStringInput("Enter the name or ID of the hero you want to use from your inventory: ");
            Hero myHero = searchService.findHeroByIdOrName(myHeroQuery);
            if (myHero == null) {
                System.out.println("We couldn't find that hero in the database!");
                return;
            }
            if (!currentPlayer.getOwnedHeroes().contains(myHero)) {
                System.out.println("You don't own this hero! Please select a hero from your inventory.");
                return;
            }
            String opponentHeroQuery = getGUIStringInput("Enter the name or ID of the opponent hero you wish to challenge: ");
            Hero opponentHero = searchService.findHeroByIdOrName(opponentHeroQuery);
            if (opponentHero == null) {
                System.out.println("We couldn't find that opponent hero in the database!");
                return;
            }
            List<String> combatReport = combatSimulatorService.simulateCombat(myHero, opponentHero);
            ConsolePrinter.printCombatReport(combatReport);
        }));

        panel.add(Box.createVerticalStrut(15));
        JButton logoutBtn = createBtn("[0] Log out", e -> {
            currentPlayer = null;
            outputArea.setText("");
            cardLayout.show(sideControlPanel, "LOGIN");
        });
        logoutBtn.setBackground(new Color(240, 173, 78));
        logoutBtn.setForeground(Color.WHITE);
        panel.add(logoutBtn);

        return panel;
    }

    private static JButton createBtn(String text, ActionListener action) {
        JButton btn = new JButton(text);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(300, 35));
        btn.addActionListener(action);
        return btn;
    }


    private static void managePlayers(int choice2) {
        switch (choice2) {
            case 1: {
                String playerToAdd = getGUIStringInput("Please enter the name of the player you want to add: ");
                if (playerToAdd.trim().isEmpty()) {
                    System.out.println("Player name cannot be empty!");
                    break;
                }
                dataManager.addPlayer(new Player(playerToAdd));
                System.out.println("Player added successfully!");
                break;
            }
            case 2: {
                String playerToRemoveId = getGUIStringInput("Please enter the ID or Name of the player you want to remove: ");
                Player p = searchService.findPlayerByIdOrName(playerToRemoveId);
                if (p != null && dataManager.removePlayer(p.getId())) {
                    System.out.println("Player removed successfully!");
                } else {
                    System.out.println("Player not found!");
                }
                break;
            }
            case 3: {
                String identifier = getGUIStringInput("Please enter the ID or Name of the player you want to update: ");
                Player playerToUpdate = searchService.findPlayerByIdOrName(identifier);
                if (playerToUpdate == null) {
                    System.out.println("Player not found! Returning...");
                    break;
                }
                System.out.println("Updating Player: " + playerToUpdate.getName());
                int newLevel = getGUIIntInput("Enter new level (current: " + playerToUpdate.getLevel() + "): ");
                if(newLevel > -1) playerToUpdate.setLevel(newLevel);

                String newName = getGUIStringInput("Enter new name (or press enter to keep '" + playerToUpdate.getName() + "'): ");
                if (!newName.isEmpty()) {
                    playerToUpdate.setName(newName);
                }

                String assignTeam = getGUIStringInput("Would you like to assign this player to a team? (y/n): ");
                if (assignTeam.equalsIgnoreCase("y")) {
                    String teamQuery = getGUIStringInput("Please enter the ID or Name of the team: ");
                    Team foundTeam = searchService.findTeamByIdOrName(teamQuery);
                    if (foundTeam != null) {
                        Team currentTeam = playerToUpdate.getOwnTeam();
                        if (currentTeam != null && !currentTeam.getId().equals(foundTeam.getId())) {
                            currentTeam.getMembers().remove(playerToUpdate);
                        }
                        if (currentTeam == null || !currentTeam.getId().equals(foundTeam.getId())) {
                            if (!foundTeam.getMembers().contains(playerToUpdate)) {
                                foundTeam.getMembers().add(playerToUpdate);
                            }
                            playerToUpdate.setOwnTeam(foundTeam);
                            System.out.println("Assigned " + playerToUpdate.getName() + " to team " + foundTeam.getName() + "!");
                        } else {
                            System.out.println("Player is already in this team.");
                        }
                    } else {
                        System.out.println("Team not found! Skipping team assignment.");
                    }
                }
                dataManager.updatePlayer(playerToUpdate);
                System.out.println("Player updated successfully!");
                break;
            }
        }
    }

    private static void manageTeams(int choice2) {
        switch (choice2) {
            case 1: {
                String teamName = getGUIStringInput("Please enter the name of the new team: ");
                if (teamName.trim().isEmpty()) {
                    System.out.println("Team name cannot be empty!");
                    break;
                }
                dataManager.addTeam(new Team(teamName, new java.util.ArrayList<>()));
                System.out.println("Team added successfully!");
                break;
            }
            case 2: {
                String teamIdentifier = getGUIStringInput("Please enter the ID or Name of the team to remove: ");
                Team t = searchService.findTeamByIdOrName(teamIdentifier);
                if (t != null && dataManager.removeTeam(t.getId())) {
                    System.out.println("Team removed successfully! All associated matches were rolled back.");
                } else {
                    System.out.println("Team not found!");
                }
                break;
            }
            case 3: {
                String identifier = getGUIStringInput("Please enter the ID or Name of the team to update: ");
                Team teamToUpdate = searchService.findTeamByIdOrName(identifier);
                if (teamToUpdate == null) {
                    System.out.println("Team not found! Returning...");
                    break;
                }
                System.out.println("Updating Team: " + teamToUpdate.getName());
                String newName = getGUIStringInput("Enter new name (or press enter to keep '" + teamToUpdate.getName() + "'): ");
                if (!newName.isEmpty()) {
                    teamToUpdate.setName(newName);
                }

                String assignPlayer = getGUIStringInput("Would you like to assign an existing player to this team? (y/n): ");
                if (assignPlayer.equalsIgnoreCase("y")) {
                    String playerQuery = getGUIStringInput("Please enter the ID or Name of the player: ");
                    Player foundPlayer = searchService.findPlayerByIdOrName(playerQuery);
                    if (foundPlayer != null) {
                        Team currentTeam = foundPlayer.getOwnTeam();
                        if (currentTeam != null && !currentTeam.getId().equals(teamToUpdate.getId())) {
                            currentTeam.getMembers().remove(foundPlayer);
                        }
                        if (currentTeam == null || !currentTeam.getId().equals(teamToUpdate.getId())) {
                            if (!teamToUpdate.getMembers().contains(foundPlayer)) {
                                teamToUpdate.getMembers().add(foundPlayer);
                            }
                            foundPlayer.setOwnTeam(teamToUpdate);
                            System.out.println("Assigned " + foundPlayer.getName() + " to team " + teamToUpdate.getName() + "!");
                            dataManager.updatePlayer(foundPlayer);
                        } else {
                            System.out.println("Player is already in this team.");
                        }
                    } else {
                        System.out.println("Player not found! Skipping player assignment.");
                    }
                }
                dataManager.updateTeam(teamToUpdate);
                System.out.println("Team updated successfully!");
                break;
            }
        }
    }

    private static void manageMatchRecords(int choice2) {
        switch (choice2) {
            case 1: {
                String ta = getGUIStringInput("Enter Team A name or ID: ");
                Team teamA = searchService.findTeamByIdOrName(ta);
                String tb = getGUIStringInput("Enter Team B name or ID: ");
                Team teamB = searchService.findTeamByIdOrName(tb);
                if (teamA == null || teamB == null) {
                    System.out.println("One or both teams could not be found. Aborting.");
                    break;
                }

                int res = getGUIIntInput("Who won? [1] Team A  [2] Team B  [3] Draw : ");
                MatchResult mr = (res == 1) ? MatchResult.TEAM_A_WIN
                        : (res == 2) ? MatchResult.TEAM_B_WIN : MatchResult.DRAW;

                MatchRecord record = new MatchRecord(teamA, teamB, mr);

                System.out.println("Please enter the hero ID or Name picked by each player:");
                for (Player p : teamA.getMembers()) {
                    String hQuery = getGUIStringInput(p.getName() + " picked: ");
                    Hero h = searchService.findHeroByIdOrName(hQuery);
                    if (h != null) {
                        record.addPick(p.getId(), h.getId());
                    } else {
                        System.out.println("Hero not found. Skipping pick for " + p.getName());
                    }
                }
                for (Player p : teamB.getMembers()) {
                    String hQuery = getGUIStringInput(p.getName() + " picked: ");
                    Hero h = searchService.findHeroByIdOrName(hQuery);
                    if (h != null) {
                        record.addPick(p.getId(), h.getId());
                    } else {
                        System.out.println("Hero not found. Skipping pick for " + p.getName());
                    }
                }

                dataManager.addMatchRecord(record);
                System.out.println("Match recorded successfully! Team statistics have been updated.");
                break;
            }
            case 2: {
                String matchId = getGUIStringInput("Please enter the exact ID of the match to remove: ");
                if (dataManager.removeMatchRecord(matchId)) {
                    System.out.println("Match removed! Team and player statistics have been safely rolled back.");
                } else {
                    System.out.println("Match record not found.");
                }
                break;
            }
            case 3: {
                String matchId = getGUIStringInput("Please enter the exact ID of the match to update: ");
                MatchRecord recordToUpdate = searchService.findMatchRecordById(matchId);

                if (recordToUpdate == null) {
                    System.out.println("Match record not found! Returning...");
                    break;
                }

                System.out.println("Updating Match: " + recordToUpdate.getTeamA().getName() + " vs " + recordToUpdate.getTeamB().getName());
                System.out.println("Current Result: " + recordToUpdate.getResult());

                int newRes = getGUIIntInput("Enter corrected result [1] Team A Win  [2] Team B Win  [3] Draw : ");
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
                System.out.println("Match record updated! Team and player statistics have been safely recalculated.");
                break;
            }
        }
    }

    private static void manageHeroes(int choice2) {
        switch (choice2) {
            case 1: {
                String name = getGUIStringInput("Enter Hero Name: ");
                if (name.trim().isEmpty()) {
                    System.out.println("Hero name cannot be empty!");
                    break;
                }
                String typeStr = getGUIStringInput("Enter Hero Type " + java.util.Arrays.toString(HeroType.values()) + ": ").toUpperCase();
                try {
                    HeroType type = HeroType.valueOf(typeStr);
                    int hp = getGUIIntInput("Enter Base HP: ");
                    int atk = getGUIIntInput("Enter Base Attack: ");
                    dataManager.addHero(new Hero(name, type, hp, atk));
                    System.out.println("Hero added successfully!");
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid Hero Type! Aborting.");
                }
                break;
            }
            case 2: {
                String heroIdentifier = getGUIStringInput("Please enter the ID or name of the hero to remove: ");
                Hero h = searchService.findHeroByIdOrName(heroIdentifier);
                if (h != null && dataManager.removeHero(h.getId())) {
                    System.out.println("Hero removed successfully! Automatically deleted from player inventories.");
                } else {
                    System.out.println("Hero not found!");
                }
                break;
            }
            case 3: {
                String identifier = getGUIStringInput("Please enter the ID or name of the hero to update: ");
                Hero heroToUpdate = searchService.findHeroByIdOrName(identifier);

                if (heroToUpdate == null) {
                    System.out.println("Hero not found! Returning...");
                    break;
                }

                System.out.println("Updating Hero: " + heroToUpdate.getName());
                String newName = getGUIStringInput("Enter new name (or press enter to keep '" + heroToUpdate.getName() + "'): ");
                if (!newName.isEmpty()) {
                    heroToUpdate.setName(newName);
                }

                int newHp = getGUIIntInput("Enter new Base HP (current: " + heroToUpdate.getBaseHp() + "): ");
                if(newHp > -1) heroToUpdate.setBaseHp(newHp);

                dataManager.updateHero(heroToUpdate);
                System.out.println("Hero updated successfully!");
                break;
            }
        }
    }

    private static void manageEquipment(int choice2) {
        switch (choice2) {
            case 1: {
                String eqName = getGUIStringInput("Enter the name of the new equipment: ");
                if (eqName.trim().isEmpty()) {
                    System.out.println("Equipment name cannot be empty!");
                    break;
                }
                dataManager.addEquipment(new Equipment(eqName));
                System.out.println("Equipment added successfully!");
                break;
            }
            case 2: {
                String eqName = getGUIStringInput("Enter the exact name or ID of the equipment to remove: ");
                Equipment foundEq = searchService.findEquipmentByIdOrName(eqName);

                if (foundEq != null && dataManager.removeEquipment(foundEq.getId())) {
                    System.out.println("Equipment removed successfully! Safely detached from all heroes.");
                } else {
                    System.out.println("Equipment not found!");
                }
                break;
            }
            case 3: {
                String eqName = getGUIStringInput("Enter the name or ID of the equipment to update: ");
                Equipment eqToUpdate = searchService.findEquipmentByIdOrName(eqName);

                if (eqToUpdate == null) {
                    System.out.println("Equipment not found! Returning...");
                    break;
                }

                System.out.println("Updating Equipment: " + eqToUpdate.getName());
                String newName = getGUIStringInput("Enter new name (or press enter to keep '" + eqToUpdate.getName() + "'): ");
                if (!newName.isEmpty()) {
                    eqToUpdate.setName(newName);
                }

                dataManager.updateEquipment(eqToUpdate);
                System.out.println("Equipment updated successfully!");
                break;
            }
        }
    }
}