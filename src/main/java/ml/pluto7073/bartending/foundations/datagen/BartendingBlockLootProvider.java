package ml.pluto7073.bartending.foundations.datagen;

import ml.pluto7073.bartending.content.block.BartendingBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.world.level.block.Block;

public class BartendingBlockLootProvider extends FabricBlockLootTableProvider {

    public BartendingBlockLootProvider(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generate() {
        BartendingBlocks.DROPS_SELF_LIST.forEach(this::dropSelf);
    }
}
