package ml.pluto7073.bartending.foundations.tags;

import ml.pluto7073.bartending.TheArtOfBartending;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class BartendingTags {

    public static final TagKey<Block> EXTRA_BOILER_HEATERS = TagKey.create(Registries.BLOCK, TheArtOfBartending.asId("extra_boiler_heaters"));
    public static final TagKey<Block> SUPERHEATING_BLOCKS = TagKey.create(Registries.BLOCK, TheArtOfBartending.asId("superheating_blocks"));

    public static final TagKey<Item> EMPTY_GLASS_BOTTLES = TagKey.create(Registries.ITEM, TheArtOfBartending.asId("empty_glass_bottles"));

}
