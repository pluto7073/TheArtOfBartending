package ml.pluto7073.bartending.foundations.datagen;

import ml.pluto7073.bartending.TheArtOfBartending;
import ml.pluto7073.bartending.content.alcohol.AlcoholicDrinks;
import ml.pluto7073.bartending.content.block.BartendingBlocks;
import ml.pluto7073.bartending.content.item.BartendingItems;
import ml.pluto7073.bartending.foundations.BartendingStats;
import ml.pluto7073.bartending.foundations.alcohol.AlcoholicDrink;
import ml.pluto7073.bartending.foundations.fluid.FluidHolder;
import ml.pluto7073.bartending.foundations.item.AlcoholicShotItem;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.Fluid;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Locale;

public class BartendingEnglishProvider extends FabricLanguageProvider {

    private static final HashMap<WoodType, String> WOOD_NAMES = Util.make(() -> {
        HashMap<WoodType, String> names = new HashMap<>();
        names.put(WoodType.OAK, "Oak");
        names.put(WoodType.SPRUCE, "Spruce");
        names.put(WoodType.BIRCH, "Birch");
        names.put(WoodType.JUNGLE, "Jungle");
        names.put(WoodType.ACACIA, "Acacia");
        names.put(WoodType.DARK_OAK, "Dark Oak");
        names.put(WoodType.CHERRY, "Cherry");
        names.put(WoodType.MANGROVE, "Mangrove");
        names.put(WoodType.BAMBOO, "Bamboo");
        names.put(WoodType.CRIMSON, "Crimson");
        names.put(WoodType.WARPED, "Warped");
        return names;
    });

    protected BartendingEnglishProvider(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generateTranslations(TranslationBuilder builder) {
        for (AlcoholicDrink drink : AlcoholicDrinks.values()) {
            builder.add(AlcoholicDrinks.getId(drink).toLanguageKey("alcohol"), drink.englishName());
        }

        BartendingBlocks.BARRELS.forEach((wood, barrel) -> {
            builder.add(barrel, WOOD_NAMES.get(wood) + " Fermenting Barrel");
        });
        BartendingItems.BOTTLES.forEach((alc, bottle) -> builder.add(bottle, alc.englishName()));
        BartendingItems.SHOTS.forEach((alc, shot) -> {
            builder.add(shot, "Shot of " + alc.englishName());
            builder.add(getShotAdditionKey(shot), "Shot of " + alc.englishName() + " x%1$s");
        });
        for (AlcoholicDrink drink : AlcoholicDrinks.values()) {
            FluidHolder holder = drink.fluid();
            builder.add(getFluidKey(holder.still()), drink.englishName());
            builder.add(getFluidKey(holder.flowing()), "Flowing " + drink.englishName());
            builder.add(holder.block(), drink.englishName());
            builder.add(holder.bucket(), "Bucket of " + drink.englishName());
        }

        BartendingItems.GLASSES.forEach((alc, glass) -> {
            if (alc == AlcoholicDrinks.BEER) {
                builder.add(glass, "Beer");
                return;
            }
            builder.add(glass, "Glass of " + alc.englishName());
        });

        for (BartendingStats value : BartendingStats.values()) {
            builder.add(value.get().toLanguageKey("stat"), value.getName());
        }

        try {
            Path existing = dataOutput.getModContainer().findPath("assets/bartending/lang/en_us.existing.json")
                    .orElseThrow(FileNotFoundException::new);
            builder.add(existing);
        } catch (Exception e) {
            TheArtOfBartending.LOGGER.error("Failed to load existing translations", e);
        }
    }

    private static String getShotAdditionKey(AlcoholicShotItem shot) {
        ResourceLocation key = BuiltInRegistries.ITEM.getKey(shot);
        return key.toLanguageKey("drink_addition");
    }

    private static String getFluidKey(Fluid fluid) {
        ResourceLocation key = BuiltInRegistries.FLUID.getKey(fluid);
        return key.toLanguageKey("fluid");
    }

}
