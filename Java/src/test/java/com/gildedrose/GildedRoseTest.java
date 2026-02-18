package com.gildedrose;

/*
 * Unit tests for validating GildedRose behavior.
 *
 * PURPOSE:
 * These tests define the expected business rules of the system.
 * They describe WHAT the system should do — not HOW it does it.
 *
 * WHY THIS MATTERS:
 * - They act as a safety net when refactoring legacy code.
 * - They protect against regressions when adding new features.
 * - They document system behavior in executable form.
 *
 * The original placeholder test from the kata is preserved
 * below (commented out) to show the starting point.
 */

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GildedRoseTest {

    /* ============================================================
       ORIGINAL PLACEHOLDER TEST (FROM KATA)
       ------------------------------------------------------------
       This test was intentionally incorrect and always failed.
       It confirmed that the test framework was wired correctly.
       It has been commented out and replaced with real tests.
       ============================================================ */

//    @Test
//    void foo() {
//        // Placeholder test from the kata.
//        // Confirms the test framework is working.
//
//        Item[] items = new Item[] { new Item("foo", 0, 0) };
//        // Dummy item with no special rules
//
//        GildedRose app = new GildedRose(items);
//        app.updateQuality();
//
//        // Intentionally failing assertion — to be replaced
//        assertEquals("fixme", app.items[0].name);
//    }

    /* NORMAL ITEMS

       Default behavior that most items follow.
      */

    @Test
    void normalItem_decreasesQualityAndSellInByOne() {
        // WHAT: Tests standard item behavior before sell-by date
        // WHY: This represents the default rule

        Item[] items = new Item[] {
            new Item("Elixir of the Mongoose", 5, 7)
        };

        GildedRose app = new GildedRose(items);
        app.updateQuality();

        // sellIn decreases by 1 each day
        assertEquals(4, app.items[0].sellIn);

        // quality decreases by 1 before expiration
        assertEquals(6, app.items[0].quality);
    }

    @Test
    void normalItem_qualityDegradesTwiceAsFastAfterSellDate() {
        // WHAT: Tests behavior once sellIn reaches 0
        // WHY: Expired normal items degrade twice as fast

        Item[] items = new Item[] {
            new Item("Elixir of the Mongoose", 0, 6)
        };

        GildedRose app = new GildedRose(items);
        app.updateQuality();

        // sellIn still decreases
        assertEquals(-1, app.items[0].sellIn);

        // quality decreases by 2 after expiration
        assertEquals(4, app.items[0].quality);
    }

    @Test
    void qualityNeverNegative() {
        // WHAT: Ensures quality never drops below zero
        // WHY: Global business constraint

        Item[] items = new Item[] {
            new Item("Elixir of the Mongoose", 0, 0)
        };

        GildedRose app = new GildedRose(items);
        app.updateQuality();

        assertEquals(0, app.items[0].quality);
    }

    /* AGED BRIE

       Special item that increases in quality over time.
      */

    @Test
    void agedBrie_increasesInQualityOverTime() {
        // WHAT: Aged Brie improves as it ages
        // WHY: This item breaks the normal degradation rule

        Item[] items = new Item[] {
            new Item("Aged Brie", 5, 10)
        };

        GildedRose app = new GildedRose(items);
        app.updateQuality();

        assertEquals(11, app.items[0].quality);
    }

    @Test
    void agedBrie_qualityIncreasesTwiceAsFastAfterSellDate() {
        // WHAT: Behavior after expiration
        // WHY: Aged Brie increases twice as fast once expired

        Item[] items = new Item[] {
            new Item("Aged Brie", 0, 10)
        };

        GildedRose app = new GildedRose(items);
        app.updateQuality();

        assertEquals(12, app.items[0].quality);
    }

    @Test
    void qualityNeverExceedsFifty() {
        // WHAT: Ensures quality never exceeds 50
        // WHY: Global upper-bound constraint for all items (except Sulfuras)

        Item[] items = new Item[] {
            new Item("Aged Brie", 0, 50)
        };

        GildedRose app = new GildedRose(items);
        app.updateQuality();

        assertEquals(50, app.items[0].quality);
    }

    /* SULFURAS

       Legendary item that never changes.
        */

    @Test
    void sulfuras_neverChangesSellInOrQuality() {
        // WHAT: Tests immutable item behavior
        // WHY: Sulfuras is legendary and never degrades

        Item[] items = new Item[] {
            new Item("Sulfuras, Hand of Ragnaros", 10, 80)
        };

        GildedRose app = new GildedRose(items);
        app.updateQuality();

        assertEquals(10, app.items[0].sellIn);
        assertEquals(80, app.items[0].quality);
    }

    /* BACKSTAGE PASSES

       Increases in value as the concert approaches,
       then drops to zero after the event.
       */

    @Test
    void backstagePasses_increaseByOneWhenMoreThanTenDaysLeft() {
        // WHAT: Early concert period
        // WHY: Value increases slowly when event is far away

        Item[] items = new Item[] {
            new Item("Backstage passes to a TAFKAL80ETC concert", 15, 20)
        };

        GildedRose app = new GildedRose(items);
        app.updateQuality();

        assertEquals(21, app.items[0].quality);
    }

    @Test
    void backstagePasses_increaseByTwoWhenTenDaysOrLess() {
        // WHAT: Mid concert window (10 days or fewer)
        // WHY: Demand increases faster

        Item[] items = new Item[] {
            new Item("Backstage passes to a TAFKAL80ETC concert", 10, 20)
        };

        GildedRose app = new GildedRose(items);

        app.updateQuality();
        assertEquals(22, app.items[0].quality);

        app.updateQuality();
        assertEquals(24, app.items[0].quality);
    }

    @Test
    void backstagePasses_increaseByThreeWhenFiveDaysOrLess() {
        // WHAT: Final concert window (5 days or fewer)
        // WHY: Highest demand just before event

        Item[] items = new Item[] {
            new Item("Backstage passes to a TAFKAL80ETC concert", 5, 20)
        };

        GildedRose app = new GildedRose(items);

        app.updateQuality();
        assertEquals(23, app.items[0].quality);

        app.updateQuality();
        assertEquals(26, app.items[0].quality);
    }

    @Test
    void backstagePasses_dropToZeroAfterConcert() {
        // WHAT: After sellIn reaches 0
        // WHY: Passes become worthless after the concert

        Item[] items = new Item[] {
            new Item("Backstage passes to a TAFKAL80ETC concert", 0, 20)
        };

        GildedRose app = new GildedRose(items);
        app.updateQuality();

        assertEquals(0, app.items[0].quality);
    }

    /* CONJURED ITEMS

       New feature added via TDD.
       Conjured items degrade twice as fast as normal items.
       */

    @Test
    void conjured_items_degradeTwiceAsFast_beforeExpiration() {
        // WHAT: Behavior before sell-by date
        // WHY: Conjured items degrade twice as fast as normal items

        Item[] items = new Item[] {
            new Item("Conjured Mana Cake", 5, 10)
        };

        GildedRose app = new GildedRose(items);
        app.updateQuality();

        // Normal item: 10 -> 9
        // Conjured item: 10 -> 8
        assertEquals(8, app.items[0].quality);

        // sellIn still decreases normally
        assertEquals(4, app.items[0].sellIn);
    }

    @Test
    void conjured_items_degradeFourAfterExpiration() {
        // WHAT: Behavior after expiration
        // WHY: Normal expired items degrade by 2, so Conjured expired items degrade by 4

        Item[] items = new Item[] {
            new Item("Conjured Mana Cake", 0, 10)
        };

        GildedRose app = new GildedRose(items);
        app.updateQuality();

        assertEquals(6, app.items[0].quality);
        assertEquals(-1, app.items[0].sellIn);
    }

}
