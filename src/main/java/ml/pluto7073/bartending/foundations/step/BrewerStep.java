package ml.pluto7073.bartending.foundations.step;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

import java.util.List;

public interface BrewerStep {

    /**
     * Whether the <code>data</code> specified can qualify for this step
     *
     * @param data  The <code>CompoundTag</code> that represents this step in the brewing process
     * @param level The current Level
     * @return <code>true</code> if the <code>data</code> falls within acceptable guidelines<br>
     * <code>false</code> if the data does not
     */
    boolean matches(CompoundTag data, Level level);

    /**
     * Finds the effect on alcohol content that a specific step in the brewing process has
     *
     * @param data     The <code>CompoundTag</code> that represents this step
     * @param standard The standard amount of alcohol in a perfectly crafted drink
     * @param level The current level
     * @return The amount of Alcohol in grams that will be added or removed from the drink
     * once completed based on how close to the desired requirements of this step the brewer was
     */
    int getDeviation(CompoundTag data, float standard, Level level);

    void createExactMatchData(CompoundTag tag, Level level);

    default boolean mightMatch(CompoundTag data, Level level) {
        return matches(data, level);
    }

    String id();

}
