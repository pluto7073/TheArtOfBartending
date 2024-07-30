package ml.pluto7073.bartending.content.alcohol;

import ml.pluto7073.bartending.TheArtOfBartending;
import ml.pluto7073.bartending.foundations.step.BarrelPredicate;
import ml.pluto7073.bartending.foundations.step.BoilingBrewerStep;
import ml.pluto7073.bartending.foundations.step.FermentingBrewerStep;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.HashMap;
import java.util.Map;

public class AlcoholicDrinkRegistry {

    private static final Map<ResourceLocation, AlcoholicDrink> REGISTRY = new HashMap<>();

    public static final AlcoholicDrink BEER = register("beer", new AlcoholicDrink.Builder()
            .addStep(new BoilingBrewerStep(Ingredient.of(Items.WHEAT), 5, 9600, 600))
            .addStep(new FermentingBrewerStep(BarrelPredicate.ANY, 72000)).build());
    public static final AlcoholicDrink RED_WINE = register("red_wine", new AlcoholicDrink.Builder()
            .addStep(new BoilingBrewerStep(Ingredient.of(Items.SWEET_BERRIES), 64, 9600, 600))
            .addStep(new FermentingBrewerStep(BarrelPredicate.ANY, 24000, 6000)).build());
    public static final AlcoholicDrink WHITE_WINE = register("white_wine", new AlcoholicDrink.Builder()
            .addStep(new BoilingBrewerStep(Ingredient.of(Items.GLOW_BERRIES), 64, 9600, 600))
            .addStep(new FermentingBrewerStep(BarrelPredicate.ANY, 24000, 6000)).build());

    public static AlcoholicDrink register(ResourceLocation id, AlcoholicDrink drink) {
        REGISTRY.put(id, drink);
        return drink;
    }

    private static AlcoholicDrink register(String id, AlcoholicDrink drink) {
        return register(TheArtOfBartending.asId(id), drink);
    }

    public static AlcoholicDrink get(ResourceLocation id) {
        AlcoholicDrink drink = REGISTRY.get(id);
        if (drink == null) throw new IllegalArgumentException("No registered Drink with id " + id);
        return drink;
    }

    public static ResourceLocation getId(AlcoholicDrink drink) {
        for (Map.Entry<ResourceLocation, AlcoholicDrink> entry : REGISTRY.entrySet()) {
            if (entry.getValue().equals(drink)) return entry.getKey();
        }
        return null;
    }

    public static void init() {}

}
