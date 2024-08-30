package ml.pluto7073.bartending.foundations.fluid;

import ml.pluto7073.bartending.TheArtOfBartending;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public record FluidHolder(AlcoholFluid still, AlcoholFluid flowing, Block block, Item bucket) {

    public void register(String id) {
        Registry.register(BuiltInRegistries.FLUID, TheArtOfBartending.asId(id), still);
        Registry.register(BuiltInRegistries.FLUID, TheArtOfBartending.asId("flowing_" + id), flowing);
        Registry.register(BuiltInRegistries.BLOCK, TheArtOfBartending.asId(id), block);
        Registry.register(BuiltInRegistries.ITEM, TheArtOfBartending.asId(id + "_bucket"), bucket);
    }

}
