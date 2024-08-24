package ml.pluto7073.bartending.foundations.step;

import ml.pluto7073.bartending.foundations.BrewingUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.function.Predicate;

public class FermentingBrewerStep implements BrewerStep {

    public static final String TYPE_ID = "fermenting";

    private final BarrelPredicate predicate;
    private final int wantedTicks, tickLeeway;

    public FermentingBrewerStep(BarrelPredicate predicate, int wantedTicks) {
        this(predicate, wantedTicks, 12000);
    }

    public FermentingBrewerStep(BarrelPredicate predicate, int wantedTicks, int tickLeeway) {
        this.predicate = predicate;
        this.wantedTicks = wantedTicks;
        this.tickLeeway = tickLeeway;
    }

    @Override
    public boolean matches(CompoundTag data) {
        if (!TYPE_ID.equals(data.getString("type"))) return false;
        ResourceLocation barrelId = new ResourceLocation(data.getString("barrel"));
        if (!predicate.test(BuiltInRegistries.BLOCK.get(barrelId))) return false;
        int ticks = data.getInt("ticks");
        int diff = Math.abs(ticks - wantedTicks);
        return diff < tickLeeway;
    }

    @Override
    public int getDeviation(CompoundTag data, float standard) {
        int ticks = data.getInt("ticks");
        return Math.round(Mth.clampedMap(ticks, wantedTicks - tickLeeway,
                wantedTicks + tickLeeway, -0.5f, 0.5f) * standard);
    }

    public static void appendInProgressText(CompoundTag data, List<Component> tooltips) {
        int ticks = data.getInt("ticks");
        Block barrel = BuiltInRegistries.BLOCK.get(new ResourceLocation(data.getString("barrel")));
        tooltips.add(Component.translatable("tooltip.bartending.fermenting_in").append(Component.translatable(barrel.getDescriptionId()))
                .withStyle(ChatFormatting.GRAY));
        String time = BrewingUtil.getMinecraftDays(ticks);
        tooltips.add(Component.translatable("tooltip.bartending.fermenting_for", time).withStyle(ChatFormatting.GRAY));
    }

}
