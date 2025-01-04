
# Changes
- Temporarily removed the Sipping feature, will be re-added later

# Fixes
- Appletini recipe once again requires sugar and an apple

---

## Fixes
- Dark beer's glass is now the Glass Bottle, instead of Glass of Beer
- Fixed Workstation recipes for Apple and Cocoa Bean additions

---

## Additions
- Champagne!
  - Made by fermenting a bottle of White Wine for at least a day, then placing it in the bottler
- Various New Cocktails:
  - Mimosa (Requires Fruitful Fun): Add an Orange to a class of Champagne
  - Death in the Afternoon: Add a shot of Absinthe to a glass of Champagne
  - Old Fashioned: Sugar + Whiskey Shot + Sweet Berries for Garnish
  - Kamikaze (Requires Fruitful Fun): Vodka + Orange Liqueur + Lime
  - Manhattan: 2 Shots of Whiskey + Sweet Vermouth
  - Vodka Martini: 3 shots of Vodka + Dry Vermouth

## Changes
- Updated structure of drinks/additions for new PDAPI syntax (0.3.3+)
- ABV of Orange Liqueur has been raised to 40% (from 25%)

## Fixes
- Specialty Drinks based off of Mixed Drinks now give the player an empty cocktail glass when drank

---

## Additions
- A new chemical, Absorbed Alcohol, this chemical is now what determines effects

## Changes
- Removed the buckets and distinct fluids for each alcohol type, moving them to a single virtual fluid with NBT data
- Alcohol is no longer absorbed immediately when drinking, and now it takes some time for the player to feel the effects

## Fixes
- Coffee Liqueur once again has a model and recipes

---

## Changes
- Updated to PDAPI 0.3.x, had to shuffle some things

## Fixes
- Alcoholic drinks based on Dark Beer or Apple Mead now correctly show the amount of alcohol

---

## Additions
- `/drink alcohol set ...` command to set the BAC of a player

## Changes
- Blackouts are now controlled by the `doBlackout` gamerule, defaults to true

---