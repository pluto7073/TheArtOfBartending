package ml.pluto7073.bartending.compat.rei;

import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import ml.pluto7073.bartending.TheArtOfBartending;
import ml.pluto7073.bartending.compat.rei.category.AlcoholBrewingCategory;
import ml.pluto7073.bartending.compat.rei.display.AlcoholBrewingDisplay;
import ml.pluto7073.bartending.content.alcohol.AlcoholicDrinks;
import ml.pluto7073.bartending.content.block.BartendingBlocks;
import ml.pluto7073.bartending.content.item.BartendingItems;
import ml.pluto7073.bartending.foundations.alcohol.AlcoholicDrink;
import ml.pluto7073.bartending.foundations.step.AlternativeBrewerStep;
import ml.pluto7073.bartending.foundations.step.BrewerStep;
import ml.pluto7073.pdapi.compat.rei.DrinkREI;

import java.util.Arrays;

public class TheArtOfREI implements REIClientPlugin {

    public static final CategoryIdentifier<AlcoholBrewingDisplay> ALCOHOL_BREWING = CategoryIdentifier.of(TheArtOfBartending.asId("alcohol_brewing"));

    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new AlcoholBrewingCategory());
        registry.addWorkstations(DrinkREI.INGREDIENT_SEQUENCE, EntryStacks.of(BartendingItems.COUNTER_TOP));
        registry.addWorkstations(ALCOHOL_BREWING, EntryStacks.of(BartendingBlocks.BOILER), EntryStacks.of(BartendingBlocks.BOTTLER), EntryStacks.of(BartendingBlocks.DISTILLERY));
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        registry.registerFiller(AlcoholicDrink.class, AlcoholBrewingDisplay::new);
        AlcoholicDrinks.values().stream().filter(drink -> Arrays.stream(drink.steps()).noneMatch(step -> step instanceof AlternativeBrewerStep)).forEach(registry::add);
    }
}
