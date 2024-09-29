package ml.pluto7073.bartending.compat.rei.display;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.registry.RecipeManagerContext;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import ml.pluto7073.bartending.compat.rei.TheArtOfREI;
import ml.pluto7073.bartending.content.alcohol.AlcoholicDrinks;
import ml.pluto7073.bartending.foundations.recipe.PouringRecipe;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.crafting.Recipe;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class PouringDisplay extends BasicDisplay {

    private final float ounces;

    public PouringDisplay(PouringRecipe recipe) {
        this(List.of(EntryIngredients.of(AlcoholicDrinks.getFinalDrink(recipe.drink())),
                EntryIngredients.ofIngredient(recipe.glass())), Collections.singletonList(EntryIngredients.of(recipe.result())),
                recipe, recipe.ounces());
    }

    public PouringDisplay(List<EntryIngredient> ingredients, List<EntryIngredient> outputs, CompoundTag tag) {
        this(ingredients, outputs, RecipeManagerContext.getInstance().byId(tag, "location"), tag.getFloat("ounces"));
    }

    public PouringDisplay(List<EntryIngredient> ingredients, List<EntryIngredient> outputs, Recipe<?> recipe, float ounces) {
        super(ingredients, outputs, Optional.ofNullable(recipe).map(Recipe::getId));
        this.ounces = ounces;
    }

    public float ounces() {
        return ounces;
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return TheArtOfREI.POURING;
    }
}
