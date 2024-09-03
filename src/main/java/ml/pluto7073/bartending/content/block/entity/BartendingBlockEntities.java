package ml.pluto7073.bartending.content.block.entity;

import ml.pluto7073.bartending.TheArtOfBartending;
import ml.pluto7073.bartending.content.block.FermentingBarrelBlock;
import ml.pluto7073.bartending.content.block.BartendingBlocks;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

@SuppressWarnings("UnstableApiUsage")
public class BartendingBlockEntities {

    public static final BlockEntityType<BoilerBlockEntity> BOILER_BLOCK_ENTITY_TYPE =
            create("boiler", BlockEntityType.Builder.of(BoilerBlockEntity::new, BartendingBlocks.BOILER));

    public static final BlockEntityType<FermentingBarrelBlockEntity> FERMENTING_BARREL_BLOCK_ENTITY_TYPE =
            create("fermenting_barrel", BlockEntityType.Builder.of(
                    (pos, state) -> {
                        if (!(state.getBlock() instanceof FermentingBarrelBlock block)) throw new IllegalStateException();
                        return new FermentingBarrelBlockEntity(block.woodType, pos, state);
                    },
                    BartendingBlocks.BARRELS.values().toArray(new FermentingBarrelBlock[0])));

    public static final BlockEntityType<BottlerBlockEntity> BOTTLER_BLOCK_ENTITY_TYPE =
            create("bottler", BlockEntityType.Builder.of(BottlerBlockEntity::new, BartendingBlocks.BOTTLER));

    public static final BlockEntityType<DistilleryBlockEntity> DISTILLERY_BLOCK_ENTITY_TYPE =
            create("distillery", BlockEntityType.Builder.of(DistilleryBlockEntity::new, BartendingBlocks.DISTILLERY));

    private static <T extends BlockEntity> BlockEntityType<T> create(String id, BlockEntityType.Builder<T> builder) {
        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, TheArtOfBartending.asId(id), builder.build(null));
    }

    public static void init() {
        FluidStorage.SIDED.registerForBlockEntity((entity, dir) -> entity.exposed, BOILER_BLOCK_ENTITY_TYPE);
    }

}
