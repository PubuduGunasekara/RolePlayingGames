/**
 * Represents a game character who equips items to increase combat stats.
 * A character has immutable base attack and defense values plus three item slots:
 * one head slot (HeadGear), two hand slots (HandGear), and two foot slots (Footwear).
 * When a slot is full and a new item of that type is added, the new item is
 * automatically combined with an existing item in that slot rather than rejected.
 */
public class Character {

  private static final int MAX_HEAD  = 1;
  private static final int MAX_HANDS = 2;
  private static final int MAX_FEET  = 2;

  private final int baseAttack;
  private final int baseDefense;

  private HeadGear   head;
  private HandGear[] hands;
  private Footwear[] feet;
  private int handCount;
  private int feetCount;

  /**
   * Constructs a Character with the given base stats and empty item slots.
   *
   * @param baseAtk base attack value; does not change after construction
   * @param baseDef base defense value; does not change after construction
   */
  public Character(int baseAtk, int baseDef) {
    this.baseAttack  = baseAtk;
    this.baseDefense = baseDef;
    this.hands       = new HandGear[MAX_HANDS];
    this.feet        = new Footwear[MAX_FEET];
    this.handCount   = 0;
    this.feetCount   = 0;
  }

  /**
   * Adds an item to this character, routing it to the correct slot by runtime type.
   * If the appropriate slot is full, the item is combined with an existing one.
   *
   * @param item the item to add; must be HeadGear, HandGear, or Footwear
   * @throws IllegalArgumentException if the item type is not recognised
   */
  public void addItem(Item item) {
    if (item instanceof HeadGear) {
      addHeadGear((HeadGear) item);
    } else if (item instanceof HandGear) {
      addHandGear((HandGear) item);
    } else if (item instanceof Footwear) {
      addFootwear((Footwear) item);
    } else {
      throw new IllegalArgumentException("Unrecognised item type: " + item.getClass().getName());
    }
  }

  /**
   * Returns true if the head slot is currently empty and can accept a HeadGear.
   *
   * @return true if no HeadGear is equipped
   */
  public boolean hasAvailableHeadSlot() {
    return head == null;
  }

  /**
   * Returns true if at least one hand slot is still open.
   *
   * @return true if fewer than two HandGear items are equipped
   */
  public boolean hasAvailableHandSlot() {
    return handCount < MAX_HANDS;
  }

  /**
   * Returns true if at least one foot slot is still open.
   *
   * @return true if fewer than two Footwear items are equipped
   */
  public boolean hasAvailableFootSlot() {
    return feetCount < MAX_FEET;
  }

  /**
   * Returns the number of HandGear items currently equipped.
   * Useful for verifying slot state in tests.
   *
   * @return count of equipped hand items (0, 1, or 2)
   */
  public int getHandCount() {
    return handCount;
  }

  /**
   * Returns the number of Footwear items currently equipped.
   * Useful for verifying slot state in tests.
   *
   * @return count of equipped foot items (0, 1, or 2)
   */
  public int getFootCount() {
    return feetCount;
  }

  /**
   * Returns the currently equipped HeadGear, or null if the slot is empty.
   *
   * @return the equipped HeadGear or null
   */
  public HeadGear getHead() {
    return head;
  }

  /**
   * Returns the total attack: base attack plus all equipped items' attack values.
   *
   * @return total attack strength
   */
  public int getTotalAttack() {
    int total = baseAttack;
    for (int i = 0; i < handCount; i++) {
      total += hands[i].getAttack();
    }
    for (int i = 0; i < feetCount; i++) {
      total += feet[i].getAttack();
    }
    return total;
  }

  /**
   * Returns the total defense: base defense plus all equipped items' defense values.
   *
   * @return total defense strength
   */
  public int getTotalDefense() {
    int total = baseDefense;
    if (head != null) {
      total += head.getDefense();
    }
    for (int i = 0; i < feetCount; i++) {
      total += feet[i].getDefense();
    }
    return total;
  }

  /**
   * Returns a readable summary of this character's stats and all equipped items.
   *
   * @return formatted string showing total attack, defense, and all equipped items
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(String.format("Total ATK: %d | Total DEF: %d%n",
        getTotalAttack(), getTotalDefense()));
    if (head != null) {
      sb.append("  Head  : ").append(head).append("\n");
    }
    for (int i = 0; i < handCount; i++) {
      sb.append(String.format("  Hand %d: %s%n", i + 1, hands[i]));
    }
    for (int i = 0; i < feetCount; i++) {
      sb.append(String.format("  Foot %d: %s%n", i + 1, feet[i]));
    }
    return sb.toString();
  }

  // ── private helpers ──────────────────────────────────────────────────────

  private void addHeadGear(HeadGear item) {
    if (head == null) {
      head = item;
    } else {
      head = (HeadGear) head.combine(item);
    }
  }

  private void addHandGear(HandGear item) {
    if (handCount < MAX_HANDS) {
      hands[handCount++] = item;
    } else {
      hands[0] = (HandGear) hands[0].combine(item);
    }
  }

  private void addFootwear(Footwear item) {
    if (feetCount < MAX_FEET) {
      feet[feetCount++] = item;
    } else {
      feet[0] = (Footwear) feet[0].combine(item);
    }
  }
}
