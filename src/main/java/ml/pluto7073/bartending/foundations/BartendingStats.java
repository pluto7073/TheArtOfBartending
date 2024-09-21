package ml.pluto7073.bartending.foundations;

import ml.pluto7073.bartending.TheArtOfBartending;
import ml.pluto7073.bartending.foundations.alcohol.AlcoholHandler;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.Stat;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;

public enum BartendingStats {

    CONSUME_ALCOHOL("Amount of Alcohol Consumed", AlcoholHandler.ALCOHOL_FORMATTER),
    ALCOHOL_BOTTLES_DRANK("Alcohol Bottles Emptied");

    private final ResourceLocation stat;
    private final String name;
    private final StatFormatter formatter;

    BartendingStats(String name) {
        this(name, StatFormatter.DEFAULT);
    }

    BartendingStats(String name, StatFormatter formatter) {
        stat = TheArtOfBartending.asId(name().toLowerCase());
        this.name = name;
        this.formatter = formatter;
    }

    public ResourceLocation get() {
        return stat;
    }

    public Stat<ResourceLocation> getStat() {
        return Stats.CUSTOM.get(stat);
    }

    public String getName() {
        return name;
    }

    public void register() {
        Registry.register(BuiltInRegistries.CUSTOM_STAT, stat.getPath(), stat);
        Stats.CUSTOM.get(stat, formatter);
    }

    private static String convertToId(String name) {
        return name.toLowerCase().replace(' ', '_');
    }

}
