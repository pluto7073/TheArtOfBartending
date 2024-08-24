package ml.pluto7073.bartending.foundations.step;

import ml.pluto7073.bartending.foundations.BrewingUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public class BoilingBrewerStep implements BrewerStep {

    public static final String TYPE_ID = "boiling";

    private final Ingredient base;
    private final int wantedTicks;
    private final int count;
    private final int tickLeeway;

    public BoilingBrewerStep(Ingredient base, int count, int wantedTicks) {
        this(base, wantedTicks, count, 1200);
    }

    public BoilingBrewerStep(Ingredient base, int count, int wantedTicks, int tickLeeway) {
        this(base, count, wantedTicks, tickLeeway, 5);
    }

    public BoilingBrewerStep(Ingredient base, int count, int wantedTicks, int tickLeeway, int countLeeway) {
        this.base = base;
        this.count = count;
        this.wantedTicks = wantedTicks;
        this.tickLeeway = tickLeeway;
    }

    @Override
    public boolean matches(CompoundTag data) {
        if (!TYPE_ID.equals(data.getString("type"))) return false;
        int ticks = data.getInt("ticks");
        if (Math.abs(wantedTicks - ticks) > tickLeeway) return false;
        ResourceLocation itemId = new ResourceLocation(data.getString("item"));
        Item item = BuiltInRegistries.ITEM.get(itemId);
        if (!base.test(new ItemStack(item))) return false;
        int count = data.getInt("count");
        return this.count == count;
    }

    @Override
    public int getDeviation(CompoundTag data, float standard) {
        int count = data.getInt("count");
        int fromCount = Math.round(Mth.clampedMap(count, 0, this.count, -standard, 0));
        if (count > this.count) fromCount = 0;
        int ticks = data.getInt("ticks");
        int fromTicks = Math.round(Mth.clampedMap(ticks, wantedTicks - tickLeeway,
                wantedTicks + tickLeeway, -0.5f, 0.5f) * standard);
        if (Math.abs(fromTicks) > Math.abs(fromCount)) return fromTicks;
        else if (Math.abs(fromCount) > Math.abs(fromTicks)) return fromCount;
        else return Math.min(fromTicks, fromCount);
    }

    public static void appendInProgressText(CompoundTag data, List<Component> tooltips) {
        int ticks = data.getInt("ticks");
        Item item = BuiltInRegistries.ITEM.get(new ResourceLocation(data.getString("item")));
        int count = data.getInt("count");
        tooltips.add(Component.literal(count + "x ").append(Component.translatable(item.getDescriptionId()))
                .withStyle(ChatFormatting.GRAY));
        String time = BrewingUtil.getTimeString(ticks / 20);
        tooltips.add(Component.translatable("tooltip.bartending.boiling_in_progress", time).withStyle(ChatFormatting.GRAY));
    }

}
