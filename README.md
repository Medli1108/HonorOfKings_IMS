# AI-Assisted Honor of Kings Information Management System

## 1. Project Overview
This project is an Object-Oriented Java application designed to simulate an Information Management System for the game *Honor of Kings*. Developed to fulfill the Java OOP coursework requirements, this system demonstrates core programming concepts, structural design patterns (like the Singleton pattern for data management), and responsible, documented usage of AI coding assistants. The application manages core game entities including Players, Teams, Heroes, Equipment, and Match Records, supporting complex querying, statistical recalculations, and complete data persistence.

## 2. How to Run
The project provides two execution modes: a traditional console-based interface and a modern Java Swing Graphical User Interface (GUI).

1. **Compile the project:** Ensure you have the Java Development Kit (JDK) installed. Compile all `.java` files inside the `src/` directory.
2. **Run the Console Version:** Execute `Main.java` to start the terminal-based menu loop.
3. **Run the GUI Version:** Execute `Main_With_GUI.java` to launch the desktop application shell.
4. **Data Initialization:** Upon the first launch, if no saved data is found, the system will automatically generate a mock dataset and save the records to CSV files in the `data/` directory (invisible on GitHub since it is empty). Subsequent launches will dynamically load from these files.

## 3. Default Login Accounts
The system utilizes a mock dataset initialized with distinct character names. You can log in by typing any of the following names when prompted:

* **Admin Accounts (Full Management Access):** `Twilight Sparkle`, `Celestia`, `Luna`, `Cadance`
* **Player Accounts (View-Only & Personal Edits):** `Applejack`, `Pinkie Pie`, `Rainbow Dash`, `Fluttershy`, `Rarity`, `Starlight Glimmer`, `Sunset Shimmer`, `Coco Pommel`, `Derpy`, `Vapor Trail`, `Lyra Heartstrings`, `Minuette`, `Berry Punch`, `Bon Bon`, `Spitfire`
* *System Command:* Type `exit` at the login prompt to safely write all memory states to the CSV files and shut down the application.

## 4. Implemented Features
* **Role-Based Routing:** Distinct permissions, views, and dashboards for Admins and Players.
* **Data Management (CRUD):** Admins can add, update, and remove players, teams, heroes, equipment, and match records. The backend rigorously handles cascade deletions (e.g., deleting a team scrubs its match history).
* **Search & Lookup:** Find specific entities by ID or Name using a decoupled `SearchService`.
* **Leaderboards & Rankings:** Dynamically generated sorting for Players (by Win Rate, then Level tie-breaker) and Equipment (by Win Rate, then Usage Count).
* **Combat Simulation (Extra Credit):** A turn-based battle simulator (`CombatSimulatorService`) that calculates effective statistics based on equipment ratings, factoring in dodge chances and critical hits.
* **Recommendation Engine (Extra Credit):** Recommends optimal equipment for specific heroes based on global win rates, popularity, and innate hero compatibility.
* **Graphical User Interface (Extra Credit):** A functional desktop UI shell built in Java Swing, featuring real-time terminal output interception to preserve the console logging aesthetic.
* **Data Persistence (Extra Credit):** Complete CSV serialization and deserialization (`FileStorageService`) to ensure all application states are permanently preserved between sessions.

## 5. Java Concepts Used
* **Inheritance & Polymorphism:** Both `Player` and `Admin` extend the base abstract `Person` class.
* **Interfaces:** The `Searchable` interface is implemented by core models (Person, Team, Hero, Equipment) to enforce standardized ID and Name accessors for the search service.
* **Collections:** Extensive use of `List`, `ArrayList`, and `Map`/`HashMap` (specifically for mapping player IDs to their respective hero picks in Match Records).
* **Thread Safety:** Utilized `Collections.synchronizedList()` and defensive array copying within getters to prevent `ConcurrentModificationException` crashes during data mutations and GUI rendering.
* **Enums:** `Role`, `HeroType`, and `MatchResult` ensure strict type safety and structured branching logic.
* **File I/O:** `BufferedReader`, `PrintWriter`, and custom CSV character escaping used for robust database read/write operations.

## 6. AI Usage Summary
Artificial Intelligence (specifically Gemini Pro) was used transparently as an academic tool under three distinct personas:
1.  **Architect Agent:** Brainstormed class structures, decoupled the service layer (e.g., extracting `SearchService` and `GameDataManager`), and engineered the Swing GUI event loop framework.
2.  **Implementation Agent:** Generated repetitive boilerplate code, formatted CSV serialization logic, and built the mathematical formulas for the combat simulator.
3.  **Testing/Reviewer Agent:** Identified edge cases (such as the "Exit" lockout flaw, duplicate name shadowing traps, and ghost object desyncs) and suggested vital thread-safety refactors.
All prompts, AI responses, and human architectural decisions are heavily documented in `ai/prompts.md` and `docs/agent-log.md`.

## 7. Testing Summary
Rigorous manual testing was conducted and documented in `docs/test-cases.md`, totaling 35 detailed scenarios. Key verification areas included:
* Authentication boundary enforcement and invalid login fallbacks.
* Mathematical integrity of post-match win/loss rollbacks and team statistical distributions.
* Cascade deletion safety (verifying that deleted entities are fully scrubbed from player inventories and team rosters without throwing `NullPointerException`).
* File loading sequences, date-parsing integrity, and safe shutdown triggers.

## 8. Known Limitations
* **CSV Relational Constraints:** Relying on flat `.csv` files for a highly relational dataset requires complex parsing and manual ID re-mapping upon load. A relational database utilizing JDBC/SQL would be significantly more efficient for a production-scale application.
* **Exact Name Matching:** The current `SearchService` architecture relies heavily on exact string matches (ignoring case). Fuzzy search or partial substring matching is not yet supported.
* **Swing GUI Boilerplate:** While the Swing framework successfully wraps the console commands, the layout relies heavily on `JOptionPane` dialogs rather than bespoke input panels.

## 9. ???

# ***Friendship is Magic***
