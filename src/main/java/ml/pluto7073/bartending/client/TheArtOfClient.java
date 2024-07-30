package ml.pluto7073.bartending.client;

import ml.pluto7073.bartending.client.gui.BoilerScreen;
import ml.pluto7073.bartending.content.block.TAOBBlocks;
import ml.pluto7073.bartending.content.block.entity.BoilerBlockEntity;
import ml.pluto7073.bartending.content.gui.TAOBMenuTypes;
import ml.pluto7073.bartending.content.item.TAOBItems;
import ml.pluto7073.bartending.foundations.BrewingUtil;
import ml.pluto7073.bartending.foundations.step.ItemColors;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.MapColor;

public class TheArtOfClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        initColors();
        registerScreens();
    }

    private static void initColors() {
        ColorProviderRegistry.BLOCK.register((state, blockTintGetter, pos, index) -> {
            if (blockTintGetter == null) return -1;
            if (pos == null) return -1;
            if (!(blockTintGetter.getBlockEntity(pos) instanceof BoilerBlockEntity boiler)) return -1;
            if (!boiler.isBoiling(pos)) return 4210943;
            return BrewingUtil.getColorForConcoction(boiler.inventory.get(BoilerBlockEntity.DISPLAY_RESULT_ITEM_SLOT_INDEX));
        }, TAOBBlocks.BOILER);
        ColorProviderRegistry.ITEM.register((stack, index) -> index > 0 ? -1 : BrewingUtil.getColorForConcoction(stack), TAOBItems.CONCOCTION);
    }

    private static void registerScreens() {
        MenuScreens.register(TAOBMenuTypes.BOILER_MENU_TYPE, BoilerScreen::new);
    }

}
