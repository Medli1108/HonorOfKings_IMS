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