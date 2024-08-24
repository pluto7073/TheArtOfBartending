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
            TAOBBlocks.BARRELS.values().toArray(new FermentingBarrelBlock[0])
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
