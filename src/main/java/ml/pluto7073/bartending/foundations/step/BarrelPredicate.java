package ml.pluto7073.bartending.foundations.step;

import com.google.common.collect.Lists;
import ml.pluto7073.bartending.content.block.FermentingBarrelBlock;
import ml.pluto7073.bartending.content.block.TAOBBlocks;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class BarrelPredicate implements Predicate<Block> {

    public static final BarrelPredicate ANY = new BarrelPredicate(
            TAOBBlocks.OAK_FERMENTING_BARREL,
            TAOBBlocks.SPRUCE_FERMENTING_BARREL,
            TAOBBlocks.BIRCH_FERMENTING_BARREL,
            TAOBBlocks.JUNGLE_FERMENTING_BARREL,
            TAOBBlocks.ACACIA_FERMENTING_BARREL,
            TAOBBlocks.DARK_OAK_FERMENTING_BARREL,
            TAOBBlocks.MANGROVE_FERMENTING_BARREL,
            TAOBBlocks.CHERRY_FERMENTING_BARREL,
            TAOBBlocks.BAMBOO_FERMENTING_BARREL,
            TAOBBlocks.CRIMSON_FERMENTING_BARREL,
            TAOBBlocks.WARPED_FERMENTING_BARREL
    );

    private final List<FermentingBarrelBlock> allowed;

    public BarrelPredicate(FermentingBarrelBlock... allowed) {
        this.allowed = Lists.newArrayList(allowed);
    }

    @Override
    public boolean test(Block block) {
        if (!(block instanceof FermentingBarrelBlock fermentingBarrelBlock)) return false;
        return allowed.contains(fermentingBarrelBlock);
    }
}
