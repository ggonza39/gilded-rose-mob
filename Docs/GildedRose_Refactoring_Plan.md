# Gilded Rose Refactoring Plan

## Establish a Safety Net Before Refactoring

### Goal
Ensure that we can refactor the production code safely without changing existing behavior.

### What We Did
- Replaced the placeholder test with a meaningful unit test suite in `GildedRoseTest.java`
- Added focused tests for:
    - Normal items
    - Aged Brie
    - Sulfuras
    - Backstage passes
- Verified that **all tests pass** before refactoring

### Why This Matters
- The existing `updateQuality()` method is complex and fragile
- Refactoring without tests risks breaking business rules
- These tests act as a **behavioral contract** for refactoring

---

## Identified Code Smells in the Production Code

We reviewed `GildedRose.java` together and identified the following issues:

### Major Code Smells
- **Long method**: `updateQuality()` is doing too much
- **Deeply nested conditionals**: Hard to read and reason about
- **String comparisons everywhere**: Item type logic is scattered
- **Magic strings**: Item names are repeated inline
- **Duplicated logic**:
    - Quality bounds checks
    - Sell-in decrement logic
- **Poor readability**: Business rules are buried inside conditionals

### Why We Are Not Fixing Them Yet
- Changing structure before understanding behavior risks regressions
- We first agree on *what* to refactor before touching code

---

## Agree on a Refactoring Strategy (Mob Alignment)

Before writing any code, the mob agreed on the following refactoring plan:

### Refactoring Principles
- **No behavior changes** (tests must remain green)
- **Small, incremental steps**
- **Improve readability first**, not performance

### Planned Refactoring Steps
1. Introduce helper methods to reduce nesting
2. Replace repeated string checks with named helper methods
3. Make business rules readable at the top level
4. Keep `Item` unchanged for now

---

## Mob Programming Notes

- All changes will be discussed before coding
- Tests run after every small change

---




