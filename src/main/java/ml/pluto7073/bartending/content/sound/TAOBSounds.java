package ml.pluto7073.bartending.content.sound;

import ml.pluto7073.bartending.TheArtOfBartending;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;

public class TAOBSounds {

    public static final SoundEvent LIQUID_POUR = SoundEvent.createVariableRangeEvent(TheArtOfBartending.asId("liquid_pour"));

    public static void init() {
        Registry.register(BuiltInRegistries.SOUND_EVENT, TheArtOfBartending.asId("liquid_pour"), LIQUID_POUR);
    }

}
