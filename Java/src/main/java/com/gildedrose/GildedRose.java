package com.gildedrose;

/**
 * GildedRose is responsible for updating the sellIn and quality
 * values for all items in the inventory.
 *
 * NOTE:
 * This implementation is intentionally complex and hard to read.
 * It relies heavily on nested conditionals and string comparisons.
 * This is the baseline code we will refactor later.
 */
class GildedRose {

    Item[] items;

    // Constructor simply stores the items array
    public GildedRose(Item[] items) {
        this.items = items;
    }

    /**
     * updateQuality()
     *
     * This method applies all business rules for each item:
     * - Normal items decrease in quality over time
     * - Aged Brie increases in quality
     * - Backstage passes increase faster as the concert approaches
     * - Sulfuras never changes
     *
     * The logic is implemented using deeply nested conditionals
     * and repeated string comparisons, which makes it difficult
     * to read, understand, and maintain.
     */
    public void updateQuality() {

        // Iterate over every item in the inventory
        for (int i = 0; i < items.length; i++) {

            // Handle items that are NOT Aged Brie or Backstage passes
            if (!items[i].name.equals("Aged Brie")
                && !items[i].name.equals("Backstage passes to a TAFKAL80ETC concert")) {

                // Quality can only decrease if it is above zero
                if (items[i].quality > 0) {

                    // Sulfuras never changes quality
                    if (!items[i].name.equals("Sulfuras, Hand of Ragnaros")) {
                        items[i].quality = items[i].quality - 1;
                    }
                }

            } else {
                // Handle Aged Brie and Backstage passes (they increase in quality)

                // Quality is capped at 50
                if (items[i].quality < 50) {
                    items[i].quality = items[i].quality + 1;

                    // Additional rules for Backstage passes
                    if (items[i].name.equals("Backstage passes to a TAFKAL80ETC concert")) {

                        // Increase quality again if 10 days or fewer remain
                        if (items[i].sellIn < 11) {
                            if (items[i].quality < 50) {
                                items[i].quality = items[i].quality + 1;
                            }
                        }

                        // Increase quality a third time if 5 days or fewer remain
                        if (items[i].sellIn < 6) {
                            if (items[i].quality < 50) {
                                items[i].quality = items[i].quality + 1;
                            }
                        }
                    }
                }
            }

            // Decrease sellIn for all items except Sulfuras
            if (!items[i].name.equals("Sulfuras, Hand of Ragnaros")) {
                items[i].sellIn = items[i].sellIn - 1;
            }

            // Additional rules once the sell-by date has passed
            if (items[i].sellIn < 0) {

                // Normal items degrade twice as fast after expiration
                if (!items[i].name.equals("Aged Brie")) {

                    if (!items[i].name.equals("Backstage passes to a TAFKAL80ETC concert")) {

                        if (items[i].quality > 0) {

                            if (!items[i].name.equals("Sulfuras, Hand of Ragnaros")) {
                                items[i].quality = items[i].quality - 1;
                            }
                        }

                    } else {
                        // Backstage passes drop to zero quality after the concert
                        items[i].quality = items[i].quality - items[i].quality;
                    }

                } else {
                    // Aged Brie increases in quality after expiration
                    if (items[i].quality < 50) {
                        items[i].quality = items[i].quality + 1;
                    }
                }
            }
        }
    }
}
