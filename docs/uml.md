# UML

### 1. Interfaces & Enums

**`<<Interface>> Searchable`**
* **Methods:**
    * `+ getId(): String`
    * `+ getName(): String`

**`<<Enum>> Role`**
* **Values:** `ADMIN`, `PLAYER`

**`<<Enum>> HeroType`**
* **Values:** `WARRIOR`, `ASSASSIN`, `MAGE`, `DRAGON`, `ELF`

**`<<Enum>> MatchResult`**
* **Values:** `TEAM_A_WIN`, `TEAM_B_WIN`, `DRAW`

---

### 2. Base Classes

**`<<Abstract>> Person`** (Implements `Searchable`)
* **Attributes:**
    * `- id: String`
    * `- name: String`
    * `- role: Role`
* **Methods:**
    * `+ getName(): String`
    * `+ getId(): String`
    * `+ getRole(): Role`
    * `+ setName(name: String): void`

---

### 3. Core Object Models

**`Admin`** (Extends `Person`)
* **Constructors:**
    * `+ Admin(name: String)`
    * `+ Admin(id: String, name: String)`

**`Player`** (Extends `Person`)
* **Attributes:**
    * `- winRate: double`
    * `- level: int`
    * `- totalMatches: int`
    * `- wins: int`
    * `- ownedHeroes: List<Hero>`
    * `- ownTeam: Team`
* **Methods:**
    * `+ addHero(hero: Hero): void`
    * `+ getOwnedHeroes(): List<Hero>`
    * `+ getWinRate(): double`
    * `+ setWinRate(winRate: double): void`
    * `+ getLevel(): int`
    * `+ setLevel(level: int): void`
    * `+ getTotalMatches(): int`
    * `+ setTotalMatches(totalMatches: int): void`
    * `+ getWins(): int`
    * `+ setWins(wins: int): void`
    * `+ getOwnTeam(): Team`
    * `+ setOwnTeam(ownTeam: Team): void`

**`Equipment`** (Implements `Searchable`)
* **Attributes:**
    * `- id: String`
    * `- name: String`
    * `- usageCount: int`
    * `- winRate: double`
    * `- averageRating: double`
    * `- wins: int`
* **Methods:**
    * *(Standard Getters and Setters for all attributes)*

**`Hero`** (Implements `Searchable`)
* **Attributes:**
    * `- id: String`
    * `- name: String`
    * `- type: HeroType`
    * `- baseHp: int`
    * `- baseAttack: int`
    * `- compatibleEquipments: List<Equipment>`
    * `- recommendedEquipments: List<Equipment>`
    * `- currentEquipments: List<Equipment>`
* **Methods:**
    * `+ addCompatibleEquipment(equipment: Equipment): void`
    * `+ addRecommendedEquipment(equipment: Equipment): void`
    * *(Standard Getters and Setters for all attributes)*

**`Team`** (Implements `Searchable`)
* **Attributes:**
    * `- id: String`
    * `- name: String`
    * `- members: List<Player>`
    * `- totalMatches: int`
    * `- wins: int`
* **Methods:**
    * `+ getTopPlayer(): Player`
    * `+ calculateAverageLevel(): double`
    * `+ calculateWinRate(): double`
    * *(Standard Getters and Setters for all attributes)*

**`MatchRecord`**
* **Attributes:**
    * `- id: String`
    * `- teamA: Team`
    * `- teamB: Team`
    * `- result: MatchResult`
    * `- matchDate: LocalDateTime`
    * `- playerHeroPicks: Map<String, String>`
* **Methods:**
    * `+ addPick(playerId: String, heroId: String): void`
    * *(Standard Getters and Setters for all attributes)*

---

### 4. Relationships & Cardinality

* **Inheritance:** `Admin` and `Player` inherit from `Person`.
* **Implementation:** `Person`, `Equipment`, `Hero`, and `Team` implement `Searchable`.
* **Association:** `Person` has a `Role`. `Hero` has a `HeroType`. `MatchRecord` has a `MatchResult`.
* **Composition/Aggregation:** * One `Player` owns Many `Hero` objects (`1` to `*`).
    * Many `Player` objects belong to One `Team` (`*` to `1`).
    * One `Team` contains Many `Player` objects (`1` to `*`).
    * One `Hero` utilizes Many `Equipment` objects (`1` to `*` for compatible, recommended, and current lists).
    * One `MatchRecord` references Two `Team` objects (`teamA` and `teamB`).