package ml.pluto7073.bartending.foundations.alcohol;

import ml.pluto7073.bartending.foundations.BrewingUtil;
import ml.pluto7073.bartending.foundations.step.BrewerStep;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;

public record AlcoholicDrink(BrewerStep[] steps, int standardProof, float standardOunces, int color, Item bottle) {

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
     * @param steps The <code>ListTag</code> of steps from the concoction
     * @apiNote This should only be performed on a list of steps that is known to match this Alcoholic drink, as this method does not check whether the list of steps is correct or not
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
        private int standardProof = 0, color = 0;
        private float standardOunces = 0f;
        private Item bottle = Items.GLASS_BOTTLE;

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

        public AlcoholicDrink build() {
            return new AlcoholicDrink(steps.toArray(new BrewerStep[0]), standardProof, standardOunces, color, bottle);
        }

    }

}
