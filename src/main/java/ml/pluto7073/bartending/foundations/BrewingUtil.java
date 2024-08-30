package ml.pluto7073.bartending.foundations;

import ml.pluto7073.bartending.content.item.BartendingItems;
import ml.pluto7073.bartending.foundations.alcohol.AlcDisplayType;
import ml.pluto7073.bartending.foundations.alcohol.AlcoholicDrink;
import ml.pluto7073.bartending.foundations.step.*;
import ml.pluto7073.pdapi.DrinkUtil;
import ml.pluto7073.pdapi.addition.DrinkAddition;
import ml.pluto7073.pdapi.item.AbstractCustomizableDrinkItem;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class BrewingUtil {

    public static ItemStack createConcoctionFromBaseTicks(NonNullList<ItemStack> base, int ticks) {
        CompoundTag data = new CompoundTag();
        data.putString("type", BoilingBrewerStep.TYPE_ID);
        data.putInt("ticks", ticks);
        if (isInventorySingleItem(base)) {
            data.putString("item", BuiltInRegistries.ITEM.getKey(base.get(0).getItem()).toString());
            data.putInt("count", base.get(0).getCount());
        } else {
            ContainerHelper.saveAllItems(data, base, false);
        }
        ItemStack stack = new ItemStack(BartendingItems.CONCOCTION, 1);
        ListTag list = new ListTag();
        list.add(data);
        stack.getOrCreateTag().put("BrewingSteps", list);
        return stack;
    }

    public static boolean isInventorySingleItem(NonNullList<ItemStack> inventory) {
        return inventory.stream().filter(s -> !s.isEmpty()).toArray().length == 1;
    }

    public static ResourceLocation[] getBaseItemFromConcoction(ItemStack concoction) {
        if (!concoction.getOrCreateTag().contains("BrewingSteps"))
            return new ResourceLocation[] {new ResourceLocation("minecraft:air")};
        ListTag list = concoction.getOrCreateTag().getList("BrewingSteps", Tag.TAG_COMPOUND);
        CompoundTag data = list.getCompound(0);
        if (data.contains("item", Tag.TAG_STRING)) {
            return new ResourceLocation[] {
                    new ResourceLocation(data.getString("item"))
            };
        } else if (data.contains("Items", Tag.TAG_LIST)) {
            NonNullList<ItemStack> stacks = NonNullList.withSize(4, ItemStack.EMPTY);
            ContainerHelper.loadAllItems(data, stacks);
            return stacks.stream().filter(Predicate.not(ItemStack::isEmpty)).map(ItemStack::getItem)
                    .map(BuiltInRegistries.ITEM::getKey).toList().toArray(new ResourceLocation[0]);
        }
        return new ResourceLocation[0];
    }

    public static int getColorForConcoction(ItemStack concoction) {
        ResourceLocation[] items = getBaseItemFromConcoction(concoction);
        if (items.length == 1) {
            return ColorUtil.get(items[0].toString());
        }
        List<Integer> colors = Arrays.stream(items).map(ResourceLocation::toString)
                .filter(ColorUtil.COLORS_REGISTRY::containsKey).map(ColorUtil::get).toList();
        return averageColors(colors);
    }

    public static int getColorForDrinkWithDefault(ItemStack drink, int normal) {
        DrinkAddition[] additions = DrinkUtil.getAdditionsFromStack(drink);
        List<Integer> colors = Arrays.stream(additions).filter(DrinkAddition::changesColor)
                .map(DrinkAddition::getColor).collect(Collectors.toCollection(ArrayList::new));
        colors.add(0, normal);
        return averageColors(colors);
    }

    public static boolean isEmpty(NonNullList<ItemStack> items) {
        for (ItemStack s : items) {
            if (!s.isEmpty()) return false;
        }
        return true;
    }

    public static int averageColors(Collection<Integer> colors) {
        if (colors.isEmpty()) return 0xFFFFFF;
        int r = 0;
        int g = 0;
        int b = 0;
        for (int color : colors) {
            r += (color >> 16 & 255);
            g += (color >> 8 & 255);
            b += (color & 255);
        }
        r /= colors.size();
        g /= colors.size();
        b /= colors.size();
        return r << 16 | g << 8 | b;
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
        ItemStack failed = new ItemStack(BartendingItems.CONCOCTION);
        int ticksBoiled = 0, ticksFermented = 0;
        boolean distilled = false;
        ListTag steps = og.getOrCreateTag().getList("BrewingSteps", Tag.TAG_COMPOUND);
        for (Tag tag : steps) {
            if (!(tag instanceof CompoundTag data)) continue;
            switch (data.getString("type")) {
                case BoilingBrewerStep.TYPE_ID -> ticksBoiled += data.getInt("ticks");
                case FermentingBrewerStep.TYPE_ID -> ticksFermented += data.getInt("ticks");
                case DistillingBrewerStep.TYPE_ID -> distilled = true;
            }
        }
        CompoundTag data = new CompoundTag();
        data.putInt("ticksBoiled", ticksBoiled);
        data.putInt("ticksFermented", ticksFermented);
        data.putBoolean("distilled", distilled);
        failed.getOrCreateTag().put("FailedConcoctionData", data);
        return failed;
    }

}
