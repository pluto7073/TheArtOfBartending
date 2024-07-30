package ml.pluto7073.bartending.content.block.entity;

import ml.pluto7073.bartending.TheArtOfBartending;
import ml.pluto7073.bartending.content.block.TAOBBlocks;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class TAOBBlockEntities {

    public static final BlockEntityType<BoilerBlockEntity> BOILER_BLOCK_ENTITY_TYPE =
            create("boiler", BlockEntityType.Builder.of(BoilerBlockEntity::new, TAOBBlocks.BOILER));

    private static <T extends BlockEntity> BlockEntityType<T> create(String id, BlockEntityType.Builder<T> builder) {
        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, TheArtOfBartending.asId(id), builder.build(null));
    }

    public static void init() {}

}
