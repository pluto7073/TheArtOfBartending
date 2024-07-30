package ml.pluto7073.bartending.content.block.entity;

import ml.pluto7073.bartending.content.gui.BoilerMenu;
import ml.pluto7073.bartending.content.tags.TAOBlockTags;
import ml.pluto7073.bartending.foundations.BrewingUtil;
import ml.pluto7073.bartending.foundations.water.ValidWaterSources;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

@MethodsReturnNonnullByDefault
public class BoilerBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer {

    public static final int WATER_INPUT_SLOT_INDEX = 0;
    public static final int ITEM_INPUT_SLOT_INDEX = 1;
    public static final int DISPLAY_RESULT_ITEM_SLOT_INDEX = 2;
    public static final int GLASS_BOTTLE_INSERT_SLOT_INDEX = 3;
    public static final int RESULT_SLOT_INDEX = 4;
    public static final int INVENTORY_SIZE = 5;

    public static final int WATER_AMOUNT_DATA = 0;
    public static final int BOIL_TIME_DATA = 1;
    public static final int HEATED_DATA = 2;
    public static final int DATA_COUNT = 3;

    public static final int[] TOP_SLOTS = { 1, 3 };
    public static final int[] SIDE_SLOTS = { 0, 1 };
    public static final int[] BOTTOM_SLOTS = { 0, 3 };

    public final ContainerData data;

    public NonNullList<ItemStack> inventory;
    public int waterInMB;
    public int boilTicks;

    private Item prevTickItem;
    private int prevTickCount;

