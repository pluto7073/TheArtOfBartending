package ml.pluto7073.bartending;

import ml.pluto7073.bartending.content.alcohol.AlcoholicDrinkRegistry;
import ml.pluto7073.bartending.content.block.TAOBBlocks;
import ml.pluto7073.bartending.content.block.entity.TAOBBlockEntities;
import ml.pluto7073.bartending.content.gui.TAOBMenuTypes;
import ml.pluto7073.bartending.content.item.TAOBItems;
import ml.pluto7073.bartending.foundations.alcohol.AlcoholHandler;
import net.fabricmc.api.ModInitializer;
import net.minecraft.SharedConstants;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TheArtOfBartending implements ModInitializer {

    public static final String MOD_ID = "bartending";
    public static final Logger LOGGER = LogManager.getLogger("TheArtOfBartending");

    @Override
    public void onInitialize() {
        SharedConstants.IS_RUNNING_IN_IDE = true;
        AlcoholHandler.init();
        TAOBBlocks.init();
        TAOBBlockEntities.init();
        TAOBMenuTypes.init();
        TAOBItems.init();
        AlcoholicDrinkRegistry.init();
        LOGGER.info("Oh no its alcohol time...");
    }

    public static ResourceLocation asId(String name) {
        return new ResourceLocation(MOD_ID, name);
    }

}
