package ml.pluto7073.bartending.content.block.entity;

import ml.pluto7073.bartending.TheArtOfBartending;
import ml.pluto7073.bartending.content.block.FermentingBarrelBlock;
import ml.pluto7073.bartending.content.block.TAOBBlocks;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.properties.WoodType;

public class TAOBBlockEntities {

    public static final BlockEntityType<BoilerBlockEntity> BOILER_BLOCK_ENTITY_TYPE =
            create("boiler", BlockEntityType.Builder.of(BoilerBlockEntity::new, TAOBBlocks.BOILER));

    public static final BlockEntityType<FermentingBarrelBlockEntity> FERMENTING_BARREL_BLOCK_ENTITY_TYPE =
            create("fermenting_barrel", BlockEntityType.Builder.of(
                    (pos, state) -> {
                        if (!(state.getBlock() instanceof FermentingBarrelBlock block)) throw new IllegalStateException();
                        return new FermentingBarrelBlockEntity(block.woodType, pos, state);
                    },
                    TAOBBlocks.BARRELS.values().toArray(new FermentingBarrelBlock[0])));

    public static final BlockEntityType<BottlerBlockEntity> BOTTLER_BLOCK_ENTITY_TYPE =
            create("bottler", BlockEntityType.Builder.of(BottlerBlockEntity::new, TAOBBlocks.BOTTLER));

    private static <T extends BlockEntity> BlockEntityType<T> create(String id, BlockEntityType.Builder<T> builder) {
        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, TheArtOfBartending.asId(id), builder.build(null));
    }

    public static void init() {}

}
