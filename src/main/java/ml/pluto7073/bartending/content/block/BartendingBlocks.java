package ml.pluto7073.bartending.content.block;

import ml.pluto7073.bartending.TheArtOfBartending;
import ml.pluto7073.bartending.content.fluid.BartendingFluids;
import net.minecraft.Util;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.PushReaction;

import java.util.HashMap;

public class BartendingBlocks {

    public static final Block BOILER = new BoilerBlock(BlockBehaviour.Properties.copy(Blocks.CAULDRON).pushReaction(PushReaction.BLOCK));
    public static final Block BOTTLER = new BottlerBlock(BlockBehaviour.Properties.copy(Blocks.BREWING_STAND));
    public static final Block DISTILLERY = new DistilleryBlock(BlockBehaviour.Properties.copy(Blocks.BREWING_STAND));

    public static final HashMap<WoodType, FermentingBarrelBlock> BARRELS = Util.make(() -> {
        HashMap<WoodType, FermentingBarrelBlock> blocks = new HashMap<>();
        for (WoodType type : WoodType.values().toList()) {
            Block planks = BuiltInRegistries.BLOCK.get(new ResourceLocation(type.name() + "_planks"));
            blocks.put(type, new FermentingBarrelBlock(type, BlockBehaviour.Properties.copy(Blocks.BARREL).mapColor(planks.defaultMapColor())));
        }
        return blocks;
    });

    public static final Block BEER = fluid(BartendingFluids.BEER);
    public static final Block RED_WINE = fluid(BartendingFluids.RED_WINE);
    public static final Block WHITE_WINE = fluid(BartendingFluids.WHITE_WINE);
    public static final Block APPLE_LIQUEUR = fluid(BartendingFluids.APPLE_LIQUEUR);
    public static final Block VODKA = fluid(BartendingFluids.VODKA);
    public static final Block RUM = fluid(BartendingFluids.RUM);

    private static void register(String id, Block block) {
        Registry.register(BuiltInRegistries.BLOCK, TheArtOfBartending.asId(id), block);
    }

    private static Block fluid(BartendingFluids.FluidHolder holder) {
        return new LiquidBlock(holder.still(), BlockBehaviour.Properties.copy(Blocks.WATER));
    }

    public static void init() {
        register("boiler", BOILER);
        register("bottler", BOTTLER);
        register("distillery", DISTILLERY);

        register("beer", BEER);
        register("red_wine", RED_WINE);
        register("white_wine", WHITE_WINE);
        register("apple_liqueur", APPLE_LIQUEUR);
        register("vodka", VODKA);
        register("rum", RUM);

        BARRELS.forEach((type, block) -> register(type.name() + "_fermenting_barrel", block));
    }

}
