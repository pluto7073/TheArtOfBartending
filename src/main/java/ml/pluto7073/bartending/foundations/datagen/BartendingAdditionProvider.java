package ml.pluto7073.bartending.foundations.datagen;

import ml.pluto7073.bartending.content.item.BartendingItems;
import ml.pluto7073.bartending.foundations.BrewingUtil;
import ml.pluto7073.pdapi.datagen.provider.DrinkAdditionProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.registries.BuiltInRegistries;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Consumer;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class BartendingAdditionProvider extends DrinkAdditionProvider {

    public BartendingAdditionProvider(FabricDataOutput out) {
        super(out);
    }

    @Override
    public void buildAdditions(Consumer<Builder> consumer) {
        BartendingItems.SHOTS.forEach((alc, shot) -> {
            int amount = BrewingUtil.getAlcohol(alc, 1.5f);
            Builder builder = (Builder) builder(BuiltInRegistries.ITEM.getKey(shot))
                    .changesColor(alc.color() != 0xFFFFFF)
                    .color(alc.color())
                    .chemical("alcohol", amount);
            consumer.accept(builder);
        });
    }

}
