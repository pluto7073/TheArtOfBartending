package ml.pluto7073.bartending.foundations.step;

import net.minecraft.nbt.CompoundTag;

public interface BrewerStep {

    boolean matches(CompoundTag data);

}
