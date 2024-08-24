package ml.pluto7073.bartending.content.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class VineyardWalkerEnchantment extends Enchantment {

    public VineyardWalkerEnchantment(Rarity rarity, EquipmentSlot... applicableSlots) {
        super(rarity, EnchantmentCategory.ARMOR_LEGS, applicableSlots);
    }

}
