package ml.pluto7073.bartending.content.alcohol;

import ml.pluto7073.bartending.TheArtOfBartending;
import ml.pluto7073.bartending.content.block.BartendingBlocks;
import ml.pluto7073.bartending.content.fluid.BartendingFluids;
import ml.pluto7073.bartending.content.item.BartendingItems;
import ml.pluto7073.bartending.foundations.alcohol.AlcoholicDrink;
import ml.pluto7073.bartending.foundations.step.*;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.state.properties.WoodType;

import java.util.*;

@MethodsReturnNonnullByDefault
public final class AlcoholicDrinks {

    private static final Map<ResourceLocation, AlcoholicDrink> REGISTRY = new HashMap<>();
    private static final HashMap<AlcoholicDrink, Item> DRINK_TO_ITEM = new HashMap<>();

    public static final AlcoholicDrink BEER = register("beer", new AlcoholicDrink.Builder().proof(10).ounces(12)
            .addStep(new BoilingBrewerStep.Builder().addIngredient(Ingredient.of(Items.WHEAT), 5, 1)
                    .setTicks(9600).setLeeway(600).build())
            .addStep(new FermentingBrewerStep(BarrelPredicate.ANY, 84000, 24000))
            .bottle(BartendingItems.BEER_BOTTLE).fluid(BartendingFluids.BEER).name("Beer")
            .color(9402184).build());
    public static final AlcoholicDrink RED_WINE = register("red_wine", new AlcoholicDrink.Builder().proof(24).ounces(5)
            .addStep(new BoilingBrewerStep.Builder().addIngredient(Ingredient.of(Items.SWEET_BERRIES), 96, 10)
                    .setTicks(6000).setLeeway(600).build())
            .addStep(new FermentingBrewerStep(BarrelPredicate.ANY, 36000, 18000))
            .bottle(BartendingItems.WINE_BOTTLE).fluid(BartendingFluids.RED_WINE).name("Red Wine")
            .color(0x2b0010).build());
    public static final AlcoholicDrink WHITE_WINE = register("white_wine", new AlcoholicDrink.Builder().proof(24).ounces(5)
            .addStep(new BoilingBrewerStep.Builder().addIngredient(Ingredient.of(Items.GLOW_BERRIES), 96, 10)
                    .setTicks(6000).setLeeway(600).build())
            .addStep(new FermentingBrewerStep(BarrelPredicate.ANY, 36000, 18000))
            .bottle(BartendingItems.WINE_BOTTLE).fluid(BartendingFluids.WHITE_WINE).name("White Wine")
            .color(0xe2c36c).build());
    public static final AlcoholicDrink APPLE_LIQUEUR = register("apple_liqueur", new AlcoholicDrink.Builder()
            .proof(60).ounces(1.5f).addStep(new BoilingBrewerStep.Builder()
                    .addIngredient(Ingredient.of(Items.APPLE, Items.GOLDEN_APPLE, Items.ENCHANTED_GOLDEN_APPLE), 64)
                    .setTicks(24000).setLeeway(6000).build())
            .addStep(new FermentingBrewerStep(new BarrelPredicate(BartendingBlocks.BARRELS.get(WoodType.CHERRY),
                    BartendingBlocks.BARRELS.get(WoodType.ACACIA)), 156000, 24000))
            .addStep(new DistillingBrewerStep()).bottle(BartendingItems.LIQUOR_BOTTLE)
            .color(0xbc8a49).fluid(BartendingFluids.APPLE_LIQUEUR).name("Apple Liqueur").build());
    public static final AlcoholicDrink VODKA = register("vodka", new AlcoholicDrink.Builder().proof(80).ounces(1.5f)
            .addStep(new BoilingBrewerStep.Builder().addIngredient(Ingredient.of(Items.POTATO, Items.POISONOUS_POTATO), 10)
                    .setTicks(24000).build())
            .addStep(new DistillingBrewerStep()).bottle(BartendingItems.LIQUOR_BOTTLE)
            .fluid(BartendingFluids.VODKA).name("Vodka").build());
    public static final AlcoholicDrink RUM = register("rum", new AlcoholicDrink.Builder().proof(80).ounces(1.5f)
            .addStep(new BoilingBrewerStep.Builder().addIngredient(Ingredient.of(Items.SUGAR_CANE), 50)
                    .setTicks(2400).setLeeway(600).build())
            .addStep(new FermentingBrewerStep(new BarrelPredicate(BartendingBlocks.BARRELS.get(WoodType.OAK), BartendingBlocks.BARRELS.get(WoodType.DARK_OAK)),
                    204000, 24000))
            .addStep(new DistillingBrewerStep()).bottle(BartendingItems.LIQUOR_BOTTLE)
            .color(0x825424).fluid(BartendingFluids.RUM).name("Rum").build());
    public static final AlcoholicDrink COFFEE_LIQUEUR = register("coffee_liqueur", new AlcoholicDrink.Builder()
            .addStep(new AlternativeBrewerStep()).name("Coffee Liqueur")
            .setVisibleWhen(() -> FabricLoader.getInstance().isModLoaded("plutoscoffee"))
            .color(0x211304).ounces(1.5f).proof(80).fluid(BartendingFluids.COFFEE_LIQUEUR).build());
    public static final AlcoholicDrink GIN = register("gin", new AlcoholicDrink.Builder().proof(90).ounces(1.5f)
            .addStep(new BoilingBrewerStep.Builder()
                    .addIngredient(Ingredient.of(Items.BLUE_ORCHID, Items.LILY_OF_THE_VALLEY,
                            Items.CORNFLOWER, Items.ALLIUM), 10, 2)
                    .addIngredient(Ingredient.of(Items.WHEAT), 10, 3)
                    .addIngredient(Ingredient.of(Items.SWEET_BERRIES), 20)
                    .setTicks(6000).setLeeway(600).build())
            .addStep(new DistillingBrewerStep()).name("Gin")
            .fluid(BartendingFluids.GIN).bottle(BartendingItems.LIQUOR_BOTTLE).build());
    public static final AlcoholicDrink TEQUILA = register("tequila", new AlcoholicDrink.Builder().proof(80)
            .ounces(1.5f).addStep(new BoilingBrewerStep.Builder()
                    .addIngredient(Ingredient.of(Items.CACTUS), 20, 5)
                    .setTicks(18000).setLeeway(3600).build())
            .addStep(new FermentingBrewerStep(new BarrelPredicate(BartendingBlocks.BARRELS.get(WoodType.BIRCH)),
                    252000, 36000))
            .addStep(new DistillingBrewerStep()).fluid(BartendingFluids.TEQUILA)
            .color(0xEFEFEF).name("Tequila").bottle(BartendingItems.LIQUOR_BOTTLE).build());
    public static final AlcoholicDrink DRY_VERMOUTH = register("dry_vermouth", new AlcoholicDrink.Builder().proof(33)
            .ounces(1.5f).addStep(new AlternativeBrewerStep()).fluid(BartendingFluids.DRY_VERMOUTH)
            .color(0xfcf3ba).name("Dry Vermouth").bottle(BartendingItems.WINE_BOTTLE).build());
    public static final AlcoholicDrink SWEET_VERMOUTH = register("sweet_vermouth", new AlcoholicDrink.Builder()
            .proof(33).ounces(1.5f).addStep(new AlternativeBrewerStep()).fluid(BartendingFluids.SWEET_VERMOUTH)
            .color(0x51190d).name("Sweet Vermouth").bottle(BartendingItems.WINE_BOTTLE).build());

    public static AlcoholicDrink register(ResourceLocation id, AlcoholicDrink drink) {
        REGISTRY.put(id, drink);
        return drink;
    }

    private static AlcoholicDrink register(String id, AlcoholicDrink drink) {
        return register(TheArtOfBartending.asId(id), drink);
    }

    public static AlcoholicDrink get(ResourceLocation id) {
        AlcoholicDrink drink = REGISTRY.get(id);
        if (drink == null) throw new IllegalArgumentException("No registered Drink with id " + id);
        return drink;
    }

    public static ResourceLocation getId(AlcoholicDrink drink) {
        for (Map.Entry<ResourceLocation, AlcoholicDrink> entry : REGISTRY.entrySet()) {
            if (entry.getValue().equals(drink)) return entry.getKey();
        }
        throw new IllegalArgumentException("Unregistered value: " + drink);
    }

    public static Collection<AlcoholicDrink> values() {
        return REGISTRY.values();
    }

    public static void registerFinalDrink(AlcoholicDrink drink, Item item) {
        DRINK_TO_ITEM.put(drink, item);
    }

    public static Item getFinalDrink(AlcoholicDrink drink) {
        return DRINK_TO_ITEM.get(drink);
    }

}
