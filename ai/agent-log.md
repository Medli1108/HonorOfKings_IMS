# Agent Log

## Architect Agent
**Main contribution:**
The Architect Agent was primarily responsible for outlining the initial OOP framework, directory structure (`model`, `service`, `util`), and Java class skeletons. It proposed a centralized data manager, outlined the state-machine traffic controller logic for `Main.java`, and designed the structural framework for the "Combat Simulation" stretch goal and the GUI wrapper. 

**Human decision:**
I accepted the overarching package structure and the final Swing GUI state layout, as it preserved my console logging functionality via stream interception. However, I actively rejected several of the Architect's suggestions. I rejected its monolithic `GameDataManager` draft and insisted on decoupling the lookup functions into a distinct `SearchService`. I also rejected its proposal to use an `EquipmentType` enum to prevent unnecessary complexity, and I completely rejected its initial JavaFX GUI framework because I am unfamiliar with JavaFX, steering it toward a Swing-based approach instead.

**Related commits:**
* `d056a49` typo/grammar review in plan.md
* `ae7699d` initial project framework build
* `0c22a61` model blueprints and base enums
* `2b589c8` GameDataManager monolithic framework (rejected)
* `760baab` Main.java state machine framework
* `db549e7` JavaFX GUI proposal (rejected)
* `6429d75` Swing GUI architecture (accepted)

## Implementation Agent
**Main contribution:**
The Implementation Agent handled heavy lifting for specific logic modules. It populated the `DataInitializer` with a coursework-compliant mock dataset, wrote the comprehensive CSV File I/O utilities for data persistence, refactored the `GameDataManager` into a Thread-Safe Singleton pattern, and extracted highly duplicated match statistics math into a clean, centralized helper method. It also generated the foundational logic for the `CombatSimulatorService` stretch goal (damage variance, dodge chance, and critical hits).

**Human decision:**
I accepted the File I/O stream implementation since that was a new Java concept for me to study. I manually tweaked the `DataInitializer` dataset to fix the AI's slight misunderstanding of class associations. For the Combat Simulator, I accepted the mathematical logic but manually hand-coded the integration into my `Main.java` and `ConsolePrinter.java` classes to ensure the inventory ownership checks aligned perfectly with my existing backend architecture.

**Related commits:**
* `04889b3` automated edits to plan.md
* `9ae513b` DataInitializer population implementation
* `76946e6` Singleton GameDataManager & SearchService implementation
* `3039ce0` CSV file I/O and exception handling
* `7b47954` update DataInitializer & FileStorageService syncing
* `4c9e53c` Main.java player menu integration
* `e656cad` CombatSimulatorService stretch goal logic
* `163d7c9` refactoring match stats math & dynamic enums

## Testing/Reviewer Agent
**Main contribution:**
The Testing/Reviewer Agent acted as a continuous code reviewer and bug hunter. It identified critical issues like hardcoded `final` UUIDs breaking data persistence, dangerous floating-point equality comparisons (`==` on doubles), and missing enum requirements (`Role`). It aggressively reviewed thread-safety vulnerabilities, suggesting synchronized block guards and defensive array copying. It also caught logical pitfalls, including a switch fall-through in the Admin menu, a "Ghost Match" bug where hero picks weren't mapped properly, and a "Ghost Object" memory overwrite flaw during player updates.

**Human decision:**
I accepted the majority of its structural code review findings, especially regarding thread safety, as `ConcurrentModificationException` errors would break the GUI implementation later. I implemented its suggestion to use defensive copying (returning `new ArrayList<>(list)`) instead of relying on blocking synchronization loops. However, I reverted one of its suggestions regarding date parsing in the `FileStorageService` after realizing it was incorrect and caused a system crash. I also used this agent iteratively to process small batches of my manual test results, successfully fixing the logic gaps without breaking the core architecture.

**Related commits:**
* `8045c03` found UUID persistence bug, missing setters, and missing Role enum
* `dd4ccb7` found missing Searchable interface and calculation logic errors
* `724cf64` flagged dangling object references
* `5d57076` identified missing match history hero picks mapping
* `7d2fe97` suggested multi-threading synchronization guards
* `25f2911` fixed memory leak and dangling team references
* `6dd72ca` caught collection wrapper wipeout bug in file loading
* `793cf83` implemented defensive array copying for thread safety
* `1b40bbc` spotted memory overwrite flaw in object updates
* `b7067fb` identified match record rollback mathematical bug
* `876a343` found critical switch fall-through bug in Admin menu
* `29d8748` caught edge cases (e.g., 'exit' username lockout)
* `0594a27` iterative test result debugging
* `e73e9ef` iterative test result debugging