package ml.pluto7073.bartending.content.fluid;

import ml.pluto7073.bartending.foundations.alcohol.AlcoholicDrink;
import ml.pluto7073.bartending.foundations.fluid.AlcoholFluid;
import ml.pluto7073.bartending.foundations.fluid.SimpleFlowableFluid;
import ml.pluto7073.bartending.foundations.util.BrewingUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.fabricmc.fabric.api.transfer.v1.client.fluid.FluidVariantRenderHandler;
import net.fabricmc.fabric.api.transfer.v1.client.fluid.FluidVariantRendering;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariantAttributeHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.Fluid;

import javax.annotation.Nullable;
import java.util.*;

@SuppressWarnings("UnstableApiUsage")
public final class BartendingFluids {

    public static final Map<Fluid, List<TagKey<Fluid>>> FLUID_TAGS = new HashMap<>();

    public static final AlcoholFluid ALCOHOL = AlcoholFluid.create("alcohol")
            .attributes(AlcoholFluidVariantAttributeHandler::new)
            .tag(FluidTags.WATER)
            .register();

    public static void init() {}

    @Environment(EnvType.CLIENT)
    public static void initRendering() {
        FluidRenderHandlerRegistry.INSTANCE
                .register(ALCOHOL.getSource(), ALCOHOL,
                        new SimpleFluidRenderHandler(new ResourceLocation("block/water_still"),
                                new ResourceLocation("block/water_flow")));
        AlcoholFluidVariantRenderHandler handler = new AlcoholFluidVariantRenderHandler();
        FluidVariantRendering.register(ALCOHOL.getSource(), handler);
        FluidVariantRendering.register(ALCOHOL.getFlowing(), handler);
    }

    @Environment(EnvType.CLIENT)
    public static class AlcoholFluidVariantRenderHandler implements FluidVariantRenderHandler {

        @Override
        public int getColor(FluidVariant fluidVariant, @Nullable BlockAndTintGetter view, @Nullable BlockPos pos) {
            return BrewingUtil.getDrink(Objects.requireNonNull(fluidVariant.getNbt())).color() | 0xff000000;
        }

    }

    private static class AlcoholFluidVariantAttributeHandler implements FluidVariantAttributeHandler {

        @Override
        public Component getName(FluidVariant fluidVariant) {
            return Component.translatable(getTranslationKey(fluidVariant));
        }

        public String getTranslationKey(FluidVariant stack) {
            CompoundTag tag = stack.getNbt();
            if (tag == null)
                return "alcohol.bartending.empty";
            AlcoholicDrink drink = BrewingUtil.getDrink(tag);
            return drink.getLanguageKey();
        }

    }

}
