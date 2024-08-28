package ml.pluto7073.bartending.foundations.item;

import ml.pluto7073.bartending.foundations.alcohol.AlcoholicDrink;
import ml.pluto7073.bartending.foundations.BrewingUtil;
import ml.pluto7073.pdapi.item.AbstractCustomizableDrinkItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class AlcoholicDrinkItem extends AbstractCustomizableDrinkItem {

    public final int alcohol;
    public final AlcoholicDrink source;
    public final Item bottle;

    public AlcoholicDrinkItem(AlcoholicDrink source, Item bottle, Properties settings) {
        super(bottle, Temperature.NORMAL, settings);
        this.source = source;
        this.bottle = bottle;
        alcohol = BrewingUtil.getStandardAlcohol(source);
    }

    @Override
    public int getChemicalContent(String name, ItemStack stack) {
        return super.getChemicalContent(name, stack) + ("alcohol".equals(name) ? alcohol +
                (stack.getOrCreateTagElement(DRINK_DATA_NBT_KEY).contains("Deviation") ?
                        stack.getOrCreateTagElement(DRINK_DATA_NBT_KEY).getInt("Deviation") : 0) : 0);
    }
}
