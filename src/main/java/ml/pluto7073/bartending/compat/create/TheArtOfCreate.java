package ml.pluto7073.bartending.compat.create;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class TheArtOfCreate {

    public static int getHeatFromBlazeBurner(BlockState state) {
        if (!state.is(AllBlocks.BLAZE_BURNER.get())) return -1;
        return switch (state.getValue(BlazeBurnerBlock.HEAT_LEVEL)) {
            case FADING -> 1;
            case KINDLED -> 2;
            case SEETHING -> 3;
            default -> 0;
        };
    }

}
