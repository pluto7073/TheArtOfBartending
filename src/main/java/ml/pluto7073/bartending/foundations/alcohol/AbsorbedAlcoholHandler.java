package ml.pluto7073.bartending.foundations.alcohol;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import ml.pluto7073.bartending.TheArtOfBartending;
import ml.pluto7073.bartending.content.entity.effect.BartendingMobEffects;
import ml.pluto7073.bartending.content.item.BartendingItems;
import ml.pluto7073.bartending.foundations.command.BartendingCommands;
import ml.pluto7073.bartending.foundations.util.BrewingUtil;
import ml.pluto7073.chemicals.Chemicals;
import ml.pluto7073.chemicals.handlers.HalfLifeChemicalHandler;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.Registry;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AbsorbedAlcoholHandler extends HalfLifeChemicalHandler {

    public static final int ALCOHOL_HALF_LIFE_TICKS = 4500;
    public static final AbsorbedAlcoholHandler INSTANCE = new AbsorbedAlcoholHandler(ALCOHOL_HALF_LIFE_TICKS);

    public AbsorbedAlcoholHandler(int halfLifeTicks) {
        super(halfLifeTicks);
    }

    @Override
    public Collection<MobEffectInstance> getEffectsForAmount(float amount, Level level) {
        float bac = BrewingUtil.calculateBAC(amount);
        List<MobEffectInstance> list = new ArrayList<>();
        if (bac >= 0.01f) {
            list.add(new MobEffectInstance(MobEffects.WEAKNESS, 30 * 20, 0, true, true));
        }
        if (bac > 0.05f) {
            list.add(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 30 * 20, 0, true, true));
        }
        if (bac > 0.11f) {
            list.add(new MobEffectInstance(MobEffects.CONFUSION, 30 * 20, 0, true, true));
            list.add(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 30 * 20, 0, true, true));
            list.add(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 30 * 20, 1, true, true));
        }
        if (bac > 0.21f) {
            list.add(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 30 * 20, 2, true, true));
            list.add(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 30 * 20, 1, true, true));
        }
        if (bac > 0.35f) {
            list.add(new MobEffectInstance(BartendingMobEffects.ALCOHOL_POISON, 30 * 20, 0, true, true));
        }
        return list;
    }

    @Override
    public @Nullable LiteralArgumentBuilder<CommandSourceStack> createCustomChemicalCommandExtension() {
        return BartendingCommands.alcohol(this);
    }

    public static void init() {
        Registry.register(Chemicals.REGISTRY, TheArtOfBartending.asId("absorbed_alcohol"), INSTANCE);
    }

}
