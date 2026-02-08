# GildedRoseTest | Proposed Test Suite Walkthrough

This document explains the proposed replacement tests for `GildedRoseTest.java`
in the Gilded Rose refactoring kata.

The purpose of this walkthrough is to ensure the entire mob programming team:

- Understands what behavior each test protects
- Agrees on why each test exists
- Is aligned before recording and refactoring begin

These tests replace the original placeholder test and act as a safety net for
refactoring `updateQuality()` without changing existing behavior.

---

## Normal Item Tests

### Normal Item – Standard Degradation

```java
@Test
void normalItem_decreasesQualityAndSellInByOne() {
    Item[] items = { new Item("Elixir of the Mongoose", 5, 7) };
    GildedRose app = new GildedRose(items);
    app.updateQuality();
    assertEquals(4, items[0].sellIn);
    assertEquals(6, items[0].quality);
}
```
### What this tests

- Normal items decrease sellIn by 1

- Normal items decrease quality by 1

### Why this test exists
This is the default rule for most items. If this breaks, core system behavior
is broken.

### Normal Item – Expired Items Degrade Faster

```java
@Test
void normalItem_degradesTwiceAsFastAfterSellDate() {
    Item[] items = { new Item("Elixir of the Mongoose", 0, 10) };
    GildedRose app = new GildedRose(items);
    app.updateQuality();
    assertEquals(8, items[0].quality);
}
```
### What this tests

After expiration, quality degrades twice as fast

### Why this test exists
Expiration logic is easy to break during refactoring due to conditional ordering.

### Quality Lower Bound

```java
@Test
void qualityIsNeverNegative() {
Item[] items = { new Item("Elixir of the Mongoose", 5, 0) };
GildedRose app = new GildedRose(items);
app.updateQuality();
assertEquals(0, items[0].quality);
}
```
### What this tests

Quality never drops below zero

### Why this test exists
This is a global invariant that must always hold.

## Aged Brie Tests
### Aged Brie – Increases Over Time

```java
@Test
void agedBrie_increasesInQualityOverTime() {
Item[] items = { new Item("Aged Brie", 5, 10) };
GildedRose app = new GildedRose(items);
app.updateQuality();
assertEquals(11, items[0].quality);
}
```
### What this tests

Aged Brie increases in quality instead of decreasing

### Why this test exists
Aged Brie violates the default rule and must be explicitly protected.

### Aged Brie – Faster Increase After Sell Date

```java
@Test
void agedBrie_qualityIncreasesTwiceAsFastAfterSellDate() {
Item[] items = { new Item("Aged Brie", 0, 10) };
GildedRose app = new GildedRose(items);
app.updateQuality();
assertEquals(12, items[0].quality);
}
```
### What this tests

After expiration, Aged Brie increases twice as fast

### Why this test exists
Expiration rules differ by item type and are commonly broken.

### Quality Upper Bound

```java
@Test
void qualityNeverExceedsFifty() {
Item[] items = { new Item("Aged Brie", 5, 50) };
GildedRose app = new GildedRose(items);
app.updateQuality();
assertEquals(50, items[0].quality);
}
```
### What this tests

Quality never exceeds 50

### Why this test exists
This is another global invariant that prevents invalid state.

## Sulfuras Tests
### Sulfuras – Legendary Item

```java
@Test
void sulfuras_neverChangesSellInOrQuality() {
Item[] items = { new Item("Sulfuras, Hand of Ragnaros", 0, 80) };
GildedRose app = new GildedRose(items);
app.updateQuality();
assertEquals(0, items[0].sellIn);
assertEquals(80, items[0].quality);
}
```
### What this tests

Sulfuras never changes sellIn or quality

### Why this test exists
Sulfuras bypasses all standard rules and must remain immutable.

## Backstage Pass Tests
### Backstage Pass – More Than 10 Days Left

```java
@Test
void backstagePasses_increaseByOneWhenMoreThanTenDaysLeft() {
Item[] items = {
new Item("Backstage passes to a TAFKAL80ETC concert", 15, 20)
};
GildedRose app = new GildedRose(items);
app.updateQuality();
assertEquals(21, items[0].quality);
}
```
### Backstage Pass – 10 Days or Less

```java
@Test
void backstagePasses_increaseByTwoWhenTenDaysOrLess() {
Item[] items = {
new Item("Backstage passes to a TAFKAL80ETC concert", 11, 20)
};
GildedRose app = new GildedRose(items);
app.updateQuality();
assertEquals(22, items[0].quality);
app.updateQuality();
assertEquals(24, items[0].quality);
}
```
### Backstage Pass – 5 Days or Less

```java
@Test
void backstagePasses_increaseByThreeWhenFiveDaysOrLess() {
Item[] items = {
new Item("Backstage passes to a TAFKAL80ETC concert", 5, 20)
};
GildedRose app = new GildedRose(items);
app.updateQuality();
assertEquals(23, items[0].quality);
app.updateQuality();
assertEquals(26, items[0].quality);
}
```

### Backstage Pass – After the Concert

```java
@Test
void backstagePasses_dropToZeroAfterConcert() {
Item[] items = {
new Item("Backstage passes to a TAFKAL80ETC concert", 0, 20)
};
GildedRose app = new GildedRose(items);
app.updateQuality();
assertEquals(0, items[0].quality);
}
```

### What this tests

That the backstage passes quality increaces by 2 on or after the 10th day, and by 3 on or after the  5th day.
Also tests that quality drops to 0 after the concert.

### Why these tests exist
Backstage passes contain tiered, time-sensitive logic and are the most complex
rules in the system.

## Mob Programming Agreement

- Before refactoring begins, the mob agrees that:

- These tests define the current expected behavior

- All tests must pass before and after refactoring

- Refactoring is not allowed to change observable behavior

### Rule of Thumb:
If a refactor changes a test result, the refactor is wrong.









