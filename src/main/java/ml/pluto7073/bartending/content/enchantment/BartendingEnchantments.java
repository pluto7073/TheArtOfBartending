package ml.pluto7073.bartending.content.enchantment;

import ml.pluto7073.bartending.TheArtOfBartending;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;

public class BartendingEnchantments {

    public static final Enchantment VINEYARD_WALKER = register("vineyard_walker", new VineyardWalkerEnchantment(Enchantment.Rarity.COMMON, EquipmentSlot.LEGS));

    private static Enchantment register(String id, Enchantment enchantment) {
        return Registry.register(BuiltInRegistries.ENCHANTMENT, TheArtOfBartending.asId(id), enchantment);
    }

    public static void init() {}

}
