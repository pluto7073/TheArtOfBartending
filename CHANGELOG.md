# Fixes
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