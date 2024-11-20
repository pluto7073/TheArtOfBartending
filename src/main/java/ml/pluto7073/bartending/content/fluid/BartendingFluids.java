package ml.pluto7073.bartending.content.fluid;

import ml.pluto7073.bartending.foundations.fluid.AlcoholFluid;
import ml.pluto7073.bartending.foundations.fluid.FluidHolder;
import net.fabricmc.loader.api.FabricLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class BartendingFluids {

    public static final List<FluidHolder> FLUIDS = new ArrayList<>();

    public static final FluidHolder
            BEER = AlcoholFluid.create(),
            WHEAT_BEER = AlcoholFluid.create(),
            DARK_BEER = AlcoholFluid.create(),
            MEAD = AlcoholFluid.create(),
            APPLE_MEAD = AlcoholFluid.create(),
            RED_WINE = AlcoholFluid.create(),
            WHITE_WINE = AlcoholFluid.create(),
            CRIMSON_WINE = AlcoholFluid.create(),
            WARPED_WINE = AlcoholFluid.create(),
            APPLE_LIQUEUR = AlcoholFluid.create(),
            VODKA = AlcoholFluid.create(),
            RUM = AlcoholFluid.create(),
            COFFEE_LIQUEUR = AlcoholFluid.create(),
            GIN = AlcoholFluid.create(),
            TEQUILA = AlcoholFluid.create(),
            DRY_VERMOUTH = AlcoholFluid.create(),
            SWEET_VERMOUTH = AlcoholFluid.create(),
            ORANGE_LIQUEUR = AlcoholFluid.create(),
            ABSINTHE = AlcoholFluid.create(),
            WHISKEY = AlcoholFluid.create();

    public static void init() {
        BEER.register("beer");
        WHEAT_BEER.register("wheat_beer");
        DARK_BEER.register("dark_beer");
        MEAD.register("mead");
        APPLE_MEAD.register("apple_mead");
        RED_WINE.register("red_wine");
        WHITE_WINE.register("white_wine");
        CRIMSON_WINE.register("crimson_wine");
        WARPED_WINE.register("warped_wine");
        APPLE_LIQUEUR.register("apple_liqueur");
        VODKA.register("vodka");
        RUM.register("rum");
        GIN.register("gin");
        TEQUILA.register("tequila");
        DRY_VERMOUTH.register("dry_vermouth");
        SWEET_VERMOUTH.register("sweet_vermouth");
        COFFEE_LIQUEUR.register("coffee_liqueur");
        ORANGE_LIQUEUR.register("orange_liqueur");
        ABSINTHE.register("absinthe");
        WHISKEY.register("whiskey");
    }

}
