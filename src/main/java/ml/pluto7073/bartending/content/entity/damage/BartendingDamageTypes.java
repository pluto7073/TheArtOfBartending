package ml.pluto7073.bartending.content.entity.damage;

import ml.pluto7073.bartending.TheArtOfBartending;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.level.Level;

public final class BartendingDamageTypes {

    public static final ResourceKey<DamageType> ALCOHOL_POISONING = ResourceKey.create(Registries.DAMAGE_TYPE, TheArtOfBartending.asId("alcohol_poisoning"));

    public static DamageSource of(Level world, ResourceKey<DamageType> key) {
        return new DamageSource(world.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(key));
    }

}
