package ml.pluto7073.bartending.foundations.alcohol;

import ml.pluto7073.bartending.content.fluid.BartendingFluids;
import ml.pluto7073.bartending.foundations.BrewingUtil;
import ml.pluto7073.bartending.foundations.fluid.FluidHolder;
import ml.pluto7073.bartending.foundations.step.BrewerStep;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiConsumer;

public class AlcoholicDrink {

    private final BrewerStep[] steps;
    private final int standardProof;
    private final float standardOunces;
    private final int color;
    private final Item bottle;
    @Nullable private final FluidHolder fluid;
    private final HashMap<Item, Integer> itemToAmountMap;

    public AlcoholicDrink(BrewerStep[] steps, int standardProof, float standardOunces, int color, Item bottle, @Nullable FluidHolder fluid) {
        this.steps = steps;
        this.standardProof = standardProof;
        this.standardOunces = standardOunces;
        this.color = color;
        this.bottle = bottle;
        this.fluid = fluid;
        itemToAmountMap = new HashMap<>();
    }

    public void addItem(Item item, int mb) {
        itemToAmountMap.put(item, mb);
    }

    public void forEach(BiConsumer<Item, Integer> consumer) {
        itemToAmountMap.forEach(consumer);
    }

    public BrewerStep[] steps() {
        return steps;
    }

    public int standardProof() {
        return standardProof;
    }

    public float standardOunces() {
        return standardOunces;
    }

    public int color() {
        return color;
    }

    public Item bottle() {
        return bottle;
    }

    @Nullable
    public FluidHolder fluid() {
        return fluid;
    }

    public boolean matches(ListTag steps) {
        if (steps.size() != this.steps.length) return false;

        for (int i = 0; i < steps.size(); i++) {
            BrewerStep step = this.steps[i];
            CompoundTag data = steps.getCompound(i);
            if (!step.matches(data)) return false;
        }

        return true;
    }

    /**
     * Returns the total deviation in grams of alcohol from the standard recipe.
     * This should only be performed on a list of steps that is known to match this Alcoholic drink, as this method does not check whether the list of steps is correct or not
     * @param steps The <code>ListTag</code> of steps from the concoction
     * @return The amount of alcohol in grams to be added or removed from the standard amount
     */
    public int getTotalDeviation(ListTag steps) {
        int deviation = 0;
        float standard = BrewingUtil.getStandardAlcohol(this);
        for (int i = 0; i < steps.size(); i++) {
            BrewerStep step = this.steps[i];
            CompoundTag data = steps.getCompound(i);
            deviation += step.getDeviation(data, standard);
        }
        return deviation;
    }

    public static class Builder {

        private final List<BrewerStep> steps = new ArrayList<>();
        private int standardProof = 0, color = 0xFFFFFF;
        private float standardOunces = 0f;
        private Item bottle = Items.GLASS_BOTTLE;
        private FluidHolder fluid;

        public Builder addStep(BrewerStep step) {
            steps.add(step);
            return this;
        }

        public Builder proof(int proof) {
            standardProof = proof;
            return this;
        }

        public Builder ounces(float ounces) {
            standardOunces = ounces;
            return this;
        }

        public Builder color(int color) {
            this.color = color;
            return this;
        }

        public Builder bottle(Item bottle) {
            this.bottle = bottle;
            return this;
        }

        public Builder fluid(FluidHolder fluid) {
            this.fluid = fluid;
            return this;
        }

        public AlcoholicDrink build() {
            return new AlcoholicDrink(steps.toArray(new BrewerStep[0]), standardProof, standardOunces, color, bottle, fluid);
        }

    }

}
