package ml.pluto7073.bartending.foundations.step;

import net.minecraft.nbt.CompoundTag;

public interface BrewerStep {

    /**
     * Whether the <code>data</code> specified can qualify for this step
     * @param data The <code>CompoundTag</code> that represents this step in the brewing process
     * @return <code>true</code> if the <code>data</code> falls within acceptable guidelines<br>
     * <code>false</code> if the data does not
     */
    boolean matches(CompoundTag data);

    /**
     * Finds the effect on alcohol content that a specific step in the brewing process has
     * @param data The <code>CompoundTag</code> that represents this step
     * @param standard The standard amount of alcohol in a perfectly crafted drink
     * @return The amount of Alcohol in grams that will be added or removed from the drink
     * once completed based on how close to the desired requirements of this step the brewer was
     */
    int getDeviation(CompoundTag data, float standard);

    void createExactMatchData(CompoundTag tag);

    default boolean mightMatch(CompoundTag data) {
        return matches(data);
    }

    String id();

}
