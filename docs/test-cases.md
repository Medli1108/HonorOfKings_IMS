## Test 01: Admin Authentication Login
**Function Tested:** Authentication Service Routing
**Input:** Login name: "Twilight Sparkle"
**Expected:** The system identifies the user's Role as ADMIN and routes them to the Admin management menu.
**Actual:** System correctly identified the Admin role and displayed the Admin management interface.
**Result:** Pass

## Test 02: Player Authentication Login
**Function Tested:** Authentication Service Routing
**Input:** Login name: "Applejack"
**Expected:** The system identifies the user's Role as PLAYER and routes them to the Player view-only menu.
**Actual:** System correctly identified the Player role and displayed the Player profile interface.
**Result:** Pass

## Test 03: Invalid Login Fallback
**Function Tested:** Authentication Error Handling
**Input:** Login name: "Unknown Pony"
**Expected:** The system fails to find the account, denies access, and prompts the user to try again or exit.
**Actual:** The system displayed the error message and successfully looped back to the retry prompt.
**Result:** Pass

## Test 04: Player Lookup by Name
**Function Tested:** Player Search and Details Output
**Input:** Search player name: "Rainbow Dash"
**Expected:** The system displays Rainbow Dash's team, level, owned heroes, and equipped items.
**Actual:** The system fetched the correct `Player` object and printed all formatting grids accurately.
**Result:** Pass

## Test 05: Team Overview Data Integrity
**Function Tested:** Team Search and Math Calculations
**Input:** Search team name: "Team Pegasus"
**Expected:** The system displays the team members, average level, win rate, and calculates the top player.
**Actual:** The system displayed the team roster and correctly executed the average level and win rate calculations without throwing division-by-zero exceptions.
**Result:** Pass

## Test 06: Hero Details and Ownership Mapping
**Function Tested:** Hero Search and Bidirectional Lookup
**Input:** Search hero name: "Gerald"
**Expected:** The system displays Gerald's base stats, compatible/recommended equipment, and lists all players who currently own him.
**Actual:** The system correctly mapped the `Hero` object back to the `Player` list and displayed the owning players.
**Result:** Pass

## Test 07: Global Equipment Ranking
**Function Tested:** Complex List Sorting (`RankingService`)
**Input:** Menu selection: [7] Equipment Leaderboard
**Expected:** The system sorts all equipment first by win rate (descending), and then by usage count (descending) as a tie-breaker.
**Actual:** The system printed the ranked array accurately.
**Result:** Pass

## Test 08: Input Mismatch Exception Handling
**Function Tested:** `InputHelper` format safety
**Input:** Prompt: "Please enter your selection:" 
User Input: "five" (String instead of Int)
**Expected:** The system catches the `NumberFormatException` and prompts the user to enter a valid number without crashing.
**Actual:** The scanner caught the exception and successfully looped the prompt.
**Result:** Pass

## Test 09: Cascade Deletion (Remove Team)
**Function Tested:** Data Integrity upon Object Removal
**Input:** Admin Menu -> Manage Teams -> Remove Team -> "Team Earth Pony"
**Expected:** The team is deleted, all match records involving this team are scrubbed from the system, and the players' `ownTeam` references are set to null.
**Actual:** Team deleted, match records purged, but players remained safely in the database without dangling pointers.
**Result:** Pass

## Test 10: Manual Match Record Creation
**Function Tested:** Admin Game Data Management
**Input:** Admin Menu -> Manage Match Records -> Add Match (Team Pegasus vs Team Unicorn)
**Expected:** The match is recorded, and the winning/losing team's overall statistics (and their players' statistics) update accordingly.
**Actual:** The match record was created and team wins/losses were updated. However, hero pick rates and equipment win rates did not update.
**Result:** Pass but with flaws

## Test 11: Duplicate Player Creation
**Function Tested:** Admin Data Management (Add Player)
**Input:** Admin Menu -> Manage Players -> Add a Player -> Name: "Applejack" (A name that already exists in the dataset).
**Expected:** The system alerts the admin that the name is taken, or forces the creation of a unique identifier.
**Actual:** The system accepted the input and created a second, distinct "Applejack" object with a brand new UUID.
**Result:** Pass (The execution succeeded without crashing, but sets up a data flaw).

