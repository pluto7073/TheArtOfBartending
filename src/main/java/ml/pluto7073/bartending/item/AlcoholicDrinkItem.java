package ml.pluto7073.bartending.item;

import ml.pluto7073.pdapi.item.AbstractCustomizableDrinkItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class AlcoholicDrinkItem extends AbstractCustomizableDrinkItem {

    protected AlcoholicDrinkItem(Settings settings) {
        super(Items.GLASS_BOTTLE, Temperature.NORMAL, settings);
    }



}
