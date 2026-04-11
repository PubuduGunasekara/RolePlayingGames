import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for Item, AbstractItem, HeadGear, HandGear, and Footwear.
 * Items are created directly via their constructors — no factory needed.
 * Covers: creation, stat constraints, name formatting, combine logic,
 * isStrongerThan, toString, and all IllegalArgumentException cases.
 */
public class ItemTest {

  // ── HeadGear creation ────────────────────────────────────────────────────

  @Test
  public void testHeadGearAttackAlwaysZero() {
    HeadGear helmet = new HeadGear("Iron", "Helmet", 5);
    assertEquals(0, helmet.getAttack());
  }

  @Test
  public void testHeadGearDefenseStoredCorrectly() {
    HeadGear helmet = new HeadGear("Iron", "Helmet", 5);
    assertEquals(5, helmet.getDefense());
  }

  @Test
  public void testHeadGearNameFormed() {
    HeadGear helmet = new HeadGear("Iron", "Helmet", 5);
    assertEquals("Iron Helmet", helmet.getName());
  }

  // ── HandGear creation ────────────────────────────────────────────────────

  @Test
  public void testHandGearDefenseAlwaysZero() {
    HandGear sword = new HandGear("Sharp", "Sword", 6);
    assertEquals(0, sword.getDefense());
  }

  @Test
  public void testHandGearAttackStoredCorrectly() {
    HandGear sword = new HandGear("Sharp", "Sword", 6);
    assertEquals(6, sword.getAttack());
  }

  @Test
  public void testHandGearNameFormed() {
    HandGear sword = new HandGear("Sharp", "Sword", 6);
    assertEquals("Sharp Sword", sword.getName());
  }

  // ── Footwear creation ────────────────────────────────────────────────────

  @Test
  public void testFootwearStoresBothStats() {
    Footwear boots = new Footwear("Swift", "Boots", 2, 3);
    assertEquals(2, boots.getAttack());
    assertEquals(3, boots.getDefense());
  }

  @Test
  public void testFootwearNameFormed() {
    Footwear boots = new Footwear("Swift", "Boots", 2, 3);
    assertEquals("Swift Boots", boots.getName());
  }

  // ── HeadGear combine ─────────────────────────────────────────────────────

  @Test
  public void testHeadGearCombineOtherStronger() {
    HeadGear weak   = new HeadGear("Leather", "Cap", 2);
    HeadGear strong = new HeadGear("Iron", "Helmet", 6);
    Item result = weak.combine(strong);
    assertEquals("Leather Iron Helmet", result.getName());
    assertEquals(0, result.getAttack());
    assertEquals(8, result.getDefense());
    assertTrue(result instanceof HeadGear);
  }

  @Test
  public void testHeadGearCombineThisStronger() {
    HeadGear strong = new HeadGear("Iron", "Helmet", 8);
    HeadGear weak   = new HeadGear("Leather", "Cap", 3);
    Item result = strong.combine(weak);
    assertEquals("Leather Iron Helmet", result.getName());
    assertEquals(0, result.getAttack());
    assertEquals(11, result.getDefense());
    assertTrue(result instanceof HeadGear);
  }

  @Test
  public void testHeadGearCombineResultIsAlwaysHeadGear() {
    HeadGear h1 = new HeadGear("Golden", "Crown", 4);
    HeadGear h2 = new HeadGear("Silver", "Visor", 7);
    Item result = h1.combine(h2);
    assertEquals(0, result.getAttack());
    assertTrue(result instanceof HeadGear);
  }

  // ── HandGear combine ─────────────────────────────────────────────────────

  @Test
  public void testHandGearCombineOtherStronger() {
    HandGear weak   = new HandGear("Rusty", "Dagger", 2);
    HandGear strong = new HandGear("Sharp", "Sword", 7);
    Item result = weak.combine(strong);
    assertEquals("Rusty Sharp Sword", result.getName());
    assertEquals(9, result.getAttack());
    assertEquals(0, result.getDefense());
    assertTrue(result instanceof HandGear);
  }

  @Test
  public void testHandGearCombineThisStronger() {
    HandGear strong = new HandGear("Sharp", "Sword", 8);
    HandGear weak   = new HandGear("Rusty", "Dagger", 3);
    Item result = strong.combine(weak);
    assertEquals("Rusty Sharp Sword", result.getName());
    assertEquals(11, result.getAttack());
    assertEquals(0, result.getDefense());
    assertTrue(result instanceof HandGear);
  }

  @Test
  public void testHandGearCombineResultIsAlwaysHandGear() {
    HandGear h1 = new HandGear("Flaming", "Glove", 5);
    HandGear h2 = new HandGear("Frozen", "Shield", 3);
    Item result = h1.combine(h2);
    assertEquals(0, result.getDefense());
    assertTrue(result instanceof HandGear);
  }

  // ── Footwear combine ─────────────────────────────────────────────────────

  @Test
  public void testFootwearCombineHigherAttackWins() {
    Footwear sandals    = new Footwear("Scurrying", "Sandals", 0, 1);
    Footwear hoverboard = new Footwear("Happy", "HoverBoard", 1, 3);
    Item result = sandals.combine(hoverboard);
    assertEquals("Scurrying Happy HoverBoard", result.getName());
    assertEquals(1, result.getAttack());
    assertEquals(4, result.getDefense());
    assertTrue(result instanceof Footwear);
  }

