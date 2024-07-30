package ml.pluto7073.bartending.foundations;

import ml.pluto7073.bartending.content.item.TAOBItems;
import ml.pluto7073.bartending.foundations.step.BoilingBrewerStep;
import ml.pluto7073.bartending.foundations.step.ItemColors;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.MapColor;

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
        return ItemColors.COLORS_REGISTRY.get(getBaseItemFromConcoction(concoction).toString());
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

}
