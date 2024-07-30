package ml.pluto7073.bartending.foundations.water;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.HashMap;
import java.util.Map;

public class ValidWaterSources {

    // Watersource Ingredient to corresponding water in mb amount
    public static final HashMap<Ingredient, Integer> REGISTRY = new HashMap<>();

    static {
        REGISTRY.put(Ingredient.of(Items.WATER_BUCKET), 1000);
        REGISTRY.put(Ingredient.of(Items.POTION, Items.SPLASH_POTION, Items.LINGERING_POTION), 250);
    }

    public static int getAmountFromItem(ItemStack item) {
        int amount = 0;
        for (Map.Entry<Ingredient, Integer> entry : REGISTRY.entrySet()) {
            if (entry.getKey().test(item)) {
                amount = entry.getValue();
                break;
            }
        }
        return amount;
    }

}
