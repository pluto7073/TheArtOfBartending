package ml.pluto7073.bartending.foundations.datagen.builders;

import com.google.gson.JsonObject;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.foundation.fluid.FluidIngredient;
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
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Consumer;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class FillingRecipeBuilder implements RecipeBuilder {

    private final Ingredient base;
    private final FluidIngredient fluid;
    private final Item result;
    private final Advancement.Builder advancement;

    public FillingRecipeBuilder(Ingredient base, FluidIngredient fluid, Item result) {
        this.base = base;
        this.fluid = fluid;
        this.result = result;
        this.advancement = Advancement.Builder.recipeAdvancement();
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
    public void save(Consumer<FinishedRecipe> finishedRecipeConsumer, ResourceLocation recipeId) {
        advancement.parent(ROOT_RECIPE_ADVANCEMENT)
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(recipeId.withPrefix("filling/")))
                .rewards(AdvancementRewards.Builder.recipe(recipeId.withPrefix("filling/")))
                .requirements(RequirementsStrategy.OR);
        finishedRecipeConsumer.accept(new Result(
                recipeId.withPrefix("filling/"), base, fluid, result, advancement,
                recipeId.withPrefix("recipes/filling/")
        ));
    }

    public static class Result implements FinishedRecipe {

        private final ResourceLocation id;
        private final Ingredient base;
        private final FluidIngredient fluid;
        private final Item result;
        private final Advancement.Builder advancement;
        private final ResourceLocation advancementId;

        protected Result(ResourceLocation id, Ingredient base, FluidIngredient fluid, Item result, Advancement.Builder advancement, ResourceLocation advancementId) {
            this.id = id;
            this.base = base;
            this.fluid = fluid;
            this.result = result;
            this.advancement = advancement;
            this.advancementId = advancementId;
        }

        @Override
        public void serializeRecipeData(JsonObject json) {
            json.add("ingredients", JsonBuilder.array()
                    .add(base.toJson())
                    .add(fluid.serialize()).build());
            ResourceLocation item = BuiltInRegistries.ITEM.getKey(result);
            json.add("results", JsonBuilder.array().add(JsonBuilder.object()
                    .put("item", item.toString())).build());

            json.add("fabric:load_conditions",
                    JsonBuilder.createFabricModLoadedConditions("create"));
        }

        @Override
        public ResourceLocation getId() {
            return id;
        }

        @Override
        public RecipeSerializer<?> getType() {
            return AllRecipeTypes.FILLING.getSerializer();
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
