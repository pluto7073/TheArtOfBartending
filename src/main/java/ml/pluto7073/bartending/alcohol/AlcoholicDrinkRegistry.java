package ml.pluto7073.bartending.alcohol;

import ml.pluto7073.bartending.TheArtOfBartending;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class AlcoholicDrinkRegistry {

    private static final Map<Identifier, AlcoholicDrink> REGISTRY = new HashMap<>();

    public static final AlcoholicDrink BEER = register("beer", new AlcoholicDrink(new BreweryRecipeStep[0], 0));
    public static final AlcoholicDrink RED_WINE = register("red_wine", new AlcoholicDrink(new BreweryRecipeStep[0], 0));
    public static final AlcoholicDrink WHITE_WINE = register("white_wine", new AlcoholicDrink(new BreweryRecipeStep[0], 0));

    public static AlcoholicDrink register(Identifier id, AlcoholicDrink drink) {
        REGISTRY.put(id, drink);
        return drink;
    }

    private static AlcoholicDrink register(String id, AlcoholicDrink drink) {
        return register(TheArtOfBartending.asId(id), drink);
    }

    public static AlcoholicDrink get(Identifier id) {
        AlcoholicDrink drink = REGISTRY.get(id);
        if (drink == null) throw new IllegalArgumentException("No registered Drink with id " + id);
        return drink;
    }

    public static Identifier getId(AlcoholicDrink drink) {
        for (Map.Entry<Identifier, AlcoholicDrink> entry : REGISTRY.entrySet()) {
            if (entry.getValue().equals(drink)) return entry.getKey();
        }
        return null;
    }

    public static void init() {}

}
