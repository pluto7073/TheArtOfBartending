package ml.pluto7073.bartending.foundations.fluid;

import ml.pluto7073.bartending.TheArtOfBartending;
import ml.pluto7073.bartending.content.fluid.BartendingFluids;
import ml.pluto7073.bartending.foundations.util.BrewingUtil;
import ml.pluto7073.bartending.foundations.util.NonNullConsumer;
import ml.pluto7073.bartending.foundations.util.StaticSupplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariantAttributeHandler;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariantAttributes;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
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
import net.minecraft.world.level.material.Fluids;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class AlcoholFluid extends SimpleFlowableFluid {

    public AlcoholFluid(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isSource(FluidState state) {
        return true;
    }

    @Override
    public int getAmount(FluidState state) {
        return 0;
    }

    @Override
    public Fluid getFlowing() {
        return this;
    }

    @Override
    public Item getBucket() {
        return Items.AIR;
    }

    @Override
    protected BlockState createLegacyBlock(FluidState state) {
        return Blocks.AIR.defaultBlockState();
    }

    public static Builder create(String name, ResourceLocation stillId, ResourceLocation flowId) {
        return new Builder(AlcoholFluid::new, stillId, flowId, name);
    }

    @SuppressWarnings("UnstableApiUsage")
    public static class Builder {

        private final Supplier<? extends SimpleFlowableFluid> source;
        private final Supplier<? extends SimpleFlowableFluid> flowing;
        private NonNullConsumer<SimpleFlowableFluid.Properties> properties;
        private Supplier<FluidVariantAttributeHandler> fluidAttributes;
        private final List<TagKey<Fluid>> tags = new ArrayList<>();
        private final ResourceLocation stillId, flowId;
        private final String name;

        public Builder(Function<SimpleFlowableFluid.Properties, ? extends SimpleFlowableFluid> factory, ResourceLocation stillId, ResourceLocation flowId, String name) {
            source = new StaticSupplier<SimpleFlowableFluid>(() -> factory.apply(createProperties()));
            flowing = new StaticSupplier<SimpleFlowableFluid>(() -> factory.apply(createProperties()));
            this.stillId = stillId;
            this.flowId = flowId;
            this.name = name;
            this.properties = p -> p.bucket(() -> BuiltInRegistries.ITEM.get(TheArtOfBartending.asId(name + "_bucket")))
                    .block(() -> (LiquidBlock) BuiltInRegistries.BLOCK.get(TheArtOfBartending.asId(name)));
        }

        public Builder properties(NonNullConsumer<SimpleFlowableFluid.Properties> consumer) {
            properties = properties.andThen(consumer);
            return this;
        }

        public Builder attributes(Supplier<FluidVariantAttributeHandler> handler) {
            fluidAttributes = handler;
            return this;
        }

        public Builder tag(TagKey<Fluid> tag) {
            this.tags.add(tag);
            return this;
        }

        private SimpleFlowableFluid.Properties createProperties() {
            Properties props = new Properties(source, flowing);
            properties.accept(props);
            return props;
        }

        public AlcoholFluid register() {
            ResourceLocation id = TheArtOfBartending.asId(name);
            SimpleFlowableFluid fluid = flowing.get();
            Registry.register(BuiltInRegistries.FLUID, id, fluid.getSource());
            Registry.register(BuiltInRegistries.FLUID, id.withPrefix("flowing_"), fluid);
            if (fluidAttributes != null) {
                FluidVariantAttributeHandler attributes = fluidAttributes.get();
                FluidVariantAttributes.register(fluid, attributes);
                FluidVariantAttributes.register(fluid.getSource(), attributes);
            }

            BrewingUtil.runOn(EnvType.CLIENT, () -> () ->
                    FluidRenderHandlerRegistry.INSTANCE.register(fluid.getSource(), fluid, new SimpleFluidRenderHandler(stillId, flowId)));

            BartendingFluids.FLUID_TAGS.computeIfAbsent(fluid, k -> new ArrayList<>()).addAll(tags);
            BartendingFluids.FLUID_TAGS.computeIfAbsent(fluid.getSource(), k -> new ArrayList<>()).addAll(tags);
            return (AlcoholFluid) fluid;
        }

    }

}
