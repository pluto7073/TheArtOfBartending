package ml.pluto7073.bartending.compat.fruitfulfun;

import ml.pluto7073.bartending.content.fluid.BartendingFluids;
import ml.pluto7073.bartending.content.item.BartendingItems;
import ml.pluto7073.bartending.foundations.alcohol.AlcoholicDrink;
import ml.pluto7073.bartending.foundations.fluid.AlcoholFluid;
import ml.pluto7073.bartending.foundations.step.AlternativeBrewerStep;
import ml.pluto7073.bartending.foundations.step.BoilingBrewerStep;
import ml.pluto7073.bartending.foundations.step.BrewerStep;
import ml.pluto7073.bartending.foundations.step.DistillingBrewerStep;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;

public final class FruityAlcoholicDrinkManager {

    public static AlcoholicDrink createOrangeLiqueur() {
        AlcoholicDrink.Builder builder = new AlcoholicDrink.Builder().proof(50).ounces(1.5f).fluid(BartendingFluids.ORANGE_LIQUEUR)
                .name("Orange Liqueur").bottle(BartendingItems.LIQUOR_BOTTLE).color(0xe0d2ba)
                .setVisibleWhen(() -> FabricLoader.getInstance().isModLoaded("fruitfulfun"));

        BrewerStep boiling;

        if (FabricLoader.getInstance().isModLoaded("fruitfulfun")) {
            Item orange = BuiltInRegistries.ITEM.get(new ResourceLocation("fruitfulfun:orange"));
            boiling = new BoilingBrewerStep.Builder().addIngredient(Ingredient.of(orange), 40, 10)
                    .setTicks(24000).setLeeway(6000).build();
        } else boiling = new AlternativeBrewerStep();

        return builder.addStep(boiling).addStep(new DistillingBrewerStep()).build();
    }

}
