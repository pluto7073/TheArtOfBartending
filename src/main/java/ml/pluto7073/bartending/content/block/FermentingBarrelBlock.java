package ml.pluto7073.bartending.content.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import org.jetbrains.annotations.Nullable;

public class FermentingBarrelBlock extends BaseEntityBlock {

    public final WoodType woodType;

    protected FermentingBarrelBlock(WoodType type, Properties properties) {
        super(properties);
        this.woodType = type;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return null;
    }

}