## Test 12: The Shadowing Trap
**Function Tested:** SearchService Name Resolution
**Input:** Admin Menu -> Manage Players -> Update a Player -> Name: "Applejack"
**Expected:** The system prompts the Admin to clarify *which* Applejack they want to update (perhaps by showing their UUIDs or Levels).
**Actual:** The system automatically fetched and updated the first "Applejack" found in the underlying `ArrayList`. The newly created duplicate Applejack is completely unreachable by name.
**Result:** Fail
**Bug Found:** Duplicate Name Shadowing Trap. The `SearchService` returns the first match it finds, leaving duplicates orphaned.

## Test 13: Manual Match Creation (Data Entry)
**Function Tested:** Admin Data Management (Add Match Record)
**Input:** Admin Menu -> Manage Match Records -> Add a Match Record -> Team A: "Team Earth Pony", Team B: "Team Pegasus", Winner: "1" (Team A).
**Expected:** The system records the match, updates the win/loss records for the teams, and prompts the Admin to input the 10 specific heroes picked by the players.
**Actual:** The match was recorded and team stats were updated, but the console loop instantly returned to the menu without ever asking for hero picks.
**Result:** Pass (Execution succeeded, but creates incomplete data).

## Test 14: Equipment Desync Verification
**Function Tested:** Hero and Equipment Statistic Recalculation
**Input:** Admin Menu -> View Information -> Equipment Leaderboard
**Expected:** Equipment win rates and usage counts shift slightly due to the newly added match from Test 13.
**Actual:** Equipment statistics remained entirely frozen. Because the manual match generated an empty `playerHeroPicks` map, the `GameDataManager` had no data to pass down to the equipment calculation loop.
**Result:** Fail
**Bug Found:** The "Ghost Match" Equipment Desync.

## Test 15: Empty String Validation (Admin Creation)
**Function Tested:** `InputHelper` & Validation Logic
**Input:** Admin Menu -> Manage Players -> Add a Player -> Name: "   " (Only whitespace)
**Expected:** The system strips the whitespace and rejects the empty string to prevent nameless players.
**Actual:** The system recognized the empty string, printed "Player name cannot be empty!", and safely returned to the menu.
**Result:** Pass

## Test 16: Invalid Enum Handling
**Function Tested:** Hero Creation Type Parsing
**Input:** Manage Heroes -> Add a Hero -> Name: "Discord", Type: "CHAOS"
**Expected:** The system catches the `IllegalArgumentException` triggered by `HeroType.valueOf()` and prevents a fatal crash.
**Actual:** The system caught the exception, outputted "Invalid Hero Type! Aborting.", and safely dropped back to the menu.
**Result:** Pass

## Test 17: Self-Edit Name Cancellation
**Function Tested:** Player Profile Modification
**Input:** Player Menu -> Edit My Name -> New Name: [Enter Key] (Empty Input)
**Expected:** The system cancels the update process and retains the player's original name.
**Actual:** The system evaluated `!newName.isEmpty()`, skipped the update block, and successfully kept the original name.
**Result:** Pass

## Test 18: Non-Existent Entity Removal
**Function Tested:** Cascade Deletion Safety Checks
**Input:** Admin Menu -> Manage Teams -> Remove a Team -> "Team Alicorn"
**Expected:** The system recognizes the query doesn't match any team and cancels the deletion logic without throwing a `NullPointerException`.
**Actual:** The `SearchService` returned null, triggering the fallback message: "Team not found!".
**Result:** Pass

## Test 19: Leaderboard Out-Of-Bounds Handling
**Function Tested:** `RankingService` Sublist Generation
**Input:** Player Menu -> View Leaderboards -> Prompt: "How many players to show?": 1000
**Expected:** The system displays all available players without crashing via an `IndexOutOfBoundsException`.
**Actual:** The `getPlayerLeaderboard` method detected that `topX` was larger than the dataset size and safely returned the entire sorted list.
**Result:** Pass

