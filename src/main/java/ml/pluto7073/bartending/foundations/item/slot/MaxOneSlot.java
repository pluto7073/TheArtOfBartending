package ml.pluto7073.bartending.foundations.item.slot;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;

public class MaxOneSlot extends Slot {

    public MaxOneSlot(Container container, int slot, int x, int y) {
        super(container, slot, x, y);
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }
}