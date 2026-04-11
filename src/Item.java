/**
 * Represents a wearable item in the RPG game.
 * Every item has a name (adjective + noun), an attack value, and a defense value.
 * Items of the same type can be combined when a character's slot for that type is full.
 */
public interface Item {

  /** Returns the full name of this item as adjective followed by noun. */
  String getName();

  /** Returns the attack value this item contributes to a character. */
  int getAttack();

  /** Returns the defense value this item contributes to a character. */
  int getDefense();

  /**
   * Combines this item with another of the same type into a new item.
   * The stronger item's full name is kept; the weaker item's adjective is prepended.
   * Strength is determined by attack first, then defense, then randomly if still tied.
   * The combined item's stats are the sum of both items' stats.
   * Neither original item is modified.
   *
   * @param other the item to combine with; must be the same concrete type
   * @return a new Item representing the combination
   */
  Item combine(Item other);

  /**
   * Returns true if this item is strictly stronger than the other.
   * Compares attack first; uses defense as a tiebreaker.
   * This logic is the same for all item types so it lives here as a default method.
   *
   * @param other the item to compare against
   * @return true if this item has higher attack, or equal attack and higher defense
   */
  default boolean isStrongerThan(Item other) {
    if (this.getAttack() != other.getAttack()) {
      return this.getAttack() > other.getAttack();
    }
    return this.getDefense() > other.getDefense();
  }
}
