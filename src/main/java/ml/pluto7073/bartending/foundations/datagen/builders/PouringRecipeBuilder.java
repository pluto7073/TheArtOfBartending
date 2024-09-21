package ml.pluto7073.bartending.foundations.datagen.builders;

import com.google.gson.JsonObject;
import ml.pluto7073.bartending.content.alcohol.AlcoholicDrinks;
import ml.pluto7073.bartending.foundations.alcohol.AlcoholicDrink;
import ml.pluto7073.bartending.foundations.recipe.BartendingRecipes;
import ml.pluto7073.pdapi.util.JsonBuilder;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Consumer;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class PouringRecipeBuilder implements RecipeBuilder {

    private final AlcoholicDrink drink;
    private final Ingredient glass;
    private final Item result;
    private final float ounces;
    private final Advancement.Builder advancement = Advancement.Builder.recipeAdvancement();

    public PouringRecipeBuilder(AlcoholicDrink drink, Ingredient glass, Item result, float ounces) {
        this.drink = drink;
        this.glass = glass;
        this.result = result;
        this.ounces = ounces;
    }

    @Override
    public RecipeBuilder unlockedBy(String criterionName, CriterionTriggerInstance criterionTrigger) {
        advancement.addCriterion(criterionName, criterionTrigger);
        return this;
    }

    @Override
    public RecipeBuilder group(@Nullable String groupName) {
        return this;
    }

    @Override
    public Item getResult() {
        return result;
    }

    @Override
    public void save(Consumer<FinishedRecipe> consumer, ResourceLocation recipeId) {
        advancement.parent(ROOT_RECIPE_ADVANCEMENT)
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(recipeId.withPrefix("pouring/")))
                .rewards(AdvancementRewards.Builder.recipe(recipeId.withPrefix("pouring/")))
                .requirements(RequirementsStrategy.OR);
        consumer.accept(new Result(
                recipeId.withPrefix("pouring/"), drink, glass, ounces, result, advancement,
                recipeId.withPrefix("recipes/pouring/")
        ));
    }

    public static class Result implements FinishedRecipe {

        private final ResourceLocation id;
        private final AlcoholicDrink drink;
        private final Ingredient glass;
        private final float ounces;
        private final Item result;
        private final Advancement.Builder advancement;
        private final ResourceLocation advancementId;

        protected Result(ResourceLocation id, AlcoholicDrink drink, Ingredient glass, float ounces, Item result, Advancement.Builder advancement, ResourceLocation advancementId) {
            this.id = id;
            this.drink = drink;
            this.glass = glass;
            this.ounces = ounces;
            this.result = result;
            this.advancement = advancement;
            this.advancementId = advancementId;
        }

        @Override
        public void serializeRecipeData(JsonObject json) {
            json.addProperty("alcoholType", AlcoholicDrinks.getId(drink).toString());
            json.addProperty("ounces", ounces);
            json.add("glass", glass.toJson());
            json.add("result", JsonBuilder.object().put("item", BuiltInRegistries.ITEM.getKey(result).toString()).build());
        }

        @Override
        public ResourceLocation getId() {
            return id;
        }

        @Override
        public RecipeSerializer<?> getType() {
            return BartendingRecipes.POURING.serializer();
        }

        @Override
        public @Nullable JsonObject serializeAdvancement() {
            return advancement.serializeToJson();
        }

        @Override
        public @Nullable ResourceLocation getAdvancementId() {
            return advancementId;
        }
    }

}
