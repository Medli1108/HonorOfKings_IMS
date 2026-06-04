# Grand Plan for Honor of Kings: Java AI-Assisted Coursework

## 1. Project Goal
The goal of this project is to design a console-based, menu-driven Java program that can simulate the HoK data management system.

## 2. Requirement Anaylasis
The project will be able to implement the following features:
*   Player Lookup: Search a player by ID or name. Display: 
        player ID and name;
        team;
        level and win rate;
        owned heroes;
        each hero’s equipped items.
*   Team Overview: Search a team by ID or name. Display: 
        team name; all members;
        average level;
        total matches;
        win rate;
        top player in the team.
*   Hero Details: Search a hero by name. Display:
        hero name and hero type;
        base stats;
        available or compatible equipment;
        players who own this hero;
        recommended equipment if implemented.
*   Equipment Statistics: Rank equipment by at least one metric, such as:
        usage count;
        average rating;
        number of heroes using it;
        win-rate contribution;
        a custom score.
    P.S. The ranking formula in the documentation will be explained.
*   Match History: Retrieve the last N matches for a player or team. Display:
        opponent;
        date;
        result;
        heroes picked;
        win/loss record;
        hero pick rate.
*   Leaderboard: Display top X players by:
        win rate;
        level;
        number of matches;
        custom score.
    P.S. How ties are handled will be explained.
*   Data Management: 
        Admin users can add, delete, and edit:
            players;
            heroes;
            equipment;
            teams;
            match records.
        Player users can:
            view their own information;
            edit limited personal information;
            view their heroes and match history;
            view public hero, team, and leaderboard information.
*   Authentication: Implement a simple login and logout system with at least two roles:
        Admin;
        Player.
    P.S. Admin users can create, modify, or delete all data. Player users can only view general data and edit their own basic information.

## 3. Java Concepts Used
The following Java comcepts will be used:
*   Inheritance: Player and Admin extend Person.
*   Association: A Player owns multiple Hero objects; a Hero can use multiple Equipment objects.
*   Aggregation & Composition: A Team contains multiple Player objects.
*   Interfaces: Use at least one meaningful interface such as Searchable, Reportable, Persistable, or Authenticatable.
*   Encapsulation: Fields should be private; access should be controlled through appropriate methods.
*   Polymorphism: Use common superclass or interface references where appropriate, such as storing users as Person.
*   Collections: Use ArrayList, HashMap, Set, TreeMap, or similar collections.
*   Exception Handling: Handle invalid input, missing records, duplicate IDs, and file loading errors.
*   File I/O: Save and load system data from text, CSV, JSON, or another documented format.
*   Enums: Use enums such as HeroType, MatchResult, Role, or EquipmentType.

## 4. Class Design
The project will include at least the following classes:
*   Person: Abstract superclass for system users.
*   Player: Subclass of Person; represents a game player.
*   Admin: Subclass of Person; represents a user with data-management permission.
*   Hero: Represents a playable hero, including type, stats, and equipment compatibility.
*   Equipment: Represents an item that can be equipped by heroes.
*   Team: Represents a team containing multiple players.
*   MatchRecord: Represents a match result, participants, hero picks, and date.

## 5. UML Draft
*   `Person` <|-- `Player`
*   `Person` <|-- `Admin`
*   `Team` o-- `Player` (Aggregation)
*   `Player` --> `Hero` (Association)
*   `Hero` --> `Equipment` (Association)

## 6. Data Design
*   3 Teams (5+ players each)
*   10 Players (3+ heroes each)
*   15 Heroes (2+ compatible equipment each)
*   20 Equipment items
*   10 Match records
P.S. The dataset may be hard-coded during early development, but later the program should be able to save and load data through files

## 7. AI Usage Plans
I will be using 3 AI agents: the Architect Agent, the Implementation Agent and the Testing/Reviewer Agent.
*   Architect Agent: Give me the big picture and brainstorm some ideas.
*   Implementation Agent: Implement the code after my permission.
*   Testing/Reviewer Agent: Test and debug the code and detect logic loopholes.

## 8. Prompt Strategy
Prompts will follow a strict format: Role assignment -> Context limitation -> Specific task.

## 9. Development Timeline
*   Read requirements, create repository, write first plan.md.
*   Ask Architect Agent for design feedback; revise class structure manually.
*   Implement model classes and initial data.
*   Implement menu system and search features.
*   Implement authentication and admin/player permissions.
*   Implement persistence and ranking functions.
*   Use Testing/Reviewer Agent to find bugs; fix and record decisions.
*   Finish documentation, reflection, Git export, and final testing.

## 10. Testing Plan
I will perform manual testing documented in "test-cases.md", ensuring at least 10 comprehensive cases. Tests will cover:
*   Valid and invalid ID lookups.
*   Permission boundary testing (Player attempting Admin actions).
*   Correct mathematical ranking for leaderboards.
*   Data persistence integrity (saving, then reloading the application).

## 11. Risk Analysis
*   Blindly trusting AI-generated code leading to unexplainable logic.
*   Corrupting the Git history.
*   Overcomplicating the scope.

## 12. Final Reflection Placeholder
This just serves as a reminder for a reflection.
P.S. After completing the main tasks, I might consider adding some extra features (GUIs, more intersting and helpful classes, amazing new features like  a combat system, etc.)