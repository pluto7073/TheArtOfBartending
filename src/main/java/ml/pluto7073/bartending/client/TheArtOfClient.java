package ml.pluto7073.bartending.client;

import ml.pluto7073.bartending.TheArtOfBartending;
import ml.pluto7073.bartending.client.block.renderer.BottlerBlockEntityRenderer;
import ml.pluto7073.bartending.client.gui.BoilerScreen;
import ml.pluto7073.bartending.client.gui.BottlerScreen;
import ml.pluto7073.bartending.client.gui.DistilleryScreen;
import ml.pluto7073.bartending.content.block.BartendingBlocks;
import ml.pluto7073.bartending.content.block.entity.BartendingBlockEntities;
import ml.pluto7073.bartending.content.block.entity.BoilerBlockEntity;
import ml.pluto7073.bartending.content.fluid.BartendingFluids;
import ml.pluto7073.bartending.content.gui.BartendingMenuTypes;
import ml.pluto7073.bartending.content.item.BartendingItems;
import ml.pluto7073.bartending.content.item.ConcoctionItem;
import ml.pluto7073.bartending.foundations.item.AlcoholicDrinkItem;
import ml.pluto7073.bartending.foundations.network.BartendingClientboundPackets;
import ml.pluto7073.bartending.foundations.util.BrewingUtil;
import ml.pluto7073.bartending.client.config.ClientConfig;
import ml.pluto7073.bartending.foundations.util.ColorUtil;
import ml.pluto7073.pdapi.util.DrinkUtil;
import ml.pluto7073.pdapi.addition.DrinkAddition;
import ml.pluto7073.pdapi.item.PDItems;
import ml.pluto7073.pdapi.specialty.SpecialtyDrink;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;

import java.util.Arrays;

@Environment(EnvType.CLIENT)
public class TheArtOfClient implements ClientModInitializer {

    private static ClientConfig CONFIG = null;

    @Override
    public void onInitializeClient() {
        FabricLoader.getInstance().getModContainer(TheArtOfBartending.MOD_ID).ifPresent(container ->
                ResourceManagerHelper.registerBuiltinResourcePack(TheArtOfBartending.asId("pdapi_overrides"), container,
                        Component.translatable("pack.bartending.pdapi_overrides"), ResourcePackActivationType.ALWAYS_ENABLED));
        initColors();
        registerScreens();
        initRendering();
        registerItemProperties();
        BartendingClientboundPackets.registerReceivers();
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
        }, BartendingBlocks.BOILER);

        ColorProviderRegistry.ITEM.register((stack, i) -> i > 0 ? -1 : 4210943, BartendingItems.BOILER);

        ColorProviderRegistry.ITEM.register((stack, index) -> index > 0 ? -1 : ConcoctionItem.isFailed(stack) ? 0x545252 : BrewingUtil.getColorForConcoction(stack), BartendingItems.CONCOCTION);

        ColorProviderRegistry.ITEM.register((stack, i) -> {
            DrinkAddition[] array = Arrays.stream(DrinkUtil.getAdditionsFromStack(stack))
                    .filter(DrinkAddition::changesColor).toList().toArray(new DrinkAddition[0]);
            if (array.length == 0) return i > 0 ? -1 : 4210943;
            return i > 0 ? -1 : BrewingUtil.getColorForDrinkWithDefault(stack, array[0].getColor());
        }, BartendingItems.MIXED_DRINK);

        BartendingBlocks.BARRELS.forEach((type, barrel) -> {
            String id = BuiltInRegistries.BLOCK.getKey(barrel).toString();
            int color = ColorUtil.COLORS_REGISTRY.containsKey(id) ? ColorUtil.get(BuiltInRegistries.BLOCK.getKey(barrel).toString()) : barrel.defaultMapColor().col;
            ColorProviderRegistry.BLOCK.register((state, getter, pos, index) -> color, barrel);
            ColorProviderRegistry.ITEM.register((stack, i) -> color, barrel);
        });

        BartendingItems.SHOTS.forEach((drink, item) ->
                ColorProviderRegistry.ITEM.register((stack, i) -> i > 0 ? -1 : drink.color(), item));
        BartendingItems.BOTTLES.forEach((drink, item) ->
                ColorProviderRegistry.ITEM.register((stack, i) -> i > 0 ? -1 : drink.color(), item));
        BartendingItems.GLASSES.forEach((drink, item) ->
                ColorProviderRegistry.ITEM.register((stack, i) -> i > 0 ? -1 : BrewingUtil.getColorForDrinkWithDefault(stack, drink.color()), item));
        BartendingItems.SERVING_BOTTLES.forEach((drink, item) ->
                ColorProviderRegistry.ITEM.register((stack, i) -> i > 0 ? -1 : BrewingUtil.getColorForDrinkWithDefault(stack, drink.color()), item));
    }

    private static void initRendering() {
        BlockRenderLayerMap.INSTANCE.putBlock(BartendingBlocks.DISTILLERY, RenderType.translucent());
        BlockEntityRenderers.register(BartendingBlockEntities.BOTTLER_BLOCK_ENTITY_TYPE, BottlerBlockEntityRenderer::new);

        BartendingFluids.initRendering();
    }

    private static void registerScreens() {
        MenuScreens.register(BartendingMenuTypes.BOILER_MENU_TYPE, BoilerScreen::new);
        MenuScreens.register(BartendingMenuTypes.BOTTLER_MENU_TYPE, BottlerScreen::new);
        MenuScreens.register(BartendingMenuTypes.DISTILLERY_MENU_TYPE, DistilleryScreen::new);
    }

    private static void registerItemProperties() {
        ItemProperties.register(PDItems.SPECIALTY_DRINK, TheArtOfBartending.asId("mixed_drink"), (stack, level, entity, i) -> {
            if (!stack.getOrCreateTag().contains("Drink")) return 0;
            SpecialtyDrink drink = DrinkUtil.getSpecialDrink(stack);
            if (drink.base().buildItemStack().getItem() == BartendingItems.MIXED_DRINK) return 1;
            if (drink.base().buildItemStack().getItem() instanceof AlcoholicDrinkItem item) {
                return BartendingItems.GLASSES.containsValue(item) ? 1 : 0;
            }
            return 0;
        });

        ItemProperties.register(BartendingItems.CONCOCTION, TheArtOfBartending.asId("just_liquid"), (itemStack, clientLevel, livingEntity, i) -> {
            if (!itemStack.getOrCreateTag().contains("JustLiquid")) return 0;
            boolean b = itemStack.getOrCreateTag().getBoolean("JustLiquid");
            return b ? 1 : 0;
        });
    }

}
