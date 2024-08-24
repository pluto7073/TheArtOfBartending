package ml.pluto7073.bartending.foundations.item.slot;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

public class NoInsertMaxOne extends MaxOneSlot {

    public NoInsertMaxOne(Container container, int slot, int x, int y) {
        super(container, slot, x, y);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return false;
    }
}
