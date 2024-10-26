package ml.pluto7073.bartending.content.block;

import ml.pluto7073.bartending.content.block.entity.BartendingBlockEntities;
import ml.pluto7073.bartending.content.block.entity.DistilleryBlockEntity;
import ml.pluto7073.bartending.content.block.state.properties.DistilleryPart;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
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
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.function.BiPredicate;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
@SuppressWarnings("deprecation")
public class DistilleryBlock extends BaseEntityBlock {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final VoxelShape SHAPE = Shapes.or(Block.box(12, 0, 3, 14, 5, 5),
            Block.box(12, 0, 13, 14, 5, 15),
            Block.box(2, 0, 13, 4, 5, 15),
            Block.box(2, 0, 3, 4, 5, 5),
            Block.box(1, 5, 2, 15, 14, 16),
            Block.box(2, 14, 3, 14, 19, 15),
            Block.box(4, 19, 5, 12, 23, 13),
            Block.box(6, 23, 7, 10, 25, 11),
            Block.box(7, 24, 8, 9, 29, 10));
    //public static final EnumProperty<DistilleryPart> PART = EnumProperty.create("part", DistilleryPart.class);

    public DistilleryBlock(Properties properties) {
        super(properties);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

//    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
//        super.setPlacedBy(level, pos, state, placer, stack);
//        if (!level.isClientSide) {
//            BlockPos blockPos = pos.above();
//            level.setBlock(blockPos, state.setValue(PART, DistilleryPart.TOP), 3);
//            level.blockUpdated(pos, Blocks.AIR);
//            state.updateNeighbourShapes(level, pos, 3);
//        }
//    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext ctx) {
        Direction direction = ctx.getHorizontalDirection().getOpposite();
        return defaultBlockState().setValue(FACING, direction);
//        BlockPos blockPos = ctx.getClickedPos();
//        BlockPos blockPos2 = blockPos.above();
//        Level level = ctx.getLevel();
//        return level.getBlockState(blockPos2).canBeReplaced(ctx) && level.getWorldBorder().isWithinBounds(blockPos2) ? this.defaultBlockState().setValue(FACING, direction) : null;
    }

//    public static DoubleBlockCombiner.BlockType getBlockType(BlockState state) {
//        DistilleryPart bedPart = state.getValue(PART);
//        return bedPart == DistilleryPart.BOTTOM ? DoubleBlockCombiner.BlockType.FIRST : DoubleBlockCombiner.BlockType.SECOND;
//    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter level, BlockPos pos, PathComputationType type) {
        return false;
    }

    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    public int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos) {
        return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(world.getBlockEntity(pos));
    }

//    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
//        if (direction == getNeighbourDirection(state.getValue(PART))) {
//            return neighborState.is(this) && neighborState.getValue(PART) != state.getValue(PART) ? state.setValue(FACING, neighborState.getValue(FACING)) : Blocks.AIR.defaultBlockState();
//        }
//        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
//    }

    private static Direction getNeighbourDirection(DistilleryPart part) {
        return part == DistilleryPart.BOTTOM ? Direction.UP : Direction.DOWN;
    }

//    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
//        if (!level.isClientSide && player.isCreative()) {
//            DistilleryPart bedPart = state.getValue(PART);
//            if (bedPart == DistilleryPart.BOTTOM) {
//                BlockPos blockPos = pos.relative(getNeighbourDirection(bedPart));
//                BlockState blockState = level.getBlockState(blockPos);
//                if (blockState.is(this) && blockState.getValue(PART) == DistilleryPart.TOP) {
//                    level.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 35);
//                    level.levelEvent(player, 2001, blockPos, Block.getId(blockState));
//                }
//            }
//        }
//
//        super.playerWillDestroy(level, pos, state, player);
//    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new DistilleryBlockEntity(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null : createTickerHelper(type, BartendingBlockEntities.DISTILLERY_BLOCK_ENTITY_TYPE, DistilleryBlockEntity::tick);
    }

    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (world.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof DistilleryBlockEntity entity) {
                player.openMenu(entity);
                // Stats.InteractWithDistillery
            }
            return InteractionResult.CONSUME;
        }
    }

    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.is(newState.getBlock())) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof DistilleryBlockEntity entity) {
                Containers.dropContents(world, pos, entity);
            }
            super.onRemove(state, world, pos, newState, moved);
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }
}
