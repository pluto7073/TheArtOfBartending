package ml.pluto7073.bartending.foundations.recipe;

import ml.pluto7073.bartending.TheArtOfBartending;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.Locale;

public enum BartendingRecipes {

    POURING(new PouringRecipe.Serializer());

    private final RecipeSerializer<?> serializer;
    private final RecipeType<?> type;
    private final ResourceLocation id;

    <S extends RecipeSerializer<T>, T extends Recipe<?>> BartendingRecipes(S serializer) {
        String id = this.name().toLowerCase(Locale.ROOT);
        this.id = TheArtOfBartending.asId(id);
        type = new RecipeType<>() {
            @Override
            public String toString() {
                return BartendingRecipes.this.id.toString();
            }
        };
        this.serializer = serializer;
    }

    public void register() {
        Registry.register(BuiltInRegistries.RECIPE_TYPE, id, type);
        Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, id, serializer);
    }

    @SuppressWarnings("unchecked")
    public <T extends Recipe<?>> RecipeType<T> type() {
        return (RecipeType<T>) type;
    }

    public RecipeSerializer<?> serializer() {
        return serializer;
    }
}
