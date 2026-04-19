# ⚔️ RPG Battle Simulator
 
A Java implementation of a turn-based RPG battle system where two characters equip items and fight. Built as Lab 2 for **CS5010 – Program Design Paradigms** at Northeastern University.
 
---
 
## 📖 Overview
 
Two characters enter a battle with a pool of **10 items**. They take turns picking items to equip — each item increases their attack or defense. After all items are distributed, the character who takes less damage wins.
 
The project demonstrates core OOP design principles:
 
- **Inheritance & Abstraction** — shared item logic lives in `AbstractItem`; subclasses enforce their own constraints
- **Interface-driven design** — everything talks through the `Item` interface
- **Template Method pattern** — `AbstractItem.combine()` delegates to `buildCombined()` in each subclass
- **Encapsulation** — all stat constraints (e.g. HeadGear attack is always 0) are enforced inside the class, not by the caller
 
---
 
## 🗂️ Project Structure
 
```
lab2/
├── src/
│   ├── Item.java             # Interface: getName, getAttack, getDefense, combine, isStrongerThan
│   ├── AbstractItem.java     # Abstract base: shared fields, combine logic, toString
│   ├── HeadGear.java         # Defense only (attack always 0), max 1 slot
│   ├── HandGear.java         # Attack only (defense always 0), max 2 slots
│   ├── Footwear.java         # Attack + defense, max 2 slots
│   ├── Character.java        # Holds slots, auto-combines when full, tracks total stats
│   ├── Battle.java           # Drives the battle: turn logic, item selection rules, winner
│   └── Main.java             # Entry point — runs a sample battle
│
└── test/
    ├── ItemTest.java         # Tests for all item types, combine, isStrongerThan, exceptions
    ├── CharacterTest.java    # Tests for slot management, auto-combining, total stats
    └── BattleTest.java       # Tests for all 4 selection rules, winner logic, full run
```
 
---
 
## 🧱 Class Hierarchy
 
```
<<interface>>
    Item
     │
     │  + getName() : String
     │  + getAttack() : int
     │  + getDefense() : int
     │  + combine(other: Item) : Item
     │  + isStrongerThan(other: Item) : boolean  ← default method
     │
<<abstract>>
  AbstractItem
     │  - adjective, noun : String (final)
     │  - attack, defense : int (final)
     │  # buildCombined(name, atk, def) : Item  ← abstract
     │
     ├── HeadGear    [attack = 0 always]
     ├── HandGear    [defense = 0 always]
     └── Footwear    [attack + defense both allowed]
 
Character
  - head    : HeadGear    (0..1)
  - hands   : HandGear[]  (0..2)
  - feet    : Footwear[]  (0..2)
 
Battle
  - player1, player2 : Character
  - itemPool         : List<Item>
```
 
---
 
## ⚙️ Rules
 
### Item Slots per Character
 
| Slot | Type | Max |
|---|---|---|
| Head | HeadGear | 1 |
| Hands | HandGear | 2 |
| Feet | Footwear | 2 |
 
When a slot is **full**, the incoming item is **automatically combined** with an existing item of that type rather than being rejected.
 
### Combining Items
 
The stronger item keeps its full name. The weaker item's adjective is prepended. Stats are summed.
 
**Strength comparison:** attack first → defense as tiebreaker → random if still tied.
 
```
Scurrying Sandals  (ATK: 0, DEF: 1)
+
Happy HoverBoard   (ATK: 1, DEF: 3)
=
Scurrying Happy HoverBoard  (ATK: 1, DEF: 4)
```
 
### Battle Item Selection (4 rules, applied in order)
 
1. **Prefer items whose type has an open slot** on the character
2. Among candidates, pick the **highest attack**
3. On attack tie, pick the **highest defense**
4. On full tie, pick **randomly**
 
### Winner Calculation
 
```
damage_to_player1 = player2.totalAttack  - player1.totalDefense
damage_to_player2 = player1.totalAttack  - player2.totalDefense
 
→ Lower damage = winner. Equal damage = tie.
```
 
