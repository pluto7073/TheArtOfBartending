package ml.pluto7073.bartending.content.block.entity;

import ml.pluto7073.bartending.content.block.DistilleryBlock;
import ml.pluto7073.bartending.content.gui.DistilleryMenu;
import ml.pluto7073.bartending.content.item.BartendingItems;
import ml.pluto7073.bartending.foundations.step.DistillingBrewerStep;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

@MethodsReturnNonnullByDefault
public class DistilleryBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer {

    public static final int INPUT_SLOT_INDEX = 0,
    FUEL_SLOT_INDEX = 1,
    RESULT_SLOT_INDEX = 2,
    INVENTORY_SIZE = 3;

    public static final int DATA_LIT_TIME = 0,
    DATA_MAX_LIT_TIME = 1,
    DATA_DISTILL_TIME = 2,
    DATA_COUNT = 3;

    public static final int[] TOP_SLOTS = { INPUT_SLOT_INDEX, RESULT_SLOT_INDEX },
    SIDE_SLOTS = { FUEL_SLOT_INDEX, INPUT_SLOT_INDEX },
    BOTTOM_SLOTS = { INPUT_SLOT_INDEX, RESULT_SLOT_INDEX };

    public static final int MAX_DISTILL_TIME = 600;

    protected NonNullList<ItemStack> items = NonNullList.withSize(INVENTORY_SIZE, ItemStack.EMPTY);
    protected final ContainerData data = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case DATA_LIT_TIME -> litTime;
                case DATA_MAX_LIT_TIME -> maxLitTime;
                case DATA_DISTILL_TIME -> distillTime;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case DATA_LIT_TIME -> litTime = value;
                case DATA_MAX_LIT_TIME -> maxLitTime = value;
                case DATA_DISTILL_TIME -> distillTime = value;
            }
        }

        @Override
        public int getCount() {
            return DATA_COUNT;
        }
    };

    public int litTime, maxLitTime, distillTime;

    public DistilleryBlockEntity(BlockPos pos, BlockState state) {
        super(BartendingBlockEntities.DISTILLERY_BLOCK_ENTITY_TYPE, pos, state);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        items = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, items);
        litTime = tag.getShort("BurnTime");
        distillTime = tag.getShort("DistillTime");
        maxLitTime = getBurnDuration(items.get(1));
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putShort("BurnTime", (short) litTime);
        tag.putShort("DistillTime", (short) distillTime);
        ContainerHelper.saveAllItems(tag, items);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.distillery");
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return new DistilleryMenu(containerId, inventory, this, data);
    }

    protected static int getBurnDuration(ItemStack fuel) {
        if (fuel.isEmpty()) {
            return 0;
        }
        Item item = fuel.getItem();
        return AbstractFurnaceBlockEntity.getFuel().getOrDefault(item, 0);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, DistilleryBlockEntity entity) {
        boolean litStartTick = entity.litTime > 0;
        boolean changed = false;
        if (litStartTick) {
            entity.litTime--;
        }
        ItemStack fuel = entity.getItem(FUEL_SLOT_INDEX);
        int tempMaxFuelTime = getBurnDuration(fuel);
        ItemStack input = entity.getItem(INPUT_SLOT_INDEX);
        boolean hasInput = !input.isEmpty();
        boolean lastTickInput = state.getValue(DistilleryBlock.HAS_INPUT);
        if (hasInput != lastTickInput) {
            level.setBlock(pos, state = state.setValue(DistilleryBlock.HAS_INPUT, hasInput), 1 | 2);
            changed = true;
        }
        ItemStack output = entity.getItem(RESULT_SLOT_INDEX);
        boolean hasOutput = !output.isEmpty();
        boolean lastTickOutput = state.getValue(DistilleryBlock.HAS_OUTPUT);
        if (hasOutput != lastTickOutput) {
            level.setBlock(pos, state = state.setValue(DistilleryBlock.HAS_OUTPUT, hasOutput), 1 | 2);
            changed = true;
        }
        int lastTickState = state.getValue(DistilleryBlock.DISTILL_STATE);
        int newState = lastTickState;
        if (litStartTick || (output.is(Items.GLASS_BOTTLE) && input.is(BartendingItems.CONCOCTION) && tempMaxFuelTime > 0)) {
            if (!litStartTick) {
                changed = true;
                entity.maxLitTime = tempMaxFuelTime;
                entity.litTime = tempMaxFuelTime;
                fuel.shrink(1);
                if (fuel.isEmpty()) {
                    Item remainder = fuel.getItem().getCraftingRemainingItem();
                    entity.setItem(FUEL_SLOT_INDEX, remainder == null ? ItemStack.EMPTY : new ItemStack(remainder));
                }
                level.setBlock(pos, state = state.setValue(DistilleryBlock.HEATED, true), 1 | 2);
            }
            if (output.is(Items.GLASS_BOTTLE) && input.is(BartendingItems.CONCOCTION)) {
                entity.distillTime++;
                newState = 2;
                if (entity.distillTime >= MAX_DISTILL_TIME) {
                    changed = true;
                    entity.distillTime = 0;
                    ItemStack newOutput = input.copy();
                    ListTag steps = newOutput.getOrCreateTag().getList("BrewingSteps", ListTag.TAG_COMPOUND);
                    CompoundTag distilled = new CompoundTag();
                    distilled.putString("type", DistillingBrewerStep.TYPE_ID);
                    steps.add(distilled);
                    entity.setItem(INPUT_SLOT_INDEX, new ItemStack(Items.GLASS_BOTTLE));
                    entity.setItem(RESULT_SLOT_INDEX, newOutput);
                    newState = 3;
                }
            } else {
                newState = 0;
                entity.distillTime = 0;
            }
        } else if (!output.is(Items.GLASS_BOTTLE) || !output.is(BartendingItems.CONCOCTION)) {
            entity.distillTime = 0;
        } else {
            newState = 1;
        }

        if (newState != lastTickState) {
            level.setBlock(pos, state = state.setValue(DistilleryBlock.DISTILL_STATE, newState), 1 | 2);
            changed = true;
        }
        if (litStartTick != entity.litTime > 0) changed = true;
        if (changed) setChanged(level, pos, state);
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        if (side == Direction.DOWN) {
            return BOTTOM_SLOTS;
        }
        if (side == Direction.UP) {
            return TOP_SLOTS;
        }
        return SIDE_SLOTS;
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack itemStack, @Nullable Direction direction) {
        return this.canPlaceItem(index, itemStack);
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        if (direction == Direction.DOWN && index == FUEL_SLOT_INDEX) {
            return stack.is(Items.BUCKET);
        }
        return true;
    }

    @Override
    public int getContainerSize() {
        return INVENTORY_SIZE;
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack itemStack : this.items) {
            if (itemStack.isEmpty()) continue;
            return false;
        }
        return true;
    }

    @Override
    public ItemStack getItem(int slot) {
        return this.items.get(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        return ContainerHelper.removeItem(this.items, slot, amount);
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return ContainerHelper.takeItem(this.items, slot);
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        ItemStack itemStack = items.get(slot);
        boolean bl = !stack.isEmpty() && ItemStack.isSameItemSameTags(itemStack, stack);
        items.set(slot, stack);
        if (stack.getCount() > getMaxStackSize()) {
            stack.setCount(getMaxStackSize());
        }
        if (slot == INPUT_SLOT_INDEX && !bl) {
            distillTime = 0;
            setChanged();
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    @Override
    public boolean canPlaceItem(int index, ItemStack stack) {
        if (index == RESULT_SLOT_INDEX) {
            return stack.is(Items.GLASS_BOTTLE);
        }
        if (index == FUEL_SLOT_INDEX) {
            return AbstractFurnaceBlockEntity.isFuel(stack);
        }
        return stack.is(BartendingItems.CONCOCTION);
    }

    @Override
    public void clearContent() {
        this.items.clear();
    }

}
