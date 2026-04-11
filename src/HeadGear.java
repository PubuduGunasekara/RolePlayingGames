/**
 * A head-slot item (hat, helmet, visor) that provides only defense.
 * Attack value is always fixed at 0 — head gear offers no offensive capability.
 * A character may equip at most one HeadGear at a time; a second one is combined
 * with the existing head item automatically.
 */
public final class HeadGear extends AbstractItem {

  /**
   * Constructs a HeadGear item with the given name and defense value.
   * Attack is fixed at 0 and cannot be changed.
   *
   * @param adjective the adjective part of the name; must not be null
   * @param noun      the noun part of the name; must not be null
   * @param defense   the defense value; must be &gt;= 0
   * @throws IllegalArgumentException if adjective or noun is null or defense is negative
   */
  public HeadGear(String adjective, String noun, int defense) {
    super(adjective, noun, 0, defense);
  }

  /**
   * Creates a new HeadGear from the combined name and stats.
   * Attack is always forced to 0 regardless of the combined attack argument.
   *
   * @param combinedName the new name
   * @param atk          ignored — HeadGear attack is always 0
   * @param def          the summed defense value
   * @return a new HeadGear with the combined name and summed defense
   */
  @Override
  protected Item buildCombined(String combinedName, int atk, int def) {
    String[] parts = combinedName.split(" ", 2);
    return new HeadGear(parts[0], parts[1], def);
  }
}
