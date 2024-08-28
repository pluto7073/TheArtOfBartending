package ml.pluto7073.bartending.compat.create;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class TheArtOfCreate {

    public static boolean isBlockSuperheatedBlazeBurner(BlockState state) {
        return state.is(AllBlocks.BLAZE_BURNER.get()) && state.getValue(BlazeBurnerBlock.HEAT_LEVEL) == BlazeBurnerBlock.HeatLevel.SEETHING;
    }

}
