package ml.pluto7073.bartending.foundations.datagen;

import com.simibubi.create.foundation.fluid.FluidIngredient;
import ml.pluto7073.bartending.content.alcohol.AlcoholicDrinks;
import ml.pluto7073.bartending.content.item.BartendingItems;
import ml.pluto7073.bartending.foundations.datagen.builders.EmptyingRecipeBuilder;
import ml.pluto7073.bartending.foundations.datagen.builders.FillingRecipeBuilder;
import ml.pluto7073.bartending.foundations.datagen.builders.PouringRecipeBuilder;
import ml.pluto7073.bartending.foundations.util.BrewingUtil;
import ml.pluto7073.pdapi.datagen.builder.WorkstationRecipeBuilder;
import ml.pluto7073.pdapi.tag.PDTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Consumer;

public class BartendingRecipeProviders extends FabricRecipeProvider {

    public BartendingRecipeProviders(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void buildRecipes(Consumer<FinishedRecipe> exporter) {
        BartendingItems.BOTTLES.forEach((alc, bottle) -> {
            ResourceLocation id = BuiltInRegistries.ITEM.getKey(bottle);
            float amount = 20250;
            if (bottle.emptyBottleItem == BartendingItems.LIQUOR_BOTTLE) amount = 50625;
            if (bottle.emptyBottleItem == BartendingItems.WINE_BOTTLE) amount = 42187.5f;
            if (bottle.emptyBottleItem == BartendingItems.JUG) amount = 67500;
            if (FabricLoader.getInstance().isModLoaded("create")) {
                EmptyingRecipeBuilder.emptying(
                                Ingredient.of(bottle), bottle.emptyBottleItem,
                                alc.fluid().still(), amount
                        ).unlockedBy("obtain_input", InventoryChangeTrigger.TriggerInstance.hasItems(bottle))
                        .save(exporter, id);

                new FillingRecipeBuilder(
                        Ingredient.of(bottle.emptyBottleItem),
                        FluidIngredient.fromFluid(alc.fluid().still(), (long) amount),
                        bottle
                ).unlockedBy("obtain_result", InventoryChangeTrigger.TriggerInstance.hasItems(bottle))
                        .save(exporter, id);
            }

        });

        BartendingItems.SERVING_BOTTLES.forEach((alc, bottle) -> {
            ResourceLocation id = BuiltInRegistries.ITEM.getKey(bottle);
            if (FabricLoader.getInstance().isModLoaded("create")) {
                EmptyingRecipeBuilder
                        .emptying(Ingredient.of(bottle), bottle.bottle, alc.fluid().still(), BrewingUtil.mbFromOunces(alc.standardOunces()) * 81)
                        .unlockedBy("obtain_input", InventoryChangeTrigger.TriggerInstance.hasItems(bottle))
                        .save(exporter, id);

                new FillingRecipeBuilder(
                        Ingredient.of(bottle.bottle),
                        FluidIngredient.fromFluid(alc.fluid().still(), (long) BrewingUtil.mbFromOunces(alc.standardOunces()) * 81),
                        bottle
                ).unlockedBy("obtain_input", InventoryChangeTrigger.TriggerInstance.hasItems(bottle.bottle))
                        .save(exporter, id);
            }

            new PouringRecipeBuilder(alc, Ingredient.of(bottle.bottle), bottle, alc.standardOunces())
                    .unlockedBy("has_glass", has(bottle.bottle))
                    .save(exporter, id);
        });

        BartendingItems.GLASSES.forEach((alc, glass) -> {
            ResourceLocation id = BuiltInRegistries.ITEM.getKey(glass);
            if (FabricLoader.getInstance().isModLoaded("create")) {
                EmptyingRecipeBuilder
                        .emptying(Ingredient.of(glass), glass.bottle, alc.fluid().still(), BrewingUtil.mbFromOunces(alc.standardOunces()) * 81)
                        .unlockedBy("obtain_input", InventoryChangeTrigger.TriggerInstance.hasItems(glass))
                        .save(exporter, id);

                new FillingRecipeBuilder(
                        Ingredient.of(glass.bottle),
                        FluidIngredient.fromFluid(alc.fluid().still(), (long) BrewingUtil.mbFromOunces(alc.standardOunces()) * 81),
                        glass
                ).unlockedBy("obtain_input", InventoryChangeTrigger.TriggerInstance.hasItems(glass.bottle))
                        .save(exporter, id);
            }

            new PouringRecipeBuilder(alc, Ingredient.of(glass.bottle), glass, alc.standardOunces())
                    .unlockedBy("has_glass", has(glass.bottle))
                    .save(exporter, id);
        });

        BartendingItems.SHOTS.forEach((alc, shot) -> {
            ResourceLocation id = BuiltInRegistries.ITEM.getKey(shot);
            if (FabricLoader.getInstance().isModLoaded("create")) {
                EmptyingRecipeBuilder
                        .emptying(Ingredient.of(shot), BartendingItems.SHOT_GLASS, alc.fluid().still(), 2531.25f)
                        .unlockedBy("obtain_input", InventoryChangeTrigger.TriggerInstance.hasItems(shot))
                        .save(exporter, id);

                new FillingRecipeBuilder(Ingredient.of(BartendingItems.SHOT_GLASS),
                        FluidIngredient.fromFluid(alc.fluid().still(), 2531L), shot)
                        .unlockedBy("obtain_result", InventoryChangeTrigger.TriggerInstance.hasItems(shot))
                        .save(exporter, id);
            }

            new PouringRecipeBuilder(alc, Ingredient.of(BartendingItems.SHOT_GLASS), shot, 1.5f)
                    .unlockedBy("has_glass", has(BartendingItems.SHOT_GLASS))
                    .save(exporter, id);

            new WorkstationRecipeBuilder(Ingredient.of(PDTags.WORKSTATION_DRINKS), Ingredient.of(shot), id)
                    .unlockedBy("has_input", has(shot))
                    .save(exporter, AlcoholicDrinks.getId(alc).withPrefix("drink_workstation/add_"));
        });
    }

}
