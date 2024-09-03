package ml.pluto7073.bartending.foundations.fluid;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Predicate;
import java.util.function.Supplier;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class AlcoholFluid extends FlowingFluid {

    private final boolean isSource;

    private AlcoholFluid(boolean source) {
        isSource = source;
    }

    private Fluid flowing = null, source = null;
    private Item bucket = null;
    private Block legacyBlock = null;

    @Override
    public Fluid getFlowing() {
        if (flowing == null) {
            throw new IllegalStateException("AlcoholFluid.getFlowing() called before full initialization");
        }
        return flowing;
    }

    @Override
    public Fluid getSource() {
        if (source == null) {
            throw new IllegalStateException("AlcoholFluid.getSource() called before full initialization");
        }
        return source;
    }

    @Override
    public boolean isSame(Fluid fluid) {
        return fluid == getFlowing() || fluid == getSource();
    }

    @Override
    protected boolean canConvertToSource(Level level) {
        return false;
    }

    @Override
    protected void beforeDestroyingBlock(LevelAccessor level, BlockPos pos, BlockState state) {
        final BlockEntity blockEntity = state.hasBlockEntity() ? level.getBlockEntity(pos) : null;
        Block.dropResources(state, level, pos, blockEntity);
    }

    @Override
    protected int getSlopeFindDistance(LevelReader level) {
        return 3;
    }

    @Override
    protected int getDropOff(LevelReader level) {
        return 1;
    }

    @Override
    public Item getBucket() {
        if (bucket == null) throw new IllegalStateException("Tried to get BucketItem before registration was complete");
        return bucket;
    }

    @Override
    protected boolean canBeReplacedWith(FluidState state, BlockGetter level, BlockPos pos, Fluid fluid, Direction direction) {
        return false;
    }

    @Override
    public int getTickDelay(LevelReader level) {
        return 5;
    }

    @Override
    protected float getExplosionResistance() {
        return 100;
    }

    @Override
    protected BlockState createLegacyBlock(FluidState state) {
        if (legacyBlock == null) throw new IllegalStateException("Tried to get legacy block before fluid registration was complete");
        return legacyBlock.defaultBlockState().setValue(LiquidBlock.LEVEL, getLegacyLevel(state));
    }

    @Override
    public boolean isSource(FluidState state) {
        return isSource;
    }

    @Override
    public int getAmount(FluidState state) {
        if (isSource) return 8;
        return state.getValue(LEVEL);
    }

    @Override
    protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
        super.createFluidStateDefinition(builder);
        if (!isSource) builder.add(LEVEL);
    }

    public static FluidHolder create() {
        AlcoholFluid source = new AlcoholFluid(true);
        AlcoholFluid flowing = new AlcoholFluid(false);
        source.source = source;
        flowing.source = source;
        source.flowing = flowing;
        flowing.flowing = flowing;
        Block block = new LiquidBlock(source, BlockBehaviour.Properties.copy(Blocks.WATER));
        source.legacyBlock = block;
        flowing.legacyBlock = block;
        Item bucket = new BucketItem(source, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1));
        source.bucket = bucket;
        flowing.bucket = bucket;
        return new FluidHolder(source, flowing, block, bucket);
    }

}
