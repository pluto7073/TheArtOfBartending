package ml.pluto7073.bartending.alcohol;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.HashMap;
import java.util.Map;

public class ItemToAlcoholRegistry {

    private static final Map<Item, AlcoholicDrink> REGISTRY = new HashMap<>();

    public static AlcoholicDrink get(Item item) {
        return REGISTRY.get(item);
    }

    public static void init() {}

    static {
        REGISTRY.put(Items.WHEAT, AlcoholicDrinkRegistry.BEER);
        REGISTRY.put(Items.SWEET_BERRIES, AlcoholicDrinkRegistry.RED_WINE);
        REGISTRY.put(Items.GLOW_BERRIES, AlcoholicDrinkRegistry.WHITE_WINE);
    }

}
