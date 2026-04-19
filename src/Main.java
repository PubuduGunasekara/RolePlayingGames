import java.util.Arrays;
import java.util.List;

/**
 * Entry point for the RPG Battle simulation.
 * Creates two characters, a pool of 10 items, and runs the battle.
 */
public class Main {

  public static void main(String[] args) {

    // ── Create two characters (baseAttack, baseDefense) ───────────────────
    Character warrior = new Character(8, 5);
    Character rogue   = new Character(6, 7);

    System.out.println("╔══════════════════════════════════════╗");
    System.out.println("║        ⚔  RPG BATTLE BEGINS  ⚔       ║");
    System.out.println("╠══════════════════════════════════════╣");
    System.out.printf ("║  Player 1 (Warrior)  ATK: %2d  DEF: %2d ║%n", 8, 5);
    System.out.printf ("║  Player 2 (Rogue)    ATK: %2d  DEF: %2d ║%n", 6, 7);
    System.out.println("╚══════════════════════════════════════╝");
    System.out.println();

    // ── Create exactly 10 items ───────────────────────────────────────────
    List<Item> items = Arrays.asList(
        new HeadGear("Iron",      "Helmet",     6),   //  1 - head, DEF 6
        new HeadGear("Leather",   "Cap",        3),   //  2 - head, DEF 3
        new HandGear("Flaming",   "Sword",      8),   //  3 - hand, ATK 8
        new HandGear("Quick",     "Dagger",     5),   //  4 - hand, ATK 5
        new HandGear("Heavy",     "Axe",        7),   //  5 - hand, ATK 7
        new HandGear("Rusty",     "Shield",     2),   //  6 - hand, ATK 2
        new Footwear("Swift",     "Boots",      4, 3),//  7 - foot, ATK 4 DEF 3
        new Footwear("Bouncy",    "Sneakers",   2, 5),//  8 - foot, ATK 2 DEF 5
        new Footwear("Scurrying", "Sandals",    0, 4),//  9 - foot, ATK 0 DEF 4
        new Footwear("Happy",     "HoverBoard", 3, 2) // 10 - foot, ATK 3 DEF 2
    );

    // ── Run the battle ────────────────────────────────────────────────────
    Battle battle = new Battle(warrior, rogue, items);
    battle.start();
  }
}
