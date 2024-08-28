package ml.pluto7073.bartending.content.item;

import ml.pluto7073.bartending.TheArtOfBartending;
import ml.pluto7073.bartending.content.alcohol.AlcoholicDrinks;
import ml.pluto7073.bartending.content.block.BartendingBlocks;
import ml.pluto7073.bartending.content.fluid.BartendingFluids;
import ml.pluto7073.bartending.foundations.item.AlcoholicShotItem;
import ml.pluto7073.bartending.foundations.item.tier.GlassBottleTier;
import ml.pluto7073.bartending.foundations.item.AlcoholicDrinkItem;
import ml.pluto7073.bartending.foundations.item.PourableBottleItem;
import ml.pluto7073.pdapi.specialty.InProgressItemRegistry;
import net.minecraft.Util;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.state.properties.WoodType;

import java.util.HashMap;

import static ml.pluto7073.bartending.content.alcohol.AlcoholicDrinks.registerFinalDrink;
import static net.minecraft.world.item.Items.*;

public class BartendingItems {

    // Items

    public static final ConcoctionItem CONCOCTION = new ConcoctionItem(new Item.Properties().stacksTo(1).craftRemainder(GLASS_BOTTLE));
    public static final Item BEER_BOTTLE =
            new SwordItem(GlassBottleTier.INSTANCE, 3, 0, new Item.Properties().stacksTo(64));
    public static final Item WINE_BOTTLE =
            new SwordItem(GlassBottleTier.INSTANCE, 5, 0, new Item.Properties().stacksTo(64));
    public static final Item LIQUOR_BOTTLE =
            new SwordItem(GlassBottleTier.INSTANCE, 6, 0, new Item.Properties().stacksTo(64));

