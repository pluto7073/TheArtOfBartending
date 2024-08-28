package ml.pluto7073.bartending.foundations.datagen;

import ml.pluto7073.bartending.compat.create.data.EmptyingRecipeGenerator;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.loader.api.FabricLoader;

public class TheArtOfDatagen implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator gen) {
        FabricDataGenerator.Pack pack = gen.createPack();

        pack.addProvider(BartendingRecipeGenerator::new);
        if (FabricLoader.getInstance().isModLoaded("create")) pack.addProvider(EmptyingRecipeGenerator::new);
    }

}
