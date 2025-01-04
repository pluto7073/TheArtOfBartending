package ml.pluto7073.bartending.foundations.specialty;

import ml.pluto7073.bartending.content.item.BartendingItems;
import ml.pluto7073.pdapi.specialty.SpecialtyDrinkBase;
import ml.pluto7073.pdapi.specialty.SpecialtyDrinkBaseSerializer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public record GlassDrinkBase(Item glass) implements SpecialtyDrinkBase {

    @Override
    public ItemStack buildItemStack() {
        ItemStack stack = new ItemStack(BartendingItems.MIXED_DRINK);
        stack.getOrCreateTag()
                .putString("FromItem", BuiltInRegistries.ITEM.getKey(glass).toString());
        return stack;
    }

    @Override
    public boolean matches(ItemStack stack) {
        if (stack.getTag() == null) return false;
        if (!stack.getOrCreateTag().contains("FromItem")) return false;
        return BuiltInRegistries.ITEM.get(
                new ResourceLocation(stack.getOrCreateTag().getString("FromItem"))) == glass;
    }

    @Override
    public SpecialtyDrinkBaseSerializer serializer() {
        return null;
    }
}
