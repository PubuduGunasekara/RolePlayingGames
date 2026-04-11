/**
 * Provides shared state and behaviour for all wearable RPG items.
 * Stores the immutable name components and stat values common to every item.
 * Subclasses define which stats are applicable and how combining produces
 * the correct concrete type via the abstract buildCombined() method.
 */
public abstract class AbstractItem implements Item {

  private final String adjective;
  private final String noun;
  private final int attack;
  private final int defense;

  /**
   * Constructs an item with the given name components and stat values.
   *
   * @param adjective the adjective part of the item name; must not be null
   * @param noun      the noun part of the item name; must not be null
   * @param attack    attack contribution; must be &gt;= 0
   * @param defense   defense contribution; must be &gt;= 0
   * @throws IllegalArgumentException if adjective or noun is null,
   *                                  or if attack or defense is negative
   */
  protected AbstractItem(String adjective, String noun, int attack, int defense) {
    if (adjective == null || noun == null) {
      throw new IllegalArgumentException("Adjective and noun must not be null.");
    }
    if (attack < 0 || defense < 0) {
      throw new IllegalArgumentException("Attack and defense must be non-negative.");
    }
    this.adjective = adjective;
    this.noun      = noun;
    this.attack    = attack;
    this.defense   = defense;
  }

  @Override
  public String getName() {
    return adjective + " " + noun;
  }

  @Override
  public int getAttack() {
    return attack;
  }

  @Override
  public int getDefense() {
    return defense;
  }

  /**
   * Combines this item with another of the same type.
   * Determines the stronger item by attack then defense, then randomly on a full tie.
   * The weaker item's adjective is prepended to the stronger item's full name.
   * Stats are summed. Delegates to buildCombined() so each subclass returns its own type.
   *
   * @param other the item to combine with
   * @return a new Item with the combined name and summed stats
   */
  @Override
  public Item combine(Item other) {
    Item stronger;
    Item weaker;

    if (this.isStrongerThan(other)) {
      stronger = this;
      weaker   = other;
    } else if (other.isStrongerThan(this)) {
      stronger = other;
      weaker   = this;
    } else {
      // Exact tie on both attack and defense — pick randomly
      if (Math.random() < 0.5) {
        stronger = this;
        weaker   = other;
      } else {
        stronger = other;
        weaker   = this;
      }
    }

    String weakerAdjective  = weaker.getName().split(" ")[0];
    String strongerFullName = stronger.getName();
    String combinedName     = weakerAdjective + " " + strongerFullName;
    int    combinedAtk      = this.attack + other.getAttack();
    int    combinedDef      = this.defense + other.getDefense();

    return buildCombined(combinedName, combinedAtk, combinedDef);
  }

  /**
   * Constructs the resulting combined item of the correct concrete type.
   * Subclasses override this to enforce their own slot constraints —
   * for example HandGear ignores combinedDef since its defense is always 0.
   *
   * @param combinedName the new name formed from weaker adjective + stronger full name
   * @param atk          summed attack value
   * @param def          summed defense value
   * @return a new item of the appropriate subtype
   */
  protected abstract Item buildCombined(String combinedName, int atk, int def);

  @Override
  public String toString() {
    return String.format("%s (ATK: %d, DEF: %d)", getName(), attack, defense);
  }
}
