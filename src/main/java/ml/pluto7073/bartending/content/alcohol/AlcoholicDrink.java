package ml.pluto7073.bartending.content.alcohol;

import ml.pluto7073.bartending.foundations.step.BrewerStep;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;

public class AlcoholicDrink {

    protected final BrewerStep[] steps;
    protected final int standardProof;
    protected final Item result;

    protected AlcoholicDrink(BrewerStep[] steps, int standardProof, Item result) {
        this.steps = steps;
        this.standardProof = standardProof;
        this.result = result;
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

    public static class Builder {

        private final List<BrewerStep> steps = new ArrayList<>();
        private int standardProof = 0;
        private Item result = Items.AIR;

        public Builder addStep(BrewerStep step) {
            steps.add(step);
            return this;
        }

        public Builder proof(int proof) {
            standardProof = proof;
            return this;
        }

        public Builder result(Item result) {
            this.result = result;
            return this;
        }

        public AlcoholicDrink build() {
            return new AlcoholicDrink(steps.toArray(new BrewerStep[0]), standardProof, result);
        }

    }

}
