package ml.pluto7073.bartending.foundations.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.data.models.ModelProvider;

public class TheArtOfDatagen implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator gen) {
        FabricDataGenerator.Pack pack = gen.createPack();
        pack.addProvider(BartendingTagProviders.FluidTagProvider::new);
        pack.addProvider(BartendingModelsProvider::new);
        pack.addProvider(BartendingEnglishProvider::new);
        pack.addProvider(BartendingRecipeProviders::new);
        pack.addProvider(BartendingAdditionProvider::new);
    }

}
