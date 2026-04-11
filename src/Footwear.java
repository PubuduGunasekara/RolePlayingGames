/**
 * A foot-slot item (boots, sneakers, hoverboard) that can provide both
 * attack and defense values. A character may equip at most two Footwear
 * items; a third is combined with an existing foot item automatically.
 */
public final class Footwear extends AbstractItem {

  /**
   * Constructs a Footwear item with the given name, attack, and defense values.
   *
   * @param adjective the adjective part of the name; must not be null
   * @param noun      the noun part of the name; must not be null
   * @param attack    the attack value; must be &gt;= 0
   * @param defense   the defense value; must be &gt;= 0
   * @throws IllegalArgumentException if adjective or noun is null,
   *                                  or if attack or defense is negative
   */
  public Footwear(String adjective, String noun, int attack, int defense) {
    super(adjective, noun, attack, defense);
  }

  /**
   * Creates a new Footwear from the combined name and stats.
   * Both attack and defense are preserved in the result.
   *
   * @param combinedName the new name
   * @param atk          the summed attack value
   * @param def          the summed defense value
   * @return a new Footwear with the combined name and summed stats
   */
  @Override
  protected Item buildCombined(String combinedName, int atk, int def) {
    String[] parts = combinedName.split(" ", 2);
    return new Footwear(parts[0], parts[1], atk, def);
  }
}
