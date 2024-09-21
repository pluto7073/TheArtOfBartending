package ml.pluto7073.bartending.content.gui;

import ml.pluto7073.bartending.content.block.entity.BoilerBlockEntity;
import ml.pluto7073.bartending.foundations.item.slot.DisplaySlot;
import ml.pluto7073.bartending.foundations.item.slot.MaxOneSlot;
import ml.pluto7073.bartending.foundations.item.slot.NoInsertMaxOne;
import ml.pluto7073.bartending.foundations.tags.BartendingTags;
import ml.pluto7073.bartending.foundations.water.ValidWaterSources;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

import javax.annotation.ParametersAreNonnullByDefault;

import static ml.pluto7073.bartending.content.block.entity.BoilerBlockEntity.*;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class BoilerMenu extends AbstractContainerMenu {

    private final Container container;
    private final ContainerData data;
    protected final Level level;

    public BoilerMenu(int containerId, Inventory inventory) {
        this(containerId, inventory, new SimpleContainer(INVENTORY_SIZE), new SimpleContainerData(DATA_COUNT));
    }

    public BoilerMenu(int containerId, Inventory inventory, Container container, ContainerData data) {
        super(BartendingMenuTypes.BOILER_MENU_TYPE, containerId);
        checkContainerSize(container, INVENTORY_SIZE);
        checkContainerDataCount(data, DATA_COUNT);
        this.container = container;
        this.data = data;
        this.level = inventory.player.level();
        this.addSlot(new MaxOneSlot(container, WATER_INPUT_SLOT_INDEX, 48, 42));
        this.addSlot(new Slot(container, ITEM_INPUT_SLOT_INDEX, 48, 17));
        this.addSlot(new DisplaySlot(container, DISPLAY_RESULT_ITEM_SLOT_INDEX, 124, 28));
        this.addSlot(new Slot(container, GLASS_BOTTLE_INSERT_SLOT_INDEX, 92, 55));
        this.addSlot(new NoInsertMaxOne(container, RESULT_SLOT_INDEX, 124, 55));

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

    public int getWater() {
        return data.get(WATER_AMOUNT_DATA);
    }

    public int getBoilTime() {
        return data.get(BOIL_TIME_DATA);
    }

    public boolean isSuperHeated() { return data.get(HEATED_DATA) == 2 || data.get(HEATED_DATA) == 3; }

    public boolean isHeated() {
        return data.get(HEATED_DATA) == 1;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            // 0-4 : Boiler
            // 5-40 : Inventory & Hotbar
            // 5-31 : Inv
            // 31-40 : Hotbar
            ItemStack itemStack2 = slot.getItem();
            itemStack = itemStack2.copy();
            if (index == RESULT_SLOT_INDEX) {
                if (!this.moveItemStackTo(itemStack2, 5, 41, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(itemStack2, itemStack);
            } else if (index == 0 || index == 1 || index == 3 ? !moveItemStackTo(itemStack2, 5, 41, false) :
                            itemStack2.is(BartendingTags.BOILABLES) ? !moveItemStackTo(itemStack2, 1, 2, false) :
                            ValidWaterSources.getAmountFromItem(itemStack2) > 0 ? !moveItemStackTo(itemStack2, 0, 1, false) :
                            itemStack2.is(Items.GLASS_BOTTLE) ? !moveItemStackTo(itemStack2, 3, 4, false) :
                            index >= 5 && index < 32 ? !moveItemStackTo(itemStack2, 32, 41, false) :
                            index >= 32 && index < 41 && !moveItemStackTo(itemStack2, 5, 32, false)) {
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
