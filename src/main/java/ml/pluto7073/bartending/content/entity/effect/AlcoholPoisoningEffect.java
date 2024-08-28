package ml.pluto7073.bartending.content.entity.effect;

import ml.pluto7073.bartending.content.entity.damage.BartendingDamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class AlcoholPoisoningEffect extends MobEffect {

    protected AlcoholPoisoningEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void applyEffectTick(LivingEntity livingEntity, int amplifier) {
        super.applyEffectTick(livingEntity, amplifier);
        livingEntity.hurt(BartendingDamageTypes.of(livingEntity.level(), BartendingDamageTypes.ALCOHOL_POISONING), 1);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
}
