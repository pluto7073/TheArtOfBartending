package ml.pluto7073.bartending.foundations.item;

import ml.pluto7073.bartending.TheArtOfBartending;
import ml.pluto7073.bartending.content.item.TAOBItems;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class AlcoholCreativeTabs {

    public static final ResourceKey<CreativeModeTab> MAIN_TAB =
            ResourceKey.create(Registries.CREATIVE_MODE_TAB, TheArtOfBartending.asId("main"));

    public static void init() {
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, MAIN_TAB,
                FabricItemGroup.builder().title(Component.translatable("itemGroup.bartending.main"))
                        .icon(() -> new ItemStack(TAOBItems.RED_WINE)).build());
        ItemGroupEvents.modifyEntriesEvent(MAIN_TAB).register(stacks -> {

            // Blocks
            stacks.accept(TAOBItems.BOILER);
            stacks.accept(TAOBItems.BOTTLER);
            stacks.acceptAll(TAOBItems.FERMENTING_BARRELS.values().stream().map(ItemStack::new).toList());

            // Bottles
            stacks.accept(Items.GLASS_BOTTLE);
            stacks.accept(TAOBItems.WINE_BOTTLE);
            stacks.accept(TAOBItems.BEER_BOTTLE);
            stacks.accept(TAOBItems.LIQUOR_BOTTLE);

            // Drinks
            stacks.accept(TAOBItems.RED_WINE);
            stacks.accept(TAOBItems.WHITE_WINE);
            stacks.accept(TAOBItems.BEER);
            stacks.accept(TAOBItems.VODKA);

            // Servings
            stacks.accept(TAOBItems.GLASS_OF_RED_WINE);
            stacks.accept(TAOBItems.GLASS_OF_WHITE_WINE);
            stacks.accept(TAOBItems.SHOT_OF_VODKA);
        });
    }

}