    public BoilerBlockEntity(BlockPos pos, BlockState blockState) {
        super(TAOBBlockEntities.BOILER_BLOCK_ENTITY_TYPE, pos, blockState);
        this.inventory = NonNullList.withSize(INVENTORY_SIZE, ItemStack.EMPTY);
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case WATER_AMOUNT_DATA -> waterInMB;
                    case BOIL_TIME_DATA -> boilTicks;
                    case HEATED_DATA -> isHeated(getBlockPos()) ? 1 : 0;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case WATER_AMOUNT_DATA -> waterInMB = value;
                    case BOIL_TIME_DATA -> boilTicks = value;
                }
            }

            @Override
            public int getCount() {
                return DATA_COUNT;
            }
        };
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        boilTicks = tag.getInt("BoilTicks");
        waterInMB = tag.getInt("WaterAmount");
        ContainerHelper.loadAllItems(tag, inventory);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("BoilTicks", boilTicks);
        tag.putInt("WaterAmount", (short) waterInMB);
        ContainerHelper.saveAllItems(tag, inventory);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, BoilerBlockEntity entity) {
        // Ticking Water Refill
        ItemStack waterStack = entity.getItem(WATER_INPUT_SLOT_INDEX);
        int water;
        if ((water = ValidWaterSources.getAmountFromItem(waterStack)) > 0) {
            if (entity.waterInMB <= (1000 - water) || entity.waterInMB < 25) {
                entity.waterInMB += water;
                if (entity.waterInMB > 1000) entity.waterInMB = 1000;
                waterStack = new ItemStack(waterStack.getItem().asItem(), 1);
            }
        }
        entity.inventory.set(WATER_INPUT_SLOT_INDEX, waterStack);

        // Testing Boiling

        if (!entity.isBoiling(pos)) {
            entity.inventory.set(DISPLAY_RESULT_ITEM_SLOT_INDEX, ItemStack.EMPTY);
            return;
        }

        ItemStack itemInput = entity.getItem(ITEM_INPUT_SLOT_INDEX);

        if (itemInput.isEmpty()) {
            entity.boilTicks = 0;
            entity.prevTickItem = Items.AIR;
            entity.prevTickCount = 0;
            entity.inventory.set(DISPLAY_RESULT_ITEM_SLOT_INDEX, PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WATER));
            return;
        }

        if (itemInput.getCount() != entity.prevTickCount) {
            entity.boilTicks = 0;
            entity.prevTickCount = itemInput.getCount();
        }

        if (itemInput.getItem() != entity.prevTickItem) {
            entity.boilTicks = 0;
            entity.prevTickItem = itemInput.getItem();
        }

        ItemStack display = BrewingUtil.createConcoctionFromBaseTicks(itemInput, entity.boilTicks);

        entity.inventory.set(DISPLAY_RESULT_ITEM_SLOT_INDEX, display);

        entity.boilTicks++;

        ItemStack glassBottle = entity.getItem(GLASS_BOTTLE_INSERT_SLOT_INDEX);

        if (glassBottle.isEmpty()) return;
        if (glassBottle.is(Items.GLASS_BOTTLE) && entity.waterInMB >= 250) {
            glassBottle = ItemStack.EMPTY;
            entity.waterInMB -= 250;
        }

        entity.inventory.set(GLASS_BOTTLE_INSERT_SLOT_INDEX, glassBottle);
        entity.inventory.set(RESULT_SLOT_INDEX, display.copy());
        entity.inventory.set(ITEM_INPUT_SLOT_INDEX, ItemStack.EMPTY);

        setChanged(level, pos, state);
    }

    public boolean isHeated(BlockPos pos) {
        // If heated from below
        BlockState below = level.getBlockState(pos.below());
        boolean heated = below.is(BlockTags.CAMPFIRES);
        heated = heated || below.is(BlockTags.FIRE);
        heated = heated || below.is(Blocks.LAVA);
        return heated || below.is(TAOBlockTags.EXTRA_BOILER_HEATERS);
    }

    public boolean isBoiling(BlockPos pos) {
        // If water
        return waterInMB > 0 && isHeated(pos);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.boiler");
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return new BoilerMenu(containerId, inventory, this, this.data);
    }

    @Override
    public int getContainerSize() {
        return INVENTORY_SIZE;
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : inventory) {
            if (!stack.isEmpty()) return false;
        }
        return true;
    }

    @Override
    public ItemStack getItem(int slot) {
        return inventory.get(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        if (slot == DISPLAY_RESULT_ITEM_SLOT_INDEX) return ItemStack.EMPTY;
        return ContainerHelper.removeItem(this.inventory, slot, amount);
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return ContainerHelper.takeItem(this.inventory, slot);
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        ItemStack s = this.inventory.get(slot);
        boolean canInsert = !stack.isEmpty() && stack.getItem().equals(s.getItem()) && ItemStack.isSameItemSameTags(s, stack);
        this.inventory.set(slot, stack);
        if (stack.getCount() > this.getMaxStackSize()) {
            stack.setCount(this.getMaxStackSize());
        }
    }

    @Override
    public boolean stillValid(Player player) {
        if (level.getBlockEntity(worldPosition) != this) {
            return false;
        } else {
            return player.distanceToSqr(worldPosition.getX() + 0.5, worldPosition.getY() + 0.5,
                    worldPosition.getZ() + 0.5) <= 64.0;
        }
    }

    @Override
    public void clearContent() {
        this.inventory.clear();
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return switch (side) {
            case DOWN -> BOTTOM_SLOTS;
            case UP -> TOP_SLOTS;
            default -> SIDE_SLOTS;
        };
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack itemStack, @Nullable Direction direction) {
        return isValid(index, itemStack);
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction direction) {
        if (direction == Direction.DOWN && slot == WATER_INPUT_SLOT_INDEX) {
            return stack.is(Items.GLASS_BOTTLE) || stack.is(Items.BUCKET);
        } else {
            return true;
        }
    }

    public boolean isValid(int slot, ItemStack stack) {
        return switch (slot) {
            case WATER_INPUT_SLOT_INDEX -> ValidWaterSources.getAmountFromItem(stack) > 0;
            case ITEM_INPUT_SLOT_INDEX -> true;
            case DISPLAY_RESULT_ITEM_SLOT_INDEX -> false;
            case GLASS_BOTTLE_INSERT_SLOT_INDEX -> stack.is(Items.GLASS_BOTTLE);
            default -> false;
        };
    }

}
