package ml.pluto7073.bartending.content.gui;

import ml.pluto7073.bartending.content.block.entity.BoilerBlockEntity;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import static ml.pluto7073.bartending.content.block.entity.BoilerBlockEntity.DATA_COUNT;
import static ml.pluto7073.bartending.content.block.entity.BoilerBlockEntity.INVENTORY_SIZE;

public class BoilerMenu extends AbstractContainerMenu {

    private final Container container;
    private final ContainerData data;
    protected final Level level;

    public BoilerMenu(int containerId, Inventory inventory) {
        this(containerId, inventory, new SimpleContainer(INVENTORY_SIZE), new SimpleContainerData(DATA_COUNT));
    }

    public BoilerMenu(int containerId, Inventory inventory, Container container, ContainerData data) {
        super(TAOBMenuTypes.BOILER_MENU_TYPE, containerId);
        checkContainerSize(container, INVENTORY_SIZE);
        checkContainerDataCount(data, DATA_COUNT);
        this.container = container;
        this.data = data;
        this.level = inventory.player.level();
        class MaxOneSlot extends Slot {

            public MaxOneSlot(Container container, int slot, int x, int y) {
                super(container, slot, x, y);
            }

            @Override
            public int getMaxStackSize() {
                return 1;
            }
        }
        class NoInsertMaxOne extends MaxOneSlot {

            public NoInsertMaxOne(Container container, int slot, int x, int y) {
                super(container, slot, x, y);
            }

            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }
        }
        class DisplaySlot extends MaxOneSlot {

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
        }
        this.addSlot(new MaxOneSlot(container, BoilerBlockEntity.WATER_INPUT_SLOT_INDEX, 48, 42));
        this.addSlot(new Slot(container, BoilerBlockEntity.ITEM_INPUT_SLOT_INDEX, 48, 17));
        this.addSlot(new DisplaySlot(container, BoilerBlockEntity.DISPLAY_RESULT_ITEM_SLOT_INDEX, 124, 28));
        this.addSlot(new MaxOneSlot(container, BoilerBlockEntity.GLASS_BOTTLE_INSERT_SLOT_INDEX, 92, 55));
        this.addSlot(new NoInsertMaxOne(container, BoilerBlockEntity.RESULT_SLOT_INDEX, 124, 55));

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
        return data.get(BoilerBlockEntity.WATER_AMOUNT_DATA);
    }

    public int getBoilTime() {
        return data.get(BoilerBlockEntity.BOIL_TIME_DATA);
    }

    public boolean isHeated() {
        return data.get(BoilerBlockEntity.HEATED_DATA) == 1;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return null;
    }

    @Override
    public boolean stillValid(Player player) {
        return container.stillValid(player);
    }
}
