package ml.pluto7073.bartending.foundations.item.slot;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;

public class DisplaySlot extends MaxOneSlot {

    public DisplaySlot(Container container, int slot, int x, int y) {
        super(container, slot, x, y);
    }

    @Override
    public boolean mayPickup(Player player) {
        return false;
    }

    @Override
    public boolean allowModification(Player player) {
        return false;
    }

    @Override
    public int getMaxStackSize() {
        return 256;
    }
}
