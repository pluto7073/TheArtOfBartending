package ml.pluto7073.bartending.content.block.state.properties;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum DistilleryPart implements StringRepresentable {

    TOP("top"),
    BOTTOM("bottom");

    private final String name;

    DistilleryPart(String name) {
        this.name = name;
    }

    @Override
    public @NotNull String getSerializedName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
