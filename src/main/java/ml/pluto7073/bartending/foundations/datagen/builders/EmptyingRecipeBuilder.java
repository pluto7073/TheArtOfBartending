package ml.pluto7073.bartending.foundations.datagen.builders;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.simibubi.create.AllRecipeTypes;
import ml.pluto7073.pdapi.util.JsonBuilder;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.Advancement.Builder;
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
public class EmptyingRecipeBuilder implements RecipeBuilder {

    private final Ingredient input;
    private final Item resultItem;
    private final Fluid resultFluid;
    private final float amount;
    private final Advancement.Builder advancement = Builder.recipeAdvancement();

    private EmptyingRecipeBuilder(Ingredient input, Item resultItem, Fluid resultFluid, float amount) {
        this.input = input;
        this.resultItem = resultItem;
        this.resultFluid = resultFluid;
        this.amount = amount;
    }

    public static EmptyingRecipeBuilder emptying(Ingredient input, Item remaining, Fluid fluid, float amount) {
        return new EmptyingRecipeBuilder(input, remaining, fluid, amount);
    }

    @Override
    public EmptyingRecipeBuilder unlockedBy(String criterionName, CriterionTriggerInstance criterionTrigger) {
        advancement.addCriterion(criterionName, criterionTrigger);
        return this;
    }

    @Override
    public EmptyingRecipeBuilder group(@Nullable String groupName) {
        return this;
    }

    @Override
    public Item getResult() {
        return resultItem;
    }

    @Override
    public void save(Consumer<FinishedRecipe> finishedRecipeConsumer, ResourceLocation recipeId) {
        this.advancement.parent(ROOT_RECIPE_ADVANCEMENT)
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(recipeId.withPrefix("emptying/")))
                .rewards(AdvancementRewards.Builder.recipe(recipeId.withPrefix("emptying/")))
                .requirements(RequirementsStrategy.OR);
        finishedRecipeConsumer.accept(new Result(
                recipeId.withPrefix("emptying/"), input, resultItem, resultFluid, amount, advancement,
                recipeId.withPrefix("recipes/emptying/")
        ));
    }

    public static class Result implements FinishedRecipe {

        private final ResourceLocation id;
        private final Ingredient input;
        private final Item remaining;
        private final Fluid fluid;
        private final float amount;
        private final Advancement.Builder advancement;
        private final ResourceLocation advancementId;

        protected Result(ResourceLocation id, Ingredient input, Item remaining, Fluid fluid, float amount, Builder advancement, ResourceLocation advancementId) {
            this.id = id;
            this.input = input;
            this.remaining = remaining;
            this.fluid = fluid;
            this.amount = amount;
            this.advancement = advancement;
            this.advancementId = advancementId;
        }

        @Override
        public void serializeRecipeData(JsonObject json) {
            json.add("ingredients", JsonBuilder.array().add(input.toJson()).build());

            ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(remaining);
            ResourceLocation fluidId = BuiltInRegistries.FLUID.getKey(fluid);

            json.add("results", JsonBuilder.array()
                    .add(JsonBuilder.object().put("item", itemId.toString()))
                    .add(JsonBuilder.object()
                            .put("fluid", fluidId.toString())
                            .put("amount", amount))
                    .build());

            json.add("fabric:load_conditions", JsonBuilder.createFabricModLoadedConditions("create"));
        }

        @Override
        public ResourceLocation getId() {
            return id;
        }

        @Override
        public RecipeSerializer<?> getType() {
            return AllRecipeTypes.EMPTYING.getSerializer();
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