## Test 20: Match Record Result Rollback
**Function Tested:** Match Update Mathematical Integrity
**Input:** Admin Menu -> Manage Match Records -> Update Match Record -> [Valid Match ID] -> Change winner from Team A to Draw.
**Expected:** The system rolls back the old wins/losses, applies the new result, and preserves the original hero picks map.
**Actual:** The `tempUpdatedRecord` successfully cloned the `HashMap` of hero picks, ensuring that when the record was removed and re-added to calculate the new result, the granular player-hero statistics were not lost.
**Result:** Pass

## Test 21: The "Exit" Identity Lockout
**Function Tested:** Authentication Loop and System Shutdown (`Main.java`)
**Input:** 1. Admin Menu -> Add a Player -> Name: "exit"
2. Log out. 
3. Login prompt -> enter: "exit"
**Expected:** The system bypasses the player login and gracefully shuts down the application as intended by the shutdown keyword.
**Actual:** Because the `AuthenticationService` successfully finds a player named "exit", `thisPerson` is not null. The shutdown condition `if (thisPerson == null && name.equalsIgnoreCase("exit"))` evaluates to false. The system logs the user in as the player "exit" instead of shutting down.
**Result:** Fail
**Bug Found:** Logic flaw in authentication prioritization. A user maliciously or accidentally named "exit" effectively traps everyone in the system, disabling the ability to trigger the Phase 5 shutdown sequence.

## Test 22: The Empty Team Roster Trap
**Function Tested:** Admin Data Management (`manageTeams` and `managePlayers` in `Main.java`)
**Input:** 1. Admin Menu -> Manage Teams -> Add a Team ("Team Wonderbolts"). 
2. Admin Menu -> Manage Players -> Update a Player -> Name: "Spitfire". Attempt to assign Spitfire to "Team Wonderbolts".
**Expected:** The Admin interface prompts the user to select a team to assign the player to, successfully linking the `Player` object to the `Team` object.
**Actual:** `manageTeams()` creates a team with an empty `ArrayList`. `managePlayers()` only prompts the Admin to update "Level" and "Name". There is zero UI code in `Main.java` allowing an Admin to link a player to a team.
**Result:** Fail
**Bug Found:** Missing UI business logic. Any newly created team will permanently have 0 members, and a newly created player can never join a team without directly editing the `.csv` file.

## Test 23: The "Ghost Match" Missing Prompts (Unresolved)
**Function Tested:** `manageMatchRecords()` - Match Creation
**Input:** Admin Menu -> Manage Match Records -> Add a Match Record (Team Earth Pony vs Team Pegasus).
**Expected:** After the Admin records the teams and the winner, the console loops through the 10 participating players and prompts the Admin to input which `Hero` each player picked, so the `HashMap` can be passed to the backend.
**Actual:** `Main.java` instantiates `new MatchRecord(teamA, teamB, mr)` which defaults to an empty `playerHeroPicks` HashMap. The UI immediately prints "Match recorded successfully!" and returns to the menu. 
**Result:** Fail
**Bug Found:** Because the UI never asks for the hero picks, the backend delta-math (`updateTeamPostMatch`) has no hero IDs to look up, meaning `Equipment` win rates and usage counts will permanently stagnate for all manually added matches.

## Test 24: Mock Data Statistical De-sync
**Function Tested:** `DataInitializer.java` Data Integrity
**Input:** Boot the system for the very first time (triggering `DataInitializer`). Go to `[1] View information -> [7] Equipment Leaderboard`. Pick an equipment item and note its usage count and win rate. Then, manually tally how many times that item actually appears in the 10 generated Match Records.
**Expected:** The Equipment's `usageCount` and `wins` should perfectly match the mathematical reality of the mock matches it participated in.
**Actual:** `DataInitializer` calculates Team and Player stats via an accurate simulation loop, but at the very end of the file, it completely fakes Equipment stats using `rand.nextInt(500)`. 
**Result:** Fail
**Bug Found:** Mock data integrity breach. The initialized equipment statistics are artificially inflated and completely divorced from the actual generated match histories.

