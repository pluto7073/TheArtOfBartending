package ml.pluto7073.bartending.content.block;

import com.google.common.collect.ImmutableMap;
import ml.pluto7073.bartending.content.block.entity.BottlerBlockEntity;
import ml.pluto7073.bartending.content.block.entity.BartendingBlockEntities;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.Objects;

@SuppressWarnings("deprecation")
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class BottlerBlock extends BaseEntityBlock {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final ImmutableMap<Direction, VoxelShape> SHAPE_FOR_DIRECTION = Util.make(() -> {
        HashMap<Direction, VoxelShape> map = new HashMap<>();
        VoxelShape same = Shapes.or(Block.box(2, 0, 2, 14, 1, 14),
                Block.box(6, 1, 6, 10, 11, 10));
        map.put(Direction.NORTH, Shapes.or(same, Block.box(6, 11, 3, 10, 14, 10),
                Block.box(6, 5, 3, 10, 6,6)));
        map.put(Direction.SOUTH, Shapes.or(same, Block.box(6, 11, 6, 10, 14, 13),
                Block.box(6, 5, 10, 10, 6, 13)));
        map.put(Direction.WEST, Shapes.or(same, Block.box(3, 11, 6, 10, 14, 10),
                Block.box(3, 5, 6, 6, 6, 10)));
        map.put(Direction.EAST, Shapes.or(same, Block.box(10, 5, 6, 13, 6, 10),
                Block.box(6, 11, 6, 13, 14, 10)));
        return ImmutableMap.copyOf(map);
    });

    public BottlerBlock(Properties properties) {
        super(properties);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Objects.requireNonNull(SHAPE_FOR_DIRECTION.get(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BottlerBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return level.isClientSide ? null : createTickerHelper(blockEntityType, BartendingBlockEntities.BOTTLER_BLOCK_ENTITY_TYPE, BottlerBlockEntity::tick);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            this.openScreen(level, pos, player);
            return InteractionResult.CONSUME;
        }
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(level.getBlockEntity(pos));
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if (stack.hasCustomHoverName()) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof BottlerBlockEntity e) {
                e.setCustomName(stack.getHoverName());
            }
        }
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        BlockEntity entity = level.getBlockEntity(pos);
        if (entity instanceof BottlerBlockEntity boiler) {
            if (level instanceof ServerLevel) {
                boiler.setItem(BottlerBlockEntity.RESULT_DISPLAY_SLOT, ItemStack.EMPTY);
                Containers.dropContents(level, pos, boiler);
            }

            level.updateNeighbourForOutputSignal(pos, this);
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    protected void openScreen(Level level, BlockPos pos, Player player) {
        if (level.getBlockEntity(pos) instanceof BottlerBlockEntity boiler) {
            player.openMenu(boiler);
        }
    }

}
