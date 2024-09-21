package ml.pluto7073.bartending.foundations.datagen;

import ml.pluto7073.bartending.content.block.BartendingBlocks;
import ml.pluto7073.bartending.content.fluid.BartendingFluids;
import ml.pluto7073.bartending.foundations.fluid.FluidHolder;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;

import java.util.concurrent.CompletableFuture;

public class BartendingTagProviders {

    public static class FluidTagProvider extends FabricTagProvider.FluidTagProvider {

        public FluidTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
            super(output, completableFuture);
        }

        @Override
        protected void addTags(HolderLookup.Provider arg) {
            FabricTagBuilder water = getOrCreateTagBuilder(FluidTags.WATER);
            for (FluidHolder fluid : BartendingFluids.FLUIDS) {
                water.add(fluid.still()).add(fluid.flowing());
            }
        }

    }

    public static class BlockTagProvider extends FabricTagProvider.BlockTagProvider {

        public BlockTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
            super(output, registriesFuture);
        }

        @Override
        protected void addTags(HolderLookup.Provider arg) {
            FabricTagBuilder pickaxe = getOrCreateTagBuilder(BlockTags.MINEABLE_WITH_PICKAXE);
            pickaxe.add(BartendingBlocks.BOILER, BartendingBlocks.BOTTLER, BartendingBlocks.DISTILLERY);

            FabricTagBuilder axe = getOrCreateTagBuilder(BlockTags.MINEABLE_WITH_AXE);
            BartendingBlocks.BARRELS.values().forEach(axe::add);
        }

    }

}
