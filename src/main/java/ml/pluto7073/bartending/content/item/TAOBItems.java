package ml.pluto7073.bartending.content.item;

import ml.pluto7073.bartending.TheArtOfBartending;
import ml.pluto7073.bartending.content.block.TAOBBlocks;
import ml.pluto7073.bartending.content.item.tier.GlassBottleTier;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.*;

public class TAOBItems {

    // Items

    public static final Item CONCOCTION = new ConcoctionItem(new Item.Properties().stacksTo(1).craftRemainder(Items.GLASS_BOTTLE));
    public static final Item WINE_BOTTLE = new SwordItem(GlassBottleTier.INSTANCE, 3, 0, new Item.Properties().stacksTo(1));

    // Block Items

    public static final Item BOILER = new BlockItem(TAOBBlocks.BOILER, new Item.Properties());

    private static void register(String id, Item item) {
        Registry.register(BuiltInRegistries.ITEM, TheArtOfBartending.asId(id), item);
    }

    public static void init() {
        register("concoction", CONCOCTION);
        register("wine_bottle", WINE_BOTTLE);

        register("boiler", BOILER);
    }

}
