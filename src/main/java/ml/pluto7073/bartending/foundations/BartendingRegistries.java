package ml.pluto7073.bartending.foundations;

import ml.pluto7073.bartending.TheArtOfBartending;
import ml.pluto7073.bartending.content.alcohol.AlcoholicDrinks;
import ml.pluto7073.bartending.foundations.alcohol.AlcoholicDrink;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;

public class BartendingRegistries {

    public static final ResourceKey<Registry<AlcoholicDrink>> ALCOHOLIC_DRINK_KEY =
            ResourceKey.createRegistryKey(TheArtOfBartending.asId("alcoholic_drink"));

    public static final Registry<AlcoholicDrink> ALCOHOLIC_DRINK =
            BuiltInRegistries.registerSimple(ALCOHOLIC_DRINK_KEY, registry -> AlcoholicDrinks.BEER);

}
