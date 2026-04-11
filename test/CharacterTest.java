import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the Character class.
 * Covers: construction, slot management, automatic combining when slots are full,
 * total attack/defense calculations, slot-availability queries, and toString output.
 */
public class CharacterTest {

  private Character hero;

  @Before
  public void setUp() {
    hero = new Character(10, 8);
  }

  // ── Construction ─────────────────────────────────────────────────────────

  @Test
  public void testBaseStatsStoredCorrectly() {
    assertEquals(10, hero.getTotalAttack());
    assertEquals(8,  hero.getTotalDefense());
  }

  @Test
  public void testNoItemsEquippedOnCreation() {
    assertNull(hero.getHead());
    assertEquals(0, hero.getHandCount());
    assertEquals(0, hero.getFootCount());
  }

  // ── Slot availability: head ───────────────────────────────────────────────

  @Test
  public void testHeadSlotAvailableWhenEmpty() {
    assertTrue(hero.hasAvailableHeadSlot());
  }

  @Test
  public void testHeadSlotNotAvailableWhenFull() {
    hero.addItem(new HeadGear("Iron", "Helmet", 5));
    assertFalse(hero.hasAvailableHeadSlot());
  }

  // ── Slot availability: hands ─────────────────────────────────────────────

  @Test
  public void testHandSlotAvailableWhenEmpty() {
    assertTrue(hero.hasAvailableHandSlot());
  }

  @Test
  public void testHandSlotAvailableWithOneItem() {
    hero.addItem(new HandGear("Sharp", "Sword", 5));
    assertTrue(hero.hasAvailableHandSlot());
  }

  @Test
  public void testHandSlotNotAvailableWhenBothFull() {
    hero.addItem(new HandGear("Sharp", "Sword", 5));
    hero.addItem(new HandGear("Quick", "Dagger", 3));
    assertFalse(hero.hasAvailableHandSlot());
  }

  // ── Slot availability: feet ───────────────────────────────────────────────

  @Test
  public void testFootSlotAvailableWhenEmpty() {
    assertTrue(hero.hasAvailableFootSlot());
  }

  @Test
  public void testFootSlotAvailableWithOneItem() {
    hero.addItem(new Footwear("Swift", "Boots", 1, 2));
    assertTrue(hero.hasAvailableFootSlot());
  }

  @Test
  public void testFootSlotNotAvailableWhenBothFull() {
    hero.addItem(new Footwear("Swift", "Boots", 1, 2));
    hero.addItem(new Footwear("Bouncy", "Sneakers", 0, 3));
    assertFalse(hero.hasAvailableFootSlot());
  }

  // ── HeadGear slot ────────────────────────────────────────────────────────

  @Test
  public void testAddHeadGearIncreasesDefense() {
    hero.addItem(new HeadGear("Iron", "Helmet", 5));
    assertEquals(13, hero.getTotalDefense()); // 8 + 5
    assertEquals(10, hero.getTotalAttack());  // unchanged
  }

  @Test
  public void testHeadGearSlotFullCombines() {
    hero.addItem(new HeadGear("Iron", "Helmet", 4));
    hero.addItem(new HeadGear("Leather", "Cap", 3));
    assertEquals(15, hero.getTotalDefense()); // 8 + 4 + 3
    assertFalse(hero.hasAvailableHeadSlot());
    assertTrue(hero.getHead().toString().contains("DEF: 7"));
  }

  @Test
  public void testHeadGearSlotRemainsOneAfterCombine() {
    hero.addItem(new HeadGear("Iron", "Helmet", 4));
    hero.addItem(new HeadGear("Leather", "Cap", 3));
    String s = hero.toString();
    assertTrue(s.contains("Head"));
    assertFalse(hero.hasAvailableHeadSlot());
  }

  // ── HandGear slots ────────────────────────────────────────────────────────

  @Test
  public void testAddOneHandGear() {
    hero.addItem(new HandGear("Sharp", "Sword", 5));
    assertEquals(15, hero.getTotalAttack()); // 10 + 5
    assertEquals(1,  hero.getHandCount());
  }

  @Test
  public void testAddTwoHandGear() {
    hero.addItem(new HandGear("Sharp", "Sword", 5));
    hero.addItem(new HandGear("Quick", "Dagger", 3));
    assertEquals(18, hero.getTotalAttack());  // 10 + 5 + 3
    assertEquals(8,  hero.getTotalDefense()); // unchanged
    assertEquals(2,  hero.getHandCount());
  }

