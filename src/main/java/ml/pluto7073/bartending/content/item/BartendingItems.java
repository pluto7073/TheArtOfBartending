package ml.pluto7073.bartending.content.item;

import ml.pluto7073.bartending.TheArtOfBartending;
import ml.pluto7073.bartending.content.alcohol.AlcoholicDrinks;
import ml.pluto7073.bartending.content.block.BartendingBlocks;
import ml.pluto7073.bartending.foundations.alcohol.AlcoholicDrink;
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

    public static final HashMap<AlcoholicDrink, AlcoholicShotItem> SHOTS = new HashMap<>();
    public static final HashMap<AlcoholicDrink, PourableBottleItem> BOTTLES = new HashMap<>();

    // Items

    public static final ConcoctionItem CONCOCTION =
            new ConcoctionItem(new Item.Properties().stacksTo(1).craftRemainder(GLASS_BOTTLE));
    public static final Item BEER_BOTTLE =
            new SwordItem(GlassBottleTier.INSTANCE, 3, 0,
                    new Item.Properties().stacksTo(64));
    public static final Item WINE_BOTTLE =
            new SwordItem(GlassBottleTier.INSTANCE, 5, 0,
                    new Item.Properties().stacksTo(64));
    public static final Item LIQUOR_BOTTLE =
            new SwordItem(GlassBottleTier.INSTANCE, 6, 0,
                    new Item.Properties().stacksTo(64));

    public static final Item BEER = new AlcoholicDrinkItem(AlcoholicDrinks.BEER, BEER_BOTTLE,
            new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON));

    public static final Item GLASS_OF_RED_WINE = new AlcoholicDrinkItem(AlcoholicDrinks.RED_WINE, GLASS_BOTTLE,
            new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON));
    public static final Item RED_WINE = bottle(AlcoholicDrinks.RED_WINE, GLASS_OF_RED_WINE, WINE_BOTTLE);

    public static final Item GLASS_OF_WHITE_WINE = new AlcoholicDrinkItem(AlcoholicDrinks.WHITE_WINE, GLASS_BOTTLE,
            new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON));
    public static final Item WHITE_WINE = bottle(AlcoholicDrinks.WHITE_WINE, GLASS_OF_WHITE_WINE, WINE_BOTTLE);

    public static final Item SHOT_OF_DRY_VERMOUTH = shot(AlcoholicDrinks.DRY_VERMOUTH);
    public static final Item DRY_VERMOUTH = bottle(AlcoholicDrinks.DRY_VERMOUTH, SHOT_OF_DRY_VERMOUTH, WINE_BOTTLE);

    public static final Item SHOT_OF_SWEET_VERMOUTH = shot(AlcoholicDrinks.SWEET_VERMOUTH);
    public static final Item SWEET_VERMOUTH =
            bottle(AlcoholicDrinks.SWEET_VERMOUTH, SHOT_OF_SWEET_VERMOUTH, WINE_BOTTLE);

    public static final Item SHOT_OF_VODKA = shot(AlcoholicDrinks.VODKA);
    public static final Item VODKA = liquor(AlcoholicDrinks.VODKA, SHOT_OF_VODKA);

    public static final Item SHOT_OF_APPLE_LIQUEUR = shot(AlcoholicDrinks.APPLE_LIQUEUR);
    public static final Item APPLE_LIQUEUR = liquor(AlcoholicDrinks.APPLE_LIQUEUR, SHOT_OF_APPLE_LIQUEUR);

    public static final Item SHOT_OF_RUM = shot(AlcoholicDrinks.RUM);
    public static final Item RUM = liquor(AlcoholicDrinks.RUM, SHOT_OF_RUM);

    public static final Item SHOT_OF_COFFEE_LIQUEUR = shot(AlcoholicDrinks.COFFEE_LIQUEUR);
    public static final Item COFFEE_LIQUEUR = liquor(AlcoholicDrinks.COFFEE_LIQUEUR, SHOT_OF_COFFEE_LIQUEUR);

    public static final Item SHOT_OF_GIN = shot(AlcoholicDrinks.GIN);
    public static final Item GIN = liquor(AlcoholicDrinks.GIN, SHOT_OF_GIN);

    public static final Item SHOT_OF_TEQUILA = shot(AlcoholicDrinks.TEQUILA);
    public static final Item TEQUILA = liquor(AlcoholicDrinks.TEQUILA, SHOT_OF_TEQUILA);

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

    private static PourableBottleItem liquor(AlcoholicDrink drink, Item serving) {
        return bottle(drink, serving, LIQUOR_BOTTLE);
    }

    private static PourableBottleItem bottle(AlcoholicDrink drink, Item serving, Item bottle) {
        int ouncesTotal = 12;
        if (bottle == LIQUOR_BOTTLE) ouncesTotal = 30;
        else if (bottle == WINE_BOTTLE)  ouncesTotal = 25;
        int servings = (int) Math.floor(ouncesTotal / drink.standardOunces());
        Item.Properties properties = new Item.Properties()
                .defaultDurability(servings)
                .rarity(Rarity.UNCOMMON);
        PourableBottleItem item = new PourableBottleItem(serving, bottle, drink, properties);
        registerFinalDrink(drink, item);
        BOTTLES.put(drink, item);
        return item;
    }

    private static AlcoholicShotItem shot(AlcoholicDrink drink) {
        AlcoholicShotItem shot = new AlcoholicShotItem(
                drink, new Item.Properties()
                .rarity(Rarity.UNCOMMON)
                .craftRemainder(GLASS_BOTTLE)
                .stacksTo(16)
        );
        SHOTS.put(drink, shot);
        return shot;
    }

    private static void register(String id, Item item) {
        Registry.register(BuiltInRegistries.ITEM, TheArtOfBartending.asId(id), item);
    }

    public static void init() {
        BOTTLES.forEach((alc, bottle) -> {
            String path = AlcoholicDrinks.getId(alc).getPath();
            register(path, bottle);
        });
        SHOTS.forEach((alc, shot) -> {
            String path = AlcoholicDrinks.getId(alc).withPrefix("shot_of_").getPath();
            register(path, shot);
        });
        register("concoction", CONCOCTION);
        register("wine_bottle", WINE_BOTTLE);
        register("beer_bottle", BEER_BOTTLE);
        register("liquor_bottle", LIQUOR_BOTTLE);
        register("mixed_drink", MIXED_DRINK);
        register("beer", BEER);
        register("glass_of_red_wine", GLASS_OF_RED_WINE);
        register("glass_of_white_wine", GLASS_OF_WHITE_WINE);

        register("boiler", BOILER);
        register("bottler", BOTTLER);
        register("distillery", DISTILLERY);
        FERMENTING_BARRELS.forEach((type, item) -> register(type.name() + "_fermenting_barrel", item));

        InProgressItemRegistry.register(GLASS_BOTTLE, MIXED_DRINK);

        registerFinalDrink(AlcoholicDrinks.BEER, BEER);
    }

}
