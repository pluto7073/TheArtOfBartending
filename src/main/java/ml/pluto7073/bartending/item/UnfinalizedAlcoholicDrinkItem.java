package ml.pluto7073.bartending.item;

import ml.pluto7073.bartending.BrewingUtil;
import ml.pluto7073.bartending.alcohol.ItemToAlcoholRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

public class UnfinalizedAlcoholicDrinkItem extends Item {

    public UnfinalizedAlcoholicDrinkItem(Settings settings) {
        super(settings);
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        return "unfinalized." + ItemToAlcoholRegistry.get(BrewingUtil.getBaseUnfinalizedItem(stack)).getTranslationKey();
    }
}