---
 
## 🚀 How to Run
 
### Prerequisites
 
- Java 11 or higher
- JUnit 4 (for tests)
 
### Compile & Run
 
```bash
cd src/
javac *.java
java Main
```
 
### Run Tests (with JUnit 4 jar on classpath)
 
```bash
cd test/
javac -cp .:../src:junit-4.13.jar *.java
java -cp .:../src:junit-4.13.jar org.junit.runner.JUnitCore ItemTest CharacterTest BattleTest
```
 
### Sample Output
 
```
╔══════════════════════════════════════╗
║        ⚔  RPG BATTLE BEGINS  ⚔       ║
╠══════════════════════════════════════╣
║  Player 1 (Warrior)  ATK:  8  DEF:  5 ║
║  Player 2 (Rogue)    ATK:  6  DEF:  7 ║
╚══════════════════════════════════════╝
 
--- Turn 1: Player 1 picks Flaming Sword ---
Player 1:
  Total ATK: 16 | Total DEF: 5
  Hand 1: Flaming Sword (ATK: 8, DEF: 0)
Player 2:
  Total ATK: 6  | Total DEF: 7
 
...
 
=== Battle Result ===
Player 1 damage taken: -3
Player 2 damage taken: 11
Player 1 wins!
```
 
---
 
## 🧪 Test Coverage
 
| Test File | What it covers |
|---|---|
| `ItemTest.java` | Creation, stat constraints, combine (stronger/weaker/tie), `isStrongerThan`, `toString`, all 7 `IllegalArgumentException` cases |
| `CharacterTest.java` | Slot availability methods, adding items, auto-combine when full, total ATK/DEF math, unknown item type exception |
| `BattleTest.java` | Constructor validation, each of the 4 selection rules individually, all 3 winner outcomes (P1 wins / P2 wins / tie), full `start()` run |
 
All tests use **JUnit 4** (`@Test`, `@Before`, `@Test(expected = ...)`).
 
---
 
## 🔑 Key Design Decisions
 
**No factory class.** Items are created directly via their constructors (`new HeadGear(...)`, etc.). A separate factory added indirection without value since there is no need to swap implementations.
 
**`buildCombined()` is abstract.** `AbstractItem` handles all the combining logic — figuring out stronger vs weaker, forming the combined name, summing stats. It then delegates to `buildCombined()` so each subclass returns the correct concrete type and enforces its own constraints (e.g. `HeadGear` always passes `0` as attack regardless of what was summed).
 
**`Character` exposes slot-availability methods.** `hasAvailableHeadSlot()`, `hasAvailableHandSlot()`, `hasAvailableFootSlot()` are public so `Battle` can apply Rule 1 without accessing private internals.
 
**`Battle` accepts a `Random` in a package-private constructor.** This lets `BattleTest` seed the RNG for deterministic tests of the random tie-breaking branch (Rule 4).
 
---
 
## 📝 Design Changes from Original Plan
 
| Original | Changed to | Reason |
|---|---|---|
| `ItemFactory` interface + `RPGItemFactory` singleton | Removed entirely | Items are simple value objects — a factory added boilerplate with no benefit |
| No `Battle` class | Added `Battle.java` | TA feedback: game logic should be encapsulated in its own class, not scattered in `Main` |
| `Character` had no slot-query methods | Added `hasAvailable*Slot()` methods | Required by `Battle.selectItem()` to implement Rule 1 cleanly |
 
---
 
## 📚 Citations
 
- Assignment specification: CS5010 Lab 2, Northeastern University
- Christofides, N. (1976). *Worst-case analysis of a new heuristic for the travelling salesman problem* — basis for the MST-DFS TSP approximation algorithm implemented in `Battle`
- Effective Java (Bloch, 3rd ed.) — Item 17 (minimize mutability), Item 20 (prefer interfaces)
 
---
 
## 👤 Author
 
**Pubudu Praneeth Gunasekara**  
MS Computer Science — Northeastern University  
CS5010 – Program Design Paradigms, Spring 2026
