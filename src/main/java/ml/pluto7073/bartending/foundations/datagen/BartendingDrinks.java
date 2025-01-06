package ml.pluto7073.bartending.foundations.datagen;

import ml.pluto7073.bartending.content.alcohol.AlcoholicDrinks;
import ml.pluto7073.bartending.content.item.BartendingItems;
import ml.pluto7073.pdapi.PDAPI;
import ml.pluto7073.pdapi.datagen.provider.SpecialtyDrinkProvider;
import ml.pluto7073.pdapi.specialty.SpecialtyDrink;
import ml.pluto7073.plutoscoffee.PlutosCoffee;
import ml.pluto7073.teatime.TeaTime;
import ml.pluto7073.teatime.teatypes.TeaSpecialtyBase;
import ml.pluto7073.teatime.teatypes.TeaTypeManager;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.resource.conditions.v1.DefaultResourceConditions;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

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
            .step(PDAPI.asId("sugar"))
            .step(asId("apple"))
            .color(12356169);

    private static final DrinkBuilder MARTINI = staticBaseBuilder(BartendingItems.MIXED_DRINK)
            .step(PDAPI.asId("ice"))
            .step(asId("shot_of_dry_vermouth"))
            .step(asId("shot_of_gin"))
            .step(asId("shot_of_gin"))
            .step(asId("shot_of_gin"))
            .color(15724527);

    private static final DrinkBuilder MARGARITA = staticBaseBuilder(BartendingItems.MIXED_DRINK)
            .step(PDAPI.asId("ice"))
            .step(asId("shot_of_tequila"))
            .step(asId("shot_of_tequila"))
            .step(asId("shot_of_orange_liqueur"))
            .step(asId("compat/fruitfulfun/lime"))
            .color(14808274);

    private static final DrinkBuilder ESPRESSO_MARTINI = staticBaseBuilder(BartendingItems.MIXED_DRINK)
            .step(asId("shot_of_vodka"))
            .step(asId("shot_of_coffee_liqueur"))
            .step(PlutosCoffee.asId("espresso_shot"))
            .step(PDAPI.asId("sugar"))
            .step(asId("cocoa_beans"))
            .step(asId("compat/plutoscoffee/coffee_bean"))
            .color(2167556);

    private static final DrinkBuilder DEATH_IN_THE_AFTERNOON = staticBaseBuilder(BartendingItems.GLASS_OF_CHAMPAGNE)
            .step(asId("shot_of_absinthe"))
            .color(0xe0de50);

    private static final DrinkBuilder OLD_FASHIONED = staticBaseBuilder(BartendingItems.MIXED_DRINK)
            .step(PDAPI.asId("sugar"))
            .step(asId("shot_of_whiskey"))
            .step(asId("sweet_berries"))
            .color(0x442612);

    private static final DrinkBuilder MIMOSA = staticBaseBuilder(BartendingItems.GLASS_OF_CHAMPAGNE)
            .step(asId("compat/fruitfulfun/orange"))
            .color(0xf2c041);

    private static final DrinkBuilder KAMIKAZE = staticBaseBuilder(BartendingItems.MIXED_DRINK)
            .step(asId("shot_of_vodka"))
            .step(asId("shot_of_orange_liqueur"))
            .step(asId("compat/fruitfulfun/lime"))
            .color(0xe0f9db);

    private static final DrinkBuilder MANHATTAN = staticBaseBuilder(BartendingItems.MIXED_DRINK)
            .step(asId("shot_of_whiskey"))
            .step(asId("shot_of_whiskey"))
            .step(asId("shot_of_sweet_vermouth"))
            .color(0xc46836);

    private static final DrinkBuilder VODKA_MARTINI = staticBaseBuilder(BartendingItems.MIXED_DRINK)
            .step(PDAPI.asId("ice"))
            .step(asId("shot_of_vodka"))
            .step(asId("shot_of_vodka"))
            .step(asId("shot_of_vodka"))
            .step(asId("shot_of_dry_vermouth"))
            .color(15724527);

    private static final DrinkBuilder EARL_GRAY_OLD_FASHIONED = staticBaseBuilder(BartendingItems.MIXED_DRINK)
            .step(asId("shot_of_whiskey"))
            .step(asId("shot_of_whiskey"))
            .step(PDAPI.asId("sugar"))
            .step(TeaTime.asId("earl_gray"))
            .step(TeaTime.asId("earl_gray"))
            .step(PDAPI.asId("ice"))
            .step(asId("compat/fruitfulfun/orange"))
            .color(AlcoholicDrinks.WHISKEY.color());

    private static final DrinkBuilder BOURBON_SWEET_TEA = builder(new TeaSpecialtyBase(ResourceKey.create(TeaTypeManager.TEA_TYPE, TeaTime.asId("black_tea"))))
            .step(PDAPI.asId("sugar"))
            .step(PDAPI.asId("sugar"))
            .step(PDAPI.asId("sugar"))
            .step(asId("shot_of_whiskey"))
            .step(PDAPI.asId("ice"))
            .color(5048069);

    public BartendingDrinks(FabricDataOutput out) {
        super(out);
    }

    @Override
    public void buildDrinks(BiConsumer<ResourceLocation, SpecialtyDrink> output) {
        BiConsumer<ResourceLocation, SpecialtyDrink> fruitfulfun = withConditions(output, DefaultResourceConditions.allModsLoaded("fruitfulfun"));
        BiConsumer<ResourceLocation, SpecialtyDrink> plutoscoffee = withConditions(output, DefaultResourceConditions.allModsLoaded("plutoscoffee"));
        BiConsumer<ResourceLocation, SpecialtyDrink> teatimeFFF = withConditions(output, DefaultResourceConditions.allModsLoaded("teatime", "fruitfulfun"));
        BiConsumer<ResourceLocation, SpecialtyDrink> teatime = withConditions(output, DefaultResourceConditions.allModsLoaded("teatime"));
        APPLETINI.save(asId("appletini"), output);
        MARTINI.save(asId("martini"), output);
        MARGARITA.save(asId("compat/fruitfulfun/margarita"), fruitfulfun);
        ESPRESSO_MARTINI.save(asId("compat/plutoscoffee/espresso_martini"), plutoscoffee);
        DEATH_IN_THE_AFTERNOON.save(asId("death_in_the_afternoon"), output);
        OLD_FASHIONED.save(asId("old_fashioned"), output);
        MIMOSA.save(asId("compat/fruitfulfun/mimosa"), fruitfulfun);
        KAMIKAZE.save(asId("compat/fruitfulfun/kamikaze"), fruitfulfun);
        MANHATTAN.save(asId("manhattan"), output);
        VODKA_MARTINI.save(asId("vodka_martini"), output);
        EARL_GRAY_OLD_FASHIONED.save(asId("compat/teatime/earl_gray_old_fashioned"), teatimeFFF);
        BOURBON_SWEET_TEA.save(asId("compat/teatime/bourbon_sweet_tea"), teatime);
    }

}
