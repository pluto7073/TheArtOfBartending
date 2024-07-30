package ml.pluto7073.bartending.content.block.entity;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BarrelBlock;
import net.minecraft.world.level.block.entity.BarrelBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;

@MethodsReturnNonnullByDefault
public class FermentingBarrelBlockEntity extends RandomizableContainerBlockEntity {

    public final WoodType woodType;

    private NonNullList<ItemStack> contents;
    private final ContainerOpenersCounter counter;

    public FermentingBarrelBlockEntity(BlockEntityType<?> type, WoodType woodType, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
        this.woodType = woodType;
        contents = NonNullList.withSize(5, ItemStack.EMPTY);
        counter = new ContainerOpenersCounter() {
            protected void onOpen(Level level, BlockPos pos, BlockState state) {
                FermentingBarrelBlockEntity.this.playSound( SoundEvents.BARREL_OPEN);
                FermentingBarrelBlockEntity.this.updateBlockState(state, true);
            }

            protected void onClose(Level level, BlockPos pos, BlockState state) {
                FermentingBarrelBlockEntity.this.playSound(SoundEvents.BARREL_CLOSE);
                FermentingBarrelBlockEntity.this.updateBlockState(state, false);
            }

            protected void openerCountChanged(Level level, BlockPos pos, BlockState state, int count, int openCount) {
            }

            protected boolean isOwnContainer(Player player) {
                if (player.containerMenu instanceof ChestMenu) {
                    Container container = ((ChestMenu)player.containerMenu).getContainer();
                    return container == FermentingBarrelBlockEntity.this;
                } else {
                    return false;
                }
            }
        };
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return contents;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> itemStacks) {
        contents = itemStacks;
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("block.bartending." + woodType.name() + "_fermenting_barrel");
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return null;
    }

    @Override
    public int getContainerSize() {
        return 0;
    }

    void updateBlockState(BlockState state, boolean open) {
        this.level.setBlock(this.getBlockPos(), (BlockState)state.setValue(BarrelBlock.OPEN, open), 3);
    }

    void playSound(SoundEvent sound) {
        double d = (double)this.worldPosition.getX() + 0.5;
        double e = (double)this.worldPosition.getY() + 0.5;
        double f = (double)this.worldPosition.getZ() + 0.5;
        this.level.playSound(null, d, e, f, sound, SoundSource.BLOCKS, 0.5F, this.level.random.nextFloat() * 0.1F + 0.9F);
    }

}
