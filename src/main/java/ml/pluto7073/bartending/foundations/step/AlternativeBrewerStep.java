package ml.pluto7073.bartending.foundations.step;

import net.minecraft.nbt.CompoundTag;

public class AlternativeBrewerStep implements BrewerStep {
    @Override
    public boolean matches(CompoundTag data) {
        return false;
    }

    @Override
    public int getDeviation(CompoundTag data, float standard) {
        return 0;
    }

    @Override
    public void createExactMatchData(CompoundTag tag) {}

    @Override
    public String id() {
        return "";
    }

}