## Test 25: Unsafe Null Printing in Match History
**Function Tested:** `ConsolePrinter.printMatchHistory()`
**Input:** Admin Menu -> Manage Teams -> Remove Team ("Team Unicorn"). Then, go to Player Menu -> View Own Match Records (for a player who previously played against Team Unicorn). 
**Expected:** The system prints the match history, safely handling the fact that the opposing team no longer exists in the database.
**Actual:** `ConsolePrinter.java` executes `match.getTeamB().getName()`. Because the team was purged, `getTeamB()` returns `null`, and calling `.getName()` on it triggers a fatal `NullPointerException`, crashing the application.
**Result:** Fail
**Bug Found:** Missing null-safety checks in the UI layer. `printMatchHistory` forgets the ternary operator safeguard entirely.

## Test 26: Empty Name Bypass for Heroes and Equipment
**Function Tested:** `InputHelper` Validation in `Main.java` (`manageHeroes()` and `manageEquipment()`)
**Input:** Admin Menu -> Manage Heroes -> Add a Hero -> Name: "   " (Whitespace/Empty String)
**Expected:** The system strips the whitespace, rejects the empty string, and alerts the admin to enter a valid name (mirroring the safe validation logic already implemented for adding a Player or Team).
**Actual:** The system accepts the empty string without validation and creates a nameless hero, which disrupts console UI formatting layouts and creates an unsearchable entity.
**Result:** Fail
**Bug Found:** Missing `.trim().isEmpty()` validation check for the Hero and Equipment creation workflows in the UI layer.

## Test 27: Unowned Hero Equipment Desync
**Function Tested:** Match Record Pick Validation and `updateTeamPostMatch()`
**Input:** Admin Menu -> Manage Match Records -> Add Match Record. When prompted for player picks, the Admin enters a valid Hero ID that exists in the database, but that the specific Player does *not* currently own in their personal inventory.
**Expected:** The system should either reject the hero pick for that specific player, OR it should accept it and correctly update the equipment statistics (usage count, win rate) for the items associated with that hero.
**Actual:** The UI accepts the hero pick and successfully logs it in the match record. However, the `GameDataManager` silently skips the equipment math because the nested loops in `updateTeamPostMatch()` only iterate through `player.getOwnedHeroes()`. The match is recorded, but the equipment statistics become permanently desynced from the actual play history.
**Result:** Fail
**Bug Found:** Data consistency bug between global match histories and local player inventories. There is no UI validation in `Main.java` to ensure a player actually owns a hero before logging it in a match pick.

## Test 28: Team Transfer Match History Erasure
**Function Tested:** `SearchService.getMatchHistoryForPlayer()`
**Input:** 1. Player A plays 5 matches while assigned to "Team Earth Pony".
2. The Admin goes to Manage Players -> Update Player, and transfers Player A to "Team Pegasus".
3. Player A logs in and selects `[6] View Own Match Records`.
**Expected:** The system displays the 5 match records Player A personally participated in while they were on Team Earth Pony.
**Actual:** The system only returns matches involving Player A's *current* team ("Team Pegasus"). Player A's personal historical match logs completely vanish from their view, and they falsely "inherit" matches that Team Pegasus played before the player joined the roster.
**Result:** Fail
**Bug Found:** Logic flaw in `SearchService`. Instead of scanning the `MatchRecord.playerHeroPicks` map to verify if a player actually participated in a historical game, the search blindly queries match history using the player's active `getOwnTeam().getId()`.

