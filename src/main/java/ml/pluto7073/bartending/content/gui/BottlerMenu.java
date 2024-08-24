package ml.pluto7073.bartending.content.gui;

import ml.pluto7073.bartending.content.item.TAOBItems;
import ml.pluto7073.bartending.foundations.item.slot.DisplaySlot;
import ml.pluto7073.bartending.foundations.item.slot.MaxOneSlot;
import ml.pluto7073.bartending.foundations.item.slot.NoInsertMaxOne;
import ml.pluto7073.bartending.foundations.tags.TAOBTags;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import static ml.pluto7073.bartending.content.block.entity.BottlerBlockEntity.*;

@MethodsReturnNonnullByDefault
public class BottlerMenu extends AbstractContainerMenu {

    private final Container container;
    private final ContainerData data;
    protected final Level level;

    public BottlerMenu(int containerId, Inventory inventory) {
        this(containerId, inventory, new SimpleContainer(CONTAINER_SIZE), new SimpleContainerData(DATA_COUNT));
    }

    public BottlerMenu(int containerId, Inventory inventory, Container container, ContainerData data) {
        super(TAOBMenuTypes.BOTTLER_MENU_TYPE, containerId);
        checkContainerSize(container, CONTAINER_SIZE);
        checkContainerDataCount(data, DATA_COUNT);
        this.container = container;
        this.data = data;
        this.level = inventory.player.level();
        this.addSlot(new MaxOneSlot(container, CONCOCTION_INPUT_SLOT, 54, 27));
        this.addSlot(new DisplaySlot(container, RESULT_DISPLAY_SLOT, 103, 32));
        this.addSlot(new Slot(container, BOTTLE_INSERT_SLOT, 69, 55));
        this.addSlot(new NoInsertMaxOne(container, RESULT_SLOT, 103, 55));

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(inventory, i, 8 + i * 18, 142));
        }

        this.addDataSlots(data);
    }

    public int bottleTime() {
        return data.get(BOTTLE_TIME_DATA);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = slots.get(index);
        if (slot.hasItem()) {
            ItemStack itemStack2 = slot.getItem();
            itemStack = itemStack2.copy();
            if (index < 0 || index > CONTAINER_SIZE - 1) {
                if (slots.get(CONCOCTION_INPUT_SLOT).mayPlace(itemStack2) && itemStack2.is(TAOBItems.CONCOCTION)) {
                    if (!this.moveItemStackTo(itemStack2, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (slots.get(BOTTLE_INSERT_SLOT).mayPlace(itemStack2) && itemStack2.is(TAOBTags.EMPTY_GLASS_BOTTLES)) {
                    if (!this.moveItemStackTo(itemStack2, 2, 3, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 4 && index < 31) {
                    if (!this.moveItemStackTo(itemStack2, 31, 40, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 31 && index < 40) {
                    if (!this.moveItemStackTo(itemStack2, 4, 31, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.moveItemStackTo(itemStack2, 4, 40, false)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (!this.moveItemStackTo(itemStack2, 4, 40, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(itemStack2, itemStack);
            }

            if (itemStack2.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemStack2.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemStack2);
        }

        return itemStack;
    }

    @Override
    public boolean stillValid(Player player) {
        return container.stillValid(player);
    }

}
