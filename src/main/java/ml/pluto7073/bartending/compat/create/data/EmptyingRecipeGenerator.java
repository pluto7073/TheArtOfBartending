package ml.pluto7073.bartending.compat.create.data;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.foundation.data.recipe.ProcessingRecipeGen;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import ml.pluto7073.bartending.content.alcohol.AlcoholicDrinks;
import ml.pluto7073.bartending.foundations.alcohol.AlcoholicDrink;
import ml.pluto7073.bartending.foundations.item.AlcoholicDrinkItem;
import ml.pluto7073.bartending.foundations.item.PourableBottleItem;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Items;

public class EmptyingRecipeGenerator extends ProcessingRecipeGen {

    public EmptyingRecipeGenerator(FabricDataOutput generator) {
        super(generator);
        for (AlcoholicDrink drink : AlcoholicDrinks.values()) {
            drink.forEach((item, i) -> {
                create(BuiltInRegistries.ITEM.getKey(item).withSuffix("_emptying"), b -> b.require(item)
                        .output(drink.fluid().still(), i)
                        .output(item instanceof PourableBottleItem pour ? pour.emptyBottleItem :
                                item instanceof AlcoholicDrinkItem alc ? alc.bottle : Items.GLASS_BOTTLE));
            });
        }
    }

    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return AllRecipeTypes.EMPTYING;
    }
}
