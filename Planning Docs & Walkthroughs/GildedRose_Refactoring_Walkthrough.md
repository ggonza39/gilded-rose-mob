# Gilded Rose – Refactoring Walkthrough

This document explains how our team refactored the legacy `GildedRose.java` production code as a mob.

The goal of this refactoring was not to change behavior, but to remove code smells and improve readability, maintainability, and safety. All refactoring was done after unit tests were written and passing, giving us confidence that behavior remained unchanged.

---

## Original Problem
The original `updateQuality()` method contained:
* Deeply nested `if` statements
* Repeated string comparisons
* Mixed responsibilities (sellIn updates, quality updates, expiration logic)
* Hard-to-follow business rules

Although the code worked, it was risky to modify and difficult to explain.

---

## Refactoring Goals
As a mob, we agreed on the following goals:
1. Preserve 100% existing behavior.
2. Reduce nesting and duplication.
3. Make business rules explicit.
4. Improve readability for future developers.
5. Keep the code short and easy to explain during a recording.

---

## Refactoring Strategy
We refactored in small, safe steps, guided by our unit tests:
* **Extract logic** into small helper methods.
* **Separate responsibilities:**
    * Quality updates (before expiration)
    * Sell-in updates
    * Quality updates (after expiration)
* **Centralize item type checks.**
* **Replace deeply nested logic** with clear conditionals.

---

## Refactored GildedRose.java

```java
package com.gildedrose;

class GildedRose {
    Item[] items;

    public GildedRose(Item[] items) {
        this.items = items;
    }

    /**
     * Main update method.
     */
    public void updateQuality() {
        for (Item item : items) {
            // Step 1: Update quality before sellIn changes
            updateItemQuality(item);

            // Step 2: Decrease sellIn for non-legendary items
            updateSellIn(item);

            // Step 3: Apply expiration rules if sellIn < 0
            handleExpiredItem(item);
        }
    }
}
```
## Step 1: Quality Updates Before Expiration

```java
private void updateItemQuality(Item item) {
    if (isAgedBrie(item)) {
        increaseQuality(item);
    } else if (isBackstagePass(item)) {
        increaseQuality(item);
        if (item.sellIn <= 10) {
            increaseQuality(item);
        }
        if (item.sellIn <= 5) {
            increaseQuality(item);
        }
    } else if (!isSulfuras(item)) {
        decreaseQuality(item);
    }
}
```

## Why This Change?

- Replaces deeply nested if statements

- Makes each business rule visible

- Matches rules from the kata description:

  - Aged Brie increases in quality

  - Backstage passes increase faster as the concert approaches

  - Normal items decrease in quality

  - Sulfuras never changes
  
## Step 2: Sell-In Updates

```java
private void updateSellIn(Item item) {
    if (!isSulfuras(item)) {
        item.sellIn--;
    }
}
```
## Why This Change?

- The original code repeated this logic in multiple places

- Sulfuras is legendary and never changes

- Separating this logic improves readability and correctness

## Step 3: Expiration Logic

```java
private void handleExpiredItem(Item item) {
    if (item.sellIn >= 0) {
        return;
    }

    if (isAgedBrie(item)) {
        increaseQuality(item);
    } else if (isBackstagePass(item)) {
        item.quality = 0;
    } else if (!isSulfuras(item)) {
        decreaseQuality(item);
    }
}
```
## Why This Change?

- Expiration rules were previously mixed into multiple nested blocks

- This isolates post-expiration behavior

- Makes it easy to explain:

  - Normal items degrade faster

  - Aged Brie improves faster

  - Backstage passes drop to zero

  - Sulfuras remains unchanged

# Helper Methods
## Quality Boundaries

```java
private void increaseQuality(Item item) {
    if (item.quality < 50) {
        item.quality++;
    }
}

private void decreaseQuality(Item item) {
    if (item.quality > 0) {
        item.quality--;
    }
}
```
## Why This Change?

- Quality bounds (0–50) are global rules

- Centralizing them prevents bugs

- Makes future changes safer

## Item Type Checks

```java
private boolean isAgedBrie(Item item) {
    return item.name.equals("Aged Brie");
}

private boolean isBackstagePass(Item item) {
    return item.name.equals("Backstage passes to a TAFKAL80ETC concert");
}

private boolean isSulfuras(Item item) {
    return item.name.equals("Sulfuras, Hand of Ragnaros");
}
```
## Why This Change?

- Removes duplicated string comparisons

- Avoids “magic strings” scattered across the code

- Prepares the code for future refactoring (i.e. polymorphism)

## What We Did Not Do (On Purpose)
- We did not introduce inheritance or new classes yet.

- We did not change method signatures.

- We did not add new features yet (like Conjured items).
