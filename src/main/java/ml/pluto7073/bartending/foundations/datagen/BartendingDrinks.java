package ml.pluto7073.bartending.foundations.datagen;

import ml.pluto7073.bartending.TheArtOfBartending;
import ml.pluto7073.bartending.content.item.BartendingItems;
import ml.pluto7073.pdapi.PDAPI;
import ml.pluto7073.pdapi.addition.action.ApplyStatusEffectAction;
import ml.pluto7073.pdapi.addition.action.RestoreHungerAction;
import ml.pluto7073.pdapi.datagen.provider.SpecialtyDrinkProvider;
import ml.pluto7073.pdapi.specialty.SpecialtyDrink;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.resource.conditions.v1.ConditionJsonProvider;
import net.fabricmc.fabric.api.resource.conditions.v1.DefaultResourceConditions;
import net.fabricmc.fabric.impl.datagen.FabricDataGenHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;

import java.util.function.BiConsumer;

import static ml.pluto7073.bartending.TheArtOfBartending.asId;

public class BartendingDrinks extends SpecialtyDrinkProvider {

    private static final DrinkBuilder APPLETINI = staticBaseBuilder(BartendingItems.MIXED_DRINK)
            .step(PDAPI.asId("ice"))
            .step(asId("shot_of_vodka"))
            .step(asId("shot_of_vodka"))
            .step(asId("shot_of_vodka"))
            .step(asId("shot_of_vodka"))
            .step(asId("shot_of_apple_liqueur"))
            .step(asId("shot_of_apple_liqueur"))
            .chemical(asId("alcohol"), 72)
            .color(12356169)
            .action(new RestoreHungerAction(6, 4))
            .action(new ApplyStatusEffectAction(MobEffects.FIRE_RESISTANCE, 200, 1));

    private static final DrinkBuilder MARTINI = staticBaseBuilder(BartendingItems.MIXED_DRINK)
            .step(PDAPI.asId("ice"))
            .step(asId("shot_of_dry_vermouth"))
            .step(asId("shot_of_gin"))
            .step(asId("shot_of_gin"))
            .step(asId("shot_of_gin"))
            .chemical(asId("alcohol"), 51)
            .color(15724527)
            .action(new ApplyStatusEffectAction(MobEffects.FIRE_RESISTANCE, 200, 1));

    private static final DrinkBuilder MARGARITA = staticBaseBuilder(BartendingItems.MIXED_DRINK)
            .step(PDAPI.asId("ice"))
            .step(asId("shot_of_tequila"))
            .step(asId("shot_of_tequila"))
            .step(asId("shot_of_orange_liqueur"))
            .step(asId("compat/fruitfulfun/lime"))
            .chemical(asId("alcohol"), 34)
            .color(14808274)
            .action(new ApplyStatusEffectAction(MobEffects.FIRE_RESISTANCE, 200, 1))
            .action(new RestoreHungerAction(3, 3));

    private static final DrinkBuilder ESPRESSO_MARTINI = staticBaseBuilder(BartendingItems.MIXED_DRINK)
            .step(asId("shot_of_vodka"))
            .step(asId("shot_of_coffee_liqueur"))
            .step(new ResourceLocation("plutoscoffee:espresso_shot"))
            .step(PDAPI.asId("sugar"))
            .step(asId("cocoa_beans"))
            .step(asId("compat/plutoscoffee/coffee_bean"))
            .chemical(PDAPI.asId("caffeine"), 80)
            .chemical(asId("alcohol"), 26)
            .color(2167556)
            .action(new RestoreHungerAction(3, 0));

    public BartendingDrinks(FabricDataOutput out) {
        super(out);
    }

    @Override
    public void buildDrinks(BiConsumer<ResourceLocation, SpecialtyDrink> output) {
        BiConsumer<ResourceLocation, SpecialtyDrink> fruitfulfun = withConditions(output, DefaultResourceConditions.allModsLoaded("fruitfulfun"));
        BiConsumer<ResourceLocation, SpecialtyDrink> plutoscoffee = withConditions(output, DefaultResourceConditions.allModsLoaded("plutoscoffee"));
        APPLETINI.save(asId("appletini"), output);
        MARTINI.save(asId("martini"), output);
        MARGARITA.save(asId("compat/fruitfulfun/margarita"), fruitfulfun);
        ESPRESSO_MARTINI.save(asId("compat/plutoscoffee/espresso_martini"), plutoscoffee);
    }

}