## Test 29: Data Persistence - Safe Shutdown Saving
**Function Tested:** Phase 5 System Shutdown (`FileStorageService.saveData`)
**Input:** Admin logs in -> Adds new equipment named "Alicorn Amulet" -> Logs out -> Types "exit" at the login prompt.
**Expected:** The system triggers Phase 5, iterates through all `GameDataManager` collections, and writes the in-memory data to the CSV files before the JVM terminates.
**Actual:** The system printed "GOODBYE, EVERYPONY!" and a manual check of `data/equipments.csv` confirmed that the newly created "Alicorn Amulet" was appended to the bottom of the file.
**Result:** Pass

## Test 30: Data Persistence - File Loading Sequence
**Function Tested:** Phase 1 System Initialization (`FileStorageService.loadData`)
**Input:** Relaunch the application after executing Test 29. Log in as a Player and search for the "Alicorn Amulet".
**Expected:** The system detects the existing `.csv` files, bypasses the `DataInitializer` mock data generation entirely, and accurately loads the previous session's memory state.
**Actual:** The console outputted "Data loaded successfully from CSV files!" and the `SearchService` successfully located the "Alicorn Amulet", proving the file I/O deserialization works.
**Result:** Pass

## Test 31: Tie-Breaker Logic for Player Leaderboards
**Function Tested:** `RankingService.getPlayerLeaderboard()` Comparator Logic
**Input:** Player Menu -> [7] View Leaderboards -> "How many players to show?": 10
**Expected:** When multiple players share an identical `winRate` (e.g., 0.50), the system must utilize the secondary `level` metric as a tie-breaker, ranking higher-level players above lower-level ones.
**Actual:** The system accurately sorted players like "Coco Pommel" (Level 30, 50% WR) above "Applejack" (Level 15, 50% WR) in the printed leaderboard array.
**Result:** Pass

## Test 32: Cascade Deletion for Equipped Items
**Function Tested:** `GameDataManager.removeEquipment()` Cascade Protocol
**Input:** Admin Menu -> Manage Equipment -> Remove Equipment -> "The Master Sword". Then, view the Hero Details for "Gerald".
**Expected:** "The Master Sword" is deleted globally, and the backend safely iterates through all `Hero` objects to scrub the item from their `compatibleEquipments`, `recommendedEquipments`, and `currentEquipments` arrays.
**Actual:** The equipment was successfully removed without triggering a `ConcurrentModificationException`, and it no longer appeared in Gerald's equipped items list.
**Result:** Pass

## Test 33: Player Self-Service Profile Persistence
**Function Tested:** Player Interface & Shared Memory Mutation
**Input:** Login as "Pinkie Pie" -> [2] Edit My Name -> New Name: "Commander Pinkie". Then go to [3] View Teams -> "Team Earth Pony".
**Expected:** The `dataManager.updatePlayer()` method modifies the shared memory reference. The updated name should immediately reflect across all other modules, including the team roster lookup, without requiring a system reboot.
**Actual:** The team roster lookup dynamically displayed "Commander Pinkie" because the underlying memory pointer was correctly mutated instead of overwritten.
**Result:** Pass

## Test 34: Admin Match Result Rollback Recalculation
**Function Tested:** `GameDataManager.updateMatchRecord()` Mathematical Rollback
**Input:** Admin Menu -> Manage Match Records -> Update Match Record -> Select a match where Team A won, and change the result to `TEAM_B_WIN`. 
**Expected:** The backend mathematically subtracts the previous wins from Team A (and its players/equipment), and correctly credits those wins to Team B (and its players/equipment) while preserving the total match count.
**Actual:** The system executed `removeMatchRecord()` to strip the old statistics and then recursively ran `addMatchRecord()` to distribute the new statistics perfectly.
**Result:** Pass

## Test 35: Global Match Record Limit Safety
**Function Tested:** `ConsolePrinter.printRecentMatches()` Index Guard
**Input:** Player Menu -> [5] View Global Match Records -> Prompt: "How many matches you would like to review?": 50 (when only 10 exist).
**Expected:** The system prints all 10 available matches in reverse chronological order and safely terminates the loop without throwing an `IndexOutOfBoundsException`.
**Actual:** The loop boundary condition `i >= 0 && count < n` successfully prevented the crash and displayed the entire available dataset.
**Result:** Pass