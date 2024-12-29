package ml.pluto7073.bartending.compat.create;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeSerializer;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import ml.pluto7073.bartending.TheArtOfBartending;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class TheArtOfCreate {

    public static final IRecipeTypeInfo ALCOHOL_FILLING =
            new RecipeInfo(TheArtOfBartending.asId("alcohol_filling"), AllRecipeTypes.FILLING.getType(),
                    new ProcessingRecipeSerializer<>(AlcoholFillingRecipe::new));

    public static void init() {
        Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, ALCOHOL_FILLING.getId(), ALCOHOL_FILLING.getSerializer());
    }

    public static int getHeatFromBlazeBurner(BlockState state) {
        if (!state.is(AllBlocks.BLAZE_BURNER.get())) return -1;
        return switch (state.getValue(BlazeBurnerBlock.HEAT_LEVEL)) {
            case FADING -> 1;
            case KINDLED -> 2;
            case SEETHING -> 3;
            default -> 0;
        };
    }

    private static class RecipeInfo implements IRecipeTypeInfo {

        private final ResourceLocation id;
        private final RecipeType<?> type;
        private final RecipeSerializer<?> serializer;

        public RecipeInfo(ResourceLocation id, RecipeType<?> recipeType, RecipeSerializer<?> serializer) {
            this.id = id;
            this.type = recipeType;
            this.serializer = serializer;
        }

        @Override
        public ResourceLocation getId() {
            return id;
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T extends RecipeSerializer<?>> T getSerializer() {
            return (T) serializer;
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T extends RecipeType<?>> T getType() {
            return (T) type;
        }
    }

}
