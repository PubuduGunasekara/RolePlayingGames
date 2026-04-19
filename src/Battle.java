import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Drives a battle between two RPG characters over a pool of exactly 10 items.
 * Characters alternate turns picking items from the pool using a priority rule set.
 * After all items are picked, the winner is the character who takes less damage.
 * Damage is calculated as the opponent's total attack minus this character's total defense.
 *
 * <p>Item-selection priority (applied in order):
 * <ol>
 *   <li>Prefer items whose type still has an open slot on the character.</li>
 *   <li>Among candidates, prefer the item with the highest attack.</li>
 *   <li>On an attack tie, prefer the item with the highest defense.</li>
 *   <li>On a full tie, pick randomly.</li>
 * </ol>
 */
public class Battle {

  private final Character player1;
  private final Character player2;
  private final List<Item> itemPool;
  private final Random random;

  /**
   * Constructs a Battle with two characters and the item pool.
   *
   * @param player1 the first character; must not be null
   * @param player2 the second character; must not be null
   * @param items   exactly 10 items available for the battle; must not be null
   * @throws IllegalArgumentException if any argument is null or items does not contain exactly 10
   */
  public Battle(Character player1, Character player2, List<Item> items) {
    this(player1, player2, items, new Random());
  }

  /**
   * Package-private constructor that accepts a seeded Random for deterministic testing.
   *
   * @param player1 the first character; must not be null
   * @param player2 the second character; must not be null
   * @param items   exactly 10 items; must not be null
   * @param random  the Random instance to use for tie-breaking
   * @throws IllegalArgumentException if any argument is null or items does not contain exactly 10
   */
  Battle(Character player1, Character player2, List<Item> items, Random random) {
    if (player1 == null || player2 == null || items == null || random == null) {
      throw new IllegalArgumentException("Players, items, and random must not be null.");
    }
    if (items.size() != 10) {
      throw new IllegalArgumentException(
          "Exactly 10 items are required for a battle, but got: " + items.size());
    }
    this.player1  = player1;
    this.player2  = player2;
    this.itemPool = new ArrayList<>(items);
    this.random   = random;
  }

  /**
   * Runs the full battle.
   * Characters alternate picking items for 10 turns (player 1 goes first).
   * After each turn, both characters' current state is printed.
   * After all 10 turns, the winner is announced.
   */
  public void start() {
    List<Item> pool = new ArrayList<>(itemPool);

    for (int turn = 0; turn < 10; turn++) {
      Character current    = (turn % 2 == 0) ? player1 : player2;
      String    playerName = (turn % 2 == 0) ? "Player 1" : "Player 2";

      Item chosen = selectItem(current, pool);
      pool.remove(chosen);
      current.addItem(chosen);

      System.out.printf("--- Turn %d: %s picks %s ---%n",
          turn + 1, playerName, chosen.getName());
      System.out.println("Player 1:\n" + player1);
      System.out.println("Player 2:\n" + player2);
    }

    System.out.println(determineWinner());
  }

  /**
   * Selects the best item from the pool for the given character using the four priority rules.
   * Rule 1 — prefer items whose type has an open slot.
   * Rule 2 — highest attack among candidates.
   * Rule 3 — highest defense on an attack tie.
   * Rule 4 — random on a full tie.
   *
   * @param character the character making the pick
   * @param pool      the current list of available items
   * @return the chosen item (never null; pool must not be empty)
   */
  Item selectItem(Character character, List<Item> pool) {

    // Rule 1: filter to items whose type still has an open slot
    List<Item> preferred = new ArrayList<>();
    for (Item item : pool) {
      if ((item instanceof HeadGear && character.hasAvailableHeadSlot())
          || (item instanceof HandGear && character.hasAvailableHandSlot())
          || (item instanceof Footwear && character.hasAvailableFootSlot())) {
        preferred.add(item);
      }
    }
    // If no open slot matches any remaining item, all items are candidates
    List<Item> candidates = preferred.isEmpty() ? new ArrayList<>(pool) : preferred;

    // Rule 2: highest attack
    int maxAtk = Integer.MIN_VALUE;
    for (Item item : candidates) {
      if (item.getAttack() > maxAtk) {
        maxAtk = item.getAttack();
      }
    }
    List<Item> atkTied = new ArrayList<>();
    for (Item item : candidates) {
      if (item.getAttack() == maxAtk) {
        atkTied.add(item);
      }
    }
    if (atkTied.size() == 1) {
      return atkTied.get(0);
    }

    // Rule 3: highest defense
    int maxDef = Integer.MIN_VALUE;
    for (Item item : atkTied) {
      if (item.getDefense() > maxDef) {
        maxDef = item.getDefense();
      }
    }
    List<Item> defTied = new ArrayList<>();
    for (Item item : atkTied) {
      if (item.getDefense() == maxDef) {
        defTied.add(item);
      }
    }
    if (defTied.size() == 1) {
      return defTied.get(0);
    }

    // Rule 4: random
    return defTied.get(random.nextInt(defTied.size()));
  }

  /**
   * Determines and returns the winner summary string.
   * Damage = opponent's total attack − this character's total defense.
   * The character with less damage wins; equal damage is a tie.
   *
   * @return a formatted result string naming the winner or declaring a tie
   */
  String determineWinner() {
    int damage1 = player2.getTotalAttack() - player1.getTotalDefense();
    int damage2 = player1.getTotalAttack() - player2.getTotalDefense();

    StringBuilder sb = new StringBuilder("=== Battle Result ===\n");
    sb.append(String.format("Player 1 damage taken: %d%n", damage1));
    sb.append(String.format("Player 2 damage taken: %d%n", damage2));

    if (damage1 < damage2) {
      sb.append("Player 1 wins!");
    } else if (damage2 < damage1) {
      sb.append("Player 2 wins!");
    } else {
      sb.append("It's a tie!");
    }
    return sb.toString();
  }
}
