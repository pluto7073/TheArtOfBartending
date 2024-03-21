package ml.pluto7073.bartending;

import ml.pluto7073.bartending.alcohol.AlcoholicDrinkRegistry;
import ml.pluto7073.bartending.alcohol.ItemToAlcoholRegistry;
import ml.pluto7073.bartending.item.TAOBItems;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TheArtOfBartending implements ModInitializer {

    public static final String MOD_ID = "bartending";
    public static final Logger LOGGER = LogManager.getLogger("TheArtOfBartending");

    @Override
    public void onInitialize() {
        TAOBItems.init();
        AlcoholicDrinkRegistry.init();
        ItemToAlcoholRegistry.init();
        LOGGER.info("Oh no its alcohol time...");
    }

    public static Identifier asId(String name) {
        return new Identifier(MOD_ID, name);
    }

}
