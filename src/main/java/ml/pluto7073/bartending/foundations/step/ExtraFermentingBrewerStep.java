package ml.pluto7073.bartending.foundations.step;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public class ExtraFermentingBrewerStep implements BrewerStep {

    public static final String TYPE_ID = "extra_fermenting";

    private final FermentingBrewerStep baseStep;
    private final Supplier<Item> baseItem;

    public ExtraFermentingBrewerStep(FermentingBrewerStep baseStep, Supplier<Item> baseItem) {
        this.baseStep = baseStep;
        this.baseItem = baseItem;
    }

    @Override
    public boolean matches(CompoundTag data) {
        return baseStep.matches(data);
    }

    @Override
    public int getDeviation(CompoundTag data, float standard) {
        return baseStep.getDeviation(data, standard);
    }

    @Override
    public void createExactMatchData(CompoundTag tag) {
        baseStep.createExactMatchData(tag);
    }

    public boolean testItem(ItemStack stack) {
        return stack.is(baseItem.get());
    }

    @Override
    public String id() {
        return TYPE_ID;
    }
}
