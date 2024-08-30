package ml.pluto7073.bartending.client;

import ml.pluto7073.bartending.TheArtOfBartending;
import ml.pluto7073.bartending.client.block.renderer.BottlerBlockEntityRenderer;
import ml.pluto7073.bartending.client.gui.BoilerScreen;
import ml.pluto7073.bartending.client.gui.BottlerScreen;
import ml.pluto7073.bartending.client.gui.DistilleryScreen;
import ml.pluto7073.bartending.content.alcohol.AlcoholicDrinks;
import ml.pluto7073.bartending.content.block.BartendingBlocks;
import ml.pluto7073.bartending.content.block.entity.BartendingBlockEntities;
import ml.pluto7073.bartending.content.block.entity.BoilerBlockEntity;
import ml.pluto7073.bartending.content.fluid.BartendingFluids;
import ml.pluto7073.bartending.content.gui.BartendingMenuTypes;
import ml.pluto7073.bartending.content.item.BartendingItems;
import ml.pluto7073.bartending.content.item.ConcoctionItem;
import ml.pluto7073.bartending.foundations.BrewingUtil;
import ml.pluto7073.bartending.client.config.ClientConfig;
import ml.pluto7073.bartending.foundations.ColorUtil;
import ml.pluto7073.bartending.foundations.alcohol.AlcoholicDrink;
import ml.pluto7073.bartending.foundations.fluid.FluidHolder;
import ml.pluto7073.pdapi.DrinkUtil;
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
import net.minecraft.resources.ResourceLocation;

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

        ColorProviderRegistry.ITEM.register((stack, i) -> i > 0 ? -1 : 0x2b0010, BartendingItems.RED_WINE);
        ColorProviderRegistry.ITEM.register((stack, i) -> i > 0 ? -1 : BrewingUtil.getColorForDrinkWithDefault(stack, 0x2b0010), BartendingItems.GLASS_OF_RED_WINE);
        ColorProviderRegistry.ITEM.register((stack, i) -> i > 0 ? -1 : 0xe2c36c, BartendingItems.WHITE_WINE);
        ColorProviderRegistry.ITEM.register((stack, i) -> i > 0 ? -1 : BrewingUtil.getColorForDrinkWithDefault(stack, 0xe2c36c), BartendingItems.GLASS_OF_WHITE_WINE);

        ColorProviderRegistry.ITEM.register((stack, i) -> {
            DrinkAddition[] array = Arrays.stream(DrinkUtil.getAdditionsFromStack(stack))
                    .filter(DrinkAddition::changesColor).toList().toArray(new DrinkAddition[0]);
            if (array.length == 0) return i > 0 ? -1 : 4210943;
            return i > 0 ? -1 : BrewingUtil.getColorForDrinkWithDefault(stack, array[0].getColor());
        }, BartendingItems.MIXED_DRINK);

        ColorProviderRegistry.ITEM.register((stack, i) -> i > 0 ? -1 : 0xbc8a49, BartendingItems.APPLE_LIQUEUR, BartendingItems.SHOT_OF_APPLE_LIQUEUR);
        ColorProviderRegistry.ITEM.register((stack, i) -> i > 0 ? -1 : 0x825424, BartendingItems.RUM, BartendingItems.SHOT_OF_RUM);
        ColorProviderRegistry.ITEM.register((stack, i) -> i > 0 ? -1 : 0x211304, BartendingItems.COFFEE_LIQUEUR, BartendingItems.SHOT_OF_COFFEE_LIQUEUR);

        BartendingBlocks.BARRELS.forEach((type, barrel) -> {
            String id = BuiltInRegistries.BLOCK.getKey(barrel).toString();
            int color = ColorUtil.COLORS_REGISTRY.containsKey(id) ? ColorUtil.get(BuiltInRegistries.BLOCK.getKey(barrel).toString()) : barrel.defaultMapColor().col;
            ColorProviderRegistry.BLOCK.register((state, getter, pos, index) -> color, barrel);
            ColorProviderRegistry.ITEM.register((stack, i) -> color, barrel);
        });
    }

    private static void initRendering() {
        BlockRenderLayerMap.INSTANCE.putBlock(BartendingBlocks.DISTILLERY, RenderType.translucent());
        BlockEntityRenderers.register(BartendingBlockEntities.BOTTLER_BLOCK_ENTITY_TYPE, BottlerBlockEntityRenderer::new);

        // Fluids
        for (AlcoholicDrink drink : AlcoholicDrinks.values()) {
            FluidHolder holder = drink.fluid();
            if (holder == null) continue;
            registerFluidRenderer(holder, drink.color());
        }
    }

    private static void registerFluidRenderer(FluidHolder holder, int color) {
        FluidRenderHandlerRegistry.INSTANCE.register(holder.still(), holder.flowing(), SimpleFluidRenderHandler.coloredWater(color));
        ColorProviderRegistry.ITEM.register((stack, i) -> i > 0 ? color : -1, holder.still().getBucket());
        BlockRenderLayerMap.INSTANCE.putBlock(holder.block(), RenderType.translucent());
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
            return drink.base() == BartendingItems.MIXED_DRINK ? 1 : 0;
        });
    }

}
