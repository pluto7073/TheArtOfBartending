package ml.pluto7073.bartending.client;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import ml.pluto7073.bartending.client.gui.BoilerScreen;
import ml.pluto7073.bartending.client.gui.BottlerScreen;
import ml.pluto7073.bartending.content.block.TAOBBlocks;
import ml.pluto7073.bartending.content.block.entity.BoilerBlockEntity;
import ml.pluto7073.bartending.content.gui.TAOBMenuTypes;
import ml.pluto7073.bartending.content.item.TAOBItems;
import ml.pluto7073.bartending.foundations.BrewingUtil;
import ml.pluto7073.bartending.client.config.ClientConfig;
import ml.pluto7073.pdapi.DrinkUtil;
import ml.pluto7073.pdapi.addition.DrinkAddition;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.gui.screens.MenuScreens;

import java.util.Arrays;

public class TheArtOfClient implements ClientModInitializer {

    private static ClientConfig CONFIG = null;

    @Override
    public void onInitializeClient() {
        initColors();
        registerScreens();
    }

    public static ClientConfig config() {
        return CONFIG == null ? CONFIG = new ClientConfig() : CONFIG;
    }

    private static void initColors() {
        ColorProviderRegistry.BLOCK.register((state, blockTintGetter, pos, index) -> {
            if (blockTintGetter == null) return -1;
            if (pos == null) return -1;
            if (!(blockTintGetter.getBlockEntity(pos) instanceof BoilerBlockEntity boiler)) return -1;
            if (boiler.data.get(BoilerBlockEntity.BOIL_TIME_DATA) <= 0) return 4210943;
            return BrewingUtil.getColorForConcoction(boiler.getItem(BoilerBlockEntity.DISPLAY_RESULT_ITEM_SLOT_INDEX));
        }, TAOBBlocks.BOILER);

        ColorProviderRegistry.ITEM.register((stack, i) -> i > 0 ? -1 : 4210943, TAOBItems.BOILER);

        ColorProviderRegistry.ITEM.register((stack, index) -> index > 0 ? -1 : BrewingUtil.getColorForConcoction(stack), TAOBItems.CONCOCTION);

        ColorProviderRegistry.ITEM.register((stack, i) -> i > 0 ? -1 : 0x2b0010, TAOBItems.RED_WINE);
        ColorProviderRegistry.ITEM.register((stack, i) -> i > 0 ? -1 : BrewingUtil.getColorForDrinkWithDefault(stack, 0x2b0010), TAOBItems.GLASS_OF_RED_WINE);
        ColorProviderRegistry.ITEM.register((stack, i) -> i > 0 ? -1 : 0xe2c36c, TAOBItems.WHITE_WINE);
        ColorProviderRegistry.ITEM.register((stack, i) -> i > 0 ? -1 : BrewingUtil.getColorForDrinkWithDefault(stack, 0xe2c36c), TAOBItems.GLASS_OF_WHITE_WINE);

        ColorProviderRegistry.ITEM.register((stack, i) -> {
            DrinkAddition[] array = Arrays.stream(DrinkUtil.getAdditionsFromStack(stack))
                    .filter(DrinkAddition::changesColor).toList().toArray(new DrinkAddition[0]);
            if (array.length == 0) return i > 0 ? -1 : 4210943;
            return i > 0 ? -1 : BrewingUtil.getColorForDrinkWithDefault(stack, array[0].getColor());
        }, TAOBItems.MIXED_DRINK);
    }

    private static void registerScreens() {
        MenuScreens.register(TAOBMenuTypes.BOILER_MENU_TYPE, BoilerScreen::new);
        MenuScreens.register(TAOBMenuTypes.BOTTLER_MENU_TYPE, BottlerScreen::new);
    }

}
