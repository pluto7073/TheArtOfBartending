package ml.pluto7073.bartending.content.gui;

import ml.pluto7073.bartending.content.item.BartendingItems;
import ml.pluto7073.bartending.foundations.item.slot.MaxOneSlot;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;

import static ml.pluto7073.bartending.content.block.entity.DistilleryBlockEntity.*;

@MethodsReturnNonnullByDefault
public class DistilleryMenu extends AbstractContainerMenu {

    private final Container container;
    private final ContainerData data;
    protected final Level level;

    public DistilleryMenu(int containerId, Inventory inventory) {
        this(containerId, inventory, new SimpleContainer(INVENTORY_SIZE), new SimpleContainerData(DATA_COUNT));
    }

    public DistilleryMenu(int containerId, Inventory inventory, Container container, ContainerData data) {
        super(BartendingMenuTypes.DISTILLERY_MENU_TYPE, containerId);
        checkContainerSize(container, INVENTORY_SIZE);
        checkContainerDataCount(data, DATA_COUNT);
        this.container = container;
        this.data = data;
        this.level = inventory.player.level();
        addSlot(new MaxOneSlot(container, INPUT_SLOT_INDEX, 56, 17));
        addSlot(new Slot(container, FUEL_SLOT_INDEX, 56, 53));
        addSlot(new MaxOneSlot(container, RESULT_SLOT_INDEX, 116, 35));

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

    public int getLitProgress() {
        int i = this.data.get(DATA_MAX_LIT_TIME);
        if (i == 0) {
            i = 200;
        }
        return this.data.get(DATA_LIT_TIME) * 13 / i;
    }

    public int getDistillProgress() {
        int i = this.data.get(DATA_DISTILL_TIME);
        if (i == 0) {
            return 0;
        }
        return i * 24 / MAX_DISTILL_TIME;
    }

    public int getFuelTime() {
        return data.get(DATA_LIT_TIME);
    }

    protected boolean isFuel(ItemStack stack) {
        return AbstractFurnaceBlockEntity.isFuel(stack);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack itemStack2 = slot.getItem();
            itemStack = itemStack2.copy();
            if (index == 2) {
                if (!this.moveItemStackTo(itemStack2, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(itemStack2, itemStack);
            } else if (index == 1 || index == 0 ? !this.moveItemStackTo(itemStack2, 3, 39, false) :
                    (itemStack2.is(BartendingItems.CONCOCTION) ? !this.moveItemStackTo(itemStack2, 0, 1, false) :
                            (this.isFuel(itemStack2) ? !this.moveItemStackTo(itemStack2, 1, 2, false) :
                                    (index >= 3 && index < 30 ? !this.moveItemStackTo(itemStack2, 30, 39, false) :
                                            index >= 30 && index < 39 && !this.moveItemStackTo(itemStack2, 3, 30, false))))) {
                return ItemStack.EMPTY;
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
