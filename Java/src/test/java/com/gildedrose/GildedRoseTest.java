package com.gildedrose;

/*
 * Unit tests for validating GildedRose behavior.
 *
 * IMPORTANT:
 * - Tests describe EXPECTED BEHAVIOR, not implementation.
 * - These tests act as a safety net before refactoring legacy code.
 * - The original placeholder test is kept below (commented out)
 *   to show the starting point of the kata.
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

    /* NORMAL ITEMS */

    @Test
    void normalItem_decreasesQualityAndSellInByOne() {
        // WHAT: Tests standard item behavior before sell-by date
        // WHY: This is the default rule most items follow

        Item[] items = new Item[] {
            new Item("Elixir of the Mongoose", 5, 7)
        };
        GildedRose app = new GildedRose(items);

        app.updateQuality();

        // sellIn decreases by 1
        assertEquals(4, app.items[0].sellIn);

        // quality decreases by 1
        assertEquals(6, app.items[0].quality);
    }

    @Test
    void normalItem_qualityDegradesTwiceAsFastAfterSellDate() {
        // WHAT: Tests behavior after the sell-by date passes
        // WHY: Normal items degrade twice as fast when expired

        Item[] items = new Item[] {
            new Item("Elixir of the Mongoose", 0, 6)
        };
        GildedRose app = new GildedRose(items);

        app.updateQuality();

        assertEquals(-1, app.items[0].sellIn);
        assertEquals(4, app.items[0].quality);
    }

    @Test
    void qualityNeverNegative() {
        // WHAT: Ensures quality never drops below zero
        // WHY: Global business rule for all items

        Item[] items = new Item[] {
            new Item("Elixir of the Mongoose", 0, 0)
        };
        GildedRose app = new GildedRose(items);

        app.updateQuality();

        assertEquals(0, app.items[0].quality);
    }

    /* AGED BRIE */

    @Test
    void agedBrie_increasesInQualityOverTime() {
        // WHAT: Tests that Aged Brie improves with age
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
        // WHAT: Tests Aged Brie after sell-by date
        // WHY: Aged Brie improves faster once expired

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
        // WHY: Global upper-bound constraint

        Item[] items = new Item[] {
            new Item("Aged Brie", 0, 50)
        };
        GildedRose app = new GildedRose(items);

        app.updateQuality();

        assertEquals(50, app.items[0].quality);
    }

    /* SULFURAS */

    @Test
    void sulfuras_neverChangesSellInOrQuality() {
        // WHAT: Tests legendary item behavior
        // WHY: Sulfuras is immutable and never degrades

        Item[] items = new Item[] {
            new Item("Sulfuras, Hand of Ragnaros", 10, 80)
        };
        GildedRose app = new GildedRose(items);

        app.updateQuality();

        assertEquals(10, app.items[0].sellIn);
        assertEquals(80, app.items[0].quality);
    }

    /* BACKSTAGE PASSES */

    @Test
    void backstagePasses_increaseByOneWhenMoreThanTenDaysLeft() {
        // WHAT: Early concert period behavior
        // WHY: Passes increase slowly at first

        Item[] items = new Item[] {
            new Item("Backstage passes to a TAFKAL80ETC concert", 15, 20)
        };
        GildedRose app = new GildedRose(items);

        app.updateQuality();

        assertEquals(21, app.items[0].quality);
    }

    @Test
    void backstagePasses_increaseByTwoWhenTenDaysOrLess() {
        // WHAT: Mid concert window behavior
        // WHY: Value increases faster as event approaches

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
        // WHAT: Final concert window behavior
        // WHY: Highest demand just before the event

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
        // WHAT: Post-concert behavior
        // WHY: Passes are worthless after the event

        Item[] items = new Item[] {
            new Item("Backstage passes to a TAFKAL80ETC concert", 0, 20)
        };
        GildedRose app = new GildedRose(items);

        app.updateQuality();

        assertEquals(0, app.items[0].quality);
    }
}