    public static final Item BEER = new AlcoholicDrinkItem(AlcoholicDrinks.BEER, BEER_BOTTLE,
            new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON));
    public static final Item BEER_BUCKET = bucket(BartendingFluids.BEER);

    public static final Item GLASS_OF_RED_WINE = new AlcoholicDrinkItem(AlcoholicDrinks.RED_WINE, GLASS_BOTTLE,
            new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON));
    public static final Item RED_WINE = new PourableBottleItem(GLASS_OF_RED_WINE, WINE_BOTTLE, AlcoholicDrinks.RED_WINE,
            new Item.Properties().defaultDurability(5).rarity(Rarity.UNCOMMON));
    public static final Item RED_WINE_BUCKET = bucket(BartendingFluids.RED_WINE);

    public static final Item GLASS_OF_WHITE_WINE = new AlcoholicDrinkItem(AlcoholicDrinks.WHITE_WINE, GLASS_BOTTLE,
            new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON));
    public static final Item WHITE_WINE = new PourableBottleItem(GLASS_OF_WHITE_WINE, WINE_BOTTLE, AlcoholicDrinks.WHITE_WINE,
            new Item.Properties().defaultDurability(5).rarity(Rarity.UNCOMMON));
    public static final Item WHITE_WINE_BUCKET = bucket(BartendingFluids.WHITE_WINE);

    public static final Item SHOT_OF_VODKA = new AlcoholicShotItem(AlcoholicDrinks.VODKA,
            new Item.Properties().stacksTo(16).rarity(Rarity.UNCOMMON).craftRemainder(GLASS_BOTTLE));
    public static final Item VODKA = new PourableBottleItem(SHOT_OF_VODKA, LIQUOR_BOTTLE, AlcoholicDrinks.VODKA,
            new Item.Properties().defaultDurability(20).rarity(Rarity.UNCOMMON));
    public static final Item VODKA_BUCKET = bucket(BartendingFluids.VODKA);

    public static final Item SHOT_OF_APPLE_LIQUEUR = new AlcoholicShotItem(AlcoholicDrinks.APPLE_LIQUEUR,
            new Item.Properties().stacksTo(16).rarity(Rarity.UNCOMMON).craftRemainder(GLASS_BOTTLE));
    public static final Item APPLE_LIQUEUR = new PourableBottleItem(SHOT_OF_APPLE_LIQUEUR, LIQUOR_BOTTLE, AlcoholicDrinks.APPLE_LIQUEUR,
            new Item.Properties().defaultDurability(20).rarity(Rarity.UNCOMMON));
    public static final Item APPLE_LIQUEUR_BUCKET = bucket(BartendingFluids.APPLE_LIQUEUR);

    public static final Item SHOT_OF_RUM = new AlcoholicShotItem(AlcoholicDrinks.RUM,
            new Item.Properties().stacksTo(16).rarity(Rarity.UNCOMMON).craftRemainder(GLASS_BOTTLE));
    public static final Item RUM = new PourableBottleItem(SHOT_OF_RUM, LIQUOR_BOTTLE, AlcoholicDrinks.RUM,
            new Item.Properties().defaultDurability(20).rarity(Rarity.UNCOMMON));
    public static final Item RUM_BUCKET = bucket(BartendingFluids.RUM);

    public static final Item SHOT_OF_COFFEE_LIQUEUR = new AlcoholicShotItem(AlcoholicDrinks.RUM,
            new Item.Properties().stacksTo(16).rarity(Rarity.UNCOMMON).craftRemainder(GLASS_BOTTLE));
    public static final Item COFFEE_LIQUEUR = new PourableBottleItem(SHOT_OF_COFFEE_LIQUEUR, LIQUOR_BOTTLE, AlcoholicDrinks.RUM,
            new Item.Properties().defaultDurability(20).rarity(Rarity.UNCOMMON));

    public static final Item MIXED_DRINK = new MixedDrinkItem(new Item.Properties().stacksTo(1));

    // Block Items

    public static final Item BOILER = new BlockItem(BartendingBlocks.BOILER, new Item.Properties());
    public static final Item BOTTLER = new BlockItem(BartendingBlocks.BOTTLER, new Item.Properties());
    public static final Item DISTILLERY = new BlockItem(BartendingBlocks.DISTILLERY, new Item.Properties());

    public static final HashMap<WoodType, BlockItem> FERMENTING_BARRELS = Util.make(() -> {
        HashMap<WoodType, BlockItem> map = new HashMap<>();
        BartendingBlocks.BARRELS.forEach((type, block) -> map.put(type, new BlockItem(block, new Item.Properties())));
        return map;
    });

    private static void register(String id, Item item) {
        Registry.register(BuiltInRegistries.ITEM, TheArtOfBartending.asId(id), item);
    }

    private static Item bucket(BartendingFluids.FluidHolder holder) {
        return new BucketItem(holder.still(), new Item.Properties().stacksTo(1).craftRemainder(BUCKET));
    }

    public static void init() {
        register("concoction", CONCOCTION);
        register("wine_bottle", WINE_BOTTLE);
        register("beer_bottle", BEER_BOTTLE);
        register("liquor_bottle", LIQUOR_BOTTLE);
        register("mixed_drink", MIXED_DRINK);
        register("beer", BEER);
        register("beer_bucket", BEER_BUCKET);
        register("red_wine", RED_WINE);
        register("glass_of_red_wine", GLASS_OF_RED_WINE);
        register("red_wine_bucket", RED_WINE_BUCKET);
        register("white_wine", WHITE_WINE);
        register("glass_of_white_wine", GLASS_OF_WHITE_WINE);
        register("white_wine_bucket", WHITE_WINE_BUCKET);
        register("shot_of_apple_liqueur", SHOT_OF_APPLE_LIQUEUR);
        register("apple_liqueur", APPLE_LIQUEUR);
        register("apple_liqueur_bucket", APPLE_LIQUEUR_BUCKET);
        register("shot_of_vodka", SHOT_OF_VODKA);
        register("vodka", VODKA);
        register("vodka_bucket", VODKA_BUCKET);
        register("shot_of_rum", SHOT_OF_RUM);
        register("rum", RUM);
        register("rum_bucket", RUM_BUCKET);
        register("shot_of_coffee_liqueur", SHOT_OF_COFFEE_LIQUEUR);
        register("coffee_liqueur", COFFEE_LIQUEUR);

        register("boiler", BOILER);
        register("bottler", BOTTLER);
        register("distillery", DISTILLERY);
        FERMENTING_BARRELS.forEach((type, item) -> register(type.name() + "_fermenting_barrel", item));

        InProgressItemRegistry.register(GLASS_BOTTLE, MIXED_DRINK);

        registerFinalDrink(AlcoholicDrinks.BEER, BEER);
        registerFinalDrink(AlcoholicDrinks.RED_WINE, RED_WINE);
        registerFinalDrink(AlcoholicDrinks.WHITE_WINE, WHITE_WINE);
        registerFinalDrink(AlcoholicDrinks.VODKA, VODKA);
        registerFinalDrink(AlcoholicDrinks.APPLE_LIQUEUR, APPLE_LIQUEUR);
        registerFinalDrink(AlcoholicDrinks.RUM, RUM);
    }

}
