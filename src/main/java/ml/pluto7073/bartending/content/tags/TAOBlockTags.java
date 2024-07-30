package ml.pluto7073.bartending.content.tags;

import ml.pluto7073.bartending.TheArtOfBartending;
import ml.pluto7073.bartending.client.TheArtOfClient;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class TAOBlockTags {

    public static final TagKey<Block> EXTRA_BOILER_HEATERS = TagKey.create(Registries.BLOCK, TheArtOfBartending.asId("extra_boiler_heaters"));

}
