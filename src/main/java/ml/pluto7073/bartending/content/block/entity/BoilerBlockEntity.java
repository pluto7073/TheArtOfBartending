package ml.pluto7073.bartending.content.block.entity;

import ml.pluto7073.bartending.compat.create.TheArtOfCreate;
import ml.pluto7073.bartending.content.gui.BoilerMenu;
import ml.pluto7073.bartending.content.item.BartendingItems;
import ml.pluto7073.bartending.foundations.ColorUtil;
import ml.pluto7073.bartending.foundations.tags.BartendingTags;
import ml.pluto7073.bartending.foundations.BrewingUtil;
import ml.pluto7073.bartending.foundations.water.ValidWaterSources;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.item.ItemEntity;
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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.Hopper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.stream.Collectors;

@ParametersAreNonnullByDefault
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
    public static final int COLOR_DATA = 3;
    public static final int DATA_COUNT = 4;

    public static final int[] TOP_SLOTS = { 1, 3 };
    public static final int[] SIDE_SLOTS = { 0, 1, 4 };
    public static final int[] BOTTOM_SLOTS = { 0, 3, 4 };

    public final ContainerData data;

    private static final VoxelShape INSIDE = Block.box(2.0, 11.0, 2.0, 14.0, 16.0, 14.0);
    private static final VoxelShape ABOVE = Block.box(0.0, 16.0, 0.0, 16.0, 20.0, 16.0);
    private static final VoxelShape SUCK = Shapes.or(INSIDE, ABOVE);

    public NonNullList<ItemStack> inventory;
    public int waterInMB;
    public int boilTicks;

    private ItemStack currentBoilingStack;

    public BoilerBlockEntity(BlockPos pos, BlockState blockState) {
        super(BartendingBlockEntities.BOILER_BLOCK_ENTITY_TYPE, pos, blockState);
        this.inventory = NonNullList.withSize(INVENTORY_SIZE, ItemStack.EMPTY);
        currentBoilingStack = ItemStack.EMPTY;
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case WATER_AMOUNT_DATA -> waterInMB;
                    case BOIL_TIME_DATA -> boilTicks;
                    case HEATED_DATA -> getHeatedData();
                    case COLOR_DATA -> currentBoilingStack.isEmpty() ? 4210943 :
                        ColorUtil.get(BuiltInRegistries.ITEM.getKey(currentBoilingStack.getItem()).toString());
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
        currentBoilingStack = ItemStack.of(tag.getCompound("CurrentStack"));
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("BoilTicks", boilTicks);
        tag.putInt("WaterAmount", (short) waterInMB);
        CompoundTag current = new CompoundTag();
        currentBoilingStack.save(current);
        tag.put("CurrentStack", current);
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
                Item remaining = waterStack.getItem().getCraftingRemainingItem();
                entity.setItem(WATER_INPUT_SLOT_INDEX, remaining == null ? ItemStack.EMPTY : new ItemStack(remaining, 1));
            }
        }
        int maxConcoctions = entity.waterInMB / 250;

        List<ItemEntity> aboveItems = getItemsAtAndAbove(level, entity);

        ItemStack input = entity.getItem(ITEM_INPUT_SLOT_INDEX);

        for (ItemEntity item : aboveItems) {
            if ((item.getItem().is(entity.currentBoilingStack.getItem()) || entity.currentBoilingStack.isEmpty()) && entity.waterInMB >= 250) {
                if (input.isEmpty()) {
                    input = item.getItem().copy();
                } else {
                    input.grow(item.getItem().getCount());
                }
                item.discard();
            }
        }

        if (!input.isEmpty() && entity.waterInMB >= 250) {
            if (entity.currentBoilingStack.isEmpty()) {
                entity.currentBoilingStack = input.copy();
                entity.setItem(ITEM_INPUT_SLOT_INDEX, ItemStack.EMPTY);
                entity.boilTicks = 0;
            } else if (entity.currentBoilingStack.is(input.getItem())) {
                entity.currentBoilingStack.grow(input.getCount());
                entity.setItem(ITEM_INPUT_SLOT_INDEX, ItemStack.EMPTY);
                entity.boilTicks = 0;
            }
        }

        if (entity.isBoiling()) {
            entity.boilTicks += (int) Math.pow(2, entity.getHeatedData() - 1);
        }

        if (entity.currentBoilingStack.isEmpty()) {
            entity.setItem(DISPLAY_RESULT_ITEM_SLOT_INDEX, ItemStack.EMPTY);
        } else {
            ItemStack display = BrewingUtil.createConcoctionFromBaseTicks(entity.currentBoilingStack, entity.boilTicks);
            ItemStack currentDisplay = entity.getItem(DISPLAY_RESULT_ITEM_SLOT_INDEX);
            if (!currentDisplay.is(BartendingItems.CONCOCTION) || currentDisplay.getCount() > maxConcoctions) {
                display.setCount(maxConcoctions);
            } else display.setCount(currentDisplay.getCount());
            entity.setItem(DISPLAY_RESULT_ITEM_SLOT_INDEX, display);
        }

        ItemStack bottle = entity.getItem(GLASS_BOTTLE_INSERT_SLOT_INDEX);
        ItemStack display = entity.getItem(DISPLAY_RESULT_ITEM_SLOT_INDEX);

        if (!bottle.isEmpty() && !display.isEmpty() && entity.getItem(RESULT_SLOT_INDEX).isEmpty()) {
            entity.waterInMB -= 250;
            bottle.shrink(1);
            if (bottle.isEmpty()) entity.setItem(GLASS_BOTTLE_INSERT_SLOT_INDEX, ItemStack.EMPTY);
            ItemStack res = display.copy();
            res.setCount(1);
            entity.setItem(RESULT_SLOT_INDEX, res);
            display.shrink(1);
            if (display.isEmpty()) {
                entity.setItem(DISPLAY_RESULT_ITEM_SLOT_INDEX, ItemStack.EMPTY);
                entity.currentBoilingStack = ItemStack.EMPTY;
                entity.boilTicks = 0;
            }
        }

        setChanged(level, pos, state);
    }

    public static List<ItemEntity> getItemsAtAndAbove(Level level, BoilerBlockEntity entity) {
        return SUCK.toAabbs().stream().flatMap((box) ->
                level.getEntitiesOfClass(ItemEntity.class, box.move(entity.worldPosition.getX(), entity.worldPosition.getY(), entity.worldPosition.getZ()),
                EntitySelector.ENTITY_STILL_ALIVE).stream()).collect(Collectors.toList());
    }

    public int getHeatedData() {
        // If heated from below
        if (level == null) return 0;
        BlockState below = level.getBlockState(getBlockPos().below());
        boolean heated = below.is(BlockTags.CAMPFIRES);
        heated = heated || below.is(BlockTags.FIRE);
        heated = heated || below.is(Blocks.LAVA);
        boolean superheated = below.is(BartendingTags.SUPERHEATING_BLOCKS);
        if (FabricLoader.getInstance().isModLoaded("create")) {
            if (TheArtOfCreate.isBlockSuperheatedBlazeBurner(below)) return 3;
        }
        return superheated ? 2 : heated ? 1 : 0;
    }

    public boolean isBoiling() {
        // If water
        return waterInMB > 0 && getHeatedData() != 0;
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
            case GLASS_BOTTLE_INSERT_SLOT_INDEX -> stack.is(Items.GLASS_BOTTLE);
            default -> false;
        };
    }

}
