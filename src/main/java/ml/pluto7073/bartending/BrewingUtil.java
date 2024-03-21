package ml.pluto7073.bartending;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class BrewingUtil {

    public static ItemStack setBaseUnfinalizedData(ItemStack stack, Item baseItem) {
        NbtCompound alcData = stack.getOrCreateSubNbt("BrewingData");
        alcData.putString("base", Registries.ITEM.getId(baseItem).toString());
        return stack;
    }

    public static Item getBaseUnfinalizedItem(ItemStack stack) {
        String id = stack.getOrCreateSubNbt("BrewingData").getString("base");
        return Registries.ITEM.get(new Identifier(id));
    }

}
