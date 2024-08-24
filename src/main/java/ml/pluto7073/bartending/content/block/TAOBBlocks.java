package ml.pluto7073.bartending.content.block;

import ml.pluto7073.bartending.TheArtOfBartending;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.Util;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.PushReaction;

import java.util.HashMap;

public class TAOBBlocks {

    public static final Block BOILER = new BoilerBlock(BlockBehaviour.Properties.copy(Blocks.CAULDRON)
            .pushReaction(PushReaction.BLOCK));

    public static final Block BOTTLER = new BottlerBlock(BlockBehaviour.Properties.copy(Blocks.BREWING_STAND));

    public static final HashMap<WoodType, FermentingBarrelBlock> BARRELS = Util.make(() -> {
        HashMap<WoodType, FermentingBarrelBlock> blocks = new HashMap<>();
        for (WoodType type : WoodType.values().toList()) {
            blocks.put(type, new FermentingBarrelBlock(type, BlockBehaviour.Properties.copy(Blocks.BARREL)));
        }
        return blocks;
    });

    private static void register(String id, Block block) {
        Registry.register(BuiltInRegistries.BLOCK, TheArtOfBartending.asId(id), block);
    }

    public static void init() {
        register("boiler", BOILER);
        register("bottler", BOTTLER);

        BARRELS.forEach((type, block) -> {
            register(type.name() + "_fermenting_barrel", block);
        });
    }

}
