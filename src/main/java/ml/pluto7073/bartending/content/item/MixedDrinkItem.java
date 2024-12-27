package ml.pluto7073.bartending.content.item;

import ml.pluto7073.pdapi.item.AbstractCustomizableDrinkItem;
import net.minecraft.world.item.Items;

public class MixedDrinkItem extends AbstractCustomizableDrinkItem {

    public MixedDrinkItem(Properties settings) {
        super(BartendingItems.COCKTAIL_GLASS, Temperature.NORMAL, settings);
    }

}
