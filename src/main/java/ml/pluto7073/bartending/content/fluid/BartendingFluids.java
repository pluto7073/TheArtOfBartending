package ml.pluto7073.bartending.content.fluid;

import ml.pluto7073.bartending.foundations.fluid.AlcoholFluid;
import ml.pluto7073.bartending.foundations.fluid.FluidHolder;

public final class BartendingFluids {

    public static final FluidHolder
            BEER = AlcoholFluid.create(),
            RED_WINE = AlcoholFluid.create(),
            WHITE_WINE = AlcoholFluid.create(),
            APPLE_LIQUEUR = AlcoholFluid.create(),
            VODKA = AlcoholFluid.create(),
            RUM = AlcoholFluid.create(),
            COFFEE_LIQUEUR = AlcoholFluid.create(),
            GIN = AlcoholFluid.create(),
            VERMOUTH = AlcoholFluid.create();

    public static void init() {
        BEER.register("beer");
        RED_WINE.register("red_wine");
        WHITE_WINE.register("white_wine");
        APPLE_LIQUEUR.register("apple_liqueur");
        VODKA.register("vodka");
        RUM.register("rum");
        COFFEE_LIQUEUR.register("coffee_liqueur");
        GIN.register("gin");
        VERMOUTH.register("vermouth");
    }

}
