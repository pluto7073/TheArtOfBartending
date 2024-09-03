package ml.pluto7073.bartending.foundations.tags;

import ml.pluto7073.bartending.TheArtOfBartending;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class BartendingTags {

    public static final TagKey<Block> EXTRA_BOILER_HEATERS = block("extra_boiler_heaters");
    public static final TagKey<Block> SUPERHEATING_BLOCKS = block("superheating_blocks");

    public static final TagKey<Item> EMPTY_GLASS_BOTTLES = item("empty_glass_bottles");
    public static final TagKey<Item> BOILABLES = item("boilables");
    public static final TagKey<Item> BOTANICAL_ELEMENTS = item("botanical_elements");

    private static TagKey<Item> item(String name) {
        return TagKey.create(Registries.ITEM, TheArtOfBartending.asId(name));
    }

    private static TagKey<Block> block(String name) {
        return TagKey.create(Registries.BLOCK, TheArtOfBartending.asId(name));
    }

}
