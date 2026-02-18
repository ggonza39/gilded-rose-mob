package com.gildedrose;

/**
 * GildedRose is responsible for updating the sellIn and quality
 * values for all inventory items.

 * This version has been refactored to:
 * - Remove deeply nested conditionals
 * - Separate business rules into small, readable methods
 * - Preserve original behavior exactly
 */
class GildedRose {

    // Collection of items managed by the system
    Item[] items;

    /**
     * Constructor simply stores the items array.
     */
    public GildedRose(Item[] items) {
        this.items = items;
    }

    /**
     * Main update method executed once per day.

     * ORDER OF OPERATIONS (must match legacy behavior):
     * 1. Update quality based on item type (before expiration)
     * 2. Decrease sellIn (except Sulfuras)
     * 3. Apply additional rules if item has expired
     */
    public void updateQuality() {

        for (Item item : items) {

            // Step 1: Apply standard quality rules
            updateItemQuality(item);

            // Step 2: Decrement sellIn where appropriate
            updateSellIn(item);

            // Step 3: Apply expiration rules (sellIn < 0)
            handleExpiredItem(item);
        }
    }

    /**
     * Quality update rules before expiration:
     * Updates item quality according to type-specific rules
     * before the sell-by date is evaluated.

     * Rules:
     * - Aged Brie increases in quality
     * - Backstage passes increase as the concert approaches
     * - Normal items decrease in quality
     * - Sulfuras never changes
     */
    private void updateItemQuality(Item item) {

        if (isAgedBrie(item)) {
            increaseQuality(item);

        } else if (isBackstagePass(item)) {

            // Backstage passes increase in quality as sell date approaches
            increaseQuality(item);

            // Additional increase when 10 days or fewer remain
            if (item.sellIn <= 10) {
                increaseQuality(item);
            }

            // Additional increase when 5 days or fewer remain
            if (item.sellIn <= 5) {
                increaseQuality(item);
            }

        } else if (!isSulfuras(item)) {
            // Normal and Conjured items handled here
            checkConjured(item);
        }
    }

    /**
     * Sell-In Update:
     * Decrements sellIn value for all items except Sulfuras.

     * Sulfuras is legendary and does not change.
     */
    private void updateSellIn(Item item) {
        if (!isSulfuras(item)) {
            item.sellIn--;
        }
    }

    /**
     * Quality update rules after expiration:
     * Applies additional rules once sellIn < 0.

     * Rules:
     * - Normal items degrade twice as fast
     * - Aged Brie increases again
     * - Backstage passes drop to zero
     * - Sulfuras remains unchanged
     */
    private void handleExpiredItem(Item item) {

        // If item has not expired, no additional logic applies
        if (item.sellIn >= 0) {
            return;
        }

        if (isAgedBrie(item)) {
            increaseQuality(item);

        } else if (isBackstagePass(item)) {
            // After concert, quality drops to zero
            item.quality = 0;

        } else if (!isSulfuras(item)) {
            // Apply additional degradation logic after expiration
            checkConjured(item);
        }
    }

    /**
     * Handles degradation logic for normal and Conjured items.
     * - Normal items decrease quality by 1 per call.
     * - Conjured items decrease quality twice per call
     *   (representing "twice as fast" degradation).
     * This method is reused both before and after expiration.
     */
    private void checkConjured(Item item) {
        if (isConjured(item)){
            decreaseQuality(item);
            decreaseQuality(item);
        } else {
            decreaseQuality(item);
        }
    }

    /**
     * Quality boundary helpers:
     * Increases quality by 1, ensuring it never exceeds 50.
     */
    private void increaseQuality(Item item) {
        if (item.quality < 50) {
            item.quality++;
        }
    }

    /**
     * Decreases quality by 1, ensuring it never drops below 0.
     */
    private void decreaseQuality(Item item) {
        if (item.quality > 0) {
            item.quality--;
        }
    }

    /**
     * Centralized item type checks.
     * This removes repeated string literals ("magic strings")
     * from the main logic and improves readability.
     */

    private boolean isAgedBrie(Item item) {
        return item.name.equals("Aged Brie");
    }

    private boolean isBackstagePass(Item item) {
        return item.name.equals("Backstage passes to a TAFKAL80ETC concert");
    }

    private boolean isSulfuras(Item item) {
        return item.name.equals("Sulfuras, Hand of Ragnaros");
    }

    /**
     * Identifies Conjured items.
     * Conjured items degrade twice as fast as normal items.
     * Uses name matching to preserve compatibility with legacy design.
     */
    private boolean isConjured(Item item) {
        return item.name.contains("Conjured");
    }
}
