package ml.pluto7073.bartending.foundations.step;

import ml.pluto7073.bartending.foundations.config.BartendingGameRules;
import ml.pluto7073.bartending.foundations.util.BrewingUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.List;

public class FermentingBrewerStep implements BrewerStep {

    public static final String TYPE_ID = "fermenting";

    public final BarrelPredicate predicate;
    public final int years;
    public final int yearLeeway;

    public FermentingBrewerStep(BarrelPredicate predicate, int years) {
        this(predicate, years, 1);
    }

    public FermentingBrewerStep(BarrelPredicate predicate, int years, int yearLeeway) {
        this.predicate = predicate;
        this.years = years;
        this.yearLeeway = yearLeeway;
    }

    @Override
    public boolean mightMatch(CompoundTag data, Level level) {
        if (!TYPE_ID.equals(data.getString("type"))) return false;
        ResourceLocation barrelId = new ResourceLocation(data.getString("barrel"));
        if (!predicate.test(BuiltInRegistries.BLOCK.get(barrelId))) return false;
        int ticksPerYear = level.getGameRules().getInt(BartendingGameRules.YEAR_LENGTH_TICKS);
        int ticks = data.getInt("ticks");
        int years = ticks / ticksPerYear;
        return years <= this.years + this.yearLeeway;
    }

    @Override
    public String id() {
        return TYPE_ID;
    }

    @Override
    public boolean matches(CompoundTag data, Level level) {
        if (!TYPE_ID.equals(data.getString("type"))) return false;
        ResourceLocation barrelId = new ResourceLocation(data.getString("barrel"));
        if (!predicate.test(BuiltInRegistries.BLOCK.get(barrelId))) return false;
        int ticks = data.getInt("ticks");
        int years = ticks / level.getGameRules().getInt(BartendingGameRules.YEAR_LENGTH_TICKS);
        if (years == 0) return false;
        int diff = Math.abs(years - this.years);
        return diff <= yearLeeway;
    }

    @Override
    public int getDeviation(CompoundTag data, float standard, Level level) {
        int ticks = data.getInt("ticks");
        int years = ticks / level.getGameRules().getInt(BartendingGameRules.YEAR_LENGTH_TICKS);
        return Math.round(Mth.clampedMap(years, this.years - yearLeeway,
                this.years + yearLeeway, -0.25f, 0.25f) * standard);
    }

    @Override
    public void createExactMatchData(CompoundTag tag, Level level) {
        tag.putString("barrel", BuiltInRegistries.BLOCK.getKey(predicate.first()).toString());
        tag.putInt("ticks", level.getGameRules().getInt(BartendingGameRules.YEAR_LENGTH_TICKS) * years);
    }

    public static void appendInProgressText(CompoundTag data, List<Component> tooltips, Level level) {
        int ticks = data.getInt("ticks");
        Block barrel = BuiltInRegistries.BLOCK.get(new ResourceLocation(data.getString("barrel")));
        tooltips.add(Component.translatable("tooltip.bartending.fermenting_in").append(Component.translatable(barrel.getDescriptionId()))
                .withStyle(ChatFormatting.GRAY));
        if (level == null) return;
        String time = "" + (ticks / level.getGameRules().getInt(BartendingGameRules.YEAR_LENGTH_TICKS));
        tooltips.add(Component.translatable("tooltip.bartending.fermenting_for", time).withStyle(ChatFormatting.GRAY));
    }

}
