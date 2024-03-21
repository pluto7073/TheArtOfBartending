package ml.pluto7073.bartending.item;

import ml.pluto7073.bartending.TheArtOfBartending;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class TAOBItems {

    public static final Item UNFINALIZED_ALCOHOLIC_DRINK_ITEM = new UnfinalizedAlcoholicDrinkItem(new Item.Settings());

    private static void register(String id, Item item) {
        Registry.register(Registries.ITEM, TheArtOfBartending.asId(id), item);
    }

    public static void init() {
        register("unfinalized_alcoholic_drink", UNFINALIZED_ALCOHOLIC_DRINK_ITEM);
    }

}
