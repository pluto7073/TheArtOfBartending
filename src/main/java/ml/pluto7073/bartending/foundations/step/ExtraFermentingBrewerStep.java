package ml.pluto7073.bartending.foundations.step;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public class ExtraFermentingBrewerStep implements BrewerStep {

    public static final String TYPE_ID = "extra_fermenting";

    private final int wantedTicks;
    private final BarrelPredicate barrel;
    private final Supplier<Item> baseItem;

    public ExtraFermentingBrewerStep(int wantedTicks, BarrelPredicate barrel,  Supplier<Item> baseItem) {
        this.wantedTicks = wantedTicks;
        this.barrel = barrel;
        this.baseItem = baseItem;
    }

    @Override
    public boolean matches(CompoundTag data) {
        ResourceLocation barrelId = new ResourceLocation(data.getString("barrel"));
        if (!barrel.test(BuiltInRegistries.BLOCK.get(barrelId))) return false;
        int ticks = data.getInt("ticks");
        return ticks >= wantedTicks;
    }

    @Override
    public int getDeviation(CompoundTag data, float standard) {
        int ticks = data.getInt("ticks");
        return (int) ((1 - Math.exp(-(ticks - wantedTicks) / (4f * wantedTicks))) * standard);
    }

    @Override
    public void createExactMatchData(CompoundTag tag) {}

    public boolean testItem(ItemStack stack) {
        return stack.is(baseItem.get());
    }

    @Override
    public String id() {
        return TYPE_ID;
    }
}