  @Test
  public void testThirdHandGearCombinesKeepsTwoSlots() {
    hero.addItem(new HandGear("Sharp", "Sword", 5));
    hero.addItem(new HandGear("Quick", "Dagger", 3));
    hero.addItem(new HandGear("Heavy", "Axe", 4));
    assertEquals(22, hero.getTotalAttack()); // 10 + 5 + 3 + 4
    assertEquals(2,  hero.getHandCount());  // still 2 slots

    String s = hero.toString();
    assertTrue(s.contains("Hand 1"));
    assertTrue(s.contains("Hand 2"));
    assertFalse(s.contains("Hand 3"));
  }

  @Test
  public void testFourthHandGearStillTwoSlots() {
    hero.addItem(new HandGear("Sharp", "Sword", 5));
    hero.addItem(new HandGear("Quick", "Dagger", 3));
    hero.addItem(new HandGear("Heavy", "Axe", 4));
    hero.addItem(new HandGear("Flaming", "Glove", 2));
    assertEquals(24, hero.getTotalAttack()); // 10+5+3+4+2
    assertEquals(2,  hero.getHandCount());
  }

  // ── Footwear slots ────────────────────────────────────────────────────────

  @Test
  public void testAddOneFootwear() {
    hero.addItem(new Footwear("Swift", "Boots", 1, 3));
    assertEquals(11, hero.getTotalAttack());  // 10 + 1
    assertEquals(11, hero.getTotalDefense()); // 8 + 3
    assertEquals(1,  hero.getFootCount());
  }

  @Test
  public void testAddTwoFootwear() {
    hero.addItem(new Footwear("Swift", "Boots", 1, 3));
    hero.addItem(new Footwear("Bouncy", "Sneakers", 0, 2));
    assertEquals(11, hero.getTotalAttack());  // 10+1+0
    assertEquals(13, hero.getTotalDefense()); // 8+3+2
    assertEquals(2,  hero.getFootCount());
  }

  @Test
  public void testThirdFootwearCombinesKeepsTwoSlots() {
    hero.addItem(new Footwear("Swift", "Boots", 1, 3));
    hero.addItem(new Footwear("Bouncy", "Sneakers", 0, 2));
    hero.addItem(new Footwear("Happy", "HoverBoard", 2, 1));
    assertEquals(13, hero.getTotalAttack());  // 10+1+0+2
    assertEquals(14, hero.getTotalDefense()); // 8+3+2+1
    assertEquals(2,  hero.getFootCount());

    String s = hero.toString();
    assertTrue(s.contains("Foot 1"));
    assertTrue(s.contains("Foot 2"));
    assertFalse(s.contains("Foot 3"));
  }

  // ── Mixed items ───────────────────────────────────────────────────────────

  @Test
  public void testAllSlotsFilled() {
    hero.addItem(new HeadGear("Iron", "Helmet", 4));
    hero.addItem(new HandGear("Sharp", "Sword", 5));
    hero.addItem(new HandGear("Quick", "Dagger", 3));
    hero.addItem(new Footwear("Swift", "Boots", 2, 3));
    hero.addItem(new Footwear("Bouncy", "Sneakers", 1, 2));
    assertEquals(21, hero.getTotalAttack());  // 10+5+3+2+1
    assertEquals(17, hero.getTotalDefense()); // 8+4+3+2
  }

  @Test
  public void testNoItemsBaseStatsOnly() {
    assertEquals(10, hero.getTotalAttack());
    assertEquals(8,  hero.getTotalDefense());
  }

  // ── toString ─────────────────────────────────────────────────────────────

  @Test
  public void testToStringContainsTotalStats() {
    hero.addItem(new HeadGear("Iron", "Helmet", 5));
    String s = hero.toString();
    assertTrue(s.contains("Total ATK: 10"));
    assertTrue(s.contains("Total DEF: 13"));
    assertTrue(s.contains("Head"));
  }

  @Test
  public void testToStringWithNoItemsShowsBaseStats() {
    String s = hero.toString();
    assertTrue(s.contains("Total ATK: 10"));
    assertTrue(s.contains("Total DEF: 8"));
    assertFalse(s.contains("Head"));
    assertFalse(s.contains("Hand"));
    assertFalse(s.contains("Foot"));
  }

  // ── Unknown item type throws ──────────────────────────────────────────────

  @Test(expected = IllegalArgumentException.class)
  public void testUnknownItemTypeThrows() {
    hero.addItem(new Item() {
      public String getName()       { return "Ghost Item"; }
      public int    getAttack()     { return 0; }
      public int    getDefense()    { return 0; }
      public Item   combine(Item o) { return this; }
    });
  }
}
