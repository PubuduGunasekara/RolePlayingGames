/**
 * A hand-slot item (glove, sword, shield) that provides only attack.
 * Defense value is always fixed at 0 — hand gear offers no defensive capability.
 * A character may equip at most two HandGear items; a third is combined
 * with an existing hand item automatically.
 */
public final class HandGear extends AbstractItem {

  /**
   * Constructs a HandGear item with the given name and attack value.
   * Defense is fixed at 0 and cannot be changed.
   *
   * @param adjective the adjective part of the name; must not be null
   * @param noun      the noun part of the name; must not be null
   * @param attack    the attack value; must be &gt;= 0
   * @throws IllegalArgumentException if adjective or noun is null or attack is negative
   */
  public HandGear(String adjective, String noun, int attack) {
    super(adjective, noun, attack, 0);
  }

  /**
   * Creates a new HandGear from the combined name and stats.
   * Defense is always forced to 0 regardless of the combined defense argument.
   *
   * @param combinedName the new name
   * @param atk          the summed attack value
   * @param def          ignored — HandGear defense is always 0
   * @return a new HandGear with the combined name and summed attack
   */
  @Override
  protected Item buildCombined(String combinedName, int atk, int def) {
    String[] parts = combinedName.split(" ", 2);
    return new HandGear(parts[0], parts[1], atk);
  }
}
