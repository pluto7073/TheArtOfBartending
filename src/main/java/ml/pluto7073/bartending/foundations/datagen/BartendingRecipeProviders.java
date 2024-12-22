package ml.pluto7073.bartending.foundations.datagen;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.data.recipe.ProcessingRecipeGen;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import io.github.fabricators_of_create.porting_lib.fluids.FluidStack;
import ml.pluto7073.bartending.content.alcohol.AlcoholicDrinks;
import ml.pluto7073.bartending.content.fluid.BartendingFluids;
import ml.pluto7073.bartending.content.item.BartendingItems;
import ml.pluto7073.bartending.foundations.BartendingRegistries;
import ml.pluto7073.bartending.foundations.alcohol.AlcoholicDrink;
import ml.pluto7073.bartending.foundations.datagen.builders.PouringRecipeBuilder;
import ml.pluto7073.bartending.foundations.util.BrewingUtil;
import ml.pluto7073.pdapi.datagen.builder.WorkstationRecipeBuilder;
import ml.pluto7073.pdapi.tag.PDTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.resource.conditions.v1.DefaultResourceConditions;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public class BartendingRecipeProviders extends FabricRecipeProvider {

    public BartendingRecipeProviders(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void buildRecipes(Consumer<FinishedRecipe> exporter) {
        BartendingEmptyingGen emptying = new BartendingEmptyingGen(output);
        BartendingFillingGen filling = new BartendingFillingGen(output);

        BartendingItems.BOTTLES.forEach((alc, bottle) -> {
            ResourceLocation id = BuiltInRegistries.ITEM.getKey(bottle);
            int amount = bottle.emptyBottleItem == BartendingItems.LIQUOR_BOTTLE ? 50625 :
                    bottle.emptyBottleItem == BartendingItems.WINE_BOTTLE ? 42187 :
                            bottle.emptyBottleItem == BartendingItems.JUG ? 67500 :
                                    20250;
            if (FabricLoader.getInstance().isModLoaded("create")) {

                emptying.create(id, recipe -> recipe.require(Ingredient.of(bottle))
                        .output(bottle.emptyBottleItem)
                        .output(createAlcoholFluid(amount, alc)));

                filling.create(id, recipe -> recipe.require(bottle.emptyBottleItem)
                        .require(FluidIngredient.fromFluidStack(createAlcoholFluid(amount, alc)))
                        .output(bottle));

            }

        });

        BartendingItems.SERVING_BOTTLES.forEach((alc, bottle) -> {
            ResourceLocation id = BuiltInRegistries.ITEM.getKey(bottle);
            if (FabricLoader.getInstance().isModLoaded("create")) {

                emptying.create(id, recipe -> recipe.require(bottle)
                        .output(bottle.bottle)
                        .output(createAlcoholFluid((int) (BrewingUtil.mbFromOunces(alc.standardOunces()) * 81), alc)));

                filling.create(id, recipe -> recipe.require(bottle.bottle)
                        .require(FluidIngredient.fromFluidStack(createAlcoholFluid((int) (BrewingUtil.mbFromOunces(alc.standardOunces()) * 81), alc)))
                        .output(bottle));

            }

            new PouringRecipeBuilder(alc, Ingredient.of(bottle.bottle), bottle, alc.standardOunces())
                    .unlockedBy("has_glass", has(bottle.bottle))
                    .save(exporter, id);
        });

        BartendingItems.GLASSES.forEach((alc, glass) -> {
            ResourceLocation id = BuiltInRegistries.ITEM.getKey(glass);
            if (FabricLoader.getInstance().isModLoaded("create")) {

                emptying.create(id, recipe -> recipe.require(glass)
                        .output(glass.bottle)
                        .output(createAlcoholFluid((int) (BrewingUtil.mbFromOunces(alc.standardOunces()) * 81), alc)));

                filling.create(id, recipe -> recipe.require(glass.bottle)
                        .require(FluidIngredient.fromFluidStack(createAlcoholFluid((int) (BrewingUtil.mbFromOunces(alc.standardOunces()) * 81), alc)))
                        .output(glass));
            }

            new PouringRecipeBuilder(alc, Ingredient.of(glass.bottle), glass, alc.standardOunces())
                    .unlockedBy("has_glass", has(glass.bottle))
                    .save(exporter, id);
        });

        BartendingItems.SHOTS.forEach((alc, shot) -> {
            ResourceLocation id = BuiltInRegistries.ITEM.getKey(shot);
            if (FabricLoader.getInstance().isModLoaded("create")) {

                emptying.create(id, recipe -> recipe.require(shot)
                        .output(BartendingItems.SHOT_GLASS)
                        .output(createAlcoholFluid(2531, alc)));

                filling.create(id, recipe -> recipe.require(BartendingItems.SHOT_GLASS)
                        .require(FluidIngredient.fromFluidStack(createAlcoholFluid(2531, alc)))
                        .output(shot));

            }

            new PouringRecipeBuilder(alc, Ingredient.of(BartendingItems.SHOT_GLASS), shot, 1.5f)
                    .unlockedBy("has_glass", has(BartendingItems.SHOT_GLASS))
                    .save(exporter, id);

            new WorkstationRecipeBuilder(Ingredient.of(PDTags.WORKSTATION_DRINKS), Ingredient.of(shot), id)
                    .unlockedBy("has_input", has(shot))
                    .save(exporter, AlcoholicDrinks.getId(alc).withPrefix("drink_workstation/add_"));
        });

        Consumer<FinishedRecipe> createExporter = withConditions(exporter, DefaultResourceConditions.allModsLoaded("create"));

        emptying.buildRecipes(createExporter);
        filling.buildRecipes(createExporter);
    }

    private static FluidStack createAlcoholFluid(int amount, AlcoholicDrink alcohol) {
        ResourceLocation id = BartendingRegistries.ALCOHOLIC_DRINK.getKey(alcohol);
        CompoundTag tag = new CompoundTag();
        if (id == null) {
            return new FluidStack(BartendingFluids.ALCOHOL, amount, tag);
        }
        tag.putString("Alcohol", id.toString());
        return new FluidStack(BartendingFluids.ALCOHOL, amount, tag);
    }

    public static abstract class BartendingProcessingGen extends ProcessingRecipeGen {

        public BartendingProcessingGen(FabricDataOutput generator) {
            super(generator);
        }

        @Override
        protected <T extends ProcessingRecipe<?>> GeneratedRecipe create(ResourceLocation name, UnaryOperator<ProcessingRecipeBuilder<T>> transform) {
            return super.create(name, transform);
        }
    }

    public static class BartendingEmptyingGen extends BartendingProcessingGen {

        public BartendingEmptyingGen(FabricDataOutput generator) {
            super(generator);
        }

        @Override
        protected IRecipeTypeInfo getRecipeType() {
            return AllRecipeTypes.EMPTYING;
        }

    }

    public static class BartendingFillingGen extends BartendingProcessingGen {

        public BartendingFillingGen(FabricDataOutput generator) {
            super(generator);
        }

        @Override
        protected IRecipeTypeInfo getRecipeType() {
            return AllRecipeTypes.FILLING;
        }

    }

}
