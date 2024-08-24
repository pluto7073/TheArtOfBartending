package ml.pluto7073.bartending.foundations.step;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;

import java.util.List;

public class DistillingBrewerStep implements BrewerStep {

    public static final String TYPE_ID = "distilling";

    @Override
    public boolean matches(CompoundTag data) {
        return TYPE_ID.equals(data.getString("type"));
    }

    @Override
    public int getDeviation(CompoundTag data, float standard) {
        return 0;
    }

    public static void appendInProgressText(CompoundTag data, List<Component> tooltips) {
        tooltips.add(Component.translatable("tooltip.bartending.distilled").withStyle(ChatFormatting.GRAY));
    }

}
