package ml.pluto7073.bartending.content.fluid;

import ml.pluto7073.bartending.TheArtOfBartending;
import ml.pluto7073.bartending.foundations.fluid.AlcFlowableFluid;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;

public final class BartendingFluids {

    public static final FluidHolder BEER = new FluidHolder(new BeerFluid.Still(), new BeerFluid.Flowing());
    public static final FluidHolder RED_WINE = new FluidHolder(new RedWineFluid.Still(), new RedWineFluid.Flowing());
    public static final FluidHolder WHITE_WINE = new FluidHolder(new WhiteFineFluid.Still(), new WhiteFineFluid.Flowing());
    public static final FluidHolder APPLE_LIQUEUR = new FluidHolder(new AppleLiqueurFluid.Still(), new AppleLiqueurFluid.Flowing());
    public static final FluidHolder VODKA = new FluidHolder(new VodkaFluid.Still(), new VodkaFluid.Flowing());
    public static final FluidHolder RUM = new FluidHolder(new RumFluid.Still(), new RumFluid.Flowing());

    private static void register(String id, FluidHolder fluid) {
        Registry.register(BuiltInRegistries.FLUID, TheArtOfBartending.asId(id), fluid.still());
        Registry.register(BuiltInRegistries.FLUID, TheArtOfBartending.asId("flowing_" + id), fluid.flowing());
    }

    public static void init() {
        register("beer", BEER);
        register("red_wine", RED_WINE);
        register("white_wine", WHITE_WINE);
        register("apple_liqueur", APPLE_LIQUEUR);
        register("vodka", VODKA);
        register("rum", RUM);
    }

    public record FluidHolder(AlcFlowableFluid still, AlcFlowableFluid flowing) { }

}
