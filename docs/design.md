# Design

This document outlines the architectural decisions, Object-Oriented Programming (OOP) principles, and data structures utilized in the Honor of Kings Information Management System (IMS).

## 1. Architectural Overview

The application follows a modular, layered architecture to ensure a strict Separation of Concerns (SoC). The codebase is divided into four primary packages:
* **`model`**: Contains the core business objects (e.g., `Player`, `Hero`, `Team`). These classes are strictly focused on data state and basic entity-level logic.
* **`service`**: Contains the business logic controllers (e.g., `GameDataManager`, `SearchService`, `RankingService`). This layer prevents the UI and models from becoming bloated.
* **`ui`**: Handles user interaction and data presentation (e.g., `ConsolePrinter`).
* **`util`**: Contains helper classes for system operations (e.g., `InputHelper` for safe scanning, `DataInitializer` for bootstrapping data).

## 2. Object-Oriented Programming (OOP) Principles

### 2.1 Abstraction
Abstraction is utilized to hide underlying complexities and define common templates. 
* The **`Person`** class is defined as `abstract`. It captures the universal attributes of a system user (`id`, `name`, `role`) but cannot be instantiated on its own, ensuring that all people in the system must be strictly categorized as either an `Admin` or a `Player`.

### 2.2 Inheritance
Inheritance is used to establish "is-a" relationships, promoting code reusability.
* **`Player`** and **`Admin`** both extend the `Person` superclass. They inherit the base identification fields but introduce role-specific attributes and methods (e.g., `Player` introduces `winRate`, `level`, and `ownedHeroes`).

### 2.3 Polymorphism & Interfaces
Polymorphism allows the system to treat different object types uniformly.
* The **`Searchable`** interface is implemented by `Person` (and thus `Player`/`Admin`), `Hero`, `Team`, and `Equipment`. 
* This guarantees that any searchable entity provides `getId()` and `getName()` methods, allowing the `SearchService` to iterate through disparate lists of objects using a single, unified search logic pattern.

### 2.4 Encapsulation
Data hiding is enforced across all domain models to protect the integrity of the application state.
* All class attributes (e.g., `winRate` in `Player`, `baseHp` in `Hero`) are declared as `private`.
* Access and mutation are strictly controlled via `public` getter and setter methods. This prevents invalid data states (e.g., a `Team`'s member list cannot be arbitrarily wiped out by a UI class without passing through the proper setter).

## 3. Data Structures & Collections

The Java Collections Framework is utilized extensively to manage dynamic data relationships:

* **`List<T>` (ArrayList):** Used for ordered, dynamic collections where elements might be added frequently or iterated over. 
    * *Example:* `List<Player> members` in the `Team` class, and `List<Hero> ownedHeroes` in the `Player` class.
* **`Map<K, V>` (HashMap):** Used in the `MatchRecord` class (`Map<String, String> playerHeroPicks`) to track which player selected which hero. A map provides $O(1)$ time complexity for looking up a specific player's hero pick, which is highly efficient for match history generation.

## 4. Enums for Type Safety

To prevent invalid string inputs and "magic strings" in the codebase, strict categorizations are handled via enumerations:
* **`Role`**: Restricts authentication types to `ADMIN` and `PLAYER`.
* **`HeroType`**: Standardizes hero classifications (`WARRIOR`, `ASSASSIN`, `MAGE`, `DRAGON`, `ELF`).
* **`MatchResult`**: Limits match outcomes to strictly `TEAM_A_WIN`, `TEAM_B_WIN`, or `DRAW`.

## 5. Composition and Aggregation
The system effectively maps real-world entity relationships:
* **Composition ("Owns-a"):** A `Player` contains a list of `Hero` objects they own. If the player is deleted, the concept of their hero ownership is also dissolved.
* **Aggregation ("Has-a"):** A `Team` aggregates `Player` objects. The `Team` maintains a reference to its players, but the `Player` entities can exist independently in the system even if the `Team` is disbanded.