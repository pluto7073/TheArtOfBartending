package ml.pluto7073.bartending.alcohol;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.util.Identifier;

public class AlcoholicDrink {

    public AlcoholicDrink(BreweryRecipeStep[] steps, int standardProof, StatusEffectInstance... additionalEffect) {

    }

    public String getTranslationKey() {
        Identifier id = AlcoholicDrinkRegistry.getId(this);
        if (id == null) throw new IllegalStateException("Tried to get translation key of an unregistered drink");
        return "alcohol." + id.getNamespace() + "." + id.getPath();
    }

}
