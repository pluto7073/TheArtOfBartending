package ml.pluto7073.bartending.foundations.specialty;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import ml.pluto7073.bartending.TheArtOfBartending;
import ml.pluto7073.pdapi.PDRegistries;
import ml.pluto7073.pdapi.specialty.SpecialtyDrinkBase;
import ml.pluto7073.pdapi.specialty.SpecialtyDrinkBaseSerializer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.Item;

public class GlassDrinkBaseSerializer implements SpecialtyDrinkBaseSerializer {

    public static final GlassDrinkBaseSerializer INSTANCE = new GlassDrinkBaseSerializer();

    public static final Codec<GlassDrinkBase> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(BuiltInRegistries.ITEM.byNameCodec().fieldOf("glass")
                    .forGetter(GlassDrinkBase::glass))
                    .apply(instance, GlassDrinkBase::new));

    @Override
    public Codec<? extends SpecialtyDrinkBase> codec() {
        return CODEC;
    }

    @Override
    public void toNetwork(FriendlyByteBuf buf, SpecialtyDrinkBase base) {
        if (!(base instanceof GlassDrinkBase glass)) return;
        buf.writeResourceLocation(BuiltInRegistries.ITEM.getKey(glass.glass()));
    }

    @Override
    public SpecialtyDrinkBase fromNetwork(FriendlyByteBuf buf) {
        Item item = BuiltInRegistries.ITEM.get(buf.readResourceLocation());
        return new GlassDrinkBase(item);
    }

    public static void init() {
        Registry.register(PDRegistries.SPECIALTY_DRINK_BASE, TheArtOfBartending.asId("glass_base"),
                INSTANCE);
    }

}
