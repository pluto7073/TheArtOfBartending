package ml.pluto7073.bartending.content.alcohol;

import ml.pluto7073.bartending.TheArtOfBartending;
import ml.pluto7073.bartending.content.item.TAOBItems;
import ml.pluto7073.bartending.foundations.alcohol.AlcoholicDrink;
import ml.pluto7073.bartending.foundations.step.BarrelPredicate;
import ml.pluto7073.bartending.foundations.step.BoilingBrewerStep;
import ml.pluto7073.bartending.foundations.step.DistillingBrewerStep;
import ml.pluto7073.bartending.foundations.step.FermentingBrewerStep;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@MethodsReturnNonnullByDefault
public final class AlcoholicDrinks {

    private static final Map<ResourceLocation, AlcoholicDrink> REGISTRY = new HashMap<>();
    private static final HashMap<AlcoholicDrink, Item> DRINK_TO_ITEM = new HashMap<>();

    public static final AlcoholicDrink BEER = register("beer", new AlcoholicDrink.Builder().proof(10).ounces(12)
            .addStep(new BoilingBrewerStep(Ingredient.of(Items.WHEAT), 5, 9600, 600))
            .addStep(new FermentingBrewerStep(BarrelPredicate.ANY, 72000))
            .bottle(TAOBItems.BEER_BOTTLE).build());
    public static final AlcoholicDrink RED_WINE = register("red_wine", new AlcoholicDrink.Builder().proof(24).ounces(5)
            .addStep(new BoilingBrewerStep(Ingredient.of(Items.SWEET_BERRIES), 64, 9600, 600))
            .addStep(new FermentingBrewerStep(BarrelPredicate.ANY, 24000, 6000))
            .bottle(TAOBItems.WINE_BOTTLE).build());
    public static final AlcoholicDrink WHITE_WINE = register("white_wine", new AlcoholicDrink.Builder().proof(24).ounces(5)
            .addStep(new BoilingBrewerStep(Ingredient.of(Items.GLOW_BERRIES), 64, 9600, 600))
            .addStep(new FermentingBrewerStep(BarrelPredicate.ANY, 24000, 6000))
            .bottle(TAOBItems.WINE_BOTTLE).build());
    public static final AlcoholicDrink VODKA = register("vodka", new AlcoholicDrink.Builder().proof(80).ounces(1.5f)
            .addStep(new BoilingBrewerStep(Ingredient.of(Items.POTATO, Items.POISONOUS_POTATO), 10, 12000, 1200))
            .addStep(new DistillingBrewerStep()).bottle(TAOBItems.LIQUOR_BOTTLE).build());

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
        throw new IllegalArgumentException("Unregistered value: " + drink);
    }

    public static Collection<AlcoholicDrink> values() {
        return REGISTRY.values();
    }

    public static void registerFinalDrink(AlcoholicDrink drink, Item item) {
        DRINK_TO_ITEM.put(drink, item);
    }

    public static Item getFinalDrink(AlcoholicDrink drink) {
        return DRINK_TO_ITEM.get(drink);
    }

    public static void init() {}

}
