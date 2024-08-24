package ml.pluto7073.bartending.content.block;

import ml.pluto7073.bartending.TheArtOfBartending;
import net.minecraft.Util;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
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

    private static void register(String id, Block block) {
        Registry.register(BuiltInRegistries.BLOCK, TheArtOfBartending.asId(id), block);
    }

    public static void init() {
        register("boiler", BOILER);
        register("bottler", BOTTLER);
        register("distillery", DISTILLERY);

        BARRELS.forEach((type, block) -> register(type.name() + "_fermenting_barrel", block));
    }

}
