package ml.pluto7073.bartending.content.entity.effect;

import ml.pluto7073.bartending.TheArtOfBartending;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public final class BartendingMobEffects {

    public static final MobEffect ALCOHOL_POISON = new AlcoholPoisoningEffect(MobEffectCategory.HARMFUL, 0x0b1428);

    public static void init() {
        Registry.register(BuiltInRegistries.MOB_EFFECT, TheArtOfBartending.asId("alcohol_poisoning"), ALCOHOL_POISON);
    }

}
