package ml.pluto7073.bartending.content.block;

import ml.pluto7073.bartending.content.block.entity.BartendingBlockEntities;
import ml.pluto7073.bartending.content.block.entity.DistilleryBlockEntity;
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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
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
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class DistilleryBlock extends BaseEntityBlock {

    /**
     * 0 - Empty<br>
     * 1 - Not Started<br>
     * 2 - In Progress<br>
     * 3 - Finished
     */
    public static final IntegerProperty DISTILL_STATE = IntegerProperty.create("distill_state", 0, 3);
    public static final BooleanProperty HEATED = BooleanProperty.create("heated");
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty HAS_INPUT = BooleanProperty.create("has_input");
    public static final BooleanProperty HAS_OUTPUT = BooleanProperty.create("has_output");

    public DistilleryBlock(Properties properties) {
        super(properties);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(DISTILL_STATE, HEATED, FACING, HAS_INPUT, HAS_OUTPUT);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return defaultBlockState().setValue(DISTILL_STATE, 0)
                .setValue(HEATED, false)
                .setValue(FACING, ctx.getHorizontalDirection().getOpposite())
                .setValue(HAS_INPUT, false)
                .setValue(HAS_OUTPUT, false);
    }

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

    private static final HashMap<Direction, Vec3> DIRECTION_TO_FLAME_OFFSET = Util.make(() -> {
        HashMap<Direction, Vec3> map = new HashMap<>();
        map.put(Direction.NORTH, new Vec3(0.78125f, 0.375f, 0.46875f));
        map.put(Direction.EAST, new Vec3(0.46875f, 0.375f, 0.78125f));
        map.put(Direction.SOUTH, new Vec3(0.15625f, 0.375f, 0.46875f));
        map.put(Direction.WEST, new Vec3(0.78125f, 0.375f, 0.15625f));
        return map;
    });

    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (!state.getValue(HEATED)) return;
        Vec3 offset = DIRECTION_TO_FLAME_OFFSET.get(state.getValue(FACING));
        addParticlesAndSound(level, new Vec3(pos.getX() + offset.x, pos.getY() + offset.y, pos.getZ() + offset.z), random);
    }

    private static void addParticlesAndSound(Level level, Vec3 offset, RandomSource random) {
        float f = random.nextFloat();
        if (f < 0.3f) {
            level.addParticle(ParticleTypes.SMOKE, offset.x, offset.y, offset.z, 0.0, 0.0, 0.0);
            if (f < 0.17f) {
                level.playLocalSound(offset.x + 0.5, offset.y + 0.5, offset.z + 0.5, SoundEvents.CANDLE_AMBIENT, SoundSource.BLOCKS, 1.0f + random.nextFloat(), random.nextFloat() * 0.7f + 0.3f, false);
            }
        }
        level.addParticle(ParticleTypes.SMALL_FLAME, offset.x, offset.y, offset.z, 0.0, 0.0, 0.0);
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

}
