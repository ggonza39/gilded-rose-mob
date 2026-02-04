package com.gildedrose;
// Test class for validating GildedRose behavior.
// Starter test is intentionally incorrect and will be replaced.

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GildedRoseTest {

    @Test
    void foo() {
        // Placeholder test from the kata.
        // Confirms the test framework is working.

        Item[] items = new Item[] { new Item("foo", 0, 0) };
        // Dummy item with no special rules

        GildedRose app = new GildedRose(items);
        app.updateQuality();

        // Intentionally failing assertion — to be replaced
        assertEquals("fixme", app.items[0].name);
    }
}