  @Test
  public void testFootwearCombineEqualAttackDefenseTiebreak() {
    Footwear boots1 = new Footwear("Old", "Boots", 2, 2);
    Footwear boots2 = new Footwear("Steel", "Boots", 2, 5);
    Item result = boots1.combine(boots2);
    assertEquals("Old Steel Boots", result.getName());
    assertEquals(4, result.getAttack());
    assertEquals(7, result.getDefense());
  }

  @Test
  public void testFootwearCombineThisStronger() {
    Footwear strong = new Footwear("Swift", "Boots", 3, 4);
    Footwear weak   = new Footwear("Scruffy", "Sandals", 1, 2);
    Item result = strong.combine(weak);
    assertEquals("Scruffy Swift Boots", result.getName());
    assertEquals(4, result.getAttack());
    assertEquals(6, result.getDefense());
    assertTrue(result instanceof Footwear);
  }

  @Test
  public void testFootwearCombineBothStatsSummed() {
    Footwear f1 = new Footwear("Swift", "Boots", 3, 2);
    Footwear f2 = new Footwear("Bouncy", "Sneakers", 1, 4);
    Item result = f1.combine(f2);
    assertEquals(4, result.getAttack());
    assertEquals(6, result.getDefense());
    assertTrue(result instanceof Footwear);
  }

  // ── combine tie (random branch) ──────────────────────────────────────────

  @Test
  public void testCombineTieBreakRandomBranchIsHit() {
    // Two identical items triggers the random branch in AbstractItem.combine()
    HandGear a = new HandGear("Fast", "Blade", 5);
    HandGear b = new HandGear("Heavy", "Axe", 5);
    Item result = a.combine(b);
    assertNotNull(result);
    assertEquals(10, result.getAttack());
    assertEquals(0, result.getDefense());
    assertTrue(result instanceof HandGear);
  }

  // ── isStrongerThan ───────────────────────────────────────────────────────

  @Test
  public void testIsStrongerThanByAttack() {
    Item weak   = new HandGear("Rusty", "Dagger", 2);
    Item strong = new HandGear("Sharp", "Sword", 6);
    assertTrue(strong.isStrongerThan(weak));
    assertFalse(weak.isStrongerThan(strong));
  }

  @Test
  public void testIsStrongerThanDefenseTiebreak() {
    Item low  = new Footwear("Old", "Boots", 2, 2);
    Item high = new Footwear("Steel", "Boots", 2, 8);
    assertTrue(high.isStrongerThan(low));
    assertFalse(low.isStrongerThan(high));
  }

  @Test
  public void testIsStrongerThanViaItemReference() {
    Item weak   = new HeadGear("Leather", "Cap", 2);
    Item strong = new HeadGear("Iron", "Helmet", 6);
    assertTrue(strong.isStrongerThan(weak));
    assertFalse(weak.isStrongerThan(strong));
  }

  // ── Polymorphism ─────────────────────────────────────────────────────────

  @Test
  public void testAllTypesAsItemInterface() {
    Item i1 = new HeadGear("Iron", "Helmet", 5);
    Item i2 = new HandGear("Sharp", "Sword", 6);
    Item i3 = new Footwear("Swift", "Boots", 2, 3);

    assertEquals(0, i1.getAttack());
    assertEquals(5, i1.getDefense());
    assertEquals(6, i2.getAttack());
    assertEquals(0, i2.getDefense());
    assertEquals(2, i3.getAttack());
    assertEquals(3, i3.getDefense());

    assertTrue(i1 instanceof Item);
    assertTrue(i2 instanceof Item);
    assertTrue(i3 instanceof Item);
  }

  // ── toString ─────────────────────────────────────────────────────────────

  @Test
  public void testHeadGearToString() {
    String s = new HeadGear("Iron", "Helmet", 5).toString();
    assertTrue(s.contains("Iron Helmet"));
    assertTrue(s.contains("ATK: 0"));
    assertTrue(s.contains("DEF: 5"));
  }

  @Test
  public void testHandGearToString() {
    String s = new HandGear("Sharp", "Sword", 6).toString();
    assertTrue(s.contains("Sharp Sword"));
    assertTrue(s.contains("ATK: 6"));
    assertTrue(s.contains("DEF: 0"));
  }

  @Test
  public void testFootwearToString() {
    String s = new Footwear("Swift", "Boots", 2, 3).toString();
    assertTrue(s.contains("Swift Boots"));
    assertTrue(s.contains("ATK: 2"));
    assertTrue(s.contains("DEF: 3"));
  }

  // ── IllegalArgumentException cases ───────────────────────────────────────

  @Test(expected = IllegalArgumentException.class)
  public void testHeadGearNegativeDefenseThrows() {
    new HeadGear("Bad", "Helmet", -1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testHandGearNegativeAttackThrows() {
    new HandGear("Bad", "Sword", -1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFootwearNegativeAttackThrows() {
    new Footwear("Bad", "Boots", -1, 3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFootwearNegativeDefenseThrows() {
    new Footwear("Bad", "Boots", 2, -1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullAdjectiveThrows() {
    new HeadGear(null, "Helmet", 5);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullNounThrows() {
    new HandGear("Sharp", null, 6);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullBothThrows() {
    new Footwear(null, null, 2, 3);
  }
}
