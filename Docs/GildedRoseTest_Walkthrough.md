# GildedRoseTest – Starter Test Walkthrough

This document explains the original `GildedRoseTest.java` file that comes with
the Gilded Rose refactoring kata. The purpose of this walkthrough is to ensure
the entire mob programming team understands what the initial test does and why
it must be replaced with meaningful unit tests before refactoring.

---

## Original Test Code

```java
@Test
void foo() {
    Item[] items = new Item[] { new Item("foo", 0, 0) };
    GildedRose app = new GildedRose(items);
    app.updateQuality();
    assertEquals("fixme", app.items[0].name);
}
```

## Line-by-Line Explanation
### @Test
Marks the foo() method as a JUnit 5 test case.
- JUnit automatically discovers and runs this method when the suite is executed.

### Item[] items = new Item[] { new Item("foo", 0, 0) };
- Creates an array containing a single Item object with these properties:

  - Name: "foo"

  - SellIn: 0

  - Quality: 0

Note: This is a "dummy" item; it doesn't represent any real business rules.

### GildedRose app = new GildedRose(items);
Instantiates the main class and injects our item array.
This allows the test to exercise the legacy logic inside updateQuality().

### app.updateQuality();
Executes the core business logic we intend to refactor.
It typically updates sellIn, updates quality, and applies item-specific rules.

- Warning: Do not change this method until tests protect its current behavior!

### assertEquals("fixme", app.items[0].name);
Why it fails: It asserts the name is "fixme", but the name is actually "foo".
Since updateQuality() never changes item names, this test is designed to stay broken until you fix it.

## Why This Test Exists
This is a Sanity Check test. It is not meant to verify business logic, but rather to:

- Confirm JUnit is configured correctly.

- Verify tests are being discovered.

- Show you what a failure looks like.

- Signal: "Replace me with real tests before you touch the code!"

## Why It Must Be Replaced
- It does not describe real business rules (like Aged Brie or Sulfuras).
 
- It provides no "safety net" for refactoring.

- It blocks the build by always failing.

## Our Mob Programming Focus
- Before refactoring updateQuality(), we must replace this with tests covering:

- Normal items (standard degradation).

- Special items (Aged Brie, Backstage passes, Sulfuras).

- Edge cases (Quality bounds of 0 and 50, expired SellIn).

- Rule of Thumb: Only refactor once the tests fully protect the existing behavior.