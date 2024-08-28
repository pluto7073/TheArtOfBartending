package ml.pluto7073.bartending;

import ml.pluto7073.bartending.content.alcohol.AlcoholicDrinks;
import ml.pluto7073.bartending.content.block.BartendingBlocks;
import ml.pluto7073.bartending.content.block.entity.BartendingBlockEntities;
import ml.pluto7073.bartending.content.enchantment.BartendingEnchantments;
import ml.pluto7073.bartending.content.entity.effect.BartendingMobEffects;
import ml.pluto7073.bartending.content.fluid.BartendingFluids;
import ml.pluto7073.bartending.content.gui.BartendingMenuTypes;
import ml.pluto7073.bartending.content.item.BartendingItems;
import ml.pluto7073.bartending.content.sound.BartendingSounds;
import ml.pluto7073.bartending.foundations.alcohol.AlcoholData;
import ml.pluto7073.bartending.foundations.alcohol.AlcoholHandler;
import ml.pluto7073.bartending.foundations.item.BartendingCreativeTabs;
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
        AlcoholData.init();
        AlcoholHandler.init();
        BartendingSounds.init();
        BartendingFluids.init();
        BartendingBlocks.init();
        BartendingBlockEntities.init();
        BartendingMenuTypes.init();
        BartendingItems.init();
        BartendingEnchantments.init();
        BartendingMobEffects.init();
        BartendingCreativeTabs.init();
        AlcoholicDrinks.init();
        LOGGER.info("Oh no its alcohol time...");
    }

    public static ResourceLocation asId(String name) {
        return new ResourceLocation(MOD_ID, name);
    }

}
