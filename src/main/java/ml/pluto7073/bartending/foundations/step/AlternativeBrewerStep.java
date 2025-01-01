package ml.pluto7073.bartending.foundations.step;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

import java.util.List;

public class AlternativeBrewerStep implements BrewerStep {
    @Override
    public boolean matches(CompoundTag data, Level level) {
        return false;
    }

    @Override
    public int getDeviation(CompoundTag data, float standard, Level level) {
        return 0;
    }

    @Override
    public void createExactMatchData(CompoundTag tag, Level level) {}

    @Override
    public String id() {
        return "";
    }

}
