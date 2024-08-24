package ml.pluto7073.bartending.foundations;

import ml.pluto7073.bartending.foundations.alcohol.AlcDisplayType;
import ml.pluto7073.bartending.foundations.alcohol.AlcoholicDrink;
import ml.pluto7073.bartending.content.item.TAOBItems;
import ml.pluto7073.bartending.foundations.step.*;
import ml.pluto7073.pdapi.DrinkUtil;
import ml.pluto7073.pdapi.addition.DrinkAddition;
import ml.pluto7073.pdapi.item.AbstractCustomizableDrinkItem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;
import java.util.List;

public class BrewingUtil {

    public static ItemStack createConcoctionFromBaseTicks(ItemStack base, int ticks) {
        CompoundTag data = new CompoundTag();
        data.putString("type", BoilingBrewerStep.TYPE_ID);
        data.putString("item", BuiltInRegistries.ITEM.getKey(base.getItem()).toString());
        data.putInt("count", base.getCount());
        data.putInt("ticks", ticks);
        ItemStack stack = new ItemStack(TAOBItems.CONCOCTION, 1);
        ListTag list = new ListTag();
        list.add(data);
        stack.getOrCreateTag().put("BrewingSteps", list);
        return stack;
    }

    public static ResourceLocation getBaseItemFromConcoction(ItemStack concoction) {
        if (!concoction.getOrCreateTag().contains("BrewingSteps")) return new ResourceLocation("minecraft:air");
        ListTag list = concoction.getOrCreateTag().getList("BrewingSteps", Tag.TAG_COMPOUND);
        return new ResourceLocation(list.getCompound(0).getString("item"));
    }

    public static int getColorForConcoction(ItemStack concoction) {
        return ItemColors.get(getBaseItemFromConcoction(concoction).toString());
    }

    public static int getColorForDrinkWithDefault(ItemStack drink, int normal) {
        DrinkAddition[] additions = DrinkUtil.getAdditionsFromStack(drink);
        List<Integer> colors = Arrays.stream(additions).filter(DrinkAddition::changesColor).map(DrinkAddition::getColor).toList();
        float r = (normal >> 16 & 255) / 255F;
        float g = (normal >> 8 & 255) / 255F;
        float b = (normal & 255) / 255f;
        for (int color : colors) {
            r += (color >> 16 & 255) / 255f;
            g += (color >> 8 & 255) / 255f;
            b += (color & 255) / 255f;
        }
        r = r / (colors.size() + 1) * 255f;
        g = g / (colors.size() + 1) * 255f;
        b = b / (colors.size() + 1) * 255f;
        return (int) r << 16 | (int) g << 8 | (int) b;
    }

    public static String getTimeString(int seconds) {
        float minutes = seconds / 60f;
        float hours = minutes / 60;
        int hoursDisplay = (int) hours;
        int rawIntMinutes = (int) minutes;
        int minutesDisplay = rawIntMinutes - hoursDisplay * 60;
        int secondsDisplay = seconds - rawIntMinutes * 60;
        String paddedMinutes = ((minutesDisplay < 10 && hoursDisplay > 0) ? "0" : "") + minutesDisplay;
        String paddedSeconds = (secondsDisplay < 10 ? "0" : "") + secondsDisplay;

        return hoursDisplay == 0 ? "%s:%s".formatted(paddedMinutes, paddedSeconds) : "%s:%s:%s".formatted(hoursDisplay, paddedMinutes, paddedSeconds);
    }

    public static String getMinecraftDays(int ticks) {
        int seconds = ticks / 20;
        int minutes = seconds / 60;
        return "" + (minutes / 20);
    }

    public static int getAlcoholDeviation(ItemStack stack) {
        if (!stack.getOrCreateTagElement(AbstractCustomizableDrinkItem.DRINK_DATA_NBT_KEY).contains("Deviation")) return 0;
        return stack.getOrCreateTagElement(AbstractCustomizableDrinkItem.DRINK_DATA_NBT_KEY).getInt("Deviation");
    }

    /**
     * @param drink The drink to get the alcohol from
     * @return The amount of alcohol in grams in a standard size of the specified drink
     */
    public static int getStandardAlcohol(AlcoholicDrink drink) {
        float ounces = drink.standardOunces();
        return getAlcohol(drink, ounces);
    }

    public static int getAlcohol(AlcoholicDrink drink, float ounces) {
        int proof = drink.standardProof();
        float percent = proof / 200f;
        float ouncesAlc = ounces * percent;
        return Math.round(convertType(ouncesAlc, AlcDisplayType.OUNCES, AlcDisplayType.GRAMS));
    }

    public static int getProof(AlcoholicDrink drink, float newAmount) {
        float ounces = drink.standardOunces();
        float ounceAlc = convertType(newAmount, AlcDisplayType.GRAMS, AlcDisplayType.OUNCES);
        return Math.round(200 * (ounceAlc / ounces));
    }

    public static float calculateBAC(float grams) {
        return (grams / (184.35f * 454 * 0.615f)) * 100;
    }

    public static float convertType(float amount, AlcDisplayType from, AlcDisplayType to) {
        float grams = amount / from.multiplier;
        return grams * to.multiplier;
    }

    public static ItemStack constructFailedConcoction(ItemStack og) {
        ItemStack failed = new ItemStack(TAOBItems.CONCOCTION);
        int ticksBoiled = 0, ticksFermented = 0, ticksDistilled = 0;
        ListTag steps = og.getOrCreateTag().getList("BrewingSteps", Tag.TAG_COMPOUND);
        for (Tag tag : steps) {
            if (!(tag instanceof CompoundTag data)) continue;
            switch (data.getString("type")) {
                case BoilingBrewerStep.TYPE_ID -> ticksBoiled += data.getInt("ticks");
                case FermentingBrewerStep.TYPE_ID -> ticksFermented += data.getInt("ticks");
                case DistillingBrewerStep.TYPE_ID -> ticksDistilled += data.getInt("ticks");
            }
        }
        CompoundTag data = new CompoundTag();
        data.putInt("ticksBoiled", ticksBoiled);
        data.putInt("ticksFermented", ticksFermented);
        data.putInt("ticksDistilled", ticksDistilled);
        failed.getOrCreateTag().put("FailedConcoctionData", data);
        return failed;
    }

}
