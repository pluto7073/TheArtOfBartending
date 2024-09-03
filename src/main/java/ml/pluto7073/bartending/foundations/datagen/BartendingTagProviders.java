package ml.pluto7073.bartending.foundations.datagen;

import ml.pluto7073.bartending.content.fluid.BartendingFluids;
import ml.pluto7073.bartending.foundations.fluid.FluidHolder;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.FluidTags;

import java.util.concurrent.CompletableFuture;

public class BartendingTagProviders {

    public static class FluidTagProvider extends FabricTagProvider.FluidTagProvider {

        public FluidTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
            super(output, completableFuture);
        }

        @Override
        protected void addTags(HolderLookup.Provider arg) {
            FabricTagBuilder builder = getOrCreateTagBuilder(FluidTags.WATER);
            for (FluidHolder fluid : BartendingFluids.FLUIDS) {
                builder.add(fluid.still()).add(fluid.flowing());
            }
        }

    }

}
