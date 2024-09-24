package ml.pluto7073.bartending.foundations.step;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.GsonHelper;

import java.util.List;

public class DistillingBrewerStep implements BrewerStep {

    public static final String TYPE_ID = "distilling";

    public final int runs, leeway;

    public DistillingBrewerStep() {
        this(1);
    }

    public DistillingBrewerStep(int runs) {
        this(runs, 0);
    }

    public DistillingBrewerStep(int runs, int leeway) {
        this.runs = runs;
        this.leeway = leeway;
    }

    @Override
    public boolean mightMatch(CompoundTag data) {
        if (!TYPE_ID.equals(data.getString("type"))) return false;
        int runs = data.getInt("runs");
        return runs <= this.runs + leeway;
    }

    @Override
    public boolean matches(CompoundTag data) {
        if (!TYPE_ID.equals(data.getString("type"))) return false;
        int runs = data.getInt("runs");
        return runs <= this.runs + leeway && runs >= this.runs - leeway;
    }

    @Override
    public int getDeviation(CompoundTag data, float standard) {
        int runs = data.getInt("runs");
        return 4 * (runs - this.runs);
    }

    public static void appendInProgressText(CompoundTag data, List<Component> tooltips) {
        int runs = data.getInt("runs");
        switch (runs) {
            case 1 -> tooltips.add(Component.translatable("tooltip.bartending.distilled").withStyle(ChatFormatting.GRAY));
            case 2 -> tooltips.add(Component.translatable("tooltip.bartending.twice_distilled").withStyle(ChatFormatting.GRAY));
            case 3 -> tooltips.add(Component.translatable("tooltip.bartending.triple_distilled").withStyle(ChatFormatting.GRAY));
            default -> tooltips.add(Component.translatable("tooltip.bartending.x_distilled", runs).withStyle(ChatFormatting.GRAY));
        }
    }

}
