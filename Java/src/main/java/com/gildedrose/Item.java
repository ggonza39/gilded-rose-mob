package com.gildedrose;

/**
 * Item represents a single inventory item.
 *
 * This is a simple data holder class with public fields.
 * There is no validation or behavior here — all logic
 * is handled inside GildedRose.
 */
public class Item {

    // Name of the item (used heavily for string comparisons)
    public String name;

    // Number of days remaining to sell the item
    public int sellIn;

    // Quality value of the item
    public int quality;

    // Constructor initializes all fields
    public Item(String name, int sellIn, int quality) {
        this.name = name;
        this.sellIn = sellIn;
        this.quality = quality;
    }

    // Used mainly for debugging and logging
    @Override
    public String toString() {
        return this.name + ", " + this.sellIn + ", " + this.quality;
    }
}
